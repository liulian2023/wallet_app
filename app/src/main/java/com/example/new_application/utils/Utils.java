package com.example.new_application.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cambodia.zhanbang.rxhttp.net.utils.StringMyUtil;
import com.cambodia.zhanbang.rxhttp.sp.SharedPreferenceHelperImpl;
import com.example.new_application.MainActivity;
import com.example.new_application.R;
import com.example.new_application.base.BaseActivity;
import com.example.new_application.net.RequestUtils;
import com.example.new_application.net.api.HttpApiUtils;
import com.example.new_application.viewmodel.C;
import com.hjq.toast.ToastUtils;
import com.luck.picture.lib.style.PictureParameterStyle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.INPUT_METHOD_SERVICE;

public class Utils {
    // ???????????????????????????????????????????????????800??????
    private static final int MIN_CLICK_DELAY_TIME = 800;
    private static long lastClickTime;
    static SharedPreferenceHelperImpl mSharedPreferenceHelperImpl = new SharedPreferenceHelperImpl();
    public static String formatUrl(String url) {
        if (URLUtil.isHttpsUrl(url) || URLUtil.isHttpUrl(url)) {
            return url;
        } else {
            if (isValidUrl(url)) {
                return C.HTTP_PREFIX + url;
            } else {
                return C.GOOGLE_SEARCH_PREFIX + url;
            }
        }
    }
    public static boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url.toLowerCase());
        return m.matches();
    }
    /**
     * ??????????????????
     * @param activity activity??????
     * @param bgcolor ?????????(0f-1f)?????????,????????????
     */
    public static void darkenBackground(Activity activity, Float bgcolor) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgcolor;
        if(bgcolor==1f){
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }else {

            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        activity.getWindow().setAttributes(lp);
    }
    public static void copyStr(String label, String content){
        ClipboardManager clipboardManager= (ClipboardManager) MyApplication.getInstance().getSystemService(CLIPBOARD_SERVICE);//?????????clipboardManager??????
        ClipData bankCardNumData=  ClipData.newPlainText(label, content);//??????????????????????????????  newPlainText
        clipboardManager.setPrimaryClip(bankCardNumData);
        ToastUtils.show( "????????????");
    }
    public static HashMap<String, Long> urlTime = new HashMap<>();

    public static boolean fileIsExists(String strFile) {
        try {
            File sdCardDir = Environment.getExternalStorageDirectory();//??????SDCard??????
            File file = getParent();
            File f = new File(file, strFile + ".txt");
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }
    public static boolean deleteNormalFile(File file) {
        // ????????????????????????????????????????????????????????????????????????????????????
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
//                System.out.println("??????????????????" + fileName + "?????????");
                return true;
            } else {
//                System.out.println("??????????????????" + fileName + "?????????");
                return false;
            }
        } else {
//            System.out.println("???????????????????????????" + fileName + "????????????");
            return false;
        }
    }
    //?????????sd???
    public static boolean saveFileData(String obj, String fileName) {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = getParent();
            FileOutputStream fos = null;
            try {
                File sdFile = new File(file, fileName + ".txt");
                fos = new FileOutputStream(sdFile);
                fos.write(obj.getBytes());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) fos.close();
//                    System.out.println("savaData success");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    @NonNull
    private static File getParent() {

        File sdCardDir = Environment.getExternalStorageDirectory();//??????SDCard??????

        String absolutePath = sdCardDir.getAbsolutePath();
        File file = new File(absolutePath + "/color");
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            System.out.println("mkdirs=" + absolutePath + ">" + mkdirs);
        }
        return file;
    }


    /**
     * ??????????????????
     *
     * @param fileName ??????????????????????????????
     * @return ??????????????????????????????true???????????????false
     */
    public static boolean deleteFile(String fileName) {
        File parent = getParent();
        File file = new File(parent.getAbsolutePath() + "/" + fileName + ".txt");
        // ????????????????????????????????????????????????????????????????????????????????????
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
//                System.out.println("??????????????????" + fileName + "?????????");
                return true;
            } else {
//                System.out.println("??????????????????" + fileName + "?????????");
                return false;
            }
        } else {
//            System.out.println("???????????????????????????" + fileName + "????????????");
            return false;
        }
    }


    public static String getFileData(String path) {
        System.out.println("getFileData");
        StringBuffer buffer = new StringBuffer();
        try {
            File file = getParent();
            FileInputStream fis = new FileInputStream(file.getAbsolutePath() + "/" + path + ".txt");
            InputStreamReader isr = new InputStreamReader(fis);//????????????Unicode,UTF-8,ASCII,GB2312,Big5
            Reader in = new BufferedReader(isr);
            int ch;
            while ((ch = in.read()) > -1) {
                buffer.append((char) ch);
            }
            in.close();
        } catch (IOException e) {
            Log.e("yichang", path + "???????????????!");

        }
        return buffer.toString();  //buffer.toString())???????????????????????????
    }






    public static String getFormatSize(double size) {

        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0.00" + "M";//???????????????,???????????????0.3Byte???????????????,??????????????????0.00M
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    @NonNull
    private static String getCacheKey(Map<String, Object> data) {
        String game = data.get("game") + "";
        String type_id = data.get("type_id") + "";
        return "cache_" + game + type_id;
    }

    private static long getFileLastModified(String path) {
        File file = getParent();
        File fis = new File(file.getAbsolutePath() + "/" + path + ".txt");
        if (fis == null) {
            return System.currentTimeMillis();
        }
        long timeMillis = fis.lastModified();

        return timeMillis;
    }



    public static void requestPermissions(Activity activity) {
        if (activity != null) {
            ActivityCompat.requestPermissions(activity, new String[]{android
                    .Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    /*
  ????????????????????????map(key: url  value: ????????? )
 */
    public static void setUrlTime(Context context, String url) {
        String key = "cache_" + url;
        long currentTimeMillis = System.currentTimeMillis();
        urlTime.put(key, currentTimeMillis);
        SharePreferencesUtil.putLong(context, key, currentTimeMillis);
    }

    /**
     * ??????????????????????????? dp ????????? ????????? px(??????)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * ??????????????????????????? px(??????) ????????? ????????? dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //?????????????????????
    public static int LengthNum(int num) {
        int count = 0; //??????
        while (num >= 1) {
            num = num / 10;
            count++;
        }
        return count;
    }

    //??????????????????  ??????   x??????
    public static Animation getAnimation(int x) {
        Animation rotateAnimation = new RotateAnimation(0f, x, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(0);
        rotateAnimation.setRepeatCount(0);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDetachWallpaper(true);

        return rotateAnimation;
    }

    //???????????????JAVA???????????????
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    public static View getContentView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
    }
    public static String getImageUrl(String url, String domainImg){
        if(StringMyUtil.isNotEmpty(url)&&!url.startsWith("http")){
            url=domainImg+url;
        }
        return url;
    }


    public static String ImageUrlCheck(String img_url){
        if (StringMyUtil.isNotEmpty(img_url)&&!img_url.startsWith("http")&&!img_url.startsWith("https")){
            img_url = mSharedPreferenceHelperImpl.getImageDomin()+img_url;
        }
        return img_url;
    }
    public static String CPImageUrlCheck(Context context, String img_url){
        if (StringMyUtil.isNotEmpty(img_url)&&!img_url.startsWith("http")&&!img_url.startsWith("https")){
            String firstImageUrl = SharePreferencesUtil.getString(context, "FirstImageUrl", "");
            img_url = firstImageUrl+img_url;
        }
        return img_url;
    }


    public static void hideSoftKeyBoard(Activity activity) {
        if(activity!=null&&!activity.isFinishing()){
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            if (imm.isActive() && activity.getCurrentFocus() != null) {
                if (activity.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }

    }

    /**
     * ??????????????? (??????activity???????????????view???,??????????????????view???freagment??? ??????activity.getCurrentFocus()??????, ?????????fragment??????????????????????????????editText??????)
     * @param activity
     * @param editText
     */
    public static void hideSoftKeyBoard(Activity activity , EditText editText) {
        if(activity!=null&&!activity.isFinishing()){
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                if (editText.getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }
    public static void showSoftInputFromWindow(Activity activity, EditText editText){
        if(null!=activity&&!activity.isFinishing()){
/*            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            activity. getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);*/
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager)activity. getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

        }
    }
/*    public static int[] initScreenInfo(Activity activity) {
        int[] ints = new int[2];
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        Class c;
        try {
            c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            int HEIGHT = dm.heightPixels;
            int WIDTH = dm.widthPixels;
            ints[0]=WIDTH;
            ints[1]=HEIGHT;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ints;
    }*/

    public static int intgetWinndowWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        Class c;
        int width=0;
        try {
            c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            int WIDTH = dm.widthPixels;
            width=WIDTH;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return width;
    }
    public static int intgetWinndowHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        Class c;
        int height=0;
        try {
            c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            int HEIGHT = dm.heightPixels;
            height=HEIGHT;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return height;
    }



    public static int dp2px(float dpValue) {
        return (int)(0.5F + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * ??????bitmap?????????, ???????????????
     * @param bitmap
     * @param newWidth ??????????????????(?????????dp)
     * @param newHeight
     * @return
     */
    public static Bitmap getNewBitmap(Bitmap bitmap, int newWidth , int newHeight){
        // ?????????????????????.
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // ??????????????????. (????????????px)
        float scaleWidth = ((float) dp2px(newWidth)) / width;
        float scaleHeight = ((float) dp2px(newHeight)) / height;
        // ?????????????????????matrix??????.
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // ??????????????????.
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newBitmap;
    }


    public static boolean checkAccount(String input) {
        if ((input + "").length() > 11) {
            return false;
        }
        Pattern pattern = Pattern.compile("[a-zA-Z]|([0-9]{1,}|\\_){6,11}");
//        Pattern pattern = Pattern.compile("^([a-zA-Z])+[0-9A-Za-z]{6,11}$");
        Matcher m = pattern.matcher(input);
        if (!m.find()) { //????????????,??????????????????????????????
            return false;
        }
        return true;

    }
    public static boolean checkPsw(String input) {
        if ((input + "").length() > 20) {
            return false;
        }
        Pattern pattern = Pattern.compile("^([a-zA-Z])|[0-9]{1,}//_{8,20}");
        Matcher m = pattern.matcher(input);
        if (!m.find()) { //????????????,??????????????????????????????
            return false;
        }
        return true;

    }





    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) <= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
    public static boolean isNotFastClick() {
        return !isFastClick();
    }



    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.requestFocus();
                    imm.showSoftInput(view, 0);
                }
            },200);

        }
    }

    public static String getNetFileSizeDescription(long size) {
        StringBuffer bytes = new StringBuffer();
        DecimalFormat format = new DecimalFormat("###.0");
        if (size >= 1024 * 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0 * 1024.0));
            bytes.append(format.format(i)).append("GB/s");
        }
        else if (size >= 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0));
            bytes.append(format.format(i)).append("MB/s");
        }
        else if (size >= 1024) {
            double i = (size / (1024.0));
            bytes.append(format.format(i)).append("KB/s");
        }
        else if (size < 1024) {
            if (size <= 0) {
                bytes.append("0B/s");
            }
            else {
                bytes.append((int) size).append("B/s");
            }
        }
        return bytes.toString();
    }

    public static boolean isJsonObject(String content) {
        if(TextUtils.isEmpty(content)){
            return false;
        }
        boolean isJsonObject = true;
        try {
            JSONObject.parseObject(content);
        } catch (Exception e) {
            isJsonObject = false;
        }

        if(!isJsonObject){ //??????json??????
            return false;
        }
        return true;
    }
    public static boolean isInt(String content) {
        if(StringMyUtil.isEmptyString(content)){
            return false;
        }
        boolean isInt = true;
        try {
         Integer.parseInt(content);
        } catch (Exception e) {
            isInt = false;
        }
        if(!isInt){
            return false;
        }
        return true;
    }
    public static boolean isNotInt(String content) {
        return !isInt(content);
    }
    public static boolean isLong(String content) {
        if(StringMyUtil.isEmptyString(content)){
            return false;
        }
        boolean isLong = true;
        try {
            Long.parseLong(content);
        } catch (Exception e) {
            isLong = false;
        }
        if(!isLong){
            return false;
        }
        return true;
    }
    public static boolean isNotLong(String content) {
            return !isLong(content);
    }


    public static boolean isDouble(String content) {
        if(StringMyUtil.isEmptyString(content)){
            return false;
        }
        boolean isDouble = true;
        try {
            Double.parseDouble(content);
        } catch (Exception e) {
            isDouble = false;
        }
        if(!isDouble){
            return false;
        }
        return true;
    }
    //??????????????????
    public  static Uri goCamera(Fragment fragment,File cameraSavePath ) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(fragment.getContext(), fragment.getActivity().getApplication().getPackageName()+".fileprovider", cameraSavePath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(cameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //????????????startActivityForResult ????????????getActivity().startActivityForResult, activity onActivityResult????????????super.onActivityResult(requestCode, resultCode, data)???
        fragment.startActivityForResult(intent, CommonStr.REQUEST_CAMERA_CODE);
//        getActivity().startActivityForResult(intent, REQUEST_CAMERA_CODE);
        return  uri;
    }

    /*
????????????
 */
    public static void goPhotoAlbum(Fragment fragment) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        fragment. startActivityForResult(intent, CommonStr.REQUEST_PHOTO_CODE);
    }
    //??????????????????
    public static Uri goCamera(Context context,File cameraSavePath ) {
        Activity activity = (Activity) context;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, activity.getApplication().getPackageName()+".fileprovider", cameraSavePath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(cameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //????????????startActivityForResult ????????????getActivity().startActivityForResult, activity onActivityResult????????????super.onActivityResult(requestCode, resultCode, data)???
        activity.startActivityForResult(intent, CommonStr.REQUEST_CAMERA_CODE);
//        getActivity().startActivityForResult(intent, REQUEST_CAMERA_CODE);
        return uri;
    }

    /*
????????????
 */
    public static void goPhotoAlbum(Context context) {
        Activity activity = (Activity) context;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity. startActivityForResult(intent, CommonStr.REQUEST_PHOTO_CODE);
    }
    public static boolean isNotDouble(String content) {
       return  !isDouble(content);
    }


    public static void  clearCache(){
        SharedPreferenceHelperImpl sp = new SharedPreferenceHelperImpl();
        sp.setUserId("");
        sp.setToken("");
    }









    /**
     * ???????????????????????????app
     */
    public static boolean isAPPInstalled(Context context,String packageName){
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        for (int i = 0; i < installedPackages.size(); i++) {
            if(installedPackages.get(i).packageName.equals(packageName)){
                return true;
            }
        }
        return false;
    }
    public static void telegramShare(Context context,String content){
        if(isAPPInstalled(context,"org.telegram.messenger.web")){
            content = content.replace("@","");
            context. startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/"+content)));
        }else {
            ToastUtils.show("?????????telegram");
        }
    }

    public static void  whatsAppShare(Context context,String content){
        if(isAPPInstalled(context,"com.whatsapp")){
            context. startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone="+content)));
        }else {
            ToastUtils.show("?????????whatsaApp");
        }

    }


    public static void  qqShare(Context context,String content){
        if(isAPPInstalled(context,"com.tencent.mobileqq")){
            context. startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin"+content)));
        }else {
            ToastUtils.show("?????????QQ");
        }

    }
    public static void  weChatShare(Context context,String content){
        if(isAPPInstalled(context,"com.tencent.mm")){
            Intent intent = new Intent();
            ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);

            context.startActivity(intent);
        }else {
            ToastUtils.show("???????????????");
        }

    }
    public static void  skypeShare(Context context,String content){
        if(isAPPInstalled(context,"com.skype.rover")){
            context. startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("skype:"+content+"?chat")));
        }else {
            ToastUtils.show("?????????skype");
        }

    }
    private static String buildSkypeDeepLinkQuery(String account) {
        StringBuilder builder = new StringBuilder();
        builder.append("action=");
        builder.append("chat");
        builder.append("&video=off");
        builder.append("&");
        builder.append("recipient=");
        builder.append(account);
/*        builder.append("&");
        builder.append("correlationId=");
        builder.append(generateCorrelationId());
        builder.append("&");
        builder.append("source=");
        builder.append(generateSourceName());*/
        return builder.toString();
    }



    private static String generateSourceName() {
        StringBuilder builder = new StringBuilder();

        try {
            builder.append(URLEncoder.encode(MyApplication.getInstance().getResources().getString(R.string.app_name), "UTF-8"));
            builder.append("-");
            builder.append(URLEncoder.encode(MyApplication.getInstance().getPackageName(), "UTF-8"));
        } catch (UnsupportedEncodingException var3) {
            builder.append("Application");
            builder.append("-");
            builder.append("Unknown");
        }

        return builder.toString();
    }

    private static String generateCorrelationId() {
        String uuId = UUID.randomUUID().toString();
        return uuId.replace("-", "");
    }

    public static boolean isNotEmptyArray(String result){
        if(result.equals("[]")){
            return false;
        }
        return true;
    }
    /**
     * ????????????????????????????????????????????????
     *
     * @param createDate
     * @param nowDate
     * @return
     */
    public static String getDatePoor2(long nowDate, long createDate) {

        long nd = 1000 * 24 * 60 * 60l;
        long nh = 1000 * 60 * 60l;
        long nm = 1000 * 60l;
        // long ns = 1000;
        // ???????????????????????????????????????
        long diff = nowDate- createDate;
        // ??????????????????
        long day = diff / nd;
        // ?????????????????????
        long hour = diff % nd / nh;
        // ?????????????????????
        long min = diff % nd % nh / nm;
        // ??????????????????
        // long sec = diff % nd % nh % nm / ns;
        if (day > 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.format(createDate);
        }
        if (hour > 0) {
            return   hour + "?????????";
        }
        if (min > 0 && min<=1) {
            return "??????";
        }else {
            return  min + "?????????";
        }
    }
    /**
     * ???????????????????????????????????????
     */
    public static String ArrayToStrWithComma(List<String>list){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if((i+1)!=list.size()){
                sb.append(",");
            }
        }
        return sb.toString();
    }
    public static PictureParameterStyle getDefaultStyle(Context context) {
        // ????????????
        PictureParameterStyle mPictureParameterStyle = new PictureParameterStyle();
        // ?????????????????????????????????(????????????)
        mPictureParameterStyle.isChangeStatusBarFontColor = false;
        // ??????????????????????????????(0/9)??????
        mPictureParameterStyle.isOpenCompletedNumStyle = false;
        // ??????????????????QQ???????????????????????????
        mPictureParameterStyle.isOpenCheckNumStyle = false;
        // ????????????????????????
        mPictureParameterStyle.pictureStatusBarColor = Color.parseColor("#393a3e");
        // ??????????????????????????????
        mPictureParameterStyle.pictureTitleBarBackgroundColor = Color.parseColor("#393a3e");
        // ????????????????????????
        mPictureParameterStyle.pictureContainerBackgroundColor = ContextCompat.getColor(context, R.color.app_color_black);
        // ???????????????????????????????????????
        mPictureParameterStyle.pictureTitleUpResId = R.drawable.picture_icon_arrow_up;
        // ???????????????????????????????????????
        mPictureParameterStyle.pictureTitleDownResId = R.drawable.picture_icon_arrow_down;
        // ?????????????????????????????????
        mPictureParameterStyle.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval;
        // ??????????????????
        mPictureParameterStyle.pictureLeftBackIcon = R.drawable.picture_icon_back;
        // ?????????????????????
        mPictureParameterStyle.pictureTitleTextColor = ContextCompat.getColor(context, R.color.picture_color_white);
        // ????????????????????????????????????  ?????? ??????.pictureRightDefaultTextColor???.pictureRightDefaultTextColor
        mPictureParameterStyle.pictureCancelTextColor = ContextCompat.getColor(context, R.color.picture_color_white);
        // ??????????????????????????????
        mPictureParameterStyle.pictureAlbumStyle = R.drawable.picture_new_item_select_bg;
        // ??????????????????????????????
        mPictureParameterStyle.pictureCheckedStyle = R.drawable.picture_checkbox_selector;
        // ???????????????????????????
        mPictureParameterStyle.pictureBottomBgColor = ContextCompat.getColor(context, R.color.picture_color_grey);
        // ??????????????????????????????
        mPictureParameterStyle.pictureCheckNumBgStyle = R.drawable.picture_num_oval;
        // ????????????????????????????????????(?????????????????????????????????)
        mPictureParameterStyle.picturePreviewTextColor = ContextCompat.getColor(context, R.color.picture_color_fa632d);
        // ??????????????????????????????????????????(????????????????????????????????????)
        mPictureParameterStyle.pictureUnPreviewTextColor = ContextCompat.getColor(context, R.color.picture_color_white);
        // ???????????????????????????(????????? ???????????????)
        mPictureParameterStyle.pictureCompleteTextColor = ContextCompat.getColor(context, R.color.picture_color_fa632d);
        // ???????????????????????????(????????? ??????????????????)
        mPictureParameterStyle.pictureUnCompleteTextColor = ContextCompat.getColor(context, R.color.picture_color_white);
        // ???????????????????????????
        mPictureParameterStyle.picturePreviewBottomBgColor = ContextCompat.getColor(context, R.color.picture_color_grey);
        // ????????????????????????????????????
        mPictureParameterStyle.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_delete;
        // ????????????????????????  ?????????.isOriginalImageControl(true); ?????????
        mPictureParameterStyle.pictureOriginalControlStyle = R.drawable.picture_original_wechat_checkbox;
        // ?????????????????? ?????????.isOriginalImageControl(true); ?????????
        mPictureParameterStyle.pictureOriginalFontColor = ContextCompat.getColor(context, R.color.app_color_white);
        // ??????????????????????????????????????????
        mPictureParameterStyle.pictureExternalPreviewGonePreviewDelete = true;
        // ??????NavBar Color SDK Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP??????
        mPictureParameterStyle.pictureNavBarColor = Color.parseColor("#393a3e");
        // ?????????????????????
        mPictureParameterStyle.folderTextColor = Color.parseColor("#4d4d4d");
        // ?????????????????????
        mPictureParameterStyle.folderTextSize = 16;


        return mPictureParameterStyle;
    }


    /**
     * EditText??????????????????????????????
     * @param editText ???????????????EditText
     * @return true???????????????  false??????????????????
     */
    public static boolean canVerticalScroll(EditText editText) {
        //???????????????
        int scrollY = editText.getScrollY();
        //????????????????????????
        int scrollRange = editText.getLayout().getHeight();
        //???????????????????????????
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
        //???????????????????????????????????????????????????
        int scrollDifference = scrollRange - scrollExtent;

        if(scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }
}
