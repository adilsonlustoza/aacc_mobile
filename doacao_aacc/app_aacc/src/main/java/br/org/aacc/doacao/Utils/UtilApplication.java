package br.org.aacc.doacao.Utils;

import android.app.Application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by Adilson on 10/01/2018.
 */

public class UtilApplication<K,T> extends Application
{
   private Map<K,T> _dictionaryApplication;

    @Override
    public void onLowMemory(){
        super.onLowMemory();
        _dictionaryApplication.clear();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        _dictionaryApplication = new HashMap<>();
    }

    @Override
    public void onTrimMemory(int level){
        super.onTrimMemory(level);
    }


    public void setElementDicitionary(K key,T value)
    {
        if(_dictionaryApplication==null)
            _dictionaryApplication =new HashMap<>();

        if(!this._dictionaryApplication.containsKey(key))
           _dictionaryApplication.put(key,value);
    }

    public T getElementElementDictionary(K key){

        T value=null;

        try{

            if(this._dictionaryApplication.containsKey(key))
                value = this._dictionaryApplication.get(key);

        }
        catch (Exception ex)
        {
            value=null;
        }

      return value;

    }

}
