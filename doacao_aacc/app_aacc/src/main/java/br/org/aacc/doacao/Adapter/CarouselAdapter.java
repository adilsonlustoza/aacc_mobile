package br.org.aacc.doacao.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import br.org.aacc.doacao.R;

/**
 * Created by Adilson on 29/04/2018.
 */

public class CarouselAdapter extends PagerAdapter {

    List<Integer> _listImagens;
    Context _context;
    LayoutInflater _inflater;
    View _view;
   ImageView _imageView;

    public CarouselAdapter(Context context, List<Integer> listImagens) {
        this._context = context;
        this._listImagens = listImagens;
        this._inflater = LayoutInflater.from(this._context);
    }



    @Override
    public int getCount() {
        return _listImagens.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        _view = _inflater.inflate(R.layout.carousel_item,container,false);
        _imageView = _view.findViewById(R.id.imgCarousel);
        _imageView.setImageResource(_listImagens.get(position));
        container.addView(_view);
        return _view;


    }
}
