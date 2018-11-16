package br.org.aacc.doacao.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import br.org.aacc.doacao.WebViewActivity;
import br.org.aacc.doacao.R;


public class CacccFragment extends _SuperFragment {


    private ImageView imageViewPagSeguro;
    private ImageView imageViewPayPal;
    private TextView lblNomeCentro,lblTelefoneCentro,lblEmailCentro,lblEnderecoCentro,lblCepCentro;
    private TextView lblHistorico;
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


    private StringBuilder _sbCentro;
    private StringBuilder _sbConteudo;
    private StringBuilder _sbContaBancaria;
    private String url;

    private NestedScrollView  _nestedScrollViewNoticias;

    public CacccFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_caccc, container, false);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        this.OnInit(bundle);

    }


    private void OnInit(@Nullable Bundle bundle) {

        (getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            bundleArguments = this.getArguments();

            this._nestedScrollViewNoticias = getView().findViewById(R.id.nestedScrollViewNoticias);

            this._nestedScrollViewNoticias.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDonationPosition();
                }
            });

            if (bundleArguments != null)
                cacccGenericParcelable = bundleArguments.getParcelable(ConstantHelper.objCaccc);
            else
            {
                cacccUtilApplication= (UtilApplication<String, GenericParcelable<Caccc>>) getActivity().getApplicationContext();
                cacccGenericParcelable =  cacccUtilApplication.getElementElementDictionary(ConstantHelper.objCaccc);

            }

            this.handleFile = new HandleFile(getContext(), ConstantHelper.fileListarConteudoContasPorCaccc);

            try {

                this.progressBar = getView().findViewById(R.id.progress_bar);

                if (cacccGenericParcelable != null) {

                    idCentro = cacccGenericParcelable.getValue().getId();
                    eMailCentro = cacccGenericParcelable.getValue().getEmail();
                    url = String.format("%s%d", ConstantHelper.urlWebApiConteudoContasPorCaccc, idCentro);

                    if (bundleArguments != null && bundleArguments.getParcelable(ConstantHelper.objCaccc) != null) {
                        cacccGenericParcelable = bundleArguments.getParcelable(ConstantHelper.objCaccc);
                        if (cacccGenericParcelable.getValue() != null && cacccGenericParcelable.getValue().getConteudos() != null)
                            FillForm(cacccGenericParcelable.getValue());
                        else
                            new DownloadTask().execute(url);

                    } else
                        new DownloadTask().execute(url);

                }
            } catch (Exception e) {
                TrackHelper.WriteError(this, "onActivityCreated", e.getMessage());
            }
    }


   private void onDonationPosition()
    {
        try {

            _nestedScrollViewNoticias.post(new Runnable() {

                @Override
                public void run() {

                    _nestedScrollViewNoticias.fullScroll(View.FOCUS_DOWN);

                }
            });
        }
        catch (Exception e)
        {
            TrackHelper.WriteError(this, "onDonation", e.getMessage());

        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(caccc.TAG, new GenericParcelable<>(caccc));
    }


    private void FillForm(Caccc caccc) {
        try {

            _caccc = caccc;

            lblNomeCentro= getView().findViewById(R.id.lblNomeCentro);
            lblTelefoneCentro= getView().findViewById(R.id.lblTelefoneCentro);
            lblEmailCentro= getView().findViewById(R.id.lblEmailCentro);
            lblEnderecoCentro= getView().findViewById(R.id.lblEnderecoCentro);


            lblHistorico = getView().findViewById(R.id.lblHistorico);
            lblDadosBancarios = getView().findViewById(R.id.lblDadosBancarios);
            imageViewPagSeguro = getView().findViewById(R.id.imgPagSeguro);
            imageViewPayPal = getView().findViewById(R.id.imgPayPay);
            cardViewIntegracao = getView().findViewById(R.id.cardIntegracao);


            if (caccc != null) {

                _sbCentro = new StringBuilder();
                _sbCentro.append(String.format("<font size='14' color='gray' face='verdana'>Nome :</font> %s", caccc.getName()));
                 lblNomeCentro.setText(HtmlHelper.fromHtml(_sbCentro.toString()));

                _sbCentro = new StringBuilder();
                _sbCentro.append(String.format("<font size='14' color='gray' face='verdana'>Telefone :</font> %s ", caccc.getTelefone()));
                 lblTelefoneCentro.setText(HtmlHelper.fromHtml(_sbCentro.toString()));
                 Linkify.addLinks(lblTelefoneCentro,Linkify.PHONE_NUMBERS);

                _sbCentro = new StringBuilder();
                _sbCentro.append(String.format("<font size='14' color='gray' face='verdana'>E-mail :</font>  %s ", caccc.getEmail()));
                 lblEmailCentro.setText(HtmlHelper.fromHtml(_sbCentro.toString()));
                Linkify.addLinks(lblEmailCentro,Linkify.EMAIL_ADDRESSES);

                _sbCentro = new StringBuilder();
                _sbCentro.append("<hr>");
                _sbCentro.append("<br/>");
                _sbCentro.append(String.format("<font size='14' color='#a9a9a9' face='verdana'>%s</font>", "Endereço"));
                _sbCentro.append("<br/><br/>");
                _sbCentro.append(String.format("<font size='14' color='gray' face='verdana'>Logradouro :</font> %s,%s", caccc.getEndereco().getLogradouro(), caccc.getEndereco().getNumero()));
                _sbCentro.append("<br/>");
                _sbCentro.append(String.format("<font size='14' color='gray' face='verdana'>Bairro :</font> %s", caccc.getEndereco().getBairro()));
                _sbCentro.append("<br/>");
                _sbCentro.append(String.format("<font size='14' color='gray' face='verdana'>Cidade :</font> %s", caccc.getEndereco().getCidade()));
                _sbCentro.append("<br/>");
                _sbCentro.append(String.format("<font size='14' color='gray' face='verdana'>Estado :</font> %s", caccc.getEndereco().getEstado()));
                _sbCentro.append("<br/>");
                _sbCentro.append(String.format("<font size='14' color='gray' face='verdana'>Cep :</font> %s", caccc.getEndereco().getCep()));
                _sbCentro.append("<br/>");
                 lblEnderecoCentro.setText(HtmlHelper.fromHtml(_sbCentro.toString()));

                _sbConteudo = new StringBuilder();

                for (Conteudo itemConteudo : caccc.getConteudos()) {
                    _sbConteudo.append(String.format("<font size='medium' color='gray'>%s</font>", itemConteudo.getTitulo()));
                    _sbConteudo.append("<br/><br/>");
                    _sbConteudo.append(String.format("%s", itemConteudo.getColuna()));
                    _sbConteudo.append("<br/><br/>");

                }

                lblHistorico.setText(HtmlHelper.fromHtml(_sbConteudo.toString()));
                Linkify.addLinks(lblHistorico, Linkify.ALL);


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
                        intent = new Intent(getActivity(), WebViewActivity.class);
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
                        intent = new Intent(getActivity(), WebViewActivity.class);
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

                parseResult(fileJson);
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
                Toast.makeText(getActivity(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(String result) throws JSONException {
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


        } catch (JSONException e) {
            TrackHelper.WriteError(this, "parseResult", e.getMessage());
        } catch (Exception e) {
            TrackHelper.WriteError(this, "parseResult", e.getMessage());
        }
    }


}
