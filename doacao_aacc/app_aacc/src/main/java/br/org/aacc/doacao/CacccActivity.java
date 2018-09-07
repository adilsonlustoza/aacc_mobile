package br.org.aacc.doacao;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import br.org.aacc.doacao.Adapter.CacccAdapter;
import br.org.aacc.doacao.Domain.Caccc;
import br.org.aacc.doacao.Helper.ConstantHelper;
import br.org.aacc.doacao.Helper.GenericParcelable;
import br.org.aacc.doacao.Helper.HttpHelper;
import br.org.aacc.doacao.Helper.TrackHelper;
import br.org.aacc.doacao.Interfaces.OnInstituicaoItemClickListener;
import br.org.aacc.doacao.Utils.HandleFile;
import br.org.aacc.doacao.Utils.UtilityMethods;


public class CacccActivity extends _SuperActivity {

    private static final String TAG = "CacccActivity";
    private GenericParcelable<List<Caccc>> genericParcelable;
    private GenericParcelable<Caccc> cacccGenericParcelable;
    private List<Caccc> cacccList;
    private RecyclerView recyclerView;
    private CacccAdapter adapter;
    private ProgressBar progressBar;
    private Caccc caccc;
    private String _jsonString;
    private SwipeRefreshLayout _swipeRefreshLayout;
    private List<Caccc> cacccListKeep;
    private String url;
    private ImageView imageViewConsultaVazia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instituicao);
        this.ConfigureToolbar(ConstantHelper.ToolbarSubTitleCaccc);
        this.ConfigureReturnToolbar();
        this.Init(savedInstanceState);

    }

    private void Init(Bundle savedInstanceState) {
        this.url = ConstantHelper.urlWebApiListAllCaccc;
        this.recyclerView = this.findViewById(R.id.recycleInstituicao);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setNestedScrollingEnabled(true);
        this.recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        this.recyclerView.setAdapter(adapter);
        this.progressBar = findViewById(R.id.progress_bar);
        this.imageViewConsultaVazia = this.findViewById(R.id.imgConsultaVazia);
        this.SwipeRefreshLayout();
        this.handleFile = new HandleFile(_context, ConstantHelper.fileListAllCaccc);

        if (savedInstanceState != null && savedInstanceState.getParcelable(caccc.TAG) != null) {
            genericParcelable = savedInstanceState.getParcelable(caccc.TAG);
            if (genericParcelable.getValue() != null && genericParcelable.getValue().size() > 0)
                setAdapter(genericParcelable.getValue());
            else
                new DownloadTask().execute(url);
        } else
            new DownloadTask().execute(url);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        _searchView = new SearchView(this);
        _searchView.setOnQueryTextListener(new ResearchMenu());

        MenuItem itemSearch = menu.add(0, 0, 0, "SearchView");
        itemSearch.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemSearch.setActionView(_searchView);
        return true;
    }

    private class ResearchMenu implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String s) {
            this.ExecuteQuery(s);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            this.ExecuteQuery(s);
            return false;

        }

        private void ExecuteQuery(String s) {
            int count = cacccList.size();
            cacccListKeep = new ArrayList<>();

            while (count > 0) {
                Caccc caccc = cacccList.get(count - 1);
                if (caccc.getName().toLowerCase().contains(s.toLowerCase()))
                    cacccListKeep.add(caccc);
                count--;
            }

            if (TextUtils.isEmpty(s)) {
                setAdapter(cacccList);
                recyclerView.setVisibility(View.VISIBLE);
                imageViewConsultaVazia.setVisibility(View.GONE);
            } else {
                setAdapter(cacccListKeep);

                if (cacccListKeep.size() <= 0) {
                    recyclerView.setVisibility(View.GONE);
                    imageViewConsultaVazia.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    imageViewConsultaVazia.setVisibility(View.GONE);
                }

            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(caccc.TAG, new GenericParcelable<>(cacccList));

    }

    private void setAdapter(List<Caccc> cacccList) {
        try {
            adapter = new CacccAdapter(CacccActivity.this, cacccList);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnInstituicaoItemClickListener() {

                @Override
                public void onItemClick(Caccc item) {

                    intent = new Intent(_context, TabsCacccActivity.class);
                    cacccGenericParcelable = new GenericParcelable<>(item);
                    bundle = new Bundle();
                    bundle.putParcelable(ConstantHelper.objCaccc, cacccGenericParcelable);
                    intent.putExtra(ConstantHelper.objBundle, bundle);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(_activity).toBundle());
                    else
                        startActivity(intent);

                }
            });
        } catch (Exception e) {
            TrackHelper.WriteError(this, "setAdapter", e.getMessage());
        }
    }

    private void parseResult(String result) throws JSONException {
        try {
            _jsonArrayResponse = new JSONArray(result);
            cacccList = new ArrayList<>();
            cacccListKeep = new ArrayList<>();

            for (int i = 0; i < _jsonArrayResponse.length(); i++) {
                _jsonObject = _jsonArrayResponse.optJSONObject(i);
                caccc = new Caccc();
                caccc.setId(_jsonObject.optInt("CacccId"));
                caccc.setName(_jsonObject.optString("Nome"));
                caccc.setUrlImage(_jsonObject.optString("UrlImagem"));
                caccc.setEmail(_jsonObject.optString("Email"));
                caccc.setEmailPagSeguro(_jsonObject.optString("EmailPagSeguro"));
                caccc.setEmailPayPal(_jsonObject.optString("EmailPayPal"));
                caccc.setTipoDoacao(UtilityMethods.EnumTipoDoacao(_jsonObject.optString("TipoDoacao")));
                caccc.setAutorizado(UtilityMethods.IsBool(_jsonObject.optString("Autorizado")));
                caccc.setResponsavel(_jsonObject.optString("Responsavel"));
                caccc.setDoadores(null);
                cacccList.add(caccc);
            }


        } catch (JSONException e) {
            TrackHelper.WriteError(this, "parseResult", e.getMessage());
        } catch (Exception e) {
            TrackHelper.WriteError(this, "parseResult", e.getMessage());
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
                    _jsonString = HttpHelper.makeServiceCall(params[0]);

                    if (_jsonString != null && _jsonString.length() > 0) {
                        handleFile.WriteFile(_jsonString);
                        fileJson = _jsonString;
                    }
                }

                parseResult(fileJson);
                result = 1; // Successful

            } catch (Exception e) {
                TrackHelper.WriteError(this, "DownloadTask doInBackground", e.getMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            progressBar.setVisibility(View.GONE);
            if (result == 1) {
                if (cacccList!=null && cacccList.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    imageViewConsultaVazia.setVisibility(View.GONE);
                    setAdapter(cacccList);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    imageViewConsultaVazia.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(CacccActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
