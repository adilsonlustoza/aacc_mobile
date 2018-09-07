package br.org.aacc.doacao.Domain.ObjectValue;

/**
 * Created by Adilson on 31/12/2017.
 */

public enum TipoDoacao
{

      Nenhum(0),
      CupomFiscal(1),
      PagSeguro(2),
      PayPal(3),
      PagSeguro_PayPal(4),
      ContaBancaria(5);

    private final int valor;

    TipoDoacao(int valorOpcao){
        valor = valorOpcao;
    }
    public int getValor(){
        return valor;
    }
}
