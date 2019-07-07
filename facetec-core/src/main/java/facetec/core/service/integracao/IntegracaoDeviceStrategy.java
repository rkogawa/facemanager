package facetec.core.service.integracao;

import facetec.core.domain.Device;
import facetec.core.domain.enumx.ModeloDevice;

/**
 * Created by rkogawa on 06/07/19.
 */
public interface IntegracaoDeviceStrategy {

    ModeloDevice getModelo();

    String getBaseUrl(Device d);

    String getPassword();
}
