package com.samimi.nusapay.services;

import android.util.Log;

import com.samimi.nusapay.preference.PrefManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by aris on 03/12/16.
 */

public class InstanceIdService extends FirebaseInstanceIdService {

    PrefManager prefManager;

    private static final String TAG = "FirebaseId";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);


        //prefManager.setFirebaseId(refreshedToken);
    }
}
