package br.org.aacc.doacao.Services;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.Date;

import br.org.aacc.doacao.Helper.ConstantHelper;
import br.org.aacc.doacao.Helper.HttpHelper;
import br.org.aacc.doacao.Helper.PrefHelper;
import br.org.aacc.doacao.Utils.HandleFile;
import br.org.aacc.doacao.Utils.UtilityMethods;

/**
 * Created by Adilson on 17/02/2018.
 */
public class FileTaskService  extends Thread{

    private String _jsonString;
    private HandleFile handleFile;
    private Context _context;
    private String _url;
    private String _file;
    private long _dataLong;
    private long _dataFile;
    private String fileJson;
    private long _intervalo;
    private String _intervaloString;

    public FileTaskService(Context context,String file,String url)
    {
        _context=context;
        _url=url;
        _file=file;
    }


    @Override
    public void run() {

        try {

            _intervaloString= PrefHelper.getString(_context,ConstantHelper.pref_atualizar);

             if(TextUtils.isEmpty(_intervaloString))
                 _intervalo = 15;
             else
                 _intervalo = Integer.getInteger(_intervaloString,0);

             handleFile= new HandleFile(_context,_file);
             fileJson = handleFile.ReadFile();

            _dataLong =new Date().getTime();
            _dataFile = handleFile.LastChangeFile();

            if (!TextUtils.isEmpty(fileJson) || fileJson==null) {

                long difMinutes = UtilityMethods.DiferenceMinutes(_dataLong,_dataFile);

                if ( difMinutes > _intervalo)
                {
                    handleFile.ApagaArquivo();

                    _jsonString = HttpHelper.makeServiceCall(_url);

                    if (_jsonString != null && _jsonString.length() > 0)
                        handleFile.WriteFile(_jsonString);
                }
            }

        } catch (Exception e) {
            Log.d("", e.getLocalizedMessage());
        }

    }

}
