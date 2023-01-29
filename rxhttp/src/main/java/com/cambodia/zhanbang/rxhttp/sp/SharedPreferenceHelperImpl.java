package com.cambodia.zhanbang.rxhttp.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


import com.cambodia.zhanbang.rxhttp.net.common.RxLibConstants;
import com.cambodia.zhanbang.rxhttp.net.utils.AppContextUtils;

import static com.cambodia.zhanbang.rxhttp.net.common.RxLibConstants.HOME_LABEL;
import static com.cambodia.zhanbang.rxhttp.net.common.RxLibConstants.IMAGE_DOMAIN;
import static com.cambodia.zhanbang.rxhttp.net.common.RxLibConstants.ONLINE_CUSTOMER;
import static com.cambodia.zhanbang.rxhttp.net.common.RxLibConstants.SP_TOKEN;
import static com.cambodia.zhanbang.rxhttp.net.common.RxLibConstants.SYSTEM_PARAMS;
import static com.cambodia.zhanbang.rxhttp.net.common.RxLibConstants.USER_ID;
import static com.cambodia.zhanbang.rxhttp.net.common.RxLibConstants.USER_INFO;


public class SharedPreferenceHelperImpl /*implements SharedPreferenceHelper*/ {

    private final SharedPreferences mSharedPreferences;


    public SharedPreferenceHelperImpl() {
        mSharedPreferences = AppContextUtils.getContext().getSharedPreferences(RxLibConstants.SHAREDPREFERENCES_NAME,Context.MODE_PRIVATE);
    }



    public void setNewBaseUrl(String newUrl) {
        mSharedPreferences.edit().putString(RxLibConstants.NEW_BASE_URL,newUrl).apply();
    }

    public String getNewBaseUrl() {
        return mSharedPreferences.getString(RxLibConstants.NEW_BASE_URL,"");
    }

    public void setImageDomin(String imageDomin) {
        mSharedPreferences.edit().putString(IMAGE_DOMAIN,imageDomin).apply();
    }

    public String getImageDomin() {
        return mSharedPreferences.getString(IMAGE_DOMAIN,"");
    }

    public void setToken(String token) {
        mSharedPreferences.edit().putString(SP_TOKEN,token).apply();
    }
    public String getToken() {
        return mSharedPreferences.getString(SP_TOKEN,"");
    }
    public String getUserId() {
        return mSharedPreferences.getString(USER_ID,"");
    }

    public void setUserId(String id) {
        mSharedPreferences.edit().putString(USER_ID,id).apply();
    }

    public String getUserInfo() {
        return mSharedPreferences.getString(USER_INFO,"");
    }

    public void setUserInfo(String userinfo) {
        mSharedPreferences.edit().putString(USER_INFO,userinfo).apply();
    }

    public String getSystemParams() {
        return mSharedPreferences.getString(SYSTEM_PARAMS,"");
    }

    public void setSystemParams(String systemParams) {
        mSharedPreferences.edit().putString(SYSTEM_PARAMS,systemParams).apply();
    }

    public String getAllLabel() {
        return mSharedPreferences.getString(HOME_LABEL,"");
    }

    public void setAllLabel(String systemParams) {
        mSharedPreferences.edit().putString(HOME_LABEL,systemParams).apply();
    }
    public String getOnlineCustomer() {
        return mSharedPreferences.getString(ONLINE_CUSTOMER,"");
    }

    public void setOnlineCustomer(String systemParams) {
        mSharedPreferences.edit().putString(ONLINE_CUSTOMER,systemParams).apply();
    }

    public void setSearchCache(String liveSearchCache) {
        mSharedPreferences.edit().putString(RxLibConstants.SEARCH_CACHE,liveSearchCache).apply();
    }

    public String getSearchCache() {
        return mSharedPreferences.getString(RxLibConstants.SEARCH_CACHE,"");
    }
    public void setUrlList(String urlList) {
        mSharedPreferences.edit().putString(RxLibConstants.URL_LIST,urlList).apply();
    }

    public String getUrlList() {
        return mSharedPreferences.getString(RxLibConstants.URL_LIST,"");
    }

}
