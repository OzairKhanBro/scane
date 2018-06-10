package com.example.uzair.scane;


import android.net.Uri;

import java.net.URI;

/**
 * Created by uzair on 6/5/18.
 */

public class ImageDetails  {
    private  String name;
    private  String created;
    private  String size;
    private Uri uri;

    private boolean Checked=false;

    public boolean isChecked() {
        return Checked;
    }

    public void setChecked(boolean checked) {
        Checked = checked;
    }


    public ImageDetails(Uri uri) {
        this.uri = uri;
    }

    public ImageDetails(String name, String created, String size, Uri uri) {
        this.name = name;
        this.created = created;
        this.size = size;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
