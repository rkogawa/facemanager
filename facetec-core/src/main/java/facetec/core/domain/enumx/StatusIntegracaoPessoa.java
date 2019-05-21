package facetec.core.domain.enumx;

/**
 * Created by rkogawa on 21/05/19.
 */
public enum StatusIntegracaoPessoa {

    PENDENTE_INCLUSAO, ENVIADO_INCLUSAO, INCLUSAO_OK, INCLUSAO_ERRO,
    PENDENTE_ALTERACAO, ENVIADO_ALTERACAO, ALTERACAO_OK, ALTERACAO_ERRO,
    PENDENTE_EXCLUSAO, ENVIADO_EXCLUSAO, EXCLUSAO_OK, EXCLUSAO_ERRO;

    public boolean isPendenteExclusao() {
        return this.equals(PENDENTE_EXCLUSAO);
    }

    public boolean isPendenteInclusao() {
        return this.equals(PENDENTE_INCLUSAO);
    }

    public boolean isEnviadoInclusao() {
        return this.equals(ENVIADO_INCLUSAO);
    }

    public boolean isExclusao() {
        return this.equals(EXCLUSAO_OK) || this.equals(EXCLUSAO_ERRO);
    }

    public boolean isEnviadoAlteracao() {
        return this.equals(ENVIADO_ALTERACAO);
    }
}
