package com.kinitoapps.quicknotes.data;

import android.graphics.drawable.Drawable;


public class DataModelForYoutubeData {


    private String  VideoTitle;
    private Drawable VideoThumbnail;
     private  String VideoId;

    public DataModelForYoutubeData(String VideoTitle, Drawable VideoThumbnail, String VideoId){
        this.VideoThumbnail = VideoThumbnail;
        this.VideoTitle = VideoTitle;
        this.VideoId = VideoId;
   }


    public Drawable getVideoThumbnail() {
        return VideoThumbnail;
    }

    public String getVideoId() {
        return VideoId;
    }

    public void setVideoThumbnail( Drawable videoThumbnail) {
        VideoThumbnail = videoThumbnail;
    }

    public String getVideoTitle() {
        return VideoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        VideoTitle = videoTitle;
    }
}
