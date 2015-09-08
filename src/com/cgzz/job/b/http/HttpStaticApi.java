package com.cgzz.job.b.http;

import android.os.Environment;

public class HttpStaticApi {

	public static final String IP = "www.haoshoubang.com";// IP
	public static final String PORT = "8080";// 端口

	public static final String Send_TheirProfile = Environment
			.getExternalStorageDirectory() + "/PartTimeJob/theirProfile"; // 个人资料头像和身份证图片保存目录
	public static final String DB_PATH = Environment
			.getExternalStorageDirectory() + "/PartTimeJob/db/";// 数据库存放目录
	public static final int SUCCESS_HTTP = 10000;// 成功
	public static final int FAILURE_HTTP = 40001;// 失败
	public static final int FAILURE_MSG_HTTP = 40002;// 失败提示msg

	public static final int testa_Http = 10002;//
	public static final int testb_Http = 10003;//

	public static final int login_Http = 10010;// 登录
	public static final int sendCode_Http = 10011;// 验证码
	public static final int uploadiv_Http = 10012;//
	public static final int register_Http = 10013;////
	public static final int forgetPassword_Http = 10014;//
	public static final int opinion_Http = 10015;//
	public static final int updatePassword_Http = 10016;//
	public static final int carousel_Http = 10017;//
	public static final int mylist_Http = 10018;//
	public static final int Training_Http = 10019;//
	public static final int citylist_Http = 10020;//
	public static final int detail_Http = 10021;//
	public static final int collection_Http = 10022;//
	public static final int ordercList_Http = 10023;//
	public static final int grab_Http = 10024;//
	public static final int record_Http = 10025;//
	
	public static final int arrive_Http = 10026;//
	public static final int cancelReason_Http = 10027;//
	public static final int cancel_Http = 10028;//
	public static final int stopAccept_Http = 10029;//
	
	public static final int myIncome_Http = 10030;//
	
	//======================================
	public static final int search_Http = 10050;//
	public static final int infoB_Http = 10051;//
	public static final int createB_Http = 10052;//
	public static final int loginB_Http = 10053;//
	public static final int createOrderbB_Http = 10054;//
	public static final int DandqianB_Http = 10055;//
	public static final int WanchengB_Http = 10056;//
	
	public static final int PingjiaB_Http = 10057;//
	public static final int arriveB_Http = 10058;//
	public static final int member1B_Http = 10059;//
	public static final int member2B_Http = 10060;//
	
	public static final int deletememberB_Http = 10061;//
	public static final int updatememberB_Http = 10062;//
	public static final int userbSearchB_Http = 10063;//
	public static final int addmemberB_Http = 10064;//
	public static final int bangkeCountB_Http = 10065;//
	public static final int checkB_Http = 10066;//
	public static final int reviewB_Http = 10067;//
	public static final int exitHotelB_Http = 10068;//
	public static final int applyB_Http = 10069;//
	public static final int collectionB_Http = 10070;//
	public static final int myhotelB_Http = 10071;//
	public static final int updateHotelB_Http = 10072;//
	public static final int deleteCollectionB_Http = 10073;//
	public static final int  version_Http = 10037;//
	public static final int  changePriceB_Http = 10074;//
	public static final int  ChecklistDaiB_Http = 10075;//
	public static final int  ChecklistYiB_Http = 10076;//
	public static final int  statisticsB_Http = 10077;//
	
	public static final int  hsbB_Http = 10078;//
	public static final int  bangkesB_Http = 10079;//
	public static final int  orderinfoB_Http = 10080;//
	public static final int  paylistB_Http = 10081;//
	public static final int  messagelistB_Http = 10082;//
	public static final int  priceB_Http = 10083;//
}


