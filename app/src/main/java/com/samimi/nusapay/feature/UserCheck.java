package com.samimi.nusapay.feature;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.samimi.nusapay.TempActivity;
import com.samimi.nusapay.configuration.Config;
import com.samimi.nusapay.preference.PrefManager;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by aris on 15/07/18.
 */

public class UserCheck extends AsyncTask<Void, Void, String> {

    Context context;
    private PrefManager prefManager;
    private SharedPreferences sharedPreferences;

    public UserCheck(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        prefManager = new PrefManager(context);
        sharedPreferences = context.getSharedPreferences(Config.PREF_NAME, MODE_PRIVATE);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("banned")
                .child(sharedPreferences.getString(Config.DISPLAY_IDUSR, "")).child("status");
        myRef.keepSynced(true);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status= String.valueOf(dataSnapshot.getValue());
                //Toast.makeText(TempActivity.this, status, Toast.LENGTH_LONG).show();
                if (status.matches("true")) {
                    Intent intent = new Intent(context, BannedActivity.class);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
