package com.kinitoapps.quicknotes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;

import android.text.TextUtils;
import android.util.Log;

import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static com.kinitoapps.quicknotes.PdfDownloader.openPDFFile;

public class PDFDownloaderAsyncTask  extends AsyncTask<ArrayList<String>, Void, String> {

    private boolean isDownloadingPdf ;


    private File file;
    @SuppressLint("StaticFieldLeak")
    private Context context;

    public PDFDownloaderAsyncTask (Context context, File file) {

        this.file = file;
        this.context = context;
        this.isDownloadingPdf = false;
    }

    public boolean isDownloadingPdf () {

        return this.isDownloadingPdf;
    }

    @Override
    protected void onPreExecute () {

        super.onPreExecute ();

        // SHOW THE SPINNER WHILE LOADING FEEDS
        //linlaHeaderProgress.setVisibility(View.VISIBLE);
        //show loader etc
    }

    @SafeVarargs
    @Override
    protected final String doInBackground(ArrayList<String>... params) {

        isDownloadingPdf = true;
        File file = new File (params[0].get (0));
        String fileStatus = PdfDownloader.downloadFile (params[0].get (1), file);
        Log.v("dekh",params[0].get (0));
        return fileStatus;
    }

    @Override
    protected void onPostExecute (String result) {

        super.onPostExecute (result);
        Log.v("result",result);
       // Loader.hideLoader ();
        if (!TextUtils.isEmpty (result) && result.equalsIgnoreCase ("SUCCESS")) {
            showPdf ();
//            ((Activity)context).finish(); /// yeh Bhi BC problem hi h

            // HIDE THE SPINNER AFTER LOADING FEEDS
           // linlaHeaderProgress.setVisibility(View.GONE);
        }
        else {
            isDownloadingPdf = false;
            Toast.makeText (context, "could not download", Toast.LENGTH_LONG).show ();
            file.delete ();
            // HIDE THE SPINNER AFTER LOADING FEEDS
           // linlaHeaderProgress.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCancelled () {

        isDownloadingPdf = false;
        super.onCancelled ();
        //Loader.hideLoader ();
    }


    private void showPdf () {

        new Handler().postDelayed (new Runnable () {
            @Override
            public void run () {

                isDownloadingPdf = false;
                openPDFFile (context, Uri.fromFile (file));
               // ((Activity)context).finish();

            }
        }, 1000);
    }
}
