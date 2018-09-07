package br.org.aacc.doacao.Domain;

import br.org.aacc.doacao.Domain.ObjectValue.Endereco;
import br.org.aacc.doacao.Domain.ObjectValue.MasterDomain;

/**
 * Created by ubuntu on 4/10/17.
 */

public class Bazar extends MasterDomain {


    public static String TAG="Bazar";
    private Caccc caccc;
    private String Informacao;
    private Endereco endereco;



    public Bazar() {
        super();
    }

    public Bazar(int Id, String name, String description, String thumbnail) {
        super.setId(Id);
        super.setName(name);
        super.setDescription(description);
        super.setUrlImage(thumbnail);
    }


    public Caccc getCaccc() {
        return caccc;
    }

    public void setCaccc(Caccc caccc) {
        this.caccc = caccc;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }


    public String getInformacao() {
        return Informacao;
    }

    public void setInformacao(String informacao) {
        Informacao = informacao;
    }
}
