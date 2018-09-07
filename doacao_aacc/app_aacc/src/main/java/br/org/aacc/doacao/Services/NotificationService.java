package br.org.aacc.doacao.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import br.org.aacc.doacao.Domain.Notificacao;
import br.org.aacc.doacao.Helper.ConstantHelper;
import br.org.aacc.doacao.Helper.HttpHelper;
import br.org.aacc.doacao.Helper.NotificationHelper;
import br.org.aacc.doacao.Helper.PrefHelper;
import br.org.aacc.doacao.Helper.TrackHelper;
import br.org.aacc.doacao.Utils.UtilityMethods;

/**
 * Created by Adilson on 08/01/2018.
 */

public class NotificationService extends Service  {

    private JSONObject _jsonObject;
    private JSONArray _jsonArrayResponse;
    private List<String> _list = null;
    private String _lastNotification;
    private Notificacao notificacao;
    private List<Notificacao> notificacaoList;


    private Thread _thread;
    private boolean _receberNotificacao;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TrackHelper.WriteInfo(this, "onStartCommand", new Date().toString());
        return this.Execute();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    private int Execute() {


        try {

            _thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    _lastNotification = HttpHelper.makeServiceCall(ConstantHelper.urlWebApiListAllNotificacoes);
                    _receberNotificacao = PrefHelper.getBoolean(getBaseContext(), PrefHelper.PreferenciaRecebeNotificacao);
                    _list = new ArrayList<>();
                    boolean dateCheck = false;

                    TrackHelper.WriteInfo(this, "Verificando novas notificacoes as : ", new Date().toString());

                    if (!TextUtils.isEmpty(_lastNotification)) {
                        try {

                            JsonParseNotification(_lastNotification);

                            if (notificacaoList != null && notificacaoList.size() > 0) {

                                for (int i = 0; i < notificacaoList.size(); i++) {

                                    String titulo = notificacaoList.get(i).getTitulo();
                                    String descricao = notificacaoList.get(i).getDescricao();
                                    String content = notificacaoList.get(i).getConteudo();
                                    String guid = notificacaoList.get(i).getGuid();
                                    String[] contentArray = UtilityMethods.ParseStringToStringArray(content, 6);
                                    Date currentTime = Calendar.getInstance().getTime();
                                    Date dataInicial = notificacaoList.get(i).getDataInicial();
                                    Date dataFinal = notificacaoList.get(i).getDataFinal();

                                    if (currentTime.after(dataInicial) && currentTime.before(dataFinal))
                                        dateCheck = true;

                                    if (!CheckNotificationStored(guid) && dateCheck) {
                                        new NotificationHelper(getApplicationContext())
                                                .ShowNotification(
                                                        titulo,
                                                        descricao,
                                                        contentArray
                                                );


                                    }
                                    _list.add(guid);
                                }
                            }
                            //Limpa todas as notificacoes e somente manterá a que ainda estão ativas
                            StoredNotification();

                        } catch (JSONException e) {
                            TrackHelper.WriteError(this, "parseResult", e.getMessage());

                        } catch (Exception e) {
                            TrackHelper.WriteError(this, "parseResult", e.getMessage());
                        }

                    }

                }
            });

            _thread.start();

        } catch (Exception e) {
            TrackHelper.WriteError(this, "Error onStartCommand", e.getMessage());
        }

        return START_NOT_STICKY;
    }


    private void JsonParseNotification(String result) throws JSONException {
        try {

            _jsonArrayResponse = new JSONArray(result);
            notificacaoList = new ArrayList<>();

            for (int i = 0; i < _jsonArrayResponse.length(); i++) {
                _jsonObject = _jsonArrayResponse.optJSONObject(i);
                notificacao = new Notificacao();
                notificacao.setId(_jsonObject.optInt("NotificacaId"));
                notificacao.setTitulo(_jsonObject.optString("Titulo"));
                notificacao.setDescricao(_jsonObject.optString("Descricao"));
                notificacao.setConteudo(_jsonObject.optString("Conteudo"));
                notificacao.setAtiva(_jsonObject.optInt("Ativa"));
                notificacao.setDataFinal(UtilityMethods.ParseStringToDate(_jsonObject.optString("DataFinal")));
                notificacao.setDataInicial(UtilityMethods.ParseStringToDate(_jsonObject.optString("DataInicial")));
                notificacao.setGuid(_jsonObject.optString("IdentificadorUnico"));
                notificacaoList.add(notificacao);
            }

        } catch (JSONException e) {
            TrackHelper.WriteError(this, "JsonParseNotification", e.getMessage());
        } catch (Exception e) {
            TrackHelper.WriteError(this, "JsonParseNotification", e.getMessage());
        }
    }


    private boolean CheckNotificationStored(String guid) {
        boolean _findIt = false;
        try {

            String getGuid = PrefHelper.getString(getApplicationContext(), PrefHelper.PreferenciaIdentificadorUnico);

            if (getGuid != null && !TextUtils.isEmpty(getGuid)) {

                String[] _splitGuid = getGuid.split(",");

                if (_splitGuid != null && _splitGuid.length > 0) {
                    for (int i = 0; i < _splitGuid.length; i++)
                        if (_splitGuid[i].trim().equals(guid.trim())) {
                            _findIt = true;
                            break;
                        }
                }
            }

            return _findIt;

        } catch (Exception e) {
            TrackHelper.WriteError(this, "NotificationStore", e.getMessage());
        }
        return _findIt;
    }


    private void StoredNotification() {
        try {
            //Limpa todas as notificacoes e somente manterá a que ainda estão ativas
            PrefHelper.removeKey(getApplicationContext(), PrefHelper.PreferenciaIdentificadorUnico);
            String value = "";
            for (int j = 0; j < _list.size(); j++)
                value += _list.get(j) + ", ";
            PrefHelper.setString(getApplicationContext(), PrefHelper.PreferenciaIdentificadorUnico, value);
        } catch (Exception e) {
            TrackHelper.WriteError(this, "StoredNotification", e.getMessage());

        }
    }

}
