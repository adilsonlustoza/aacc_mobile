package br.org.aacc.doacao.Api;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import br.org.aacc.doacao.Helper.TrackHelper;
import br.org.aacc.doacao.Domain.GoogleUser;


/**
 * Created by ubuntu on 1/14/17.
 */
public class GoogleApi {

    private static GoogleUser googleUser;

    public void SetProfileGoogle(GoogleSignInAccount googleSignInAccount)
    {
        try
        {
            if(googleSignInAccount!=null)
            {

                googleUser =new GoogleUser();
                googleUser.Id =googleSignInAccount.getId();
                googleUser.Name = googleSignInAccount.getDisplayName();
                googleUser.LastName = googleSignInAccount.getFamilyName();
                googleUser.Email=googleSignInAccount.getEmail();
                googleUser.UrlImage =googleSignInAccount.getPhotoUrl();
                googleUser.NickName = googleSignInAccount.getGivenName();


            }
            else
                googleUser=null;


        }
        catch (Exception e)
        {
            TrackHelper.WriteError(GoogleApi.class,"GoogleUser",e.getMessage());

        }


    }


    public static GoogleUser GetProfileGoogle(){

        if(googleUser!=null)
            return googleUser;
        return null;

    }

}
