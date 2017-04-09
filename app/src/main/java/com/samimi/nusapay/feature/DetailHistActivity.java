package com.samimi.nusapay.feature;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.samimi.nusapay.configuration.Config;
import com.samimi.nusapay.R;
import com.samimi.nusapay.data.AdapterDetailHist;
import com.samimi.nusapay.data.DataPul;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailHistActivity extends AppCompatActivity implements View.OnClickListener {

    GridView gridPaket;
    RelativeLayout zerotrx;
    RecyclerView recyclerHist;
    AdapterDetailHist madapter;
    RadioButton fall, f3, f7, f30;
    RadioGroup rfilter;
    String jenis;



    JSONArray pulsa, pulsa3d, pulsa7d, pulsa30d, token, token3d, token7d, token30d, tagihan, tagihan3d, tagihan7d, tagihan30d,
            voucher, voucher3d, voucher7d, voucher30;

    private String json_string;

    List<DataPul> datapulsa;
    private TextView titlehist;
    EditText eded;
    Button bb;
    String response;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paket_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        jenis = getIntent().getStringExtra("jenis");
        response = getIntent().getStringExtra("response");
        prepareJson();
        initUi();
        if (jenis.matches("tagihan")) {
            setDetailTagihan();
            rfilter.setVisibility(View.GONE);
        } else {
            rfilter.setVisibility(View.VISIBLE);
        }  /*else if (jenis.matches("token")) {
            setDetailToken();
        }*/
        //Toast.makeText(this, response, Toast.LENGTH_LONG).show();

    }

    private void prepareJson() {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject data = result.getJSONObject(0);
            JSONArray detail = data.getJSONArray(Config.ARRAY_HIST_DETAIL);
            JSONObject detaildata = detail.getJSONObject(0);
            JSONArray last3 = data.getJSONArray(Config.ARRAY_HIST_LAST_THREE);
            JSONObject last3data = last3.getJSONObject(0);
            JSONArray last7 = data.getJSONArray(Config.ARRAY_HIST_LAST_SEVEN);
            JSONObject last7data = last7.getJSONObject(0);
            JSONArray last30 = data.getJSONArray(Config.ARRAY_HIST_LAST_THREET);
            JSONObject last30data = last30.getJSONObject(0);

            /*jsaon array Pulsa*/
            pulsa = detaildata.getJSONArray(Config.ARRAY_HIST_DETAIL_PULSA);
            token = detaildata.getJSONArray(Config.ARRAY_HIST_DETAIL_TOKEN);
            tagihan = detaildata.getJSONArray(Config.ARRAY_HIST_DETAIL_TAGIHAN);
            voucher = detaildata.getJSONArray(Config.ARRAY_HIST_DETAIL_VOUCHER);

            pulsa3d = last3data.getJSONArray(Config.ARRAY_HIST_DETAIL_PULSA);
            token3d = last3data.getJSONArray(Config.ARRAY_HIST_DETAIL_TOKEN);
            tagihan3d = last3data.getJSONArray(Config.ARRAY_HIST_DETAIL_TAGIHAN);
            voucher3d = last3data.getJSONArray(Config.ARRAY_HIST_DETAIL_VOUCHER);

            pulsa7d = last7data.getJSONArray(Config.ARRAY_HIST_DETAIL_PULSA);
            token7d = last7data.getJSONArray(Config.ARRAY_HIST_DETAIL_TOKEN);
            tagihan7d = last7data.getJSONArray(Config.ARRAY_HIST_DETAIL_TAGIHAN);
            voucher7d = last7data.getJSONArray(Config.ARRAY_HIST_DETAIL_VOUCHER);

            pulsa30d = last30data.getJSONArray(Config.ARRAY_HIST_DETAIL_PULSA);
            token30d = last30data.getJSONArray(Config.ARRAY_HIST_DETAIL_TOKEN);
            tagihan30d = last30data.getJSONArray(Config.ARRAY_HIST_DETAIL_TAGIHAN);
            voucher30 = last30data.getJSONArray(Config.ARRAY_HIST_DETAIL_VOUCHER);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setDetailToken() {
        List<DataPul> datadetail = new ArrayList<>();
        if (token.length() == 0) {
            recyclerHist.setVisibility(View.GONE);
            zerotrx.setVisibility(View.VISIBLE);
        } else {
            recyclerHist.setVisibility(View.VISIBLE);
            zerotrx.setVisibility(View.GONE);
        }
        for (int i = 0; i<token.length(); i++) {
            JSONObject jopulsa = null;
            try {
                jopulsa = token.getJSONObject(i);
                DataPul datapulsa = new DataPul();
                datapulsa.dateP = jopulsa.getString("date");
                datapulsa.kodeP = jopulsa.getString("kode");
                datapulsa.hargaP = jopulsa.getString("harga");
                datapulsa.nomorP = jopulsa.getString("nomor_tujuan");
                datadetail.add(datapulsa);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Log.d("array Pulsa", String.valueOf(datadetail));
        }
        madapter = new AdapterDetailHist(DetailHistActivity.this, datadetail);
        madapter.notifyDataSetChanged();

        recyclerHist.setAdapter(madapter);
        recyclerHist.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setDetailPulsa() {
        List<DataPul> datadetail = new ArrayList<>();
        if (pulsa.length() == 0) {
            recyclerHist.setVisibility(View.GONE);
            zerotrx.setVisibility(View.VISIBLE);
        } else {
            recyclerHist.setVisibility(View.VISIBLE);
            zerotrx.setVisibility(View.GONE);
        }
        for (int i = 0; i<pulsa.length(); i++) {
            JSONObject jopulsa = null;
            try {
                jopulsa = pulsa.getJSONObject(i);
                DataPul datapulsa = new DataPul();
                datapulsa.dateP = jopulsa.getString("date");
                datapulsa.kodeP = jopulsa.getString("kode");
                datapulsa.hargaP = jopulsa.getString("harga");
                datapulsa.nomorP = jopulsa.getString("nomor_tujuan");
                datadetail.add(datapulsa);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Log.d("array Pulsa", String.valueOf(datadetail));
        }
        madapter = new AdapterDetailHist(DetailHistActivity.this, datadetail);
        //madapter.notifyDataSetChanged();

        recyclerHist.setAdapter(madapter);

        recyclerHist.setLayoutManager(new LinearLayoutManager(this));

    }


    private void initUi() {
        titlehist = (TextView) findViewById(R.id.titlejns);
        String cap = "Detail " + jenis.substring(0, 1).toUpperCase() + jenis.substring(1);
        titlehist.setText(cap);
        eded = (EditText) findViewById(R.id.eded);
        bb = (Button) findViewById(R.id.bb);
        bb.setOnClickListener(this);
        recyclerHist = (RecyclerView) findViewById(R.id.rechisto);
        zerotrx = (RelativeLayout) findViewById(R.id.zerotrx);

        fall = (RadioButton) findViewById(R.id.fall);
        f3 = (RadioButton) findViewById(R.id.f3);
        f30 = (RadioButton) findViewById(R.id.f30);
        f7 = (RadioButton) findViewById(R.id.f7);
        rfilter = (RadioGroup) findViewById(R.id.rfilter);
        rfilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.fall:
                        //recyclerHist.setAdapter(null);
                        if (jenis.matches("pulsa")) {
                            setDetailPulsa();
                        } else if (jenis.matches("token")) {
                            setDetailToken();
                        } else if (jenis.matches("voucher")) {
                            setDetailVoucher();
                        }

                        break;
                    case R.id.f3:
                        //recyclerHist.setAdapter(null);

                        if (jenis.matches("pulsa")) {
                            setDetailPulsaThree();
                        } else if (jenis.matches("token")) {
                            setDetailTokenThree();
                        }  else if (jenis.matches("voucher")) {
                            setDetailVoucherThree();
                        }
                        break;
                    case R.id.f7:

                        if (jenis.matches("pulsa")) {
                            setDetailPulsaSeven();
                        } else if (jenis.matches("token")) {
                            setDetailTokenSeven();

                        } else if (jenis.matches("voucher")) {
                            setDetailVoucherSeven();
                        }
                        break;
                    case R.id.f30:

                        if (jenis.matches("pulsa")) {
                            setDetailPulsaThitry();
                        } else if (jenis.matches("token")) {
                            setDetailTokenThirty();
                        }  else if (jenis.matches("voucher")) {
                            setDetailVoucherThirty();
                        }
                        break;
                }
            }
        });
    }

    private void setDetailTagihanThirty() {

    }

    private void setDetailVoucherThirty() {

    }

    private void setDetailTagihanSeven() {

    }

    private void setDetailVoucherSeven() {

    }

    private void setDetailTagihanThree() {

    }

    private void setDetailVoucherThree() {

    }

    private void setDetailTagihan() {
        List<DataPul> datadetail = new ArrayList<>();
        for (int k = 0; k < tagihan.length(); k++) {
            try {
                JSONObject jotagih = tagihan.getJSONObject(k);
                DataPul datatagihan = new DataPul();
                datatagihan.dateP = jotagih.getString("date");
                datatagihan.kodeP = jotagih.getString("jenis_tagihan");
                datatagihan.hargaP = jotagih.getString("jumlah_tagihan");
                datatagihan.nomorP= jotagih.getString("nomor_tujuan");
                datadetail.add(datatagihan);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        madapter = new AdapterDetailHist(DetailHistActivity.this, datadetail);

        madapter.notifyDataSetChanged();
        recyclerHist.setAdapter(madapter);

        recyclerHist.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setDetailVoucher() {

    }

    private void setDetailPulsaThitry() {
        List<DataPul> datadetail = new ArrayList<>();
        if (pulsa30d.length() == 0) {
            recyclerHist.setVisibility(View.GONE);
            zerotrx.setVisibility(View.VISIBLE);
        } else {
            recyclerHist.setVisibility(View.VISIBLE);
            zerotrx.setVisibility(View.GONE);
        }
        for (int i = 0; i<pulsa30d.length(); i++) {
            JSONObject jopulsa = null;
            try {
                jopulsa = pulsa30d.getJSONObject(i);
                DataPul datapulsa = new DataPul();
                datapulsa.dateP = jopulsa.getString("date");
                datapulsa.kodeP = jopulsa.getString("kode");
                datapulsa.hargaP = jopulsa.getString("harga");
                datapulsa.nomorP = jopulsa.getString("nomor_tujuan");
                datadetail.add(datapulsa);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("array Pulsa", String.valueOf(datadetail));
        }
        madapter = new AdapterDetailHist(DetailHistActivity.this, datadetail);

        madapter.notifyDataSetChanged();
        recyclerHist.setAdapter(madapter);

        recyclerHist.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setDetailTokenThirty() {
        List<DataPul> datadetail = new ArrayList<>();
        if (token30d.length() == 0) {
            recyclerHist.setVisibility(View.GONE);
            zerotrx.setVisibility(View.VISIBLE);
        } else {
            recyclerHist.setVisibility(View.VISIBLE);
            zerotrx.setVisibility(View.GONE);
            for (int i = 0; i<token30d.length(); i++) {
                JSONObject jopulsa = null;
                try {
                    jopulsa = token30d.getJSONObject(i);
                    DataPul datapulsa = new DataPul();
                    datapulsa.dateP = jopulsa.getString("date");
                    datapulsa.kodeP = jopulsa.getString("kode");
                    datapulsa.hargaP = jopulsa.getString("harga");
                    datapulsa.nomorP = jopulsa.getString("nomor_tujuan");
                    datadetail.add(datapulsa);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("array Pulsa", String.valueOf(datadetail));
            }
            madapter = new AdapterDetailHist(DetailHistActivity.this, datadetail);
            madapter.notifyDataSetChanged();
        }

        recyclerHist.setAdapter(madapter);
        recyclerHist.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setDetailTokenSeven() {
        List<DataPul> datadetail = new ArrayList<>();
        if (token7d.length() == 0) {
            recyclerHist.setVisibility(View.GONE);
            zerotrx.setVisibility(View.VISIBLE);
        } else {
            recyclerHist.setVisibility(View.VISIBLE);
            zerotrx.setVisibility(View.GONE);
        }
            for (int i = 0; i < token7d.length(); i++) {
                JSONObject jopulsa = null;
                try {
                    jopulsa = token7d.getJSONObject(i);
                    DataPul datapulsa = new DataPul();
                    datapulsa.dateP = jopulsa.getString("date");
                    datapulsa.kodeP = jopulsa.getString("kode");
                    datapulsa.hargaP = jopulsa.getString("harga");
                    datapulsa.nomorP = jopulsa.getString("nomor_tujuan");
                    datadetail.add(datapulsa);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("array Pulsa", String.valueOf(datadetail));
            }
            madapter = new AdapterDetailHist(DetailHistActivity.this, datadetail);
            madapter.notifyDataSetChanged();

        recyclerHist.setAdapter(madapter);
        recyclerHist.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setDetailPulsaSeven() {
        List<DataPul> datadetail = new ArrayList<>();
        if (pulsa7d.length() == 0) {
            recyclerHist.setVisibility(View.GONE);
            zerotrx.setVisibility(View.VISIBLE);
        } else {
            recyclerHist.setVisibility(View.VISIBLE);
            zerotrx.setVisibility(View.GONE);
        }
        for (int i = 0; i<pulsa7d.length(); i++) {
            JSONObject jopulsa = null;
            try {
                jopulsa = pulsa7d.getJSONObject(i);
                DataPul datapulsa = new DataPul();
                datapulsa.dateP = jopulsa.getString("date");
                datapulsa.kodeP = jopulsa.getString("kode");
                datapulsa.hargaP = jopulsa.getString("harga");
                datapulsa.nomorP = jopulsa.getString("nomor_tujuan");
                datadetail.add(datapulsa);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("array Pulsa", String.valueOf(datadetail));
        }
        madapter = new AdapterDetailHist(DetailHistActivity.this, datadetail);
        //madapter.swap(datadetail);
        madapter.notifyDataSetChanged();
       /* recyclerHist7.setVisibility(View.VISIBLE);
        recyclerHist3.setVisibility(View.GONE);
        recyclerHist.setVisibility(View.GONE);
        recyclerHist30.setVisibility(View.GONE);*/
        recyclerHist.setAdapter(madapter);

        recyclerHist.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setDetailTokenThree() {
        List<DataPul> datadetail = new ArrayList<>();
        if (token3d.length() == 0) {
            recyclerHist.setVisibility(View.GONE);
            zerotrx.setVisibility(View.VISIBLE);
        } else {
            recyclerHist.setVisibility(View.VISIBLE);
            zerotrx.setVisibility(View.GONE);
            for (int i = 0; i<token3d.length(); i++) {
                JSONObject jopulsa = null;
                try {
                    jopulsa = token3d.getJSONObject(i);
                    DataPul datapulsa = new DataPul();
                    datapulsa.dateP = jopulsa.getString("date");
                    datapulsa.kodeP = jopulsa.getString("kode");
                    datapulsa.hargaP = jopulsa.getString("harga");
                    datapulsa.nomorP = jopulsa.getString("nomor_tujuan");
                    datadetail.add(datapulsa);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("array Pulsa", String.valueOf(datadetail));
            }
            madapter = new AdapterDetailHist(DetailHistActivity.this, datadetail);
            madapter.notifyDataSetChanged();
        }

        recyclerHist.setAdapter(madapter);
        recyclerHist.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setDetailPulsaThree() {
        List<DataPul> datadetail = new ArrayList<>();
        if (pulsa3d.length() == 0) {
            recyclerHist.setVisibility(View.GONE);
            zerotrx.setVisibility(View.VISIBLE);
        } else {
            recyclerHist.setVisibility(View.VISIBLE);
            zerotrx.setVisibility(View.GONE);
        }
        for (int i = 0; i<pulsa3d.length(); i++) {
            JSONObject jopulsa = null;
            try {
                jopulsa = pulsa3d.getJSONObject(i);
                DataPul datapulsa = new DataPul();
                datapulsa.dateP = jopulsa.getString("date");
                datapulsa.kodeP = jopulsa.getString("kode");
                datapulsa.hargaP = jopulsa.getString("harga");
                datapulsa.nomorP = jopulsa.getString("nomor_tujuan");
                datadetail.add(datapulsa);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("array Pulsa", String.valueOf(datadetail));
        }
        madapter = new AdapterDetailHist(DetailHistActivity.this, datadetail);
        //madapter.swap(datadetail);
        madapter.notifyDataSetChanged();
        /*recyclerHist3.setVisibility(View.VISIBLE);
        recyclerHist.setVisibility(View.GONE);
        recyclerHist7.setVisibility(View.GONE);
        recyclerHist30.setVisibility(View.GONE);*/
        recyclerHist.setAdapter(madapter);

        recyclerHist.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bb:
                String no = eded.getText().toString();
                backToMainPaket(no);

        }
    }

    private void backToMainPaket(String no) {

        try {
            // phone must begin with '+'
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(no, "");
            int countryCode = numberProto.getCountryCode();
            long nationalNumber = numberProto.getNationalNumber();
            Log.i("code", "code " + countryCode);
            Log.i("code", "national number " + nationalNumber);
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }

        /*layMain.setVisibility(View.VISIBLE);
        layDetail.setVisibility(View.GONE);*/
    }
}
