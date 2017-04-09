package com.samimi.nusapay.feature;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.samimi.nusapay.R;
import com.samimi.nusapay.custom.CustomGridVoucher;

public class VoucherActivity extends AppCompatActivity implements View.OnClickListener {

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
            "ic_launcher",
            "ic_launcher",
            "ic_launcher",
            "ic_launcher",
            "ic_launcher"
    };

    public static String[] grididVoucher = {
            "11",
            "12",
            "13",
            "14",
            "15"
    };

    public static int[] gridImageVouchers = {
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher

    };

    String json_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        gridVoucher.setAdapter(new CustomGridVoucher(this, gridStringsVoucher, gridImageVouchers, gridImageTag, grididVoucher));

       /* gridVoucher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ImageView imageView = (ImageView) view.findViewById(R.id.gridvocher_image);
                String tag = (String) imageView.getTag();
                TextView textView = (TextView) view.findViewById(R.id.gridvoucher_text);
                String jenisVoucher = textView.getText().toString();
                TextView textView1 = (TextView) view.findViewById(R.id.txtid);
                String idItem = textView1.getText().toString();

                updateUi(idItem, tag, jenisVoucher);
            }
        });*/
    }

    /*private void updateUi(final String jenisVoucher, String tag, String keterangan) {
        layoutVoucher.setVisibility(View.GONE);
        layoutDetailVoucher.setVisibility(View.VISIBLE);

        int resource = getResources().getIdentifier(tag, "mipmap", getPackageName());
        imgJenisVoucher.setImageDrawable(getResources().getDrawable(resource));
        txtjnsvoucher.setText(keterangan);

        class DetailVoucher extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(VoucherActivity.this, "Loading...", "Please wait", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                loading.dismiss();
                json_string = s;
                showHargaVoucher();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> paramvocher = new HashMap<>();
                paramvocher.put(Config.TAG_POST_KETERANGAN, jenisVoucher);



                RequestHandler reqHandler = new RequestHandler();
                String res = reqHandler.sendPostRequest(Config.URL_VOUCHER_GAME, paramvocher);

                return res;
            }
        }

        DetailVoucher detailVoucher = new DetailVoucher();
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

        gridVoucherDetail.setAdapter(new CustomDetailVgame(this, kodevgame, hargavgame));
        gridVoucherDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


            }
        });
    }*/

    private void initUi() {
        /*gridVoucher = (GridView) findViewById(R.id.gridvoucher);
        gridVoucherDetail = (GridView) findViewById(R.id.gridvoucherdetail);
        txtjnsvoucher = (TextView) findViewById(R.id.txtJnsVoucher);
        imgJenisVoucher = (ImageView) findViewById(R.id.imageJnsVoucher);
        layoutVoucher = (RelativeLayout) findViewById(R.id.layVoucher);
        layoutDetailVoucher = (RelativeLayout) findViewById(R.id.layVoucherDetail);
        mainMenuVoucher = (Button) findViewById(R.id.btnMainVoucher);
        mainMenuVoucher.setOnClickListener(this);*/

    }

    @Override
    public void onClick(View view) {

    }

    private void showMainMenuVoucher() {
        /*layoutDetailVoucher.setVisibility(View.GONE);
        layoutVoucher.setVisibility(View.VISIBLE);*/
    }
}
