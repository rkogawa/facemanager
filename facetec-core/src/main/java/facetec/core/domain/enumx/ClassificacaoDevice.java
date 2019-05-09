package facetec.core.domain.enumx;

/**
 * Created by rkogawa on 09/05/19.
 */
public enum ClassificacaoDevice {

    ENTRADA("Entrada"), SAIDA("Saída");

    private final String descricao;

    ClassificacaoDevice(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static ClassificacaoDevice getByDescricao(String classificacao) {
        for (ClassificacaoDevice classificacaoDevice : ClassificacaoDevice.values()) {
            if (classificacaoDevice.getDescricao().equals(classificacao)) {
                return classificacaoDevice;
            }
        }
        throw new RuntimeException(String.format("Não foi encontrado classificação para a descrição %s.", classificacao));
    }
}
