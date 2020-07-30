package com.androsubha.productcheckthroughbarcode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public String barcode;

    Button scanbtn,finish,scan_again;
    TextView country,descrip;
    LottieAnimationView anim,anim_scanner;

    TextView rate_us,share,about;

    private long backPressedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanbtn = findViewById(R.id.scan_btn);

        scanbtn.setOnClickListener(this);

        anim_scanner=findViewById(R.id.lv_main);
        rate_us=findViewById(R.id.rate_app);
        share=findViewById(R.id.share_app);
        about=findViewById(R.id.about_app);

        checkPermissions();

        rate_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent=new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Share App");
                    String shareMessage="https://play.google.com/store/apps/details?id="+BuildConfig.APPLICATION_ID+"\n\n";
                    intent.putExtra(Intent.EXTRA_TEXT,shareMessage);
                    startActivity(Intent.createChooser(intent,"Share by"));
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Error Occured",Toast.LENGTH_SHORT).show();
                }
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),credits.class));
            }
        });
    }

    private void checkPermissions() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response)
                    {
                        anim_scanner.playAnimation();
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response)
                    {
                        anim_scanner.pauseAnimation();
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token)
                    {
                       token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public void onClick(View v) {
        scancode();
    }

    private void scancode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning....");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        barcode=result.getContents();

        if (result != null) {
            if (result.getContents() != null)
            {
                if(barcode.length()>=12){

                    Dialog dialog =new Dialog(this);
                    dialog.setContentView(R.layout.check_product_country_dialog);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().getAttributes().windowAnimations= android.R.style.Animation_Dialog;

                    country=dialog.findViewById(R.id.tv_country);
                    descrip=dialog.findViewById(R.id.tv_descrip);
                    finish=dialog.findViewById(R.id.button_finish);
                    scan_again=dialog.findViewById(R.id.button_scan_again);
                    anim= dialog.findViewById(R.id.lottieAnimationView);

                    if(barcode.substring(0,3).equals("690") || barcode.substring(0,3).equals("691") || barcode.substring(0,3).equals("692") || barcode.substring(0,3).equals("693")
                            || barcode.substring(0,3).equals("694") || barcode.substring(0,3).equals("695") || barcode.substring(0,3).equals("696") || barcode.substring(0,3).equals("696")
                            || barcode.substring(0,3).equals("697") || barcode.substring(0,3).equals("698") || barcode.substring(0,3).equals("699")){
                        anim.setAnimation(R.raw.china);
                        country.setText("China");
                        descrip.setText("Boycott China");
                    }
                    else if(barcode.substring(0,3).equals("890")){
                        anim.setAnimation(R.raw.india);
                        country.setText("India");
                        descrip.setText("Congratulations! You are contributing towards Atmanirbhar Bharat");
                    }else
                    {
                        anim.setAnimation(R.raw.happy);
                        country.setText("Non Chinese");
                        descrip.setText("Congratulations! You are contributing towards Atmanirbhar Bharat");
                    }

                    finish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                    scan_again.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            scancode();
                        }
                    });
                    dialog.show();
                }else{
                    Toast.makeText(this,"Invalid, Please scan again",Toast.LENGTH_SHORT).show();
                    scancode();

                }
                }



            else {
                //Toast.makeText(this, "No result", Toast.LENGTH_SHORT).show();
            }

        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {

        if(backPressedTime+2000>System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else
            Toast.makeText(getBaseContext(),"Press back again to exit",Toast.LENGTH_SHORT).show();
        backPressedTime=System.currentTimeMillis();
    }
}