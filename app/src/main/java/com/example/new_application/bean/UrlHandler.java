package com.example.new_application.bean;

import android.net.Uri;

public interface UrlHandler {

    String getScheme();

    String handle(Uri uri);
}