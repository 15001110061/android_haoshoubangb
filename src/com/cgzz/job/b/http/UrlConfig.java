package com.cgzz.job.b.http;

import com.cgzz.job.b.application.GlobalVariables;

/**
 * Url
 * 
 * 
 */
public class UrlConfig {
	public static final int PATH_KEY_REGISTERED = 1001;// 注册成功关闭的集合
	public static final int PATH_KEY_PAY = 1002;// 支付成功关闭的集合（选择支付和最后支付界面）
//	 public static final String ROOT = "http://service.haoshoubang.com/";
//	public static final String ROOT = "http://test.haoshoubang.com/";

	public static final String notify_url = GlobalVariables.ROOT + "good-help/pay/aliyunCallBack";

	/**
	 * 
	 */
	public static final String OWNER_LABEL_LIST = GlobalVariables.ROOT + "?r=default/owner/labelList";
	/**
	GlobalVariables.ROOT录
	 */
	public static final String login_Http = GlobalVariables.ROOT + "good-helGlobalVariables.ROOTerc/login";

	/**
	 * 验证码
	 */
	public static final String sendCode_Http = GlobalVariables.ROOT + "good-help/base/sendCode";

	/**
	 * 上传图片
	 */
	public static final String uploadiv_Http = GlobalVariables.ROOT + "good-help/base/upload";

	/**
	 * 注册
	 */
	public static final String register_Http = GlobalVariables.ROOT + "good-help/userc/register";

	/**
	 * 忘记密码
	 */
	public static final String forgetPassword_Http = GlobalVariables.ROOT + "good-help/base/forgetPassword";

	/**
	 * 意见反馈
	 */
	public static final String opinion_Http = GlobalVariables.ROOT + "good-help/base/opinion";

	/**
	 * 修改密码
	 */
	public static final String updatePassword_Http = GlobalVariables.ROOT + "good-help/base/updatePassword";

	/**
	 * 更新个人资料
	 */
	public static final String updateUser_Http = GlobalVariables.ROOT + "good-help/userc/update";

	/**
	 * 9、轮播图接口
	 */
	public static final String carousel_Http = GlobalVariables.ROOT + "good-help/base/carousel";

	/**
	 * 当前订单裂变
	 */
	public static final String mylist_Http = GlobalVariables.ROOT + "good-help/orderc/mylist";
	/**
	 * 城市列表
	 */
	public static final String citylist_Http = GlobalVariables.ROOT + "good-help/base/citylist";

	/**
	 * 订单详情页接口
	 */
	public static final String detail_Http = GlobalVariables.ROOT + "good-help/orderc/detail";

	/**
	 * 我的收藏接口
	 */
	public static final String collection_Http = GlobalVariables.ROOT + "good-help/orderc/collection";

	/**
	 * 推荐订单列表接口
	 */
	public static final String ordercList_Http = GlobalVariables.ROOT + "good-help/orderc/list";

	/**
	 * 用户抢单接口
	 */
	public static final String grab_Http = GlobalVariables.ROOT + "good-help/orderc/grab";

	/**
	 * 完成酒店订单记录接口
	 */
	public static final String record_Http = GlobalVariables.ROOT + "good-help/orderc/record";

	/**
	 * 确认到达接口
	 */
	public static final String arrive_Http = GlobalVariables.ROOT + "good-help/orderc/arrive";

	/**
	 * 取消订单原因列表接口
	 */
	public static final String cancelReason_Http = GlobalVariables.ROOT + "good-help/orderc/cancelReason";

	/**
	 * 取消订单接口
	 */
	public static final String cancel_Http = GlobalVariables.ROOT + "good-help/orderc/cancel";

	/**
	 * 停止接单接口
	 */
	public static final String stopAccept_Http = GlobalVariables.ROOT + "good-help/userc/stopAccept";

	/**
	 * 我的收入接口
	 */
	public static final String myIncome_Http = GlobalVariables.ROOT + "good-help/userc/myIncome";

	// =============================

	/**
	 * 搜索酒店接口
	 */
	public static final String search_Http = GlobalVariables.ROOT + "good-help/company/search";

	/**
	 * 注册
	 */
	public static final String registerB_Http = GlobalVariables.ROOT + "good-help/userb/register";

	/**
	 * 注册时搜索出企业后点击接口
	 */
	public static final String infoB_Http = GlobalVariables.ROOT + "good-help/company/info";

	/**
	 * 填写企业信息接口
	 */
	public static final String createB_Http = GlobalVariables.ROOT + "good-help/company/create";

	/**
	 * 登录
	 */
	public static final String loginB_Http = GlobalVariables.ROOT + "good-help/userb/login";
	/**
	 * 创建订单接口
	 */
	public static final String createOrderbB = GlobalVariables.ROOT + "good-help/orderb/create";

	/**
	 * 我的订单接口
	 */
	public static final String orderbMylistB = GlobalVariables.ROOT + "good-help/orderb/mylist";

	/**
	 * 查看到岗接口
	 */
	public static final String arriveB = GlobalVariables.ROOT + "good-help/orderb/arrive";

	/**
	 * 取消订单原因列表接口
	 */
	public static final String cancelReasonB_Http = GlobalVariables.ROOT + "good-help/orderb/cancelReason";

	/**
	 * 取消订单接口
	 */
	public static final String cancelB_Http = GlobalVariables.ROOT + "good-help/orderb/cancel";

	/**
	 * 成员管理列表接口
	 */
	public static final String memberB_Http = GlobalVariables.ROOT + "good-help/userb/member";

	/**
	 * 删除成员
	 */
	public static final String deletememberB_Http = GlobalVariables.ROOT + "good-help/userb/deletemember";

	/**
	 * 同意后修改成员，忽略（同一个接口）
	 */
	public static final String updatememberB_Http = GlobalVariables.ROOT + "good-help/userb/updatemember";

	/**
	 * 搜索企业用户
	 */
	public static final String userbSearchB_Http = GlobalVariables.ROOT + "good-help/userb/search";

	/**
	 * 搜索出企业用户确认添加
	 */
	public static final String addmemberB_Http = GlobalVariables.ROOT + "good-help/userb/addmember";

	/**
	 * 可服务帮客人数接口
	 */
	public static final String bangkeCountB_Http = GlobalVariables.ROOT + "good-help/userc/bangkeCount";

	/**
	 * 订单验收接口
	 */
	public static final String checkB_Http = GlobalVariables.ROOT + "good-help/orderb/check";

	/**
	 * 评价收藏接口
	 */
	public static final String reviewB_Http = GlobalVariables.ROOT + "good-help/orderb/review";
	/**
	 * 、解除企业绑定
	 */
	public static final String exitHotelB_Http = GlobalVariables.ROOT + "good-help/userb/exitHotel";
	/**
	 * 、申请加入
	 */
	public static final String applyB_Http = GlobalVariables.ROOT + "good-help/userb/apply";

	/**
	 * 我的收藏接口
	 */
	public static final String collectionB_Http = GlobalVariables.ROOT + "good-help/userb/collection";

	/**
	 * 我的企业信息接口
	 */
	public static final String myhotelB_Http = GlobalVariables.ROOT + "good-help/userb/myhotel";

	/**
	 * 修改用户酒店信息接口
	 */
	public static final String updateHotelB_Http = GlobalVariables.ROOT + "good-help/userb/update";

	/**
	 * 删除收藏接口
	 */
	public static final String deleteCollectionB_Http = GlobalVariables.ROOT + "good-help/userb/deleteCollection";
	/**
	 * 更新版本
	 */
	public static final String version_Http = GlobalVariables.ROOT + "good-help/base/version";
	/**
	 * 调整价格接口
	 */
	public static final String createOrderbB_Http = GlobalVariables.ROOT + "good-help/orderb/changePrice";

	/**
	 * 验收订单接口
	 */
	public static final String checklistB_Http = GlobalVariables.ROOT + "good-help/orderb/checklist";

	/**
	 * 统计列表
	 */
	public static final String statisticsB_Http = GlobalVariables.ROOT + "good-help/orderb/statistics";

	/**
	 * 向好手帮转账
	 */
	public static final String hsbB_Http = GlobalVariables.ROOT + "good-help/pay/hsb";

	/**
	 * 向好手帮转账
	 */
	public static final String bangkesB_Http = GlobalVariables.ROOT + "good-help/pay/bangkes";

	/**
	 *
	 */
	public static final String orderinfoB_Http = GlobalVariables.ROOT + "good-help/pay/sign";

	/**
	 *
	 */
	public static final String paylistB_Http = GlobalVariables.ROOT + "good-help/pay/paylist";

	/**
	 * 消息列表接口
	 */
	public static final String messagelistB_Http = GlobalVariables.ROOT + "good-help/information/messagelist";

	/**
	 * 建订单前返回标间套间最低价
	 */
	public static final String priceB_Http = GlobalVariables.ROOT + "good-help/orderb/price";
	
	
	
	/**
	 * 已完成订单查看支付帮客金额
	 */
	public static final String completeDetailB_Http = GlobalVariables.ROOT + "good-help/orderb/completeDetail";
}
