package br.org.aacc.doacao.Adapter;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import br.org.aacc.doacao.Api.CircleTransformPicasso;
import br.org.aacc.doacao.Domain.Caccc;
import br.org.aacc.doacao.Helper.HtmlHelper;
import br.org.aacc.doacao.Interfaces.OnInstituicaoItemClickListener;
import br.org.aacc.doacao.R;


/**
 * Created by Adilson on 26/03/2017.
 */

public class CacccAdapter extends RecyclerView.Adapter<CacccAdapter.InstituicaoViewHolder> {

    private Context _context;
    private Caccc _caccc;
    private CacccAdapter.InstituicaoViewHolder _instituicaoViewHolder;
    private List<Caccc> _instituicoes;
    private View _view;
    private Context context;
    private OnInstituicaoItemClickListener onItemClickListener;
    private SwipeRefreshLayout _swipeRefreshLayout;

    public CacccAdapter(Context context, List<Caccc> instituicoes) {
        this._instituicoes = instituicoes;
        this._context = context;
    }

    @Override
    public CacccAdapter.InstituicaoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        _view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_instituicao, null);
        _instituicaoViewHolder = new CacccAdapter.InstituicaoViewHolder(_view);
        return _instituicaoViewHolder;
    }

    @Override
    public void onBindViewHolder(final InstituicaoViewHolder holder, int position) {
        context = holder.imageView.getContext();
        _caccc = _instituicoes.get(position);

        if (!TextUtils.isEmpty(_caccc.getUrlImage()))
            Picasso.with(context).load(_caccc.getUrlImage()).placeholder(R.drawable.progress_animation).error(R.drawable.arquivo_nao_encontrado).fit().transform(new CircleTransformPicasso()).into(holder.imageView);

        holder.textView.setText(HtmlHelper.fromHtml(_caccc.getName()));
        holder.textViewData.setText(HtmlHelper.fromHtml(String.valueOf(_caccc.getEmail())));
        Linkify.addLinks(holder.textViewData,Linkify.EMAIL_ADDRESSES);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _caccc = _instituicoes.get(holder.getAdapterPosition());
                onItemClickListener.onItemClick(_caccc);
            }
        };

        holder.cardView.setOnClickListener(listener);

    }


    @Override
    public int getItemCount() {
        return (null != this._instituicoes ? this._instituicoes.size() : 0);
    }

    class InstituicaoViewHolder extends RecyclerView.ViewHolder {

        protected ImageView imageView;
        protected TextView textView;
        protected TextView textViewData;
        protected ImageView imageViewAction;
        protected CardView cardView;


        public InstituicaoViewHolder(View view) {
            super(view);

            this.imageView = view.findViewById(R.id.thumbnail);
            this.textView = view.findViewById(R.id.title);
            this.textViewData = view.findViewById(R.id.txtData);
            this.imageViewAction = view.findViewById(R.id.imgGoTo);
            this.cardView = view.findViewById(R.id.cardInstituicao);


        }
    }


    public void setOnItemClickListener(OnInstituicaoItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
