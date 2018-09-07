package br.org.aacc.doacao.Domain;

import br.org.aacc.doacao.Domain.ObjectValue.MasterDomain;

/**
 * Created by Adilson on 31/12/2017.
 */

public class ContaBancaria  extends MasterDomain{

    private String numeroBanco;
    private String nomeBanco;
    private String agencia;
    private String beneficiario;
    private String conta;
    private Caccc caccc;


    public String getNumeroBanco() {
        return numeroBanco;
    }

    public void setNumeroBanco(String numeroBanco) {
        this.numeroBanco = numeroBanco;
    }

    public String getNomeBanco() {
        return nomeBanco;
    }

    public void setNomeBanco(String nomeBanco) {
        this.nomeBanco = nomeBanco;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public Caccc getCaccc() {
        return caccc;
    }

    public void setCaccc(Caccc caccc) {
        this.caccc = caccc;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }
}
