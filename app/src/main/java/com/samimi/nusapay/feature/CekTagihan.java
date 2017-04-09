package com.samimi.nusapay.feature;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.samimi.nusapay.configuration.Config;
import com.samimi.nusapay.configuration.RequestHandler;
import com.samimi.nusapay.R;
import com.samimi.nusapay.preference.PrefManager;

import java.util.HashMap;

public class CekTagihan extends AppCompatActivity implements View.OnClickListener {

    TextView textTagihan;
    EditText cekNmrTagihan;
    Button buttonCek;
    Bundle extras;

    String jenisTagihan, trx, fbaseuid, email;

    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cek_tagihan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefManager = new PrefManager(CekTagihan.this);
        SharedPreferences sharedPreferences = this.getSharedPreferences(Config.PREF_NAME, MODE_PRIVATE);
        fbaseuid = (sharedPreferences.getString(Config.DISPLAY_FIREBASE_ID, ""));
        email = (sharedPreferences.getString(Config.DISPLAY_EMAIL, ""));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        extras=getIntent().getExtras();
        jenisTagihan = extras.getString("jenis");
        initUi();

    }

    private void initUi() {
        textTagihan = (TextView) findViewById(R.id.textTagihan);
        textTagihan.setText("Cek Tagihan "+jenisTagihan);

        cekNmrTagihan = (EditText) findViewById(R.id.cekNmrTagihan);
        cekNmrTagihan.setHint("ID Pelanggan anda");
        buttonCek = (Button) findViewById(R.id.buttonCek);
        buttonCek.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCek:
                trx = cekNmrTagihan.getText().toString();
                postCekTagihan();
        }
    }


    private void postCekTagihan() {

        class InsCekTagihan extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;
            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> paramsCekTagihan = new HashMap<>();
                //paramsCekTagihan.put(Config.FBASE_UID, userId);
                paramsCekTagihan.put(Config.NO_TAGIHAN, trx);
                paramsCekTagihan.put(Config.FBASE_UID, fbaseuid);
                paramsCekTagihan.put(Config.JENIS, jenisTagihan);
                paramsCekTagihan.put(Config.TAG_EMAIL_USER, email);

                RequestHandler reqHandler = new RequestHandler();
                String res = reqHandler.sendPostRequest(Config.URL_INSERT_TAGIHAN, paramsCekTagihan);



                return res;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(getActivity(), "Processing...", "Wait....", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //loading.dismiss();
                Toast.makeText(CekTagihan.this, "We'll Processing Your Request", Toast.LENGTH_SHORT).show();
            }
        }

        InsCekTagihan cekTagihan = new InsCekTagihan();
        cekTagihan.execute();
    }
}
