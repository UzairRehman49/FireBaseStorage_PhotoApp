package com.example.demo;

import android.net.Uri;

public class UploadData {
    String mImageUri;
    String mName;
    public  UploadData()
    {}
    public UploadData(String name,String uri)
    {
        mImageUri=uri;
        if(name.trim()=="") {
            mName = "No Name";
        }
        else
            mName = name;
    }
    public String getName()
    {
        return mName;
    }
    public void setName(String name)
    {
        if(name.trim()=="") {
            mName = "No Name";
        }
        else
            mName = name;
    }
    public String geturi()
    {
        return mImageUri;
    }
    public void seturi(String name)
    {

            mImageUri = name;
    }
}
