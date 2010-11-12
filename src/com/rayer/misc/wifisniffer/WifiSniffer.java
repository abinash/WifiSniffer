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
	private static final int SCAN_START = 1;
	private static final int SCAN_FAILED = 2;
	private static final int SCAN_INITIALIZING = 3;
	private static final int REQUEST_WIFI_ON = 4;

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
			}else if (msg.what == SCAN_START) {
				mStatus_tv.setText("Scanning....");
			}else if (msg.what == SCAN_FAILED) {
				mStatus_tv.setText("Scanning failed!");
			}else if (msg.what == SCAN_INITIALIZING) {
				mStatus_tv.setText("Initializing scan");
			}else if (msg.what == REQUEST_WIFI_ON) {
				mStatus_tv.setText("Turning on WiFi");
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

			public void onClick(View arg0) {
		        mService.startService(WifiSniffer.this);	
			}});
    }
    
	public void OnStartScan() {
		Message msg = mMainHandler.obtainMessage(SCAN_START);
		mMainHandler.sendMessage(msg);
	}
	public boolean OnRequestWifiOn() {
		Message msg = mMainHandler.obtainMessage(REQUEST_WIFI_ON);
		mMainHandler.sendMessage(msg);
		return true;
	}
	public void OnGetScanResult(List<ScanResult> resultList) {
		Log.d("wifisniffer", "get result message!");

		Message msg = mMainHandler.obtainMessage();
		msg.what = SCAN_RESULT_GET;
		msg.obj = resultList;
		
		mMainHandler.sendMessage(msg);
	}
	public void OnScanFailed() {
		Message msg = mMainHandler.obtainMessage(SCAN_FAILED);
		mMainHandler.sendMessage(msg);
	}
	public void OnScanInitializing() {
		Message msg = mMainHandler.obtainMessage(SCAN_INITIALIZING);
		mMainHandler.sendMessage(msg);
	}
	
	public class WifiResultAdapter extends BaseAdapter {

		List<ScanResult> mList;
		public WifiResultAdapter(List<ScanResult> resultList) {
			mList = resultList;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			WifiStatusView view = new WifiStatusView(mList.get(position), WifiSniffer.this);
			return view;
		}

	}
}