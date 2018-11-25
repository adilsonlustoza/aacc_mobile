package br.org.aacc.doacao;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONObject;

import br.org.aacc.doacao.Api.FacebookApi;
import br.org.aacc.doacao.Domain.Caccc;
import br.org.aacc.doacao.Helper.ConstantHelper;
import br.org.aacc.doacao.Helper.GenericParcelable;
import br.org.aacc.doacao.Helper.PrefHelper;
import br.org.aacc.doacao.Helper.TrackHelper;
import br.org.aacc.doacao.Api.GoogleApi;
import br.org.aacc.doacao.Domain.FacebookUser;
import br.org.aacc.doacao.Domain.GoogleUser;
import br.org.aacc.doacao.Helper.ImageHelper;
import br.org.aacc.doacao.Helper.SimpleDialogFragmentHelper;
import br.org.aacc.doacao.Interfaces.OnCustomDialogClickListener;
import br.org.aacc.doacao.Services.NetWorkService;
import br.org.aacc.doacao.Utils.EnumCommand;
import br.org.aacc.doacao.Utils.HandleFile;
import br.org.aacc.doacao.Utils.UtilApplication;

/**
 * Created by ubuntu on 12/14/16.
 */

public class _SuperActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected UtilApplication<String,GenericParcelable<Caccc>> cacccUtilApplication;
    protected GenericParcelable<Caccc> cacccGenericParcelable;

    protected int idCentro;
    protected String eMailCentro;

    protected Toolbar toolbar;
    protected DrawerLayout drawer;
    protected Activity _activity;
    protected MenuInflater _menuInflater;
    protected Bitmap _bitmap;
    protected ProgressBar progressBar;
    protected String _jsonString;
    protected String _fileJson;
    protected SimpleDialogFragmentHelper _simpleDialogFragmentHelper;
    protected FragmentManager fragmentManager;

    protected ActionBarDrawerToggle actionBarDrawerToggle;

    protected ImageView imageView;
    protected Intent intent;
    protected View _globalView;
    protected Bitmap bitmap;

    protected FacebookUser facebookUser;
    protected GoogleUser googleUser;


    protected Context _context;

    protected NavigationView navigationView;

    protected View header;

    protected static Menu menu;

    protected TextView name;

    protected TextView email;

    protected Bundle bundle;

    private Slide ts;

    private OnCustomDialogClickListener onCustomDialogClickListener;

    public UtilApplication<String,GenericParcelable<Caccc>> _cacccUtilApplication;

    public GenericParcelable<Caccc> _cacccGenericParcelable;

    protected JSONArray _jsonArrayResponse;

    protected JSONObject _jsonObject;

    protected HandleFile handleFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Transition
            getWindow().requestFeature(android.view.Window.FEATURE_CONTENT_TRANSITIONS);
            ts = new Slide(); //Explode();
            ts.setStartDelay(0);
            ts.setDuration(500);
            ts.setInterpolator(new AccelerateInterpolator(3));
            getWindow().setEnterTransition(ts);
        }

        setContentView(R.layout.activity_main);
        //Toolbar
        this.ConfigureToolbar();
        this.ConfigureNavegationDrawer();

        //Global Actitivity, Context, View
        _activity = this;
        _context = this;
        _globalView = toolbar;


    }





    protected void ConfigureNavegationDrawer() {
        try {

            drawer = this.findViewById(R.id.drawer_layout);
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
            actionBarDrawerToggle.isDrawerSlideAnimationEnabled();
            navigationView = this.findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        } catch (Exception e) {
            TrackHelper.WriteError(this, "ConfigureNavegationDrawer", e.getMessage());
        }
    }

    protected void ConfigureToolbarSuporte() {
        try {
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");

        } catch (Exception e) {
            TrackHelper.WriteError(this, "ConfigureToolBar", e.getMessage());
        }

    }

    protected void ConfigureToolbar() {
        try {
            toolbar = this.findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setLogo(R.mipmap.ic_launcher);
            getSupportActionBar().setTitle(ConstantHelper.AppName);
            getSupportActionBar().setSubtitle(ConstantHelper.ToolbarSubTitleSuper);
        } catch (Exception e) {
            TrackHelper.WriteError(this, "ConfigureToolBar", e.getMessage());
        }

    }

    protected void ConfigureToolbar(String subTitle) {
        try {

            toolbar = this.findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setLogo(R.mipmap.ic_launcher);
            getSupportActionBar().setTitle(ConstantHelper.AppName);
            getSupportActionBar().setSubtitle(subTitle);


        } catch (Exception e) {
            TrackHelper.WriteError(this, "ConfigureToolBar", e.getMessage());
        }

    }

    protected void ConfigureReturnToolbar() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _activity.onBackPressed();
            }
        });

    }

    //-------------------------------------------region  Methods---------------------------------

    protected void GoToApresentacao() {
        try {
            intent = new Intent(this, ApresentacaoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            TrackHelper.WriteError(this, "GoToWelcome", e.getMessage());
        }

    }

    protected void GoToWelcome() {
        try {
            intent = new Intent(this, WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            TrackHelper.WriteError(this, "GoToWelcome", e.getMessage());
        }

    }

    protected void ExitApp() {
        try {
            LoginManager.getInstance().logOut();
            ConstantHelper.isLogado = false;
            ConstantHelper.isLogadoRedesSociais = false;
            intent = new Intent(_context, WelcomeActivity.class);
            intent.putExtra("Sair", "Sim");
            startActivity(intent);
            finish();
        } catch (Exception e) {
            throw e;
        }
    }

    protected void ConfigureFacebook() {
        try {

            FacebookSdk.sdkInitialize(getApplicationContext());
            AppEventsLogger.activateApp(this);

            if (AccessToken.getCurrentAccessToken() != null) {

                facebookUser = new FacebookApi().GetProfilePicture();
                //Get Navegation View
                header = GetNavegationViewHeader();

                TextView name = this.header.findViewById(R.id.txtNomeNavHeader);
                TextView email = this.header.findViewById(R.id.txtEmailNavHeader);

                name.setText(facebookUser.getCompositeName());
                email.setText(facebookUser.Email);
                imageView = this.header.findViewById(R.id.imgLoginNavHeader);

                _bitmap = ImageHelper.getCircularBitmap(
                        ImageHelper.getBitmap(facebookUser.ImageUrl)
                );

                imageView.setImageBitmap(_bitmap);

                PrefHelper.setString(_context, PrefHelper.PreferenciaNome, facebookUser.getCompositeName());
                PrefHelper.setString(_context, PrefHelper.PreferenciaEmail, facebookUser.Email);
                PrefHelper.setString(_context, PrefHelper.PreferenciaFoto, ImageHelper.EncodeTobase64(_bitmap));
            }

        } catch (Exception e) {
            TrackHelper.WriteError(this, "ConfigureFacebook Main", e.getMessage());
        }
    }

    protected void ConfigureGoogle() {

        try {

            googleUser = GoogleApi.GetProfileGoogle();

            if (googleUser != null) {

                header = GetNavegationViewHeader();

                TextView name = header.findViewById(R.id.txtNomeNavHeader);
                TextView email = header.findViewById(R.id.txtEmailNavHeader);

                name.setText(googleUser.getNickName());
                email.setText(googleUser.Email);
                imageView = header.findViewById(R.id.imgLoginNavHeader);

                _bitmap = ImageHelper.getCircularBitmap(
                        ImageHelper.getBitmap(googleUser.UrlImage.toString())
                );
                imageView.setImageBitmap(_bitmap);

                PrefHelper.setString(_context, PrefHelper.PreferenciaNome, googleUser.getNickName());
                PrefHelper.setString(_context, PrefHelper.PreferenciaEmail, googleUser.Email);
                PrefHelper.setString(_context, PrefHelper.PreferenciaFoto, ImageHelper.EncodeTobase64(_bitmap));

            }

        } catch (Exception e) {
            TrackHelper.WriteError(this, "ConfigureGoogle Main", e.getMessage());
        }

    }

    public View GetNavegationViewHeader() {
        try {
            navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            header = navigationView.getHeaderView(0);
            return header;
        } catch (Exception e) {
            TrackHelper.WriteError(this, "GetNavegationViewHeader", e.getMessage());

        }
        return null;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this._menuInflater = getMenuInflater();
        this._menuInflater.inflate(R.menu.main, menu);
        this.setMenuItemEnabled(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_politica) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ConstantHelper._urlPolitica)));
            return true;
        }
        else if (id == R.id.action_help) {
            startActivity(new Intent(this, AjudaActivity.class));
            return true;
        }
        //noinspection SimplifiableIfStatement
        else if (id == R.id.action_exit) {
            ExitApp();
            return true;
        } else if (id == android.R.id.home) {
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    //-------------------------------------------endregion----------------------------------------


    //-------------------------------------region  NavegationDrawer-------------------------------

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        try {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_map) {
                intent = new Intent(this, MapsActivity.class);

            }/* else if (id == R.id.nav_instituicoes) {
                intent = new Intent(this, CacccActivity.class);*/

            else if (id == R.id.nav_ong) {
                intent = new Intent(this, TabsCacccActivity.class);

            } else if (id == R.id.nav_eventos) {
                intent = new Intent(this, BazarActivity.class);

            }  else if (id == R.id.nav_newspaper) {
                    intent = new Intent(this, NoticiasActivity.class);

            }  else if (id == R.id.nav_donation) {
                intent = new Intent(this, DoacaoActivity.class);

            } else if (id == R.id.nav_perfil) {
                intent = new Intent(this, PerfilActivity.class);

            } else if (id == R.id.nav_pref) {
                intent = new Intent(this, PreferenciaActivity.class);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            else
                startActivity(intent);

            DrawerLayout drawer = this.findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        } catch (Exception e) {
            TrackHelper.WriteError(this, "onNavigationItemSelected", e.getMessage());
        } finally {
            return true;
        }


    }

    @Override
    public void onBackPressed() {

        try {
            DrawerLayout drawer = this.findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }

        } catch (Exception ex) {
            TrackHelper.WriteError(this, "onBackPressed", ex.getMessage());
        }

        super.onBackPressed();
        finish();

    }
    //-------------------------------------

    private void setMenuItemEnabled(Menu menu) {
        try {
            String className = this._activity.getComponentName().getClassName();

            for (int i = 0; i < menu.size(); i++) {
                if (className.contains(("Welcome")))
                    menu.getItem(i).setVisible(false);
                else if (className.contains(("Login")))
                    menu.getItem(i).setVisible(false);

            }

        } catch (Exception e) {
            TrackHelper.WriteError(_context, "HideOptionMenu", e.getMessage());
        }

    }


//----------------------------Region Dialog---------------------------------------


    public void showSimpleDialog(String title, String question, EnumCommand enumCommand) {

        fragmentManager = this.getSupportFragmentManager();
        boolean _exitApp = true;

        onCustomDialogClickListener = new OnCustomDialogClickListener() {
            @Override
            public void onItemClick(EnumCommand command) {

                if (command == EnumCommand.AllConfig) {

                    intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(Settings.ACTION_SETTINGS);
                    startActivity(intent);

                } else if (command == EnumCommand.NetWorkEnableWiFi) {
                    intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(intent);
                } else if (command == EnumCommand.NetWorkEnabledMobile) {
                    intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(Settings.ACTION_DATA_ROAMING_SETTINGS);
                    startActivity(intent);

                } else if (command == EnumCommand.Localization) {
                    intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.putExtra("enabled", true);
                    startActivity(intent);
                } else if (command == EnumCommand.ExitApp) {
                    ExitApp();
                } else if (command == EnumCommand.CloseWindow) {
                    finish();
                }

            }
        };

        _simpleDialogFragmentHelper = SimpleDialogFragmentHelper.newInstance(title, question, onCustomDialogClickListener);
        if (enumCommand == EnumCommand.Localization)
            _exitApp = false;

        _simpleDialogFragmentHelper.setCommand(enumCommand, _exitApp);
        _simpleDialogFragmentHelper.show(fragmentManager, "fragment_dialog_simple");

    }


    @Override
    public void onStart() {
        super.onStart();
        if (!this.getLocalClassName().contains("Apresentacao"))
            this.ChecaRede();
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    private void ChecaRede() {
        try {

            if (!NetWorkService.instance().isEnabledNetWork(_context))
                showSimpleDialog("Usar rede ?", "Este app necessita de conexão com a rede para continuar.", EnumCommand.NetWorkEnabledMobile);

        } catch (Exception e) {
            TrackHelper.WriteError(this, "ChecaInternet", e.getMessage());
        }
    }


    public void BackgroundSendMailNoAtach(Activity activity, String to, String content) {
        try {
            final Activity _context = activity;
            String _content = content.replaceAll("(\r\n|\n)", "<br />");
            BackgroundMail.newBuilder(_context)
                    .withUsername(ConstantHelper.emailApp)
                    .withPassword(ConstantHelper.emailPasswordApp)
                    .withMailto(to)
                    .withType(BackgroundMail.TYPE_HTML)
                    .withSubject(ConstantHelper.emailSubjectRetirada)
                    .withBody(_content.toString())
                    .withSendingMessage("Enviando e-mail...")
                    .withSendingMessageSuccess("Email enviado com sucesso a instituição!")
                    .withSendingMessageError("Erro ao enviar e-mail")
                    .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(_context, "Email agendado para envio", Toast.LENGTH_LONG);
                        }
                    })
                    .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                        @Override
                        public void onFail() {

                            Toast.makeText(_context, "Erro ao enviar e-mail", Toast.LENGTH_LONG);
                        }
                    })
                    .send();

        } catch (Exception e) {
            TrackHelper.WriteError(ConstantHelper.class, "BackgroundSendMail error : ", e.getMessage());
        }
    }


    public void BackgroundSendMail(Activity activity, String to, String subject, String content, @Nullable String uri) {
        try {
            final Activity _context = activity;

            BackgroundMail
                    .newBuilder(_context)
                    .withUsername(ConstantHelper.emailApp)
                    .withPassword(ConstantHelper.emailPasswordApp)
                    .withMailto(ConstantHelper.emailToTest)
                    .withType(BackgroundMail.TYPE_HTML)
                    .withSubject(subject)
                    .withBody(content)
                    .withAttachments(uri)
                    .withSendingMessage("Enviando e-mail...")
                    .withSendingMessageSuccess("Email enviado com sucesso!")
                    .withSendingMessageError("Erro ao enviar e-mail")
                    .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(_context, "Email enviado", Toast.LENGTH_LONG);
                        }
                    })
                    .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                        @Override
                        public void onFail() {
                            Toast.makeText(_context, "Erroa ao enviar e-mail", Toast.LENGTH_LONG);
                        }
                    }).send();

        } catch (Exception e) {
            TrackHelper.WriteError(ConstantHelper.class, "BackgroundSendMail error : ", e.getMessage());
        }
    }


}
