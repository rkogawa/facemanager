package facetec.core.service;

/**
 * Created by rkogawa on 09/05/19.
 */
public class DeviceVO {

    private String ip;

    private String nome;

    private String classificacao;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }
}
