package com.example.new_application.net.api;


import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface IHttpApi {
    /**
     *用户注册
     */
    @FormUrlEncoded
    @POST("api/member/regUser")
    Observable<Response<ResponseBody>> register(@FieldMap HashMap<String,Object>data);

    /**
     * 用户登录
     * @param data
     * @return
     */
    @FormUrlEncoded
    @POST("auth/oauth/token")
    Observable<Response<ResponseBody>> login(@FieldMap HashMap<String,Object>data);

    /**
     * 线路测试
     * @param data
     * @return
     */
    @GET("auth/oauth/token/test")
    Observable<Response<ResponseBody>> pingTest(@QueryMap Map<String, Object> data);

    /**
     * 用户信息
     */
    @FormUrlEncoded
    @POST("api/member/aboutPerson")
    Observable<Response<ResponseBody>> userInfo(@FieldMap HashMap<String,Object>data);



    /**
     * 收藏/取消收藏帖子
     */
    @FormUrlEncoded
    @POST("api/resource/message/collectResourceMessage")
    Observable<Response<ResponseBody>> followResource(@FieldMap HashMap<String,Object>data);
    /**
     * 系统参数
     */
    @FormUrlEncoded
    @POST("api/base/sysParameter")
    Observable<Response<ResponseBody>> systemParams(@FieldMap HashMap<String,Object>data);

    /**
     *  版本更细
     */
    @FormUrlEncoded
    @POST("api/base/appVersion")
    Observable<Response<ResponseBody>> appUpload(@FieldMap HashMap<String,Object>data);

    /**
     *
     * 私信列表
     */
    @FormUrlEncoded
    @POST("api/base/letterList")
    Observable<Response<ResponseBody>> messageList(@FieldMap HashMap<String,Object>data);

    /**
     * 文件长传
     */
    @Multipart
    @POST("api/file/upload")
    Observable<Response<ResponseBody>> uploadFile(@Part("file")RequestBody key, @Part MultipartBody.Part file);

    /**
     * 意见反馈类型列表
     */
    @FormUrlEncoded
    @POST("api/memberFeedback/opinionTypeList")
    Observable<Response<ResponseBody>> meetProblem(@FieldMap HashMap<String,Object>data);


    /**
     * 提交意见反馈
     */
    @FormUrlEncoded
    @POST("api/memberFeedback/submitFeedback")
    Observable<Response<ResponseBody>> commitFeedback(@FieldMap HashMap<String,Object>data);

    /**
     * 我的反馈列表
     */
    @FormUrlEncoded
    @POST("api/memberFeedback/page")
    Observable<Response<ResponseBody>> mineFeedback(@FieldMap HashMap<String,Object>data);

    /**
     * 所有分类集合
     */
    @GET("api/resource/classification/allLevelList")
    Observable<Response<ResponseBody>> appUpdate(@QueryMap Map<String,Object> data);

    /**
     * 修改用户信息
     */
    @FormUrlEncoded
    @POST("api/member/updateMemberInfo")
    Observable<Response<ResponseBody>> updateUserInfo(@FieldMap HashMap<String,Object>data);

    /**
     * 用户选择的服务类型
     */
    @FormUrlEncoded
    @POST("api/member/memberClassification")
    Observable<Response<ResponseBody>> mineServerType(@FieldMap HashMap<String,Object>data);


    /**
     * 发布资源
     */
    @FormUrlEncoded
    @POST("api/resource/message/add")
    Observable<Response<ResponseBody>> releaseResource(@FieldMap HashMap<String,Object>data);

    /**
     * 首页列表
     */
    @FormUrlEncoded
    @POST("api/resource/message/homeList")
    Observable<Response<ResponseBody>> homeList(@FieldMap HashMap<String,Object>data);

    /**
     * 获取首页标签列表
     */
    @FormUrlEncoded
    @POST("api/official/mark/getAllList")
    Observable<Response<ResponseBody>> labelList(@FieldMap HashMap<String,Object>data);

    /**
     * 首页二级分类集合
     */
    @GET("api/resource/classification/homeTwoLevelList/{parentId}")
    Observable<Response<ResponseBody>>twoLevelList(@Path("parentId")String parentId);

    /**
     * 资源详情
     *
     */
    @FormUrlEncoded
    @POST("api/resource/message/resourceMessageDetails")
    Observable<Response<ResponseBody>> resourceDetails(@FieldMap HashMap<String,Object>data);

    /**
     * 店铺详情
     */
    @FormUrlEncoded
    @POST("api/member/aboutOtherPerson")
    Observable<Response<ResponseBody>> storeDetails(@FieldMap HashMap<String,Object>data);

    /**
     * 发布列表
     */
    @FormUrlEncoded
    @POST("api/resource/message/memberResourceMessage")
    Observable<Response<ResponseBody>> releaseList(@FieldMap HashMap<String,Object>data);

    /**
     * 发起担保
     *
     */
    @FormUrlEncoded
    @POST("api/member/order/initiateGuarantee")
    Observable<Response<ResponseBody>> initiateGuarantee(@FieldMap HashMap<String,Object>data);

    /**
     * 收藏店铺
     */
    @FormUrlEncoded
    @POST("api/member/followShop")
    Observable<Response<ResponseBody>> followStore(@FieldMap HashMap<String,Object>data);

    /**
     * 公告列表
     */

    @POST("api/base/noticeInfoList/{type}")
    Observable<Response<ResponseBody>>systemNotice(@Path("type")String type);

    /**
     * 需求/服务大厅
     */
    @FormUrlEncoded
    @POST("api/resource/message/page")
    Observable<Response<ResponseBody>> serverHall(@FieldMap HashMap<String,Object>data);

    /**
     * 担保订单列表
     */
    @FormUrlEncoded
    @POST("api/member/order/page")
    Observable<Response<ResponseBody>> guaranteeOrderList(@FieldMap HashMap<String,Object>data);

    /**
     * 同意担保
     */
    @FormUrlEncoded
    @POST("api/member/order/confirmOrder")
    Observable<Response<ResponseBody>> agreeGuarantee(@FieldMap HashMap<String,Object>data);


    /**
     * 同意担保
     */
    @FormUrlEncoded
    @POST("api/member/order/cancelOrder")
    Observable<Response<ResponseBody>> cancelGuarantee(@FieldMap HashMap<String,Object>data);

    /**
     * 修改担保
     */
    @FormUrlEncoded
    @POST("api/member/order/updateGuarantee")
    Observable<Response<ResponseBody>> modifyGuarantee(@FieldMap HashMap<String,Object>data);


    /**
     * 修改发布的资源
     */
    @FormUrlEncoded
    @POST("api/resource/message/update")
    Observable<Response<ResponseBody>> modifyReleaseResource(@FieldMap HashMap<String,Object>data);

    /**
     * 删除发布的资源
     */
    @FormUrlEncoded
    @POST("api/resource/message/delResourceMessageById")
    Observable<Response<ResponseBody>> deleteRelease(@FieldMap HashMap<String,Object>data);

    /**
     * 上次登录时间
     */
    @FormUrlEncoded
    @POST("api/member/lastLoginTime")
    Observable<Response<ResponseBody>> lastLoginDate(@FieldMap HashMap<String,Object>data);

    /**
     * 发送验证码
     */
    @FormUrlEncoded
    @POST("api/sys/codeRecord/sendSms")
    Observable<Response<ResponseBody>> sendCode(@FieldMap HashMap<String,Object>data);

    /**
     * 忘记密码
     */
    @FormUrlEncoded
    @POST("api/sys/codeRecord/forgetPwd")
    Observable<Response<ResponseBody>> forgetPassword(@FieldMap HashMap<String,Object>data);

    /**
     * 修改登录密码
     */
    @FormUrlEncoded
    @POST("api/member/updatePassword")
    Observable<Response<ResponseBody>> modifyPassword(@FieldMap HashMap<String,Object>data);


    /**
     * 绑定/修改手机
     */
    @FormUrlEncoded
    @POST("api/sys/codeRecord/bindPhone")
    Observable<Response<ResponseBody>> bindPhone(@FieldMap HashMap<String,Object>data);


    /**
     * 收藏/浏览的帖子
     */
    @FormUrlEncoded
    @POST("api/resource/message/myCollectResourceMessage")
    Observable<Response<ResponseBody>> mineFollowResource(@FieldMap HashMap<String,Object>data);


    /**
     * 删除浏览或收藏的帖子
     */
    @FormUrlEncoded
    @POST("api/resource/message/batchDelMemberCollectMessage")
    Observable<Response<ResponseBody>> deleteFollowResource(@FieldMap HashMap<String,Object>data);


    /**
     * 关注或浏览的商家
     */
    @FormUrlEncoded
    @POST("api/member/myFollowShopList")
    Observable<Response<ResponseBody>> storeHistory(@FieldMap HashMap<String,Object>data);


    /**
     *删除浏览或收藏的店铺
     */
    @FormUrlEncoded
    @POST("api/member/batchCancelFollow")
    Observable<Response<ResponseBody>> deleteFollowStore(@FieldMap HashMap<String,Object>data);



    /**
     * 我要合作
     */
    @FormUrlEncoded
    @POST("api/business/cooperation/page")
    Observable<Response<ResponseBody>> cooperation(@FieldMap HashMap<String,Object>data);

    /**
     * 提示文字
     *
     */
    @POST("api/base/promptWord/{themetype}")
    Observable<Response<ResponseBody>>tipContent(@Path("themetype")String themetype);

    /**
     * 诚信商家
     */
    @FormUrlEncoded
    @POST("api/member/verifiedShopList")
    Observable<Response<ResponseBody>> honestyMerchant(@FieldMap HashMap<String,Object>data);

    /**
     * 首页跑马灯
     */
    @FormUrlEncoded
    @POST("api/base/indexLetterList")
    Observable<Response<ResponseBody>> homePaoMa(@FieldMap HashMap<String,Object>data);

    /**
     * 线路列表
     */
    @FormUrlEncoded
    @POST("api/base/appRequestDomains")
    Observable<Response<ResponseBody>> urlList(@FieldMap HashMap<String,Object>data);

    /**
     * 申请联合运营
     */
    @FormUrlEncoded
    @POST("api/member/submitVerifyInfo")
    Observable<Response<ResponseBody>> verify(@FieldMap HashMap<String,Object>data);

    /**
     * 未读消息数
     *
     */
    @FormUrlEncoded
    @POST("api/base/letterUnreadNum")
    Observable<Response<ResponseBody>> unReadMessage(@FieldMap HashMap<String,Object>data);

    /**
     * 私信详情
     */
    @POST("api/base/letterDetail/{letter_id}")
    Observable<Response<ResponseBody>> messageDetails(@Path("letter_id")String letter_id);

    /**
     * 担保详情
     */

    @FormUrlEncoded
    @POST("api/member/order/memberOrderDetails")
    Observable<Response<ResponseBody>> guaranteeDetails(@FieldMap HashMap<String,Object>data);

    /**
     * 官方群组
     */

    @FormUrlEncoded
    @POST("api/official/group/page")
    Observable<Response<ResponseBody>> officialFroup(@FieldMap HashMap<String,Object>data);
}