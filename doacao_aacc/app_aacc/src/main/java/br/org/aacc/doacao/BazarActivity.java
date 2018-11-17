package br.org.aacc.doacao;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;
import android.support.v7.widget.SearchView.SearchAutoComplete;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import br.org.aacc.doacao.Adapter.BazarAdapter;
import br.org.aacc.doacao.Domain.Bazar;
import br.org.aacc.doacao.Domain.ObjectValue.Endereco;
import br.org.aacc.doacao.Helper.ConstantHelper;
import br.org.aacc.doacao.Helper.GenericParcelable;
import br.org.aacc.doacao.Helper.HttpHelper;
import br.org.aacc.doacao.Helper.TrackHelper;
import br.org.aacc.doacao.Interfaces.OnDoacaoItemClickListener;
import br.org.aacc.doacao.Utils.HandleFile;


public class BazarActivity extends _SuperActivity {


    //region ***Variaveis e Propriedades***
    private static final String TAG = "EventoActivity";
    private List<Bazar> bazarList;
    private RecyclerView recyclerView;
    private BazarAdapter adapter;
    private ProgressBar progressBar;
    private Bazar bazar;
    private Endereco endereco;
    private GenericParcelable<List<Bazar>> genericParcelable;
    private GenericParcelable<Bazar> bazarGenericParcelable;
    private String _jsonString;
    private String url;
    private SwipeRefreshLayout _swipeRefreshLayout;
    private List<Bazar> bazarListKeep;
    private ImageView imageViewConsultaVazia;


    //endregion

    //region ***Metodos da Activity***

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bazar);
        this.ConfigureToolbar(ConstantHelper.ToolbarSubTitleBazares);
        this.ConfigureReturnToolbar();
        this.Init(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
         super.onCreateOptionsMenu(menu);
         return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(bazar.TAG, new GenericParcelable<>(bazarList));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //endregion


    //region ***Metodos***

    private void Init(Bundle savedInstanceState) {
        try {

            this.url = ConstantHelper.urlWebApiListAllBazaresPorCaccc.replace("{0}","2");
            this.recyclerView = this.findViewById(R.id.recycleViewBazar);
            this.recyclerView.setHasFixedSize(true);
            this.recyclerView.setNestedScrollingEnabled(true);
            this.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            this.recyclerView.setItemAnimator(new DefaultItemAnimator());
            this.recyclerView.setAdapter(adapter);
            this.progressBar = this.findViewById(R.id.progress_bar);
            this.imageViewConsultaVazia = this.findViewById(R.id.imgConsultaVazia);
            this.SwipeRefreshLayout();

            super.handleFile = new HandleFile(_context, ConstantHelper.fileListAllBazaresPorCaccc);

            if (savedInstanceState != null && savedInstanceState.getParcelable(bazar.TAG) != null) {
                genericParcelable = savedInstanceState.getParcelable(bazar.TAG);
                if (genericParcelable.getValue() != null && genericParcelable.getValue().size() > 0)
                    FillAdapter(genericParcelable.getValue());
                else
                    new DownloadTask().execute(url);
            } else
                new DownloadTask().execute(url);

        } catch (Exception e) {
            TrackHelper.WriteError(this, "Init", e.getMessage());
        }
    }



    private void SwipeRefreshLayout() {
        try {
            this._swipeRefreshLayout = findViewById(R.id.swipeToRefresh);
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
            TrackHelper.WriteError(this, "SwipeRefreshLayout", e.getMessage());
        }
    }

    private void ParseJsonToDomain(String result) throws JSONException {
        try {
            _jsonArrayResponse = new JSONArray(result);
            bazarList = new ArrayList<>();

            for (int i = 0; i < _jsonArrayResponse.length(); i++) {
                _jsonObject = _jsonArrayResponse.optJSONObject(i);
                bazar = new Bazar();
                bazar.setId(_jsonObject.optInt("BazarId"));
                bazar.setName(_jsonObject.optString("Nome"));
                bazar.setUrlImage(_jsonObject.optString("UrlImagem"));
                bazar.setInformacao(_jsonObject.optString("Informacao"));

                //---------------------------------------Endereco-------------------------------------
                endereco = new Endereco();
                endereco.setBairro(_jsonObject.optJSONObject("Endereco").optString("Bairro"));
                endereco.setLatitude(_jsonObject.optJSONObject("Endereco").optDouble("Latitude"));
                endereco.setLongitude(_jsonObject.optJSONObject("Endereco").optDouble("Longitude"));
                bazar.setEndereco(endereco);
                //-----------------------------------------------------------------------------------

                bazarList.add(bazar);
            }


        } catch (JSONException e) {
            TrackHelper.WriteError(this, "ParseJsonToDomain", e.getMessage());
        } catch (Exception e) {
            TrackHelper.WriteError(this, "ParseJsonToDomain", e.getMessage());
        }
    }

    private void FillAdapter(List<Bazar> bazarList) {
        try {

            adapter = new BazarAdapter(BazarActivity.this, bazarList);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnDoacaoItemClickListener() {

                @Override
                public void onItemClick(Bazar item) {

                    intent = new Intent(_context, MapsActivity.class);
                    bazarGenericParcelable = new GenericParcelable<>(item);
                    bundle = new Bundle();
                    bundle.putParcelable(ConstantHelper.objBazar, bazarGenericParcelable);
                    intent.putExtra(ConstantHelper.objBundle, bundle);
                    startActivity(intent);

                }
            });
        } catch (Exception e) {
            TrackHelper.WriteError(this, "setAdapter", e.getMessage());
        }
    }

    //endregion


    //region ***Classes Internas***

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
                    _jsonString = HttpHelper.makeServiceCall(params[0]);

                    if (_jsonString != null && _jsonString.length() > 0) {
                        handleFile.WriteFile(_jsonString);
                        fileJson = _jsonString;
                    }
                }

                ParseJsonToDomain(fileJson);
                result = 1; // Successful

            } catch (Exception e) {
                 TrackHelper.WriteError(this,"doInBackground Bazar",e.getMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            progressBar.setVisibility(View.GONE);

            if (result == 1) {

                if (bazarList!=null && bazarList.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    imageViewConsultaVazia.setVisibility(View.GONE);
                    FillAdapter(bazarList);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    imageViewConsultaVazia.setVisibility(View.VISIBLE);
                }

            } else {
                Toast.makeText(BazarActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }

    }
    //endregion


}
