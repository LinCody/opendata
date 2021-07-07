package tw.cody.newtaipei;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.InterstitialAd;


public class newtaipei extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newtaipei);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

//        load();
    }

    private void load() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7237500042368815/5264815026");
        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
//                Toast.makeText(MainActivity.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                if(mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
//                Toast.makeText(MainActivity.this, "onAdFailedToLoad()", Toast.LENGTH_SHORT).show();
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

    public void newtaipei_pp(View view) {
        Intent intent = new Intent(newtaipei.this,newtaipei_pp.class);
        startActivity(intent);
    }

    public void newtaipei_yb(View view) {
        Intent intent = new Intent(newtaipei.this,newtaipeiyb.class);
        startActivity(intent);
    }

    public void newtaipei_rp(View view) {
        Intent intent = new Intent(newtaipei.this,newtaipeirp.class);
        startActivity(intent);
    }

    public void newtaipeirtp(View view) {
        Intent intent = new Intent(newtaipei.this,newtaipeirtp.class);
        startActivity(intent);
    }

    public void newtaipei_107A2(View view) {
        Intent intent = new Intent(newtaipei.this,newtaipei107a2.class);
        startActivity(intent);
    }

    public void newtaipei_106A2(View view) {
        Intent intent = new Intent(newtaipei.this,newtaipei106a2.class);
        startActivity(intent);
    }

    public void newtaipei_105A1(View view) {
        Intent intent = new Intent(newtaipei.this,newtaipei105a1.class);
        startActivity(intent);
    }

    public void newtaipei_105A2(View view) {
        Intent intent = new Intent(newtaipei.this,newtaipei105a2.class);
        startActivity(intent);
    }

    public void newtaipei_104A1(View view) {
        Intent intent = new Intent(newtaipei.this,newtaipei104a1.class);
        startActivity(intent);
    }

    public void newtaipei_104A2(View view) {
        Intent intent = new Intent(newtaipei.this,newtaipei104a2.class);
        startActivity(intent);
    }
}
