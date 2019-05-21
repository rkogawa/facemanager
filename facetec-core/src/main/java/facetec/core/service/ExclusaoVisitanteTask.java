package facetec.core.service;

import facetec.core.dao.PessoaDAO;
import facetec.core.domain.Pessoa;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by rkogawa on 18/05/19.
 */
@Service
public class ExclusaoVisitanteTask {

    private static final Log LOG = LogFactory.getLog(ExclusaoVisitanteTask.class);

    @Autowired
    private PessoaDAO pessoaDAO;

    @Autowired
    private PessoaService service;

    @Value("${facetec.exclusao.visitante.dias:15}")
    private int diasParaExclusao;

    @Scheduled(cron = "${facetec.exclusao.visitante.cron:0 0 0 1/1 * *}")
    @Transactional
    public void executeTask() {
        LocalDateTime dataHoraExclusaoFormatada = LocalDateTime.now().minusDays(diasParaExclusao);
        List<Pessoa> visitantes = pessoaDAO.findBVisitantesBefore(dataHoraExclusaoFormatada);
        LOG.info(String.format("Serão excluído(s) %s visitante(s) cadastrado(s) antes de %s.", visitantes.size(), dataHoraExclusaoFormatada));
        visitantes.forEach(p -> service.delete(p.getId()));
    }

}
