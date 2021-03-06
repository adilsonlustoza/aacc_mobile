package br.org.aacc.doacao.MVP.Login;

import br.org.aacc.doacao.MVP.IGlobalPresenter;

/**
 * Created by Adilson on 07/07/2018.
 */

public class ILogin {

    public interface  IPresenterLogin extends IGlobalPresenter{
        void setView(ILogin.IViewLogin iViewLogin);
        void ValidaLogin(String name, String Email);
        boolean isEmailValid(String email);
        boolean isNameValid(String name);
        void goToMainActivity();
        void itHasBeenAuthenticated();

    }

    public interface  IModelLogin{}

    public interface IViewLogin{
         void InitView();
         void showProgressBar(int visibilidade);
         void setMessageLogin();
         void setMessageEmail();
         void setLogin(String login);
         void setEmail(String email);

    }

}
