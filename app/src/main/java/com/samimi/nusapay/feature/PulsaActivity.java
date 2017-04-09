package com.samimi.nusapay.feature;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.samimi.nusapay.PredictNumber;
import com.samimi.nusapay.R;

public class PulsaActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {


    /*String Master*/
    String trProvider;
    String trNominal;
    String transaksiKode;
    /**/
    PredictNumber predictNumber = new PredictNumber(this);
    Bundle extras;
    String selfIntent, stringOtherNumber;
    String kodeProvider, provider;
    String formatTransaksi;
    String nominal;

    TextView tuserNumber, totherNumber, tuserProvider;
    EditText edOtherNumber;
    Button bTransac;
    Spinner spinnerKode;

    Fragment fragment;
    FragmentManager fragmentManager;
    Transaksi transaksi;
    //otherIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulsa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initUI();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setFragment();




    }

    /*public void setFragment() {
        extras=getIntent().getExtras();
        if (extras!=null) {
            selfIntent = extras.getString("transaksi");

            if (selfIntent.equals("isi pulsa") ) {

                fragment = new FrSingleNumber();
            } else if (selfIntent.equals("cek")) {
                fragment = new FrTagihan();
            }

        }

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentPulsa, fragment);
        fragmentTransaction.commit();
    }*/

    private void UIOtherNumber() {
        findViewById(R.id.otherLayout).setVisibility(View.VISIBLE);
        //findViewById(R.id.selfLayout).setVisibility(View.GONE);

    }

    private void UISelfNumber() {
        //findViewById(R.id.selfLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.otherLayout).setVisibility(View.GONE);
    }

    private void initUI() {

        //tuserNumber = (TextView) findViewById(R.id.tuserNumber);
        //tuserProvider = (TextView) findViewById(R.id.tuserProvider);

        /*totherNumber = (TextView) findViewById(R.id.txtOtherNumber);
        edOtherNumber = (EditText) findViewById(R.id.editOtherNumber);
        bTransac = (Button) findViewById(R.id.bTransac);
        bTransac.setOnClickListener(this);
        spinnerKode = (Spinner) findViewById(R.id.spinnerKode);
        spinnerKode.setPrompt("Nominal");
        edOtherNumber.addTextChangedListener(this);*/


    }

    @Override
    public void onBackPressed() {
        /*super.onBackPressed();
        this.closeContextMenu();*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.bTransac:
                prosesTransaksi();
                break;*/

        }
    }

    private void prosesTransaksi() {
        /*String kodeTr = edOtherNumber.getText().toString();
        transaksiKode = trProvider+trNominal+"."+kodeTr+".3003";
        Toast.makeText(this, transaksiKode,Toast.LENGTH_SHORT).show();*/
        /*transaksi = new Transaksi(this);
        transaksi.setKode(kodeTr);
        transaksi.execute();*/
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        /*if (s.length() == 6) {
            predictNumber.readNumber(s.toString());
            provider = predictNumber.getTypeNumber();
            kodeProvider = predictNumber.getKodeTransaksi();
            setTrProvider(predictNumber.getKodeTransaksi());
            totherNumber.setText("Provider : "+provider+" ("+trProvider+")");
            queryKodeProvider(kodeProvider);
        }*/
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void queryKodeProvider(final String providerCode) {

        /*class QueryKodeAsync extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> paramsProvider = new HashMap<>();
                paramsProvider.put(Config.TAG_PROVIDER, providerCode);



                RequestHandler reqHandler = new RequestHandler();
                String res = reqHandler.sendPostRequest(Config.URL_QUERY_KODE, paramsProvider);

                return res;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSONObject jsonObject;
                //ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                List<String> list = new ArrayList<>();
                try {
                    jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray("result");
                    for (int i=0; i<result.length(); i++) {
                        JSONObject jo = result.getJSONObject(i);
                        nominal = jo.getString(Config.TAG_NOMINAL);
                        list.add(nominal+".000");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(PulsaActivity.this, "Nomor tidak dikenali", Toast.LENGTH_SHORT).show();

                    final Dialog dialog = new Dialog(PulsaActivity.this);
                    dialog.setContentView(R.layout.dialog_provider);
                    dialog.setCancelable(false);
                    dialog.setTitle("Oopss");
                    TextView txtError = (TextView) dialog.findViewById(R.id.textProviderNull);
                    txtError.setText("Nomor Tidak Dikenali");

                    Button btnError = (Button) dialog.findViewById(R.id.btnProviderNull);
                    btnError.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            edOtherNumber.setText("");
                        }
                    });

                    dialog.show();
                }
                //ListAdapter adapter = new SimpleAdapter(PulsaActivity.this, list, android.R.layout.simple_spinner_item, null, null);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(PulsaActivity.this, android.R.layout.simple_spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerKode.setAdapter(adapter);
            }
        }

        QueryKodeAsync queryKode = new QueryKodeAsync();
        queryKode.execute();

        spinnerKode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String nominalTemp = spinnerKode.getSelectedItem().toString();
                String kodeTnsk = nominalTemp.replace(".000", "");
                setTrNominal(kodeTnsk);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

    }

    public String getTrProvider() {
        return trProvider;
    }

    public void setTrProvider(String trProvider) {
        this.trProvider = trProvider;
    }

    public String getTrNominal() {
        return trNominal;
    }

    public void setTrNominal(String trNominal) {
        this.trNominal = trNominal;
    }
}
