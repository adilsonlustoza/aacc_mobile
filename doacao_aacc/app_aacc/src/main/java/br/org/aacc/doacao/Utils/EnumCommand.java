package br.org.aacc.doacao.Utils;

/**
 * Created by ubuntu on 4/18/17.
 */

 public enum  EnumCommand {

    NetWorkEnabledMobile(1), NetWorkEnableWiFi(2), AllConfig(3),  Open(4),  CloseWindow(5),Localization(6), ExitApp(7), Yes(8),No(9);
    private final int _value ;
    EnumCommand(int valorOpcao){
        _value = valorOpcao;
    }
    public int getValue(){
        return _value;
    }
}
