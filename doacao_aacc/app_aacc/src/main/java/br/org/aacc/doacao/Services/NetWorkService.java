package br.org.aacc.doacao.Services;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;

import br.org.aacc.doacao.Helper.TrackHelper;

/**
 * Created by ubuntu on 4/19/17.
 */

public class NetWorkService extends FragmentActivity {

    private ConnectivityManager _connectivityManager;
    private NetworkInfo _networkInfo;



    private Context _context;
    private  static  NetWorkService _netWorkService;

    private NetWorkService(){}


    public static NetWorkService instance()
    {
        if(_netWorkService==null)
            _netWorkService =new NetWorkService();
        return _netWorkService;
    }


    public  boolean isEnabledNetWork(Context context) {
        try {

            _context = context;
             boolean ret=false;

            if (_context != null)
            {
                _connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);

                _networkInfo = _connectivityManager.getActiveNetworkInfo();

                if(_networkInfo!=null && _networkInfo.isAvailable())
                {
                   if(_networkInfo.getType()==ConnectivityManager.TYPE_WIFI)
                        ret=true;
                    else if(_networkInfo.getType()==ConnectivityManager.TYPE_MOBILE)
                        ret=true;
                    else
                        ret=false;

                }

                return ret;
            }
        } catch (Exception e) {
            TrackHelper.WriteError(this, "EnabledNetWork", e.getMessage());
        }

        return false;
    }

    public  boolean isEnabledLocation(Context context)
    {
        try
        {
            LocationManager lm = (LocationManager)  context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        catch (Exception e)
        {
            TrackHelper.WriteError(this, "isEnabledLocation", e.getMessage());
        }

        return false;
    }


}
