package com.kinitoapps.quicknotes;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kinitoapps.quicknotes.R;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;

import static com.kinitoapps.quicknotes.PdfDownloader.getAppDirectory;
import static com.kinitoapps.quicknotes.PdfDownloader.openPDFFile;

public class DownloadTask extends AppCompatActivity  {
    StorageReference storageReference;
    StorageReference ref;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_download_task);

        String index_name = getIntent().getStringExtra("index_name");
        String class_name = getIntent().getStringExtra("class_name");

        File folder = getAppDirectory(DownloadTask.this);

        File pdfFile = new File(folder, index_name);
        if (pdfFile.exists() && pdfFile.length() > 0) {
            openPDFFile(DownloadTask.this, Uri.fromFile(pdfFile));
            finish();
        }
        else
            download(index_name,class_name);
        }


    private void download(final String string, final  String class_name) {
        storageReference = FirebaseStorage.getInstance().getReference();
        ref = storageReference.child(class_name+"/" + string + ".pdf");

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                handleViewPdf(DownloadTask.this,string, url);



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void handleViewPdf ( Context context, String filename,   String url) {



            File folder = getAppDirectory(context);

            File pdfFile = new File(folder, filename);


            if (pdfFile.exists() && pdfFile.length() > 0) {
                openPDFFile(context, Uri.fromFile(pdfFile));
                finish();
            } else {
                if (pdfFile.length() == 0) {
                    pdfFile.delete();

                }
                try {
                    pdfFile.createNewFile();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                ArrayList<String> fileNameAndURL = new ArrayList<>();
                fileNameAndURL.add(pdfFile.toString());
                fileNameAndURL.add(url);
                fileNameAndURL.add(filename);
                PDFDownloaderAsyncTask pdfDownloaderAsyncTask = new PDFDownloaderAsyncTask(context, pdfFile);

                if (!pdfDownloaderAsyncTask.isDownloadingPdf()) {
                    pdfDownloaderAsyncTask = new PDFDownloaderAsyncTask(context, pdfFile);
                    pdfDownloaderAsyncTask.execute(fileNameAndURL);
                   // finish();  // Bc yehi problem thi
                }

            }
        }





}

