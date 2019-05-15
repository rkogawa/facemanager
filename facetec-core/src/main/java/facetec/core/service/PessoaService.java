package facetec.core.service;

import facetec.core.dao.PessoaDAO;
import facetec.core.domain.Pessoa;
import facetec.core.domain.enumx.InformacaoAcessoPessoa;
import facetec.core.security.service.SecurityService;
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
    private PessoaDAO pessoaDAO;

    private byte[] resize(byte[] foto, int scaledWidth, int scaledHeight)
            throws IOException {
        // reads input image
        BufferedImage inputImage = ImageIO.read(new ByteArrayInputStream(foto));

        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        // writes to output file
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(outputImage, "jpeg", os);
        return os.toByteArray();
    }

    public PessoaResponseVO create(PessoaVO vo, MultipartFile foto) {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(vo.getNome());
        pessoa.setCpf(vo.getCpf());
        pessoa.setTelefone(vo.getTelefone());
        pessoa.setCelular(vo.getCelular());
        pessoa.setEmail(vo.getEmail());
        pessoa.setInformacaoAcesso(InformacaoAcessoPessoa.byDescricao(vo.getInformacaoAcesso()));
        pessoa.setDataHoraInicio(getDateTime(vo.getDataInicio(), vo.getHoraInicio()));
        pessoa.setDataHoraFim(getDateTime(vo.getDataFim(), vo.getHoraFim()));
        pessoa.setComentario(vo.getComentario());
        pessoa.setPredio(securityService.getUser());
        if (foto != null) {
            try {
                // XXX Verificar se vai ser necessario
                // byte[] fotoResize = this.resize(foto.getBytes(), 640, 480);
                pessoa.setFoto(Base64.getEncoder().encodeToString(foto.getBytes()));
            } catch (IOException e) {
                throw new RuntimeException(String.format("Erro ao converter foto para Base64. %s", e.getMessage()), e);
            }
        } else {
            pessoa.setFoto(vo.getFoto());
        }

        if (pessoa.getInformacaoAcesso().equals(VISITANTE) && (pessoa.getDataHoraInicio() == null || pessoa.getDataHoraFim() == null)) {
            throw new RuntimeException("Campos Data início e Data fim são obrigatórios para visitante.");
        }
        if (pessoa.getDataHoraInicio() != null && pessoa.getDataHoraFim() != null && !pessoa.getDataHoraFim()
                .isAfter(pessoa.getDataHoraInicio())) {
            throw new RuntimeException("A Data fim deve ser posterior a Data início.");
        }
        pessoaDAO.save(pessoa);

        PessoaResponseVO responseVO = new PessoaResponseVO();
        if (pessoa.getDataHoraFim() != null) {
            responseVO.setDataHoraFim(Timestamp.valueOf(pessoa.getDataHoraFim()).getTime());
        }
        responseVO.setId(pessoa.getId());
        responseVO.setFoto(pessoa.getFoto());
        return responseVO;
    }

    public PessoaVO findByCpf(String cpf) {
        try {
            Pessoa pessoa = pessoaDAO.findByCpf(cpf);
            PessoaVO vo = new PessoaVO();
            vo.setId(pessoa.getId());
            vo.setNome(pessoa.getNome());
            vo.setCpf(pessoa.getCpf());
            vo.setTelefone(pessoa.getTelefone());
            vo.setCelular(pessoa.getCelular());
            vo.setEmail(pessoa.getEmail());
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

    public void delete(Long id) {
        pessoaDAO.delete(id);
    }
}
