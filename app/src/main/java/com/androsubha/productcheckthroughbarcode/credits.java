package com.androsubha.productcheckthroughbarcode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class credits extends AppCompatActivity {

    private TextView creditsInfo;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        getSupportActionBar().setTitle("About");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        creditsInfo=findViewById(R.id.textView6);
        animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.credits);
        creditsInfo.startAnimation(animation);

    }
}
