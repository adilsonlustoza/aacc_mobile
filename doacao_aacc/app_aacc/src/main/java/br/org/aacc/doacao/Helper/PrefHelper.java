package br.org.aacc.doacao.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by ubuntu on 12/26/16.
 */
public class PrefHelper {

    private static  SharedPreferences sharedPreferences;
    private static  SharedPreferences.Editor editor;

    public  static final String PreferenciaNome="pref_name";
    public  static final String PreferenciaEmail="pref_email";
    public  static final String PreferenciaEstado="pref_estado";
    public  static final String PreferenciaCidade="pref_cidade";
    public  static final String PreferenciaBairro="pref_bairro";
    public  static final String PreferenciaLogradouro="pref_logradouro";
    public  static final String PreferenciaCep="pref_cep";
    public  static final String PreferenciaPais="pref_pais";
    public  static final String PreferenciaLatitude="pref_latitude";
    public  static final String PreferenciaLongitude="pref_longitude";
    public  static final String PreferenciaFoto="pref_photo";
    public  static final String PreferenciaLogado="pref_logado";
    public  static final String PreferenciaImagePerfil="pref_image_perfil";

    public  static final String PreferenciaRecebeNotificacao="pref_notificacao";
    public  static final String PreferenciaIdentificadorUnico="pref_identificador_unico";
    public static  final String PreferenciaRingstone= "pref_ringstone";



    public static void setString(Context context,String chave,String value)
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putString(chave,value);
        editor.commit();
    }

    public static void removeKey(Context context,String chave)
    {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.remove(chave);
        editor.apply();
    }



    public static String getString(Context context,String chave)
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(sharedPreferences!=null)
            return  sharedPreferences.getString(chave,null);
        return null;
    }


    public static Boolean getBoolean(Context context,String chave)
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(sharedPreferences!=null)
          return sharedPreferences.getBoolean(chave,true);
        return false;

    }


}
