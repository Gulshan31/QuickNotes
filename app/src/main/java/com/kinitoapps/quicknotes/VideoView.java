package com.kinitoapps.quicknotes;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;


import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.kinitoapps.quicknotes.data.YoutubePlayConfig;


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
import java.util.ArrayList;
import java.util.List;


public class VideoView extends  AppCompatActivity implements YouTubeThumbnailView.OnInitializedListener, YouTubeThumbnailLoader.OnThumbnailLoadedListener,BlankFragment.OnFragmentInteractionListener {
     List<String> Title;
    YouTubeThumbnailView thumbnailView;
    YouTubeThumbnailLoader thumbnailLoader;
    List<String> videoIds;
    ProgressBar progressBar;
    List <Drawable> loadedThumbnails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_video_view);
        Title = new ArrayList<>();
        videoIds = new ArrayList<>();
        loadedThumbnails = new ArrayList<>();
        thumbnailView = new YouTubeThumbnailView(this);
        thumbnailView.initialize(YoutubePlayConfig.YOUTUBE_API_KEY, this);
        progressBar = findViewById(R.id.loading_progress_bar);

//        new JsonTask().execute("https://www.googleapis.com/youtube/v3/videos?id= OLUWpt64GMc&key=AIzaSyDWE2dPve6-KyTQ9HBD2cjwnNv8LFEE18g\n" +
//                "&part=snippet,contentDetails,statistics,status");

    }

    @Override
    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
        thumbnailLoader = youTubeThumbnailLoader;
        youTubeThumbnailLoader.setOnThumbnailLoadedListener(VideoView.this);

        thumbnailLoader.setPlaylist("PLbu_fGT0MPsupSwt-hp6N1pUOxa-Mit_S");
        Log.v("Chla", String.valueOf(youTubeThumbnailView));


    }

    @Override
    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {

          loadedThumbnails.add(youTubeThumbnailView.getDrawable());
          videoIds.add(s);
           add();
           Log.v("Chla1",loadedThumbnails.toString()+videoIds);
    }

    private void add() {
        if (thumbnailLoader.hasNext())
            thumbnailLoader.next();
       else {

            for ( int jso = 0; jso < loadedThumbnails.size(); jso++) {
//                Log.v("Load"+jso,loadedThumbnails.toString()+ Title.toString()+videoIds.toString());
                new JsonTask().execute("https://www.googleapis.com/youtube/v3/videos?id=" + videoIds.get(jso) + "&key=AIzaSyDWE2dPve6-KyTQ9HBD2cjwnNv8LFEE18g\n" +
                        "&part=snippet,contentDetails,statistics,status");
            }

        }


        //  loadFragment(new , BlankFragment());
    }
//
//    private void loadFragment(BlankFragment blankFragment) {
//        FragmentManager fm = getSupportFragmentManager();
//       // create a FragmentTransaction to begin the transaction and replace the Fragment
//         FragmentTransaction fragmentTransaction = fm.beginTransaction();
////// replace the FrameLayout with new Fragment
//         fragmentTransaction.replace(R.id.frameLayout, blankFragment);
//         fragmentTransaction.commit(); // save the changes
//
//
//
//    }

    @Override
    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


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
                   Title.add(title);
//                Log.v("Load",Title.toString());
//                movie = new DataModelForYoutubeData(title,  thumbnailViews, VideoId);
//                data.add(movie);
//                adapter.notifyDataSetChanged();


            } catch (JSONException e) {
                e.printStackTrace();
            }
            finally{
                progressBar.setVisibility(View.GONE);

                loadFragment(new BlankFragment());

            }
            Log.v("Load","OnStart");
//        loadFragment(new PlayVideo());

            super.onPostExecute(s);
        }

        private void loadFragment(BlankFragment blankFragment) {
            ArrayList<String> title = new ArrayList<>(Title.size());
            ArrayList<String> video_ids = new ArrayList<>(videoIds.size());
            ArrayList<Drawable> thumb_nails = new ArrayList<>(loadedThumbnails.size());

            video_ids.addAll(videoIds);
            title.addAll(Title);
            thumb_nails.addAll(loadedThumbnails);
            Bundle bundle = new Bundle();
            bundle.putSerializable("valuesTitle", title);
            bundle.putSerializable("valuesVideo_ids", video_ids);
            bundle.putSerializable("valuesThumbnails", thumb_nails);
            blankFragment.setArguments(bundle);
            FragmentManager fm = getSupportFragmentManager();
            // create a FragmentTransaction to begin the transaction and replace the Fragment
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
//// replace the FrameLayout with new Fragment
            fragmentTransaction.replace(R.id.frameLayout, blankFragment);
            fragmentTransaction.commit(); // save the changes

        }
    }

}
