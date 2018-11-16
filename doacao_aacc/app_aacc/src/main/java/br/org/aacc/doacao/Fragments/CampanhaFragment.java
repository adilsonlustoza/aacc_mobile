package br.org.aacc.doacao.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import br.org.aacc.doacao.Adapter.CampanhaAdapter;
import br.org.aacc.doacao.Domain.Caccc;
import br.org.aacc.doacao.Domain.Campanha;
import br.org.aacc.doacao.Helper.ConstantHelper;
import br.org.aacc.doacao.Helper.GenericParcelable;
import br.org.aacc.doacao.Helper.HttpHelper;
import br.org.aacc.doacao.Helper.TrackHelper;
import br.org.aacc.doacao.R;
import br.org.aacc.doacao.Utils.HandleFile;
import br.org.aacc.doacao.Utils.UtilApplication;
import br.org.aacc.doacao.Utils.UtilityMethods;

public class CampanhaFragment extends _SuperFragment {

    private static final String TAG = "CampanhaFragment";

    private CampanhaAdapter adapter;
    private Campanha campanha;
    private List<Campanha> campanhaList;
    private SwipeRefreshLayout _swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageView imageViewConsultaVazia;
    private String url;
    private GenericParcelable<List<Campanha>> genericParcelable;


    public CampanhaFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_campanha, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.Init(bundle);
    }

    private void Init(Bundle bundle) {
        try {

            (getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            this.recyclerView = getView().findViewById(R.id.recycleCampanha);
            this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            this.recyclerView.setHasFixedSize(true);
            this.recyclerView.setItemAnimator(new DefaultItemAnimator());
            this.progressBar = getView().findViewById(R.id.progress_bar);
            this.handleFile = new HandleFile(getContext(), ConstantHelper.fileListAllCampanhas);
            this.imageViewConsultaVazia = getView().findViewById(R.id.imgConsultaVazia);
            this.SwipeRefreshLayout();

            bundleArguments = this.getArguments();

            if (bundleArguments != null)
                cacccGenericParcelable = bundleArguments.getParcelable(ConstantHelper.objCaccc);
            else
            {
                cacccUtilApplication= (UtilApplication<String, GenericParcelable<Caccc>>) getActivity().getApplicationContext();
                cacccGenericParcelable =  cacccUtilApplication.getElementElementDictionary(ConstantHelper.objCaccc);
            }


            if (cacccGenericParcelable != null) {

                idCentro = cacccGenericParcelable.getValue().getId();
                eMailCentro = cacccGenericParcelable.getValue().getEmail();
                url = String.format("%s", ConstantHelper.urlWebApiListAllCampanhas);

                if (bundle != null && bundle.getParcelable(Campanha.TAG) != null) {
                    genericParcelable = bundle.getParcelable(Campanha.TAG);
                    if (genericParcelable.getValue() != null && (genericParcelable.getValue()).size() > 0)
                        setAdapter(genericParcelable.getValue());
                    else
                        new DownloadTask().execute(url);
                } else
                    new DownloadTask().execute(url);

            }
        }
        catch (Exception e)
        {
            TrackHelper.WriteError(this,"OnInit",e.getMessage());
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void SwipeRefreshLayout() {
        try {
            this._swipeRefreshLayout = getView().findViewById(R.id.swipeToRefresh);
            this._swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            _swipeRefreshLayout.setRefreshing(false);
                        }

                    }, 1000);

                }
            });
        } catch (Exception e) {
            TrackHelper.WriteError(this,"SwipeRefreshLayout",e.getMessage());
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Campanha.TAG, new GenericParcelable<>(campanhaList));
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
                if (campanhaList.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    imageViewConsultaVazia.setVisibility(View.GONE);
                    setAdapter(campanhaList);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    imageViewConsultaVazia.setVisibility(View.VISIBLE);

                }
            } else {
                Toast.makeText(getActivity(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(String result) throws JSONException {
        try {

            _jsonArrayResponse = new JSONArray(result);
            campanhaList = new ArrayList<>();

            for (int i = 0; i < _jsonArrayResponse.length(); i++) {

                _jsonObject = _jsonArrayResponse.optJSONObject(i);

                if (_jsonObject.optInt("CacccId") == idCentro) {
                    campanha = new Campanha();
                    campanha.setId(_jsonObject.optInt("BoletimId"));
                    campanha.setName(_jsonObject.optString("Nome"));
                    campanha.setDescription(_jsonObject.optString("Descricao"));
                    campanha.setUrlImage(_jsonObject.optString("UrlImagem"));
                    campanha.setTipoInformacao(_jsonObject.optInt("tipoCampanha"));
                    campanha.setLinkWeb(_jsonObject.optString("LinkWeb"));
                    campanha.setAtiva(UtilityMethods.IsAtiva(_jsonObject.optString("Ativa")));
                    campanha.setDataInicial(UtilityMethods.ParseStringToDate(_jsonObject.optString("DataInicial")));
                    campanha.setDataFinal(UtilityMethods.ParseStringToDate(_jsonObject.optString("DataFinal")));
                    campanhaList.add(campanha);
                }
            }

        } catch (JSONException e) {
            TrackHelper.WriteError(this, "parseResult", e.getMessage());
        } catch (Exception e) {
            TrackHelper.WriteError(this, "parseResult", e.getMessage());
        }
    }

    private void setAdapter(List<Campanha> tabCampanhaList) {
        try {
            this.adapter = new CampanhaAdapter(getActivity(), tabCampanhaList);
            this.adapter.notifyDataSetChanged();
            this.recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            TrackHelper.WriteError(this, "setAdapter", e.getMessage());
        }
    }


}



