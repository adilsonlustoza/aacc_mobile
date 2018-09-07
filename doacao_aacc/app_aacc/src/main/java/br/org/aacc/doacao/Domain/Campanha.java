package br.org.aacc.doacao.Domain;

import java.util.Date;
import java.util.List;

import br.org.aacc.doacao.Domain.ObjectValue.MasterDomain;

/**
 * Created by Adilson on 26/03/2017.
 */

public class Campanha extends MasterDomain
{
   public static String TAG="Campanha";
   private Caccc caccc;
   private String linkWeb;
   private List<Paciente> pacientes;
   private int tipoInformacao;
   private boolean ativa;
   private Date dataInicial;
   private Date dataFinal;



    public Campanha() {
        super();
    }

    public Campanha(int id, String name, String content, String thumbnail) {

        this.setId(id);
        this.setName(name);
        this.setUrlImage(thumbnail);
    }

    public Caccc getCaccc() {
        return caccc;
    }

    public void setCaccc(Caccc caccc) {this.caccc = caccc; }

    public List<Paciente> getPacientes() { return pacientes; }

    public void setPacientes(List<Paciente> pacientes) { this.pacientes = pacientes; }

    public int getTipoInformacao() {
        return tipoInformacao;
    }

    public void setTipoInformacao(int tipoInformacao) {
        this.tipoInformacao = tipoInformacao;
    }

    public String getLinkWeb() {
        return linkWeb;
    }

    public void setLinkWeb(String linkWeb) {
        this.linkWeb = linkWeb;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }
}
