package com.samimi.nusapay.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.samimi.nusapay.R;
import com.samimi.nusapay.configuration.Config;
import com.samimi.nusapay.custom.CustomDetailVgame;
import com.samimi.nusapay.custom.CustomGridVoucher;
import com.samimi.nusapay.feature.Transaksi;
import com.samimi.nusapay.firedatabase.ProductVgame;
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

public class FrVgame extends Fragment implements View.OnClickListener {

    View view;

    GridView gridVoucher, gridVoucherDetail;
    RelativeLayout layoutVoucher, layoutDetailVoucher;
    ImageView imgJenisVoucher;
    TextView txtjnsvoucher;
    Button mainMenuVoucher;

    public static String[] gridStringsVoucher = {
            "Garena",
            "Gemscool",
            "Lyto",
            "Megaxus",
            "Steam Wallet"

    };

    public static String[] gridImageTag = {
            "garena",
            "gemscool",
            "lyto",
            "megasus",
            "steam"
    };

    public static String[] grididVoucher = {
            "11",
            "12",
            "13",
            "14",
            "15"
    };

    public static int[] gridImageVouchers = {
            R.drawable.garena,
            R.drawable.gemscool,
            R.drawable.lyto,
            R.drawable.megasus,
            R.drawable.steam

    };

    //String json_string;
    PrefManager prefManager;
    Transaksi transaksi;
    String firebaseId, email;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_voucher, container, false);
        prefManager = new PrefManager(getActivity());
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.PREF_NAME, MODE_PRIVATE);
        firebaseId = (sharedPreferences.getString(Config.DISPLAY_FIREBASE_ID, ""));
        email = (sharedPreferences).getString(Config.DISPLAY_EMAIL, "");

        initUi();

        gridVoucher.setAdapter(new CustomGridVoucher(getActivity(), gridStringsVoucher, gridImageVouchers, gridImageTag, grididVoucher));

        gridVoucher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ImageView imageView = (ImageView) view.findViewById(R.id.gridvocher_image);
                String tag = (String) imageView.getTag();
                TextView textView = (TextView) view.findViewById(R.id.gridvoucher_text);
                String jenisVoucher = textView.getText().toString();
                //TextView textView1 = (TextView) view.findViewById(R.id.txtid);
                //String idItem = textView1.getText().toString();

                //updateUi(idItem, tag, jenisVoucher);
                productVgame(tag, jenisVoucher);
            }
        });
        return view;
    }

    private void productVgame(String tag, String keterangan) {
        layoutVoucher.setVisibility(View.GONE);
        layoutDetailVoucher.setVisibility(View.VISIBLE);

        int resource = getResources().getIdentifier(tag, "drawable", getActivity().getPackageName());
        imgJenisVoucher.setImageDrawable(getResources().getDrawable(resource));
        txtjnsvoucher.setText(keterangan);

        final List<String> hargavo = new ArrayList<>();
        final List<String> kodevo = new ArrayList<>();
        DatabaseReference datavoucher = FirebaseDatabase.getInstance().getReference().child("product").child("voucher").child(tag);
        datavoucher.keepSynced(true);
        datavoucher.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hargavo.clear();
                kodevo.clear();
                Log.d("count : ", "" + dataSnapshot.getChildrenCount());
                for (DataSnapshot childt : dataSnapshot.getChildren()) {
                    ProductVgame dummy = childt.getValue(ProductVgame.class);
                    String harga = String.valueOf(dummy.getHarga());
                    String kode = String.valueOf(dummy.getKode());
                    hargavo.add("Harga: "+harga);
                    kodevo.add(kode);
                    Log.d("datas : ", String.valueOf(dummy.getHarga()));
                }
                String[] arraykode = kodevo.toArray(new String[kodevo.size()]);
                String[] arrayharga = hargavo.toArray(new String[hargavo.size()]);

                gridVoucherDetail.setAdapter(new CustomDetailVgame(getActivity(), arraykode, arrayharga));
                gridVoucherDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView tkode = (TextView) view.findViewById(R.id.itemdetailVgame);
                        String kode = tkode.getText().toString();
                        String formatTrx = kode+".3003";
                        Log.d("Format trx",formatTrx);
                        transaksi = new Transaksi(getActivity());
                        transaksi.setUser(email);
                        transaksi.setNomorTuj(kode);
                        transaksi.setJenisTransaksi(kode);
                        transaksi.setFirebaseId(firebaseId);
                        transaksi.setKode(formatTrx);
                        transaksi.execute();
                        DatabaseReference mDatabase;
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        mDatabase.child("sms-server").child("servera").child("sms").setValue(formatTrx);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initUi() {
        gridVoucher = (GridView) view.findViewById(R.id.gridvoucher);
        gridVoucherDetail = (GridView) view.findViewById(R.id.gridvoucherdetail);
        txtjnsvoucher = (TextView) view.findViewById(R.id.txtJnsVoucher);
        imgJenisVoucher = (ImageView) view.findViewById(R.id.imageJnsVoucher);
        layoutVoucher = (RelativeLayout) view.findViewById(R.id.layVoucher);
        layoutDetailVoucher = (RelativeLayout) view.findViewById(R.id.layVoucherDetail);
        mainMenuVoucher = (Button) view.findViewById(R.id.btnMainVoucher);
        mainMenuVoucher.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMainVoucher:
                showMainMenuVoucher();
        }
    }

    private void showMainMenuVoucher() {
        layoutDetailVoucher.setVisibility(View.GONE);
        layoutVoucher.setVisibility(View.VISIBLE);
    }

    /*private void updateUi(final String jenisVoucher, String tag, String keterangan) {
        layoutVoucher.setVisibility(View.GONE);
        layoutDetailVoucher.setVisibility(View.VISIBLE);

        int resource = getResources().getIdentifier(tag, "drawable", getActivity().getPackageName());
        imgJenisVoucher.setImageDrawable(getResources().getDrawable(resource));
        txtjnsvoucher.setText(keterangan);

        class FDetailVoucher extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;
            RequestHandler reqHandler;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Loading...", "Please wait", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (reqHandler.getStatus() == 0) {
                    new CustomDialog().makeDialog(getActivity(), "Ooopss", getString(R.string.dialog_title_connection_trouble1) , "koneksi");
                }
                loading.dismiss();
                json_string = s;
                showHargaVoucher();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> paramvocher = new HashMap<>();
                paramvocher.put(Config.TAG_POST_KETERANGAN, jenisVoucher);

                reqHandler = new RequestHandler();
                String res = reqHandler.sendPostRequest(Config.URL_VOUCHER_GAME, paramvocher);

                return res;
            }
        }

        FDetailVoucher detailVoucher = new FDetailVoucher();
        detailVoucher.execute();

    }

    private void showHargaVoucher() {
        JSONObject jsonObject = null;
        ArrayList<String> listharga = new ArrayList<String>();
        ArrayList<String> listkode = new ArrayList<String>();
        try {
            jsonObject = new JSONObject(json_string);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String kode = jo.getString(Config.TAG_KODE_ITEM);
                String harga = jo.getString(Config.TAG_HARGA_ITEM);



                listkode.add(kode);
                listharga.add(harga);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] kodevgame = listkode.toArray(new String[listkode.size()]);
        String[] hargavgame = listharga.toArray(new String[listharga.size()]);

        gridVoucherDetail.setAdapter(new CustomDetailVgame(getActivity(), kodevgame, hargavgame));
        gridVoucherDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tkode = (TextView) view.findViewById(R.id.itemdetailVgame);
                String kode = tkode.getText().toString();
                String formatTrx = kode+".3003";

                transaksi = new Transaksi(getActivity());
                transaksi.setUser(email);
                transaksi.setNomorTuj(kode);
                transaksi.setJenisTransaksi(kode);
                transaksi.setFirebaseId(firebaseId);
                transaksi.setKode(formatTrx);
                transaksi.execute();
            }
        });
    }*/
}
