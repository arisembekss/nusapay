package com.samimi.nusapay;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.samimi.nusapay.configuration.Config;
import com.samimi.nusapay.configuration.RequestHandler;
import com.samimi.nusapay.custom.CustomDialog;
import com.samimi.nusapay.preference.PrefManager;

import java.util.HashMap;

public class TrfConfirmActivity extends AppCompatActivity {

    PrefManager prefManager;
    private static final String PREF_NAME = "app-welcome";

    private static final String DISPLAY_EMAIL = "displayEmail";
    EditText nomorRek, namaRek, nominal;
    TextView txtRek, txtEmailUser;
    String strEmail, bank;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trf_confirm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intentButtonBank = getIntent();
        bank = intentButtonBank.getStringExtra("bank");
        prefManager = new PrefManager(this);
        SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        strEmail = (sp.getString(Config.DISPLAY_EMAIL, ""));
        initUI();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPostAddSaldo();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void sendPostAddSaldo() {
        final String email = txtEmailUser.getText().toString();
        final String noRekUser = nomorRek.getText().toString();
        final String namaRekUser = namaRek.getText().toString();
        final String rekTujuan = txtRek.getText().toString();
        final String jmlTrf = nominal.getText().toString();

        class AddSaldo extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;
            RequestHandler reqHandler;
            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> paramsAddSaldo = new HashMap<>();
                paramsAddSaldo.put(Config.TAG_EMAIL_USER, email);
                paramsAddSaldo.put(Config.TAG_NOREK_USER, noRekUser);
                paramsAddSaldo.put(Config.TAG_NAMAREK_USER, namaRekUser);
                paramsAddSaldo.put(Config.TAG_REK_TUJ, rekTujuan);
                paramsAddSaldo.put(Config.TAG_JML_TRF, jmlTrf);

                reqHandler = new RequestHandler();
                String res = reqHandler.sendPostRequest(Config.URL_ADD_SALDO, paramsAddSaldo);

                return res;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (reqHandler.getStatus() == 0) {
                    new CustomDialog().makeDialog(TrfConfirmActivity.this, "Ooopss", getString(R.string.dialog_title_connection_trouble) , "koneksi");
                }
                loading = ProgressDialog.show(TrfConfirmActivity.this, "Processing...", "Wait....", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(TrfConfirmActivity.this, "We'll Processing Your Request", Toast.LENGTH_LONG).show();
            }
        }
        if (email.matches("") || noRekUser.matches("") || namaRekUser.matches("") || jmlTrf.matches("")) {
            //Toast.makeText(TrfConfirmActivity.this, "Mohon lengkapi data terlebih dahulu",Toast.LENGTH_SHORT).show();
            final AlertDialog alertDialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(TrfConfirmActivity.this);
            builder.setTitle("Data Transfer");
            builder.setMessage("Mohon untuk melengkapi data-data terlebih dahulu");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertDialog = builder.create();
            alertDialog.show();
        } else {
            AddSaldo addSaldo = new AddSaldo();
            addSaldo.execute();
        }
    }

    private void initUI() {
        nomorRek = (EditText) findViewById(R.id.nomorRekUser);
        namaRek = (EditText) findViewById(R.id.namaRekUser);
        nominal = (EditText) findViewById(R.id.jumlahTransfer);
        txtEmailUser = (TextView) findViewById(R.id.txtEmailUser);
        txtRek = (TextView) findViewById(R.id.txtRek);

        txtEmailUser.setText(strEmail);
        txtRek.setText(bank);
    }

}
