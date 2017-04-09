package com.samimi.nusapay;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.samimi.nusapay.preference.PrefManager;

public class MainActivity extends AppCompatActivity {

    TextView user, email;
    PrefManager prefManager;
    String textUser, txtEmail;
    private static final String PREF_NAME = "app-welcome";
    private static final String DISPLAY_NAME = "displayName";
    private static final String DISPLAY_EMAIL = "displayEmail";

    PredictNumber predictNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefManager=new PrefManager(this);

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        email = (TextView) findViewById(R.id.txtemail);
        user=(TextView)findViewById(R.id.txtuser);
        textUser = (sharedPreferences.getString(DISPLAY_NAME, ""));
        txtEmail = (sharedPreferences.getString(DISPLAY_EMAIL, ""));
        user.setText(textUser);
        email.setText(txtEmail);
        /*edUser.setText(number);*/

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

        predictNumber = new PredictNumber(this);
        predictNumber.readNumber("081126");

        String kartu = predictNumber.getTypeNumber();
        user.setText(kartu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
