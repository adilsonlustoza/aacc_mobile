package br.org.aacc.doacao.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
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

import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.text.ParseException;
import java.util.List;

import br.org.aacc.doacao.Domain.Noticia;
import br.org.aacc.doacao.Helper.HtmlHelper;
import br.org.aacc.doacao.Interfaces.OnNoticiaItemClickListener;
import br.org.aacc.doacao.R;
import br.org.aacc.doacao.Utils.UtilityMethods;

/**
 * Created by Adilson on 26/03/2017.
 */

public class NoticiaAdapter extends RecyclerView.Adapter<NoticiaAdapter.NoticiaViewHolder> {
    private Context _context;
    private Noticia _noticia;
    private NoticiaViewHolder _noticiaViewHolder;
    private List<Noticia> _noticias;
    private View _view;
    private OnNoticiaItemClickListener onItemClickListener;
    private Context context;
    private int _color;

    public NoticiaAdapter(Context context, List<Noticia> noticias) {
        this._noticias = noticias;
        this._context = context;
    }

    @Override
    public NoticiaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        _view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_noticia, null);
        _view.setLayoutParams(new RecyclerView.LayoutParams(parent.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));
        _noticiaViewHolder = new NoticiaViewHolder(_view);
        return _noticiaViewHolder;
    }

    @Override
    public void onBindViewHolder(final NoticiaViewHolder holder, int position) {

        context = holder.imageView.getContext();
        _noticia = _noticias.get(position);
        //Render image using Picasso library
        if (!TextUtils.isEmpty(_noticia.getUrlImage()))
            Picasso.with(context).load(_noticia.getUrlImage()).placeholder(R.drawable.progress_animation).error(R.drawable.arquivo_nao_encontrado).fit().into(holder.imageView);
        //Setting text view title
        holder.textViewTitle.setText(HtmlHelper.fromHtml(_noticia.getName()));

        try {
            String date = UtilityMethods.ParseDateToString(_noticia.getDataPublicacao());
            if (date != null)
                holder.textViewData.setText(date);
            else
                holder.textViewData.setText(HtmlHelper.fromHtml("Data não divulgada"));
        } catch (ParseException e) {
               holder.textViewData.setText(HtmlHelper.fromHtml("Data não divulgada"));
        }

        holder.textViewDescription.setHtml(_noticia.getConteudo(),new HtmlResImageGetter(holder.textViewDescription));
        Linkify.addLinks(holder.textViewDescription, Linkify.ALL);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(_noticia);

            }
        };

        if (position % 2 == 0)
            _color = Color.WHITE;
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                _color = ContextCompat.getColor(_context, R.color.colorGreenWhite);
            else
                _color = _context.getResources().getColor(R.color.colorGreenWhite);

        }
        holder.imageView.setOnClickListener(listener);
        holder.textViewTitle.setOnClickListener(listener);
        holder.textViewDescription.setOnClickListener(listener);

        holder.textViewTitle.setBackgroundColor(_color);
        holder.textViewDescription.setBackgroundColor(_color);
        holder.cardView.setCardBackgroundColor(_color);
        holder.cardViewContent.setCardBackgroundColor(_color);
        holder.textViewData.setBackgroundColor(_color);

    }

    @Override
    public int getItemCount() {
        return (null != this._noticias ? this._noticias.size() : 0);
    }

    class NoticiaViewHolder extends RecyclerView.ViewHolder {


        private ImageView imageView;
        private TextView textViewTitle;
        private TextView textViewData;
        private HtmlTextView textViewDescription;
        private CardView cardView;
        private CardView cardViewContent;


        public NoticiaViewHolder(View view) {
            super(view);

            this.imageView = view.findViewById(R.id.thumbnail);
            this.textViewTitle = view.findViewById(R.id.txtTitle);
            this.textViewDescription = view.findViewById(R.id.txtContent);
            this.textViewData = view.findViewById(R.id.txtData);
            this.cardView = view.findViewById(R.id.cardViewNoticias);
            this.cardViewContent = view.findViewById(R.id.cardViewContent);


        }

    }

    public void setOnItemClickListener(OnNoticiaItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
