package com.cambodia.zhanbang.rxhttp.sp;

import com.cambodia.zhanbang.rxhttp.net.model.UserEntity;

/**
 * created  by ganzhe on 2019/9/26.
 */
public interface SharedPreferenceHelper {


    /**
     * 获取登录状态
     * @return
     */
    boolean getLoginStatus();
    void setLoginStatus(boolean isLogin);

    /**
     * 设置 获取token
     * @param token
     */
    void setToken(String token);

    String getToken();

    /**
     * 切换后的baseUrl
     * @param newUrl
     */
    void setNewBaseUrl(String newUrl);
    String getNewBaseUrl();

    /**
     * 图片域名
     */
    void setImageDomin(String imageDomin);
    String getImageDomin();

}
