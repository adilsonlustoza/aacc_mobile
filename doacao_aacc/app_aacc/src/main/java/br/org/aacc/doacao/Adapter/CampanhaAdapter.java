package br.org.aacc.doacao.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.text.ParseException;
import java.util.List;

import br.org.aacc.doacao.Domain.Campanha;
import br.org.aacc.doacao.Helper.HtmlHelper;
import br.org.aacc.doacao.R;
import br.org.aacc.doacao.Utils.UtilityMethods;

/**
 * Created by Adilson on 26/03/2017.
 */

public class CampanhaAdapter extends RecyclerView.Adapter<CampanhaAdapter.CampanhaViewHolder> {
    private Context _context;
    private Campanha _campanha;
    private CampanhaViewHolder _campanhaViewHolder;
    private List<Campanha> _campanhas;
    private View _view;

    public CampanhaAdapter(Context context, List<Campanha> tabCampanhas) {
        this._campanhas = tabCampanhas;
        this._context = context;
    }

    @Override
    public CampanhaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        _view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_campanha, null);
        _view.setLayoutParams(new RecyclerView.LayoutParams(parent.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));
        _campanhaViewHolder = new CampanhaViewHolder(_view);
        return _campanhaViewHolder;
    }

    @Override
    public void onBindViewHolder(CampanhaViewHolder holder, int position) {
        _campanha = _campanhas.get(position);
        holder.textViewTitle.setText(HtmlHelper.fromHtml(String.format("%s ", _campanha.getName())));
        holder.textViewTipoCampanha.setText(HtmlHelper.fromHtml(String.format(" %s ", UtilityMethods.getTipoCampanha(_campanha.getTipoInformacao()))));
        holder.textViewContent.setHtml(_campanha.getDescription(),new HtmlResImageGetter( holder.textViewContent));
        String dataVigencia = "Vigência não informada.";
        try {
            if (_campanha.getDataInicial() != null && _campanha.getDataFinal() != null)
                dataVigencia = "Vigência de  : " + UtilityMethods.ParseDateToString(_campanha.getDataInicial()) + "  a " + UtilityMethods.ParseDateToString(_campanha.getDataFinal());
            else if (_campanha.getDataInicial()==null && _campanha.getDataFinal()!=null)
                dataVigencia = "Vigência até : " +  UtilityMethods.ParseDateToString(_campanha.getDataFinal());
            else if (_campanha.getDataInicial()!=null && _campanha.getDataFinal()==null)
                dataVigencia = "Vigência de  : " + UtilityMethods.ParseDateToString(_campanha.getDataInicial()) + " a ...";

             holder.textViewData.setText(HtmlHelper.fromHtml(dataVigencia));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Linkify.addLinks(holder.textViewContent, Linkify.WEB_URLS);

    }

    @Override
    public int getItemCount() {
        return (null != this._campanhas ? this._campanhas.size() : 0);
    }

    class CampanhaViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewTipoCampanha;
        private HtmlTextView textViewContent;
        private TextView textViewData;

        public CampanhaViewHolder(View view) {
            super(view);
            this.textViewTitle = view.findViewById(R.id.txtTitle);
            this.textViewTipoCampanha = view.findViewById(R.id.txtTipoCampanha);
            this.textViewContent = view.findViewById(R.id.txtContent);
            this.textViewData = view.findViewById(R.id.txtData);

        }

    }

}
