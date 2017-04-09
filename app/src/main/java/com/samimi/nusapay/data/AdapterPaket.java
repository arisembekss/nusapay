package com.samimi.nusapay.data;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.samimi.nusapay.configuration.Config;
import com.samimi.nusapay.R;
import com.samimi.nusapay.feature.Transaksi;
import com.samimi.nusapay.preference.PrefManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by aris on 31/12/16.
 */

public class AdapterPaket extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context context;
    private LayoutInflater inflater;
    List<DataPaket> data = Collections.emptyList();
    DataPaket current;
    int currentPos = 0;

    public AdapterPaket(Context context, List<DataPaket> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_item_paketdata, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        DataPaket current = data.get(position);
        myHolder.keterangan.setText(current.keterangan);
        myHolder.kode.setText(current.kode);
        myHolder.harga.setText(current.harga);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView keterangan, kode, harga;
        Button btnBuy;
        Dialog dialogBuy;
        String keteranganPaket, kodePaket, detailPaket, hargaPaket;
        Transaksi transaksiPulsa;
        PrefManager prefManager;

        public MyHolder(View itemView) {
            super(itemView);

            keterangan = (TextView) itemView.findViewById(R.id.detailketeranganPaket);
            kode = (TextView) itemView.findViewById(R.id.detailkodePaket);
            harga = (TextView) itemView.findViewById(R.id.detailhargaPaket);
            btnBuy = (Button) itemView.findViewById(R.id.btnBuyPaket);
            btnBuy.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            keteranganPaket = keterangan.getText().toString();
            kodePaket = kode.getText().toString();
            hargaPaket = harga.getText().toString();
            detailPaket = "Anda akan membeli paket :\n" + keteranganPaket + "(" + kodePaket + ")\nHarga: " + hargaPaket;
            buyPaket();
        }

        private void buyPaket() {

            prefManager = new PrefManager(context);
            SharedPreferences sharedPreferences = context.getSharedPreferences(Config.PREF_NAME, MODE_PRIVATE);
            final String firebaseId = (sharedPreferences.getString(Config.DISPLAY_FIREBASE_ID, ""));
            final String email = (sharedPreferences).getString(Config.DISPLAY_EMAIL, "");

            dialogBuy = new Dialog(context);
            dialogBuy.setContentView(R.layout.custom_dialog_paket);
            dialogBuy.setTitle("Konfirmasi");

            TextView ketPaket = (TextView) dialogBuy.findViewById(R.id.ketPaket);
            ketPaket.setText(detailPaket);
            Button bbuyPaket = (Button) dialogBuy.findViewById(R.id.btnProsesPaket);
            final EditText edNomorPaket = (EditText) dialogBuy.findViewById(R.id.edNomorPaket);


            bbuyPaket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                       String nomorPaket = edNomorPaket.getText().toString();
                    String formatTrx = kodePaket + "." + nomorPaket + ".3003";
                    if (nomorPaket.matches("")) {
                        Toast.makeText(context, "Silahkan isi nomor anda", Toast.LENGTH_SHORT).show();
                        edNomorPaket.requestFocus();
                    } else {
                        //Toast.makeText(context, kodePaket+"."+nomorPaket+".3003", Toast.LENGTH_SHORT).show();
                        transaksiPulsa = new Transaksi(context);
                        transaksiPulsa.setUser(email);
                        transaksiPulsa.setNomorTuj(nomorPaket);
                        transaksiPulsa.setJenisTransaksi(kodePaket);
                        transaksiPulsa.setFirebaseId(firebaseId);
                        transaksiPulsa.setKode(formatTrx);
                        transaksiPulsa.execute();
                        DatabaseReference mDatabase;
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        mDatabase.child("sms-server").child("servera").child("sms").setValue(formatTrx);
                        dialogBuy.dismiss();
                    }



                }
            });
            dialogBuy.show();
        }
    }
}
