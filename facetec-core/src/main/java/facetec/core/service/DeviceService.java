package facetec.core.service;

import facetec.core.dao.DeviceDAO;
import facetec.core.domain.Device;
import facetec.core.domain.enumx.ClassificacaoDevice;
import facetec.core.security.domain.FaceTecUser;
import facetec.core.security.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return findDevices(securityService.getCurrentUser());
    }

    public List<DeviceVO> findDevices(String usuario) {
        return dao.findBy(usuario).stream().map(d -> {
            DeviceVO deviceVO = new DeviceVO();
            deviceVO.setIp(d.getIp());
            deviceVO.setNome(d.getNome());
            deviceVO.setClassificacao(d.getClassificacao().getDescricao());
            return deviceVO;
        }).collect(Collectors.toList());
    }
}
