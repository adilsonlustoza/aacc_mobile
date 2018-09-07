package br.org.aacc.doacao.Domain;

import java.util.Collection;

import br.org.aacc.doacao.Domain.ObjectValue.Endereco;
import br.org.aacc.doacao.Domain.ObjectValue.MasterUser;
import br.org.aacc.doacao.Domain.ObjectValue.TipoDoacao;

/**
 * Created by Adilson on 30/12/2017.
 */


public class Doador extends MasterUser {

    private Collection<Caccc> cacccs;
    private Endereco endereco;
    private TipoDoacao tipoDoacao;

    public Collection<Caccc> getCacccs() {
        return cacccs;
    }

    public void setCacccs(Collection<Caccc> cacccs) {
        this.cacccs = cacccs;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public TipoDoacao getTipoDoacao() {
        return tipoDoacao;
    }

    public void setTipoDoacao(TipoDoacao tipoDoacao) {
        this.tipoDoacao = tipoDoacao;
    }
}
