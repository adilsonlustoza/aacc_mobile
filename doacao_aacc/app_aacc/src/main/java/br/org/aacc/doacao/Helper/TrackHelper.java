package br.org.aacc.doacao.Helper;

import android.util.Log;

/**
 * Created by ubuntu on 12/1/16.
 */
public class TrackHelper {

    private  static String errorTrack="Ocorreu um erro no objeto %s no metodo %s  - Mensagem do Compilador : %s";
    private  static String infoTrack="Informativo objeto %s no metodo %s  - Mensagem do Programador : %s";


    public  static void WriteError(Object object,String method,String error)
    {
        Log.e("Error App Solidario",String.format(errorTrack,new Object[]{ object.toString(),method,error}));
    }
    public  static void WriteInfo(Object object,String method,String info)
    {
        Log.i("Info App Solidario : ",String.format(infoTrack,new Object[]{ object.toString(),method,info}));
    }
}
