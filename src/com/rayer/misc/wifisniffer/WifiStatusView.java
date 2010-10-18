package com.rayer.misc.wifisniffer;

import com.she.util.dev.ClassDebugger;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class WifiStatusView extends RelativeLayout {

	TextView mSSID_tv;
	TextView mNetID_tv;
	TextView mStrength_tv;
	
	String mStatusDbgString;
	
	public WifiStatusView(ScanResult status, Context context) {
		super(context);
		
		LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.wifi_status_view, this);
		
		mSSID_tv = (TextView) findViewById(R.id.wifi_status_ssid_tv);
		mNetID_tv = (TextView) findViewById(R.id.wifi_status_netid_tv);
		mStrength_tv = (TextView) findViewById(R.id.wifi_status_strength_tv);
		
		mSSID_tv.setText(status.SSID);
		mNetID_tv.setText(status.BSSID);
		mStrength_tv.setText("" + status.level);
		
		mStatusDbgString = ClassDebugger.getClassInfo(ScanResult.class, status);
	}

}
