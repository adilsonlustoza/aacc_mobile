package br.org.aacc.doacao.Utils;

import android.app.Application;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Adilson on 10/01/2018.
 */

public class UtilApplication<T> extends Application
{
   private Collection<T> _applicationList;

    @Override
    public void onCreate(){
        super.onCreate();

        if(_applicationList==null)
            _applicationList =new ArrayList<>();

    }


    @Override
    public void onLowMemory(){
        super.onLowMemory();
        _applicationList.clear();
    }

    @Override
    public void onTrimMemory(int level){
        super.onTrimMemory(level);
    }
    public Collection<T> getList() {
        return _applicationList;
    }

    public void setList(Collection<T> _applicationList) {
        this._applicationList = _applicationList;
    }
}
