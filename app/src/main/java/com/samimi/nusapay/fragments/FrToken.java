package com.samimi.nusapay.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.samimi.nusapay.R;
import com.samimi.nusapay.configuration.Config;
import com.samimi.nusapay.custom.CustomGridToken;
import com.samimi.nusapay.feature.Transaksi;
import com.samimi.nusapay.firedatabase.ProductToken;
import com.samimi.nusapay.preference.PrefManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by aris on 01/01/17.
 */

public class FrToken extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher {

    View view;
    Transaksi transaksi;
    PrefManager prefManager;
    String firebaseId, email;
    ImageButton imgDone;
    GridView gridToken;
    EditText edIdPelanggan;
    InputMethodManager inputMethodManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_token, container, false);
        prefManager = new PrefManager(getActivity());
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.PREF_NAME, MODE_PRIVATE);
        firebaseId = (sharedPreferences.getString(Config.DISPLAY_FIREBASE_ID, ""));
        email = (sharedPreferences).getString(Config.DISPLAY_EMAIL, "");
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        initUI();
        //getHargaToken();
        return view;
    }

    private void initUI() {
        gridToken = (GridView) view.findViewById(R.id.gridToken);
        edIdPelanggan = (EditText) view.findViewById(R.id.idPelangganToken);
        edIdPelanggan.addTextChangedListener(this);
        imgDone = (ImageButton) view.findViewById(R.id.imgPintoken);
        imgDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edIdPelanggan.getText().toString().matches("")) {
                    Toast.makeText(getActivity(), "Isikan nomor anda terlebih dahulu", Toast.LENGTH_SHORT).show();
                } else {
                    inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    productToken();

                }
            }
        });
        //gridToken.setEnabled(false);
    }

    private void productToken() {

        final int SPLASH_TIME_OUT = 400;

        final List<String> hargaprovider = new ArrayList<>();
        final List<String> kodeprovider = new ArrayList<>();
        DatabaseReference dataprovider = FirebaseDatabase.getInstance().getReference().child("product").child("token");
        dataprovider.keepSynced(true);
        dataprovider.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hargaprovider.clear();
                kodeprovider.clear();
                Log.d("count : ", "" + dataSnapshot.getChildrenCount());
                for (DataSnapshot childt : dataSnapshot.getChildren()) {
                    ProductToken dummy = childt.getValue(ProductToken.class);
                    String harga = String.valueOf(dummy.getHarga());
                    String kode = String.valueOf(dummy.getKode());
                    String ket = String.valueOf(dummy.getKet());
                    String textBtnToken = ket + "\n Harga: " + harga;
                    hargaprovider.add(textBtnToken);
                    kodeprovider.add(kode);
                    Log.d("datas : ", String.valueOf(dummy.getHarga()));
                }
                String[] arraykode = kodeprovider.toArray(new String[kodeprovider.size()]);
                String[] arrayharga = hargaprovider.toArray(new String[hargaprovider.size()]);

                gridToken.setAdapter(new CustomGridToken(arrayharga, arraykode, getActivity()));
                gridToken.setOnItemClickListener(FrToken.this);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        gridToken.setVisibility(View.VISIBLE);
                    }
                }, SPLASH_TIME_OUT);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView tvkode = (TextView) view.findViewById(R.id.txtkodeToken);
        /*Button btnProses = (Button)view.findViewById()*/
        String tkodetoken = tvkode.getText().toString();
        String formatTrx;
        String kodetoken = tkodetoken.substring(3);
        String idpel = edIdPelanggan.getText().toString().trim();
        /*if (!gridToken.isEnabled()) {

        }*/
        if (idpel.matches("")) {
            Toast.makeText(getActivity(), "Silahkan isi nomor pelangga anda", Toast.LENGTH_SHORT).show();
            edIdPelanggan.requestFocus();
        } else {
            formatTrx = "pln " + idpel + " " + kodetoken + " 3003";
            Log.d("format trx", formatTrx);
            //Toast.makeText(getActivity(), formatTrx, Toast.LENGTH_SHORT).show();
        //}
            transaksi = new Transaksi(getActivity());
            transaksi.setUser(email);
            transaksi.setNomorTuj(idpel);
            transaksi.setJenisTransaksi(tkodetoken);
            transaksi.setFirebaseId(firebaseId);
            transaksi.setKode(formatTrx);
            transaksi.execute();
            DatabaseReference mDatabase;
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("sms-server").child("servera").child("sms").setValue(formatTrx);
            imgDone.setVisibility(View.INVISIBLE);
            gridToken.setVisibility(View.INVISIBLE);
            edIdPelanggan.setText("");
        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //gridToken.setEnabled(false);
        if (charSequence.length() >= 5) {
            imgDone.setVisibility(View.VISIBLE);
        } else {
            imgDone.setVisibility(View.INVISIBLE);
            gridToken.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //gridToken.setEnabled(true);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    /*private void getHargaToken() {
        class HargaToken extends AsyncTask<Void, Void, String> {
            RequestHandler rh;
            @Override
            protected String doInBackground(Void... voids) {
                rh = new RequestHandler();
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
                if (rh.getStatus() == 0) {
                    new CustomDialog().makeDialog(getActivity(), "Ooopss", getString(R.string.dialog_title_connection_trouble1) , "koneksi");
                }
                json_string = s;
                showHarga();
            }
        }

        HargaToken hargaToken= new HargaToken();
        hargaToken.execute();
    }*/

    /*private void showHarga() {
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

        gridToken.setAdapter(new CustomGridToken(hargaToken, kodetoken, getActivity()));
        gridToken.setOnItemClickListener(this);
    }*/
}
