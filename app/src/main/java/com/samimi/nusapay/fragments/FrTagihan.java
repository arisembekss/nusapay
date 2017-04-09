package com.samimi.nusapay.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.samimi.nusapay.configuration.Config;
import com.samimi.nusapay.configuration.ItemClickListener;
import com.samimi.nusapay.configuration.RequestHandler;
import com.samimi.nusapay.R;
import com.samimi.nusapay.custom.CustomDialog;
import com.samimi.nusapay.custom.CustomGridVoucher;
import com.samimi.nusapay.data.AdapterKota;
import com.samimi.nusapay.data.KotaAdapter;
import com.samimi.nusapay.preference.PrefManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Context.MODE_PRIVATE;

import java.util.HashMap;

/**
 * Created by aris on 10/12/16.
 */

public class FrTagihan extends Fragment implements View.OnClickListener, ItemClickListener {

    EditText edNmrTagihan;
    public TextView txtTagihan, txtjnsTagihan;
    Button btnCek, btnPay, btnMain;
    ProgressBar prgBar;
    public RelativeLayout laymainTagihan, laydetailTagihan;
    GridView gridView;
    View view;
    PrefManager prefManager;
    Dialog kotaDialog;
    String userId;
    String email;
    String name;
    String trx;
    public String getJnsTagihan() {
        return jnsTagihan;
    }
    public void setJnsTagihan(String jnsTagihan) {
        this.jnsTagihan = jnsTagihan;
    }
    String jnsTagihan;
    AdapterKota mAdapter;
    Dialog dialogtagihan;

    public static String[] gridViewStrings = {
            "PLN",
            "PDAM",
            "Telkom",
            "Orange TV",
            "Indovision",
            "Aora TV",
            "FIF",
            "Astra Credit\nCompany",
            "WOM Finance"

    };

    public static String[] gridid = {
            "PLN",
            "PDAM",
            "Telkom",
            "orange",
            "Indovision",
            "Aora",
            "FIF",
            "acc",
            "wom"

    };

    public static int[] gridViewImages = {
            R.drawable.pln,
            R.drawable.pdam,
            R.drawable.telkom,
            R.drawable.orangetv,
            R.drawable.indovision,
            R.drawable.aora,
            R.drawable.fif,
            R.drawable.acc,
            R.drawable.wom
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fr_tagihan, container, false);
        prefManager = new PrefManager(getActivity());
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.PREF_NAME, MODE_PRIVATE);
        name = (sharedPreferences.getString(Config.DISPLAY_NAME, ""));
        email = (sharedPreferences).getString(Config.DISPLAY_EMAIL, "");
        userId = (sharedPreferences).getString(Config.DISPLAY_FIREBASE_ID, "");

        initUI();


        return view;
    }

    @Override
    public void onClick(View view, String data) {
        //String kodeKota = KotaAdapter.kodeKota[position];
        setJnsTagihan(data);
        updateUi(data);
        kotaDialog.dismiss();
        Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();
    }


    private void pilihKota() {
        kotaDialog = new Dialog(getActivity());
        kotaDialog.setContentView(R.layout.dialog_kota);
        RecyclerView recyclerView = (RecyclerView) kotaDialog.findViewById(R.id.recKota);
        mAdapter = new AdapterKota(getActivity(), KotaAdapter.kodeKota, KotaAdapter.kota);
        mAdapter.setClickListener(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        kotaDialog.show();

    }



    public void updateUi(String jnsTagihan) {
        dialogtagihan = new Dialog(getActivity());
        dialogtagihan.setContentView(R.layout.custom_dialog_paket);
        dialogtagihan.setTitle("Konfirmasi");

        TextView ketPaket = (TextView) dialogtagihan.findViewById(R.id.ketPaket);
        ketPaket.setText("Masukkan Id pelanggan "+jnsTagihan+" anda");
        Button btagihdialog = (Button) dialogtagihan.findViewById(R.id.btnProsesPaket);
        btagihdialog.setText("Cek Tagihan");
        final EditText edNomorTagih = (EditText) dialogtagihan.findViewById(R.id.edNomorPaket);
        edNomorTagih.setInputType(InputType.TYPE_CLASS_TEXT);
        btagihdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trx = edNomorTagih.getText().toString();
                if (trx.matches("")) {
                    edNomorTagih.setHint("Mohon isi nomor tagihan anda");
                } else {
                    postCekTagihan();
                }
            }
        });
        dialogtagihan.show();
    }

    private void initUI() {
        laymainTagihan = (RelativeLayout) view.findViewById(R.id.layMainTagihan);
        laydetailTagihan = (RelativeLayout) view.findViewById(R.id.layDetailTagihan);
        gridView = (GridView) view.findViewById(R.id.gridtagihan);
        edNmrTagihan = (EditText) view.findViewById(R.id.edNmrTagihan);
        txtTagihan = (TextView) view.findViewById(R.id.txtTagihan);
        txtjnsTagihan = (TextView) view.findViewById(R.id.txtjenisTagihan);
        btnCek = (Button) view.findViewById(R.id.btnCek);
        btnPay = (Button) view.findViewById(R.id.btnPay);
        btnMain = (Button) view.findViewById(R.id.btnMainTagihan);
        prgBar = (ProgressBar) view.findViewById(R.id.prgBar);
        prgBar.setVisibility(View.INVISIBLE);

        CustomGridVoucher adapter = new CustomGridVoucher(getActivity(), gridViewStrings, gridViewImages, gridViewStrings, gridid);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.txtid);
                String pilihan = textView.getText().toString();

                if (pilihan.contains("PDAM")) {
                    //jnsTagihan = pilihan + " kota";
                    pilihKota();
                } else {
                    setJnsTagihan(pilihan);
                    updateUi(pilihan);
                }


            }
        });
        btnPay.setOnClickListener(this);
        btnCek.setOnClickListener(this);
        btnMain.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCek:
                //prgBar.setVisibility(View.VISIBLE);
                //trx = edNmrTagihan.getText().toString();
                if (trx.matches("")) {
                    final AlertDialog alertDialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(false);
                    builder.setTitle("Konfirmasi");
                    builder.setMessage("Mohon untuk mengisi nomor tagihan anda terlebih dahulu");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    cekTagihan();
                }

                break;
            case R.id.btnPay:
                bayarTagihan();
                break;
            case R.id.btnMainTagihan:
                backMenuTagihan();
                break;
        }
    }

    private void backMenuTagihan() {
        laydetailTagihan.setVisibility(View.GONE);
        laymainTagihan.setVisibility(View.VISIBLE);
        String trxa = edNmrTagihan.getText().toString();
        if (!trxa.matches("")) {
            edNmrTagihan.setText("");
        }
    }

    private void bayarTagihan() {

    }

    private void cekTagihan() {

        postCekTagihan();
        txtTagihan.setText("Transaksi sedang di proses\nAnda akan mendapat pemberitahuan jumlah tagihan tersebut");
        edNmrTagihan.setText("");
    }

    private void postCekTagihan() {

        final String formatTrx = jnsTagihan+" "+trx+" cek 3003";
        class InsCekTagihan extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            RequestHandler reqHandler;
            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> paramsCekTagihan = new HashMap<>();
                //paramsCekTagihan.put(Config.FBASE_UID, userId);
                paramsCekTagihan.put(Config.NO_TAGIHAN, trx);
                paramsCekTagihan.put(Config.FBASE_UID, userId);
                paramsCekTagihan.put(Config.JENIS, jnsTagihan);
                paramsCekTagihan.put(Config.TAG_EMAIL_USER, email);
                paramsCekTagihan.put(Config.FORMAT, formatTrx);

                reqHandler = new RequestHandler();
                String res = reqHandler.sendPostRequest(Config.URL_INSERT_TAGIHAN, paramsCekTagihan);

                return res;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Loading...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (reqHandler.getStatus() == 0) {
                    new CustomDialog().makeDialog(getActivity(), "Ooopss", getString(R.string.dialog_title_connection_trouble) , "koneksi");
                }
                Toast.makeText(getActivity(), "Processing...", Toast.LENGTH_SHORT).show();
                loading.dismiss();
                dialogtagihan.dismiss();
            }
        }

        InsCekTagihan cekTagihan = new InsCekTagihan();
        cekTagihan.execute();
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("sms-server").child("servera").child("sms").setValue(formatTrx);
        //Log.d("formatTrx : ", formatTrx);
    }

}
