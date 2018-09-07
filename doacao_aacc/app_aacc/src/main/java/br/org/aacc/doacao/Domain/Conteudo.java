package br.org.aacc.doacao.Domain;

import java.util.Date;

import br.org.aacc.doacao.Domain.ObjectValue.MasterDomain;

/**
 * Created by Adilson on 19/01/2018.
 */

public class Conteudo extends MasterDomain {


     private String Titulo ;
     private String Subtitulo;
     private String Coluna ;
     private String Url;
     private Date DataCadastro ;
     private Caccc Caccc ;


    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getSubtitulo() {
        return Subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        Subtitulo = subtitulo;
    }

    public String getColuna() {
        return Coluna;
    }

    public void setColuna(String coluna) {
        Coluna = coluna;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public Date getDataCadastro() {
        return DataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        DataCadastro = dataCadastro;
    }

    public  Caccc getCaccc() {
        return Caccc;
    }

    public void setCaccc(Caccc caccc) {
        Caccc = caccc;
    }
}
