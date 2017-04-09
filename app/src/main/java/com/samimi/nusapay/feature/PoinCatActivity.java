package com.samimi.nusapay.feature;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.samimi.nusapay.configuration.Config;
import com.samimi.nusapay.R;

public class PoinCatActivity extends AppCompatActivity implements View.OnClickListener {

    WebView weba, webb, webc, webd, webe, webf;
    Button bpoina, bpoinb, bpoinc, bpoind, bpoine, bpoinf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poin_cat);

        initUi();
    }

    private void initUi() {
        weba = (WebView) findViewById(R.id.weba);
        webb = (WebView) findViewById(R.id.webb);
        webc = (WebView) findViewById(R.id.webc);
        webd = (WebView) findViewById(R.id.webd);
        webe = (WebView) findViewById(R.id.webe);
        webf = (WebView) findViewById(R.id.webf);

        weba.loadUrl(Config.WEBA);
        webb.loadUrl(Config.WEBB);
        webc.loadUrl(Config.WEBC);
        webd.loadUrl(Config.WEBD);
        webe.loadUrl(Config.WEBE);
        webf.loadUrl(Config.WEBF);

        bpoina = (Button) findViewById(R.id.bpoina);
        bpoinb = (Button) findViewById(R.id.bpoinb);
        bpoinc = (Button) findViewById(R.id.bpoinc);
        bpoind = (Button) findViewById(R.id.bpoind);
        bpoine = (Button) findViewById(R.id.bpoine);
        bpoinf = (Button) findViewById(R.id.bpoinf);

        bpoina.setOnClickListener(this);
        bpoinb.setOnClickListener(this);
        bpoinc.setOnClickListener(this);
        bpoind.setOnClickListener(this);
        bpoine.setOnClickListener(this);
        bpoinf.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bpoina:
                weba.setVisibility(View.VISIBLE);
                webb.setVisibility(View.GONE);
                webc.setVisibility(View.GONE);
                webd.setVisibility(View.GONE);
                webe.setVisibility(View.GONE);
                webf.setVisibility(View.GONE);
                break;
            case R.id.bpoinb:
                weba.setVisibility(View.GONE);
                webb.setVisibility(View.VISIBLE);
                webc.setVisibility(View.GONE);
                webd.setVisibility(View.GONE);
                webe.setVisibility(View.GONE);
                webf.setVisibility(View.GONE);
                break;
            case R.id.bpoinc:
                weba.setVisibility(View.GONE);
                webb.setVisibility(View.GONE);
                webc.setVisibility(View.VISIBLE);
                webd.setVisibility(View.GONE);
                webe.setVisibility(View.GONE);
                webf.setVisibility(View.GONE);
                break;
            case R.id.bpoind:
                weba.setVisibility(View.GONE);
                webb.setVisibility(View.GONE);
                webc.setVisibility(View.GONE);
                webd.setVisibility(View.VISIBLE);
                webe.setVisibility(View.GONE);
                webf.setVisibility(View.GONE);
                break;
            case R.id.bpoine:
                weba.setVisibility(View.GONE);
                webb.setVisibility(View.GONE);
                webc.setVisibility(View.GONE);
                webd.setVisibility(View.GONE);
                webe.setVisibility(View.VISIBLE);
                webf.setVisibility(View.GONE);
                break;
            case R.id.bpoinf:
                weba.setVisibility(View.GONE);
                webb.setVisibility(View.GONE);
                webc.setVisibility(View.GONE);
                webd.setVisibility(View.GONE);
                webe.setVisibility(View.GONE);
                webf.setVisibility(View.VISIBLE);
                break;
        }
    }
}
