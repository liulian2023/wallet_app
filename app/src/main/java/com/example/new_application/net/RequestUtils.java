package com.example.new_application.net;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;

public class RequestUtils {
    /**
     * 注册
     * password
     * username
     */
    public static String REGISTER = "api/member/regUser";
    /**
     * 登录
     * username
     * password
     */
    public static String LOGIN = "auth/oauth/token";

    /**
     * 用户信息
     */
    public static String USER_INFO="api/member/aboutPerson";

    /**
     * 系统参数
     */
    public static String SYSTEM_PARAMS = "api/base/sysParameter";

    /**
     *  版本更细
     * systemType 系统类型 1:ios;2:Android;11:ios组更新;22:安卓热更新
     * testFlightCode
     * versionCode
     */
    public static String APP_UPLOAD = "api/base/appVersion";


    /**
     * 用户私信列表
     * current
     * size 默认 10
     * 私信类型(1交易消息；0合作消息)
     */
    public static String MESSAGE_LIST = "api/base/letterList";

    /**
     *图片上传
     */
    public static String UPLOAD_FILE = "api/file/upload";

    /**
     * 意见反馈类型
     */
    public static String MEET_PROBLEM_LIST = "api/memberFeedback/opinionTypeList";


    /**
     *
     * 提交意见反馈
     * appVersion app版本
     * deviceIdentifier 设备标识
     * opinionContent  意见内容
     * opinionType  意见类型
     * pictures 反馈图片(多个用逗号隔开)
     */
    public static String COMMIT_FEED_BACK = "api/memberFeedback/submitFeedback";

    /**
     * 我的意见反馈列表
     * current
     * size
     */
    public static String MINE_FEEDBACK_LIST = "api/memberFeedback/page";

    /**
     * 所有分类合集
     */
    public static String ALL_CLASSIFICATION= "api/resource/classification/allLevelList";

    /**
     * 修改用户信息
     * image	string
     * 头像(updateFlag=0时传)
     *
     * label	string
     * 标签(updateFlag=22时传)
     *
     * nickname	string
     * 昵称(updateFlag=1时传)
     *
     * qq	string
     * QQ(updateFlag=2时传)
     *
     * skype	string
     * skype(updateFlag=2时传)
     *
     * telegram	string
     * telegram(updateFlag=2时传)
     *
     * twoLevelClassification	[...]
     * updateFlag	integer($int32)
     * 0修改头像,1修改昵称,2修改联系方式,22服务标签,3服务类型(二级分类)
     *
     * weixin	string
     * 微信(updateFlag=2时传)
     *
     * whatsapp	string
     * whatsapp(updateFlag=2时传)
     *
     */
    public static String UPDATE_USER_INFO="api/member/updateMemberInfo";

    /**
     *
     * 用户选择的服务类型
     */
    public static String MINE_SERVER_TYPE="api/member/memberClassification";


    /**
     * 发布资源
     *attachment	string
     * 附件(多个逗号分隔)
     *
     * content	string
     * 内容
     *
     * label	string
     * 自设标签
     *
     * title	string
     * 标题
     *
     * twoLevelClassify	integer($int64)
     * 二级分类
     */
    public static String RELEASE_RESOURCE="api/resource/message/add";
    /**
     * 修改发布资源
     * attachment	string
     * 附件(多个逗号分隔)
     *
     * commissionType	integer($int32)
     * 佣金类型(0买家付,1卖家付,2各付一半)
     *
     * content	string
     * 内容
     *
     * guaranteeType	integer($int32)
     * 交易模式(0：自行交易,1：担保交易)
     *
     * id	integer($int64)
     * id
     * label	string
     * 自设标签
     *
     * price	string
     * 价格(范围价格用英文横杠隔开)
     *
     * priceType	integer($int32)
     * 价格类型(1：一口价，2：范围价格，3：议价)
     *
     * title	string
     * 标题
     *
     * twoLevelClassification	integer($int6
     */
    public static String MODIFY_RELEASE_RESOURCE="api/resource/message/update";

    /**
     * 首页列表
     */
    public static String HOME_LIST = "api/resource/message/homeList";

    /**
     * 后去首页标签列表
     */
    public static String HOME_LABEL = "api/official/mark/getAllList";


    /**
     * 首页二级分类集合
     * parentId 上级id  传-1获取所有
     */
    public static String TWO_LEVEL_LIST = "api/resource/classification/homeTwoLevelList/{parentId}";

    /**
     * 资源详情
     *
     */
    public static String RESOURCE_DETAILS = "api/resource/message/resourceMessageDetails";

    /**
     * 店铺详情
     * memberId
     */
    public static String STORE_DETAILS = "api/member/aboutOtherPerson";

    /**
     * 发布列表
     */
    public static String RELEASE_list = "api/resource/message/memberResourceMessage";

    /**
     *发起担保
     * buyerLink	string
     * 买家联系方式
     *
     * content	string
     * 交易内容
     *
     * invited_user_id	integer($int64)
     * 受邀担保的用户id
     *
     * invited_username	string
     * 受邀担保的用户名(不用传，后台直接赋值)
     *
     * price	number
     * 价格(元)
     *
     * resourceMessageId	integer($int64)
     * 受邀担保的帖子id
     *
     * sellerLink	string
     * 卖家联系方式
     *
     * title	string
     * 标题
     *
     * userType	integer($int32)
     * 发起担保用户交易角色(1运营商(提需求),2服务商(提供服务))
     */
    public static String INITIATE_GUARANTEE = "api/member/order/initiateGuarantee";

    /**
     * 用户关注或取消关注商家
     */
    public static String FOLLOW_STORE = "api/member/followShop";

    /**
     * 用户收藏/或取消收藏帖子
     * flag	integer($int32)
     * 标识(1收藏;2取消收藏)
     *
     * resourceMessageId	integer($int64)
     * 资源id
     */
    public static String FOLLOW_RESOURCE = "api/resource/message/collectResourceMessage";



    /**
     *公告列表
     * 公告列表(type:1 专区管理,,type:3 弹框公告,type:9 app会员中心轮播图)
     */
    public static String SYSTEM_NOTICE = "api/base/noticeInfoList/{type}";

    /**
     * 需求/服务大厅
     *checkStatus	integer($int32)
     * 空全部，1未审核；2审核通过；3 审核失败
     *
     * current	integer($int32)
     * 当前页，默认 1
     *
     * Enum:
     * Array [ 1 ]
     * isGuarantee	integer($int32)
     * 是否允许担保(0：否,1：是)
     *
     * latest	integer($int32)
     * 最新(-1:不查最新，1：最新的)
     *
     * memberId	integer($int64)
     * 查询别人的资源列表 传该字段(查询自己的不用传)
     *
     * myCollect	integer($int32)
     * 是否是我收藏的资源(0否；1是)
     *
     * oneLevelClassification	integer($int64)
     * 一级分类
     *
     * size	integer($int32)
     * 每页显示条数，默认 10
     *
     * Enum:
     * Array [ 1 ]
     * title	string
     * 标题(模糊查询)
     *
     * twoLevelClassification	integer($int64)
     * 二级分类
     *
     * userType	integer($int32)
     * 最新(-1:所有，1：需求帖子，2：服务帖子)
     */
    public static String SERVER_HALL_LIST = "api/resource/message/page";


    /**
     * 担保列表
     * current	integer($int32)
     * 当前页，默认 1
     *
     * flag	integer($int32)
     * 0我发布的担保;1用户受委托的担保
     *
     * size	integer($int32)
     * 每页显示条数，默认 10
     *
     * status	integer($int32)
     * -1全部;0用户委托中;1平台审核中;2平台担保中;3平台拒绝;4用户取消担保;11担保成功;12担保失败(订单状态
     */
    public static String GUARANTEE_ORDER_LIST = "api/member/order/page";


    /**
     * 同意担保
     * id
     */
    public static String AGREE_GUARANTEE = "api/member/order/confirmOrder";
    /**
     * 取消担保
     * id
     */
    public static String CANCEL_GUARANTEE = "api/member/order/cancelOrder";

    /**
     * 修改担保
     * buyerLink	string
     * 买家联系方式
     *
     * commissionType	integer($int32)
     * 佣金类型(0买家付,1卖家付,2各付一半)
     *
     * content	string
     * 交易内容
     *
     * id	integer($int64)
     * 担保id
     *
     * price	number
     * 价格(元)
     *
     * sellerLink	string
     * 卖家联系方式
     *
     * title	string
     * 标题
     *
     * userType	integer($int32)
     * 发起担保用户交易角色(1运营商(提需求),2服务商(提供服务))
     */
    public static String MODIFY_GUARANTEE = "api/member/order/updateGuarantee";


    /**
     * 删除发布的资源
     */
    public static String DELETE_RELEASE = "api/resource/message/delResourceMessageById";


    /**
     * 上次登录时间
     */
    public static String LAST_LOGIN_DATE = "api/member/lastLoginTime";

    /**
     * 发送验证码
     * (phone:手机号码;type:0绑定手机或更换手机;1忘记密码)
     */
    public static String SEND_PHONE_CODE="api/sys/codeRecord/sendSms";


    /**
     * 忘记密码
     * phone 手机号
     * pwd  密码
     * smsCode 验证码
     */
    public static String FORGET_PASSWORD="api/sys/codeRecord/forgetPwd";


    /**
     * 修改密码
     * newPassword	string
     * 新密码
     *
     * oldPassword	string
     * 旧密码
     */
    public static String MODIFY_PASSWORD="api/member/updatePassword";


    /**
     * 绑定/修改手机
     *
     */
    public static String BIND_PHINE="api/sys/codeRecord/bindPhone";

    /**
     * 收藏/浏览的帖子
     *
     * current
     * size
     * type  0浏览的帖子;1收藏的帖子
     */
    public static String MINE_FOLLOW_RESOURCE="api/resource/message/myCollectResourceMessage";

    /**
     *删除浏览或收藏的帖子
     * ids 多个id用逗号隔开
     */
    public static String DELETE_FOLLOW_RESOURCE="api/resource/message/batchDelMemberCollectMessage";

    /**
     * 光柱或浏览的商家
     * current	integer($int32)
     * 当前页，默认 1
     *
     * Enum:
     * Array [ 1 ]
     * size	integer($int32)
     * 每页显示条数，默认 10
     *
     * Enum:
     * Array [ 1 ]
     * type	integer($int32)
     * example: 2
     * 2关注的商家;3浏览过的商家
     */
    public static String MINE_FOLLOW_STORE ="api/member/myFollowShopList";

    /**
     * 删除浏览或收藏的店铺
     */
    public static String DELETE_FOLLOW_STORE="api/member/batchCancelFollow";

    /**
     * 我要合作
     * current
     * size
     */
    public static String COOPERATION="api/business/cooperation/page";

    /**
     * 提示文字
     *themetype  (0发布规则,1帖子规则,2联合运营,3担保规则,4我要合作,5关于我们,6交易问题,7常见问题8信誉专区)(多个逗号分隔)
     */
    public static String TIP_CONTENT="api/base/promptWord/{themetype}";

    /**
     * 诚信商家
     *current
     * size
     */
    public static String HONESTY_MERCHANT="api/member/verifiedShopList";

    /**
     * 首页跑马灯
     */
    public static String HOME_PAOMA="api/base/indexLetterList";

    /**
     * 线路列表
     */
    public static String URL_LIST="api/base/appRequestDomains";

    /**
     * 申请联合运营
     *
     * telegram
     * twoLevelClassification 认证类型(二级分类ID集合)
     * verifyIntroduction  入驻简介
     */

    public static String VERIFY="api/member/submitVerifyInfo";

    /**
     * 维度消息数
     */
    public static String UN_READ_MESSAGE="api/base/letterUnreadNum";

    /**
     * 消息已读
     */
    public static String MESSAGE_DETAILS="api/base/letterDetail/{letter_id}";

    /**
     * 担保详情
     * memberOrderId
     */
    public static String GUARANTEE_DETAILS="api/member/order/memberOrderDetails";


    /**
     * 官方群组
     */
    public static String OFFICIAL_GROUP ="api/official/group/page";
}
