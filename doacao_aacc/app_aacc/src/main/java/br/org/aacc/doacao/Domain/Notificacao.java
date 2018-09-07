package br.org.aacc.doacao.Domain;

import java.util.Date;

import br.org.aacc.doacao.Domain.ObjectValue.MasterDomain;

/**
 * Created by Adilson on 04/12/2017.
 */

public class Notificacao extends MasterDomain {

    public static String TAG = "Notificacao";
    private Caccc caccc;
    private int Ativa;
    private Date DataInicial;
    private Date DataFinal;
    private String Titulo;
    private String Descricao;
    private String Conteudo;
    private String Guid;



    public Notificacao() {
        super();
    }


    public Caccc getCaccc() {
        return caccc;
    }

    public void setCaccc(Caccc caccc) {
        this.caccc = caccc;
    }

    public int getAtiva() {
        return Ativa;
    }

    public void setAtiva(int ativa) {
        Ativa = ativa;
    }

    public Date getDataInicial() {
        return DataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        DataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return DataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        DataFinal = dataFinal;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public String getGuid() {
        return Guid;
    }

    public void setGuid(String guid) {
        Guid = guid;
    }

    public String getConteudo() {
        return Conteudo;
    }

    public void setConteudo(String conteudo) {
        Conteudo = conteudo;
    }
}


