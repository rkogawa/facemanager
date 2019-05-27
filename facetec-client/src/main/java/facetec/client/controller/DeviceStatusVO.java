package facetec.client.controller;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by rkogawa on 27/05/19.
 */
public class DeviceStatusVO {

    private final SimpleStringProperty ip = new SimpleStringProperty("");

    private final SimpleStringProperty nome = new SimpleStringProperty("");

    private final SimpleStringProperty status = new SimpleStringProperty("");

    private final String url;

    public DeviceStatusVO(String ip, String nome, String status, String url) {
        setIp(ip);
        setNome(nome);
        setStatus(status);
        this.url = url;
    }

    public String getIp() {
        return ip.get();
    }

    public void setIp(String ip) {
        this.ip.set(ip);
    }

    public String getNome() {
        return nome.get();
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getUrl() {
        return url;
    }
}
