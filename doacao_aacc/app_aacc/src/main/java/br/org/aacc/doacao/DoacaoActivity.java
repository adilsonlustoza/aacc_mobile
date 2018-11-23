package br.org.aacc.doacao;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import br.org.aacc.doacao.Domain.Caccc;
import br.org.aacc.doacao.Domain.ContaBancaria;
import br.org.aacc.doacao.Domain.Conteudo;
import br.org.aacc.doacao.Domain.ObjectValue.Endereco;
import br.org.aacc.doacao.Domain.ObjectValue.TipoDoacao;
import br.org.aacc.doacao.Helper.ConstantHelper;
import br.org.aacc.doacao.Helper.GenericParcelable;
import br.org.aacc.doacao.Helper.HtmlHelper;
import br.org.aacc.doacao.Helper.HttpHelper;
import br.org.aacc.doacao.Helper.TrackHelper;
import br.org.aacc.doacao.Utils.HandleFile;
import br.org.aacc.doacao.Utils.UtilApplication;
import br.org.aacc.doacao.Utils.UtilityMethods;


public class DoacaoActivity extends _SuperActivity {

    private static final String TAG = "DoacaoActivity";

    private ImageView imageViewPagSeguro;
    private ImageView imageViewPayPal;
    private TextView lblDadosBancarios;
    private ProgressBar progressBar;
    private CardView cardViewIntegracao;

    private Caccc caccc;
    private Caccc _caccc;
    private Endereco endereco;
    private Conteudo conteudo;
    private Collection<Conteudo> conteudos;
    private ContaBancaria contaBancaria;
    private Collection<ContaBancaria> contaBancarias;

    private StringBuilder _sbContaBancaria;
    private String url;
    private Bundle bundleArguments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doacao);
        this.ConfigureToolbar(ConstantHelper.ToolbarSubTitleDoacao);
        this.ConfigureReturnToolbar();
        this.Init(savedInstanceState);
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        return true;
    }

    private void Init(Bundle savedInstanceState) {
        this.url = ConstantHelper.urlWebApiListAllCaccc;
        this.progressBar = findViewById(R.id.progress_bar);

        if (savedInstanceState != null)
            cacccGenericParcelable = savedInstanceState.getParcelable(ConstantHelper.objCaccc);
        else
        {
            cacccUtilApplication= (UtilApplication<String, GenericParcelable<Caccc>>) getApplicationContext();
            cacccGenericParcelable =  cacccUtilApplication.getElementElementDictionary(ConstantHelper.objCaccc);

        }

        this.handleFile = new HandleFile(_context, ConstantHelper.fileListarConteudoContasPorCaccc);

        try {

            this.progressBar = this.findViewById(R.id.progress_bar);

            if (cacccGenericParcelable != null) {

                idCentro = cacccGenericParcelable.getValue().getId();
                eMailCentro = cacccGenericParcelable.getValue().getEmail();
                this.url = ConstantHelper.urlWebApiConteudoContasPorCaccc.replace("{0}","2");

            }
            else
                new DownloadTask().execute(url);
        } catch (Exception e) {
            TrackHelper.WriteError(this, "onActivityCreated", e.getMessage());
        }
    }

    private void FillForm(Caccc caccc) {
        try {

            _caccc = caccc;
            lblDadosBancarios = this.findViewById(R.id.lblDadosBancarios);
            imageViewPagSeguro = this.findViewById(R.id.imgPagSeguro);
            imageViewPayPal = this.findViewById(R.id.imgPayPay);
            cardViewIntegracao = this.findViewById(R.id.cardIntegracao);


            if (caccc != null) {

                _sbContaBancaria = new StringBuilder();

                for (ContaBancaria itemConta : caccc.getContasBancarias()) {
                    _sbContaBancaria.append(String.format("<font size='14' color='gray' face='verdana'>Banco :</font> %s", itemConta.getNomeBanco()));
                    _sbContaBancaria.append("<br/>");
                    _sbContaBancaria.append(String.format("<font size='14' color='gray' face='verdana'>Agencia :</font> %s", itemConta.getAgencia()));
                    _sbContaBancaria.append("<br/>");
                    _sbContaBancaria.append(String.format("<font size='14' color='gray' face='verdana'>Conta :</font> %s", itemConta.getConta()));
                    _sbContaBancaria.append("<br/>");
                    _sbContaBancaria.append(String.format("<font size='14' color='gray' face='verdana'>Beneficiário :</font> %s", itemConta.getBeneficiario()));
                    _sbContaBancaria.append("<br/><br/>");
                }

                lblDadosBancarios.setText(HtmlHelper.fromHtml(_sbContaBancaria.toString()));

            }

            if (caccc.getTipoDoacao() == TipoDoacao.PagSeguro) {
                imageViewPagSeguro.setVisibility(View.VISIBLE);
                imageViewPayPal.setVisibility(View.GONE);
                cardViewIntegracao.setVisibility(View.VISIBLE);
            } else if (caccc.getTipoDoacao() == TipoDoacao.PayPal) {
                imageViewPayPal.setVisibility(View.VISIBLE);
                imageViewPagSeguro.setVisibility(View.GONE);
                cardViewIntegracao.setVisibility(View.VISIBLE);
            } else if (caccc.getTipoDoacao() == TipoDoacao.PagSeguro_PayPal) {
                imageViewPagSeguro.setVisibility(View.VISIBLE);
                imageViewPayPal.setVisibility(View.VISIBLE);
                cardViewIntegracao.setVisibility(View.VISIBLE);
            } else {
                cardViewIntegracao.setVisibility(View.GONE);
            }


            imageViewPagSeguro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        intent = new Intent(_context, WebViewActivity.class);
                        bundleArguments = new Bundle();
                        _caccc.setTipoDoacao(TipoDoacao.PagSeguro);
                        bundleArguments.putParcelable(ConstantHelper.objCaccc, new GenericParcelable<>(_caccc));
                        intent.putExtra(ConstantHelper.objBundle, bundleArguments);
                        startActivity(intent);
                    } catch (Exception e) {
                        TrackHelper.WriteError(this, "onActivityCreated", e.getMessage());
                    }


                }
            });


            imageViewPayPal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        intent = new Intent(_context, WebViewActivity.class);
                        bundleArguments = new Bundle();
                        _caccc.setTipoDoacao(TipoDoacao.PayPal);
                        bundleArguments.putParcelable(ConstantHelper.objCaccc, new GenericParcelable<>(_caccc));
                        intent.putExtra(ConstantHelper.objBundle, bundleArguments);
                        startActivity(intent);
                    } catch (Exception e) {
                        TrackHelper.WriteError(this, "onActivityCreated", e.getMessage());
                    }


                }
            });




        } catch (Exception e) {
            TrackHelper.WriteError(this, "onControlsValuesAndActions", e.getMessage());

        }

    }

    public class DownloadTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;

            try {

                String fileJson = handleFile.ReadFile();

                if (TextUtils.isEmpty(fileJson) || fileJson.length() < 10) {
                    String _jsonString = HttpHelper.makeServiceCall(params[0]);

                    if (_jsonString != null && _jsonString.length() > 0) {
                        handleFile.WriteFile(_jsonString);
                        fileJson = _jsonString;
                    }
                }

                ParseJsonObjectCaccc(fileJson);
                result = 1; //

            } catch (Exception e) {
                TrackHelper.WriteError(this, "DownloadTask doInBackground", e.getMessage());
            }
            return result; //"Failed to fetch data!";
        }


        @Override
        protected void onPostExecute(Integer result) {
            progressBar.setVisibility(View.GONE);
            if (result == 1) {
                FillForm(caccc);
            } else {
                Toast.makeText(_context, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
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


            this.FillForm(caccc);
        } catch (JSONException e) {
            TrackHelper.WriteError(this, "parseResult", e.getMessage());
        } catch (Exception e) {
            TrackHelper.WriteError(this, "parseResult", e.getMessage());
        }
    }
}
