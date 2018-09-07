package br.org.aacc.doacao;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.transition.Slide;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import br.org.aacc.doacao.Helper.ConstantHelper;
import br.org.aacc.doacao.Helper.TrackHelper;

public class ApresentacaoActivity extends _SuperActivity {

    private Animation animation;
    private ImageView imageView;
    private Slide ts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apresentacao);
        this.CallSplashScreen();
    }

    private void CallSplashScreen(){
        try{

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    GoToWelcome();
                }
            }, ConstantHelper.SplashScreenTime);

        }
        catch (Exception e)
        {
            TrackHelper.WriteError(this, "GoToWelcome", e.getMessage());

        }
    }

    protected void GoToWelcome() {
        try {
            intent = new Intent(this, WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             startActivity(intent);
             this.overridePendingTransition(R.anim.push_left_in,R.anim.push_up_out);
            finish();
        } catch (Exception e) {
            TrackHelper.WriteError(this, "GoToWelcome", e.getMessage());
        }

    }

    @Override
    public void onStart(){
        super.onStart();
        imageView =this.findViewById(R.id.imgMascote);
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.scale_up);
        imageView.startAnimation(animation);
    }
}
