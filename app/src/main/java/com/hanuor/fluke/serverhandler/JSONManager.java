package com.hanuor.fluke.serverhandler;

/**
 * Created by Shantanu Johri on 16-05-2016.
 */
public class JSONManager {
    private String track;
    private String artist;
    private String artistImage;
    private String fbUserpic;
    private String fbName;
    private String ebemail;

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtistImage() {
        return artistImage;
    }

    public void setArtistImage(String artistImage) {
        this.artistImage = artistImage;
    }

    public String getFbName() {
        return fbName;
    }

    public void setFbName(String fbName) {
        this.fbName = fbName;
    }

    public String getFbUserpic() {
        return fbUserpic;
    }

    public void setFbUserpic(String fbUserpic) {
        this.fbUserpic = fbUserpic;
    }

    public String getEbemail() {
        return ebemail;
    }

    public void setEbemail(String ebemail) {
        this.ebemail = ebemail;
    }

    public String toString() {
        return "[ "+fbName+", "+fbUserpic+", "+ebemail+", "+track+", "+artist+", "+artistImage+" ]";
    }


}
