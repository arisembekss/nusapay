package com.samimi.nusapay.data;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.samimi.nusapay.AddSaldoActivity;
import com.samimi.nusapay.configuration.Config;
import com.samimi.nusapay.configuration.RequestHandler;
import com.samimi.nusapay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by aris on 16/12/16.
 */

public class TagihanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<DataInbox> data = Collections.emptyList();
    DataInbox current;
    int currentPos = 0;

    public TagihanAdapter(Context context, List<DataInbox> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_item_tagihan, parent, false);
        MyHolder holder = new MyHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        String tagihan;
        MyHolder myHolder = (MyHolder) holder;
        DataInbox current = data.get(position);
        myHolder.detail.setText(current.mes);
        myHolder.idtagihan.setText(current.ket);
        myHolder.ketag.setText(current.ketag);
        myHolder.jenis.setText(current.jenis+" "+current.ketag);
        myHolder.idtagih.setText(current.idTagihan);

        //tagihan =
        if (current.jenis.matches("Tagihan")) {
            myHolder.bayar.setVisibility(View.VISIBLE);
        } else {
            myHolder.bayar.setVisibility(View.INVISIBLE);
        }

        if (current.ket.contains("Paid")) {
            myHolder.bayar.setEnabled(false);
            myHolder.bayar.setText("Paid");
        } /*else{
            myHolder.bayar.setVisibility(View.VISIBLE);
        }*/

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView detail, idtagihan, jenis, ketag, idtagih;
        Button hapus, bayar;
        String id_tagihan;

        public MyHolder(View itemView) {
            super(itemView);

            detail = (TextView) itemView.findViewById(R.id.txtItemTagihan);
            idtagihan = (TextView) itemView.findViewById(R.id.txtItemId);
            jenis = (TextView) itemView.findViewById(R.id.tagJenis);
            ketag = (TextView) itemView.findViewById(R.id.ketTag);
            idtagih = (TextView) itemView.findViewById(R.id.idTagih);

            hapus = (Button) itemView.findViewById(R.id.btnItemHapus);
            hapus.setOnClickListener(this);
            bayar = (Button) itemView.findViewById(R.id.btnItemBayar);
            bayar.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnItemHapus:
                    id_tagihan = idtagihan.getText().toString();
                    Toast.makeText(v.getContext(), "btn hapus clicked", Toast.LENGTH_SHORT).show();
                    asyncHapus(id_tagihan);
                    break;
                case R.id.btnItemBayar:
                    id_tagihan = idtagih.getText().toString();
                    //Toast.makeText(v.getContext(), "btn bayar clicked with id tagihan = "+id_tagihan, Toast.LENGTH_SHORT).show();
                    asyncBayar(id_tagihan);
                    break;
            }

        }

        private void asyncHapus(final String id_hapustagihan) {
            class HapusAsync extends AsyncTask<Void, Void, String> {

                @Override
                protected String doInBackground(Void... params) {
                    HashMap<String, String> paramsProvider = new HashMap<>();
                    paramsProvider.put(Config.POST_DELETE, "hapus");
                    paramsProvider.put(Config.TAG_ID_TAGIHAN, id_hapustagihan);



                    RequestHandler reqHandler = new RequestHandler();
                    String res = reqHandler.sendPostRequest(Config.URL_SHOW_TAGIHAN, paramsProvider);

                    return res;
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    //detail.setText("On Proccess Deleting");
                    bayar.setEnabled(false);
                    Toast.makeText(context, "Transaksi anda sedang di proses", Toast.LENGTH_SHORT).show();
                    //hapus.setEnabled(false);
                }
            }
            HapusAsync hapusAsync = new HapusAsync();
            hapusAsync.execute();
        }

        private void asyncBayar(final String id_tagihan) {

            class BayarAsync extends AsyncTask<Void, Void, String> {
                ProgressDialog progress;
                @Override
                protected String doInBackground(Void... params) {
                    HashMap<String, String> paramsProvider = new HashMap<>();
                    paramsProvider.put(Config.POST_BAYAR, "bayar");
                    paramsProvider.put(Config.TAG_ID_TAGIHAN, id_tagihan);



                    RequestHandler reqHandler = new RequestHandler();
                    String res = reqHandler.sendPostRequest(Config.URL_SHOW_TAGIHAN, paramsProvider);

                    return res;
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progress = ProgressDialog.show(context, "Processing...", "Wait....", false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    JSONObject jsonObject = null;
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
                            final Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.custom_dialog_keterangan);
                            dialog.setTitle("Saldo");
                            TextView tv = (TextView) dialog.findViewById(R.id.msgDialogKet);
                            tv.setText("Saldo anda tidak mencukupi -> "+saldo);
                            Button btnadd = (Button) dialog.findViewById(R.id.addBtn);
                            btnadd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    context.startActivity(new Intent(context, AddSaldoActivity.class));
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        } else if (keterangan.matches("sukses")){
                            Toast.makeText(context, "Transaksi anda sedang diproses", Toast.LENGTH_SHORT).show();
                            detail.setText("On Proccess");
                            bayar.setEnabled(false);
                            hapus.setEnabled(false);
                            progress.dismiss();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            BayarAsync bayarAsync = new BayarAsync();
            bayarAsync.execute();
        }
    }
}
