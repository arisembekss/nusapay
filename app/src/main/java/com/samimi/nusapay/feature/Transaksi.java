package com.samimi.nusapay.feature;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.samimi.nusapay.configuration.Config;
import com.samimi.nusapay.configuration.RequestHandler;
import com.samimi.nusapay.R;
import com.samimi.nusapay.custom.CustomDialog;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by aris on 27/11/16.
 */

public class Transaksi extends AsyncTask<Void, Void, String> {

    String kode;
    String user;
    String jenisTransaksi;
    String NomorTuj;
    String firebaseId;
    ProgressDialog progress;
    Context context;
    private DatabaseReference mDatabase;
// ...

    public Transaksi(Context context) {
        //this.kode = kode;
        this.context = context;
        //Intent addSaldo = new Intent(context, AddSaldoActivity.class);

    }

    public String getJenisTransaksi() {
        return jenisTransaksi;
    }

    public void setJenisTransaksi(String jenisTransaksi) {
        this.jenisTransaksi = jenisTransaksi;
    }

    public String getNomorTuj() {
        return NomorTuj;
    }

    public void setNomorTuj(String nomorTuj) {
        NomorTuj = nomorTuj;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }
    RequestHandler reqHandler;

    @Override
    protected String doInBackground(Void... params) {
        HashMap<String, String> paramsTransaksi = new HashMap<>();
        paramsTransaksi.put(Config.TRX_PULSA_EMAIL, user);
        paramsTransaksi.put(Config.TRX_PULSA_FORMATTRX, kode);
        paramsTransaksi.put(Config.TRX_PULSA_FIREBASEID, firebaseId);
        paramsTransaksi.put(Config.TRX_PULSA_KODE, jenisTransaksi);
        paramsTransaksi.put(Config.TRX_PULSA_NOMORTUJ, NomorTuj);

        reqHandler = new RequestHandler();
        String res = reqHandler.sendPostRequest(Config.TRX_URL, paramsTransaksi);

        return res;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(context, "Processing...", "Wait....", false, false);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (reqHandler.getStatus() == 0) {
            new CustomDialog().makeDialog(context, "Ooopss", context.getString(R.string.dialog_title_connection_trouble) , "koneksi");
        }
        //progress.dismiss();
        JSONObject jsonObject = null;
        //String keterangan="";
        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            String keterangan = "";
            String saldo = "";
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                keterangan = jo.getString(Config.TAG_KETERANGAN);
                saldo = jo.getString(Config.TAG_KETERANGAN_SALDO);
            }

            if (keterangan.contains("saldo")) {
                progress.dismiss();
                new CustomDialog().makeDialog(context, "Saldo", context.getString(R.string.dialog_title_saldo)+" ("+saldo+")" , "saldo");
            } else if (keterangan.matches("sukses")){
                Toast.makeText(context, "Transaksi anda sedang diproses", Toast.LENGTH_SHORT).show();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        progress.dismiss();

    }
}
