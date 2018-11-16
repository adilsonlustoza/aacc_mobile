package br.org.aacc.doacao;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import br.org.aacc.doacao.Adapter.NoticiaAdapter;
import br.org.aacc.doacao.Domain.Noticia;
import br.org.aacc.doacao.Helper.ConstantHelper;
import br.org.aacc.doacao.Helper.GenericParcelable;
import br.org.aacc.doacao.Helper.HttpHelper;
import br.org.aacc.doacao.Helper.TrackHelper;
import br.org.aacc.doacao.Interfaces.OnNoticiaItemClickListener;
import br.org.aacc.doacao.Utils.HandleFile;
import br.org.aacc.doacao.Utils.UtilityMethods;


public class NoticiasActivity extends _SuperActivity implements View.OnCreateContextMenuListener {

    private static final String TAG = "NoticiasActivity";
    private GenericParcelable<List<Noticia>> genericParcelable;
    private GenericParcelable<Noticia> noticiaGenericParcelable;
    private List<Noticia> noticiaList;
    private List<Noticia> noticiaListKeep;
    private RecyclerView recyclerView;
    private NoticiaAdapter adapter;
    private ProgressBar progressBar;
    private Noticia noticia;
    private String _jsonString;
    private SwipeRefreshLayout _swipeRefreshLayout;
    private ImageView imageViewConsultaVazia;
    private String url;
    private SearchView searchView;


    //region ***Ciclo de Vida***
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia);
        this.ConfigureToolbar(ConstantHelper.ToolbarSubTitleNoticias);
        this.ConfigureReturnToolbar();
        this.Init(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        try {
        getMenuInflater().inflate(R.menu.menu_search,menu );
        MenuItem itemSearch = menu.findItem(R.id.action_search );
        searchView = (SearchView) itemSearch.getActionView();
        searchView.setQueryHint("Pesquisar");

        searchView.setOnQueryTextListener( new ResearchMenu());

         if (noticiaGenericParcelable != null) {
            searchView.setQuery(noticiaGenericParcelable.getValue().getName(), false);
            searchView.clearFocus();
        }

          itemSearch.setActionView(searchView) ;


        } catch (Exception e) {
            TrackHelper.WriteError(this, "onInit", e.getMessage());
        }

        return true;

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(noticia.TAG, new GenericParcelable<>(noticiaList));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    //endregion


    //region ***Metodos***

    private void Init(Bundle savedInstanceState) {

        try {

            this.url = ConstantHelper.urlWebApiListAllNoticia;
            this.recyclerView = this.findViewById(R.id.recycleNoticia);
            this.recyclerView.setHasFixedSize(true);
            this.recyclerView.setItemAnimator(new DefaultItemAnimator());
            this.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            this.recyclerView.setAdapter(adapter);
            this.progressBar = this.findViewById(R.id.progress_bar);
            this.imageViewConsultaVazia = this.findViewById(R.id.imgConsultaVazia);
            this.SwipeRefreshLayout();

            handleFile = new HandleFile(_context, ConstantHelper.fileListAllNoticia);
            bundle = getIntent().getBundleExtra(ConstantHelper.objBundle);

            if (bundle != null)
                noticiaGenericParcelable = bundle.getParcelable(ConstantHelper.objNoticia);

            if (savedInstanceState != null && savedInstanceState.getParcelable(noticia.TAG) != null) {
                genericParcelable = savedInstanceState.getParcelable(noticia.TAG);
                if (genericParcelable.getValue() != null && genericParcelable.getValue().size() > 0)
                    FillAdapter(genericParcelable.getValue());
                else
                    new DownloadTask().execute(url);
            } else
                new DownloadTask().execute(url);

        } catch (Exception e) {
            TrackHelper.WriteError(this, "onInit", e.getMessage());
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

        }

    }

    private void ParseJson(String result) throws JSONException {
        try {

            _jsonArrayResponse = new JSONArray(result);
            noticiaList = new ArrayList<>();

            for (int i = 0; i < _jsonArrayResponse.length(); i++) {
                _jsonObject = _jsonArrayResponse.optJSONObject(i);
                noticia = new Noticia();
                noticia.setId(_jsonObject.optInt("NoticiaId"));
                noticia.setName(_jsonObject.optString("Titulo"));
                noticia.setUrlImage(_jsonObject.optString("UrlImagem"));
                noticia.setEmail(ConstantHelper.emailCacccTest);
                noticia.setDataPublicacao(UtilityMethods.ParseStringToDate(_jsonObject.optString("DataPublicacao")));
                noticia.setConteudo(_jsonObject.optString("Conteudo"));
                noticiaList.add(noticia);
            }

        } catch (JSONException e) {
            TrackHelper.WriteError(this, "ParseJson", e.getMessage());
        } catch (Exception e) {
            TrackHelper.WriteError(this, "ParseJson", e.getMessage());
        }
    }

    private void FillAdapter(List<Noticia> noticiaList) {
        try {
            adapter = new NoticiaAdapter(NoticiasActivity.this, noticiaList);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnNoticiaItemClickListener() {
                @Override
                public void onItemClick(Noticia item) {
                    try {
                        noticia = item;
                    } catch (Exception e) {
                        TrackHelper.WriteError(this, "onItemClick", e.getMessage());
                    }

                }
            });
        } catch (Exception e) {
            TrackHelper.WriteError(this, "FillAdapter", e.getMessage());
        }
    }
    //endregion


    //region ***Funcoes Especiais***

    private class ResearchMenu implements   SearchView.OnQueryTextListener {

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

            try {

                s = UtilityMethods.RemoveAccent(s.trim().toLowerCase());
                int count = noticiaList.size();
                noticiaListKeep = new ArrayList<>();

                if (!TextUtils.isEmpty(s)) {

                    while (count > 0) {
                        noticia = noticiaList.get(count - 1);
                        if (UtilityMethods.RemoveAccent(noticia.getName().toLowerCase().trim()).contains(s))
                            noticiaListKeep.add(noticia);
                        count--;
                    }
                }

                if (TextUtils.isEmpty(s) && noticiaList.size() > 0)
                    FillAdapter(noticiaList);
                else if (!TextUtils.isEmpty(s) && noticiaListKeep.size() > 0)
                    FillAdapter(noticiaListKeep);
                else
                    FillAdapter(null);

                if (adapter.getItemCount() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    imageViewConsultaVazia.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    imageViewConsultaVazia.setVisibility(View.VISIBLE);
                }


            } catch (Exception e) {
                TrackHelper.WriteInfo(this, "ExecuteQuery", e.getMessage());
            }


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

                ParseJson(fileJson);
                result = 1; // Successful

            } catch (Exception e) {
                TrackHelper.WriteInfo(this, "doInBackground", e.getMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {

            progressBar.setVisibility(View.GONE);

            try {

                if (result == 1) {
                    if (noticiaGenericParcelable != null)
                        new ResearchMenu().ExecuteQuery(noticiaGenericParcelable.getValue().getName());
                    else
                        new ResearchMenu().ExecuteQuery("");

                } else {
                    recyclerView.setVisibility(View.GONE);
                    imageViewConsultaVazia.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                TrackHelper.WriteInfo(this, "onPostExecute", e.getMessage());
            }

        }
    }

    //endregion

}



