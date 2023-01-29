package com.cambodia.zhanbang.rxhttp.net.interceptor;
import android.text.Spanned;

import com.cambodia.zhanbang.rxhttp.net.utils.CommonModule;
import com.cambodia.zhanbang.rxhttp.net.utils.StringMyUtil;
import com.cambodia.zhanbang.rxhttp.net.utils.SystemUtil;
import com.cambodia.zhanbang.rxhttp.net.utils.VersionUtils;
import com.cambodia.zhanbang.rxhttp.sp.SharedPreferenceHelperImpl;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.annotations.EverythingIsNonNull;
public class HttpHeaderInterceptor implements Interceptor {

    SharedPreferenceHelperImpl mSharedPreferenceHelperImpl = new SharedPreferenceHelperImpl();
    @Override
    @EverythingIsNonNull
    public Response intercept(Chain chain) throws IOException {
        String  token=  mSharedPreferenceHelperImpl.getToken();
        Request.Builder builder = chain.request().newBuilder();
        Request request=null;
        builder.addHeader("deviceNumber", SystemUtil.getUniqueId(CommonModule.getAppContext()));
        builder.addHeader("appVersionName", VersionUtils.getAppVersionName(CommonModule.getAppContext()));
        builder.addHeader("mobileSystemVersion", SystemUtil.getSystemVersion());
        builder.addHeader("mobileBrandModels", SystemUtil.getSystemModel());
        builder.addHeader("loginType","1");

        if(StringMyUtil.isNotEmpty(token)){
            builder.addHeader("id", mSharedPreferenceHelperImpl.getUserId());
            builder.addHeader("Authorization", "Bearer "+token);
        }else {
            builder.addHeader("id", "");
            builder.addHeader("Authorization", "Bearer bW9iaWxlOm1vYmlsZQ==");
        }
        request = builder.build();
        Response response = chain.proceed(request);
        return response;
    }
}
