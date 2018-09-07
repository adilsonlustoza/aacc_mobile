package br.org.aacc.doacao.Services;

import android.os.AsyncTask;
import java.util.Date;

import br.org.aacc.doacao.Helper.TrackHelper;
import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;


/**
 * Created by Adilson on 08/01/2018.
 */


public class JobServiceUpdateFiles extends JobService {
    JobAsyncTask jobAsyncTask;
    StoredHandleFileTask _storedHandleFileTask;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        TrackHelper.WriteInfo(this, "onStartJob", jobParameters.getExtras().getString("job") + "  " + new Date().toString());
        jobAsyncTask = (JobAsyncTask) new JobAsyncTask(this).execute(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        TrackHelper.WriteInfo(this, "onStopJob", new Date().toString());
        if (jobAsyncTask != null)
            jobAsyncTask.cancel(true);
        return true;
    }


    class JobAsyncTask extends AsyncTask<JobParameters, Void, String> {

        JobServiceUpdateFiles _jobServiceUpdateFiles;

        public JobAsyncTask(JobServiceUpdateFiles jobServiceUpdateFiles) {
            _jobServiceUpdateFiles = jobServiceUpdateFiles;
        }


        @Override
        protected String doInBackground(JobParameters... jobParameters) {

            TrackHelper.WriteInfo(this, "Gravando os arquivos JobServiceUpdateFiles em : ", new Date().toString());
            _storedHandleFileTask = new StoredHandleFileTask(getBaseContext());
            _storedHandleFileTask.start();
            _jobServiceUpdateFiles.jobFinished(jobParameters[0], true);
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            TrackHelper.WriteInfo(this, "onPostExecute", new Date().toString());
        }
    }
}
