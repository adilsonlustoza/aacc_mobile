package br.org.aacc.doacao.Domain;

import java.util.Date;

import br.org.aacc.doacao.Domain.ObjectValue.MasterDomain;

/**
 * Created by Adilson on 04/12/2017.
 */

public class Noticia extends MasterDomain {

    public static String TAG="Noticia";
    private Caccc caccc;
    private Date DataPublicacao;
    private String Email;
    private String Conteudo;


    public Noticia()
    {
        super();
    }


    public Caccc getCaccc() {
        return caccc;
    }

    public void setCaccc(Caccc caccc) {
        this.caccc = caccc;
    }

    public Date getDataPublicacao() { return DataPublicacao;   }

    public void setDataPublicacao(Date dataPublicacao) {  this.DataPublicacao = dataPublicacao;   }

    public String getConteudo() {
        return Conteudo;
    }

    public void setConteudo(String conteudo) {
        Conteudo = conteudo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
