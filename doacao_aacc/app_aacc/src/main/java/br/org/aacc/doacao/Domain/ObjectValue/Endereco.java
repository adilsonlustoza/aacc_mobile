package br.org.aacc.doacao.Domain.ObjectValue;
import java.io.Serializable;
/**
 * Created by ubuntu on 2/12/17.
 */
public class Endereco implements Serializable {

    private String Cep;
    private String Logradouro;
    private String Numero;
    private Double Longitude;
    private Double Latitude;
    private String Bairro;
    private String Cidade;
    private String Estado;
    private String Pais;

    public String getPaisSigla() {
        return PaisSigla;
    }

    public void setPaisSigla(String paisSigla) {
        PaisSigla = paisSigla;
    }

    private String PaisSigla;

    public Endereco(){}

    public Endereco(
                       String Logradouro,
                       String Bairro,
                       String Cidade,
                       String Estado,
                       String Cep
    )
    {
        this.setLogradouro(Logradouro);
        this.setBairro(Bairro);
        this.setCidade(Cidade);
        this.setEstado(Estado);
        this.setCep(Cep);
        this.setPais("Brasil");
        this.setPaisSigla("BR");
    }


    public String getCep() {
        return Cep;
    }

    public void setCep(String cep) {
        Cep = cep;
    }

    public String getLogradouro() {
        return Logradouro;
    }

    public void setLogradouro(String logradouro) {
        Logradouro = logradouro;
    }

    public String getBairro() {
        return Bairro;
    }

    public void setBairro(String bairro) {
        Bairro = bairro;
    }

    public String getCidade() {
        return Cidade;
    }

    public void setCidade(String cidade) {
        Cidade = cidade;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getPais() {
        return Pais;
    }

    public void setPais(String pais) {
        Pais = pais;
    }

    public String getNumero() {
        return Numero;
    }

    public void setNumero(String numero) { Numero = numero; }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    @Override
    public String toString()
    {
        return String.format("%s,%s,%s,%s", this.getLogradouro()  , this.getBairro(), this.getCidade(), this.getEstado());
    }
}
