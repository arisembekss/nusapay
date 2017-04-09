package com.samimi.nusapay;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.samimi.nusapay.configuration.Config;
import com.samimi.nusapay.configuration.RequestHandler;
import com.samimi.nusapay.custom.CustomGridToken;
import com.samimi.nusapay.feature.Transaksi;
import com.samimi.nusapay.preference.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TokenActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //    String[] harga = new String[];
    Transaksi transaksi;
    PrefManager prefManager;
    String json_string, firebaseId, email;

    GridView gridToken;
    EditText edIdPelanggan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefManager = new PrefManager(this);
        SharedPreferences sharedPreferences = this.getSharedPreferences(Config.PREF_NAME, MODE_PRIVATE);
        firebaseId = (sharedPreferences.getString(Config.DISPLAY_FIREBASE_ID, ""));
        email = (sharedPreferences).getString(Config.DISPLAY_EMAIL, "");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initUi();

        getHargaToken();
    }

    private void initUi() {
        gridToken = (GridView) findViewById(R.id.gridToken);
        edIdPelanggan = (EditText) findViewById(R.id.idPelangganToken);
    }

    private void getHargaToken() {
        class THargaToken extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.URL_HARGA_TOKEN);
                return s;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                json_string = s;
                showHarga();
            }
        }

        THargaToken thargaToken= new THargaToken();
        thargaToken.execute();
    }

    private void showHarga() {
        JSONObject jsonObject = null;
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<String> listkode = new ArrayList<String>();
        try {
            jsonObject = new JSONObject(json_string);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String keterangan = jo.getString(Config.TAG_KETERANGAN_TOKEN);
                String harga = jo.getString(Config.TAG_HARGA_TOKEN);
                String kode = jo.getString(Config.TAG_KODE_TOKEN);
                String textBtnToken = keterangan + "\n harga: " + harga;


                listkode.add(kode);
                list.add(textBtnToken);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] kodetoken = listkode.toArray(new String[listkode.size()]);
        String[] hargaToken = list.toArray(new String[list.size()]);

        gridToken.setAdapter(new CustomGridToken(hargaToken, kodetoken, this));
        gridToken.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


        TextView tvkode = (TextView) view.findViewById(R.id.txtkodeToken);
        /*Button btnProses = (Button)view.findViewById()*/
        String kodetoken = tvkode.getText().toString();
        String idpel = edIdPelanggan.getText().toString();
        String formatTrx = "pln " + idpel + " " + kodetoken + " 3003";

        Toast.makeText(this, formatTrx, Toast.LENGTH_SHORT).show();

        /*transaksi = new Transaksi(this.getActivity());
        transaksi.setUser(email);
        transaksi.setNomorTuj(idpel);
        transaksi.setJenisTransaksi("Token Pln");
        transaksi.setFirebaseId(firebaseId);
        transaksi.setKode(formatTrx);
        transaksi.execute();*/
    }
}
