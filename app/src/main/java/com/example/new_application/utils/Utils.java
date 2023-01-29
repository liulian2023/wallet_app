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
    // 两次点击按钮之间的点击间隔不能少于800毫秒
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
     * 设置背景亮度
     * @param activity activity实例
     * @param bgcolor 亮度值(0f-1f)值越小,背景越暗
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
        ClipboardManager clipboardManager= (ClipboardManager) MyApplication.getInstance().getSystemService(CLIPBOARD_SERVICE);//实例化clipboardManager对象
        ClipData bankCardNumData=  ClipData.newPlainText(label, content);//复制文本数据到粘贴板  newPlainText
        clipboardManager.setPrimaryClip(bankCardNumData);
        ToastUtils.show( "复制成功");
    }
    public static HashMap<String, Long> urlTime = new HashMap<>();

    public static boolean fileIsExists(String strFile) {
        try {
            File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录
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
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
//                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
//                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
//            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }
    //保存在sd卡
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

        File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录

        String absolutePath = sdCardDir.getAbsolutePath();
        File file = new File(absolutePath + "/color");
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            System.out.println("mkdirs=" + absolutePath + ">" + mkdirs);
        }
        return file;
    }


    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File parent = getParent();
        File file = new File(parent.getAbsolutePath() + "/" + fileName + ".txt");
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
//                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
//                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
//            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }


    public static String getFileData(String path) {
        System.out.println("getFileData");
        StringBuffer buffer = new StringBuffer();
        try {
            File file = getParent();
            FileInputStream fis = new FileInputStream(file.getAbsolutePath() + "/" + path + ".txt");
            InputStreamReader isr = new InputStreamReader(fis);//文件编码Unicode,UTF-8,ASCII,GB2312,Big5
            Reader in = new BufferedReader(isr);
            int ch;
            while ((ch = in.read()) > -1) {
                buffer.append((char) ch);
            }
            in.close();
        } catch (IOException e) {
            Log.e("yichang", path + "文件不存在!");

        }
        return buffer.toString();  //buffer.toString())就是读出的内容字符
    }






    public static String getFormatSize(double size) {

        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0.00" + "M";//清除缓存时,清理完会有0.3Byte左右的缓存,这里直接显示0.00M
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
  保存文件时间戳的map(key: url  value: 时间戳 )
 */
    public static void setUrlTime(Context context, String url) {
        String key = "cache_" + url;
        long currentTimeMillis = System.currentTimeMillis();
        urlTime.put(key, currentTimeMillis);
        SharePreferencesUtil.putLong(context, key, currentTimeMillis);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //判断整数的位数
    public static int LengthNum(int num) {
        int count = 0; //计数
        while (num >= 1) {
            num = num / 10;
            count++;
        }
        return count;
    }

    //获取动画实例  旋转   x角度
    public static Animation getAnimation(int x) {
        Animation rotateAnimation = new RotateAnimation(0f, x, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(0);
        rotateAnimation.setRepeatCount(0);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDetachWallpaper(true);

        return rotateAnimation;
    }

    //方法一：用JAVA自带的函数
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
     * 收回软键盘 (获取activity获得焦点的view时,可能出现焦点view在freagment中 导致activity.getCurrentFocus()为空, 所以在fragment中直接传入需要收回的editText控件)
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
     * 改变bitmap的尺寸, 图片不失真
     * @param bitmap
     * @param newWidth 改变后的宽度(单位为dp)
     * @param newHeight
     * @return
     */
    public static Bitmap getNewBitmap(Bitmap bitmap, int newWidth , int newHeight){
        // 获得图片的宽高.
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算缩放比例. (要先转为px)
        float scaleWidth = ((float) dp2px(newWidth)) / width;
        float scaleHeight = ((float) dp2px(newHeight)) / height;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片.
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
        if (!m.find()) { //匹配不到,說明輸入的不符合條件
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
        if (!m.find()) { //匹配不到,說明輸入的不符合條件
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

        if(!isJsonObject){ //不是json格式
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
    //激活相机操作
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
        //直接使用startActivityForResult 如果使用getActivity().startActivityForResult, activity onActivityResult中要加上super.onActivityResult(requestCode, resultCode, data)。
        fragment.startActivityForResult(intent, CommonStr.REQUEST_CAMERA_CODE);
//        getActivity().startActivityForResult(intent, REQUEST_CAMERA_CODE);
        return  uri;
    }

    /*
调用相册
 */
    public static void goPhotoAlbum(Fragment fragment) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        fragment. startActivityForResult(intent, CommonStr.REQUEST_PHOTO_CODE);
    }
    //激活相机操作
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
        //直接使用startActivityForResult 如果使用getActivity().startActivityForResult, activity onActivityResult中要加上super.onActivityResult(requestCode, resultCode, data)。
        activity.startActivityForResult(intent, CommonStr.REQUEST_CAMERA_CODE);
//        getActivity().startActivityForResult(intent, REQUEST_CAMERA_CODE);
        return uri;
    }

    /*
调用相册
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
     * 检查手机是否安装了app
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
            ToastUtils.show("未安装telegram");
        }
    }

    public static void  whatsAppShare(Context context,String content){
        if(isAPPInstalled(context,"com.whatsapp")){
            context. startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone="+content)));
        }else {
            ToastUtils.show("未安装whatsaApp");
        }

    }


    public static void  qqShare(Context context,String content){
        if(isAPPInstalled(context,"com.tencent.mobileqq")){
            context. startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin"+content)));
        }else {
            ToastUtils.show("未安装QQ");
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
            ToastUtils.show("未安装微信");
        }

    }
    public static void  skypeShare(Context context,String content){
        if(isAPPInstalled(context,"com.skype.rover")){
            context. startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("skype:"+content+"?chat")));
        }else {
            ToastUtils.show("未安装skype");
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
     * 计算俩个时间差多少天多少小时分钟
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
        // 获得两个时间的毫秒时间差异
        long diff = nowDate- createDate;
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒
        // long sec = diff % nd % nh % nm / ns;
        if (day > 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.format(createDate);
        }
        if (hour > 0) {
            return   hour + "小时前";
        }
        if (min > 0 && min<=1) {
            return "刚刚";
        }else {
            return  min + "分钟前";
        }
    }
    /**
     * 数组转成用逗号拼接的字符串
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
        // 相册主题
        PictureParameterStyle mPictureParameterStyle = new PictureParameterStyle();
        // 是否改变状态栏字体颜色(黑白切换)
        mPictureParameterStyle.isChangeStatusBarFontColor = false;
        // 是否开启右下角已完成(0/9)风格
        mPictureParameterStyle.isOpenCompletedNumStyle = false;
        // 是否开启类似QQ相册带数字选择风格
        mPictureParameterStyle.isOpenCheckNumStyle = false;
        // 相册状态栏背景色
        mPictureParameterStyle.pictureStatusBarColor = Color.parseColor("#393a3e");
        // 相册列表标题栏背景色
        mPictureParameterStyle.pictureTitleBarBackgroundColor = Color.parseColor("#393a3e");
        // 相册父容器背景色
        mPictureParameterStyle.pictureContainerBackgroundColor = ContextCompat.getColor(context, R.color.app_color_black);
        // 相册列表标题栏右侧上拉箭头
        mPictureParameterStyle.pictureTitleUpResId = R.drawable.picture_icon_arrow_up;
        // 相册列表标题栏右侧下拉箭头
        mPictureParameterStyle.pictureTitleDownResId = R.drawable.picture_icon_arrow_down;
        // 相册文件夹列表选中圆点
        mPictureParameterStyle.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval;
        // 相册返回箭头
        mPictureParameterStyle.pictureLeftBackIcon = R.drawable.picture_icon_back;
        // 标题栏字体颜色
        mPictureParameterStyle.pictureTitleTextColor = ContextCompat.getColor(context, R.color.picture_color_white);
        // 相册右侧取消按钮字体颜色  废弃 改用.pictureRightDefaultTextColor和.pictureRightDefaultTextColor
        mPictureParameterStyle.pictureCancelTextColor = ContextCompat.getColor(context, R.color.picture_color_white);
        // 选择相册目录背景样式
        mPictureParameterStyle.pictureAlbumStyle = R.drawable.picture_new_item_select_bg;
        // 相册列表勾选图片样式
        mPictureParameterStyle.pictureCheckedStyle = R.drawable.picture_checkbox_selector;
        // 相册列表底部背景色
        mPictureParameterStyle.pictureBottomBgColor = ContextCompat.getColor(context, R.color.picture_color_grey);
        // 已选数量圆点背景样式
        mPictureParameterStyle.pictureCheckNumBgStyle = R.drawable.picture_num_oval;
        // 相册列表底下预览文字色值(预览按钮可点击时的色值)
        mPictureParameterStyle.picturePreviewTextColor = ContextCompat.getColor(context, R.color.picture_color_fa632d);
        // 相册列表底下不可预览文字色值(预览按钮不可点击时的色值)
        mPictureParameterStyle.pictureUnPreviewTextColor = ContextCompat.getColor(context, R.color.picture_color_white);
        // 相册列表已完成色值(已完成 可点击色值)
        mPictureParameterStyle.pictureCompleteTextColor = ContextCompat.getColor(context, R.color.picture_color_fa632d);
        // 相册列表未完成色值(请选择 不可点击色值)
        mPictureParameterStyle.pictureUnCompleteTextColor = ContextCompat.getColor(context, R.color.picture_color_white);
        // 预览界面底部背景色
        mPictureParameterStyle.picturePreviewBottomBgColor = ContextCompat.getColor(context, R.color.picture_color_grey);
        // 外部预览界面删除按钮样式
        mPictureParameterStyle.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_delete;
        // 原图按钮勾选样式  需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle.pictureOriginalControlStyle = R.drawable.picture_original_wechat_checkbox;
        // 原图文字颜色 需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle.pictureOriginalFontColor = ContextCompat.getColor(context, R.color.app_color_white);
        // 外部预览界面是否显示删除按钮
        mPictureParameterStyle.pictureExternalPreviewGonePreviewDelete = true;
        // 设置NavBar Color SDK Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP有效
        mPictureParameterStyle.pictureNavBarColor = Color.parseColor("#393a3e");
        // 文件夹字体颜色
        mPictureParameterStyle.folderTextColor = Color.parseColor("#4d4d4d");
        // 文件夹字体大小
        mPictureParameterStyle.folderTextSize = 16;


        return mPictureParameterStyle;
    }


    /**
     * EditText竖直方向是否可以滚动
     * @param editText 需要判断的EditText
     * @return true：可以滚动  false：不可以滚动
     */
    public static boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if(scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }
}
