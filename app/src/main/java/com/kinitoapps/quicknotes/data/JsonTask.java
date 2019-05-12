package com.kinitoapps.quicknotes.data;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kinitoapps.quicknotes.BlankFragment;
import com.kinitoapps.quicknotes.R;
import com.kinitoapps.quicknotes.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JsonTask extends AsyncTask<String,String,String> {
    @Override
    protected String doInBackground(String... strings) {


        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");

                Log.d("Response: ", "> "+ line);   //here u ll get whole response...... :-)

            }

//                Log.d("Respo", String.valueOf(buffer));

            return buffer.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {


        try {
            JSONObject jsonObject = new JSONObject(s);
            // Getting JSON Array node
            JSONArray items = jsonObject.getJSONArray("items");

            JSONObject c = items.getJSONObject(0);
            // Phone node is JSON Object
            JSONObject phone = c.getJSONObject("snippet");
            String title = phone.getString("title");

           Log.v("Json",title);
//                Log.v("Load"+jso,title);
//                Title.add(title);
//                Log.v("Load",Title.toString());
//                movie = new DataModelForYoutubeData(title,  thumbnailViews, VideoId);
//                data.add(movie);
//                adapter.notifyDataSetChanged();


        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally{
            loadFragment(new BlankFragment());

        }
        Log.v("Load","OnStart");
//        loadFragment(new PlayVideo());

        super.onPostExecute(s);
    }

    private void loadFragment(BlankFragment blankFragment) {
        Context context = new VideoView();
        AppCompatActivity activity = (AppCompatActivity) context;
        FragmentManager fm = activity.getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, blankFragment);
        fragmentTransaction.commit(); // save the changes

    }
}
