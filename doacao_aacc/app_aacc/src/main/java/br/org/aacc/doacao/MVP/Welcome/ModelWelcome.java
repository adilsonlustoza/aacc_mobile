package br.org.aacc.doacao.MVP.Welcome;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import br.org.aacc.doacao.Api.GoogleApi;
/**
 * Created by Adilson on 24/06/2018.
 */

public class ModelWelcome  implements IWelcome.IModelWelcome {

    private PresenterWelcome _welcomePresenter;

    public  ModelWelcome(PresenterWelcome welcomePresenter)
    {
         _welcomePresenter = welcomePresenter;
    }

    @Override
    public void CallGoogleApi(GoogleSignInAccount googleSignInAccount){
        new GoogleApi().SetProfileGoogle(googleSignInAccount);
    }
}
