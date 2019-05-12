package com.kinitoapps.quicknotes.data;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kinitoapps.quicknotes.R;

import java.util.ArrayList;

public class YoutubeDataRecord  extends RecyclerView.Adapter<YoutubeDataRecord.ViewHolder>{
     Context context;
     String Title;
     Drawable thumbnail;
     String VideoIds;
    private ArrayList<DataModelForYoutubeData>dataModelForYoutubeData;




    public void update(String title, Drawable thumb,String video_ids){
        Title = title;
        thumbnail= thumb;
        VideoIds = video_ids;
        notifyDataSetChanged(); // this refreshes the recyclerview automatically so that the latest item populated
    }
    public YoutubeDataRecord(Context context,ArrayList<DataModelForYoutubeData> dataModelForYoutubeData) {
                this.dataModelForYoutubeData = dataModelForYoutubeData;
                this.context = context;
    }


    @NonNull
    @Override
    public YoutubeDataRecord.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.youtube_playlists_view_items,viewGroup,false);
        return new YoutubeDataRecord.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final YoutubeDataRecord.ViewHolder viewHolder, final int position) {

        final DataModelForYoutubeData data =  dataModelForYoutubeData.get(position);
        viewHolder.youtubeVideoThumbnail.setImageDrawable(data.getVideoThumbnail());
        viewHolder.youtubeVideoTitle.setText(data.getVideoTitle());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Log.v("clicked",data.getVideoId().get(position));

            }
        });
    }


    @Override
    public int getItemCount() {
        return dataModelForYoutubeData.size();
    }

//    @Override
//    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//
//        Player = youTubePlayer;
//        Player.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
//            @Override
//            public void onFullscreen(boolean b) {
//               // VideoList.setVisibility(b ? View.GONE : View.VISIBLE);
//            }
//        });
//
//
//        if (!b) {
//
//            youTubePlayer.cuePlaylist("PLbu_fGT0MPsupSwt-hp6N1pUOxa-Mit_S");
//        }
//
//
//    }
//
//    @Override
//    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView youtubeVideoTitle;
        private ImageView youtubeVideoThumbnail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            youtubeVideoTitle = itemView.findViewById(R.id.title);
            youtubeVideoThumbnail = itemView.findViewById(R.id.youtubeThumbnailView);
//             youTubeView = itemView. findViewById(R.id.youtube_player);
//            youTubeView.initialize(YoutubePlayConfig.YOUTUBE_API_KEY, YoutubeDataRecord.this);


        }

    }
}
