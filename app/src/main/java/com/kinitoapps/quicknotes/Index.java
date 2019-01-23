package com.kinitoapps.quicknotes;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kinitoapps.quicknotes.R;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class    Index extends AppCompatActivity {
    PDFView pdfView ;
    Uri path;
    StorageReference ref;
    StorageReference storageReference;
    FirebaseUser user;
    private String m_Text;
    String promoCode ;
    DatabaseReference mDatabaseRefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE); // preventing the scren short of this activity.
        setContentView(R.layout.activity_index);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// preventing screen from sleep mode
      user =  FirebaseAuth.getInstance().getCurrentUser();
      mDatabaseRefrence = FirebaseDatabase.getInstance().getReference().child("Promocode");
      mDatabaseRefrence.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              promoCode = dataSnapshot.child("My code").getValue(String.class);
              Log.v("Promocode",promoCode);

          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });
        pdfView = findViewById(R.id.pdfView);
         path = Uri.parse(getIntent().getStringExtra("path"));
        Log.v("URIPATH",path.getLastPathSegment());
        pdfView.fromUri(path)
                // spacing between pages in dp. To define spacing color, set view background

                .load();

}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.browser, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_download) {

            if (user!=null) {


                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Enter code for downloading file");

// Set up the input
                final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        if (m_Text.equals(promoCode))
                         download(path.getLastPathSegment());
                        else
                        {

                            AlertDialog alertDialog = new AlertDialog.Builder(Index.this).create();
                            alertDialog.setTitle("ERROR");
                            alertDialog.setMessage("Wrong Promocode, Please Enter Right one");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();

                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
            else {

                Toast.makeText(Index.this,"Please Login for downloading",Toast.LENGTH_LONG).show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    private void download(final String string) {
        storageReference = FirebaseStorage.getInstance().getReference();
        ref = storageReference.child("10th Class Science's Notes/" + string + ".pdf");

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
//    File file = new File(context.getFilesDir(), "EasyNotes");

                  downloadFiles(Index.this, string, ".pdf", DIRECTORY_DOWNLOADS, url);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }



        private void downloadFiles(Context context, String filename, String fileextension, String destinationDirectory, String url) {

            DownloadManager downloadManager = (DownloadManager) context.
                    getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalFilesDir(context, destinationDirectory, filename + fileextension);
            downloadManager.enqueue(request);
            AlertDialog alertDialog = new AlertDialog.Builder(Index.this).create();
            alertDialog.setTitle("Downloaded");
            alertDialog.setMessage("Check Your Download Manager File Or Notification ");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });
            alertDialog.show();


        }




}


