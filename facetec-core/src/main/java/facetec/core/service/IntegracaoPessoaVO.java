package facetec.core.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rkogawa on 21/05/19.
 */
public class IntegracaoPessoaVO {

    private String id;

    private List<IntegracaoPessoaDeviceVO> devices = new ArrayList<>();

    private List<IntegracaoPessoaRequestVO> requests = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<IntegracaoPessoaDeviceVO> getDevices() {
        return devices;
    }

    public void setDevices(List<IntegracaoPessoaDeviceVO> devices) {
        this.devices = devices;
    }

    public List<IntegracaoPessoaRequestVO> getRequests() {
        return requests;
    }

    public void setRequests(List<IntegracaoPessoaRequestVO> requests) {
        this.requests = requests;
    }

}
