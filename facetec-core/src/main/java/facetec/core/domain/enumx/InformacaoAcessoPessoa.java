package facetec.core.domain.enumx;

/**
 * Created by rkogawa on 08/05/19.
 */
public enum InformacaoAcessoPessoa {

    PERMANENTE("Permanente"), VISITANTE("Visitante");

    private final String descricao;

    InformacaoAcessoPessoa(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }

    public static InformacaoAcessoPessoa byDescricao(String informacaoAcesso) {
        for (InformacaoAcessoPessoa info : InformacaoAcessoPessoa.values()) {
            if (info.getDescricao().equals(informacaoAcesso)) {
                return info;
            }
        }
        throw new RuntimeException(String.format("Não foi encontrado informação para a descrição %s.", informacaoAcesso));
    }
}
