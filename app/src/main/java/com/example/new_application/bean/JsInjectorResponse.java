package com.example.new_application.bean;

public class JsInjectorResponse {
    public final String data;
    public final String url;
    public final String mime;
    public final String charset;
    public final boolean isRedirect;

    public JsInjectorResponse(String data, int code, String url, String mime, String charset, boolean isRedirect) {
        this.data = data;
        this.url = url;
        this.mime = mime;
        this.charset = charset;
        this.isRedirect = isRedirect;
    }
}
