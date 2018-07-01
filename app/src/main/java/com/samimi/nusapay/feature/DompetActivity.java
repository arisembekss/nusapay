package com.samimi.nusapay.feature;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.samimi.nusapay.AddSaldoActivity;
import com.samimi.nusapay.configuration.Config;
import com.samimi.nusapay.configuration.RequestHandler;
import com.samimi.nusapay.R;
import com.samimi.nusapay.custom.CustomDialog;
import com.samimi.nusapay.preference.PrefManager;
import com.race604.drawable.wave.WaveDrawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DompetActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tsaldo, tpoin;
    EditText edTukarpoin;
    RadioButton radioAll, radioSome;
    RadioGroup rGroup;
    Button bTukar;
    RelativeLayout reldompet;
    String JSON_STRING;
    String status;
    String balance;
    String point;
    String txtEmail;
    Button bTukarPoin, bDetailPoin;
    AlertDialog alertDialog;
    PrefManager prefManager;
    SharedPreferences sharedPreferences;
    WaveDrawable waveDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dompet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initCollapsingToolbar();

        prefManager = new PrefManager(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(DompetActivity.this, AddSaldoActivity.class);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        iniUi();
        getJson();
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void getJson() {
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            RequestHandler rh;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DompetActivity.this,"Loading...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (rh.getStatus() == 0) {
                    new CustomDialog().makeDialog(DompetActivity.this, "Ooopss", getString(R.string.dialog_title_connection_trouble1) , "koneksi");
                }

                JSON_STRING = s;
                showCustomer(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_SELECT_ALL, txtEmail);
                return s;
            }
        }

        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void showCustomer(String json) {
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            status = c.getString(Config.TAG_STATUS);
            balance = c.getString(Config.TAG_BALANCE);
            point = c.getString(Config.TAG_POINT);

            if (point == "null") {
                point = "0";
            }

            tsaldo.setText(balance);
            tpoin.setText(point);
            prefManager.setPoin(point);

            //Toast.makeText(DompetActivity.this, status, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void iniUi() {

        waveDrawable = new WaveDrawable(this, R.drawable.circle_image);
        tsaldo = (TextView) findViewById(R.id.tsaldo);
        tpoin = (TextView) findViewById(R.id.tpoin);
        bTukarPoin = (Button) findViewById(R.id.bTukarPoin);
        bDetailPoin = (Button) findViewById(R.id.bdetaiPoin);
        bTukarPoin.setOnClickListener(this);
        bDetailPoin.setOnClickListener(this);

        sharedPreferences = getSharedPreferences(Config.PREF_NAME, MODE_PRIVATE);
        txtEmail = (sharedPreferences.getString(Config.DISPLAY_EMAIL, ""));
        reldompet = (RelativeLayout) findViewById(R.id.reldompet);
        reldompet.setBackground(waveDrawable);
        waveDrawable.setLevel(7000);
        waveDrawable.setWaveAmplitude(40);
        waveDrawable.setWaveSpeed(5);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bTukarPoin:
                tukarPoin();
                break;
            case R.id.bdetaiPoin:
                detailPoin();
        }
    }

    private void detailPoin() {
        Intent detail = new Intent(DompetActivity.this, PoinCatActivity.class);
        startActivity(detail);

    }

    private void tukarPoin() {


        AlertDialog.Builder builder = new AlertDialog.Builder(DompetActivity.this, R.style.MyDialogTheme);
        builder.setTitle("Tukar Poin");
        //builder.setMessage("Pilih Salah Satu");
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_1, null);

        builder.setView(dialogView);

        alertDialog = builder.create();
        //alertDialog.setTitle("Tukar Poin");

        alertDialog.show();
        rGroup = (RadioGroup) dialogView.findViewById(R.id.rGroup);
        radioAll = (RadioButton) dialogView.findViewById(R.id.radioAll);
        radioSome = (RadioButton) dialogView.findViewById(R.id.radioSome);
        edTukarpoin = (EditText) dialogView.findViewById(R.id.editPoin);
        bTukar = (Button) dialogView.findViewById(R.id.bTukar);

        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioAll:
                        edTukarpoin.setVisibility(View.GONE);
                        break;
                    case R.id.radioSome:
                        edTukarpoin.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        bTukar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = rGroup.getCheckedRadioButtonId();
                if (selectedId == radioAll.getId()) {
                    String allPoin = tpoin.getText().toString();
                    sendTukar(allPoin);
                    Toast.makeText(DompetActivity.this, allPoin, Toast.LENGTH_SHORT).show();
                } else if (selectedId == radioSome.getId()) {
                    String somePoin = edTukarpoin.getText().toString();
                    if (somePoin.matches("")) {
                        Toast.makeText(DompetActivity.this, "Input jumlah poin yang akan di tukar", Toast.LENGTH_SHORT).show();
                        edTukarpoin.requestFocus();
                    } else {
                        sendTukar(somePoin);
                        Toast.makeText(DompetActivity.this, somePoin, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

                getJson();
            }
        });
    }

    private void sendTukar(final String poin) {

        int currentPoint = Integer.parseInt(point);
        int changePoint = Integer.parseInt(poin);
        if (currentPoint-changePoint<0){
            Toast.makeText(DompetActivity.this, "jumlah poin yang akan di tukar tidak sesuai", Toast.LENGTH_SHORT).show();
        } else{
            class CheckpCustomer extends AsyncTask<Void, Void, String> {

                ProgressDialog loading;
                RequestHandler requestHandler;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    //loading = ProgressDialog.show(WelcomeActivity.this, "Proccessing ..", "Reading Server", false, false);

                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    /*if (requestHandler.getStatus() == 0) {
                        new CustomDialog().makeDialog(DompetActivity.this, "Ooopss", getString(R.string.dialog_title_connection_trouble1) , "koneksi");
                    }*/
                    //loading.dismiss();
                }


                @Override
                protected String doInBackground(Void... v) {

                    HashMap<String, String> params = new HashMap<>();
                    params.put(Config.POST_POINT, poin);
                    params.put(Config.TRX_PULSA_EMAIL, txtEmail);


                    requestHandler = new RequestHandler();
                    String result = requestHandler.sendPostRequest(Config.URL_UPD_POINT, params);

                    return result;
                }
            }
            CheckpCustomer checkCustomer = new CheckpCustomer();
            checkCustomer.execute();

            alertDialog.dismiss();
        }


    }


}
