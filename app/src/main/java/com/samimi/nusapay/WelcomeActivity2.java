package com.samimi.nusapay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.samimi.nusapay.configuration.Config;
import com.samimi.nusapay.configuration.RequestHandler;
import com.samimi.nusapay.preference.PrefManager;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.HashMap;

public class WelcomeActivity2 extends AppCompatActivity  implements
        View.OnClickListener{

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    /**/
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    /**/
    String userName, userEmail, id;
    String userNumber;
    RelativeLayout rel, relui1, relui2;
    TextView txtakun, txt2, txtNo;
    EditText eduserNumber, uname, umail, ufbase;
    Button next1, next2;
    GoogleApiClient mGoogleApiClient;
    SignInButton signInButton;
    PrefManager prefManager;
    String imgAcc;
    String usrStatus;
    Button bsign;

     FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    FirebaseAuth.AuthStateListener mAuthListener;

    CheckBox cbx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(this);
        /*if (!prefManager.isFirstTimeLaunch()) {
            launchHome();
            finish();
        }*/
        setContentView(R.layout.activity_welcome_2);

        initAnim();
        findViewById(R.id.bsign).setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);
        findViewById(R.id.btn_next2).setOnClickListener(this);

        /*initialize google sign in*/
        /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.api_sign_in))
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(WelcomeActivity2.this)
                .enableAutoManage(this  *//*FragmentActivity*//* , this  *//*OnConnectionFailedListener*//* )
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(this)
                .build();*/
        /*mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(WelcomeActivity.this  *//*FragmentActivity*//* , WelcomeActivity.this  *//*OnConnectionFailedListener*//* )
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();*/
        /*mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();*/
        /**/
        /*1st ui*/
        relui1 = (RelativeLayout) findViewById(R.id.ui1);
        relui2 = (RelativeLayout) findViewById(R.id.ui2);
        eduserNumber = (EditText) findViewById(R.id.usrnumber);
        uname = (EditText) findViewById(R.id.uname);
        umail = (EditText) findViewById(R.id.umail);
        ufbase = (EditText) findViewById(R.id.ufbase);
        rel = (RelativeLayout) findViewById(R.id.activity_welcome);
        bsign = (Button) findViewById(R.id.bsign);
        //txtakun = (TextView) findViewById(R.id.textView);
        //signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        next1 = (Button) findViewById(R.id.btn_next);
        /*2nd ui*/
        //txt2 = (TextView) findViewById(R.id.textView2);
        txtNo = (TextView) findViewById(R.id.textViewNo);
        next2 = (Button) findViewById(R.id.btn_next2);
        cbx = (CheckBox) findViewById(R.id.cbx);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                //updateUI(user);
                // [END_EXCLUDE]
            }
        };
        //mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        /*mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {

                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };*/



    }

    private void initAnim() {
        YoYo.with(Techniques.DropOut).duration(1000).playOn(findViewById(R.id.text1));
        YoYo.with(Techniques.StandUp).duration(1500).playOn(findViewById(R.id.text2));
        YoYo.with(Techniques.BounceInLeft).duration(1500).playOn(findViewById(R.id.garis));
        //YoYo.with(Techniques.BounceInRight).duration(1500).playOn(findViewById(R.id.textView));
        //YoYo.with(Techniques.BounceInLeft).duration(1500).playOn(findViewById(R.id.sign_in_button));
        YoYo.with(Techniques.ZoomIn).duration(1500).playOn(findViewById(R.id.text3));
        YoYo.with(Techniques.Wobble).duration(1500).playOn(findViewById(R.id.textViewNo));
        YoYo.with(Techniques.BounceInRight).duration(1500).playOn(findViewById(R.id.usrnumber));
        YoYo.with(Techniques.Tada).duration(1500).playOn(findViewById(R.id.tketerangan2));
    }

    private void launchHome() {

        Intent intent = new Intent(WelcomeActivity2.this, TempActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bsign:
                userName = uname.getText().toString();
                userEmail = umail.getText().toString();
                imgAcc = "";
                String token = ufbase.getText().toString();
                String tokenfbase = FirebaseInstanceId.getInstance().getToken();
                prefManager.setFirebaseId(tokenfbase);
                prefManager.setUserDisplay(userName);
                prefManager.setUserEmail(userEmail);
                prefManager.setUri(imgAcc);
                InputMethodManager imm = (InputMethodManager) WelcomeActivity2.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                signIn(userEmail, token);
                break;
            case R.id.btn_next:
                if (uname.getText().toString().matches("") || umail.getText().toString().matches("") || ufbase.getText().toString().matches("")) {
                    Toast.makeText(this, "mohon melengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show();
                } else {

                    //txtakun.setText(userName + "\n" + userEmail);
                }

                updateUI();
                break;
            case R.id.btn_next2:

                userNumber = eduserNumber.getText().toString();
                prefManager.setUserNumber(userNumber);
                prefManager.setFirstTimeLaunch(false);
                String string = "first time = false";
                FileOutputStream outputStream;

                try {
                    outputStream = openFileOutput(Config.FIRST_TIME, Context.MODE_PRIVATE);
                    outputStream.write(string.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                checkCustomer();
                /*launchHome();*/
                break;
        }
    }

    private void updateUI() {
        /*String userNumber = "";*/
        /*GONE-ing 1st ui*/
        relui1.setVisibility(View.GONE);
        findViewById(R.id.activity_welcome).setBackground(getResources().getDrawable(R.drawable.bgts));

        //String token = FirebaseInstanceId.getInstance().getToken();

        /*VISIBLE-ing 2nd ui*/
        relui2.setVisibility(View.VISIBLE);


        cbx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbx.isChecked()) {
                    next2.setEnabled(true);
                    next2.setTextColor(Color.WHITE);
                } else {
                    next2.setEnabled(false);
                    next2.setTextColor(Color.GRAY);
                }

            }
        });

        //txtNo.setText(userNumber);
    }




    /*@SuppressLint("StringFormatInvalid")
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());

        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            userName = acct.getDisplayName();
            userEmail = acct.getEmail();
            imgAcc = acct.getPhotoUrl().toString();
            Log.d("Google result ", imgAcc);
            txtakun.setText(userName + "\n" + userEmail);
            prefManager.setUserDisplay(userName);
            prefManager.setUserEmail(userEmail);
            prefManager.setUri(imgAcc);

            firebaseAuthWithGoogle(acct);

            next1.setTextColor(Color.WHITE);
            next1.setEnabled(true);

        }

    }
*/
    /*private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(WelcomeActivity2.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }*/

    private void checkCustomer() {
        class CheckCustomer extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(WelcomeActivity2.this, "Proccessing ..", "Reading Server", false, false);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                showCustomer(s);
                Toast.makeText(WelcomeActivity2.this, "Welcome", Toast.LENGTH_LONG).show();
                launchHome();
            }


            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.TAG_NAME, userName);
                params.put(Config.TAG_EMAIL, userEmail);
                params.put(Config.TAG_PHONE, userNumber);

                RequestHandler requestHandler = new RequestHandler();
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
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            id = c.getString(Config.POST_ID);
            usrStatus = c.getString("user_status");
            Log.d("Id from Server : ", id);
            //if (!usrStatus.contains("Advance")) {
                prefManager.setStatus(usrStatus);
                Log.d("prefManager : ", "sukses set pref status user");
            //}
            prefManager.setIdUsr(id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            if (hasFocus) {
                getWindow().getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }

    /*senin, 13 feb '17*/

    @Override
    protected void onStart() {
        //Log.v("Google API papoi:", "Starting");
        //mGoogleApiClient.connect();

        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        //mGoogleApiClient.disconnect();
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void signIn(final String email, String password) {
        Log.d(TAG, "signIn:" + email);

        /*if (!validateForm()) {
            return;
        }

        showProgressDialog();*/

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        Toast.makeText(WelcomeActivity2.this, "Sign in Success\nWelcome "+email, Toast.LENGTH_SHORT).show();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(WelcomeActivity2.this, "Authentication Failed",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "mAuth Failed");
                        }
                        //hideProgressDialog();
                        next1.setTextColor(Color.WHITE);
                        next1.setEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    /*private void showErrorDialog(int errorCode) {

        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();

        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }
*/

    /*public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() { }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((WelcomeActivity2) getActivity()).onDialogDismissed();
        }
    }

    private void onDialogDismissed() {
        mResolvingError = false;
    }*/
}
