package br.org.aacc.doacao.Helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import br.org.aacc.doacao.MainActivity;
import br.org.aacc.doacao.R;


/**
 * Created by Adilson on 15/07/2017.
 */

public class NotificationHelper {

    private Context _context;
    private NotificationManager _notificationManager;
    private Notification _notification;
    private PendingIntent _pendingIntent;
    private NotificationCompat.Builder _builder;
    private NotificationCompat.InboxStyle _style;
    private Uri _uri;
    private String _prefRingstone;
    private Ringtone _ringtone;
    static String _notificationID="001";

    public NotificationHelper(Context context) {
        _context = context;
    }

    public void ShowNotification(String tituto, String descricao, String[] conteudo) {

        this._notificationManager = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
        this._pendingIntent = PendingIntent.getActivity(_context, 0, new Intent(_context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);


        _builder = new NotificationCompat.Builder(_context, _notificationID);
        _builder.setDefaults(Notification.DEFAULT_ALL);
        _builder.setWhen(System.currentTimeMillis());
        _builder.setTicker(descricao);
        _builder.setContentTitle(tituto);
        _builder.setContentText(descricao);
        _builder.setAutoCancel(true);
        _builder.setSmallIcon(R.drawable.information_16);
        _builder.setLargeIcon(BitmapFactory.decodeResource(_context.getResources(), R.mipmap.ic_launcher));
        _builder.setContentIntent(this._pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            _builder.setFullScreenIntent(this._pendingIntent, false);

        _style = new NotificationCompat.InboxStyle();

        for (int i = 0; i < conteudo.length; i++)
            _style.addLine(conteudo[i]);

        _builder.setStyle(_style);

        _notification = _builder.build();
        _notification.vibrate = new long[]{150, 300, 150, 600};
        _notification.flags = Notification.FLAG_AUTO_CANCEL;
        _notificationManager.notify(Integer.parseInt(_notificationID), _notification);

        try {

            _prefRingstone = PrefHelper.getString(_context, PrefHelper.PreferenciaRingstone);
            _uri = Uri.parse(_prefRingstone);
            _ringtone = RingtoneManager.getRingtone(_context, _uri);
            _ringtone.play();

        } catch (Exception e) {

        }


    }


}
