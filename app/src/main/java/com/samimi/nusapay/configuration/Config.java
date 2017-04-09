package com.samimi.nusapay.configuration;

/**
 * Created by aris on 02/10/16.
 */

public class Config {
    /*file*/
    public static final String FIRST_TIME = "first";
    /*poin*/
    public static final String WEBA = "http://samimi.web.id/dev/poina.php";
    public static final String WEBB = "http://samimi.web.id/dev/poinb.php";
    public static final String WEBC = "http://samimi.web.id/dev/poinc.php";
    public static final String WEBD = "http://samimi.web.id/dev/poind.php";
    public static final String WEBE = "http://samimi.web.id/dev/poine.php";
    public static final String WEBF = "http://samimi.web.id/dev/poinf.php";
    public static final String URL_UPD_POINT = "http://samimi.web.id/dev/admin/fbase-tagihan.php";
    public static final String POST_POINT = "poin";
    public static final String POST_ID = "id";

    /*Shared Preference*/
    public static final String PREF_NAME = "app-welcome";
    public static final String DISPLAY_NAME = "displayName";
    public static final String DISPLAY_NUMBER = "displayNumber";
    public static final String DISPLAY_EMAIL = "displayEmail";
    public static final String DISPLAY_FIREBASE_ID = "firebaseId";
    public static final String DISPLAY_POIN = "poin";
    public static final String DISPLAY_ID = "id";
    public static final String DISPLAY_IDUSR = "idUsr";
    public static final String DISPLAY_STATUS = "status";
    public static final String IS_FIRST_TIME_TEMP = "IsFirstTimeTemp";
    public static final String IS_FIRST_TIME_ADD = "IsFirstTimeAdd";
    public static final String IS_FIRST_TIME_DOMPET = "IsFirstTimeDompet";
    public static final String IS_FIRST_TIME_TR = "IsFirstTimeTr";
    public static final String KODE_STATUS_SERVER = "kodeStatusServer";
    /**/
    public static final String URL_KODE="http://samimi.web.id/dev/add-code.php";
    public static final String URL_SELECT_CUSTOMER="http://samimi.web.id/dev/select-customer.php";
    public static final String URL_SELECT_ALL="http://samimi.web.id/dev/select-all.php?email=";
    public static final String URL_ADD_SALDO="http://samimi.web.id/dev/tambah-saldo.php";


    /*url and post variable request -> query kode provider*/
    public static final String URL_QUERY_KODE="http://samimi.web.id/dev/post-request.php";
    public static final String TAG_PROVIDER = "provider";
    public static final String TAG_NOMINAL = "nominal";
    public static final String TAG_HARGA_PROV = "harga";

    /*post variables for transaksi pulsa
    * $email = $_POST['email'];
    $firebase = $_POST['firebaseId'];
    $transaksi = $_POST['kodeTrans'];
    $nomorTuj = $_POST['nomor'];
    $status = $_POST['status'];
    $formatTrx = $_POST['formatTrx'];
    */
    public static final String TRX_URL = "http://samimi.web.id/dev/insert-log-transaksi.php";
    public static final String TAG_KETERANGAN = "keterangan";
    public static final String TAG_KETERANGAN_SALDO = "saldo";
    public static final String TRX_PULSA_EMAIL = "email";
    public static final String TRX_PULSA_FIREBASEID = "firebaseId";
    public static final String TRX_PULSA_KODE = "kodeTrans";
    public static final String TRX_PULSA_NOMORTUJ = "nomor";
    public static final String TRX_PULSA_FORMATTRX = "formatTrx";

    /*post variables cek tagihan
    * $fbase = $_POST['fbaseUid'];
      $nomorTagihan = $_POST['nomorTag'];*/
    public static final String URL_INSERT_TAGIHAN ="http://samimi.web.id/dev/cek-tagihan.php";
    public static final String FBASE_UID = "fbaseUid";
    public static final String NO_TAGIHAN = "nomorTag";
    public static final String JENIS = "jenis";
    public static final String FORMAT = "formatTrx";

    public static final String URL_GET_TAGIHAN = "http://samimi.web.id/dev/jml-tagihan.php?nomortag=";
    public static final String JML_TAGIHAN = "tagihan";

    //variable and json tag for show tagihan
    /* "jenis"=>$row['jenis'],
        "nomortagihan"=>$row['nomor_tagihan'],
        "tagihan"=>$row['tagihan']

        array_push($result,array(
 "id"=>$rowunpay['id_inbox'],
 "jenis"=>$rowunpay['jenis'],
 "keterangan"=>$rowunpay['keterangan'],
 "kettagihan"=>$rowunpay['ket_tagihan'],
 "message"=>$rowunpay['message']*/
    public static final String URL_SHOW_TAGIHAN = "http://samimi.web.id/dev/admin/select-tagihan.php";
    public static final String TAG_JENIS_TAGIHAN = "jenis";
    public static final String TAG_KET_TAGIHAN = "kettagihan";
    public static final String TAG_KET_INBOX = "keterangan";
    public static final String TAG_MES_INBOX = "message";
    public static final String TAG_ID_TAGIHAN = "id";
    public static final String TAG_ID_TAGIH = "idtagih";
    public static final String POST_DELETE = "deltagihan";
    public static final String POST_BAYAR = "bayar";


    //variable and json tag for select harga token
    /*
			"id"=>$row['id_harga'],
			"keterangan"=>$row['keterangan'],
			"harga"=>$harga*/

    public static final String URL_HARGA_TOKEN = "http://samimi.web.id/dev/select-token.php";
    public static final String TAG_ID_HARGA = "id";
    public static final String TAG_KETERANGAN_TOKEN = "keterangan";
    public static final String TAG_HARGA_TOKEN = "harga";
    public static final String TAG_KODE_TOKEN = "kode";

    //variable and json tag select voucher game
    /*"id"=>$row['id_harga'],
            "keterangan"=>$row['keterangan'],
			"kode"=>$row['kode'],
			"nominal"=>$row['nominal'],
			"harga"=>$harga*/
    public static final String URL_VOUCHER_GAME = "http://samimi.web.id/dev/select-vgame.php";
    public static final String TAG_POST_KETERANGAN = "keteranganv";
    public static final String TAG_KODE_ITEM = "kode";
    public static final String TAG_HARGA_ITEM = "harga";
    public static final String TAG_KETERANGAN_ITEM = "keterangan";

    //
    public static final String KEY_TKODE_NAME = "name";
    public static final String KEY_TKODE_KODE = "kode";

    //json tags for select customer
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_id="id";
    public static final String TAG_NAME="user_name";
    public static final String TAG_EMAIL="user_email";
    public static final String TAG_PHONE="user_phone";
    public static final String TAG_BALANCE="user_balance";
    public static final String TAG_POINT="user_point";
    public static final String TAG_STATUS="user_status";

    /*json tags for input table tambahSaldo.php
    * $date = $_POST['uDate'];
      $time = $_POST['uTime'];
      $last_update = $date." ".$time;
      $nomorRekUser = $_POST['nomorRekUser'];
      $namaRekUser = $_POST['namaRekUser'];
      $email = $_POST['email'];
      $rekening = $_POST['rekening'];
      $jumlahTransfer = $_POST['jmlTransfer'];*/
    public static final String TAG_NOREK_USER = "nomorRekUser";
    public static final String TAG_NAMAREK_USER = "namaRekUser";
    public static final String TAG_REK_TUJ = "rekening";
    public static final String TAG_JML_TRF = "jmlTransfer";
    public static final String TAG_EMAIL_USER = "email";

    /*url and json data for history*/
    public static final String URL_HIST_TRX_NEW = "http://samimi.web.id/dev/hist-tr-new.php?id=";
    public static final String ARRAY_HIST_DETAIL = "detail";
    public static final String ARRAY_HIST_DETAIL_PULSA = "pulsa";
    public static final String ARRAY_HIST_DETAIL_TOKEN = "token";
    public static final String ARRAY_HIST_DETAIL_TAGIHAN = "tagihan";
    public static final String ARRAY_HIST_DETAIL_VOUCHER = "voucher";
    public static final String HIST_DATA_LASTDATESALDO = "last_date_saldo";
    public static final String HIST_DATA_LASTSALDO = "last_saldo";
    public static final String HIST_DATA_CURRENTSALDO = "current_saldo";
    public static final String HIST_DATA_TOTALTRX = "total_trx";
    public static final String HIST_DATA_TOTALPULSA = "total_pulsa";
    public static final String HIST_DATA_TOTALTAGIHAN = "total_tagihan";
    public static final String HIST_DATA_TOTALTOKEN = "total_token";
    public static final String HIST_DATA_TOTALVOUCHER = "total_voucher";
    public static final String ARRAY_HIST_LAST_THREE = "last_three";
    public static final String ARRAY_HIST_LAST_SEVEN = "last_seven";
    public static final String ARRAY_HIST_LAST_THREET = "last_threet";
}

