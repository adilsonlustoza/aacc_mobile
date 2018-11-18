package br.org.aacc.doacao;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import br.org.aacc.doacao.Domain.Bazar;
import br.org.aacc.doacao.Domain.Caccc;
import br.org.aacc.doacao.Domain.ObjectValue.Endereco;
import br.org.aacc.doacao.Helper.ConstantHelper;
import br.org.aacc.doacao.Helper.GenericParcelable;
import br.org.aacc.doacao.Helper.HtmlHelper;
import br.org.aacc.doacao.Helper.HttpHelper;
import br.org.aacc.doacao.Helper.TrackHelper;
import br.org.aacc.doacao.Mapas.MapLocationSource;
import br.org.aacc.doacao.Services.NetWorkService;
import br.org.aacc.doacao.Services.MapService;
import br.org.aacc.doacao.Helper.PrefHelper;
import br.org.aacc.doacao.Utils.EnumCommand;
import br.org.aacc.doacao.Utils.HandleFile;


public class MapsActivity extends _SuperActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    // private FragmentTransaction transaction;
    private SupportMapFragment mapFragment;
    //Google Objects

    //Criar o objeto mapa
    protected GoogleMap map;

    protected GoogleApiClient googleApiClient;

    protected MapService mapService;
    //Opcoes de Configuração do Mapa
    protected GoogleMapOptions options;
    //Posicionamento e Configuração da Camera
    protected CameraUpdate cameraUpdate;
    protected CameraPosition cameraPosition;
    //Seta os pontos no mapa com Imagem
    //  protected MarkerOptions markerOptions;
    //   protected Marker marker;
    //Latitude e Longitude(Usado para tudo)
    protected LatLng latLng;
    protected List<LatLng> listLatLng;
    //  protected List<LatLng> listEventos;
    //Desenha as linhas da rota
    //  protected Polyline polyline;
    //Usado para o GPS
    protected MapLocationSource myLocation;
    protected LocationManager locationManager;
    protected static Location globalLocation;
    protected LocationRequest mLocationRequest;

    //Distancia Total
    protected boolean allowNetwork;
    private Marker markerChange;

    private static final String TAG = "MapsActivity";

    private GenericParcelable<List<Caccc>> genericParcelable;
    private GenericParcelable<Caccc> cacccGenericParcelable;
    private GenericParcelable<Bazar> bazarGenericParcelable;
    private Collection<Caccc> cacccList;
    private Collection<Bazar> bazarList;
    private ProgressBar progressBar;

    private Caccc caccc;
    private Bazar bazar;
    private String _jsonString;
    private String url;
    private Endereco endereco;
    private Bundle bundle;
    private TextView txtViewWindowInfo;
    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        this.ConfigureToolbar("Localização");
        this.ConfigureReturnToolbar();
        this.url = ConstantHelper.urlWebApiConteudoContasPorCaccc.replace("{0}","2");

        try {

            bundle = getIntent().getExtras().getBundle(ConstantHelper.objBundle);
            this.progressBar = this.findViewById(R.id.progress_bar);
            handleFile = new HandleFile(_context, ConstantHelper.fileListAllBazaresPorCaccc);

            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);


            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();


            if (savedInstanceState != null && savedInstanceState.getParcelable(caccc.TAG) != null) {
                genericParcelable = savedInstanceState.getParcelable(caccc.TAG);
                if (genericParcelable.getValue() != null && genericParcelable.getValue().size() > 0)
                    PutInMap(genericParcelable.getValue());
                else
                    new MapsActivity.DownloadTask().execute(url);
            } else
                new MapsActivity.DownloadTask().execute(url);

        } catch (Exception e) {
            TrackHelper.WriteError(this, "onCreate", e.getMessage());
        }

    }

    private void ChecaLocalizacao() {
        if (!NetWorkService.instance().isEnabledLocation(_context))
            super.showSimpleDialog("Usar localização? ", "Está funcionalidade ativar a localização para continuar", EnumCommand.Localization);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.ChecaLocalizacao();
        if (!googleApiClient.isConnected())
            googleApiClient.connect();


    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {

            this.map = googleMap;

            if (this.map != null) {
                this.ConfigMap();
                this.MapEvents();
            }

        } catch (Exception e) {
            TrackHelper.WriteError(this, "onMapReady", e.getMessage());
        }

    }

    private void ConfigMap() {
        try {
            //Tipo do Mapa
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            if (listLatLng == null)
                listLatLng = new ArrayList();
            //Inicia o Mapa na localização( Av. Paulista -SP)
            if (bundle == null)
                latLng = new LatLng(-23.6362736, -46.780512);
            else if (bundle.getParcelable(ConstantHelper.objCaccc) != null) {
                caccc = ((GenericParcelable<Caccc>) bundle.getParcelable(ConstantHelper.objCaccc)).getValue();
                latLng = new LatLng(caccc.getEndereco().getLatitude(), caccc.getEndereco().getLongitude());
            } else if (bundle.getParcelable(ConstantHelper.objBazar) != null) {
                bazar = ((GenericParcelable<Bazar>) bundle.getParcelable(ConstantHelper.objBazar)).getValue();
                latLng = new LatLng(bazar.getEndereco().getLatitude(), bazar.getEndereco().getLongitude());
            } else if (bundle.getString(ConstantHelper.objActivity) != null) {
                if (bundle.getString(ConstantHelper.objActivity).contains("Perfil")) {
                    Double latitude = Double.parseDouble(PrefHelper.getString(_context, PrefHelper.PreferenciaLatitude));
                    Double longitude = Double.parseDouble(PrefHelper.getString(_context, PrefHelper.PreferenciaLongitude));
                    latLng = new LatLng(latitude, longitude);
                }

            }


            //Posiciona a camera/*
            this.ConfigMapService(map);
            //Adiciona a casa
            this.AddMapHome();

            map.getUiSettings().setCompassEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);
            map.getUiSettings().setIndoorLevelPickerEnabled(true);
            //Gestures
            map.getUiSettings().setAllGesturesEnabled(true);
            map.getUiSettings().setMapToolbarEnabled(true);
            map.getUiSettings().setScrollGesturesEnabled(true);
            map.getUiSettings().setTiltGesturesEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);

        } catch (Exception e) {
            TrackHelper.WriteError(this, "ConfigMap", e.getMessage());
        }

    }

    private void ConfigMapService(GoogleMap map) {
        try {

            mapService = new MapService(map, getApplicationContext());

            if (bundle == null) {
                mapService.ConfigCameraPosition(latLng);
                mapService.SetDeviceLocation(latLng);
            } else {
                globalLocation = this.GetLastKnownLocation();
                mapService.ConfigCameraPosition(latLng);

                if (globalLocation != null) {
                    latLng = new LatLng(globalLocation.getLatitude(), globalLocation.getLongitude());
                    mapService.SetDeviceLocation(latLng);
                }
            }
        } catch (Exception e) {
            TrackHelper.WriteError(this, "ConfigMapService : ", e.getMessage());
        }
    }

    private void AddMapServiceInstitute(Caccc caccc) {
        try {

            double Latitude = caccc.getLatitude();
            double Longitute = caccc.getLongitude();
            String complemento = caccc.getTelefone().equals("") ? caccc.getCelular() : caccc.getTelefone();

            if (caccc.getDescription() != null && !caccc.getDescription().isEmpty())
                complemento += " - " + caccc.getDescription();

            mapService.CustomAddMarkerInstitute(
                    new LatLng(Latitude, Longitute),
                    caccc.getName(),
                    String.format("%s", complemento),
                    "Centro",
                     caccc.getUrlImage()
            );


        } catch (Exception e) {
            throw e;
        }
    }

    private void AddMapServiceStore(Bazar bazar) {
        try {
            double Latitude = bazar.getEndereco().getLatitude();
            double Longitute = bazar.getEndereco().getLongitude();
            String complemento = TextUtils.isEmpty(bazar.getDescription()) ? bazar.getEndereco().getBairro() : bazar.getDescription();

            mapService.CustomAddMarkerStore(
                    new LatLng(Latitude, Longitute),
                    bazar.getName(),
                    complemento,
                    "Bazar"
            );

        } catch (Exception e) {
            throw e;
        }
    }

    private void AddMapHome() {
        try {
            if (PrefHelper.getString(_context, "pref_latitude") != null && PrefHelper.getString(_context, "pref_longitude") != null) {

                double latHome = Double.parseDouble(PrefHelper.getString(_context, "pref_latitude"));
                double lonHome = Double.parseDouble(PrefHelper.getString(_context, "pref_longitude"));

                if (latHome != 0 && lonHome != 0) {
                    mapService.CustomAddHome(
                            new LatLng(latHome, lonHome),
                            "Lar, docê lar!",
                            "Sua residência",
                            "Lar"
                    );
                }
            }
        } catch (Exception e) {
            TrackHelper.WriteError(this, "setOnMapClickListener", e.getMessage());
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(caccc.TAG, new GenericParcelable<>(cacccList));
    }

    private void MapEvents() {
        try {
            //Nao vamos trabalhar com este metodo
            map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    String infoText;

                    String tag = String.valueOf(marker.getTag());

                    if (tag.equals("Centro")) {
                        infoText = "<b><font color='#1B5E20' size='2'>" + marker.getTitle() + "</font></b> " +
                                "<br/> " +
                                "<font color='#E65100' size='1'> " + marker.getSnippet() + " </font>" +
                                "<br/> " +
                                "<b><i><font color='#9E9E9E' size='1'>Clique para saber mais!</font></i><b/>";
                    } else if (tag.equals("Bazar")) {

                        infoText = "<b><font color='#1B5E20' size='2'>" + marker.getTitle() + "</font></b> " +
                                "<br/> " +
                                "<font color='#E65100' size='1'> " + marker.getSnippet() + " </font>" +
                                "<br/> " +
                                "<b><i><font color='#9E9E9E' size='1'>Clique para conhecer as proximidades ao bazar!</font></i><b/>";
                    } else {

                        infoText = "<b><font color='#1B5E20' size='2'>" + marker.getTitle() + "</font></b> " +
                                "<br /> " +
                                "<font color='#E65100' size='1'> " + marker.getSnippet() + " </font>" +
                                "<br/> ";

                    }


                    ll = new LinearLayout(MapsActivity.this);
                    ll.setPadding(10, 10, 10, 10);
                    ll.setBackgroundColor(Color.WHITE);

                    txtViewWindowInfo = new TextView(getBaseContext());
                    txtViewWindowInfo.setTextSize(10);
                    txtViewWindowInfo.setText(HtmlHelper.fromHtml(infoText));


                    Linkify.addLinks(txtViewWindowInfo, Linkify.PHONE_NUMBERS);
                    ll.addView(txtViewWindowInfo);
                    return ll;
                }
            });

            map.setOnCameraMoveStartedListener((new GoogleMap.OnCameraMoveStartedListener() {
                @Override
                public void onCameraMoveStarted(int i) {

                }
            }));

            map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {


                }
            });

            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {

                    try {

                        TrackHelper.WriteInfo(this, "setOnMapClickListener", latLng.latitude + " - " + latLng.longitude);

                    } catch (Exception e) {

                        TrackHelper.WriteError(this, "setOnMapClickListener", e.getMessage());

                    }


                }
            });


            //Aqui capturar a posição para a localizaçao da instituiçao ou bazar
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    try {
                        markerChange = marker;
                        ChangeRoute(markerChange);
                    } catch (Exception e) {
                        TrackHelper.WriteError(this, "setOnMarkerClickListener", e.getMessage());
                    } finally {
                        return false;
                    }
                }
            });

            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    TrackHelper.WriteInfo(this, "onInfoWindowClick", marker.getSnippet());
                    caccc = null;
                    bazar = null;
                    String tag = String.valueOf(marker.getTag());

                    if (cacccList.size() > 0) {

                        for (Caccc itemCaccc : cacccList) {

                            if (tag.equals("Centro") && itemCaccc.getName().contains(marker.getTitle().trim()))
                                caccc = itemCaccc;

                            if (tag.equals("Bazar")) {

                                for (Bazar itemBazar : itemCaccc.getBazars()) {
                                    if (itemBazar.getName().contains(marker.getTitle().trim()))
                                        bazar = itemBazar;
                                }
                            }
                        }
                    }

                    if (caccc != null && tag.equals("Centro")) {
                        intent = new Intent(_context, TabsCacccActivity.class);
                        cacccGenericParcelable = new GenericParcelable<>(caccc);
                        bundle = new Bundle();
                        bundle.putParcelable(ConstantHelper.objCaccc, cacccGenericParcelable);
                        intent.putExtra(ConstantHelper.objBundle, bundle);
                        startActivity(intent);
                    } else if (bazar != null && tag.equals("Bazar")) {
                        intent = new Intent(_context, VisaoRuaActivity.class);
                        bazarGenericParcelable = new GenericParcelable<>(bazar);
                        bundle = new Bundle();
                        bundle.putParcelable(ConstantHelper.objBazar, bazarGenericParcelable);
                        intent.putExtra(ConstantHelper.objBundle, bundle);
                        startActivity(intent);
                    }

                }
            });

        } catch (Exception e) {
            TrackHelper.WriteError(this, "setOnMapClickListener", e.getMessage());
        }
    }

    //***********************************************************************************************

    private void ChangeRoute(Marker marker) {
        try {
            LatLng origin;
            if (marker != null) {
                if (globalLocation != null)
                    origin = new LatLng(globalLocation.getLatitude(), globalLocation.getLongitude());
                else
                    origin = latLng;

                LatLng destination = marker.getPosition();
                mapService.ClearRoute();
                GoogleRoute(origin, destination);
            }
        } catch (Exception e) {
            TrackHelper.WriteError(this, "ChangeRoute", e.getMessage());
        }

    }

    public void GoogleRoute(final LatLng origin, final LatLng destination) {

        new Thread() {
            public void run() {

                String url = "http://maps.googleapis.com/maps/api/directions/json?" +
                        "origin=" + origin.latitude + "," + origin.longitude +
                        "&destination=" + destination.latitude + "," + destination.longitude +
                        "&sensor=false&mode=driving&alternatives=true";

                try {


                    final String answer = HttpHelper.makeServiceCall(url);

                    runOnUiThread(new Runnable() {
                        public void run() {

                            try {

                                listLatLng = mapService.BuildJSONRoute(answer);
                                mapService.DrawRoute(listLatLng);

                            } catch (JSONException e) {
                                TrackHelper.WriteError(this, "setOnMapClickListener", e.getMessage());
                            }
                        }
                    });

                } catch (Exception e) {
                    TrackHelper.WriteError(this, "GoogleRoute", e.getMessage());
                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        if (googleApiClient.isConnected())
            googleApiClient.disconnect();
        super.onDestroy();
        this.finish();

    }

    //**************************************LocationListener***************************************

    protected void startLocationUpdates() {

        try {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                return;

            if (!googleApiClient.isConnected())
                return;

            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(ConstantHelper.OneSecond * 10);
            mLocationRequest.setFastestInterval(ConstantHelper.OneSecond * 1);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);

        } catch (Exception e) {
            TrackHelper.WriteInfo(this, "startLocationUpdates", String.valueOf(googleApiClient.isConnected()));
        }


    }

    protected void stopLocationUpdates() {
        if (googleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        else
            TrackHelper.WriteInfo(this, "stopLocationUpdates", String.valueOf(googleApiClient.isConnected()));

    }

    @Override
    public void onStart() {
        super.onStart();
        if (!googleApiClient.isConnected())
            googleApiClient.connect();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient.isConnected())
            googleApiClient.disconnect();
    }

    @Nullable
    private Location GetLastKnownLocation() {
        LocationManager lm;
        Location localLocation;

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                return null;

            localLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            if (localLocation == null) {
                lm = (LocationManager) getSystemService(this.LOCATION_SERVICE);
                localLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            if (localLocation == null) {
                lm = (LocationManager) getSystemService(this.LOCATION_SERVICE);
                localLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (localLocation != null)
                globalLocation = localLocation;

        } catch (Exception e) {
            TrackHelper.WriteError(this, "parseResult", e.getMessage());
        } finally {
            return globalLocation;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        TrackHelper.WriteInfo(this, "onConnected", "Connectado ao google");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        TrackHelper.WriteInfo(this, "onConnectionSuspended", "Conexao google suspensa");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        TrackHelper.WriteInfo(this, "onConnectionFailed", "Conexao falhou");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            globalLocation = location;
            mapService.SetDeviceLocation(new LatLng(globalLocation.getLatitude(), globalLocation.getLongitude()));
            ChangeRoute(markerChange);
        }
    }


    //---------------------------------Async---------------------------------------------------

    public class DownloadTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {

            Integer result = 0;

            try {
                String fileJson = handleFile.ReadFile();

                if (TextUtils.isEmpty(fileJson)) {
                    _jsonString = HttpHelper.makeServiceCall(params[0]);

                    if (_jsonString != null && _jsonString.length() > 0) {
                        handleFile.WriteFile(_jsonString);
                        fileJson = _jsonString;
                    }
                }

                parseResult(fileJson);
                result = 1; // Successful

            } catch (Exception e) {
                TrackHelper.WriteError(this, "DownloadTask doInBackground", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            progressBar.setVisibility(View.GONE);
            if (result == 1) {
                PutInMap(cacccList);
            } else {
                Toast.makeText(MapsActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(String result) {
        try {


            _jsonObject = new JSONObject(result);

            if(_jsonObject!=null)
            {

                if(cacccList == null)
                    cacccList =new ArrayList<>();

                caccc = new Caccc();
                caccc.setId(_jsonObject.optInt("CacccId"));
                caccc.setName(_jsonObject.optString("Nome"));
                caccc.setUrlImage(_jsonObject.optString("UrlImagem"));
                caccc.setEmail(_jsonObject.optString("Email"));
                caccc.setTelefone(_jsonObject.optString("Telefone"));
                caccc.setCelular(_jsonObject.optString("Celular"));
                caccc.setEmailPagSeguro(_jsonObject.optString("EmailPagSeguro"));
                caccc.setEmailPayPal(_jsonObject.optString("EmailPayPal"));
                caccc.setLatitude(_jsonObject.optJSONObject("Endereco").optDouble("Latitude"));
                caccc.setLongitude(_jsonObject.optJSONObject("Endereco").optDouble("Longitude"));
                caccc.setDoadores(null);


                JSONArray jsonArray = _jsonObject.getJSONArray("Bazares");

                if (jsonArray != null && jsonArray.length() > 0) {
                    bazarList = new ArrayList<>();

                    for (int j = 0; j < jsonArray.length(); j++) {

                        bazar = new Bazar();
                        endereco = new Endereco();

                        bazar.setId(jsonArray.getJSONObject(j).optInt("BazarId"));
                        bazar.setName(jsonArray.getJSONObject(j).optString("Nome"));
                        bazar.setUrlImage(jsonArray.getJSONObject(j).optString("UrlImagem"));
                        bazar.setDescription(jsonArray.getJSONObject(j).optString("Descricao"));
                        //----------------------------------------------------Endereco------------------------------
                        endereco.setBairro(jsonArray.getJSONObject(j).optJSONObject("Endereco").optString("Bairro"));
                        endereco.setLatitude(jsonArray.getJSONObject(j).optJSONObject("Endereco").optDouble("Latitude"));
                        endereco.setLongitude(jsonArray.getJSONObject(j).optJSONObject("Endereco").optDouble("Longitude"));

                        if ((caccc.getLatitude() == endereco.getLatitude()) && (caccc.getLongitude() == endereco.getLongitude())) {
                            endereco.setLatitude(0);
                            endereco.setLongitude(0);
                            endereco.setBairro(null);
                            caccc.setDescription("Possui bazar no local.");

                        }
                        bazar.setEndereco(endereco);
                        bazarList.add(bazar);
                    }
                }

                caccc.setBazars(bazarList);
                cacccList.add(caccc);
            }

        } catch (JSONException e) {
            TrackHelper.WriteError(this, "parseResult", e.getMessage());
        } catch (Exception e) {
            TrackHelper.WriteError(this, "parseResult", e.getMessage());
        }
    }

    private void PutInMap(Collection<Caccc> listCaccc) {
        try {

            for (Caccc caccc : listCaccc) {

                this.AddMapServiceInstitute(caccc);

                if (caccc.getBazars() != null) {
                    for (Bazar bazar : caccc.getBazars())
                        this.AddMapServiceStore(bazar);
                }
            }

        } catch (Exception e) {
            TrackHelper.WriteError(this, "setAdapter", e.getMessage());
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            TrackHelper.WriteInfo(this, "onConfigurationChanged", "ORIENTATION_PORTRAIT");

        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            TrackHelper.WriteInfo(this, "onConfigurationChanged", "ORIENTATION_LANDSCAPE");

        }


    }


}
