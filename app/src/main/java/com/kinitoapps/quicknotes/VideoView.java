package com.kinitoapps.quicknotes;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;

import com.google.android.youtube.player.YouTubePlayerView;
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

public class VideoView extends  YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, YouTubeThumbnailView.OnInitializedListener, YouTubeThumbnailLoader.OnThumbnailLoadedListener {
    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    YouTubePlayer Player;
    YouTubeThumbnailView thumbnailView;
    YouTubeThumbnailLoader thumbnailLoader;
    RecyclerView VideoList;
    List<String> Title;
    RecyclerView.Adapter adapter;
    List<Drawable> thumbnailViews;
    List<String> VideoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        thumbnailViews = new ArrayList<>();
        VideoId = new ArrayList<>();
        Title = new ArrayList<>();

        VideoList =  findViewById(R.id.youtube_playlist_recycler_view);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        VideoList.setLayoutManager(layoutManager);

        VideoList.setAdapter(adapter);
        youTubeView =  findViewById(R.id.youtube_player);
        youTubeView.initialize(YoutubePlayConfig.YOUTUBE_API_KEY, this);
        adapter=new VideoListAdapter();
        thumbnailView = new YouTubeThumbnailView(this);
        thumbnailView.initialize(YoutubePlayConfig.YOUTUBE_API_KEY, this);


    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
        Log.v("TASK","KAB CALL ");
        Player=youTubePlayer;
        Player.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean b) {
               VideoList.setVisibility(b?View.GONE: View.VISIBLE);
            }
        });


        if (!b){

            youTubePlayer.cueVideo("M8rrQHGTIbM");
        }




    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {


        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), youTubeInitializationResult.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(YoutubePlayConfig.YOUTUBE_API_KEY, this);
        }
    }
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }



    public void add() {

        if (thumbnailLoader.hasNext())
            thumbnailLoader.next();
        adapter.notifyDataSetChanged();

    }


    @Override
    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
        Log.v("TASK1","KAB CALL ");
        new JsonTask().execute("https://www.googleapis.com/youtube/v3/videos?id="+s+"&key=AIzaSyDWE2dPve6-KyTQ9HBD2cjwnNv8LFEE18g\n" +
                "&part=snippet,contentDetails,statistics,status");
        Log.v("TASK1","thumbnail se pehle ");
        thumbnailViews.add(youTubeThumbnailView.getDrawable());
        VideoId.add(s);



    }




    @Override
    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

    }

    @Override
    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
        Log.v("TASK2","KAB CALL ");

        thumbnailLoader = youTubeThumbnailLoader;
        youTubeThumbnailLoader.setOnThumbnailLoadedListener(VideoView.this);

        thumbnailLoader.setPlaylist("PLbu_fGT0MPsupSwt-hp6N1pUOxa-Mit_S");

//

    }

    @Override
    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

    }

    public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.MyView>{

        public class MyView extends RecyclerView.ViewHolder{

            ImageView imageView;
            TextView titleView ;
            public MyView(View itemView) {

                super(itemView);

                imageView=  itemView.findViewById(R.id.youtubeThumbnailView);
                titleView = itemView.findViewById(R.id.title);
            }


        }

        @Override
        public VideoListAdapter.MyView onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube_playlists_view_items, parent, false);
            return new MyView(itemView);
        }

        @Override
        public void onBindViewHolder(VideoListAdapter.MyView holder, final int position) {
            holder.imageView.setImageDrawable(thumbnailViews.get(position));
            holder.titleView.setText(Title.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Player.cueVideo(VideoId.get(position));
                }
            });
        }

        @Override
        public int getItemCount() {
            return thumbnailViews.size();
        }
    }


    private class JsonTask extends AsyncTask<String, String, String> {


        protected String doInBackground(String... params) {

            Log.v("Taskkkk1","kuch ");
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                  //  Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)


                }
                Log.d("Respo", String.valueOf(buffer));
                try {
                    JSONObject jsonObject = new JSONObject(buffer.toString());
                    // Getting JSON Array node
                    JSONArray items = jsonObject.getJSONArray("items");

                        JSONObject c = items.getJSONObject(0);
                        // Phone node is JSON Object
                        JSONObject snippet = c.getJSONObject("snippet");
                        String title = snippet.getString("title");
                        Title.add(title);
                        add();
                    Log.d("title",title);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
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




    }



}
