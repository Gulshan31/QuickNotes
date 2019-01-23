package com.kinitoapps.quicknotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kinitoapps.quicknotes.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PdfDownloader    extends AppCompatActivity {
    private static final int MEGABYTE = 2048 * 1024;

    public static String downloadFile (String fileUrl, File directory) {

        String downloadStatus;
        try {
            URL url = new URL (fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection ();
            urlConnection.connect ();

            InputStream inputStream = urlConnection.getInputStream ();
            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            int totalSize = urlConnection.getContentLength ();

            Log.d ("PDF", "Total size: " + totalSize);
            byte[] buffer = new byte[MEGABYTE];
            int bufferLength ;
            while ((bufferLength = inputStream.read (buffer)) > 0) {
                fileOutputStream.write (buffer, 0, bufferLength);
            }
            downloadStatus = "success";
            fileOutputStream.close ();
            Log.v("YES",downloadStatus);


        }
        catch (FileNotFoundException e) {
            downloadStatus = "FileNotFoundException";
            e.printStackTrace ();
        }
        catch (MalformedURLException e) {
            downloadStatus = "MalformedURLException";
            e.printStackTrace ();
        }
        catch (IOException e) {
            downloadStatus = "IOException";
            e.printStackTrace ();
        }
        Log.d ("PDF", "Download Status: " + downloadStatus);
        return downloadStatus;
    }


    public static void openPDFFile (Context context, Uri path) {


        Intent intent = new Intent (context,Index.class);
        intent.putExtra("path",path.toString());
        context.startActivity(intent);
        ((Activity)context).finish();

    }


    public static File getAppDirectory (Context context) {

        File extStorageDirectory = context.getFilesDir();
        File folder = new File(extStorageDirectory, context.getString (R.string.app_folder_name).trim ());


                    if (!folder.exists ()) {

                boolean success = folder.mkdirs();
                Log.d ("Directory", "mkdirs():" + success);

            }



        return folder;

    }





}