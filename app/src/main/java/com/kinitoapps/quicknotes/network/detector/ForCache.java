package com.kinitoapps.quicknotes.network.detector;

import com.google.firebase.database.FirebaseDatabase;


public class ForCache extends android.app.Application  {
    private static ForCache mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
  /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mInstance = this;
    }

    public static synchronized ForCache getInstance() {
        return mInstance;
    }


}
