package br.org.aacc.doacao.Domain;


import java.util.Collection;

import br.org.aacc.doacao.Domain.ObjectValue.Endereco;
import br.org.aacc.doacao.Domain.ObjectValue.MasterDomain;
import br.org.aacc.doacao.Domain.ObjectValue.TipoDoacao;

/**
 * Created by Adilson on 26/03/2017.
 */

public class Caccc extends MasterDomain
{

    public static String TAG="Caccc";



    private String EmailPagSeguro;
    private String EmailPayPal;
    private Endereco Endereco;
    private Double Latitude;
    private Double Longitude;
    private String Email;
    private String Telefone;
    private String Celular;
    private TipoDoacao tipoDoacao;
    private boolean Autorizado;
    private String Responsavel;

    private Collection<Conteudo> Conteudos;
    private Collection<Bazar> Bazares;
    private Collection<Noticia> Noticias;
    private Collection<Campanha> Campanhas;
    private Collection<Doador> Doadores;
    private Collection<ContaBancaria> ContasBancarias;



    public Caccc()
    {
        super();
    }


    public Collection<Bazar> getBazars() {
        return Bazares;
    }

    public void setBazars(Collection<Bazar> bazares) {
        this.Bazares = bazares;
    }

    public Collection<Noticia> getNoticias() {return Noticias;  }

    public void setNoticias(Collection<Noticia> noticias) {
        Noticias = noticias;
    }

    public Collection<Campanha> getCampanhas() {
        return Campanhas;
    }

    public void setCampanhas(Collection<Campanha> campanhas) {
        Campanhas = campanhas;
    }

    public Endereco getEndereco() { return Endereco; }

    public void setEndereco(Endereco endereco) { Endereco = endereco;}

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Collection<Doador> getDoadores() {
        return Doadores;
    }

    public void setDoadores(Collection<Doador> doadores) {
        Doadores = doadores;
    }

    public Collection<ContaBancaria> getContasBancarias() {  return ContasBancarias;   }

    public void setContasBancarias(Collection<ContaBancaria> contasBancarias) {ContasBancarias = contasBancarias;  }

    public String getEmailPayPal() {
        return EmailPayPal;
    }

    public void setEmailPayPal(String emailPayPal) {
        EmailPayPal = emailPayPal;
    }

    public String getEmailPagSeguro() {
        return EmailPagSeguro;
    }

    public void setEmailPagSeguro(String emailPagSeguro) {
        EmailPagSeguro = emailPagSeguro;
    }

    public Collection<Conteudo> getConteudos() {
        return Conteudos;
    }

    public void setConteudos(Collection<Conteudo> conteudos) {
        Conteudos = conteudos;
    }

    public String getTelefone() {
        return Telefone;
    }

    public void setTelefone(String telefone) {
        Telefone = telefone;
    }

    public String getCelular() {
        return Celular;
    }

    public void setCelular(String celular) {
        Celular = celular;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public TipoDoacao getTipoDoacao() {
        return tipoDoacao;
    }

    public void setTipoDoacao(TipoDoacao tipoDoacao) {
        this.tipoDoacao = tipoDoacao;
    }

    public boolean isAutorizado() {
        return Autorizado;
    }

    public void setAutorizado(boolean autorizado) {
        Autorizado = autorizado;
    }

    public String getResponsavel() {
        return Responsavel;
    }

    public void setResponsavel(String responsavel) {
        Responsavel = responsavel;
    }
}
