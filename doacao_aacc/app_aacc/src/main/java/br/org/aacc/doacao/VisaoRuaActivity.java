package br.org.aacc.doacao;

import android.os.Bundle;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.maps.model.StreetViewPanoramaOrientation;

import br.org.aacc.doacao.Domain.Bazar;
import br.org.aacc.doacao.Helper.ConstantHelper;
import br.org.aacc.doacao.Helper.GenericParcelable;
import br.org.aacc.doacao.Helper.TrackHelper;

public class VisaoRuaActivity extends _SuperActivity implements OnStreetViewPanoramaReadyCallback {

    private static final String TAG = "VisaoRuaActivity";
    private StreetViewPanoramaFragment _streetViewPanoramaFragment;
    private Bundle _savedInstanceRecover = null;
    private Bazar bazar;
    private LatLng _latLng;
    private double _latitude;
    private double _longitude;
    private GenericParcelable<Bazar> bazarGenericParcelable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street_view);
        this.ConfigureToolbar(ConstantHelper.ToolbarSubTitleVisaoDeRua);
        this.ConfigureReturnToolbar();
        if (savedInstanceState != null)
            _savedInstanceRecover = savedInstanceState.getBundle(TAG);
        this.Init(_savedInstanceRecover);
    }

    private void Init(Bundle savedInstanceState) {
        try {

            bundle = getIntent().getExtras().getBundle(ConstantHelper.objBundle);

            if (bundle != null) {
                _latitude = ((GenericParcelable<Bazar>) bundle.getParcelable(ConstantHelper.objBazar)).getValue().getEndereco().getLatitude();
                _longitude = ((GenericParcelable<Bazar>) bundle.getParcelable(ConstantHelper.objBazar)).getValue().getEndereco().getLongitude();
                _latLng =new LatLng(_latitude,_longitude);

            } else if (savedInstanceState != null && savedInstanceState.getParcelable(bazar.TAG) != null) {
                bazarGenericParcelable = savedInstanceState.getParcelable(bazar.TAG);
                if (bazarGenericParcelable.getValue() != null)
                    _latLng = new LatLng(
                            bazarGenericParcelable.getValue().getEndereco().getLatitude(),
                            bazarGenericParcelable.getValue().getEndereco().getLongitude()
                    );

                _streetViewPanoramaFragment.onCreate(savedInstanceState);
            }

            _streetViewPanoramaFragment = (StreetViewPanoramaFragment) getFragmentManager().findFragmentById(R.id.map_street_view);
            _streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);

        } catch (Exception e) {
            TrackHelper.WriteError(this, "Init Street View Activity", e.getMessage());
        }
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        try {
            streetViewPanorama.setPosition(_latLng);
            streetViewPanorama.setPanningGesturesEnabled(true);
            streetViewPanorama.setUserNavigationEnabled(true);
            streetViewPanorama.setStreetNamesEnabled(true);

            streetViewPanorama.animateTo(
                    new StreetViewPanoramaCamera.Builder().
                            orientation( new StreetViewPanoramaOrientation(20, 20))
                            .zoom(streetViewPanorama.getPanoramaCamera().zoom)
                            .build(), 2000);
        }
        catch (Exception e)
        {
            TrackHelper.WriteError(this, "onStreetViewPanoramaReady", e.getMessage());
        }

    }

    @Override
    public void onResume() {
        _streetViewPanoramaFragment.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        _streetViewPanoramaFragment.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // _streetViewPanoramaFragment.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mStreetViewBundle = outState.getBundle(TAG);
        if (mStreetViewBundle == null) {
            mStreetViewBundle = new Bundle();
            outState.putBundle(TAG, mStreetViewBundle);
        }
        _streetViewPanoramaFragment.onSaveInstanceState(mStreetViewBundle);
    }
}
