package facetec.core.service.integracao;

import facetec.core.domain.Device;
import facetec.core.domain.enumx.ClassificacaoDevice;
import facetec.core.domain.enumx.ModeloDevice;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by rkogawa on 07/07/19.
 */
public class DeviceKeyVO {

    private final ClassificacaoDevice classificacao;

    private final ModeloDevice modelo;

    public DeviceKeyVO(Device device) {
        this.classificacao = device.getClassificacao();
        this.modelo = device.getModelo();
    }

    public ClassificacaoDevice getClassificacao() {
        return classificacao;
    }

    public ModeloDevice getModelo() {
        return modelo;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DeviceKeyVO)) {
            return false;
        }
        DeviceKeyVO other = (DeviceKeyVO) obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.getClassificacao(), other.getClassificacao());
        builder.append(this.getModelo(), other.getModelo());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.getClassificacao());
        builder.append(this.getModelo());
        return builder.toHashCode();
    }
}
