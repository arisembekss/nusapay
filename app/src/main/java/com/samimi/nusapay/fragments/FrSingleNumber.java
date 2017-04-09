package com.samimi.nusapay.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.samimi.nusapay.PredictNumber;
import com.samimi.nusapay.R;
import com.samimi.nusapay.configuration.Config;
import com.samimi.nusapay.custom.CustomGridToken;
import com.samimi.nusapay.feature.Transaksi;
import com.samimi.nusapay.firedatabase.DumDum;
import com.samimi.nusapay.preference.PrefManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

//import static com.dtech.smartpulsa.R.id.bTransac;

/**
 * Created by aris on 30/11/16.
 */

public class FrSingleNumber extends Fragment implements TextWatcher, AdapterView.OnItemClickListener {

    PredictNumber predictNumber = new PredictNumber(getActivity());
    PrefManager prefManager;
    Transaksi transaksiPulsa;

    String trProvider;
    String trNominal;
    String numToPred;

    String kodeProvider, provider;
    String formatTrx;
    //String nominal;
    String firebaseId;
    String email;

    View view;
    TextView totherNumber;
    EditText edOtherNumber;
    ImageButton imgPin;

    SharedPreferences sharedPreferences;
    //String harga;
    GridView gridView;
    //String json_string;
    InputMethodManager imm;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fr_single_number, container, false);
        prefManager = new PrefManager(getActivity());
        sharedPreferences = getActivity().getSharedPreferences(Config.PREF_NAME, MODE_PRIVATE);
        firebaseId = (sharedPreferences.getString(Config.DISPLAY_FIREBASE_ID, ""));
        email = (sharedPreferences).getString(Config.DISPLAY_EMAIL, "");

         imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        /*Gson gson = new Gson();
        String jsonHarga = (sharedPreferences.getString("th", ""));
        String jsonKode = (sharedPreferences.getString("tk", ""));

        String[] listHarga = gson.fromJson(jsonHarga, String[].class);
        String[] listKode = gson.fromJson(jsonKode, String[].class);
        Log.d("listharga", Arrays.toString(listHarga));*/

        initUI();
        return view;
    }

    private void initUI() {
        gridView = (GridView) view.findViewById(R.id.gridPulsa);
        gridView.setVisibility(View.GONE);
        totherNumber = (TextView) view.findViewById(R.id.txtOtherNumber);
        edOtherNumber = (EditText) view.findViewById(R.id.editOtherNumber);
        imgPin = (ImageButton) view.findViewById(R.id.imgPin);
        /*imgPin.setImageDrawable(GoogleMaterial.Icon.gmd_search);*/
        imgPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edOtherNumber.getText().toString().matches("")) {
                    Toast.makeText(getActivity(), "Isikan nomor anda terlebih dahulu", Toast.LENGTH_SHORT).show();
                } else {
                    /*getActivity().getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.HI
                    );*/
                    String natio = "";
                    String numberisi = edOtherNumber.getText().toString();
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                    if (Character.toString(numberisi.charAt(0)).matches("0") && numberisi.contains("-")) {

                        numToPred = numberisi.replaceAll("-", "");
                    } else if (numberisi.contains("+")) {
                        // phone must begin with '+'
                        try {
                            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                            Phonenumber.PhoneNumber numberProto = null;
                            numberProto = phoneUtil.parse(numberisi, "");
                            int countryCode = numberProto.getCountryCode();
                            long nationalNumber = numberProto.getNationalNumber();
                            //natio =  Long.toString(nationalNumber);
                            natio = ""+nationalNumber;
                            Log.i("code", "code " + countryCode);
                            Log.i("code", "national number " + nationalNumber);

                            numToPred = "0" + natio;
                            Log.i("code", "final number " + numToPred);

                        } catch (NumberParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        numToPred = numberisi;
                    }

                    predictNumber.readProvider(numToPred);
                    provider = predictNumber.getTypeNumber();
                    kodeProvider = predictNumber.getKodeTransaksi();
                    setTrProvider(predictNumber.getKodeTransaksi());

                    if (provider.equals("Unknown")) {
                        final Dialog dialog = new Dialog(getActivity());
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
                        gridView.setVisibility(View.INVISIBLE);
                        totherNumber.setText("Provider : ");
                    } else {
                        totherNumber.setText("Provider : "+provider+" ("+trProvider+")");
                    }
                    //queryKodeProvider(kodeProvider);
                    tesTransaksi(kodeProvider);

                }

            }
        });

        edOtherNumber.addTextChangedListener(this);
    }


    /*private void prosesTransaksi() {

        JSONObject jsonObject;
        ArrayList<String> listharga = new ArrayList<String>();
        ArrayList<String> listkode = new ArrayList<String>();
        List<String> list = new ArrayList<>();
        try {
            jsonObject = new JSONObject(json_string);
            JSONArray result = jsonObject.getJSONArray("result");
            for (int i=0; i<result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                nominal = jo.getString(Config.TAG_NOMINAL);
                harga = jo.getString(Config.TAG_HARGA_PROV);
                listkode.add(nominal+".000");
                listharga.add("Harga: "+harga);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String[] kodetoken = listkode.toArray(new String[listkode.size()]);
            String[] hargaToken = listharga.toArray(new String[listharga.size()]);

            gridView.setAdapter(new CustomGridToken(hargaToken, kodetoken, getActivity()));
            gridView.setOnItemClickListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void tesTransaksi(String kodeProvider) {

        final int SPLASH_TIME_OUT = 400;

        final List<String> hargaprovider = new ArrayList<>();
        final List<String> kodeprovider = new ArrayList<>();
        DatabaseReference dataprovider = FirebaseDatabase.getInstance().getReference().child(kodeProvider);
        dataprovider.keepSynced(true);
        dataprovider.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hargaprovider.clear();
                kodeprovider.clear();
                Log.d("count : ", "" + dataSnapshot.getChildrenCount());
                for (DataSnapshot childt : dataSnapshot.getChildren()) {
                    DumDum dummy = childt.getValue(DumDum.class);
                    String harga = String.valueOf(dummy.getHarga());
                    String kode = String.valueOf(dummy.getkode());
                    hargaprovider.add("Harga: "+harga);
                    kodeprovider.add(kode);
                    Log.d("datas : ", String.valueOf(dummy.getHarga()));
                }
                String[] arraykode = kodeprovider.toArray(new String[kodeprovider.size()]);
                String[] arrayharga = hargaprovider.toArray(new String[hargaprovider.size()]);

                gridView.setAdapter(new CustomGridToken(arrayharga, arraykode, getActivity()));
                gridView.setOnItemClickListener(FrSingleNumber.this);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        gridView.setVisibility(View.VISIBLE);
                    }
                }, SPLASH_TIME_OUT);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        gridView.setVisibility(View.GONE);
        gridView.setAdapter(null);
        if (s.length() >= 6) {

            if (s.length() >= 10) {
                imgPin.setVisibility(View.VISIBLE);
            } else {
                imgPin.setVisibility(View.INVISIBLE);
            }

        }

        if (s.length() <= 4) {
            totherNumber.setText("Provider : ");
        }
    }



    @Override
    public void afterTextChanged(Editable s) {

    }

    /*private void queryKodeProvider(final String providerCode) {

        gridView.setVisibility(View.GONE);
        class QueryKodeAsync extends AsyncTask<Void, Void, String> {
            RequestHandler reqHandler;
            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> paramsProvider = new HashMap<>();
                paramsProvider.put(Config.TAG_PROVIDER, providerCode);

                reqHandler = new RequestHandler();
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
                if (reqHandler.getStatus() == 0) {
                    new CustomDialog().makeDialog(getActivity(), "Ooopss", getString(R.string.dialog_title_connection_trouble1) , "koneksi");
                }
                json_string = s;
                prosesTransaksi();
            }

        }


        QueryKodeAsync queryKode = new QueryKodeAsync();
        queryKode.execute();

    }*/

    /*public String getTrProvider() {
        return trProvider;
    }*/

    public void setTrProvider(String trProvider) {
        this.trProvider = trProvider;
    }

    /*public String getTrNominal() {
        return trNominal;
    }*/

    public void setTrNominal(String trNominal) {
        this.trNominal = trNominal;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView tvkode = (TextView) view.findViewById(R.id.txtkodeToken);

        String nominalTemp = tvkode.getText().toString();
        String kodeTnsk = nominalTemp.replace(".000", "");
        setTrNominal(kodeTnsk);

        String nomorTuj = numToPred;
        if (nomorTuj.length() < 9 || nomorTuj.matches("")) {
            Toast.makeText(getActivity(), "Cek nomor tujuan anda", Toast.LENGTH_SHORT).show();
            edOtherNumber.requestFocus();
        }
        //PredictNumber ambilNomorTujuan = new PredictNumber(getActivity());
        //String nomorTuj = predictNumber.getNomorTujuan();
        String transaksi = trProvider + trNominal;
        formatTrx = transaksi+"."+nomorTuj+".3003";
        transaksiPulsa = new Transaksi(getActivity());
        transaksiPulsa.setUser(email);
        transaksiPulsa.setNomorTuj(nomorTuj);
        transaksiPulsa.setJenisTransaksi(transaksi);
        transaksiPulsa.setFirebaseId(firebaseId);
        transaksiPulsa.setKode(formatTrx);

        //Toast.makeText(getActivity(), formatTrx,Toast.LENGTH_SHORT).show();
        final AlertDialog alertDialog ;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Transaksi");
        builder.setMessage("Anda akan melakukan transaksi dengan detail :\nNomor Tujuan : "+nomorTuj+"\nProvider : "+provider+"\nNominal : "+nominalTemp);
        builder.setPositiveButton("Proses", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                transaksiPulsa.execute();
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("sms-server").child("servera").child("sms").setValue(formatTrx);
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                edOtherNumber.setText("");
                gridView.setAdapter(null);
            }
        });

    }

    /*@Override
    public void onResume() {
        super.onResume();
        edOtherNumber.setText("");
        gridView.setVisibility(View.INVISIBLE);
    }*/
}
