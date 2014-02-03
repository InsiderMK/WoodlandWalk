package com.insidermk.woodlandwalk;

import android.os.Bundle;
import android.app.Activity;

public class MainActivity extends Activity {
    static final String TAG = "WoodlandWalk";
    private MapView map = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		map = new MapView(this);
		setContentView(map);
	}
	
	protected void onDestroy() {
		super.onDestroy();
		
		map.Destroy();
		map = null;
	}
}
