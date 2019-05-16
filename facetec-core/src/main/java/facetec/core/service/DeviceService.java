package facetec.core.service;

import facetec.core.dao.DeviceDAO;
import facetec.core.domain.Device;
import facetec.core.domain.enumx.ClassificacaoDevice;
import facetec.core.security.domain.FaceTecUser;
import facetec.core.security.service.SecurityService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rkogawa on 09/05/19.
 */
@Service
public class DeviceService {

    @Autowired
    private DeviceDAO dao;

    @Autowired
    private SecurityService securityService;

    public void create(DevicesVO devices) {
        if (!this.securityService.checkAdminPassword(devices.getAdminPassword())) {
            throw new RuntimeException("Senha do admin inválida.");
        }

        FaceTecUser currentUser = securityService.getUser();

        dao.findBy(currentUser).forEach(d -> dao.delete(d));

        devices.getDevices().forEach(d -> {
            Device device = new Device();
            device.setIp(d.getIp());
            device.setNome(d.getNome());
            device.setPredio(currentUser);
            device.setClassificacao(ClassificacaoDevice.getByDescricao(d.getClassificacao()));
            dao.save(device);
        });
    }

    public List<DeviceVO> findDevices() {
        return dao.findBy(securityService.getUser()).stream().map(d -> {
            DeviceVO deviceVO = new DeviceVO();
            deviceVO.setIp(d.getIp());
            deviceVO.setNome(d.getNome());
            deviceVO.setClassificacao(d.getClassificacao().getDescricao());
            return deviceVO;
        }).collect(Collectors.toList());
    }

    //@Scheduled(cron = "0/5 * * * * *")
    public void executeTask() throws Exception{
        dao.findBy("facetec").stream().forEach(d -> {
            try {
                CloseableHttpClient client = HttpClients.createDefault();
                HttpPost httpPost = new HttpPost("http://" + d.getIp() + ":8088/person/find");

                String json = "{\"pass\": \"12345\", \"id\":\"-1\"}";
                StringEntity entity = new StringEntity(json);
                httpPost.setEntity(entity);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

                CloseableHttpResponse response = client.execute(httpPost);
                System.out.println(EntityUtils.toString(response.getEntity()));
                client.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
