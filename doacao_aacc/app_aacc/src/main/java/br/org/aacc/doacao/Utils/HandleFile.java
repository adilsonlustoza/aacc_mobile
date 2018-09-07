package br.org.aacc.doacao.Utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import br.org.aacc.doacao.Helper.TrackHelper;

/**
 * Created by Adilson on 13/02/2018.
 */

public class HandleFile {

    private Context _context;
    private File _file;
    private FileOutputStream _fileOutputStream;
    private FileInputStream _fileInputStream;
    private BufferedReader _bufferedReader;
    private StringBuilder _stringBuilder;
    private String _fileName;


    public HandleFile(Context context, String fileName) {
        _context = context;
        _fileName = fileName;

    }


    public void WriteFile(String content) {

        try {

            _file = _context.getFileStreamPath(_fileName);

            if (!_file.exists())
                _file.createNewFile();

            _fileOutputStream = new FileOutputStream(_file);
            if (_fileOutputStream != null) {
                _fileOutputStream.write(content.getBytes());
                _fileOutputStream.flush();
                _fileOutputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public long LastChangeFile() {

        try {
            _file = _context.getFileStreamPath(_fileName);
            if (_file.exists())
             return  _file.lastModified();

        } catch (Exception e) {
            TrackHelper.WriteError(this, "LastChangeFile", e.getMessage());
        }
        return 0;
    }

    public String ReadFile() {

        try {
            _file = _context.getFileStreamPath(_fileName);
            if (_file.exists()) {
                _fileInputStream = new FileInputStream(_file);
                _bufferedReader = new BufferedReader(new InputStreamReader(_fileInputStream));
                _stringBuilder = new StringBuilder();
                String _line;
                while ((_line = _bufferedReader.readLine()) != null)
                    _stringBuilder.append(_line);
                return _stringBuilder.toString();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public boolean ApagaArquivo() throws IOException {

        _file = _context.getFileStreamPath(_fileName);
        if (_file.exists())
            return _file.delete();
        return false;
    }

}

