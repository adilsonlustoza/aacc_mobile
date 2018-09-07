package br.org.aacc.doacao.Services;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import br.org.aacc.doacao.Helper.ConstantHelper;
import br.org.aacc.doacao.Helper.PrefHelper;
import br.org.aacc.doacao.Helper.TrackHelper;
import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;
import me.tatarka.support.os.PersistableBundle;

/**
 * Created by Adilson on 4/27/2017.
 */

public class JobServiceUpdateFilesBroadcast extends BroadcastReceiver {
    private Context _context;
    private ComponentName _componentName;
    private PersistableBundle _persistableBundle;
    private JobInfo _jobInfo;
    private JobScheduler _jobScheduler;
    private String sMinutos;
    private boolean isDadosLocais = true;
    private int minutos = 5;
    private int JobId = 1;
    private StoredHandleFileTask _storedHandleFileTask;


    @Override
    public void onReceive(Context context, Intent intent) {
        _context = context;
        if (_context != null)
            this.onJobExecute();
    }


    private void onJobExecute() {
        try {

             ConstantHelper.isStartedAplication = false;

            _storedHandleFileTask = new StoredHandleFileTask(_context);
            _storedHandleFileTask.start();

            sMinutos = PrefHelper.getString(_context, "pref_atualizar");
            isDadosLocais = PrefHelper.getBoolean(_context, "pref_local");

            if (!TextUtils.isEmpty(sMinutos))
                minutos = Integer.parseInt(sMinutos);

            _componentName = new ComponentName(_context, JobServiceUpdateFiles.class);
            _persistableBundle = new PersistableBundle();
            _persistableBundle.putString("job", "job iniciado");

            _jobInfo = new JobInfo
                    .Builder(JobId, _componentName)
                    .setBackoffCriteria((ConstantHelper.OneMinute * 1), JobInfo.NETWORK_TYPE_ANY)
                    .setExtras(_persistableBundle)
                    .setPersisted(true)
                    .setPeriodic((ConstantHelper.OneMinute * minutos))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setRequiresCharging(false)
                    .setRequiresDeviceIdle(false)
                    .build();

            _jobScheduler = JobScheduler.getInstance(_context);
            _jobScheduler.schedule(_jobInfo);

            if (!isDadosLocais)
                _jobScheduler.cancel(JobId);


        } catch (Exception e) {
            TrackHelper.WriteError(this, "onJobExecute", e.getMessage());
        }
    }
}
