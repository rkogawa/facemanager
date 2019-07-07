package facetec.core.service.integracao;

import facetec.core.domain.Device;
import facetec.core.domain.enumx.ModeloDevice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by rkogawa on 06/07/19.
 */
@Component
public class IntegracaoDeviceHSStrategy implements IntegracaoDeviceStrategy {

    @Value("${facetec.client.deviceBaseUrl.hs:http://%s:8090/}")
    private String deviceBaseUrl;

    @Value("${facetec.client.password.hs:123456}")
    private String devicePassword;

    @Override
    public ModeloDevice getModelo() {
        return ModeloDevice.HS;
    }

    @Override
    public String getBaseUrl(Device d) {
        return String.format(deviceBaseUrl, d.getIp());
    }

    @Override
    public String getPassword() {
        return devicePassword;
    }
}
