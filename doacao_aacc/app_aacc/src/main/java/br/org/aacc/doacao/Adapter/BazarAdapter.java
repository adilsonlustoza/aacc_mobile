package br.org.aacc.doacao.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.Collections;
import java.util.List;
import br.org.aacc.doacao.Domain.Bazar;
import br.org.aacc.doacao.Helper.TrackHelper;
import br.org.aacc.doacao.Helper.HtmlHelper;
import br.org.aacc.doacao.Interfaces.OnDoacaoItemClickListener;
import br.org.aacc.doacao.R;

/**
 * Created by Adilson on 26/03/2017.
 */

public class BazarAdapter extends RecyclerView.Adapter<BazarAdapter.DoacaoViewHolder>
{

    private Context _context;
    private Bazar _bazar;
    private DoacaoViewHolder _doacaoViewHolder;
    private List<Bazar> _doacoes = Collections.emptyList();
    private View _view;
    private Context context;

    private OnDoacaoItemClickListener onItemClickListener;

    public BazarAdapter(Context context, List<Bazar> doacoes) {
        this._doacoes = doacoes;
        this._context = context;
    }

    @Override
    public DoacaoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        _view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_bazar, null);
        _view.setLayoutParams(new RecyclerView.LayoutParams(parent.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));
        _doacaoViewHolder = new DoacaoViewHolder(_view);
        return _doacaoViewHolder;
    }

    @Override
    public void onBindViewHolder(final DoacaoViewHolder holder, int position) {
        try {

             context = holder.imageView.getContext();
            _bazar = _doacoes.get(position);

            if (!TextUtils.isEmpty(_bazar.getUrlImage()))
                Picasso.with(context).load(_bazar.getUrlImage()).placeholder(R.drawable.progress_animation).error(R.drawable.arquivo_nao_encontrado).fit().into(holder.imageView);

            holder.textViewTitle.setText(HtmlHelper.fromHtml(_bazar.getName()));
            holder.textBairro.setText(HtmlHelper.fromHtml(_bazar.getEndereco().getBairro()));
            holder.textViewDescription.setText(HtmlHelper.fromHtml(_bazar.getInformacao()));
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(_doacoes.get(holder.getAdapterPosition()));
                }
            };

            holder.cardViewBazar.setOnClickListener(listener);

        } catch (Exception e) {
            TrackHelper.WriteError(this, "onBindViewHolder", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return (null != this._doacoes ? this._doacoes.size() : 0);
    }

    class DoacaoViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textViewTitle;
        private TextView textBairro;
        private TextView textViewDescription;
        private ImageView imageViewAction;
        private CardView cardViewBazar;

        public DoacaoViewHolder(View view) {
            super(view);

            this.imageView =  view.findViewById(R.id.thumbnail);
            this.textViewTitle = view.findViewById(R.id.title);
            this.textBairro = view.findViewById(R.id.txtBairro);
            this.textViewDescription = view.findViewById(R.id.description);
            this.imageViewAction =  view.findViewById(R.id.imgPin);
            this.cardViewBazar=view.findViewById(R.id.cardViewBazar);

        }
    }


    public void setOnItemClickListener(OnDoacaoItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Bazar data) {
        _doacoes.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Bazar data) {
        int position = _doacoes.indexOf(data);
        _doacoes.remove(position);
        notifyItemRemoved(position);
    }
}
