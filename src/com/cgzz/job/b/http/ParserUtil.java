package com.cgzz.job.b.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;

/**
 * 解析类
 * 
 * 
 */
public class ParserUtil {
	public static final String USE_TIME = "use_time";
	public static final String SEAT = "seat";
	public static final String UID = "uid";
	public static final String GENDER = "gender";
	public static final String AGE = "age";
	public static final String EMOTION = "emotion";
	public static final String SIGNATURE = "signature";
	public static final String PROFESSION = "profession";
	public static final String LOGO = "logo";
	public static final String GRADE = "grade";
	public static final String BIRTHDAY = "birthday";
	public static final String INSURANCE = "insurance";
	public static final String VERIFY = "verify";
	public static final String ORDER_ID = "order_id";
	public static final String LNG = "lng";
	public static final String LAT = "lat";
	public static final String WAY = "way";
	public static final String END_LAT = "end_lat";
	public static final String END_LON = "end_lon";
	public static final String START_LAT = "start_lat";
	public static final String START_LON = "start_lon";
	public static final String TYPE = "type";
	public static final String TERMINAL = "terminal";
	public static final String STEER = "steer";
	public static final String DRIVING = "driving";
	public static final String STATE = "state";
	public static final String MESSAGE = "message";
	public static final String LIST = "list";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String PIC = "pic";

	public static final String ORDER_SN = "order_sn";
	public static final String MONEY = "money";
	public static final String E_FLOWER = "e-flower";

	public static final String USER_ID = "user_id";
	public static final String ORIGIN = "origin";
	public static final String COMMENT = "comment";
	public static final String LABEL = "label";
	public static final String RANK = "rank";
	public static final String CONTENT = "content";
	public static final String ADD_TIME = "add_time";
	public static final String FIVE = "five";
	public static final String FOUR = "four";
	public static final String THREE = "three";
	public static final String TWO = "two";
	public static final String ONE = "one";

	public static final String INFO_ID = "info_id";
	public static final String ROAD_FUEL = "road_fuel";
	public static final String DISCOUNT = "discount";
	public static final String VOUCHER = "voucher";
	public static final String NUMBER = "number";

	public static final String CARBON = "carbon";
	public static final String SAVE_CARBON = "save_carbon";

	public static final String USER_NAME = "user_name";
	public static final String USER_LOGO = "user_logo";

	public static final String INTEGRAL = "integral";
	public static final String CARNUMBER = "carNumber";
	public static final String FANS = "fans";
	public static final String GENERALIZE = "generalize";
	public static final String RANKING = "ranking";

	public static final String DISTANCE = "distance";
	public static final String USERNUMBER = "userNumber";
	public static final String NOTICE = "notice";

	public static final String PLACE = "place";
	public static final String INTRODUCE = "introduce";
	public static final String JOIN = "join";
	public static final String MAINID = "mainId";
	public static final String MAINNAME = "mainName";
	public static final String MAINlOGO = "mainLogo";
	public static final String LIVENESS = "liveness";
	public static final String GROUPUSER = "groupUser";

	public static final String COMMENT_RANK = "comment_rank";
	public static final String BRAND = "brand";
	public static final String COLOR = "color";
	public static final String SUCCESS_NUMBER = "success_number";

	public static final String GID = "gid";

	/**
	 * 判断返回的结果是否是成功的.初步解析result和message,如果成功(result!=0)返回true,调用该方法的代码应该继续解析data
	 * .否则,返回false,data不用再解析.
	 * 
	 * @param bundle
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	private static boolean isResultSuccess(Bundle bundle, JSONObject jsonObject) throws JSONException {
		boolean isSuc = false;
		if (jsonObject.has(STATE)) {
			int state = jsonObject.optInt(STATE);
			bundle.putInt(STATE, state);
			if (state == 1) {
				isSuc = true;
			}
			if (jsonObject.has(MESSAGE)) {
				bundle.putString(MESSAGE, jsonObject.optString(MESSAGE));
			}
		}
		return isSuc;
	}

	/**
	 * 解析评价标签
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserLabelList(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {// 如果result==0,不再解析data,直接返回bundle;
			return bundle;
		}
		ArrayList<Map<String, String>> labels = new ArrayList<Map<String, String>>();
		JSONArray listObject = jsonObject.optJSONArray(LIST);
		if (listObject != null) {
			for (int i = 0; i < listObject.length(); i++) {
				JSONObject object = listObject.optJSONObject(i);
				String name = object.optString(NAME);
				String id = object.optString(ID);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(NAME, name);
				map.put(ID, id);
				labels.add(map);
			}
			bundle.putSerializable(LIST, labels);
		}
		return bundle;
	}

	/**
	 * 解析热门搜索
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserHotSearch(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {// 如果result==0,不再解析data,直接返回bundle;
			return bundle;
		}
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		JSONArray listObject = jsonObject.optJSONArray(LIST);
		if (listObject != null) {
			for (int i = 0; i < listObject.length(); i++) {
				JSONObject object = listObject.optJSONObject(i);
				String name = object.optString(NAME);
				String id = object.optString(ID);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(NAME, name);
				map.put(ID, id);
				list.add(map);
			}
			bundle.putSerializable(LIST, list);
		}
		return bundle;
	}

	/**
	 * 解析活动
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserActivityList(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		JSONArray listObject = jsonObject.optJSONArray(LIST);
		if (listObject != null) {
			for (int i = 0; i < listObject.length(); i++) {
				JSONObject object = listObject.optJSONObject(i);
				String title = object.optString(TITLE);
				String id = object.optString(ID);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(TITLE, title);
				map.put(ID, id);
				list.add(map);
			}
			bundle.putSerializable(LIST, list);
		}
		return bundle;
	}

	/**
	 * 解析轮播图
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserShuffling(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		JSONArray listObject = jsonObject.optJSONArray("pics");
		if (listObject != null) {
			for (int i = 0; i < listObject.length(); i++) {
				JSONObject object = listObject.optJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("title", object.optString("title"));
				map.put("pic_url_path", object.optString("pic_url_path"));
				map.put("id", object.optString("id"));
				map.put("req_url_path", object.optString("req_url_path"));
				list.add(map);
			}
			bundle.putSerializable("list", list);
		}

		JSONObject jsonObject2 = new JSONObject(jsonObject.getString("sum").toString());
		if (jsonObject2.has("pinche_sum")) {
			bundle.putString("pinche_sum", jsonObject2.getString("pinche_sum"));
		}
		if (jsonObject2.has("cost_save")) {
			bundle.putString("cost_save", jsonObject2.getString("cost_save"));
		}
		return bundle;
	}

	/**
	 * 解析身份认证
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserAccountBalance(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		int driving = jsonObject.optInt(DRIVING);
		int steer = jsonObject.optInt(STEER);
		bundle.putInt(DRIVING, driving);
		bundle.putInt(STEER, steer);
		return bundle;
	}

	/**
	 * 解析上传证件返回的数据
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserUploadLicence(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		int id = jsonObject.optInt(ID);
		bundle.putInt(ID, id);
		return bundle;

	}

	/**
	 * 解析驾驶证验证返回的数据
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserDrivingLicence(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		return bundle;
	}

	/**
	 * 解析个人信息中，昵称、感情状态、性别、行业、个性签名、年龄段、时间、头像修改等信息 注册1,分享,车主—评价
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserJson(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		isResultSuccess(bundle, jsonObject);
		return bundle;
	}

	/**
	 * 解析登录，第三方登录，注册2
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserLogin(String json) {
		Bundle bundle = null;
		try {

			JSONObject jsonObject = new JSONObject(json);
			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}
			if (jsonObject.has("uid")) {
				bundle.putString("uid", jsonObject.getString("uid"));
			}
			if (jsonObject.has("phone")) {
				bundle.putString("phone", jsonObject.getString("phone"));
			}
			if (jsonObject.has("token")) {
				bundle.putString("token", jsonObject.getString("token"));
			}
			if (jsonObject.has("member")) {
				bundle.putString("member", jsonObject.getString("member"));
			}
			if (jsonObject.has("name")) {
				bundle.putString("name", jsonObject.getString("name"));
			}
			if (jsonObject.has("logo")) {
				bundle.putString("logo", jsonObject.getString("logo"));
			}

			if (jsonObject.has("signature")) {
				bundle.putString("signature", jsonObject.getString("signature"));
			}

			if (jsonObject.has("profession")) {
				bundle.putString("profession", jsonObject.getString("profession"));
			}

			if (jsonObject.has("car_name")) {
				bundle.putString("car_name", jsonObject.getString("car_name"));
			}

			if (jsonObject.has("licence_sn")) {
				bundle.putString("licence_sn", jsonObject.getString("licence_sn"));
			}

			if (jsonObject.has("is_register")) {
				bundle.putString("is_register", jsonObject.getString("is_register"));
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return bundle;
	}

	/**
	 * 解析验证码
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserCode(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		if (jsonObject.has("code")) {
			bundle.putString("code", jsonObject.getString("code"));
		}
		return bundle;
	}

	/**
	 * 解析忘记密码，修改密码
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserForgetPwd(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		if (jsonObject.has("token")) {
			bundle.putString("token", jsonObject.getString("token"));
		}
		return bundle;
	}

	/**
	 * 解析我的信息
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserMyInfo(String json) {
		JSONObject jsonObject;
		Bundle bundle = null;
		try {
			jsonObject = new JSONObject(json);

			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}
			if (jsonObject.has(UID)) {
				bundle.putString(UID, jsonObject.getString(UID));
			}
			if (jsonObject.has("phone")) {
				bundle.putString("phone", jsonObject.getString("phone"));
			}
			if (jsonObject.has(NAME)) {
				bundle.putString(NAME, jsonObject.getString(NAME));
			}
			if (jsonObject.has(GENDER)) {
				bundle.putString(GENDER, jsonObject.getString(GENDER));
			}
			if (jsonObject.has(AGE)) {
				bundle.putString(AGE, jsonObject.getString(AGE));
			}
			if (jsonObject.has(EMOTION)) {
				bundle.putString(EMOTION, jsonObject.getString(EMOTION));
			}
			if (jsonObject.has(SIGNATURE)) {
				bundle.putString(SIGNATURE, jsonObject.getString(SIGNATURE));
			}
			if (jsonObject.has(PROFESSION)) {
				bundle.putString(PROFESSION, jsonObject.getString(PROFESSION));
			}
			if (jsonObject.has(BIRTHDAY)) {
				bundle.putString(BIRTHDAY, jsonObject.getString(BIRTHDAY));
			}
			if (jsonObject.has(ADD_TIME)) {
				bundle.putString(ADD_TIME, jsonObject.getString(ADD_TIME));
			}
			if (jsonObject.has(LOGO)) {
				bundle.putString(LOGO, jsonObject.getString(LOGO));
			}
			if (jsonObject.has(PIC)) {
				JSONArray jsonArray = jsonObject.getJSONArray(PIC);
				ArrayList<Map<String, String>> gridviewList = new ArrayList<Map<String, String>>();
				for (int i = 0; i < jsonArray.length(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();

					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
					map.put(ID, jsonObject2.optString(ID));
					map.put(USER_ID, jsonObject2.optString(USER_ID));
					map.put(LOGO, jsonObject2.optString(LOGO));
					map.put(PIC, jsonObject2.optString(PIC));
					gridviewList.add(map);
				}
				bundle.putSerializable("gridviewList", gridviewList);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bundle;
	}

	/**
	 * 解析我的信息 ls
	 */
	public static Bundle MyInfoJSON(String json) {
		JSONObject jsonObject;
		Bundle bundle = null;
		try {
			jsonObject = new JSONObject(json);

			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}
			if (jsonObject.has("gender")) {
				bundle.putString("gender", jsonObject.getString("gender"));
			}
			if (jsonObject.has("logo")) {
				bundle.putString("logo", jsonObject.getString("logo"));
			}
			if (jsonObject.has("name")) {
				bundle.putString("name", jsonObject.getString("name"));
			}
			if (jsonObject.has("profession")) {
				bundle.putString("profession", jsonObject.getString("profession"));
			}
			if (jsonObject.has("licence_sn")) {
				bundle.putString("licence_sn", jsonObject.getString("licence_sn"));
			}
			if (jsonObject.has("uid")) {
				bundle.putString("uid", jsonObject.getString("uid"));
			}
			if (jsonObject.has("car_name")) {
				bundle.putString("car_name", jsonObject.getString("car_name"));
			}
			if (jsonObject.has("brand_id")) {
				bundle.putString("brand_id", jsonObject.getString("brand_id"));
			}
			if (jsonObject.has("phone")) {
				bundle.putString("phone", jsonObject.getString("phone"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	/**
	 * 账户余额
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserMyAccount(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		if (jsonObject.has("balance")) {
			bundle.putString("balance", jsonObject.getString("balance"));
		}
		return bundle;
	}

	/**
	 * 解析我的等级
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserMyLevel(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		if (jsonObject.has(GRADE)) {
			bundle.putInt(GRADE, jsonObject.getInt(GRADE));
		}
		if (jsonObject.has("score")) {
			bundle.putInt("score", jsonObject.getInt("score"));
		}
		if (jsonObject.has("infoNumber")) {
			bundle.putInt("infoNumber", jsonObject.getInt("infoNumber"));
		}
		if (jsonObject.has("info")) {
			bundle.putInt("info", jsonObject.getInt("info"));
		}
		if (jsonObject.has("userNumber")) {
			bundle.putInt("userNumber", jsonObject.getInt("userNumber"));
		}
		if (jsonObject.has("user")) {
			bundle.putInt("user", jsonObject.getInt("user"));
		}
		if (jsonObject.has("commentNumber")) {
			bundle.putInt("commentNumber", jsonObject.getInt("commentNumber"));
		}
		if (jsonObject.has("comment")) {
			bundle.putString("comment", jsonObject.getString("comment"));
		}

		return bundle;
	}

	/**
	 * 3.15 车主_轮询获取支付结果接口
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserPayStatus(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		if (jsonObject.has(TYPE)) {
			bundle.putInt(TYPE, jsonObject.getInt(TYPE));
		}
		if (jsonObject.has(ORDER_SN)) {
			bundle.putString(ORDER_SN, jsonObject.getString(ORDER_SN));
		}
		if (jsonObject.has(MONEY)) {
			bundle.putString(MONEY, jsonObject.getString(MONEY));
		}
		if (jsonObject.has(E_FLOWER)) {
			bundle.putString(E_FLOWER, jsonObject.getString(E_FLOWER));
		}

		return bundle;
	}

	/**
	 * 解析车主查看详情
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle insuranceSurplusJSON(String json) {
		Bundle bundle = null;
		try {
			JSONObject jsonObject;

			jsonObject = new JSONObject(json);

			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}

			if (jsonObject.has("user_name")) {
				bundle.putString("user_name", jsonObject.getString("user_name"));
			}
			if (jsonObject.has("user_logo")) {
				bundle.putString("user_logo", jsonObject.getString("user_logo"));
			}
			if (jsonObject.has("end_lng")) {
				bundle.putString("end_lng", jsonObject.getString("end_lng"));
			}
			if (jsonObject.has(END_LAT)) {
				bundle.putString(END_LAT, jsonObject.getString(END_LAT));
			}
			if (jsonObject.has("comment_rank")) {
				bundle.putString("comment_rank", jsonObject.getString("comment_rank"));
			}
			if (jsonObject.has("rank")) {
				bundle.putString("rank", jsonObject.getString("rank"));
			}
			if (jsonObject.has(GENDER)) {
				bundle.putString(GENDER, jsonObject.getString(GENDER));
			}
			if (jsonObject.has("is_insurance")) {
				bundle.putString("is_insurance", jsonObject.getString("is_insurance"));
			}
			if (jsonObject.has("origin")) {
				bundle.putString("origin", jsonObject.getString("origin"));
			}
			if (jsonObject.has(TERMINAL)) {
				bundle.putString(TERMINAL, jsonObject.getString(TERMINAL));
			}

			if (jsonObject.has("dist")) {
				bundle.putString("dist", jsonObject.getString("dist"));
			}
			if (jsonObject.has("time")) {
				bundle.putString("time", jsonObject.getString("time"));
			}
			if (jsonObject.has("user_sn")) {
				bundle.putString("user_sn", jsonObject.getString("user_sn"));
			}
			if (jsonObject.has("money")) {
				bundle.putString("money", jsonObject.getString("money"));
			}
			if (jsonObject.has("start_lng")) {
				bundle.putString("start_lng", jsonObject.getString("start_lng"));
			}

			if (jsonObject.has("start_lat")) {
				bundle.putString("start_lat", jsonObject.getString("start_lat"));
			}
			if (jsonObject.has("comment_count")) {
				bundle.putString("comment_count", jsonObject.getString("comment_count"));
			}
			if (jsonObject.has("phone")) {
				bundle.putString("phone", jsonObject.getString("phone"));
			}
			if (jsonObject.has("vehicle_number")) {
				bundle.putString("vehicle_number", jsonObject.getString("vehicle_number"));
			}
			if (jsonObject.has("act_id")) {
				bundle.putString("act_id", jsonObject.getString("act_id"));
			}

			if (jsonObject.has("owner_order_id")) {
				bundle.putString("owner_order_id", jsonObject.getString("owner_order_id"));
			}

			if (jsonObject.has("pass_order_id")) {
				bundle.putString("pass_order_id", jsonObject.getString("pass_order_id"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	/**
	 * 解析我的常用路线
	 * 
	 * @param json
	 * @throws JSONException
	 */
	public static Bundle parserRouteList(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		JSONArray listObject = jsonObject.optJSONArray(LIST);
		if (listObject != null) {
			for (int i = 0; i < listObject.length(); i++) {
				JSONObject object = listObject.optJSONObject(i);
				Map<String, Object> map = new HashMap<String, Object>();
				if (object != null) {
					map.put(ID, object.optString(ID));
					map.put(USER_ID, object.optString(USER_ID));
					map.put(ORIGIN, object.optString(ORIGIN));
					map.put(TERMINAL, object.optString(TERMINAL));
					map.put(TYPE, object.optString(TYPE));
					map.put(START_LON, object.optString(START_LON));
					map.put(START_LAT, object.optString(START_LAT));
					map.put(END_LON, object.optString(END_LON));
					map.put(END_LAT, object.optString(END_LAT));
					JSONArray jsonArray = object.optJSONArray(WAY);
					if (jsonArray != null && jsonArray.length() > 0) {
						List<Map<String, String>> wayList = new ArrayList<Map<String, String>>();
						for (int j = 0; j < jsonArray.length(); j++) {
							JSONObject wayObj = jsonArray.optJSONObject(j);
							Map<String, String> wayMap = new HashMap<String, String>();
							if (wayObj != null) {
								wayMap.put(ID, wayObj.optString(ID));
								wayMap.put(ORDER_ID, wayObj.optString(ORDER_ID));
								wayMap.put(NAME, wayObj.optString(NAME));
								wayMap.put(LNG, wayObj.optString(LNG));
								wayMap.put(LAT, wayObj.optString(LAT));
							}
							wayList.add(wayMap);
						}
						map.put(WAY, wayList);
					}
				}
				list.add(map);
			}
		}
		bundle.putSerializable(LIST, list);
		return bundle;
	}

	/**
	 * 解析添加常用路线之后的数据
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserAddRoute(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		return bundle;

	}

	/**
	 * 车主_取消邀请和发布接口共用
	 * 
	 * @param json
	 * @throws JSONException
	 */
	public static Bundle driversCancelDisinvitedJSON(String json) {
		JSONObject jsonObject;
		Bundle bundle = null;
		try {
			jsonObject = new JSONObject(json);

			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}
			if (jsonObject.has("info_id")) {
				bundle.putString("info_id", jsonObject.getString("info_id"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bundle;
	}

	/**
	 * 车主_确认到达目的地(state=2 的情况)
	 * 
	 * @param json
	 * @throws JSONException
	 */
	public static Bundle driversreachJSON(String json) {
		JSONObject jsonObject;
		Bundle bundle = null;
		try {
			jsonObject = new JSONObject(json);
			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	/**
	 * 解析用户评价返回的数据
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserUserComment(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		bundle.putString(COMMENT, jsonObject.optInt(COMMENT) + "");
		JSONArray labelArray = jsonObject.optJSONArray(LABEL);
		ArrayList<Map<String, String>> labelList = new ArrayList<Map<String, String>>();
		if (labelArray != null && labelArray.length() > 0) {
			for (int i = 0; i < labelArray.length(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				JSONObject labelObj = labelArray.optJSONObject(i);
				if (labelObj != null) {
					map.put(ID, labelObj.optString(ID));
					map.put(NAME, labelObj.optString(NAME));
				}
				labelList.add(map);
			}
		}
		bundle.putSerializable(LABEL, labelList);
		JSONArray listArray = jsonObject.optJSONArray(LIST);
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (listArray != null && listArray.length() > 0) {
			for (int i = 0; i < listArray.length(); i++) {
				JSONObject obj = listArray.optJSONObject(i);
				Map<String, String> map = new HashMap<String, String>();
				if (obj != null) {
					map.put(ID, obj.optString(ID));
					map.put(RANK, obj.optString(RANK));
					map.put(CONTENT, obj.optString(CONTENT));
					map.put(ADD_TIME, obj.optString(ADD_TIME));
				}
				list.add(map);
			}
		}
		bundle.putSerializable(LIST, list);
		bundle.putString(ONE, jsonObject.optString(ONE));
		bundle.putString(TWO, jsonObject.optString(TWO));
		bundle.putString(THREE, jsonObject.optString(THREE));
		bundle.putString(FOUR, jsonObject.optString(FOUR));
		bundle.putString(FIVE, jsonObject.optString(FIVE));
		return bundle;
	}

	/**
	 * 匹配结果解析
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Bundle matchingJSON(String json) {
		Bundle bundle = null;
		try {
			JSONObject jsonObject;

			jsonObject = new JSONObject(json);

			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}
			if (jsonObject.has("list")) {
				JSONArray jsonArray = jsonObject.getJSONArray("list");
				ArrayList<Map<String, String>> gridviewList = new ArrayList<Map<String, String>>();
				for (int i = 0; i < jsonArray.length(); i++) {
					HashMap map = new HashMap();

					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
					if (jsonObject2.has("user_id")) {
						map.put("user_id", jsonObject2.getString("user_id"));
					}

					if (jsonObject2.has("name")) {
						map.put("name", jsonObject2.getString("name"));
					}
					if (jsonObject2.has("logo")) {
						map.put("logo", jsonObject2.getString("logo"));
					}
					if (jsonObject2.has("gender")) {
						map.put("gender", jsonObject2.getString("gender"));
					}
					if (jsonObject2.has("origin")) {
						map.put("origin", jsonObject2.getString("origin"));
					}
					if (jsonObject2.has("terminal")) {
						map.put("terminal", jsonObject2.getString("terminal"));
					}
					if (jsonObject2.has("go_off")) {
						map.put("go_off", jsonObject2.getString("go_off"));
					}
					if (jsonObject2.has("seat")) {
						map.put("seat", jsonObject2.getString("seat"));
					}
					if (jsonObject2.has("distance")) {
						map.put("distance", jsonObject2.getString("distance"));
					}
					if (jsonObject2.has("user_time")) {
						map.put("user_time", jsonObject2.getString("user_time"));
					}
					if (jsonObject2.has("group_name")) {
						map.put("group_name", jsonObject2.getString("group_name"));
					}
					if (jsonObject2.has("surplus_seat")) {
						map.put("surplus_seat", jsonObject2.getString("surplus_seat"));
					}
					if (jsonObject2.has("is_insurance")) {
						map.put("is_insurance", jsonObject2.getString("is_insurance"));
					}
					if (jsonObject2.has("comment_rank")) {
						map.put("comment_rank", jsonObject2.getString("comment_rank"));
					}

					if (jsonObject2.has("brand")) {
						map.put("brand", jsonObject2.getString("brand"));
					}

					if (jsonObject2.has("color")) {
						map.put("color", jsonObject2.getString("color"));
					}
					if (jsonObject2.has("money")) {
						map.put("money", jsonObject2.getString("money"));
					}
					if (jsonObject2.has("id")) {
						map.put("id", jsonObject2.getString("id"));
					}
					if (jsonObject2.has("info_id")) {
						map.put("info_id", jsonObject2.getString("info_id"));
					}
					gridviewList.add(map);
				}
				bundle.putSerializable("lvlist", gridviewList);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	/**
	 * 我的邀请解析
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Bundle invitationJSON(String json) {
		Bundle bundle = null;
		try {
			JSONObject jsonObject;

			jsonObject = new JSONObject(json);

			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}
			if (jsonObject.has("list")) {
				JSONArray jsonArray = jsonObject.getJSONArray("list");
				ArrayList<Map<String, String>> gridviewList = new ArrayList<Map<String, String>>();
				for (int i = 0; i < jsonArray.length(); i++) {
					HashMap map = new HashMap();

					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
					if (jsonObject2.has("act_id")) {
						map.put("act_id", jsonObject2.getString("act_id"));
					}
					if (jsonObject2.has("info_id")) {
						map.put("info_id", jsonObject2.getString("info_id"));
					}
					if (jsonObject2.has("id")) {
						map.put("id", jsonObject2.getString("id"));
					}
					if (jsonObject2.has("order_id")) {
						map.put("order_id", jsonObject2.getString("order_id"));
					}
					if (jsonObject2.has("time")) {
						map.put("time", jsonObject2.getString("time"));
					}
					if (jsonObject2.has("user_id")) {
						map.put("user_id", jsonObject2.getString("user_id"));
					}

					if (jsonObject2.has("user_name")) {
						map.put("user_name", jsonObject2.getString("user_name"));
					}
					if (jsonObject2.has("user_logo")) {
						map.put("user_logo", jsonObject2.getString("user_logo"));
					}
					if (jsonObject2.has("gender")) {
						map.put("gender", jsonObject2.getString("gender"));
					}
					if (jsonObject2.has("rank")) {
						map.put("rank", jsonObject2.getString("rank"));
					}
					if (jsonObject2.has("comment_rank")) {
						map.put("comment_rank", jsonObject2.getString("comment_rank"));
					}
					if (jsonObject2.has("origin")) {
						map.put("origin", jsonObject2.getString("origin"));
					}
					if (jsonObject2.has("terminal")) {
						map.put("terminal", jsonObject2.getString("terminal"));
					}
					if (jsonObject2.has("type")) {
						map.put("type", jsonObject2.getString("type"));
					}
					if (jsonObject2.has("distance")) {
						map.put("distance", jsonObject2.getString("distance"));
					}

					if (jsonObject2.has(USE_TIME)) {
						map.put(USE_TIME, jsonObject2.getString(USE_TIME));
					}

					if (jsonObject2.has("match_type")) {
						map.put("match_type", jsonObject2.getString("match_type"));
					}
					if (jsonObject2.has("is_insurance")) {
						map.put("is_insurance", jsonObject2.getString("is_insurance"));
					}
					if (jsonObject2.has("numbers")) {
						map.put("numbers", jsonObject2.getString("numbers"));
					}

					if (jsonObject2.has("user_phone")) {
						map.put("user_phone", jsonObject2.getString("user_phone"));
					}
					if (jsonObject2.has("status")) {
						map.put("status", jsonObject2.getString("status"));
					}

					if (jsonObject2.has("owner_order_id")) {
						map.put("owner_order_id", jsonObject2.getString("owner_order_id"));
					}

					if (jsonObject2.has("pass_order_id")) {
						map.put("pass_order_id", jsonObject2.getString("pass_order_id"));
					}
					if (jsonObject2.has("start_lng")) {
						map.put("start_lng", jsonObject2.getString("start_lng"));
					}
					if (jsonObject2.has("start_lat")) {
						map.put("start_lat", jsonObject2.getString("start_lat"));
					}
					gridviewList.add(map);
				}
				bundle.putSerializable("lvlist", gridviewList);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	/**
	 * 3.10 乘客_支付页面
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserPay(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		if (jsonObject.has(INFO_ID)) {
			bundle.putString(INFO_ID, jsonObject.getString(INFO_ID));
		}
		if (jsonObject.has(ORDER_ID)) {
			bundle.putString(ORDER_ID, jsonObject.getString(ORDER_ID));
		}
		if (jsonObject.has(ORDER_SN)) {
			bundle.putString(ORDER_SN, jsonObject.getString(ORDER_SN));
		}
		if (jsonObject.has(ROAD_FUEL)) {
			bundle.putString(ROAD_FUEL, jsonObject.getString(ROAD_FUEL));
		}
		if (jsonObject.has(DISCOUNT)) {
			bundle.putString(DISCOUNT, jsonObject.getString(DISCOUNT));
		}
		if (jsonObject.has(VOUCHER)) {
			JSONArray jsonArray = jsonObject.getJSONArray(VOUCHER);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject voucher = jsonArray.getJSONObject(i);
				if (voucher.has(NUMBER)) {
					bundle.putString(NUMBER, voucher.getString(NUMBER));
				}
				if (voucher.has(MONEY)) {
					bundle.putString(MONEY, voucher.getString(MONEY));
				}
				if (voucher.has(ID)) {
					bundle.putString(ID, voucher.getString(ID));
				}
				if (voucher.has(CONTENT)) {
					bundle.putString(CONTENT, voucher.getString(CONTENT));
				}
			}

		}

		return bundle;
	}

	/**
	 * 5.7碳排放量
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserCarbonEmission(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		if (jsonObject.has(LIST)) {
			JSONArray jsonArray = jsonObject.getJSONArray(LIST);

			ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map = null;
			double carbon = 0;

			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject voucher = jsonArray.getJSONObject(i);
				map = new HashMap<String, String>();
				list.add(map);

				if (voucher.has(ADD_TIME)) {
					map.put(ADD_TIME, voucher.getString(ADD_TIME));
				}
				if (voucher.has(ORDER_SN)) {
					map.put(ORDER_SN, voucher.getString(ORDER_SN));
				}
				if (voucher.has(CARBON)) {
					map.put(CARBON, voucher.getDouble(CARBON) + "");
					carbon += Double.valueOf(voucher.getDouble(CARBON));
				}
			}
			bundle.putSerializable(LIST, list);
			bundle.putString(SAVE_CARBON, carbon + "");
		}
		return bundle;
	}

	/**
	 * 5.8排放量排行
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserCarbonBest(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		if (jsonObject.has(LIST)) {
			JSONArray jsonArray = jsonObject.getJSONArray(LIST);
			ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map = null;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject voucher = jsonArray.getJSONObject(i);
				map = new HashMap<String, String>();
				list.add(map);

				if (voucher.has(STATE)) {
					map.put(STATE, voucher.getString(STATE));
				}
				if (voucher.has(USER_NAME)) {
					map.put(USER_NAME, voucher.getString(USER_NAME));
				}
				if (voucher.has(USER_LOGO)) {
					map.put(USER_LOGO, voucher.getString(USER_LOGO));
				}
				if (voucher.has(CARBON)) {
					map.put(CARBON, voucher.getDouble(CARBON) + "");
				}
			}

			bundle.putSerializable(LIST, list);
		}
		return bundle;
	}

	/**
	 * 5.9推广排行
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserGeneralize(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		list.add(map);
		if (jsonObject.has(UID)) {
			map.put(UID, jsonObject.getString(UID));
		}
		if (jsonObject.has(NAME)) {
			map.put(NAME, jsonObject.getString(NAME));
		}
		if (jsonObject.has(LOGO)) {
			map.put(LOGO, jsonObject.getString(LOGO));
		}
		if (jsonObject.has(INTEGRAL)) {
			map.put(INTEGRAL, jsonObject.getString(INTEGRAL));
		}
		if (jsonObject.has(CARNUMBER)) {
			map.put(CARNUMBER, jsonObject.getString(CARNUMBER));
		}
		if (jsonObject.has(FANS)) {
			map.put(FANS, jsonObject.getString(FANS));
		}
		if (jsonObject.has(GENERALIZE)) {
			map.put(GENERALIZE, jsonObject.getString(GENERALIZE));
		}
		if (jsonObject.has(RANKING)) {
			map.put(RANKING, jsonObject.getString(RANKING));
		}
		if (jsonObject.has(LIST)) {
			JSONArray jsonArray = jsonObject.getJSONArray(LIST);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				map = new HashMap<String, String>();
				list.add(map);

				if (object.has(UID)) {
					map.put(UID, object.getString(UID));
				}
				if (object.has(NAME)) {
					map.put(NAME, object.getString(NAME));
				}
				if (object.has(LOGO)) {
					map.put(LOGO, object.getString(LOGO));
				}
				if (object.has(INTEGRAL)) {
					map.put(INTEGRAL, object.getString(INTEGRAL));
				}
				if (object.has(CARNUMBER)) {
					map.put(CARNUMBER, object.getString(CARNUMBER));
				}
				if (object.has(FANS)) {
					map.put(FANS, object.getString(FANS));
				}
				if (object.has(GENERALIZE)) {
					map.put(GENERALIZE, object.getString(GENERALIZE));
				}
			}
			bundle.putSerializable(LIST, list);
		}
		return bundle;
	}

	/**
	 * 解析用户信息返回来的数据
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserUserInfo(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		bundle.putString(UID, jsonObject.optString(UID));
		bundle.putString(NAME, jsonObject.optString(NAME));
		bundle.putString(GENDER, jsonObject.optString(GENDER));
		bundle.putString(AGE, jsonObject.optString(AGE));
		bundle.putString(EMOTION, jsonObject.optString(EMOTION));
		bundle.putString(SIGNATURE, jsonObject.optString(SIGNATURE));
		bundle.putString(PROFESSION, jsonObject.optString(PROFESSION));
		bundle.putString(LOGO, jsonObject.optString(LOGO));
		JSONArray picArray = jsonObject.optJSONArray(PIC);
		ArrayList<Map<String, String>> picList = new ArrayList<Map<String, String>>();
		if (picArray != null && picArray.length() > 0) {
			for (int i = 0; i < picArray.length(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				JSONObject obj = picArray.optJSONObject(i);
				if (obj != null) {
					map.put(ID, obj.optString(ID));
					map.put(USER_ID, obj.optString(USER_ID));
					map.put(LOGO, obj.optString(LOGO));
					map.put(PIC, obj.optString(PIC));
				}
				picList.add(map);
			}
		}
		bundle.putSerializable(PIC, picList);
		bundle.putString(GRADE, jsonObject.optString(GRADE));
		bundle.putString(BIRTHDAY, jsonObject.optString(BIRTHDAY));
		bundle.putString(COMMENT, jsonObject.optString(COMMENT));
		bundle.putString(NUMBER, jsonObject.optString(NUMBER));
		bundle.putString(INSURANCE, jsonObject.optString(INSURANCE));
		bundle.putString(VERIFY, jsonObject.optString(VERIFY));
		return bundle;
	}

	/**
	 * 解析车主身份登录时,返回的附近的乘客的数据.
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserOwnerList(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		bundle.putString(NUMBER, jsonObject.optString(NUMBER));
		JSONArray listArray = jsonObject.optJSONArray(LIST);
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (listArray != null && listArray.length() > 0) {
			for (int i = 0; i < listArray.length(); i++) {
				JSONObject obj = listArray.optJSONObject(i);
				Map<String, String> map = new HashMap<String, String>();
				map.put(UID, obj.optString(UID));
				map.put(LOGO, obj.optString(LOGO));
				map.put(GENDER, obj.optString(GENDER));
				map.put(NAME, obj.optString(NAME));
				map.put(DISTANCE, obj.optString(DISTANCE));
				map.put(USE_TIME, obj.optString(USE_TIME));
				map.put(LNG, obj.optString(LNG));
				map.put(LAT, obj.optString(LAT));
				map.put(ID, obj.optString(ID));
				map.put(ORIGIN, obj.optString(ORIGIN));
				map.put(TERMINAL, obj.optString(TERMINAL));
				map.put(SEAT, obj.optString(SEAT));
				map.put(ADD_TIME, obj.optString(ADD_TIME));
				list.add(map);
			}
		}
		bundle.putSerializable(LIST, list);
		return bundle;
	}

	/**
	 * 解析乘客身份登录时,返回的附近的车主的数据.
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserPassengerList(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		bundle.putString(NUMBER, jsonObject.optString(NUMBER));
		JSONArray listArray = jsonObject.optJSONArray(LIST);
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (listArray != null && listArray.length() > 0) {
			for (int i = 0; i < listArray.length(); i++) {
				JSONObject obj = listArray.optJSONObject(i);
				Map<String, String> map = new HashMap<String, String>();
				map.put(UID, obj.optString(UID));
				map.put(LOGO, obj.optString(LOGO));
				map.put(GENDER, obj.optString(GENDER));
				map.put(NAME, obj.optString(NAME));
				map.put(LNG, obj.optString(LNG));
				map.put(LAT, obj.optString(LAT));
				map.put(ID, obj.optString(ID));
				map.put(DISTANCE, obj.optString(DISTANCE));
				map.put(USE_TIME, obj.optString(USE_TIME));
				map.put(INSURANCE, obj.optString(INSURANCE));
				map.put(ORIGIN, obj.optString(ORIGIN));
				map.put(TERMINAL, obj.optString(TERMINAL));
				map.put(SEAT, obj.optString(SEAT));
				map.put(ADD_TIME, obj.optString(ADD_TIME));
				list.add(map);
			}
		}
		bundle.putSerializable(LIST, list);
		return bundle;
	}

	/**
	 * 解析附近群组
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserNearGroup(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		JSONArray listArray = jsonObject.optJSONArray(LIST);
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (listArray != null && listArray.length() > 0) {
			for (int i = 0; i < listArray.length(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				JSONObject obj = listArray.optJSONObject(i);
				if (obj != null) {
					map.put(ID, obj.optString(ID));
					map.put(LOGO, obj.optString(LOGO));
					map.put(NAME, obj.optString(NAME));
					map.put(LNG, obj.optString(LNG));
					map.put(LAT, obj.optString(LAT));
					map.put(DISTANCE, obj.optString(DISTANCE));
					map.put(USERNUMBER, obj.optString(USERNUMBER));

				}
				list.add(map);
			}
		}
		bundle.putSerializable(LIST, list);
		return bundle;
	}

	/**
	 * 解析 我的群组
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserMyGroup(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		JSONArray listArray = jsonObject.optJSONArray(LIST);
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (listArray != null && listArray.length() > 0) {
			for (int i = 0; i < listArray.length(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				JSONObject obj = listArray.optJSONObject(i);
				if (obj != null) {
					map.put(ID, obj.optString(ID));
					map.put(NAME, obj.optString(NAME));
					map.put(NUMBER, obj.optString(NUMBER));
					map.put(NOTICE, obj.optString(NOTICE));
					map.put(TYPE, obj.optString(TYPE));
					map.put(LOGO, obj.optString(LOGO));
					map.put(USERNUMBER, obj.optString(USERNUMBER));
				}
				list.add(map);
			}
		}
		bundle.putSerializable(LIST, list);
		return bundle;
	}

	/**
	 * 解析创建群返回数据
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserCreateGroup(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		return bundle;
	}

	/**
	 * 5.10我的粉丝
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Bundle parserMyFans(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}

		if (jsonObject.has(UID)) {
			bundle.putString(UID, jsonObject.getString(UID));
		}
		if (jsonObject.has(NAME)) {
			bundle.putString(NAME, jsonObject.getString(NAME));
		}
		if (jsonObject.has(LOGO)) {
			bundle.putString(LOGO, jsonObject.getString(LOGO));
		}
		if (jsonObject.has(INTEGRAL)) {
			bundle.putString(INTEGRAL, jsonObject.getString(INTEGRAL));
		}
		if (jsonObject.has(CARNUMBER)) {
			bundle.putString(CARNUMBER, jsonObject.getString(CARNUMBER));
		}
		if (jsonObject.has(FANS)) {
			bundle.putString(FANS, jsonObject.getString(FANS));
		}
		if (jsonObject.has(GENERALIZE)) {
			bundle.putString(GENERALIZE, jsonObject.getString(GENERALIZE));
		}
		if (jsonObject.has(RANKING)) {
			bundle.putString(RANKING, jsonObject.getString(RANKING));
		}
		if (jsonObject.has(LIST)) {
			JSONArray jsonArray = jsonObject.getJSONArray(LIST);
			ArrayList<HashMap> list = new ArrayList<HashMap>();
			HashMap map = null;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				map = new HashMap();
				list.add(map);

				if (object.has(UID)) {
					map.put(UID, object.getString(UID));
				}
				if (object.has("car")) {
					map.put("car", object.getString("car"));
				}
				if (object.has("exponent")) {
					map.put("exponent", object.getString("exponent"));
				}
				if (object.has(INTEGRAL)) {
					map.put(INTEGRAL, object.getString(INTEGRAL));
				}

				ArrayList<HashMap<String, String>> userList = new ArrayList<HashMap<String, String>>();
				map.put("user", userList);

				JSONArray userArray = jsonObject.getJSONArray("user");
				for (int j = 0; j < userArray.length(); j++) {
					JSONObject userObject = jsonArray.getJSONObject(j);
					HashMap<String, String> map1 = new HashMap<String, String>();
					userList.add(map1);

					if (userObject.has(ID)) {
						map1.put(ID, userObject.getString(ID));
					}
					if (userObject.has(LOGO)) {
						map1.put(LOGO, userObject.getString(LOGO));
					}
				}
			}
			bundle.putSerializable(LIST, list);
		}
		return bundle;
	}

	/**
	 * 解析车主摇一摇接口
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserOwnerBeginGame(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (jsonObject != null) {
			if (jsonObject.has(STATE)) {
				int state = jsonObject.optInt(STATE);
				bundle.putInt(STATE, state);
			}
			if (jsonObject.has(MESSAGE)) {
				bundle.putString(MESSAGE, jsonObject.optString(MESSAGE));
			}
		}
		return bundle;
	}

	/**
	 * 解析乘客摇一摇接口
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserPassengerBeginGame(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (jsonObject != null) {
			if (jsonObject.has(STATE)) {
				int state = jsonObject.optInt(STATE);
				bundle.putInt(STATE, state);
			}
			if (jsonObject.has(MESSAGE)) {
				bundle.putString(MESSAGE, jsonObject.optString(MESSAGE));
			}
		}
		return bundle;
	}

	/**
	 * 解析游戏结果
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserGameResult(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (jsonObject != null) {
			if (jsonObject.has(STATE)) {
				int state = jsonObject.optInt(STATE);
				bundle.putInt(STATE, state);
			}
			if (jsonObject.has(MESSAGE)) {
				bundle.putString(MESSAGE, jsonObject.optString(MESSAGE));
			}
		}
		return bundle;
	}

	/**
	 * 解析推荐群组
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserRecommendGroup(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		JSONArray listArray = jsonObject.optJSONArray(LIST);
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (listArray != null && listArray.length() > 0) {
			for (int i = 0; i < listArray.length(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				JSONObject obj = listArray.optJSONObject(i);
				if (obj != null) {
					map.put(ID, obj.optString(ID));
					map.put(LOGO, obj.optString(LOGO));
					map.put(NAME, obj.optString(NAME));
					map.put(LNG, obj.optString(LNG));
					map.put(LAT, obj.optString(LAT));
					map.put(DISTANCE, obj.optString(DISTANCE));
					map.put(NOTICE, obj.optString(NOTICE));
					map.put(USERNUMBER, obj.optString(USERNUMBER));
				}
				list.add(map);
			}
		}
		bundle.putSerializable(LIST, list);
		return bundle;
	}

	/**
	 * 解析群详情
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserGroupDetail(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		if (jsonObject.has(ID)) {
			String id = jsonObject.getString(ID);
			bundle.putString(ID, id);
		}
		if (jsonObject.has(NAME)) {
			String name = jsonObject.getString(NAME);
			bundle.putString(NAME, name);
		}

		if (jsonObject.has(NUMBER)) {
			String number = jsonObject.getString(NUMBER);
			bundle.putString(NUMBER, number);
		}
		if (jsonObject.has(NOTICE)) {
			String notice = jsonObject.getString(NOTICE);
			bundle.putString(NOTICE, notice);
		}
		if (jsonObject.has(PLACE)) {
			String place = jsonObject.getString(PLACE);
			bundle.putString(PLACE, place);
		}
		if (jsonObject.has(INTRODUCE)) {
			String introduce = jsonObject.getString(INTRODUCE);
			bundle.putString(INTRODUCE, introduce);
		}
		if (jsonObject.has(ADD_TIME)) {
			String add_time = jsonObject.getString(ADD_TIME);
			bundle.putString(ADD_TIME, add_time);
		}
		if (jsonObject.has(LOGO)) {
			String logo = jsonObject.getString(LOGO);
			bundle.putString(LOGO, logo);
		}
		if (jsonObject.has(USERNUMBER)) {
			String userNumber = jsonObject.getString(USERNUMBER);
			bundle.putString(USERNUMBER, userNumber);
		}
		if (jsonObject.has(JOIN)) {
			String join = jsonObject.getString(JOIN);
			bundle.putString(JOIN, join);
		}
		if (jsonObject.has(MAINID)) {
			String mainId = jsonObject.getString(MAINID);
			bundle.putString(MAINID, mainId);
		}
		if (jsonObject.has(MAINNAME)) {
			String mainName = jsonObject.getString(MAINNAME);
			bundle.putString(MAINNAME, mainName);
		}
		if (jsonObject.has(MAINlOGO)) {
			String mainLogo = jsonObject.getString(MAINlOGO);
			bundle.putString(MAINlOGO, mainLogo);
		}
		if (jsonObject.has(LIVENESS)) {
			String liveness = jsonObject.getString(LIVENESS);
			bundle.putString(LIVENESS, liveness);
		}

		JSONArray listArray = jsonObject.optJSONArray(GROUPUSER);
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (listArray != null && listArray.length() > 0) {
			for (int i = 0; i < listArray.length(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				JSONObject obj = listArray.optJSONObject(i);
				if (obj != null) {
					map.put(ID, obj.optString(ID));
					map.put(LOGO, obj.optString(LOGO));
					map.put(NAME, obj.optString(NAME));
				}
				list.add(map);
			}
		}
		bundle.putSerializable(GROUPUSER, list);
		return bundle;
	}

	/**
	 * 解析搜索群
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserSearchGroup(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		JSONArray listArray = jsonObject.optJSONArray(LIST);
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (listArray != null && listArray.length() > 0) {
			for (int i = 0; i < listArray.length(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				JSONObject obj = listArray.optJSONObject(i);
				if (obj != null) {
					map.put(ID, obj.optString(ID));
					map.put(LOGO, obj.optString(LOGO));
					map.put(NAME, obj.optString(NAME));
					map.put(LNG, obj.optString(LNG));
					map.put(LAT, obj.optString(LAT));
					map.put(DISTANCE, obj.optString(DISTANCE));
					map.put(USERNUMBER, obj.optString(USERNUMBER));
					map.put(NOTICE, obj.optString(NOTICE));
				}
				list.add(map);
			}
		}
		bundle.putSerializable(LIST, list);
		return bundle;
	}

	/**
	 * 拼车路线
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserLineList(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		JSONArray listArray = jsonObject.optJSONArray(LIST);
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (listArray != null && listArray.length() > 0) {
			for (int i = 0; i < listArray.length(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject obj = listArray.optJSONObject(i);
				if (obj != null) {
					map.put(ID, obj.optString(ID));
					map.put(MONEY, obj.optString(MONEY));
					map.put(ORIGIN, obj.optString(ORIGIN));
					map.put(SEAT, obj.optString(SEAT));
					map.put(ADD_TIME, obj.optString(ADD_TIME));
					map.put(TERMINAL, obj.optString(TERMINAL));
					ArrayList<HashMap<String, String>> way_list = new ArrayList<HashMap<String, String>>();
					JSONArray way_array = obj.getJSONArray(WAY);
					for (int j = 0; j < way_array.length(); j++) {
						JSONObject way_object = way_array.getJSONObject(j);
						HashMap<String, String> way_map = new HashMap<String, String>();
						if (way_object.has(ID)) {
							String id = way_object.getString(ID);
							way_map.put(ID, id);
						}
						if (way_object.has(ORDER_ID)) {
							String order_id = way_object.getString(ORDER_ID);
							way_map.put(ORDER_ID, order_id);
						}
						if (way_object.has(NAME)) {
							String name = way_object.getString(NAME);
							way_map.put(NAME, name);
						}
						if (way_object.has(LAT)) {
							String lat = way_object.getString(LAT);
							way_map.put(LAT, lat);
						}
						if (way_object.has(LNG)) {
							String lng = way_object.getString(LNG);
							way_map.put(LNG, lng);
						}
						way_list.add(way_map);
					}
					map.put("way", way_list);
				}
				list.add(map);
			}
		}
		bundle.putSerializable(LIST, list);
		return bundle;
	}

	/**
	 * 解析加入群组
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserJoinGroup(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		if (jsonObject.has(ID)) {
			String id = jsonObject.getString(ID);
			bundle.putString(ID, id);
		}
		return bundle;
	}

	/**
	 * 解析搜索群
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserMessage(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		JSONArray listArray = jsonObject.optJSONArray(LIST);
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (listArray != null && listArray.length() > 0) {
			for (int i = 0; i < listArray.length(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				JSONObject obj = listArray.optJSONObject(i);
				if (obj != null) {
					map.put(ID, obj.optString(ID));
					map.put(CONTENT, obj.optString(CONTENT));
					map.put(TYPE, obj.optString(TYPE));
					map.put(INFO_ID, obj.optString(INFO_ID));
					map.put(GID, obj.optString(GID));

				}
				list.add(map);
			}
		}
		bundle.putSerializable(LIST, list);
		return bundle;
	}

	/**
	 * 解析清空通知
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserMessageDel(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		return bundle;
	}

	/**
	 * 解析审核通过
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserAudit(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		return bundle;
	}

	/**
	 * 3.14 乘客_支付成功后调用的接口
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle paySuccess(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		if (jsonObject.has(USER_ID)) {
			bundle.putString(USER_ID, jsonObject.getString(USER_ID));
		}
		if (jsonObject.has(USER_NAME)) {
			bundle.putString(USER_NAME, jsonObject.getString(USER_NAME));
		}
		if (jsonObject.has(USER_LOGO)) {
			bundle.putString(USER_LOGO, jsonObject.getString(USER_LOGO));
		}
		if (jsonObject.has(RANK)) {
			bundle.putString(RANK, jsonObject.getString(RANK));
		}
		if (jsonObject.has(COMMENT_RANK)) {
			bundle.putString(COMMENT_RANK, jsonObject.getString(COMMENT_RANK));
		}
		if (jsonObject.has(INSURANCE)) {
			bundle.putString(INSURANCE, jsonObject.getString(INSURANCE));
		}
		if (jsonObject.has(BRAND)) {
			bundle.putString(BRAND, jsonObject.getString(BRAND));
		}
		if (jsonObject.has(COLOR)) {
			bundle.putString(COLOR, jsonObject.getString(COLOR));
		}
		if (jsonObject.has(SUCCESS_NUMBER)) {
			bundle.putString(SUCCESS_NUMBER, jsonObject.getString(SUCCESS_NUMBER));
		}
		if (jsonObject.has(INFO_ID)) {
			bundle.putString(INFO_ID, jsonObject.getString(INFO_ID));
		}
		if (jsonObject.has(ORDER_ID)) {
			bundle.putString(ORDER_ID, jsonObject.getString(ORDER_ID));
		}
		return bundle;
	}

	/**
	 * 解析审核通过
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parseriInfoDel(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		return bundle;
	}

	/**
	 * 解析获取证件图片url
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserGetLicence(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		bundle.putString(ONE, jsonObject.optString(ONE));
		bundle.putString(TWO, jsonObject.optString(TWO));
		return bundle;
	}

	/**
	 * 5.28分摊记录
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle parserApportion(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		Bundle bundle = new Bundle();
		if (!isResultSuccess(bundle, jsonObject)) {
			return bundle;
		}
		if (jsonObject.has(LIST)) {
			JSONArray jsonArray = jsonObject.getJSONArray(LIST);

			ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map = null;

			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject voucher = jsonArray.getJSONObject(i);
				map = new HashMap<String, String>();
				list.add(map);

				if (voucher.has(ID)) {
					map.put(ID, voucher.getString(ID));
				}
				if (voucher.has(NUMBER)) {
					map.put(NUMBER, voucher.getString(NUMBER));
				}
				if (voucher.has(MONEY)) {
					map.put(MONEY, voucher.getString(MONEY));
				}
				if (voucher.has(ADD_TIME)) {
					map.put(ADD_TIME, voucher.getString(ADD_TIME));
				}
			}
			bundle.putSerializable(LIST, list);
		}
		return bundle;
	}

	/**
	 * 累计页面接口
	 */
	public static Bundle addUpAccount(String json) {
		JSONObject jsonObject;

		Bundle bundle = null;
		try {
			jsonObject = new JSONObject(json);
			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}
			if (jsonObject.has("revenue_sum")) {
				bundle.putString("revenue_sum", jsonObject.getString("revenue_sum"));
			}
			if (jsonObject.has("pay_sum")) {
				bundle.putString("pay_sum", jsonObject.getString("pay_sum"));
			}
			if (jsonObject.has("withdraw_sum")) {
				bundle.putString("withdraw_sum", jsonObject.getString("withdraw_sum"));
			}
			if (jsonObject.has("balance")) {
				bundle.putString("balance", jsonObject.getString("balance"));
			}

			if (jsonObject.has("refund_sum")) {
				bundle.putString("refund_sum", jsonObject.getString("refund_sum"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return bundle;
	}

	/**
	 * 累计的详细界面
	 */
	public static Bundle addUpDetailedJSON(String json) {
		Bundle bundle = null;
		try {
			JSONObject jsonObject;

			jsonObject = new JSONObject(json);

			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}
			if (jsonObject.has("list")) {
				JSONArray jsonArray = jsonObject.getJSONArray("list");
				ArrayList<Map<String, String>> gridviewList = new ArrayList<Map<String, String>>();
				for (int i = 0; i < jsonArray.length(); i++) {
					HashMap map = new HashMap();

					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);

					if (jsonObject2.has("time")) {
						map.put("time", jsonObject2.getString("time"));
					}

					if (jsonObject2.has("money")) {
						map.put("money", jsonObject2.getString("money"));
					}
					gridviewList.add(map);
				}
				bundle.putSerializable("list", gridviewList);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	/**
	 * 车主或者乘客周边查询接口
	 */
	public static Bundle PeripheryJSON(String json) {
		Bundle bundle = null;
		try {
			JSONObject jsonObject;

			jsonObject = new JSONObject(json);

			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}
			if (jsonObject.has("list")) {
				JSONArray jsonArray = jsonObject.getJSONArray("list");
				ArrayList<Map<String, String>> gridviewList = new ArrayList<Map<String, String>>();
				for (int i = 0; i < jsonArray.length(); i++) {
					HashMap map = new HashMap();
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
					if (jsonObject2.has("order_id")) {
						map.put("order_id", jsonObject2.getString("order_id"));
					}

					if (jsonObject2.has("user_id")) {
						map.put("user_id", jsonObject2.getString("user_id"));
					}

					if (jsonObject2.has("start_loc")) {
						map.put("start_loc", jsonObject2.getString("start_loc"));
					}

					if (jsonObject2.has("stop_loc")) {
						map.put("stop_loc", jsonObject2.getString("stop_loc"));
					}

					if (jsonObject2.has("start_lng")) {
						map.put("start_lng", jsonObject2.getString("start_lng"));
					}
					if (jsonObject2.has("start_lat")) {
						map.put("start_lat", jsonObject2.getString("start_lat"));
					}
					if (jsonObject2.has("setoff_time")) {
						map.put("setoff_time", jsonObject2.getString("setoff_time"));
					}

					if (jsonObject2.has("detail")) {
						map.put("detail", jsonObject2.getString("detail"));
					}
					gridviewList.add(map);
				}
				bundle.putSerializable("list", gridviewList);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	/**
	 * 车主或者乘客发布路线列表解析
	 */
	public static Bundle MyRouteJSON(String json) {
		Bundle bundle = null;
		try {
			JSONObject jsonObject;

			jsonObject = new JSONObject(json);

			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}
			if (jsonObject.has("orderout_list")) {
				JSONArray jsonArray = jsonObject.getJSONArray("orderout_list");
				ArrayList<Map<String, String>> gridviewList = new ArrayList<Map<String, String>>();
				for (int i = 0; i < jsonArray.length(); i++) {
					HashMap map = new HashMap();
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
					if (jsonObject2.has("distance")) {
						map.put("distance", jsonObject2.getString("distance"));
					}
					if (jsonObject2.has("order_id")) {
						map.put("order_id", jsonObject2.getString("order_id"));
					}

					if (jsonObject2.has("price")) {
						map.put("price", jsonObject2.getString("price"));
					}

					if (jsonObject2.has("setoff_time")) {
						map.put("setoff_time", jsonObject2.getString("setoff_time"));
					}

					if (jsonObject2.has("start_loc")) {
						map.put("start_loc", jsonObject2.getString("start_loc"));
					}
					if (jsonObject2.has("stop_loc")) {
						map.put("stop_loc", jsonObject2.getString("stop_loc"));
					}
					if (jsonObject2.has("user_id")) {
						map.put("user_id", jsonObject2.getString("user_id"));
					}
					if (jsonObject2.has("push_times")) {
						map.put("push_times", jsonObject2.getString("push_times"));
					}

					if (jsonObject2.has("des_name")) {
						map.put("des_name", jsonObject2.getString("des_name"));
					}
					if (jsonObject2.has("des_car_id")) {
						map.put("des_car_id", jsonObject2.getString("des_car_id"));
					}
					gridviewList.add(map);
				}
				bundle.putSerializable("list", gridviewList);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	/**
	 * 车主或者乘客我的消息 ls
	 */
	public static Bundle MyNewsJSON(String json) {
		Bundle bundle = null;
		try {
			JSONObject jsonObject;

			jsonObject = new JSONObject(json);

			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}

			if (jsonObject.has("order_list")) {
				JSONArray jsonArray = jsonObject.getJSONArray("order_list");
				ArrayList<Map<String, String>> gridviewList = new ArrayList<Map<String, String>>();
				for (int i = 0; i < jsonArray.length(); i++) {
					HashMap map = new HashMap();
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);

					if (jsonObject2.has("age")) {

						map.put("age", jsonObject2.getString("age"));
					}
					if (jsonObject2.has("distance")) {
						map.put("distance", jsonObject2.getString("distance"));
					}
					if (jsonObject2.has("gender")) {
						map.put("gender", jsonObject2.getString("gender"));
					}
					if (jsonObject2.has("logo")) {
						map.put("logo", jsonObject2.getString("logo"));
					}
					if (jsonObject2.has("name")) {
						map.put("name", jsonObject2.getString("name"));
					}
					if (jsonObject2.has("order_id")) {
						map.put("order_id", jsonObject2.getString("order_id"));
					}
					if (jsonObject2.has("price")) {
						map.put("price", jsonObject2.getString("price"));
					}
					if (jsonObject2.has("setoff_time")) {
						map.put("setoff_time", jsonObject2.getString("setoff_time"));
					}

					if (jsonObject2.has("driver_order_id")) {
						map.put("driver_order_id", jsonObject2.getString("driver_order_id"));
					}
					if (jsonObject2.has("driver_start_lat")) {
						map.put("driver_start_lat", jsonObject2.getString("driver_start_lat"));
					}
					if (jsonObject2.has("licence_sn")) {
						map.put("licence_sn", jsonObject2.getString("licence_sn"));
					}
					if (jsonObject2.has("car_name")) {
						map.put("car_name", jsonObject2.getString("car_name"));
					}
					if (jsonObject2.has("brand_id")) {
						map.put("brand_id", jsonObject2.getString("brand_id"));
					}

					if (jsonObject2.has("start_loc")) {
						map.put("start_loc", jsonObject2.getString("start_loc"));
					}
					if (jsonObject2.has("stop_loc")) {
						map.put("stop_loc", jsonObject2.getString("stop_loc"));
					}
					if (jsonObject2.has("driver_start_lng")) {
						map.put("driver_start_lng", jsonObject2.getString("driver_start_lng"));
					}
					if (jsonObject2.has("driver_start_loc")) {
						map.put("driver_start_loc", jsonObject2.getString("driver_start_loc"));
					}

					if (jsonObject2.has("driver_stop_loc")) {
						map.put("driver_stop_loc", jsonObject2.getString("driver_stop_loc"));
					}
					if (jsonObject2.has("passenger_order_id")) {
						map.put("passenger_order_id", jsonObject2.getString("passenger_order_id"));
					}

					if (jsonObject2.has("passenger_start_lat")) {
						map.put("passenger_start_lat", jsonObject2.getString("passenger_start_lat"));
					}
					if (jsonObject2.has("passenger_start_lng")) {
						map.put("passenger_start_lng", jsonObject2.getString("passenger_start_lng"));
					}

					if (jsonObject2.has("end_lat")) {
						map.put("end_lat", jsonObject2.getString("end_lat"));
					}
					if (jsonObject2.has("end_lng")) {
						map.put("end_lng", jsonObject2.getString("end_lng"));
					}
					if (jsonObject2.has("msg_id")) {
						map.put("msg_id", jsonObject2.getString("msg_id"));
					}

					if (jsonObject2.has("car_name")) {
						map.put("car_name", jsonObject2.getString("car_name"));
					}

					if (jsonObject2.has("licence_sn")) {
						map.put("licence_sn", jsonObject2.getString("licence_sn"));
					}
					gridviewList.add(map);
				}
				bundle.putSerializable("list", gridviewList);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	/**
	 * 车主或者乘客正在进行
	 */
	public static Bundle ConductJSON(String json) {
		Bundle bundle = null;
		try {
			JSONObject jsonObject;

			jsonObject = new JSONObject(json);

			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}

			if (jsonObject.has("deal_list")) {
				JSONArray jsonArray = jsonObject.getJSONArray("deal_list");
				ArrayList<Map<String, String>> gridviewList = new ArrayList<Map<String, String>>();
				for (int i = 0; i < jsonArray.length(); i++) {
					HashMap map = new HashMap();
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);

					if (jsonObject2.has("insurance_status")) {

						map.put("insurance_status", jsonObject2.getString("insurance_status"));
					}
					if (jsonObject2.has("commented")) {

						map.put("commented", jsonObject2.getString("commented"));
					}

					if (jsonObject2.has("car_name")) {

						map.put("car_name", jsonObject2.getString("car_name"));
					}
					if (jsonObject2.has("car_logo")) {

						map.put("car_logo", jsonObject2.getString("car_logo"));
					}

					if (jsonObject2.has("brand_id")) {

						map.put("brand_id", jsonObject2.getString("brand_id"));
					}
					if (jsonObject2.has("deal_id")) {

						map.put("deal_id", jsonObject2.getString("deal_id"));
					}
					if (jsonObject2.has("deal_finish")) {

						map.put("deal_finish", jsonObject2.getString("deal_finish"));
					}

					if (jsonObject2.has("driver_order_id")) {

						map.put("driver_order_id", jsonObject2.getString("driver_order_id"));
					}

					if (jsonObject2.has("driver_start_lat")) {

						map.put("driver_start_lat", jsonObject2.getString("driver_start_lat"));
					}

					if (jsonObject2.has("driver_start_lng")) {

						map.put("driver_start_lng", jsonObject2.getString("driver_start_lng"));
					}

					if (jsonObject2.has("age")) {

						map.put("age", jsonObject2.getString("age"));
					}

					if (jsonObject2.has("distance")) {

						map.put("distance", jsonObject2.getString("distance"));
					}
					if (jsonObject2.has("gender")) {

						map.put("gender", jsonObject2.getString("gender"));
					}
					if (jsonObject2.has("logo")) {

						map.put("logo", jsonObject2.getString("logo"));
					}
					if (jsonObject2.has("name")) {

						map.put("name", jsonObject2.getString("name"));
					}
					if (jsonObject2.has("passenger_order_id")) {

						map.put("passenger_order_id", jsonObject2.getString("passenger_order_id"));
					}
					if (jsonObject2.has("phone")) {

						map.put("phone", jsonObject2.getString("phone"));
					}

					if (jsonObject2.has("price")) {

						map.put("price", jsonObject2.getString("price"));
					}

					if (jsonObject2.has("setoff_time")) {

						map.put("setoff_time", jsonObject2.getString("setoff_time"));
					}
					if (jsonObject2.has("passenger_start_lat")) {

						map.put("passenger_start_lat", jsonObject2.getString("passenger_start_lat"));
					}
					if (jsonObject2.has("passenger_start_lng")) {

						map.put("passenger_start_lng", jsonObject2.getString("passenger_start_lng"));
					}

					if (jsonObject2.has("start_loc")) {

						map.put("start_loc", jsonObject2.getString("start_loc"));
					}
					if (jsonObject2.has("stop_loc")) {

						map.put("stop_loc", jsonObject2.getString("stop_loc"));
					}

					if (jsonObject2.has("driver_user_id")) {

						map.put("driver_user_id", jsonObject2.getString("driver_user_id"));
					}
					if (jsonObject2.has("passenger_user_id")) {

						map.put("passenger_user_id", jsonObject2.getString("passenger_user_id"));
					}

					if (jsonObject2.has("licence_sn")) {

						map.put("licence_sn", jsonObject2.getString("licence_sn"));
					}

					if (jsonObject2.has("car_name")) {

						map.put("car_name", jsonObject2.getString("car_name"));
					}
					gridviewList.add(map);
				}
				bundle.putSerializable("list", gridviewList);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	/**
	 * 我的消息接受解析
	 */
	public static Bundle takeJSON(String json) {
		JSONObject jsonObject;

		Bundle bundle = null;
		try {
			jsonObject = new JSONObject(json);
			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}
			if (jsonObject.has("result")) {
				bundle.putString("result", jsonObject.getString("result"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return bundle;
	}

	/**
	 * 正在进行职工取消订单
	 */

	public static Bundle CancelJSON(String json) {
		JSONObject jsonObject;
		Bundle bundle = null;
		try {
			jsonObject = new JSONObject(json);
			bundle = new Bundle();

			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bundle;

	}

	/**
	 * 推送单个信息解析
	 */

	public static Bundle JGlJSON(String json) {
		Bundle bundle = null;
		try {
			bundle = new Bundle();
			if (json == null) {

				return bundle = null;
			}

			JSONObject jsonObject3 = new JSONObject(json);
			JSONObject jsonObject2 = null;
			if (jsonObject3.has("msg")) {
				jsonObject2 = new JSONObject(jsonObject3.getString("msg"));
			}
			if (jsonObject2.has("poid")) {
				bundle.putString("poid", jsonObject2.getString("poid"));
			}

			if (jsonObject2.has("duid")) {
				bundle.putString("duid", jsonObject2.getString("duid"));
			}
			if (jsonObject2.has("t")) {
				bundle.putString("t", jsonObject2.getString("t"));
			}
			if (jsonObject2.has("puid")) {
				bundle.putString("puid", jsonObject2.getString("puid"));
			}
			if (jsonObject2.has("doid")) {
				bundle.putString("doid", jsonObject2.getString("doid"));
			}
			if (jsonObject2.has("msgid")) {
				bundle.putString("msgid", jsonObject2.getString("msgid"));
			}
			if (jsonObject2.has("deid")) {
				bundle.putString("deid", jsonObject2.getString("deid"));
			}
			if (jsonObject2.has("role")) {
				bundle.putString("role", jsonObject2.getString("role"));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bundle;

	}

	/**
	 * 推送单个信息返回值解析
	 */

	public static Bundle JGPersonallJSON(String json) {
		Bundle bundle = null;
		try {
			bundle = new Bundle();
			if (json == null) {

				return bundle = null;
			}

			JSONObject jsonObject2 = new JSONObject(json);

			if (jsonObject2.has("start_loc")) {
				bundle.putString("start_loc", jsonObject2.getString("start_loc"));
			}

			if (jsonObject2.has("insurance_status")) {
				bundle.putString("insurance_status", jsonObject2.getString("insurance_status"));
			}
			if (jsonObject2.has("stop_loc")) {
				bundle.putString("stop_loc", jsonObject2.getString("stop_loc"));
			}
			if (jsonObject2.has("name")) {
				bundle.putString("name", jsonObject2.getString("name"));
			}
			if (jsonObject2.has("gender")) {
				bundle.putString("gender", jsonObject2.getString("gender"));
			}
			if (jsonObject2.has("age")) {
				bundle.putString("age", jsonObject2.getString("age"));
			}

			if (jsonObject2.has("price")) {
				bundle.putString("price", jsonObject2.getString("price"));
			}
			if (jsonObject2.has("distance")) {
				bundle.putString("distance", jsonObject2.getString("distance"));
			}
			if (jsonObject2.has("logo")) {
				bundle.putString("logo", jsonObject2.getString("logo"));
			}
			if (jsonObject2.has("pinche_sum")) {
				bundle.putString("pinche_sum", jsonObject2.getString("pinche_sum"));
			}

			if (jsonObject2.has("haopin_sum")) {
				bundle.putString("haopin_sum", jsonObject2.getString("haopin_sum"));
			}

			if (jsonObject2.has("start_lng")) {
				bundle.putString("start_lng", jsonObject2.getString("start_lng"));
			}
			if (jsonObject2.has("start_lat")) {
				bundle.putString("start_lat", jsonObject2.getString("start_lat"));
			}
			if (jsonObject2.has("end_lng")) {
				bundle.putString("end_lng", jsonObject2.getString("end_lng"));
			}
			if (jsonObject2.has("end_lat")) {
				bundle.putString("end_lat", jsonObject2.getString("end_lat"));
			}
			if (jsonObject2.has("setoff_time")) {
				bundle.putString("setoff_time", jsonObject2.getString("setoff_time"));
			}
			if (jsonObject2.has("drv_exp")) {
				bundle.putString("drv_exp", jsonObject2.getString("drv_exp"));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bundle;

	}

	/**
	 * 游戏启动解析
	 */

	public static Bundle GameStartJSON(String json) {
		Bundle bundle = null;
		JSONObject jsonObject = null;

		try {

			jsonObject = new JSONObject(json);

			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}

			if (jsonObject.has("gesture")) {
				bundle.putString("gesture", jsonObject.getString("gesture"));
			}
			if (jsonObject.has("game_id")) {
				bundle.putString("game_id", jsonObject.getString("game_id"));
			}

			if (jsonObject.has("status")) {
				bundle.putString("status", jsonObject.getString("status"));
			}
			if (jsonObject.has("driver_gesture")) {
				bundle.putString("driver_gesture", jsonObject.getString("driver_gesture"));
			}
			if (jsonObject.has("passenger_gesture")) {
				bundle.putString("passenger_gesture", jsonObject.getString("passenger_gesture"));
			}
			if (jsonObject.has("who_win")) {
				bundle.putString("who_win", jsonObject.getString("who_win"));
			}

			if (jsonObject.has("text")) {
				bundle.putString("text", jsonObject.getString("text"));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bundle;

	}

	/**
	 * 乘客获取支付列表--lgs
	 */
	public static Bundle PayListJSON(String json) {
		Bundle bundle = null;
		try {
			bundle = new Bundle();
			if (json == null) {

				return bundle = null;
			}
			JSONObject jsonObject2 = new JSONObject(json);
			if (jsonObject2.has("price")) {
				bundle.putString("price", jsonObject2.getString("price"));
			}

			if (jsonObject2.has("distance")) {
				bundle.putString("distance", jsonObject2.getString("distance"));
			}
			if (jsonObject2.has("passenger_user_id")) {
				bundle.putString("passenger_user_id", jsonObject2.getString("passenger_user_id"));
			}
			if (jsonObject2.has("driver_user_id")) {
				bundle.putString("driver_user_id", jsonObject2.getString("driver_user_id"));
			}
			if (jsonObject2.has("passenger_order_id")) {
				bundle.putString("passenger_order_id", jsonObject2.getString("passenger_order_id"));
			}
			if (jsonObject2.has("driver_order_id")) {
				bundle.putString("driver_order_id", jsonObject2.getString("driver_order_id"));
			}
			if (jsonObject2.has("deal_id")) {
				bundle.putString("deal_id", jsonObject2.getString("deal_id"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return bundle;

	}

	/**
	 * 个人信息解析
	 */
	public static Bundle informationJSON(String json) {
		Bundle bundle = null;
		try {
			JSONObject jsonObject;

			jsonObject = new JSONObject(json);

			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}

			if (jsonObject.has("age")) {
				bundle.putString("age", jsonObject.getString("age"));
			}
			// if (jsonObject.has("car_info")) {
			// bundle.putString("car_info", jsonObject.getString("car_info"));
			// }
			if (jsonObject.has("comments")) {
				bundle.putString("comments", jsonObject.getString("comments"));
			}

			if (jsonObject.has("drv_exp")) {
				bundle.putString("drv_exp", jsonObject.getString("drv_exp"));
			}
			if (jsonObject.has("emotion")) {
				bundle.putString("emotion", jsonObject.getString("emotion"));
			}
			if (jsonObject.has("gender")) {
				bundle.putString("gender", jsonObject.getString("gender"));
			}
			if (jsonObject.has("logo")) {
				bundle.putString("logo", jsonObject.getString("logo"));
			}
			if (jsonObject.has("name")) {
				bundle.putString("name", jsonObject.getString("name"));
			}

			if (jsonObject.has("pic")) {
				JSONArray jsonArray = jsonObject.getJSONArray("pic");
				ArrayList<Map<String, String>> gridviewList = new ArrayList<Map<String, String>>();
				for (int i = 0; i < jsonArray.length(); i++) {
					HashMap map = new HashMap();
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);

					if (jsonObject2.has("user_id")) {

						map.put("user_id", jsonObject2.getString("user_id"));
					}
					if (jsonObject2.has("id")) {

						map.put("id", jsonObject2.getString("id"));
					}
					if (jsonObject2.has("logo")) {

						map.put("logo", jsonObject2.getString("logo"));
					}

					if (jsonObject2.has("pic")) {

						map.put("pic", jsonObject2.getString("pic"));
					}

					gridviewList.add(map);
				}
				bundle.putSerializable("listpic", gridviewList);
			}

			if (jsonObject.has("tags")) {
				JSONArray jsonArray = jsonObject.getJSONArray("tags");
				ArrayList<Map<String, String>> gridviewList = new ArrayList<Map<String, String>>();
				for (int i = 0; i < jsonArray.length(); i++) {
					HashMap map = new HashMap();
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);

					if (jsonObject2.has("tag_id")) {

						map.put("tag_id", jsonObject2.getString("tag_id"));
					}
					if (jsonObject2.has("tag_name")) {

						map.put("tag_name", jsonObject2.getString("tag_name"));
					}
					if (jsonObject2.has("times")) {

						map.put("times", jsonObject2.getString("times"));
					}

					gridviewList.add(map);
				}
				bundle.putSerializable("listtags", gridviewList);
			}

			if (jsonObject.has("pinche_sum")) {
				bundle.putString("pinche_sum", jsonObject.getString("pinche_sum"));
			}

			if (jsonObject.has("haopin_sum")) {
				bundle.putString("haopin_sum", jsonObject.getString("haopin_sum"));
			}
			if (jsonObject.has("profession")) {
				bundle.putString("profession", jsonObject.getString("profession"));
			}
			if (jsonObject.has("signature")) {
				bundle.putString("signature", jsonObject.getString("signature"));
			}

			if (jsonObject.has("car_info")) {
				try {
					String car_info = jsonObject.getString("car_info").toString();
					JSONObject jsonObject2 = new JSONObject(car_info);
					if (jsonObject2.has("name")) {
						bundle.putString("carname", jsonObject2.getString("name"));
					}

					if (jsonObject2.has("logo")) {
						bundle.putString("carlogo", jsonObject2.getString("logo"));
					}
				} catch (Exception e) {
					bundle.putString("carlogo", "");
					bundle.putString("carname", "");
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	/**
	 * 个人评价
	 */
	public static Bundle evaluationJSON(String json) {
		Bundle bundle = null;
		try {
			JSONObject jsonObject;

			jsonObject = new JSONObject(json);

			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}

			if (jsonObject.has("comment_list")) {
				JSONArray jsonArray = jsonObject.getJSONArray("comment_list");
				ArrayList<Map<String, String>> gridviewList = new ArrayList<Map<String, String>>();
				for (int i = 0; i < jsonArray.length(); i++) {
					HashMap map = new HashMap();
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);

					if (jsonObject2.has("add_time")) {

						map.put("add_time", jsonObject2.getString("add_time"));
					}
					if (jsonObject2.has("content")) {

						map.put("content", jsonObject2.getString("content"));
					}
					if (jsonObject2.has("user_id")) {

						map.put("user_id", jsonObject2.getString("user_id"));
					}
					if (jsonObject2.has("user_logo")) {

						map.put("user_logo", jsonObject2.getString("user_logo"));
					}
					if (jsonObject2.has("rank")) {

						map.put("rank", jsonObject2.getString("rank"));
					}

					if (jsonObject2.has("name")) {

						map.put("name", jsonObject2.getString("name"));
					}
					gridviewList.add(map);
				}
				bundle.putSerializable("list", gridviewList);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	/**
	 * 我的优惠券解析，可以解析bundle中的state状态
	 */
	public static Bundle couponJSON(String json) {
		JSONObject jsonObject;
		Bundle bundle = null;
		try {
			jsonObject = new JSONObject(json);
			bundle = new Bundle();

			if (!isResultSuccess(bundle, jsonObject)) {// 当状态为0，返回一个代状态和错误信息的bundle
				return bundle;
			}
			if (jsonObject.has("list")) {
				JSONArray jsonArray = jsonObject.getJSONArray("list");
				ArrayList<Map<String, String>> couponList = new ArrayList<Map<String, String>>();
				for (int i = 0; i < jsonArray.length(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);

					if (jsonObject2.has("finish")) {
						map.put("finish", jsonObject2.getString("finish"));
					}

					if (jsonObject2.has("money")) {
						map.put("money", jsonObject2.getString("money"));
					}
					if (jsonObject2.has("logo")) {
						map.put("logo", jsonObject2.getString("logo"));
					}

					if (jsonObject2.has("start_time")) {
						map.put("start_time", jsonObject2.getString("start_time"));
					}
					if (jsonObject2.has("end_time")) {
						map.put("end_time", jsonObject2.getString("end_time"));
					}
					if (jsonObject2.has("voucher_id")) {
						map.put("voucher_id", jsonObject2.getString("voucher_id"));
					}
					if (jsonObject2.has("code")) {
						map.put("code", jsonObject2.getString("code"));
					}
					couponList.add(map);
				}
				bundle.putSerializable("list", couponList);
				return bundle;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;

	}

	/**
	 * 评价的标签解析
	 */
	public static Bundle CommentTagListJSON(String json) {
		JSONObject jsonObject;
		Bundle bundle = null;
		try {
			jsonObject = new JSONObject(json);
			bundle = new Bundle();

			if (!isResultSuccess(bundle, jsonObject)) {// 当状态为0，返回一个代状态和错误信息的bundle
				return bundle;
			}
			if (jsonObject.has("logo")) {
				bundle.putString("logo", jsonObject.getString("logo"));
			}
			if (jsonObject.has("pinche")) {
				bundle.putString("pinche", jsonObject.getString("pinche"));
			}
			if (jsonObject.has("car")) {
				bundle.putString("car", jsonObject.getString("car"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;

	}

	/**
	 * pay sgin
	 */

	public static Bundle PaySginJSON(String json) {
		Bundle bundle = null;
		JSONObject jsonObject = null;

		try {

			jsonObject = new JSONObject(json);

			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}

			if (jsonObject.has("sign")) {
				bundle.putString("sign", jsonObject.getString("sign"));
			}
			if (jsonObject.has("notify_url")) {
				bundle.putString("notify_url", jsonObject.getString("notify_url"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bundle;

	}

	/**
	 * 车主购买保险的sign
	 */
	public static Bundle driverInsurancePaySginJSON(String json) {
		Bundle bundle = null;
		JSONObject jsonObject = null;

		try {
			jsonObject = new JSONObject(json);
			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}

			if (jsonObject.has("sign")) {
				bundle.putString("sign", jsonObject.getString("sign"));
			}
			if (jsonObject.has("notify_url")) {
				bundle.putString("notify_url", jsonObject.getString("notify_url"));
			}
			if (jsonObject.has("pay_id")) {
				bundle.putString("pay_id", jsonObject.getString("pay_id"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;

	}

	/**
	 * 评价统计
	 */
	public static Bundle evaluationNumberJSON(String json) {
		Bundle bundle = null;
		try {
			JSONObject jsonObject;

			jsonObject = new JSONObject(json);

			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}
			if (jsonObject.has("comment_stat")) {
				jsonObject = (JSONObject) jsonObject.opt("comment_stat");
				if (jsonObject.has("count")) {
					String count = jsonObject.getString("count");
					bundle.putString("count", count);
				}
				if (jsonObject.has("rank")) {
					String rank = jsonObject.getString("rank");
					bundle.putString("rank", rank);
				}
				ArrayList<Map<String, String>> gridviewList = new ArrayList<Map<String, String>>();
				if (jsonObject.has("tags")) {
					JSONArray jsonArray = jsonObject.optJSONArray("tags");
					for (int i = 0; i < jsonArray.length(); i++) {
						HashMap map = new HashMap();
						JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
						if (jsonObject2.has("tag_id")) {
							map.put("tag_id", jsonObject2.getString("tag_id"));
						}
						if (jsonObject2.has("tag_name")) {
							map.put("tag_name", jsonObject2.getString("tag_name"));
						}
						if (jsonObject2.has("times")) {
							map.put("times", jsonObject2.getString("times"));
						}
						gridviewList.add(map);
					}
				}
				bundle.putSerializable("list", gridviewList);
			}
		} catch (Exception e) {

		}

		return bundle;
	}

	/**
	 * 提现接口解析
	 */

	public static Bundle withdrawalJSON(String json) {
		Bundle bundle = null;
		JSONObject jsonObject = null;

		try {

			jsonObject = new JSONObject(json);

			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}

			if (jsonObject.has("balance")) {
				bundle.putString("balance", jsonObject.getString("balance"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bundle;

	}

	/**
	 * 摇一摇历史列表
	 */
	public static Bundle ShakeHistoryJSON(String json) {
		Bundle bundle = null;
		try {
			JSONObject jsonObject;

			jsonObject = new JSONObject(json);

			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}

			ArrayList<Map<String, String>> gridviewList = new ArrayList<Map<String, String>>();
			if (jsonObject.has("game_history")) {
				JSONArray jsonArray = jsonObject.optJSONArray("game_history");
				for (int i = 0; i < jsonArray.length(); i++) {
					HashMap map = new HashMap();
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);

					if (jsonObject2.has("win")) {
						map.put("win", jsonObject2.getString("win"));
					}
					if (jsonObject2.has("add_time")) {
						map.put("add_time", jsonObject2.getString("add_time"));
					}
					gridviewList.add(map);
				}
			}
			bundle.putSerializable("list", gridviewList);
		} catch (Exception e) {

		}

		return bundle;
	}

	/**
	 * 车主保险投保人的个人信息
	 */
	public static Bundle driverInsuranceJSON(String json) {
		Bundle bundle = null;
		try {
			JSONObject jsonObject;

			jsonObject = new JSONObject(json);

			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}
			ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
			if (jsonObject.has("fee_policy")) {
				JSONArray jsonArray = jsonObject.optJSONArray("fee_policy");
				for (int i = 0; i < jsonArray.length(); i++) {
					HashMap map = new HashMap();
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
					if (jsonObject2.has("fee")) {
						String count = jsonObject2.getString("fee");
						map.put("fee", count);
					}
					if (jsonObject2.has("logo")) {
						String rank = jsonObject2.getString("logo");
						map.put("logo", rank);
					}
					if (jsonObject2.has("month")) {
						String rank = jsonObject2.getString("month");
						map.put("month", rank);
					}
					if (jsonObject2.has("end_date")) {
						String end_date = jsonObject2.getString("end_date");
						map.put("end_date", end_date);
					}

					if (jsonObject2.has("type")) {
						String type = jsonObject2.getString("type");
						map.put("type", type);
					}
					list.add(map);
				}
			}

			if (jsonObject.has("insurance")) {
				String optString = jsonObject.optString("insurance");
				if ("[]".equals(optString)) {

				} else {
					JSONObject jsonObject2 = jsonObject.optJSONObject("insurance");
					if (jsonObject2.has("beg_date")) {
						String beg_date = jsonObject2.optString("beg_date");
						bundle.putString("beg_date", beg_date);
					}
					if (jsonObject2.has("end_date")) {
						String end_date = jsonObject2.optString("end_date");
						bundle.putString("end_date", end_date);
					}
					if (jsonObject2.has("id")) {
						String beg_date = jsonObject2.optString("id");
						bundle.putString("id", beg_date);
					}
				}

			}
			if (jsonObject.has("insurance_url")) {
				String insurance_url = jsonObject.optString("insurance_url");
				bundle.putString("insurance_url", insurance_url);
			}

			if (jsonObject.has("passengers")) {
				String passengers = jsonObject.optString("passengers");
				bundle.putString("passengers", passengers);
			}

			if (jsonObject.has("user_info")) {
				JSONObject jsonUserInfo = jsonObject.optJSONObject("user_info");

				if (jsonUserInfo.has("alipay_account")) {
					String alipay_account = jsonUserInfo.optString("alipay_account");
					bundle.putString("alipay_account", alipay_account);
				}
				if (jsonUserInfo.has("card_id")) {
					String card_id = jsonUserInfo.optString("card_id");
					bundle.putString("card_id", card_id);
				}
				if (jsonUserInfo.has("logo")) {
					String logo = jsonUserInfo.optString("logo");
					bundle.putString("logo", logo);
				}
				if (jsonUserInfo.has("name")) {
					String name = jsonUserInfo.optString("name");
					bundle.putString("name", name);
				}
				if (jsonUserInfo.has("phone")) {
					String phone = jsonUserInfo.optString("phone");
					bundle.putString("phone", phone);
				}
			}
			bundle.putSerializable("list", list);
		} catch (Exception e) {

		}
		return bundle;
	}

	/**
	 * 获取保险信息解析
	 */
	public static Bundle insuranceCheckJSON(String json) {
		JSONObject jsonObject;
		Bundle bundle = null;
		try {
			jsonObject = new JSONObject(json);

			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				// return bundle;
			}
			if (jsonObject.has("left_times")) {
				bundle.putString("left_times", jsonObject.getString("left_times"));
			}
			// if (jsonObject.has("state")) {
			// bundle.putString("state",
			// jsonObject.getString("state"));
			// }
			if (jsonObject.has("insurance_url")) {
				bundle.putString("insurance_url", jsonObject.getString("insurance_url"));
			}

			ArrayList<Map<String, String>> gridviewList = new ArrayList<Map<String, String>>();
			if (jsonObject.has("insurance")) {
				JSONObject jsonArray = new JSONObject(jsonObject.getString("insurance").toString());
				if (jsonArray.has("id")) {
					bundle.putString("id", jsonArray.getString("id"));
				}
				if (jsonArray.has("beg_date")) {
					bundle.putString("beg_date", jsonArray.getString("beg_date"));
				}

				if (jsonArray.has("end_date")) {
					bundle.putString("end_date", jsonArray.getString("end_date"));
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bundle;
	}

	/**
	 * 乘客获取保险信息解析
	 */
	public static Bundle insurancePassengerCheckJSON(String json) {
		JSONObject jsonObject;
		Bundle bundle = null;
		try {
			jsonObject = new JSONObject(json);

			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}
			if (jsonObject.has("insurance_url")) {
				bundle.putString("insurance_url", jsonObject.getString("insurance_url"));
			}

			if (jsonObject.has("name")) {
				bundle.putString("name", jsonObject.getString("name"));
			}

			if (jsonObject.has("phone")) {
				bundle.putString("phone", jsonObject.getString("phone"));
			}
			if (jsonObject.has("card_id")) {
				bundle.putString("card_id", jsonObject.getString("card_id"));
			}

			if (jsonObject.has("insurance_status")) {
				bundle.putString("insurance_status", jsonObject.getString("insurance_status"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bundle;
	}

	/**
	 * 车主或者乘客 我的路线集合
	 */
	public static Bundle MyRoutesJSON(String json) {
		Bundle bundle = null;
		try {
			JSONObject jsonObject;

			jsonObject = new JSONObject(json);

			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}
			ArrayList<Map<String, String>> gridviewList = new ArrayList<Map<String, String>>();

			String order_collection = jsonObject.getString("order_collection");
			jsonObject = new JSONObject(order_collection);
			// order_collection.split("")
			// JSONObject jsonObject3 =null;
			// for (int i = 0; i < order_collection.length(); i++) {
			// HashMap map = new HashMap();
			// jsonObject3 = (JSONObject) order_collection.opt(i);
			// }

			if (jsonObject.has("applied")) {
				JSONArray jsonArray = jsonObject.getJSONArray("applied");

				for (int i = 0; i < jsonArray.length(); i++) {
					HashMap map = new HashMap();
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);

					map.put("type", "0");

					if (jsonObject2.has("category")) {

						map.put("category", jsonObject2.getString("category"));
					}

					if (jsonObject2.has("des_car_id")) {

						map.put("des_car_id", jsonObject2.getString("des_car_id"));
					}
					if (jsonObject2.has("des_name")) {

						map.put("des_name", jsonObject2.getString("des_name"));
					}
					if (jsonObject2.has("distance")) {

						map.put("distance", jsonObject2.getString("distance"));
					}
					if (jsonObject2.has("insurance_status")) {

						map.put("insurance_status", jsonObject2.getString("insurance_status"));
					}
					if (jsonObject2.has("order_id")) {

						map.put("order_id", jsonObject2.getString("order_id"));
					}
					if (jsonObject2.has("price")) {

						map.put("price", jsonObject2.getString("price"));
					}
					if (jsonObject2.has("push_times")) {

						map.put("push_times", jsonObject2.getString("push_times"));
					}
					if (jsonObject2.has("setoff_time")) {

						map.put("setoff_time", jsonObject2.getString("setoff_time"));
					}
					if (jsonObject2.has("start_loc")) {

						map.put("start_loc", jsonObject2.getString("start_loc"));
					}
					if (jsonObject2.has("stop_loc")) {

						map.put("stop_loc", jsonObject2.getString("stop_loc"));
					}
					if (jsonObject2.has("user_id")) {

						map.put("user_id", jsonObject2.getString("user_id"));
					}

					gridviewList.add(map);
				}
			}

			// 正在进行

			if (jsonObject.has("incomplete")) {
				JSONArray jsonArray = jsonObject.getJSONArray("incomplete");

				for (int i = 0; i < jsonArray.length(); i++) {
					HashMap map = new HashMap();
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);

					map.put("type", "1");

					if (jsonObject2.has("insurance_status")) {

						map.put("insurance_status", jsonObject2.getString("insurance_status"));
					}
					try {
						if (jsonObject2.has("left_time")) {
							map.put("left_time", (Long.parseLong(jsonObject2.getString("left_time")) * 1000
									+ System.currentTimeMillis()) + "");
						}
					} catch (Exception e) {
						if (jsonObject2.has("left_time")) {
							map.put("left_time", "");
						}
					}

					if (jsonObject2.has("commented")) {

						map.put("commented", jsonObject2.getString("commented"));
					}

					if (jsonObject2.has("car_name")) {

						map.put("car_name", jsonObject2.getString("car_name"));
					}
					if (jsonObject2.has("car_logo")) {

						map.put("car_logo", jsonObject2.getString("car_logo"));
					}

					if (jsonObject2.has("logo")) {

						map.put("logo", jsonObject2.getString("logo"));
					}
					if (jsonObject2.has("brand_id")) {

						map.put("brand_id", jsonObject2.getString("brand_id"));
					}
					if (jsonObject2.has("deal_id")) {

						map.put("deal_id", jsonObject2.getString("deal_id"));
					}
					if (jsonObject2.has("deal_finish")) {

						map.put("deal_finish", jsonObject2.getString("deal_finish"));
					}

					if (jsonObject2.has("driver_order_id")) {

						map.put("driver_order_id", jsonObject2.getString("driver_order_id"));
					}

					if (jsonObject2.has("driver_start_lat")) {

						map.put("driver_start_lat", jsonObject2.getString("driver_start_lat"));
					}

					if (jsonObject2.has("driver_start_lng")) {

						map.put("driver_start_lng", jsonObject2.getString("driver_start_lng"));
					}

					if (jsonObject2.has("age")) {

						map.put("age", jsonObject2.getString("age"));
					}

					if (jsonObject2.has("distance")) {

						map.put("distance", jsonObject2.getString("distance"));
					}
					if (jsonObject2.has("gender")) {

						map.put("gender", jsonObject2.getString("gender"));
					}
					if (jsonObject2.has("logo")) {

						map.put("logo", jsonObject2.getString("logo"));
					}
					if (jsonObject2.has("name")) {

						map.put("name", jsonObject2.getString("name"));
					}
					if (jsonObject2.has("passenger_order_id")) {

						map.put("passenger_order_id", jsonObject2.getString("passenger_order_id"));
					}
					if (jsonObject2.has("phone")) {

						map.put("phone", jsonObject2.getString("phone"));
					}

					if (jsonObject2.has("price")) {

						map.put("price", jsonObject2.getString("price"));
					}

					if (jsonObject2.has("setoff_time")) {

						map.put("setoff_time", jsonObject2.getString("setoff_time"));
					}
					if (jsonObject2.has("passenger_start_lat")) {

						map.put("passenger_start_lat", jsonObject2.getString("passenger_start_lat"));
					}
					if (jsonObject2.has("passenger_start_lng")) {

						map.put("passenger_start_lng", jsonObject2.getString("passenger_start_lng"));
					}

					if (jsonObject2.has("start_loc")) {

						map.put("start_loc", jsonObject2.getString("start_loc"));
					}
					if (jsonObject2.has("stop_loc")) {

						map.put("stop_loc", jsonObject2.getString("stop_loc"));
					}

					if (jsonObject2.has("driver_user_id")) {

						map.put("driver_user_id", jsonObject2.getString("driver_user_id"));
					}
					if (jsonObject2.has("passenger_user_id")) {

						map.put("passenger_user_id", jsonObject2.getString("passenger_user_id"));
					}

					gridviewList.add(map);
				}

			}

			// 发布路线

			if (jsonObject.has("published")) {
				JSONArray jsonArray = jsonObject.getJSONArray("published");
				for (int i = 0; i < jsonArray.length(); i++) {
					HashMap map = new HashMap();
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
					map.put("type", "2");
					if (jsonObject2.has("distance")) {
						map.put("distance", jsonObject2.getString("distance"));
					}
					if (jsonObject2.has("order_id")) {
						map.put("order_id", jsonObject2.getString("order_id"));
					}

					if (jsonObject2.has("price")) {
						map.put("price", jsonObject2.getString("price"));
					}

					if (jsonObject2.has("setoff_time")) {
						map.put("setoff_time", jsonObject2.getString("setoff_time"));
					}

					if (jsonObject2.has("start_loc")) {
						map.put("start_loc", jsonObject2.getString("start_loc"));
					}
					if (jsonObject2.has("stop_loc")) {
						map.put("stop_loc", jsonObject2.getString("stop_loc"));
					}
					if (jsonObject2.has("user_id")) {
						map.put("user_id", jsonObject2.getString("user_id"));
					}
					if (jsonObject2.has("push_times")) {
						map.put("push_times", jsonObject2.getString("push_times"));
					}

					if (jsonObject2.has("des_name")) {
						map.put("des_name", jsonObject2.getString("des_name"));
					}
					if (jsonObject2.has("des_car_id")) {
						map.put("des_car_id", jsonObject2.getString("des_car_id"));
					}
					gridviewList.add(map);
				}
			}

			if (gridviewList.size() > 0) {
				bundle.putSerializable("list", gridviewList);
			} else {
				bundle.putSerializable("list", null);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	/**
	 * 获取两点的价格
	 */
	public static Bundle distanceGetJSON(String json) {
		JSONObject jsonObject;
		Bundle bundle = null;
		try {
			jsonObject = new JSONObject(json);

			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}
			if (jsonObject.has("distance")) {
				bundle.putString("distance", jsonObject.getString("distance"));
			}

			if (jsonObject.has("price")) {
				bundle.putString("price", jsonObject.getString("price"));
			}

			if (jsonObject.has("taxi_cost")) {
				bundle.putString("taxi_cost", jsonObject.getString("taxi_cost"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	/**
	 * 当乘客时，解析来自车主所有评价
	 * 
	 * @param json
	 * @return
	 */
	public static Bundle parserFromOwnerJson(String json) {
		JSONObject jsonObject = null;
		Bundle bundle = null;
		HashMap<String, String> hMap = null;
		JSONObject optJSONObject = null;
		try {
			jsonObject = new JSONObject(json);
			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}
			if (jsonObject.has("count")) {
				bundle.putString("count", jsonObject.getString("count"));
			}
			if (jsonObject.has("rank")) {
				bundle.putString("rank", jsonObject.getString("rank"));
			}
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			if (jsonObject.has("comment_list")) {
				JSONArray optJSONArray = jsonObject.optJSONArray("comment_list");
				for (int i = 0; i < optJSONArray.length(); i++) {
					hMap = new HashMap<String, String>();
					optJSONObject = optJSONArray.optJSONObject(i);
					if (optJSONObject.has("add_time")) {
						hMap.put("add_time", optJSONObject.optString("add_time"));
					}
					if (optJSONObject.has("content")) {
						hMap.put("content", optJSONObject.optString("content"));
					}
					if (optJSONObject.has("user_id")) {
						hMap.put("user_id", optJSONObject.optString("user_id"));
					}
					if (optJSONObject.has("rank")) {
						hMap.put("rank", optJSONObject.optString("rank"));
					}
					if (optJSONObject.has("user_logo")) {
						hMap.put("user_logo", optJSONObject.optString("user_logo"));
					}
					if (optJSONObject.has("name")) {
						hMap.put("name", optJSONObject.optString("name"));
					}
					data.add(hMap);
				}

			}
			bundle.putSerializable("list", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	/**
	 * 当车主时，解析来自乘客的所有评价
	 * 
	 * @param json
	 * @return budle
	 */
	public static Bundle parserFromPassengerJson(String json) {
		JSONObject jsonObject = null;
		Bundle bundle = null;
		HashMap<String, String> hMap = null;
		JSONObject optJSONObject = null;
		try {
			jsonObject = new JSONObject(json);
			bundle = new Bundle();
			if (!isResultSuccess(bundle, jsonObject)) {
				return bundle;
			}
			if (jsonObject.has("count")) {
				bundle.putString("count", jsonObject.getString("count"));
			}
			if (jsonObject.has("rank")) {
				bundle.putString("rank", jsonObject.getString("rank"));
			}
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			if (jsonObject.has("comment_list")) {
				JSONArray optJSONArray = jsonObject.optJSONArray("comment_list");
				for (int i = 0; i < optJSONArray.length(); i++) {
					hMap = new HashMap<String, String>();
					optJSONObject = optJSONArray.optJSONObject(i);
					if (optJSONObject.has("add_time")) {
						hMap.put("add_time", optJSONObject.optString("add_time"));
					}
					if (optJSONObject.has("name")) {
						hMap.put("name", optJSONObject.optString("name"));
					}
					if (optJSONObject.has("content")) {
						hMap.put("content", optJSONObject.optString("content"));
					}
					if (optJSONObject.has("user_id")) {
						hMap.put("user_id", optJSONObject.optString("user_id"));
					}
					if (optJSONObject.has("rank")) {
						hMap.put("rank", optJSONObject.optString("rank"));
					}
					if (optJSONObject.has("user_logo")) {
						hMap.put("user_logo", optJSONObject.optString("user_logo"));
					}
					data.add(hMap);
				}
			}
			bundle.putSerializable("list", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	/***
	 * 通用
	 */
	// private static int isSuccess(Bundle bundle, JSONObject jsonObject)
	// throws JSONException {
	// int isSuc=0;
	// if (jsonObject.has("code")) {
	// String code = jsonObject.optString("code");
	// if (code.equals("")) {
	// if (jsonObject.has("status")) {
	// int state = jsonObject.optInt(STATE);
	//
	// bundle.putInt(STATE, state);
	// if (state == 200) {
	// isSuc=200;
	// } else if (state == 201) {
	// isSuc=201;
	// }
	// if (jsonObject.has("msg")) {
	// bundle.putString("msg", jsonObject.optString("msg"));
	// }
	// }
	// }else if(code.equals("-1")){
	// isSuc=-1;
	// }
	//
	// }
	//
	//
	// return isSuc;
	// }

	public static Bundle ParserMsg(String json) {
		Bundle bundle = null;
		try {

			JSONObject jsonObject = new JSONObject(json);
			bundle = new Bundle();
			if (jsonObject.has("msg")) {
				bundle.putString("msg", jsonObject.getString("msg"));
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return bundle;
	}

	public static Bundle ParserLogin(String json) {
		Bundle bundle = null;
		try {

			JSONObject jsonObject1 = new JSONObject(json);
			bundle = new Bundle();
			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			if (jsonObject1.has("result")) {
				JSONObject jsonObject = new JSONObject(jsonObject1.getString("result"));

				if (jsonObject.has("is_accept")) {
					bundle.putString("is_accept", jsonObject.getString("is_accept"));
				}
				if (jsonObject.has("id")) {
					bundle.putString("id", jsonObject.getString("id"));
				}
				if (jsonObject.has("userno")) {
					bundle.putString("userno", jsonObject.getString("userno"));
				}
				if (jsonObject.has("realname")) {
					bundle.putString("realname", jsonObject.getString("realname"));
				}
				if (jsonObject.has("mobile")) {
					bundle.putString("mobile", jsonObject.getString("mobile"));
				}
				if (jsonObject.has("age")) {
					bundle.putString("age", jsonObject.getString("age"));
				}
				if (jsonObject.has("workage")) {
					bundle.putString("workage", jsonObject.getString("workage"));
				}
				if (jsonObject.has("sex")) {
					bundle.putString("sex", jsonObject.getString("sex"));
				}
				if (jsonObject.has("card")) {
					bundle.putString("card", jsonObject.getString("card"));
				}
				if (jsonObject.has("address")) {
					bundle.putString("address", jsonObject.getString("address"));
				}
				if (jsonObject.has("portrait")) {
					bundle.putString("portrait", jsonObject.getString("portrait"));
				}
				if (jsonObject.has("starlevel")) {
					bundle.putString("starlevel", jsonObject.getString("starlevel"));
				}
				if (jsonObject.has("xinyong")) {
					bundle.putString("xinyong", jsonObject.getString("xinyong"));
				}
				if (jsonObject.has("token")) {
					bundle.putString("token", jsonObject.getString("token"));
				}
				if (jsonObject.has("is_healthy")) {
					bundle.putString("is_healthy", jsonObject.getString("is_healthy"));
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return bundle;
	}

	public static Bundle ParserSendCode(String json) {
		Bundle bundle = null;
		try {

			JSONObject jsonObject = new JSONObject(json);
			bundle = new Bundle();
			if (jsonObject.has("msg")) {
				bundle.putString("msg", jsonObject.getString("msg"));
			}

			if (jsonObject.has("result")) {
				bundle.putString("result", jsonObject.getString("result"));
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return bundle;
	}

	public static Bundle ParserUpload(String json) {
		Bundle bundle = null;
		try {

			JSONObject jsonObject1 = new JSONObject(json);
			bundle = new Bundle();
			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			if (jsonObject1.has("result")) {
				JSONObject jsonObject = new JSONObject(jsonObject1.getString("result"));

				if (jsonObject.has("url")) {
					bundle.putString("url", jsonObject.getString("url"));
				}

				if (jsonObject.has("length")) {
					bundle.putString("length", jsonObject.getString("length"));
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return bundle;
	}

	public static Bundle ParserForgetPassword(String json) {
		Bundle bundle = null;
		try {

			JSONObject jsonObject1 = new JSONObject(json);
			bundle = new Bundle();
			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			if (jsonObject1.has("result")) {
				JSONObject jsonObject = new JSONObject(jsonObject1.getString("result"));

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return bundle;
	}

	public static Bundle ParserCarousel(String json) {
		JSONObject jsonObject1 = null;
		Bundle bundle = null;
		HashMap<String, String> hMap = null;
		JSONObject optJSONObject = null;
		try {
			jsonObject1 = new JSONObject(json);
			bundle = new Bundle();

			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			if (jsonObject1.has("result")) {
				JSONArray optJSONArray = jsonObject1.optJSONArray("result");
				for (int i = 0; i < optJSONArray.length(); i++) {
					hMap = new HashMap<String, String>();
					optJSONObject = optJSONArray.optJSONObject(i);

					if (optJSONObject.has("id")) {
						hMap.put("id", optJSONObject.optString("id"));

					}
					if (optJSONObject.has("remark")) {
						hMap.put("remark", optJSONObject.optString("remark"));

					}
					if (optJSONObject.has("dict_value")) {
						hMap.put("dict_value", optJSONObject.optString("dict_value"));

					}
					if (optJSONObject.has("dict_param")) {
						hMap.put("dict_param", optJSONObject.optString("dict_param"));

					}
					data.add(hMap);
				}
			}
			bundle.putSerializable("list", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	public static Bundle ParserCollection(String json) {
		JSONObject jsonObject1 = null;
		Bundle bundle = null;
		HashMap<String, String> hMap = null;
		JSONObject optJSONObject = null;
		try {
			jsonObject1 = new JSONObject(json);
			bundle = new Bundle();

			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			if (jsonObject1.has("result")) {

				if (jsonObject1.has("result")) {
					JSONObject jsonObject = new JSONObject(jsonObject1.getString("result"));

					if (jsonObject.has("data")) {
						// jsonObject.getString("data") ;

						JSONArray optJSONArray = jsonObject.optJSONArray("data");

						for (int i = 0; i < optJSONArray.length(); i++) {
							hMap = new HashMap<String, String>();
							optJSONObject = optJSONArray.optJSONObject(i);

							if (optJSONObject.has("phone")) {
								hMap.put("phone", optJSONObject.optString("phone"));

							}
							if (optJSONObject.has("front_photos")) {
								hMap.put("front_photos", optJSONObject.optString("front_photos"));

							}
							if (optJSONObject.has("hotelid")) {
								hMap.put("hotelid", optJSONObject.optString("hotelid"));

							}

							if (optJSONObject.has("income")) {
								hMap.put("income", optJSONObject.optString("income"));

							}
							if (optJSONObject.has("name")) {
								hMap.put("name", optJSONObject.optString("name"));

							}
							if (optJSONObject.has("grab_count")) {
								hMap.put("grab_count", optJSONObject.optString("grab_count"));

							}
							if (optJSONObject.has("cooperate_count")) {
								hMap.put("cooperate_count", optJSONObject.optString("cooperate_count"));

							}

							data.add(hMap);
						}
					}

				}

			}
			bundle.putSerializable("list", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	public static Bundle ParserCitylist(String json) {
		JSONObject jsonObject1 = null;
		Bundle bundle = null;
		HashMap<String, String> hMap = null;
		JSONObject optJSONObject = null;
		try {
			jsonObject1 = new JSONObject(json);
			bundle = new Bundle();

			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			if (jsonObject1.has("result")) {
				JSONArray optJSONArray = jsonObject1.optJSONArray("result");
				for (int i = 0; i < optJSONArray.length(); i++) {
					hMap = new HashMap<String, String>();
					optJSONObject = optJSONArray.optJSONObject(i);
					if (optJSONObject.has("cityno")) {
						hMap.put("cityno", optJSONObject.optString("cityno"));

					}
					if (optJSONObject.has("name")) {
						hMap.put("name", optJSONObject.optString("name"));

					}
					data.add(hMap);
				}
			}
			bundle.putSerializable("list", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	public static Bundle ParserDetail(String json) {
		Bundle bundle = null;
		try {

			JSONObject jsonObject1 = new JSONObject(json);
			bundle = new Bundle();
			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			if (jsonObject1.has("result")) {
				JSONObject jsonObject = new JSONObject(jsonObject1.getString("result"));

				if (jsonObject.has("front_photos")) {
					bundle.putString("front_photos", jsonObject.getString("front_photos"));
				}

				if (jsonObject.has("address")) {
					bundle.putString("address", jsonObject.getString("address"));
				}
				if (jsonObject.has("name")) {
					bundle.putString("name", jsonObject.getString("name"));
				}
				if (jsonObject.has("made")) {
					bundle.putString("made", jsonObject.getString("made"));
				}
				if (jsonObject.has("suite_price")) {
					bundle.putString("suite_price", jsonObject.getString("suite_price"));
				}
				if (jsonObject.has("standard_price")) {
					bundle.putString("standard_price", jsonObject.getString("standard_price"));
				}
				if (jsonObject.has("dutydate")) {
					bundle.putString("dutydate", jsonObject.getString("dutydate"));
				}
				if (jsonObject.has("latitude")) {
					bundle.putString("latitude", jsonObject.getString("latitude"));
				}
				if (jsonObject.has("longitude")) {
					bundle.putString("longitude", jsonObject.getString("longitude"));
				}

				if (jsonObject.has("phone")) {
					bundle.putString("phone", jsonObject.getString("phone"));
				}
				if (jsonObject.has("star")) {
					bundle.putString("star", jsonObject.getString("star"));
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return bundle;
	}

	public static Bundle ParserMylist(String json) {
		JSONObject jsonObject1 = null;
		Bundle bundle = null;
		HashMap<String, String> hMap = null;
		JSONObject optJSONObject = null;
		try {
			jsonObject1 = new JSONObject(json);
			bundle = new Bundle();

			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			if (jsonObject1.has("result")) {

				if (jsonObject1.has("result")) {
					JSONObject jsonObject = new JSONObject(jsonObject1.getString("result"));

					if (jsonObject.has("data")) {
						// jsonObject.getString("data") ;

						JSONArray optJSONArray = jsonObject.optJSONArray("data");

						for (int i = 0; i < optJSONArray.length(); i++) {
							hMap = new HashMap<String, String>();
							optJSONObject = optJSONArray.optJSONObject(i);

							if (optJSONObject.has("orderdetailid")) {
								hMap.put("orderdetailid", optJSONObject.optString("orderdetailid"));

							}

							if (optJSONObject.has("workercount")) {
								hMap.put("workercount", optJSONObject.optString("workercount"));

							}
							if (optJSONObject.has("roomcount")) {
								hMap.put("roomcount", optJSONObject.optString("roomcount"));

							}
							if (optJSONObject.has("front_photos")) {
								hMap.put("front_photos", optJSONObject.optString("front_photos"));

							}
							if (optJSONObject.has("status")) {
								hMap.put("status", optJSONObject.optString("status"));

							}

							if (optJSONObject.has("totalprice")) {
								hMap.put("totalprice", optJSONObject.optString("totalprice"));

							}
							if (optJSONObject.has("name")) {
								hMap.put("name", optJSONObject.optString("name"));

							}
							if (optJSONObject.has("made")) {
								hMap.put("made", optJSONObject.optString("made"));

							}
							if (optJSONObject.has("suite_price")) {
								hMap.put("suite_price", optJSONObject.optString("suite_price"));

							}
							if (optJSONObject.has("dutydate")) {
								hMap.put("dutydate", optJSONObject.optString("dutydate"));

							}
							if (optJSONObject.has("orderid")) {
								hMap.put("orderid", optJSONObject.optString("orderid"));

							}
							if (optJSONObject.has("standard_price")) {
								hMap.put("standard_price", optJSONObject.optString("standard_price"));

							}

							if (optJSONObject.has("phone")) {
								hMap.put("phone", optJSONObject.optString("phone"));

							}
							if (optJSONObject.has("earn_money")) {
								hMap.put("earn_money", optJSONObject.optString("earn_money"));

							}
							if (optJSONObject.has("dict_value")) {
								hMap.put("dict_value", optJSONObject.optString("dict_value"));

							}
							if (optJSONObject.has("score")) {
								hMap.put("score", optJSONObject.optString("score"));

							}
							if (optJSONObject.has("address")) {
								hMap.put("address", optJSONObject.optString("address"));

							}

							data.add(hMap);
						}
					}

				}

			}
			bundle.putSerializable("list", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	public static Bundle ParserOrdercList(String json) {
		JSONObject jsonObject1 = null;
		Bundle bundle = null;
		HashMap<String, String> hMap = null;
		JSONObject optJSONObject = null;
		try {
			jsonObject1 = new JSONObject(json);
			bundle = new Bundle();

			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			if (jsonObject1.has("result")) {

				if (jsonObject1.has("result")) {
					JSONObject jsonObject = new JSONObject(jsonObject1.getString("result"));

					if (jsonObject.has("data")) {
						// jsonObject.getString("data") ;

						JSONArray optJSONArray = jsonObject.optJSONArray("data");

						for (int i = 0; i < optJSONArray.length(); i++) {
							hMap = new HashMap<String, String>();
							optJSONObject = optJSONArray.optJSONObject(i);

							if (jsonObject.has("count")) {
								hMap.put("count", jsonObject.optString("count"));

							}
							if (jsonObject.has("star")) {
								hMap.put("star", jsonObject.optString("star"));

							}

							if (optJSONObject.has("address")) {
								hMap.put("address", optJSONObject.optString("address"));

							}

							if (optJSONObject.has("name")) {
								hMap.put("name", optJSONObject.optString("name"));

							}
							if (optJSONObject.has("made")) {
								hMap.put("made", optJSONObject.optString("made"));

							}
							if (optJSONObject.has("havebar")) {
								hMap.put("havebar", optJSONObject.optString("havebar"));

							}
							if (optJSONObject.has("havelaunch")) {
								hMap.put("havelaunch", optJSONObject.optString("havelaunch"));

							}
							if (optJSONObject.has("suite_price")) {
								hMap.put("suite_price", optJSONObject.optString("suite_price"));

							}
							if (optJSONObject.has("bounus")) {
								hMap.put("bounus", optJSONObject.optString("bounus"));

							}
							if (optJSONObject.has("dutydate")) {
								hMap.put("dutydate", optJSONObject.optString("dutydate"));

							}
							if (optJSONObject.has("standard_price")) {
								hMap.put("standard_price", optJSONObject.optString("standard_price"));

							}
							if (optJSONObject.has("orderid")) {
								hMap.put("orderid", optJSONObject.optString("orderid"));

							}

							if (optJSONObject.has("longitude")) {
								hMap.put("longitude", optJSONObject.optString("longitude"));

							}
							if (optJSONObject.has("latitude")) {
								hMap.put("latitude", optJSONObject.optString("latitude"));

							}
							data.add(hMap);
						}
					}

				}

			}
			bundle.putSerializable("list", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	public static Bundle ParserRecord(String json) {
		JSONObject jsonObject1 = null;
		Bundle bundle = null;
		HashMap<String, String> hMap = null;
		JSONObject optJSONObject = null;
		try {
			jsonObject1 = new JSONObject(json);
			bundle = new Bundle();

			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			if (jsonObject1.has("result")) {

				if (jsonObject1.has("result")) {
					JSONObject jsonObject = new JSONObject(jsonObject1.getString("result"));

					if (jsonObject.has("data")) {
						// jsonObject.getString("data") ;

						JSONArray optJSONArray = jsonObject.optJSONArray("data");

						for (int i = 0; i < optJSONArray.length(); i++) {
							hMap = new HashMap<String, String>();
							optJSONObject = optJSONArray.optJSONObject(i);

							if (optJSONObject.has("num")) {
								hMap.put("num", optJSONObject.optString("num"));

							}

							if (optJSONObject.has("front_photos")) {
								hMap.put("front_photos", optJSONObject.optString("front_photos"));

							}
							if (optJSONObject.has("name")) {
								hMap.put("name", optJSONObject.optString("name"));

							}

							data.add(hMap);
						}
					}

				}

			}
			bundle.putSerializable("list", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	public static Bundle ParserCancelReason(String json) {
		JSONObject jsonObject1 = null;
		Bundle bundle = null;
		HashMap<String, String> hMap = null;
		JSONObject optJSONObject = null;
		try {
			jsonObject1 = new JSONObject(json);
			bundle = new Bundle();

			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			if (jsonObject1.has("result")) {
				JSONArray optJSONArray = jsonObject1.optJSONArray("result");
				for (int i = 0; i < optJSONArray.length(); i++) {
					hMap = new HashMap<String, String>();
					optJSONObject = optJSONArray.optJSONObject(i);
					if (optJSONObject.has("id")) {
						hMap.put("id", optJSONObject.optString("id"));

					}
					if (optJSONObject.has("dict_value")) {
						hMap.put("dict_value", optJSONObject.optString("dict_value"));

					}
					hMap.put("flag", "false");
					data.add(hMap);
				}
			}
			bundle.putSerializable("list", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	public static Bundle ParserMyIncome(String json) {
		JSONObject jsonObject1 = null;
		Bundle bundle = null;
		HashMap<String, String> hMap = null;
		JSONObject optJSONObject = null;
		try {
			jsonObject1 = new JSONObject(json);
			bundle = new Bundle();

			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			if (jsonObject1.has("result")) {

				if (jsonObject1.has("result")) {
					JSONObject jsonObject = new JSONObject(jsonObject1.getString("result"));

					if (jsonObject.has("totalincome")) {
						bundle.putString("totalincome", jsonObject.getString("totalincome"));
					}

					if (jsonObject.has("totalscore")) {
						bundle.putString("totalscore", jsonObject.getString("totalscore"));
					}
					if (jsonObject.has("incomeList")) {
						// jsonObject.getString("data") ;

						JSONArray optJSONArray = jsonObject.optJSONArray("incomeList");

						for (int i = 0; i < optJSONArray.length(); i++) {
							hMap = new HashMap<String, String>();
							optJSONObject = optJSONArray.optJSONObject(i);

							if (optJSONObject.has("year")) {
								hMap.put("year", optJSONObject.optString("year"));

							}
							if (optJSONObject.has("month")) {
								hMap.put("month", optJSONObject.optString("month"));

							}
							if (optJSONObject.has("income")) {
								hMap.put("income", optJSONObject.optString("income"));

							}
							if (optJSONObject.has("num")) {
								hMap.put("num", optJSONObject.optString("num"));

							}

							data.add(hMap);
						}
					}

				}

			}
			bundle.putSerializable("list", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	public static Bundle ParserSearch(String json) {
		JSONObject jsonObject1 = null;
		Bundle bundle = null;
		HashMap<String, String> hMap = null;
		JSONObject optJSONObject = null;
		try {
			jsonObject1 = new JSONObject(json);
			bundle = new Bundle();

			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			if (jsonObject1.has("result")) {
				JSONArray optJSONArray = jsonObject1.optJSONArray("result");
				for (int i = 0; i < optJSONArray.length(); i++) {
					hMap = new HashMap<String, String>();
					optJSONObject = optJSONArray.optJSONObject(i);
					if (optJSONObject.has("id")) {
						hMap.put("id", optJSONObject.optString("id"));

					}
					if (optJSONObject.has("name")) {
						hMap.put("name", optJSONObject.optString("name"));
					}
					if (optJSONObject.has("realname")) {
						hMap.put("realname", optJSONObject.optString("realname"));
					}
					if (optJSONObject.has("department")) {
						hMap.put("department", optJSONObject.optString("department"));
					}
					if (optJSONObject.has("front_photos")) {
						hMap.put("front_photos", optJSONObject.optString("front_photos"));
					}

					data.add(hMap);
				}
			}
			bundle.putSerializable("list", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	public static Bundle ParserInfoB(String json) {
		Bundle bundle = null;
		try {

			JSONObject jsonObject1 = new JSONObject(json);
			bundle = new Bundle();
			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			if (jsonObject1.has("result")) {
				JSONObject jsonObject = new JSONObject(jsonObject1.getString("result"));

				if (jsonObject.has("id")) {
					bundle.putString("id", jsonObject.getString("id"));
				}
				if (jsonObject.has("phone")) {
					bundle.putString("phone", jsonObject.getString("phone"));
				}
				if (jsonObject.has("license_photos")) {
					bundle.putString("license_photos", jsonObject.getString("license_photos"));
				}
				if (jsonObject.has("front_photos")) {
					bundle.putString("front_photos", jsonObject.getString("front_photos"));
				}
				if (jsonObject.has("zzjgdm")) {
					bundle.putString("zzjgdm", jsonObject.getString("zzjgdm"));
				}
				if (jsonObject.has("address")) {
					bundle.putString("address", jsonObject.getString("address"));
				}
				if (jsonObject.has("name")) {
					bundle.putString("name", jsonObject.getString("name"));
				}
				if (jsonObject.has("roomcounts")) {
					bundle.putString("roomcounts", jsonObject.getString("roomcounts"));
				}
				if (jsonObject.has("type")) {
					bundle.putString("type", jsonObject.getString("type"));
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return bundle;
	}

	public static Bundle ParserRegister(String json) {
		Bundle bundle = null;
		try {

			JSONObject jsonObject1 = new JSONObject(json);
			bundle = new Bundle();
			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			if (jsonObject1.has("result")) {
				JSONObject jsonObject = new JSONObject(jsonObject1.getString("result"));

				if (jsonObject.has("userId")) {
					bundle.putString("userId", jsonObject.getString("userId"));
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return bundle;
	}

	public static Bundle ParserLoginB(String json) {
		Bundle bundle = null;
		try {

			JSONObject jsonObject1 = new JSONObject(json);
			bundle = new Bundle();
			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			if (jsonObject1.has("result")) {
				JSONObject jsonObject = new JSONObject(jsonObject1.getString("result"));

				if (jsonObject.has("id")) {
					bundle.putString("id", jsonObject.getString("id"));
				}

				if (jsonObject.has("realname")) {
					bundle.putString("realname", jsonObject.getString("realname"));
				}
				if (jsonObject.has("mobile")) {
					bundle.putString("mobile", jsonObject.getString("mobile"));
				}
				if (jsonObject.has("card")) {
					bundle.putString("card", jsonObject.getString("card"));
				}
				if (jsonObject.has("portrait")) {
					bundle.putString("portrait", jsonObject.getString("portrait"));
				}
				if (jsonObject.has("xinyong")) {
					bundle.putString("xinyong", jsonObject.getString("xinyong"));
				}
				if (jsonObject.has("token")) {
					bundle.putString("token", jsonObject.getString("token"));
				}
				if (jsonObject.has("role")) {
					bundle.putString("role", jsonObject.getString("role"));
				}

				if (jsonObject.has("hotel")) {
					bundle.putString("hotel", jsonObject.getString("hotel"));
				}
				if (jsonObject.has("balance")) {
					bundle.putString("balance", jsonObject.getString("balance"));
				}
				if (jsonObject.has("star")) {
					bundle.putString("star", jsonObject.getString("star"));
				}
				if (jsonObject.has("jifen")) {
					bundle.putString("jifen", jsonObject.getString("jifen"));
				}
				if (jsonObject.has("hotelid")) {
					bundle.putString("hotelid", jsonObject.getString("hotelid"));
				}
				if (jsonObject.has("phone")) {
					bundle.putString("phone", jsonObject.getString("phone"));
				}

				if (jsonObject.has("email")) {
					bundle.putString("email", jsonObject.getString("email"));
				}

				if (jsonObject.has("address")) {
					bundle.putString("address", jsonObject.getString("address"));
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return bundle;
	}

	public static Bundle ParserMylistDangqian(String json) {
		JSONObject jsonObject1 = null;
		Bundle bundle = null;
		HashMap<String, String> hMap = null;
		JSONObject optJSONObject = null;
		try {
			jsonObject1 = new JSONObject(json);
			bundle = new Bundle();

			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			if (jsonObject1.has("result")) {

				if (jsonObject1.has("result")) {
					JSONObject jsonObject = new JSONObject(jsonObject1.getString("result"));

					if (jsonObject.has("data")) {
						// jsonObject.getString("data") ;

						JSONArray optJSONArray = jsonObject.optJSONArray("data");

						for (int i = 0; i < optJSONArray.length(); i++) {
							hMap = new HashMap<String, String>();
							optJSONObject = optJSONArray.optJSONObject(i);

							if (optJSONObject.has("id")) {
								hMap.put("id", optJSONObject.optString("id"));
							}
							if (optJSONObject.has("orderno")) {
								hMap.put("orderno", optJSONObject.optString("orderno"));
							}

							if (optJSONObject.has("standard_count")) {
								hMap.put("standard_count", optJSONObject.optString("standard_count"));
							}
							if (optJSONObject.has("suite_count")) {
								hMap.put("suite_count", optJSONObject.optString("suite_count"));
							}

							if (optJSONObject.has("status")) {
								hMap.put("status", optJSONObject.optString("status"));
							}
							if (optJSONObject.has("totalprice")) {
								hMap.put("totalprice", optJSONObject.optString("totalprice"));
							}

							if (optJSONObject.has("workercount")) {
								hMap.put("workercount", optJSONObject.optString("workercount"));
							}
							if (optJSONObject.has("bangkesname")) {
								hMap.put("bangkesname", optJSONObject.optString("bangkesname"));
							}
							if (optJSONObject.has("bangkesportrait")) {
								hMap.put("bangkesportrait", optJSONObject.optString("bangkesportrait"));
							}
							if (optJSONObject.has("dutydate")) {
								hMap.put("dutydate", optJSONObject.optString("dutydate"));
							}
							if (optJSONObject.has("can_cancel")) {
								hMap.put("can_cancel", optJSONObject.optString("can_cancel"));
							}
							if (optJSONObject.has("dict_value")) {
								hMap.put("dict_value", optJSONObject.optString("dict_value"));
							}

							if (optJSONObject.has("bounus")) {
								hMap.put("bounus", optJSONObject.optString("bounus"));
							}
							if (optJSONObject.has("standard_price")) {
								hMap.put("standard_price", optJSONObject.optString("standard_price"));
							}
							if (optJSONObject.has("suite_price")) {
								hMap.put("suite_price", optJSONObject.optString("suite_price"));
							}
							if (optJSONObject.has("totalprice")) {
								hMap.put("totalprice", optJSONObject.optString("totalprice"));
							}

							if (optJSONObject.has("bangkelist")) {
								hMap.put("bangkelist", optJSONObject.optString("bangkelist"));
							}
							if (optJSONObject.has("isSign")) {
								hMap.put("isSign", optJSONObject.optString("isSign"));
							}

							data.add(hMap);
						}
					}

				}

			}
			bundle.putSerializable("list", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	public static Bundle ParserArriveB(String json) {
		JSONObject jsonObject1 = null;
		Bundle bundle = null;
		HashMap<String, String> hMap = null;
		JSONObject optJSONObject = null;
		try {
			jsonObject1 = new JSONObject(json);
			bundle = new Bundle();

			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			ArrayList<HashMap<String, String>> data1 = new ArrayList<HashMap<String, String>>();
			ArrayList<HashMap<String, String>> data2 = new ArrayList<HashMap<String, String>>();
			if (jsonObject1.has("result")) {

				if (jsonObject1.has("result")) {
					JSONObject jsonObject = new JSONObject(jsonObject1.getString("result"));

					if (jsonObject.has("arrivenum")) {
						bundle.putString("arrivenum", jsonObject.getString("arrivenum"));
					}

					if (jsonObject.has("unarrivenum")) {
						bundle.putString("unarrivenum", jsonObject.getString("unarrivenum"));
					}

					if (jsonObject.has("unarrivelist")) {
						// jsonObject.getString("data") ;

						JSONArray optJSONArray = jsonObject.optJSONArray("unarrivelist");

						for (int i = 0; i < optJSONArray.length(); i++) {
							hMap = new HashMap<String, String>();
							optJSONObject = optJSONArray.optJSONObject(i);

							if (optJSONObject.has("realname")) {
								hMap.put("realname", optJSONObject.optString("realname"));
							}
							if (optJSONObject.has("portrait")) {
								hMap.put("portrait", optJSONObject.optString("portrait"));
							}

							data1.add(hMap);
						}
					}

					if (jsonObject.has("arrivelist")) {
						JSONArray optJSONArray = jsonObject.optJSONArray("arrivelist");

						for (int i = 0; i < optJSONArray.length(); i++) {
							hMap = new HashMap<String, String>();
							optJSONObject = optJSONArray.optJSONObject(i);

							if (optJSONObject.has("realname")) {
								hMap.put("realname", optJSONObject.optString("realname"));
							}
							if (optJSONObject.has("portrait")) {
								hMap.put("portrait", optJSONObject.optString("portrait"));
							}

							data2.add(hMap);
						}
					}

				}

			}
			bundle.putSerializable("list1", data1);
			bundle.putSerializable("list2", data2);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	public static Bundle ParserMember(String json) {
		JSONObject jsonObject1 = null;
		Bundle bundle = null;
		HashMap<String, String> hMap = null;
		JSONObject optJSONObject = null;
		try {
			jsonObject1 = new JSONObject(json);
			bundle = new Bundle();

			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			if (jsonObject1.has("result")) {
				JSONArray optJSONArray = jsonObject1.optJSONArray("result");
				for (int i = 0; i < optJSONArray.length(); i++) {
					hMap = new HashMap<String, String>();
					optJSONObject = optJSONArray.optJSONObject(i);
					if (optJSONObject.has("priority")) {
						hMap.put("priority", optJSONObject.optString("priority"));

					}
					if (optJSONObject.has("department")) {
						hMap.put("department", optJSONObject.optString("department"));

					}
					if (optJSONObject.has("department_value")) {
						hMap.put("department_value", optJSONObject.optString("department_value"));

					}

					if (optJSONObject.has("id")) {
						hMap.put("id", optJSONObject.optString("id"));

					}
					if (optJSONObject.has("realname")) {
						hMap.put("realname", optJSONObject.optString("realname"));

					}
					if (optJSONObject.has("is_verify")) {
						hMap.put("is_verify", optJSONObject.optString("is_verify"));

					}
					if (optJSONObject.has("portrait")) {
						hMap.put("portrait", optJSONObject.optString("portrait"));

					}
					if (optJSONObject.has("department")) {
						hMap.put("department", optJSONObject.optString("department"));
					}
					if (optJSONObject.has("is_order")) {
						hMap.put("is_order", optJSONObject.optString("is_order"));
					}
					if (optJSONObject.has("is_check")) {
						hMap.put("is_check", optJSONObject.optString("is_check"));
					}
					if (optJSONObject.has("is_select")) {
						hMap.put("is_select", optJSONObject.optString("is_select"));
					}
					if (optJSONObject.has("mobile")) {
						hMap.put("mobile", optJSONObject.optString("mobile"));
					}

					if (optJSONObject.has("userid")) {
						hMap.put("userid", optJSONObject.optString("userid"));
					}

					data.add(hMap);
				}
			}
			bundle.putSerializable("list", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	public static Bundle ParserUserbSearch(String json) {
		Bundle bundle = null;
		try {

			JSONObject jsonObject1 = new JSONObject(json);
			bundle = new Bundle();
			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			if (jsonObject1.has("result")) {
				JSONObject jsonObject = new JSONObject(jsonObject1.getString("result"));

				if (jsonObject.has("id")) {
					bundle.putString("id", jsonObject.getString("id"));
				}
				if (jsonObject.has("department")) {
					bundle.putString("department", jsonObject.getString("department"));
				}
				if (jsonObject.has("realname")) {
					bundle.putString("realname", jsonObject.getString("realname"));
				}

				if (jsonObject.has("portrait")) {
					bundle.putString("portrait", jsonObject.getString("portrait"));
				}
				if (jsonObject.has("mobile")) {
					bundle.putString("mobile", jsonObject.getString("mobile"));
				}

				if (jsonObject.has("department_value")) {
					bundle.putString("department_value", jsonObject.getString("department_value"));
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return bundle;
	}

	public static Bundle ParserBangkeCount(String json) {
		Bundle bundle = null;
		try {

			JSONObject jsonObject1 = new JSONObject(json);
			bundle = new Bundle();
			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			if (jsonObject1.has("result")) {
				JSONObject jsonObject = new JSONObject(jsonObject1.getString("result"));

				if (jsonObject.has("count")) {
					bundle.putString("count", jsonObject.getString("count"));
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return bundle;
	}

	public static Bundle ParserBangkelist(String json, String standard_count, String suite_count, String status) {
		Bundle bundle = null;
		HashMap<String, String> hMap = null;
		JSONObject optJSONObject = null;
		try {
			bundle = new Bundle();
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			JSONArray optJSONArray = new JSONArray(json);
			int popnum = optJSONArray.length();
			int biaojiaNnum = 0;
			int taojianNum = 0;
			if (popnum != 0) {
				biaojiaNnum = Integer.parseInt(standard_count) / popnum;
				taojianNum = Integer.parseInt(suite_count) / popnum;
			} else {
				biaojiaNnum = 0;
				taojianNum = 0;
			}

			for (int i = 0; i < optJSONArray.length(); i++) {
				hMap = new HashMap<String, String>();
				optJSONObject = optJSONArray.optJSONObject(i);
				if (optJSONObject.has("id")) {
					hMap.put("id", optJSONObject.optString("id"));

				}
				if (optJSONObject.has("userid")) {
					hMap.put("userid", optJSONObject.optString("userid"));

				}

				if (optJSONObject.has("realname")) {
					hMap.put("realname", optJSONObject.optString("realname"));

				}
				if (optJSONObject.has("portrait")) {
					hMap.put("portrait", optJSONObject.optString("portrait"));

				}
				if ("4".equals(status)) {

					if (optJSONObject.has("standard_count")) {
						hMap.put("biaojiaNnum", optJSONObject.optString("standard_count"));

					}
					if (optJSONObject.has("suite_count")) {
						hMap.put("taojianNum", optJSONObject.optString("suite_count"));

					}
					if (optJSONObject.has("bounus")) {
						hMap.put("dashangNum", optJSONObject.optString("bounus"));

					}
					data.add(hMap);
				} else {
					hMap.put("biaojiaNnum", biaojiaNnum + "");
					hMap.put("taojianNum", taojianNum + "");
					hMap.put("dashangNum", "0");
					data.add(hMap);
				}

			}
			bundle.putSerializable("list", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	public static Bundle ParserBangkelistPingjia(String json) {
		Bundle bundle = null;
		HashMap<String, String> hMap = null;
		JSONObject optJSONObject = null;
		try {
			bundle = new Bundle();
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			JSONArray optJSONArray = new JSONArray(json);
			for (int i = 0; i < optJSONArray.length(); i++) {
				hMap = new HashMap<String, String>();
				optJSONObject = optJSONArray.optJSONObject(i);
				if (optJSONObject.has("id")) {
					hMap.put("id", optJSONObject.optString("id"));

				}
				if (optJSONObject.has("userid")) {
					hMap.put("userid", optJSONObject.optString("userid"));

				}

				if (optJSONObject.has("realname")) {
					hMap.put("realname", optJSONObject.optString("realname"));

				}
				if (optJSONObject.has("portrait")) {
					hMap.put("portrait", optJSONObject.optString("portrait"));

				}

				if (optJSONObject.has("isShoucang")) {
					hMap.put("isShoucang", optJSONObject.optString("isShoucang"));

				} else {
					hMap.put("isShoucang", "0");
				}
				if (optJSONObject.has("Pingfen")) {
					hMap.put("Pingfen", optJSONObject.optString("Pingfen"));

				} else {
					hMap.put("Pingfen", "0");
				}
				data.add(hMap);
			}
			bundle.putSerializable("list", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	public static Bundle ParserCollectionB(String json) {
		JSONObject jsonObject1 = null;
		Bundle bundle = null;
		HashMap<String, String> hMap = null;
		JSONObject optJSONObject = null;
		try {
			jsonObject1 = new JSONObject(json);
			bundle = new Bundle();

			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			if (jsonObject1.has("result")) {

				if (jsonObject1.has("result")) {
					JSONObject jsonObject = new JSONObject(jsonObject1.getString("result"));

					if (jsonObject.has("data")) {
						// jsonObject.getString("data") ;

						JSONArray optJSONArray = jsonObject.optJSONArray("data");

						for (int i = 0; i < optJSONArray.length(); i++) {
							hMap = new HashMap<String, String>();
							optJSONObject = optJSONArray.optJSONObject(i);

							if (optJSONObject.has("num")) {
								hMap.put("num", optJSONObject.optString("num"));
							}
							if (optJSONObject.has("userid")) {
								hMap.put("userid", optJSONObject.optString("userid"));
							}
							if (optJSONObject.has("longitude")) {
								hMap.put("longitude", optJSONObject.optString("longitude"));
							}
							if (optJSONObject.has("latitude")) {
								hMap.put("latitude", optJSONObject.optString("latitude"));
							}
							if (optJSONObject.has("realname")) {
								hMap.put("realname", optJSONObject.optString("realname"));
							}
							if (optJSONObject.has("starlevel")) {
								hMap.put("starlevel", optJSONObject.optString("starlevel"));
							}
							if (optJSONObject.has("portrait")) {
								hMap.put("portrait", optJSONObject.optString("portrait"));
							}
							hMap.put("isxuanzhong", "0");
							data.add(hMap);
						}
					}

				}

			}
			bundle.putSerializable("list", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	public static Bundle ParserMyhotel(String json) {
		Bundle bundle = null;
		try {

			JSONObject jsonObject1 = new JSONObject(json);
			bundle = new Bundle();
			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			if (jsonObject1.has("result")) {
				JSONObject jsonObject = new JSONObject(jsonObject1.getString("result"));

				if (jsonObject.has("balance")) {
					bundle.putString("balance", jsonObject.getString("balance"));
				}
				if (jsonObject.has("phone")) {
					bundle.putString("phone", jsonObject.getString("phone"));
				}
				if (jsonObject.has("address")) {
					bundle.putString("address", jsonObject.getString("address"));
				}
				if (jsonObject.has("priority")) {
					bundle.putString("priority", jsonObject.getString("priority"));
				}

				if (jsonObject.has("name")) {
					bundle.putString("name", jsonObject.getString("name"));
				}
				if (jsonObject.has("longitude")) {
					bundle.putString("longitude", jsonObject.getString("longitude"));

				}
				if (jsonObject.has("latitude")) {
					bundle.putString("latitude", jsonObject.getString("latitude"));
				}
				if (jsonObject.has("reviewHotel")) {
					bundle.putString("reviewHotel", jsonObject.getString("reviewHotel"));
				}
				if (jsonObject.has("jifen")) {
					bundle.putString("jifen", jsonObject.getString("jifen"));
				}

				if (jsonObject.has("hotelid")) {
					bundle.putString("hotelid", jsonObject.getString("hotelid"));
				}

				if (jsonObject.has("department")) {
					bundle.putString("department", jsonObject.getString("department"));
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return bundle;
	}

	public static Bundle ParserVersion(String json) {
		Bundle bundle = null;
		try {

			JSONObject jsonObject1 = new JSONObject(json);
			bundle = new Bundle();
			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			if (jsonObject1.has("result")) {
				JSONObject jsonObject = new JSONObject(jsonObject1.getString("result"));

				if (jsonObject.has("message")) {
					bundle.putString("message", jsonObject.getString("message"));
				}
				if (jsonObject.has("downloadurl")) {
					bundle.putString("downloadurl", jsonObject.getString("downloadurl"));
				}
				if (jsonObject.has("forced_upgrade")) {
					bundle.putString("forced_upgrade", jsonObject.getString("forced_upgrade"));
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return bundle;
	}

	public static Bundle ParserStatistics(String json) {
		Bundle bundle = null;
		try {

			JSONObject jsonObject1 = new JSONObject(json);
			bundle = new Bundle();
			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			if (jsonObject1.has("result")) {
				JSONObject jsonObject = new JSONObject(jsonObject1.getString("result"));

				if (jsonObject.has("workercount")) {
					bundle.putString("workercount", jsonObject.getString("workercount"));
				}
				if (jsonObject.has("totalprice")) {
					bundle.putString("totalprice", jsonObject.getString("totalprice"));
				}
				if (jsonObject.has("ordercount")) {
					bundle.putString("ordercount", jsonObject.getString("ordercount"));
				}
				if (jsonObject.has("totalbounus")) {
					bundle.putString("totalbounus", jsonObject.getString("totalbounus"));
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return bundle;
	}

	public static Bundle ParserCheck(String json) {
		Bundle bundle = null;
		try {

			JSONObject jsonObject1 = new JSONObject(json);
			bundle = new Bundle();
			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			if (jsonObject1.has("result")) {
				JSONObject jsonObject = new JSONObject(jsonObject1.getString("result"));

				if (jsonObject.has("totalPrice")) {
					bundle.putString("totalPrice", jsonObject.getString("totalPrice"));
				}
				if (jsonObject.has("isSign")) {
					bundle.putString("isSign", jsonObject.getString("isSign"));
				}
				if (jsonObject.has("iscash")) {
					bundle.putString("iscash", jsonObject.getString("iscash"));
				}
				if (jsonObject.has("enddate")) {
					bundle.putString("enddate", jsonObject.getString("enddate"));
				}
				
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return bundle;
	}

	public static Bundle ParserBangkes(String json) {
		JSONObject jsonObject1 = null;
		Bundle bundle = null;
		HashMap<String, String> hMap = null;
		JSONObject optJSONObject = null;
		try {
			jsonObject1 = new JSONObject(json);
			bundle = new Bundle();

			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			if (jsonObject1.has("result")) {
				JSONArray optJSONArray = jsonObject1.optJSONArray("result");
				for (int i = 0; i < optJSONArray.length(); i++) {
					hMap = new HashMap<String, String>();
					optJSONObject = optJSONArray.optJSONObject(i);
					if (optJSONObject.has("portrait")) {
						hMap.put("portrait", optJSONObject.optString("portrait"));
					}

					if (optJSONObject.has("realname")) {
						hMap.put("realname", optJSONObject.optString("realname"));
					}
					if (optJSONObject.has("earn_money")) {
						hMap.put("earn_money", optJSONObject.optString("earn_money"));
					}

					data.add(hMap);
				}
			}
			bundle.putSerializable("list", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	public static Bundle ParserOrderinfo(String json) {
		Bundle bundle = null;
		try {

			JSONObject jsonObject1 = new JSONObject(json);
			bundle = new Bundle();
			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			if (jsonObject1.has("result")) {
				JSONObject jsonObject = new JSONObject(jsonObject1.getString("result"));

				if (jsonObject.has("orderInfo")) {
					bundle.putString("orderInfo", jsonObject.getString("orderInfo"));
				}

				if (jsonObject.has("orderno")) {
					bundle.putString("orderno", jsonObject.getString("orderno"));
				}

				if (jsonObject.has("notify_url")) {
					bundle.putString("notify_url", jsonObject.getString("notify_url"));
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return bundle;
	}

	public static Bundle ParserPaylistB(String json) {
		JSONObject jsonObject1 = null;
		Bundle bundle = null;
		HashMap<String, String> hMap = null;
		JSONObject optJSONObject = null;
		try {
			jsonObject1 = new JSONObject(json);
			bundle = new Bundle();

			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			if (jsonObject1.has("result")) {

				if (jsonObject1.has("result")) {
					JSONObject jsonObject = new JSONObject(jsonObject1.getString("result"));

					if (jsonObject.has("data")) {
						JSONArray optJSONArray = jsonObject.optJSONArray("data");
						for (int i = 0; i < optJSONArray.length(); i++) {
							hMap = new HashMap<String, String>();
							optJSONObject = optJSONArray.optJSONObject(i);

							if (optJSONObject.has("id")) {
								hMap.put("id", optJSONObject.optString("id"));
							}

							if (optJSONObject.has("bangkesname")) {
								hMap.put("bangkesname", optJSONObject.optString("bangkesname"));
							}

							if (optJSONObject.has("paytype")) {
								hMap.put("paytype", optJSONObject.optString("paytype"));
							}
							if (optJSONObject.has("paytime")) {
								hMap.put("paytime", optJSONObject.optString("paytime"));
							}
							if (optJSONObject.has("paymoney")) {
								hMap.put("paymoney", optJSONObject.optString("paymoney"));
							}
							if (optJSONObject.has("orderno")) {
								hMap.put("orderno", optJSONObject.optString("orderno"));
							}
							if (optJSONObject.has("realname")) {
								hMap.put("realname", optJSONObject.optString("realname"));
							}

							data.add(hMap);
						}
					}

				}

			}
			bundle.putSerializable("list", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	public static Bundle ParserMessagelistB(String json) {
		JSONObject jsonObject1 = null;
		Bundle bundle = null;
		HashMap<String, String> hMap = null;
		JSONObject optJSONObject = null;
		try {
			jsonObject1 = new JSONObject(json);
			bundle = new Bundle();

			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			if (jsonObject1.has("result")) {

				if (jsonObject1.has("result")) {
					JSONObject jsonObject = new JSONObject(jsonObject1.getString("result"));

					if (jsonObject.has("data")) {
						JSONArray optJSONArray = jsonObject.optJSONArray("data");
						for (int i = 0; i < optJSONArray.length(); i++) {
							hMap = new HashMap<String, String>();
							optJSONObject = optJSONArray.optJSONObject(i);

							if (optJSONObject.has("content")) {
								hMap.put("content", optJSONObject.optString("content"));
							}
							if (optJSONObject.has("sendtime")) {
								hMap.put("sendtime", optJSONObject.optString("sendtime"));
							}
							if (optJSONObject.has("type")) {
								hMap.put("type", optJSONObject.optString("type"));
							}
							if (optJSONObject.has("url")) {
								hMap.put("url", optJSONObject.optString("url"));
							}
							if (optJSONObject.has("imgurl")) {
								hMap.put("imgurl", optJSONObject.optString("imgurl"));
							}
							if (optJSONObject.has("title")) {
								hMap.put("title", optJSONObject.optString("title"));
							}

							data.add(hMap);
						}
					}

				}

			}
			bundle.putSerializable("list", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bundle;
	}

	public static Bundle ParserpriceB(String json) {
		Bundle bundle = null;
		try {

			JSONObject jsonObject1 = new JSONObject(json);
			bundle = new Bundle();
			if (jsonObject1.has("msg")) {
				bundle.putString("msg", jsonObject1.getString("msg"));
			}
			if (jsonObject1.has("result")) {
				JSONObject jsonObject = new JSONObject(jsonObject1.getString("result"));

				if (jsonObject.has("min_standard_price")) {
					bundle.putString("min_standard_price", jsonObject.getString("min_standard_price"));
				}

				if (jsonObject.has("min_suite_price")) {
					bundle.putString("min_suite_price", jsonObject.getString("min_suite_price"));
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return bundle;
	}

	public static Bundle JGlJSONC(String json) {
		Bundle bundle = null;
		try {
			bundle = new Bundle();
			if (json == null) {

				return bundle = null;
			}
			JSONObject jsonObject2 = null;
			JSONObject jsonObject3 = new JSONObject(json);
			// JSONObject jsonObject2= new JSONObject(json);
			if (jsonObject3.has("json")) {
				jsonObject2 = new JSONObject(jsonObject3.getString("json"));
			}
			if (jsonObject2.has("orderid")) {
				bundle.putString("orderid", jsonObject2.getString("orderid"));
			}

			if (jsonObject2.has("type")) {
				bundle.putString("type", jsonObject2.getString("type"));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bundle;

	}
}
