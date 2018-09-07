package br.org.aacc.doacao.Services;

import android.content.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.org.aacc.doacao.Helper.ConstantHelper;
import br.org.aacc.doacao.Helper.TrackHelper;


/**
 * Created by Adilson on 30/04/2018.
 */

public class StoredHandleFileTask extends Thread {

    Map<String, String> _dictionary;
    FileTaskService _fileTaskService;
    int _count = 0;
    private Context _context;


    public StoredHandleFileTask(Context context) {
        _context = context;
    }


    @Override
    public void run() {

        try {

            TrackHelper.WriteInfo(this, " StoredHandleFileTask em : ", new Date().toString());


            _dictionary = new HashMap<>();
            _dictionary.put(ConstantHelper.fileListAllCaccc, ConstantHelper.urlWebApiListAllCaccc);
            _dictionary.put(ConstantHelper.fileListAllCacccBazar, ConstantHelper.urlWebApiListAllCacccBazar);
            _dictionary.put(ConstantHelper.fileListAllBazar, ConstantHelper.urlWebApiListAllBazar);
            _dictionary.put(ConstantHelper.fileListAllNoticia, ConstantHelper.urlWebApiListAllNoticia);
            _dictionary.put(ConstantHelper.fileListAllCampanhas, ConstantHelper.urlWebApiListAllCampanhas);
            _dictionary.put(ConstantHelper.fileListarConteudoContasPorCaccc, ConstantHelper.urlWebApiListarConteudoContasPorCaccc);

            for (String key : _dictionary.keySet()) {
                String file = key;
                String url = _dictionary.get(key);
                _fileTaskService = new FileTaskService(_context, file, url);
                _fileTaskService.start();
                _count++;
            }

        } catch (Exception e) {
            TrackHelper.WriteError(this,"StoredHandleFileTask.Run",e.getMessage());
        }

    }

}
