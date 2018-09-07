package br.org.aacc.doacao;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.facebook.AccessToken;

import br.org.aacc.doacao.Api.FacebookApi;
import br.org.aacc.doacao.Api.GoogleApi;
import br.org.aacc.doacao.Fragments._SuperFragment;
import br.org.aacc.doacao.Helper.ConstantHelper;
import br.org.aacc.doacao.Helper.TrackHelper;
import br.org.aacc.doacao.Helper.ImageHelper;
import br.org.aacc.doacao.Helper.PrefHelper;
import br.org.aacc.doacao.Services.NetWorkService;


public class MainActivity extends _SuperActivity implements View.OnClickListener {

    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new _SuperFragment());
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            this.CheckWhoIsLogado();
        } catch (Exception e) {
            TrackHelper.WriteError(this, "OnCreate", e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ConstantHelper.login) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String value = data.getStringExtra(PrefHelper.PreferenciaEmail);
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        try {

            if (NetWorkService.instance().isEnabledNetWork(_context) && ConstantHelper.isStartedAplication) {
                intent = new Intent(ConstantHelper.IntentStartAllServices);
                sendBroadcast(intent);
            }

        } catch (Exception e) {
            TrackHelper.WriteError(this, "onStart", e.getMessage());
        }
    }


    public void CheckWhoIsLogado() {

        try {

            if (!ConstantHelper.foiTrocadaImagemPerfil) {
                this.ConfigureFacebook();
                this.ConfigureGoogle();
            }
            //Login Facebook
            if (AccessToken.getCurrentAccessToken() != null && ConstantHelper.isLogadoRedesSociais) {
                new GoogleApi().SetProfileGoogle(null);

            }
            //Login google
            if (GoogleApi.GetProfileGoogle() != null && ConstantHelper.isLogadoRedesSociais) {
                new FacebookApi().setNullProfileFacebook();

            }
            //Login Normal
            if (ConstantHelper.isLogado || ConstantHelper.foiTrocadaImagemPerfil) {
                ConstantHelper.foiTrocadaImagemPerfil = false;

                intent = getIntent();

                bundle = intent.getExtras();
                header = GetNavegationViewHeader();

                name = header.findViewById(R.id.txtNomeNavHeader);
                email = header.findViewById(R.id.txtEmailNavHeader);

                String setNome  = bundle.getString(PrefHelper.PreferenciaNome);
                String setEmail = bundle.getString(PrefHelper.PreferenciaEmail);

                if(!TextUtils.isEmpty(setNome))
                   name.setText(setNome);

                if(!TextUtils.isEmpty(setEmail))
                    email.setText(setEmail);

                imageView = header.findViewById(R.id.imgLoginNavHeader);
                imageView.setOnClickListener(this);
                String image = PrefHelper.getString(_context, PrefHelper.PreferenciaFoto);


                if (image != null && !TextUtils.isEmpty(image)) {
                    bitmap = ImageHelper.DecodeBase64(image);
                    imageView.setImageBitmap(ImageHelper.getCircularBitmap(bitmap));
                } else
                    imageView.setImageResource(R.drawable.user_boy);


            }


            if (!ConstantHelper.isLogado  && !ConstantHelper.isLogadoRedesSociais && !ConstantHelper.foiTrocadaImagemPerfil)
                this.GoToApresentacao();

        } catch (Exception e) {
            TrackHelper.WriteError(this, "CheckWhoIsLogado", e.getMessage());
        }

    }


    @Override
    public void onClick(View v) {
        try {

            if (v.getId() == R.id.imgLoginNavHeader)
                intent = new Intent(this, PerfilActivity.class);
            startActivity(intent);
            finish();

        } catch (Exception e) {
            TrackHelper.WriteError(this, "onClick MainActivity", e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
