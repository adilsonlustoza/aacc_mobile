package br.org.aacc.doacao.Domain;

import java.util.List;

import br.org.aacc.doacao.Domain.ObjectValue.MasterUser;

/**
 * Created by Adilson on 28/12/2017.
 */

public class Paciente extends MasterUser {

    public static String TAG="Paciente";
    private String Diagnosed;
    private String Peculiarities;
    private List<Campanha> campanhas;

    public String getDiagnosed() { return Diagnosed; }

    public void setDiagnosed(String diagnosed) {Diagnosed = diagnosed; }

    public String getPeculiarities() {return Peculiarities; }

    public void setPeculiarities(String peculiarities) { Peculiarities = peculiarities; }

    public List<Campanha> getCampanhas() { return campanhas; }

    public void setCampanhas(List<Campanha> campanhas) {  this.campanhas = campanhas; }
}
