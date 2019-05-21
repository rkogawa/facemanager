package facetec.core.service.integracao;

import facetec.core.domain.Pessoa;

/**
 * Created by rkogawa on 21/05/19.
 */
public class PersonVO {

    private final String id;

    private final String name;

    private final String idcardNum;

    private String iccardNum = "";

    public PersonVO(Pessoa pessoa) {
        this.id = pessoa.getCpf();
        this.name = pessoa.getNome();
        this.idcardNum = pessoa.getId().toString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIdcardNum() {
        return idcardNum;
    }

    public String getIccardNum() {
        return iccardNum;
    }

    public void setIccardNum(String iccardNum) {
        this.iccardNum = iccardNum;
    }
}
