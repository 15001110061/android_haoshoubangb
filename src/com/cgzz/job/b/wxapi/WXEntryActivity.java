package com.cgzz.job.b.wxapi;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

public class WXEntryActivity extends WXCallbackActivity {
@Override
public void onReq(BaseReq req) {
	// TODO Auto-generated method stub
	super.onReq(req);
}

@Override
	public void onResp(BaseResp resp) {
		// TODO Auto-generated method stub
		super.onResp(resp);
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			// ����ɹ�
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			// ����ȡ��
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			// ����ܾ�
			break;
		}
	}
}
