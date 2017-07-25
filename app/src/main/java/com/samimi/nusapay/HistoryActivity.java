package com.samimi.nusapay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.samimi.nusapay.configuration.Config;
import com.samimi.nusapay.custom.CustomDialog;
import com.samimi.nusapay.data.DataPaket;
import com.samimi.nusapay.data.DataPul;
import com.samimi.nusapay.data.DataTa;
import com.samimi.nusapay.data.DataTo;
import com.samimi.nusapay.data.DataVo;
import com.samimi.nusapay.data.MyPercentFormatter;
import com.samimi.nusapay.feature.DetailHistActivity;
import com.samimi.nusapay.preference.PrefManager;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "Volley";
    Button bhistTr, bhistTagih;
    WebView webHistTr, webhistTagih;

    PrefManager prefManager;
    SharedPreferences sharedPreferences;
    String sharedResponse;
    String idUsr;

    PieChart mChart;
    //private int[] yData ;
    private String[] xData = { "Pulsa", "Token", "Tagihan", "Voucher"};
    int a = 0;//referensi jumlah data pulsa awal
    int b = 0;//referensi jumlah data token awal
    int c = 0;//referensi jumlah data tagihan awal
    int d = 0;//referensi jumlah data voucher awal
    /*json variables*/
    List<DataPul> datapul = new ArrayList<>();
    List<DataTo> datato = new ArrayList<>();
    List<DataTa> datata = new ArrayList<>();
    List<DataVo> datavo = new ArrayList<>();
    String lastdatesaldo, lastsaldo, currentsaldo, totaltrx, totalpulsa, totaltoken, totaltagihan, totalvoucher,
            jmltrx, jmlpulsa, jmltoken, jmltagihan, jmlvoucher;
    TextView tlastdatesaldo, tcurrentsaldo, ttotaltransaksi, totalspend, ttotaltrxpulsa, ttotaltrxtoken,
            ttotaltrxtagihan, ttotaltrxvoucher, tdetailpulsa, tdetailtoken, tdetailtagihan, tdetailvoucher;

    Button btndetailpulsa, btnDetailToken, btnDetailTagihan;
    //String[] kodeP;

    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_history);

        initUi();
        getDataHist();
    }

    private void getDataHist() {
        loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);
        String url = Config.URL_HIST_TRX_NEW + idUsr;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
                sharedResponse = response;
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(HistoryActivity.this,error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        loading.dismiss();
                        new CustomDialog().makeDialog(HistoryActivity.this, "Ooopss", getString(R.string.dialog_title_connection_trouble1) , "koneksi");
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {

        List<DataPaket> kodepulsa = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject data = result.getJSONObject(0);
            lastdatesaldo = data.getString(Config.HIST_DATA_LASTDATESALDO);
            currentsaldo = data.getString(Config.HIST_DATA_CURRENTSALDO);
            totaltrx = data.getString(Config.HIST_DATA_TOTALTRX);
            totalpulsa = data.getString(Config.HIST_DATA_TOTALPULSA);
            totaltoken = data.getString(Config.HIST_DATA_TOTALTOKEN);
            totaltagihan = data.getString(Config.HIST_DATA_TOTALTAGIHAN);
            totalvoucher = data.getString(Config.HIST_DATA_TOTALVOUCHER);

            tlastdatesaldo.setText(lastdatesaldo);
            tcurrentsaldo.setText(currentsaldo);
            //ttotaltransaksi.setText(totaltrx);
            totalspend.setText(totaltrx);
            tdetailpulsa.setText(totalpulsa);
            tdetailtagihan.setText(totaltagihan);
            tdetailtoken.setText(totaltoken);
            tdetailvoucher.setText(totalvoucher);
            /*================*/
            JSONArray detail = data.getJSONArray(Config.ARRAY_HIST_DETAIL);
            JSONObject detaildata = detail.getJSONObject(0);

            /*jsaon array Pulsa*/
            JSONArray pulsa = detaildata.getJSONArray(Config.ARRAY_HIST_DETAIL_PULSA);
            JSONArray token = detaildata.getJSONArray(Config.ARRAY_HIST_DETAIL_TOKEN);
            JSONArray tagihan = detaildata.getJSONArray(Config.ARRAY_HIST_DETAIL_TAGIHAN);
            JSONArray voucher = detaildata.getJSONArray(Config.ARRAY_HIST_DETAIL_VOUCHER);
            jmlpulsa = (pulsa.length()==0)?"0":Integer.toString(pulsa.length());
            ttotaltrxpulsa.setText("("+jmlpulsa+" transaksi)");
            a = pulsa.length();
            jmltoken = (token.length()==0)?"0": Integer.toString(token.length());
            ttotaltrxtoken.setText("("+jmltoken+" transaksi)");
            b = token.length();
            jmltagihan = (tagihan.length()==0)?"0":Integer.toString(tagihan.length());
            ttotaltrxtagihan.setText("("+jmltagihan+" transksi)");
            c = tagihan.length();
            jmlvoucher = (voucher.length()==0)?"0": Integer.toString(voucher.length());
            ttotaltrxvoucher.setText("("+jmlvoucher+" transaksi)");
            d = voucher.length();


            float[] yData = {Float.parseFloat(jmlpulsa), Float.parseFloat(jmltoken), Float.parseFloat(jmltagihan), Float.parseFloat(jmlvoucher)};

            List<PieEntry> entries = new ArrayList<>();


            for (int i=0;i<yData.length;i++) {
                entries.add(new PieEntry(yData[i], xData[i]));
            }

            ArrayList<Integer> colors = new ArrayList<Integer>();

            for (int c : ColorTemplate.MATERIAL_COLORS)
                colors.add(c);

            /*if (!entries.contains(0.0)) {

            }*/
            PieDataSet set = new PieDataSet(entries, "");
            set.setValueFormatter(new MyPercentFormatter());
            set.setColors(colors);
            PieData datax = new PieData(set);
            mChart.setData(datax);
            mChart.invalidate();

            jmltrx = Integer.toString(pulsa.length() + token.length() + tagihan.length() + voucher.length());
            ttotaltransaksi.setText(jmltrx);

            /*json array tagihan*/
            for (int k = 0; k < tagihan.length(); k++) {
                JSONObject jotagih = tagihan.getJSONObject(k);
                DataTa datatagihan = new DataTa();
                datatagihan.dateTa = jotagih.getString("date");
                datatagihan.jnsTa = jotagih.getString("jenis_tagihan");
                datatagihan.jmlTa = jotagih.getString("jumlah_tagihan");
                datatagihan.nomorTa= jotagih.getString("nomor_tujuan");
                datata.add(datatagihan);
            }
            /*jsaon array Voucher*/
            for (int j = 0; j < voucher.length(); j++) {
                JSONObject jovoucher = voucher.getJSONObject(j);
                DataVo datavoucher = new DataVo();
                datavoucher.dateVo = jovoucher.getString("date");
                datavoucher.kodeVo = jovoucher.getString("kode");
                datavoucher.hargaVo = jovoucher.getString("harga");
                datavoucher.nomorVo= jovoucher.getString("nomor_tujuan");
                datavo.add(datavoucher);

            }
            //Log.d(TAG, "showJSON: "+kodepulsa);
            /*StringBuilder builder = new StringBuilder();
            for (int a =0 ;a < kodeTo.length; a++) {
                builder.append(kodeTo[a] + "\n");
            }
            Toast.makeText(HistoryActivity.this, builder+"\n"+kodeTo.length, Toast.LENGTH_LONG).show();*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initUi() {

        sharedPreferences = getSharedPreferences(Config.PREF_NAME, MODE_PRIVATE);
        idUsr = (sharedPreferences.getString(Config.DISPLAY_IDUSR, ""));
        bhistTr = (Button) findViewById(R.id.bhistTr);
        bhistTagih = (Button) findViewById(R.id.bhistTagih);
        bhistTr.setOnClickListener(this);
        bhistTagih.setOnClickListener(this);
        tlastdatesaldo = (TextView) findViewById(R.id.lastdatesaldo);
        tcurrentsaldo = (TextView) findViewById(R.id.currentsaldo);
        ttotaltransaksi = (TextView) findViewById(R.id.totalTransaksi);
        totalspend = (TextView) findViewById(R.id.totalSpend);
        tlastdatesaldo = (TextView) findViewById(R.id.lastdatesaldo);
        ttotaltrxpulsa = (TextView) findViewById(R.id.tTotaltrxPulsa);
        ttotaltrxtagihan = (TextView) findViewById(R.id.tTotaltrxTagihan);
        ttotaltrxtoken = (TextView) findViewById(R.id.tTotaltrxToken);
        ttotaltrxvoucher = (TextView) findViewById(R.id.tTotaltrxVoucher);
        tdetailpulsa = (TextView) findViewById(R.id.tdetailpulsa);
        tdetailtoken = (TextView) findViewById(R.id.tdetailtoken);
        tdetailtagihan = (TextView) findViewById(R.id.tdetailtagihan);
        tdetailvoucher = (TextView) findViewById(R.id.tdetailvoucher);

        btndetailpulsa = (Button) findViewById(R.id.btndetailpulsa);
        btnDetailToken = (Button) findViewById(R.id.btndetailtoken);
        btnDetailTagihan = (Button) findViewById(R.id.btndetailtagihan);

        btndetailpulsa.setOnClickListener(this);
        btnDetailToken.setOnClickListener(this);
        btnDetailTagihan.setOnClickListener(this);
        mChart = (PieChart) findViewById(R.id.piechart);
        //mChart.setDescription(new Description().setText("Analisa Transaksi"));

        // enable hole and configure
        mChart.setDrawHoleEnabled(true);
        mChart.setUsePercentValues(true);
        mChart.setDrawSliceText(false);
        //mChart.setHoleColorTransparent(true);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bhistTr:
                showHistTr();
                break;
            case R.id.bhistTagih:
                showHistTagih();
                break;
            case R.id.btndetailpulsa:
                if (a == 0) {
                    Toast.makeText(HistoryActivity.this, "Tidak Ada Transaksi", Toast.LENGTH_SHORT).show();
                } else {
                    showDetail();
                }
                break;
            case R.id.btndetailtoken:
                if (b == 0) {
                    Toast.makeText(HistoryActivity.this, "Tidak Ada Transaksi", Toast.LENGTH_SHORT).show();
                } else {
                    showDetailToken();
                }
                break;
            case R.id.btndetailtagihan:
                if (c == 0) {
                    Toast.makeText(HistoryActivity.this, "Tidak Ada Transaksi", Toast.LENGTH_SHORT).show();
                } else {
                    showDetailTagihan();
                }
                break;
        }

    }

    private void showDetailTagihan() {
        Intent detailhist = new Intent(HistoryActivity.this, DetailHistActivity.class);
        detailhist.putExtra("response", sharedResponse);
        detailhist.putExtra("jenis", "tagihan");

        startActivity(detailhist);
    }

    private void showDetailToken() {
        Intent detailhist = new Intent(HistoryActivity.this, DetailHistActivity.class);
        detailhist.putExtra("response", sharedResponse);
        detailhist.putExtra("jenis", "token");

        startActivity(detailhist);
    }

    private void showDetail() {
        /*Bundle bundle = new Bundle();
        bundle.putSerializable("ob", (Serializable) datapul);

        */
        Intent detailhist = new Intent(HistoryActivity.this, DetailHistActivity.class);
        detailhist.putExtra("response", sharedResponse);
        detailhist.putExtra("jenis", "pulsa");



        startActivity(detailhist);
    }

    private void showHistTagih() {
        webHistTr.setVisibility(View.GONE);
        webhistTagih.setVisibility(View.VISIBLE);

    }

    private void showHistTr() {
        webHistTr.setVisibility(View.VISIBLE);
        webhistTagih.setVisibility(View.GONE);

    }
}
