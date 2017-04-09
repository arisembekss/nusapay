package com.samimi.nusapay;

import android.content.Context;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

/**
 * Created by aris on 14/11/16.
 */

public class PredictNumber {

    Context context;
    public String typeNumber;
    public String kodeTransaksi;
    public String typeSimpati;
    public String nomorTujuan;
    List<String> jatim = Arrays.asList("081130", "081131", "081132", "081133",
            "081134", "081137", "081135", "081136", "081216", "081217", "081230", "081231",
            "081232", "081233", "081234", "081235", "081249", "081252", "081259", "081330",
            "081331", "081332", "081333", "081334", "081335", "081336", "081357", "081358",
            "081359", "082139", "082140", "082141", "082142", "082143", "085230", "085231",
            "085232", "085233", "085234", "085235", "085236", "085257", "085258", "085259",
            "085330", "085331", "085332", "085333", "085334", "085335", "085336", "082228",
            "082229", "082230", "082231", "082232", "082233", "082234", "082264", "082330",
            "082331", "082332", "082333", "082334", "082335", "082336", "082337", "082338");


    public PredictNumber(Context context) {
        this.context = context;
    }

    public void readNumber(String typeNumber) {
        String prepredict = typeNumber.replaceAll("[-\\s]", "");
        Log.d("Prepredict", prepredict);

        if (prepredict.contains("+62")) {
            String finalString = prepredict.replace("+62", "0");
            readProvider(finalString);
            setNomorTujuan(finalString);
        } else {
            readProvider(prepredict);
            setNomorTujuan(prepredict);
        }

    }

    public void readProvider(String finalString) {
        String subed = finalString.substring(0, 4);
        if (subed.equals("0855") || subed.equals("0856") || subed.equals("0857") || subed.equals("0858") ||
                subed.equals("0814") || subed.equals("0815") || subed.equals("0816")) {
            setTypeNumber("Indosat");
            setKodeTransaksi("i");
        } else if (subed.equals("0811") || subed.equals("0812") || subed.equals("0813") || subed.equals("0821")
                || subed.equals("0822") || subed.equals("0823") || subed.equals("0852") || subed.equals("0853")
                || subed.equals("0851")) {
            readSimpati(finalString);
        } else if (subed.equals("0817") || subed.equals("0818") || subed.equals("0819")
                || subed.equals("0859") || subed.equals("0877") || subed.equals("0878") || subed.equals("0838")
                || subed.equals("0831") || subed.equals("0832") || subed.equals("0833")
                ) {
            setTypeNumber("XL");
            setKodeTransaksi("xr");
        } else if (subed.equals("0896") || subed.equals("0897") || subed.equals("0895") || subed.equals("0898")
                || subed.equals("0899")) {
            setTypeNumber("Tri");
            setKodeTransaksi("t");
        } else if (subed.equals("0881") || subed.equals("0882") || subed.equals("0883")
                || subed.equals("0884") || subed.equals("0885") || subed.equals("0886")
                || subed.equals("0887") || subed.equals("0888") || subed.equals("0889")) {
            setTypeNumber("Smartfren");
            setKodeTransaksi("sf");
        } else {
            setTypeNumber("Unknown");
            setKodeTransaksi("Unknown");
        }
    }


    public void readSimpati(String typeSimpati) {
        if (!jatim.contains(typeSimpati)) {
            setTypeNumber("Telkomsel");
            setKodeTransaksi("se");
        } else {
            setTypeNumber("Telkomsel");
            setKodeTransaksi("s");
        }
    }

    public String getTypeSimpati() {
        return typeSimpati;
    }

    public void setTypeSimpati(String typeSimpati) {
        this.typeSimpati = typeSimpati;
    }

    public String getTypeNumber() {
        return typeNumber;
    }

    public void setTypeNumber(String typeNumber) {
        this.typeNumber = typeNumber;
    }

    public String getKodeTransaksi() {
        return kodeTransaksi;
    }

    public void setKodeTransaksi(String kodeTransaksi) {
        this.kodeTransaksi = kodeTransaksi;
    }

    public String getNomorTujuan() {
        return nomorTujuan;
    }

    public void setNomorTujuan(String nomorTujuan) {
        this.nomorTujuan = nomorTujuan;
    }
}
