package br.org.aacc.doacao;

import android.os.Bundle;

import br.org.aacc.doacao.Helper.ConstantHelper;


public class AjudaActivity  extends _SuperActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuda);
        this.ConfigureToolbar(ConstantHelper.ToolbarSubTitleAjuda);
        this.ConfigureReturnToolbar();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}
