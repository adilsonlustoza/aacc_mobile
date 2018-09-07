package br.org.aacc.doacao.Domain.ObjectValue;
import android.net.Uri;

/**
 * Created by ubuntu on 12/13/16.
 */
public abstract class MasterUser extends MasterDomain
{

    public  String Id;
    public  String Name;
    public  String Email;
    public  String LastName;
    public  String NickName;
    public  Uri UrlImage;

    public String getCompositeName() {
        return Name + " " + LastName;
    }

    public String getNickName() {
        return NickName;
    }


}
