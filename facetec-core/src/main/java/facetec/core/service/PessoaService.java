package facetec.core.service;

import facetec.core.dao.GrupoDAO;
import facetec.core.dao.PessoaDAO;
import facetec.core.domain.IntegracaoPessoa;
import facetec.core.domain.Pessoa;
import facetec.core.domain.enumx.InformacaoAcessoPessoa;
import facetec.core.domain.enumx.StatusIntegracaoPessoa;
import facetec.core.security.service.SecurityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.NoResultException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;

import static facetec.core.domain.enumx.InformacaoAcessoPessoa.VISITANTE;
import static facetec.core.service.DateTimeUtils.getDateTime;

/**
 * Created by rkogawa on 08/05/19.
 */
@Service
public class PessoaService {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private IntegracaoPessoaService integracaoService;

    @Autowired
    private PessoaDAO pessoaDAO;

    @Autowired
    private GrupoDAO grupoDAO;

    public PessoaResponseVO create(PessoaVO vo) {
        Pessoa pessoa = new Pessoa();
        pessoa.setCpf(vo.getCpf());
        return salvarPessoa(vo, pessoa, StatusIntegracaoPessoa.PENDENTE_INCLUSAO);
    }

    public PessoaResponseVO update(PessoaVO vo) {
        Pessoa pessoa = pessoaDAO.findById(vo.getId());
        if (pessoa == null) {
            throw new RuntimeException("Não foi encontrado pessoa com id " + vo.getId());
        }
        return salvarPessoa(vo, pessoa, StatusIntegracaoPessoa.PENDENTE_ALTERACAO);
    }

    private PessoaResponseVO salvarPessoa(PessoaVO vo, Pessoa pessoa, StatusIntegracaoPessoa status) {
        pessoa.setNome(vo.getNome());
        pessoa.setTelefone(vo.getTelefone());
        pessoa.setCelular(vo.getCelular());
        pessoa.setEmail(vo.getEmail());
        if (StringUtils.isNotEmpty(vo.getGrupo())) {
            pessoa.setGrupo(grupoDAO.findBy(vo.getGrupo(), securityService.getUser()));
        } else {
            pessoa.setGrupo(null);
        }
        pessoa.setInformacaoAcesso(InformacaoAcessoPessoa.byDescricao(vo.getInformacaoAcesso()));
        pessoa.setDataHoraInicio(getDateTime(vo.getDataInicio(), vo.getHoraInicio()));
        pessoa.setDataHoraFim(getDateTime(vo.getDataFim(), vo.getHoraFim()));
        pessoa.setDataHoraRegistro(LocalDateTime.now());
        pessoa.setComentario(vo.getComentario());
        pessoa.setPredio(securityService.getUser());
        pessoa.setFoto(vo.getFoto());

        this.validarPessoa(pessoa);
        pessoaDAO.save(pessoa);
        IntegracaoPessoa integracao = integracaoService.criarIntegracaoPessoa(pessoa, status);

        PessoaResponseVO responseVO = new PessoaResponseVO();
        if (pessoa.getDataHoraFim() != null) {
            responseVO.setDataHoraFim(Timestamp.valueOf(pessoa.getDataHoraFim()).getTime());
        }
        responseVO.setId(pessoa.getId());
        responseVO.setIntegracaoId(integracao.getId());
        responseVO.setFoto(pessoa.getFoto());
        return responseVO;
    }

    private void validarPessoa(Pessoa pessoa) {
        if (pessoaDAO.existsBy(pessoa.getCpf(), pessoa.getPredio(), pessoa.getId())) {
            throw new RuntimeException("Já existe pessoa cadastrada para o CPF " + pessoa.getCpf());
        }

        if (pessoa.getInformacaoAcesso().equals(VISITANTE) && (pessoa.getDataHoraInicio() == null || pessoa.getDataHoraFim() == null)) {
            throw new RuntimeException("Campos Data início e Data fim são obrigatórios para visitante.");
        }
        if (pessoa.getDataHoraInicio() != null && pessoa.getDataHoraFim() != null && !pessoa.getDataHoraFim()
                .isAfter(pessoa.getDataHoraInicio())) {
            throw new RuntimeException("A Data fim deve ser posterior a Data início.");
        }
    }

    public PessoaResponseVO resize(MultipartFile foto, int scaledWidth, int scaledHeight)
            throws IOException {
        // reads input image
        BufferedImage inputImage = ImageIO.read(new ByteArrayInputStream(foto.getBytes()));

        if (inputImage == null) {
            throw new RuntimeException("O arquivo importado não é uma imagem. Favor importar apenas imagens no campo foto.");
        }

        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        // writes to output file
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(outputImage, "jpeg", os);
        PessoaResponseVO vo = new PessoaResponseVO();
        vo.setFoto(Base64.getEncoder().encodeToString(os.toByteArray()));
        return vo;
    }

    public PessoaVO findByCpf(String cpf) {
        try {
            Pessoa pessoa = pessoaDAO.findByCpf(cpf, securityService.getUser());
            PessoaVO vo = new PessoaVO();
            vo.setId(pessoa.getId());
            vo.setNome(pessoa.getNome());
            vo.setCpf(pessoa.getCpf());
            vo.setTelefone(pessoa.getTelefone());
            vo.setCelular(pessoa.getCelular());
            vo.setEmail(pessoa.getEmail());
            if (pessoa.getGrupo() != null) {
                vo.setGrupo(pessoa.getGrupo().getNome());
            }
            vo.setInformacaoAcesso(pessoa.getInformacaoAcesso().getDescricao());
            vo.setDataInicio(DateTimeUtils.convertISODate(pessoa.getDataHoraInicio()));
            vo.setHoraInicio(DateTimeUtils.convertTime(pessoa.getDataHoraInicio()));
            vo.setDataFim(DateTimeUtils.convertISODate(pessoa.getDataHoraFim()));
            vo.setHoraFim(DateTimeUtils.convertTime(pessoa.getDataHoraFim()));
            vo.setComentario(pessoa.getComentario());
            vo.setFoto(pessoa.getFoto());
            return vo;
        } catch (NoResultException e) {
            throw new RuntimeException("Não foi encontrado pessoa para o cpf " + cpf);
        }
    }

    public Long delete(Long id) {
        Pessoa pessoa = pessoaDAO.findById(id);
        return integracaoService.criarIntegracaoPessoa(pessoa, StatusIntegracaoPessoa.PENDENTE_EXCLUSAO).getId();
    }

}
