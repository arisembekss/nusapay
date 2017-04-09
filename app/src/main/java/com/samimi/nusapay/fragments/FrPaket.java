package com.samimi.nusapay.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.samimi.nusapay.custom.CustomDialog;
import com.samimi.nusapay.custom.CustomGridVoucher;
import com.samimi.nusapay.data.AdapterPaket;
import com.samimi.nusapay.data.DataPaket;
import com.samimi.nusapay.firedatabase.ProductPaket;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aris on 01/01/17.
 */

public class FrPaket extends Fragment implements View.OnClickListener {

    View view;

    GridView gridPaket;
    RelativeLayout layMain, layDetail;
    ImageView imgjnspaket;
    TextView txtjnspaket;
    Button btnmainpaket;
    RecyclerView recyclerPaket;
    AdapterPaket madapter;

    public static String[] gridStringPaket = {
            "paket data Xl",
            "paket data Indosat",
            "paket data Axis",
            "paket data Smartfren",
            "paket data Telkomsel",
            "paket data Tri"

    };

    public static String[] gridImageTagPaket = {
            "xl",
            "indosat",
            "axis",
            "smartfren",
            "telkomsel",
            "tri"
    };

    public static String[] grididpaket = {
            "16",
            "17",
            "18",
            "19",
            "20",
            "21"
    };

    public static int[] gridImagePaket = {
            R.drawable.xl,
            R.drawable.indosat,
            R.drawable.axis,
            R.drawable.smartfren,
            R.drawable.telkomsel,
            R.drawable.tri

    };
    private String json_string;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_paket_data, container, false);

        initUi();

        gridPaket.setAdapter(new CustomGridVoucher(getActivity(), gridStringPaket, gridImagePaket, gridImageTagPaket, grididpaket));
        gridPaket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ImageView imageView = (ImageView) view.findViewById(R.id.gridvocher_image);
                String tag = (String) imageView.getTag();
                TextView textView = (TextView) view.findViewById(R.id.gridvoucher_text);
                String jenisPaket = textView.getText().toString();
                TextView textView1 = (TextView) view.findViewById(R.id.txtid);
                String idItem = textView1.getText().toString();
                if (jenisPaket.matches("paket data Telkomsel")) {
                    new CustomDialog().makeDialog(getActivity(), "Paket Data", getString(R.string.dialog_title_telkomsel_data) , "paket");
                } else {
                    //updateUi(idItem, tag, jenisPaket);
                    productPaket(tag, jenisPaket);
                }
            }
        });
        return view;
    }

    private void productPaket(String tag, String jenisPaket) {
        layMain.setVisibility(View.GONE);
        layDetail.setVisibility(View.VISIBLE);
        int resource = getResources().getIdentifier(tag, "drawable", getActivity().getPackageName());
        imgjnspaket.setImageDrawable(getResources().getDrawable(resource));
        txtjnspaket.setText(jenisPaket);

        final int SPLASH_TIME_OUT = 400;

        //final List<String> hargaprovider = new ArrayList<>();
        final List<DataPaket> kodeprovider = new ArrayList<>();
        DatabaseReference dataprovider = FirebaseDatabase.getInstance().getReference().child("product").child("paket").child(tag);
        dataprovider.keepSynced(true);
        dataprovider.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                kodeprovider.clear();

                Log.d("count : ", "" + dataSnapshot.getChildrenCount());
                for (DataSnapshot childt : dataSnapshot.getChildren()) {
                    ProductPaket dummy = childt.getValue(ProductPaket.class);
                    DataPaket dataPaket = new DataPaket();
                    dataPaket.harga = String.valueOf(dummy.getHarga());
                    dataPaket.kode = String.valueOf(dummy.getKode());
                    dataPaket.keterangan = String.valueOf(dummy.getKet());
                    //String textBtnToken = ket + "\n Harga: " + harga;
                    //hargaprovider.add(textBtnToken);
                    kodeprovider.add(dataPaket);
                    Log.d("datas : ", String.valueOf(dummy.getHarga()));
                }

                madapter = new AdapterPaket(getActivity(), kodeprovider);
                recyclerPaket.setAdapter(madapter);
                recyclerPaket.setLayoutManager(new LinearLayoutManager(getActivity()));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerPaket.setVisibility(View.VISIBLE);
                    }
                }, SPLASH_TIME_OUT);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initUi() {
        gridPaket = (GridView) view.findViewById(R.id.gridpaket);
        //gridPaketDetail = (GridView) findViewById(R.id.gridpaketdetail);
        recyclerPaket = (RecyclerView) view.findViewById(R.id.recyclerpaket);
        layMain = (RelativeLayout) view.findViewById(R.id.layMainPaket);
        layDetail = (RelativeLayout) view.findViewById(R.id.layDetailPaket);
        txtjnspaket = (TextView) view.findViewById(R.id.txtJnsPaket);
        imgjnspaket = (ImageView) view.findViewById(R.id.imageJnsPaket);
        btnmainpaket = (Button) view.findViewById(R.id.btnMainPaket);
        btnmainpaket.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMainPaket:
                backToMainPaket();
        }
    }

    private void backToMainPaket() {

        layMain.setVisibility(View.VISIBLE);
        layDetail.setVisibility(View.GONE);
        recyclerPaket.setVisibility(View.INVISIBLE);
    }

    /*private void updateUi(final String idItem, String tag, String jenisPaket) {
        layMain.setVisibility(View.GONE);
        layDetail.setVisibility(View.VISIBLE);

        //Toast.makeText(this, idItem + " " + jenisVoucher, Toast.LENGTH_SHORT).show();
        int resource = getResources().getIdentifier(tag, "drawable", getActivity().getPackageName());
        imgjnspaket.setImageDrawable(getResources().getDrawable(resource));
        txtjnspaket.setText(jenisPaket);

        class FDetailPaket extends AsyncTask<Void, Void, String> {

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
                showHargaPaket();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> paramvocher = new HashMap<>();
                paramvocher.put(Config.TAG_POST_KETERANGAN, idItem);

                reqHandler = new RequestHandler();
                String res = reqHandler.sendPostRequest(Config.URL_VOUCHER_GAME, paramvocher);

                return res;
            }
        }

        FDetailPaket detailPaket = new FDetailPaket();
        detailPaket.execute();
    }

    private void showHargaPaket() {

        JSONObject jsonObject = null;
        List<DataPaket> data = new ArrayList<>();
        try {
            jsonObject = new JSONObject(json_string);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                DataPaket dataPaket = new DataPaket();
                dataPaket.kode = jo.getString(Config.TAG_KODE_ITEM);
                dataPaket.harga = jo.getString(Config.TAG_HARGA_ITEM);
                dataPaket.keterangan = jo.getString(Config.TAG_KETERANGAN_ITEM);
                data.add(dataPaket);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        madapter = new AdapterPaket(getActivity(), data);
        recyclerPaket.setAdapter(madapter);
        recyclerPaket.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerPaket.setVisibility(View.VISIBLE);
    }*/
}
