package br.org.aacc.doacao;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import br.org.aacc.doacao.Domain.Caccc;
import br.org.aacc.doacao.Domain.ObjectValue.TipoDoacao;
import br.org.aacc.doacao.Helper.ConstantHelper;
import br.org.aacc.doacao.Helper.GenericParcelable;
import br.org.aacc.doacao.Helper.TrackHelper;


public class WebViewActivity extends _SuperActivity {


    private WebView webView;
    private WebSettings webSettings;
    private ProgressBar progressBar;
    private Bundle objBundle;

    private String emailPagSeguro;
    private String emailPayPal;
    private String centro;

    private TipoDoacao tipoDoacao;
    private String postData;
    byte[] _postBytes;

    private GenericParcelable<Caccc> cacccGenericParcelable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        this.ConfigureToolbar(ConstantHelper.ToolbarSubTitlePagSeguro);
        this.ConfigureReturnToolbar();
        this.Init();

    }

    private void Init() {
        emailPagSeguro = "";
        emailPayPal="";
        centro="";

        objBundle = getIntent().getExtras().getBundle(ConstantHelper.objBundle);
        if (objBundle != null) {

            cacccGenericParcelable = objBundle.getParcelable(ConstantHelper.objCaccc);

            if (cacccGenericParcelable != null) {
                emailPagSeguro = cacccGenericParcelable.getValue().getEmailPagSeguro();
                emailPayPal = cacccGenericParcelable.getValue().getEmailPayPal();
                centro = cacccGenericParcelable.getValue().getName();

               if(cacccGenericParcelable.getValue().getTipoDoacao()== TipoDoacao.PagSeguro)
                     tipoDoacao=TipoDoacao.PagSeguro ;
               else if(cacccGenericParcelable.getValue().getTipoDoacao()==TipoDoacao.PayPal)
                   tipoDoacao=TipoDoacao.PayPal ;
               else  if(cacccGenericParcelable.getValue().getTipoDoacao()==TipoDoacao.PagSeguro_PayPal)
                        tipoDoacao =TipoDoacao.PagSeguro_PayPal;

                try {

                    if(tipoDoacao==TipoDoacao.PagSeguro)
                        this.ConfigureToolbar("Integração PagSeguro");
                    else if(tipoDoacao==TipoDoacao.PayPal)
                        this.ConfigureToolbar("Integração PayPal");

                    webView = this.findViewById(R.id.wv_pagseguro);
                    progressBar = this.findViewById(R.id.progress_bar);

                    webSettings = webView.getSettings();
                    webSettings.setJavaScriptEnabled(true);

                    webSettings.setMinimumFontSize(2);
                    webSettings.setLoadWithOverviewMode(true);
                    webSettings.setUseWideViewPort(true);
                    webSettings.setBuiltInZoomControls(true);
                    webSettings.setDisplayZoomControls(false);


                    webSettings.setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
                    webSettings.setAppCacheEnabled(true);

                    webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
                    webView.setPadding(0, 0, 0, 0);
                    webView.clearHistory();


                    webView.setWebViewClient(new WebViewClient() {

                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon) {
                            super.onPageStarted(view, url, favicon);
                            progressBar.setVisibility(ProgressBar.VISIBLE);
                            webView.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onPageCommitVisible(WebView view, String url) {
                            super.onPageCommitVisible(view, url);
                            progressBar.setVisibility(ProgressBar.GONE);
                            webView.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onPageFinished(WebView webView, String url) {
                            super.onPageFinished(webView, url);
                            progressBar.setVisibility(ProgressBar.GONE);
                            webView.setVisibility(View.VISIBLE);
                            webView.loadUrl("javascript:resize(document.body.getBoundingClientRect().height)");

                        }

                    });

                    if (tipoDoacao == TipoDoacao.PagSeguro) {
                        postData = "currency='BRL'&receiverEmail='" + emailPagSeguro + "' ";
                        webView.postUrl(ConstantHelper._urlPagSeguro, postData.getBytes());
                    } else if (tipoDoacao == TipoDoacao.PayPal) {
                         postData = "cmd=_donations&business="+ emailPayPal + "&lc=BR&item_name="+ centro +"&currency_code=BRL&bn=PP-DonationsBF:btn_donateCC_LG.gif:NonHosted" ;
                         webView.postUrl(ConstantHelper._urlPayPal,postData.getBytes());
                    }


                } catch (Exception e) {
                    TrackHelper.WriteError(this, "WebViewActivity", e.getMessage());
                }

            }

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

