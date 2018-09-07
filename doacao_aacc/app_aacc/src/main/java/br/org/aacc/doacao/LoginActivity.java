package br.org.aacc.doacao;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.org.aacc.doacao.Helper.ConstantHelper;
import br.org.aacc.doacao.MVP.Login.ILogin;
import br.org.aacc.doacao.MVP.Login.PresenterLogin;


public class LoginActivity extends _SuperActivity implements ILogin.IViewLogin {


    // UI references.
    //  @BindView(R.id.name)
    private EditText mNameView;
    private AutoCompleteTextView mEmailView;
    private Button mEmailSignInButton;
    private ProgressBar progressBar;
    private ILogin.IPresenterLogin _iPresenterLogin;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.ConfigureToolbar(ConstantHelper.ToolbarSubTitleLogin);
        if(_iPresenterLogin==null)
            _iPresenterLogin =new PresenterLogin(savedInstanceState);
        _iPresenterLogin.setView(this);

    }


    @Override
    public void onStart() {
        super.onStart();
        _iPresenterLogin.itHasBeenAuthenticated();
    }


    private void setViewEvents(){

        mNameView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                mNameView.setFocusableInTouchMode(true);
                mNameView.requestFocus();
            }
        });


        mEmailView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmailView.setFocusableInTouchMode(true);
                mEmailView.requestFocus();
            }
        });

        mNameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.name || id == EditorInfo.IME_NULL) {
                    tryLogin();
                    return true;
                }
                return false;
            }
        });

        //Botao
        mEmailSignInButton = findViewById(R.id.email_sign_in_button);

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                tryLogin();
            }
        });

    }

    @Override
    public void setLogin(String login) {
        mNameView.setText(login);
    }

    @Override
    public void setEmail(String email) {
        mEmailView.setText(email);
    }

    @Override
    public void setMessageLogin(){
        mNameView.setError(getString(R.string.error_invalid_name));
        mNameView.requestFocus();
    }

    @Override
    public void setMessageEmail(){
        mEmailView.setError(getString(R.string.error_invalid_email));
        mEmailView.requestFocus();
    }

    private void tryLogin()
    {
        _iPresenterLogin.ValidaLogin(mNameView.getText().toString(),mEmailView.getText().toString());
    }

    @Override
    public void InitView() {
        this.mNameView = findViewById(R.id.name);
        this.mEmailView = findViewById(R.id.email);
        this.mEmailSignInButton =  this.findViewById(R.id.email_sign_in_button);
        this.progressBar = findViewById(R.id.progress_bar);
        this.mNameView.setError(null);
        this.mEmailView.setError(null);
        this.setViewEvents();
    }

    @Override
    public void showProgressBar(int visibilidade){
        this.progressBar.setVisibility(visibilidade);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

