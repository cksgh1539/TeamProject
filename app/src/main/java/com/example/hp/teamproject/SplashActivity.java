package com.example.hp.teamproject;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


/**
 * Created by A on 2017-12-05.
 */

public class SplashActivity extends AppCompatActivity {
    ImageView logo1;
    ImageView logo2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        logo1 = (ImageView)findViewById(R.id.logo1);
        logo2 = (ImageView)findViewById(R.id.logo2);
    }

    @Override
    protected void onResume() {
        super.onResume();

        startLogo1TweenAnimation();
        startLogo2ValuePropertyAnimation();

    }

    private void startLogo1TweenAnimation() {
        Animation logo_anim = AnimationUtils.loadAnimation(this, R.anim.logo);
        logo1.startAnimation(logo_anim);
        logo_anim.setAnimationListener(animationListener);
    }

    private void startLogo2ValuePropertyAnimation() {
        ValueAnimator alphaAnimator = ValueAnimator.ofFloat(0.1f, 1);
        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                logo2.setAlpha(value);
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.play(alphaAnimator);
        animatorSet.setDuration(2000);
        animatorSet.start();
    }

    Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            finish();
            startActivity(new Intent(getApplicationContext(), RSmap.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };

}
