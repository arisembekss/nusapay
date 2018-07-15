package com.samimi.nusapay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samimi.nusapay.configuration.Config;
import com.samimi.nusapay.configuration.ConnectivityReceiver;
import com.samimi.nusapay.configuration.MyApplication;
import com.samimi.nusapay.configuration.RequestHandler;
import com.samimi.nusapay.feature.AboutActivity;
import com.samimi.nusapay.feature.BannedActivity;
import com.samimi.nusapay.feature.DompetActivity;
import com.samimi.nusapay.feature.InboxActivity;
import com.samimi.nusapay.feature.UserCheck;
import com.samimi.nusapay.preference.PrefManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;
import com.pkmmte.view.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

public class TempActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener{

    LayoutInflater layoutInflater ;
    View headerNav;
    UserCheck userCheck;
    TextView navemail, navusername, tbalance, tstatus;
    ImageButton imageButton;
    CircularImageView imageAcc;
    ImageView imageserver;
    PrefManager prefManager;
    NavigationView navigationView;
    SharedPreferences sharedPreferences;
    public String textUser, txtEmail, txtFirebaseId, txtNumber, idUsr;
    DrawerLayout drawer;
    CircleMenu circleMenu;
    LinearLayout linNote;
    TourGuide mTourGuideHandler, mTourGuideHandler2, mTourGuideHandler3;
    private static int SPLASH_TIME_OUT = 1100;

    String imguri, sstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        prefManager = new PrefManager(this);

        drawer = findViewById(R.id.drawer_layout);

        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();*/

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        checkConnection();

        /*Embeks*/
        //FirebaseDatabase database = FirebaseDatabase.getInstance();

        sharedPreferences = getSharedPreferences(Config.PREF_NAME, MODE_PRIVATE);
        //TODO check user status prev banned false or true
        userCheck = new UserCheck(TempActivity.this);
        userCheck.execute();

        initUi();

    }

    private void initTour() {
       mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle("Selamat Datang di Pay Point").setDescription("Klik Button Utama tersebut untuk kami perkenalkan beberapa fitur di Pay Point"))
                .setOverlay(new Overlay())
                .playOn(circleMenu);

        mTourGuideHandler2 = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle("Menu Utama").setDescription("Menu kami desain dalam bentuk icon. Click 'X' untuk menutup menu"))
                .setOverlay(new Overlay());
        mTourGuideHandler3 = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle("Menu Navigasi").setDescription("Keterangan dari ikon menu sebelumnya bisa anda liat di sini (membuka Menu Navigasi juga bisa anda lakukan dengan menggeser bagian kiri layar anda ke arah kanan)"))
                .setOverlay(new Overlay());

    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Koneksi Internet Bagus";
            color = Color.WHITE;
        } else {
            message = "Tidak Ada Koneksi Internet\nFitur-fitur tidak bisa dijalankan";
            color = Color.YELLOW;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.vsnack), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    private void initUi() {

        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        /*headerNav = layoutInflater.inflate(R.dialog_kota.nav_header_temp,null, true);*/
        linNote = findViewById(R.id.linNote);
        headerNav = navigationView.getHeaderView(0);

        navusername = headerNav.findViewById(R.id.navusername);
        navemail = headerNav.findViewById(R.id.navemail);
        tbalance = findViewById(R.id.tbalance);
        imageAcc = headerNav.findViewById(R.id.imageViewAcc);
        tstatus = findViewById(R.id.status);
        /*imageAcc = (ImageView) headerNav.findViewById(R.id.imageViewAcc);*/
        //Uri setImgAcc = Uri.parse();
        imguri = (sharedPreferences.getString(Config.DISPLAY_ID, ""));
        if (imguri.matches("")) {
            Log.d("Image Uri:", "null");
        } else {
            Glide.with(getApplicationContext()).load(imguri)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageAcc);
        }

        imageserver = findViewById(R.id.imgserver);
        sstatus = (sharedPreferences.getString(Config.KODE_STATUS_SERVER, ""));
        textUser = (sharedPreferences.getString(Config.DISPLAY_NAME, ""));
        txtEmail = (sharedPreferences.getString(Config.DISPLAY_EMAIL, ""));
        txtNumber = (sharedPreferences.getString(Config.DISPLAY_NUMBER, ""));
        txtFirebaseId = (sharedPreferences.getString(Config.DISPLAY_FIREBASE_ID, ""));
        idUsr = (sharedPreferences.getString(Config.DISPLAY_IDUSR, ""));
        if (idUsr.matches("") || idUsr.isEmpty()) {
            checkCustomer();
        } else {
            String status = (sharedPreferences.getString(Config.DISPLAY_STATUS, ""));
            Log.d("Has been set ", "id user: " + idUsr + ", status: " + status);
        }

        tstatus.setText(sstatus);
        if (sstatus.contains("Tidak")) {
            imageserver.setImageResource(R.drawable.red_circle_icon);
        } else {
            imageserver.setImageResource(R.drawable.green_circle_icon);
        }
        //tTempor = (TextView) findViewById(R.id.textViewTempor);

        navemail.setText(txtEmail);
        navusername.setText(textUser);

        imageButton = findViewById(R.id.openNav);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);

                if (prefManager.isTempFirstTimeLaunch()) {
                   mTourGuideHandler3.cleanUp();
                        //mTourGuideHandler3.playOn(imageButton);
                    prefManager.setTempFirstTimeLaunch(false);
                }

            }
        });

        if (prefManager.isTempFirstTimeLaunch()) {
            initTour();
        }
        /*circle menu*/
        circleMenu = findViewById(R.id.circle_menu);

        circleMenu.setMainMenu(Color.parseColor("#5481a2"), R.mipmap.ic_local_parking_white_24dp, R.mipmap.ic_close_white_24dp);
        circleMenu.addSubMenu(Color.parseColor("#258CFF"), R.mipmap.ic_grain_white_24dp)
                .addSubMenu(Color.parseColor("#30A400"), R.mipmap.ic_mail_outline_white_24dp)
                .addSubMenu(Color.parseColor("#FF4B32"), R.mipmap.ic_assistant_white_24dp)
                .addSubMenu(Color.parseColor("#8A39FF"), R.mipmap.ic_airport_shuttle_white_24dp)
                .addSubMenu(Color.parseColor("#FF6A00"), R.mipmap.ic_monetization_on_white_24dp);

        circleMenu.setOnMenuSelectedListener(new OnMenuSelectedListener() {

                                                 @Override
                                                 public void onMenuSelected(int index) {
                                                     switch (index) {
                                                         case 0:
                                                             Toast.makeText(TempActivity.this, "Go to Transaction", Toast.LENGTH_SHORT).show();
                                                             new Handler().postDelayed(new Runnable() {

                                                                 @Override
                                                                 public void run() {
                                                                     // This method will be executed once the timer is over
                                                                     // Start your app main activity
                                                                     Intent transaksi = new Intent(TempActivity.this, TransactActivity.class);
                                                                     startActivity(transaksi);

                                                                 }
                                                             }, SPLASH_TIME_OUT);


                                                             break;
                                                         case 1:
                                                             Toast.makeText(TempActivity.this, "Inbox", Toast.LENGTH_SHORT).show();
                                                             new Handler().postDelayed(new Runnable() {

                                                                 @Override
                                                                 public void run() {
                                                                     // This method will be executed once the timer is over
                                                                     // Start your app main activity
                                                                     Intent inbox = new Intent(TempActivity.this, InboxActivity.class);
                                                                     startActivity(inbox);

                                                                 }
                                                             }, SPLASH_TIME_OUT);

                                                             break;
                                                         case 2:
                                                             Toast.makeText(TempActivity.this, "Tambah Saldo", Toast.LENGTH_SHORT).show();
                                                             new Handler().postDelayed(new Runnable() {

                                                                 @Override
                                                                 public void run() {
                                                                     // This method will be executed once the timer is over
                                                                     // Start your app main activity
                                                                     Intent saldo = new Intent(TempActivity.this, AddSaldoActivity.class);
                                                                     startActivity(saldo);

                                                                 }
                                                             }, SPLASH_TIME_OUT);

                                                             break;
                                                         case 3:
                                                             Toast.makeText(TempActivity.this, "Coming Soon :\nPemesanan Tiket", Toast.LENGTH_SHORT).show();

                                                             break;
                                                         case 4:
                                                             Toast.makeText(TempActivity.this, "Dompet Clicked", Toast.LENGTH_SHORT).show();
                                                             new Handler().postDelayed(new Runnable() {

                                                                 @Override
                                                                 public void run() {
                                                                     // This method will be executed once the timer is over
                                                                     // Start your app main activity
                                                                     Intent dompet = new Intent(TempActivity.this, DompetActivity.class);
                                                                     startActivity(dompet);

                                                                 }
                                                             }, SPLASH_TIME_OUT);
                                                             //getJson();
                                                             break;
                                                     }
                                                 }
                                             }

        );

        circleMenu.setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {

                                                     @Override
                                                     public void onMenuOpened() {
                                                         //Toast.makeText(TempActivity.this, "Menu Opend", Toast.LENGTH_SHORT).show();
                                                         linNote.setVisibility(View.VISIBLE);
                                                         if (prefManager.isTempFirstTimeLaunch()) {
                                                             mTourGuideHandler.cleanUp();
                                                             mTourGuideHandler2.playOn(circleMenu);
                                                         }
                                                     }

                                                     @Override
                                                     public void onMenuClosed() {
                                                         //Toast.makeText(TempActivity.this, "Menu Closed", Toast.LENGTH_SHORT).show();
                                                         linNote.setVisibility(View.INVISIBLE);
                                                         if (prefManager.isTempFirstTimeLaunch()) {
                                                             mTourGuideHandler2.cleanUp();
                                                             mTourGuideHandler3.playOn(imageButton);
                                                         }
                                                     }
                                                 }
        );

        /*DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("server").child("status");
        myRef.keepSynced(true);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String sstatus= String.valueOf(dataSnapshot.getValue());
                tstatus.setText(sstatus);
                if (sstatus.contains("Tidak")) {
                    imageserver.setImageResource(R.drawable.red_circle_icon);
                } else {
                    imageserver.setImageResource(R.drawable.green_circle_icon);
                }
                Log.d("dbase real", sstatus);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        DatabaseReference myRefserver = FirebaseDatabase.getInstance().getReference().child("sms-server").child("stat-server");
        myRefserver.keepSynced(true);
        myRefserver.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int sstatus= Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
                //prefManager.setKodeStatusServer(sstatus);
                /*tstatus.setText(sstatus);
                if (sstatus.contains("Tidak")) {
                    imageserver.setImageResource(R.drawable.red_circle_icon);
                } else {
                    imageserver.setImageResource(R.drawable.green_circle_icon);
                }*/
                //Log.d("dbase real", sstatus);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*getting harga tri from realtime database*/
        /*final List<String> hTri = new ArrayList<>();
        final List<String> kTri = new ArrayList<>();
        DatabaseReference tri = FirebaseDatabase.getInstance().getReference().child("t");
        tri.keepSynced(true);
        tri.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Gson gson = new Gson();
                Log.d("count : ", "" + dataSnapshot.getChildrenCount());
                for (DataSnapshot childt : dataSnapshot.getChildren()) {
                    DumDum dummy = childt.getValue(DumDum.class);
                    String harga = String.valueOf(dummy.getHarga());
                    String kode = String.valueOf(dummy.getkode());
                    hTri.add(harga);
                    kTri.add(kode);
                    Log.d("datas : ", String.valueOf(dummy.getHarga()));
                }
                String jsonHarga = gson.toJson(hTri);
                String jsonKode = gson.toJson(kTri);
                prefManager.setHargaTri(jsonHarga);
                prefManager.setKodeTri(jsonKode);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.temp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.transaksinav) {
            // Handle the camera action
            Intent transaksi = new Intent(TempActivity.this, TransactActivity.class);
            startActivity(transaksi);
        } else if (id == R.id.inboxnav) {
            Intent inbox = new Intent(this, InboxActivity.class);
            startActivity(inbox);

        } else if (id == R.id.tambahsaldonav) {
            Intent saldo = new Intent(TempActivity.this, AddSaldoActivity.class);
            startActivity(saldo);
        } else if (id == R.id.tiketnav) {
            Toast.makeText(TempActivity.this, "Coming Soon :\nPemesanan Tiket", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.dompetnav) {
            Intent dompett = new Intent(TempActivity.this, DompetActivity.class);
            startActivity(dompett);
        } else if (id == R.id.nav_history) {
            Intent history = new Intent(TempActivity.this, HistoryActivity.class);
            startActivity(history);
        } else if (id == R.id.nav_send) {
            Intent about = new Intent(TempActivity.this, AboutActivity.class);
            startActivity(about);
        }

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void checkCustomer() {
        class CheckCustomer extends AsyncTask<Void, Void, String> {

            private ProgressDialog loading;
            RequestHandler requestHandler;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TempActivity.this, "Proccessing ..", "Reading Server", false, false);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                loading.dismiss();
                showCustomer(s);
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.TAG_NAME, textUser);
                params.put(Config.TAG_EMAIL, txtEmail);
                params.put(Config.TAG_PHONE, txtNumber);

                requestHandler = new RequestHandler();
                String result = requestHandler.sendPostRequest(Config.URL_SELECT_CUSTOMER, params);

                return result;
            }
        }
        CheckCustomer checkCustomer = new CheckCustomer();
        checkCustomer.execute();

        /*next1.setTextColor(Color.WHITE);
        next1.setEnabled(true);*/

    }

    private void showCustomer(String json) {
        JSONObject jsonObject;
        String id, usrStatus;

        try {
            jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            id = c.getString(Config.POST_ID);
            usrStatus = c.getString("user_status");
            Log.d("user id : ", id);
            //if (!usrStatus.contains("Advance")) {
            prefManager.setStatus(usrStatus);
            Log.d("prefManager : ", "sukses set pref status user: "+usrStatus);
            //}
            prefManager.setIdUsr(id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}
