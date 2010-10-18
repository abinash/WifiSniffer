package com.rayer.misc.wifisniffer;

import java.util.List;

import com.rayer.util.wifi.WifiService;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class WifiSniffer extends Activity implements WifiService.WifiUpdateListener {


	
	private static final int SCAN_RESULT_GET = 0;

	WifiService mService;
	
	Handler mMainHandler = new Handler(){

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == SCAN_RESULT_GET) {
				List<ScanResult> resultList = (List<ScanResult>) msg.obj;
				mWifiList_lv.setAdapter(new WifiResultAdapter(resultList));
				Log.d("wifisniffer", "getting result...");
				return;
			}
			super.handleMessage(msg);
		}
	};
	
	TextView mStatus_tv;
	Button mRefresh_btn;
	ListView mWifiList_lv;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mStatus_tv = (TextView) findViewById(R.id.status_tv);
        mRefresh_btn = (Button) findViewById(R.id.refresh_btn);
        mWifiList_lv = (ListView) findViewById(R.id.wifi_list);
        
        mService = new WifiService(this);
        mRefresh_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
		        mService.startService(WifiSniffer.this);	
			}});
    }
    
	@Override
	public void OnStartScan() {
		mStatus_tv.setText("Scanning....");
	}
	@Override
	public boolean OnRequestWifiOn() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public void OnGetScanResult(List<ScanResult> resultList) {
		Log.d("wifisniffer", "get result message!");

		Message msg = mMainHandler.obtainMessage();
		msg.what = SCAN_RESULT_GET;
		msg.obj = resultList;
		
		mMainHandler.sendMessage(msg);
	}
	@Override
	public void OnScanFailed() {
		mStatus_tv.setText("Scanning failed!");
	}
	@Override
	public void OnScanInitializing() {
		
	}
	
	public class WifiResultAdapter extends BaseAdapter {

		List<ScanResult> mList;
		public WifiResultAdapter(List<ScanResult> resultList) {
			mList = resultList;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			WifiStatusView view = new WifiStatusView(mList.get(position), WifiSniffer.this);
			return view;
		}

	}
}