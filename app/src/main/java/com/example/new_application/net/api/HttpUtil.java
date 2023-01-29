

package com.example.new_application.net.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cambodia.zhanbang.rxhttp.net.utils.AESUtil;
import com.cambodia.zhanbang.rxhttp.net.utils.Base64Utils;
import com.cambodia.zhanbang.rxhttp.net.utils.StringMyUtil;
import com.cambodia.zhanbang.rxhttp.net.utils.SystemUtil;
import com.example.new_application.BuildConfig;
import com.example.new_application.utils.MyApplication;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * created  by ganzhe on 2019/11/16.
 */
public class HttpUtil {

    public static RequestBody postRequest(HashMap<String,Object> data ){
        putCommonParams(data);
        if(data.size()!=0){
                String encrypt = AESUtil.encrypt(JSON.toJSONString(data));
                data.put("validToken", encrypt.replace("\n",""));
            }

        RequestBody requestBody =null;
        JSONObject root = new JSONObject();
                Iterator<String> iterator = data.keySet().iterator();
                String key = "";
                while (iterator.hasNext()) {
                    key = iterator.next().toString();
                    Object obj = data.get(key);
                    if(obj instanceof JSONArray){
                        root.put(key,obj);
                    }else {
                        root.put(key, obj);
                    }
                }
                requestBody =RequestBody.create(MediaType.parse("application/json"), root.toString());
//                requestBody =RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), root.toString());
                return requestBody;

    }


    public static HashMap<String,Object> wwwPostRequestBody(HashMap<String,Object> data){
        Iterator<Map.Entry<String, Object>> iterator = data.entrySet().iterator();
        Map.Entry<String, Object> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            if (entry.getValue() == null) {
                iterator.remove();
            }
        }
        putCommonParams(data);
        if(data.size()!=0){
            String encrypt = AESUtil.encrypt(JSON.toJSONString(data));
            data.put("validToken", encrypt.replace("\n",""));
        }
        return data;
    }

    public static Map<String,Object> getRequest(HashMap<String,Object>data){
        putCommonParams(data);
        if(data.size()!=0){
            String encrypt = AESUtil.encrypt(JSON.toJSONString(data));
            data.put("validToken", encrypt.replace("\n",""));
        }
        return data;
    }
    public static String pathRequsest(String path){
        return path;
    }
    public static RequestBody uploadRequest(String filePath){
        File file = new File(filePath);//filePath为图片位置
        RequestBody  fileBody  = RequestBody.create(MediaType.parse("image/jpg"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();
        return requestBody;
    }


    private static RequestBody SetPostRequestBody(Map<String, Object> BodyParams) {
        RequestBody body = null;
        FormBody.Builder formEncodingBuilder = new FormBody.Builder();
        if (BodyParams != null) {
            Iterator<String> iterator = BodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                formEncodingBuilder.add(key, String.valueOf(BodyParams.get(key)));
            }
        }
        body = formEncodingBuilder.build();
        return body;
    }

    private static void putCommonParams(HashMap<String, Object> data) {
        data.put("timestamp", new Date().getTime());
        data.put("deviceNumber", SystemUtil.getUniqueId(MyApplication.getInstance()));//设备编号
        data.put("deviceIdentifier",String.format("%s",SystemUtil.getSystemModel()));//设备标识
        data.put("machineModel",String.format("%s",SystemUtil.getSystemModel()));//设备标识
        data.put("appVersion", BuildConfig.VERSION_NAME);//版本号
    }


}
