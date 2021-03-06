package br.org.aacc.doacao;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import br.org.aacc.doacao.Api.FacebookApi;
import br.org.aacc.doacao.Api.GoogleApi;
import br.org.aacc.doacao.Domain.Caccc;
import br.org.aacc.doacao.Domain.ContaBancaria;
import br.org.aacc.doacao.Domain.Conteudo;
import br.org.aacc.doacao.Domain.ObjectValue.Endereco;
import br.org.aacc.doacao.Fragments._SuperFragment;
import br.org.aacc.doacao.Helper.ConstantHelper;
import br.org.aacc.doacao.Helper.GenericParcelable;
import br.org.aacc.doacao.Helper.HttpHelper;
import br.org.aacc.doacao.Helper.TrackHelper;
import br.org.aacc.doacao.Helper.ImageHelper;
import br.org.aacc.doacao.Helper.PrefHelper;
import br.org.aacc.doacao.Services.NetWorkService;
import br.org.aacc.doacao.Utils.HandleFile;
import br.org.aacc.doacao.Utils.UtilApplication;
import br.org.aacc.doacao.Utils.UtilityMethods;


public class MainActivity extends _SuperActivity implements View.OnClickListener {

    private FragmentTransaction transaction;
    protected HandleFile handleFile;
    private GenericParcelable<Caccc> cacccGenericParcelable;
    private Caccc caccc;
    private Endereco endereco;
    private Conteudo conteudo;
    private Collection<Conteudo> conteudos;
    private ContaBancaria contaBancaria;
    private Collection<ContaBancaria> contaBancarias;
    private String url;

    protected boolean _webApiSucess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new _SuperFragment());
        transaction.addToBackStack(null);
        transaction.commit();

        this.ConfigCaccc(savedInstanceState);
    }


    private void ConfigCaccc(Bundle savedInstanceState){

        _cacccUtilApplication = (UtilApplication<String, GenericParcelable<Caccc>>) getApplicationContext();

        if(_cacccUtilApplication!=null) {

            cacccGenericParcelable = _cacccUtilApplication.getElementElementDictionary(ConstantHelper.objCaccc);

            this.handleFile = new HandleFile(_context, ConstantHelper.fileConteudoContasPorCaccc);

            this.url = ConstantHelper.urlWebApiConteudoContasPorCaccc.replace("{0}","2");

            if (cacccGenericParcelable != null) {

                if (savedInstanceState != null && savedInstanceState.getParcelable(caccc.TAG) != null)
                    cacccGenericParcelable = savedInstanceState.getParcelable(caccc.TAG);
            }
            else
                new DownloadTask().execute(url);
        }

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

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(caccc.TAG, new GenericParcelable<>(caccc));

    }


    //------------------------------------Fill  CacccObject -------------------------------------

    private void FillCaccc(Caccc caccc) {
        try {
            cacccGenericParcelable = new GenericParcelable<>(caccc);
           _cacccUtilApplication.setElementDictionary(ConstantHelper.objCaccc,cacccGenericParcelable);
        } catch (Exception e) {
            TrackHelper.WriteError(this, "setAdapter", e.getMessage());
        }
    }

    private void ParseJsonObjectCaccc(String result) {
        try {

            Object json = new JSONTokener(result).nextValue();

            if (json instanceof JSONObject)
                _jsonObject = new JSONObject(result);
            else {
                _jsonArrayResponse = new JSONArray(result);
                for (int i = 0; i < _jsonArrayResponse.length(); i++) {
                    _jsonObject = _jsonArrayResponse.optJSONObject(i);
                    if ((_jsonObject.optInt("CacccId") == idCentro))
                        break;
                }
            }

            if (_jsonObject.length() > 0) {

                caccc = new Caccc();
                caccc.setId(_jsonObject.optInt("CacccId"));
                caccc.setName(_jsonObject.optString("Nome"));
                caccc.setUrlImage(_jsonObject.optString("UrlImagem"));
                caccc.setEmail(_jsonObject.optString("Email"));
                caccc.setEmailPagSeguro(_jsonObject.optString("EmailPagSeguro"));
                caccc.setEmailPayPal(_jsonObject.optString("EmailPayPal"));
                caccc.setTelefone(_jsonObject.optString("Telefone"));
                caccc.setTipoDoacao(UtilityMethods.EnumTipoDoacao(_jsonObject.optString("TipoDoacao")));
                caccc.setAutorizado(UtilityMethods.IsBool(_jsonObject.optString("Autorizado")));
                caccc.setResponsavel(_jsonObject.optString("Responsavel"));
                caccc.setDoadores(null);

                endereco = new Endereco();
                endereco.setLogradouro(_jsonObject.optJSONObject("Endereco").getString("Logradouro"));
                endereco.setNumero(_jsonObject.optJSONObject("Endereco").getString("Numero"));
                endereco.setBairro(_jsonObject.optJSONObject("Endereco").getString("Bairro"));
                endereco.setCidade(_jsonObject.optJSONObject("Endereco").getString("Cidade"));
                endereco.setEstado(_jsonObject.optJSONObject("Endereco").getString("Estado"));
                endereco.setCep(_jsonObject.optJSONObject("Endereco").getString("Cep"));
                caccc.setEndereco(endereco);

                _jsonArrayResponse = _jsonObject.getJSONArray("Conteudos");


                if (_jsonArrayResponse.length() > 0) {
                    conteudos = new ArrayList<>();

                    for (int i = 0; i < _jsonArrayResponse.length(); i++) {

                        conteudo = new Conteudo();
                        conteudo.setId(_jsonArrayResponse.getJSONObject(i).getInt("ConteudoId"));
                        conteudo.setColuna(_jsonArrayResponse.getJSONObject(i).getString("Coluna"));
                        conteudo.setTitulo(_jsonArrayResponse.getJSONObject(i).getString("Titulo"));
                        conteudo.setSubtitulo(_jsonArrayResponse.getJSONObject(i).getString("Subtitulo"));
                        conteudo.setUrl(_jsonArrayResponse.getJSONObject(i).getString("Url"));
                        conteudo.setDataCadastro(new Date());
                        conteudos.add(conteudo);
                    }
                }
                caccc.setConteudos(conteudos);

                _jsonArrayResponse = _jsonObject.getJSONArray("ContasBancarias");

                if (_jsonArrayResponse.length() > 0) {
                    contaBancarias = new ArrayList<>();

                    for (int i = 0; i < _jsonArrayResponse.length(); i++) {
                        contaBancaria = new ContaBancaria();
                        contaBancaria.setNomeBanco(_jsonArrayResponse.getJSONObject(i).getString("NomeBanco"));
                        contaBancaria.setAgencia(_jsonArrayResponse.getJSONObject(i).getString("Agencia"));
                        contaBancaria.setConta(_jsonArrayResponse.getJSONObject(i).getString("Conta"));
                        contaBancaria.setBeneficiario(_jsonArrayResponse.getJSONObject(i).getString("Beneficiario"));
                        contaBancarias.add(contaBancaria);
                    }

                }

                caccc.setContasBancarias(contaBancarias);

            } else
                TrackHelper.WriteInfo(this, "parseResult no Centro", "Não encontrado informaçoes para o centro");


            this.FillCaccc(caccc);

        } catch (JSONException e) {
            TrackHelper.WriteError(this, "parseResult", e.getMessage());
        } catch (Exception e) {
            TrackHelper.WriteError(this, "parseResult", e.getMessage());
        }
    }


    //------------------------------------Common Task -------------------------------------

    public class DownloadTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            // progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {

            Integer result = 0;

            try {

                String fileJson = handleFile.ReadFile();

                if (TextUtils.isEmpty(fileJson) || fileJson.length() < 10) {

                    _jsonString = HttpHelper.makeServiceCall(params[0]);

                    if (_jsonString != null && _jsonString.length() > 0) {
                        handleFile.WriteFile(_jsonString);
                        _fileJson = _jsonString;
                    }
                }
                else
                    _fileJson=fileJson;

                ParseJsonObjectCaccc(_fileJson);
                result = 1; // Successful

            } catch (Exception e) {
                TrackHelper.WriteError(this, "DownloadTask doInBackground", e.getMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            // progressBar.setVisibility(View.GONE);
            if (result == 1) {
                TrackHelper.WriteInfo(this, "onPostExecute", "Executado na onPostExecute");
                _webApiSucess = true;
            } else {
                Toast.makeText(MainActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
