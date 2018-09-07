package br.org.aacc.doacao.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import java.util.Calendar;
import java.util.Date;

import br.org.aacc.doacao.Helper.ConstantHelper;
import br.org.aacc.doacao.Helper.TrackHelper;


/**
 * Created by Adilson on 4/27/2017.
 */

public class NotificationBroadcast extends BroadcastReceiver
{
  private Context _context;
  private String _intentString ="NotificationService";
  private Intent _intent;
  private PendingIntent _pendingIntent;
  private Calendar _calendar;
  private AlarmManager _alarmManager;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(context!=null)
            ThrowAlarm(context);
    }

    private void  ThrowAlarm(Context context)
    {
        try
        {
            _context = context;
            _intent = new Intent(_context,NotificationService.class);
            _pendingIntent = PendingIntent.getService(_context, 0, _intent, 0);

            boolean isActive= (_pendingIntent = PendingIntent.getService(_context,0,_intent,PendingIntent.FLAG_NO_CREATE))==null;

             if(!isActive)
             {
                 _calendar = Calendar.getInstance();
                 _calendar.setTimeInMillis(System.currentTimeMillis());
                 _calendar.add(Calendar.MINUTE, 1);
                 _alarmManager = (AlarmManager) _context.getSystemService(Context.ALARM_SERVICE);
                 _alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, _calendar.getTimeInMillis(), ConstantHelper.OneMinute * 5, _pendingIntent);

             }

        }
        catch (Exception e)
        {
            TrackHelper.WriteInfo(this, "onPostExecute", new Date().toString());
            _alarmManager.cancel(_pendingIntent);
        }
        finally {

        }
    }


}
