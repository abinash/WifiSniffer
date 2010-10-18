package com.rayer.util.wifi;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiService {

	WifiManager mManager;
	WifiUpdateListener mListener;
	ScanningThread mScanningThread;
	
	
	boolean mIsScanningInProgress = false;
	
	public WifiService(Context context) {
		mManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	}
	
	public boolean startService(WifiUpdateListener listener) {
		Log.d("wifisniffer", "startService start");

		if(mManager.isWifiEnabled() == false) {
			if(mListener == null)
				return false;
				
			if(mListener.OnRequestWifiOn() == false)
				return false;
			
			boolean isSucceed = mManager.setWifiEnabled(true);
			if(isSucceed == false)
				return false;
		}
		Log.d("wifisniffer", "StartService succeed");

		mScanningThread = new ScanningThread();
		mScanningThread.start();
		return true;
	}


	public interface WifiUpdateListener {
		void OnStartScan();
		boolean OnRequestWifiOn();
		void OnGetScanResult(List<ScanResult> resultList);
		void OnScanFailed();
		void OnScanInitializing();
	}
	
	public class ScanningThread extends Thread {

		@Override
		public void run() {
			if(mListener != null)
				mListener.OnStartScan();
			mIsScanningInProgress = true;
			
			if(mManager.startScan() == false)
				if(mListener != null) {
					mListener.OnScanFailed();
					return;
				}
			
			if(mListener != null)
				mListener.OnGetScanResult(mManager.getScanResults());
			
			mIsScanningInProgress = false;
			super.run();
		}
	}
}
