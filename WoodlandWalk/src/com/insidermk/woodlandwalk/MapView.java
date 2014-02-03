package com.insidermk.woodlandwalk;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MapView extends View implements OnTouchListener, LocationListener {
    static final String TAG = "WoodlandWalk";
    private int zoom = 16;

    private TileMap map = new TileMap();
    private TileCache tileCache = new TileCache(getContext());
    private GpsService gps = new GpsService(getContext(), this);

    HashMap<Pair<Integer, Integer>, Bitmap> imgCache = new HashMap<Pair<Integer, Integer>, Bitmap>();

	int touchX;
	int touchY;

    public MapView(Context context) {
        super(context);
        
        setOnTouchListener(this);

        init();
    }
    
    private void init() {
    }
    
    private void UpdateMapLocation(Location loc) {
    	int w = getWidth();
    	int h = getHeight();

    	map.CenterMapToLocation(loc, zoom);
        map.CalculateMapWindow(w, h, zoom);

        invalidate();
    }
    
    private void UpdateMapLocation(int shiftX, int shiftY) {
    	int w = getWidth();
    	int h = getHeight();

    	map.ShiftMap(shiftX, shiftY, zoom);
		map.CalculateMapWindow(w, h, zoom);

		invalidate();
    }

    private void UpdateMapSize(int w, int h) {
    	Location loc = gps.getLocation();

    	map.CenterMapToLocation(loc, zoom);
        map.CalculateMapWindow(w, h, zoom);

        invalidate();
    }
    
    private void ShiftMap(int shiftX, int shiftY) {
    	int w = getWidth();
    	int h = getHeight();
    	
		map.CalculateMapWindow(w, h, zoom, shiftX, shiftY);

		invalidate();
    }

	@Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
		UpdateMapSize(w, h);
    }

	@Override
    protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
    	if (map.visibleTiles.isInit)
		{
    		int offsetX = map.tileShift.x;
        	
			for (int i = map.visibleTiles.firstVisibleX; i <= map.visibleTiles.lastVisibleX; i++)
			{
				int offsetY = map.tileShift.y;
				for (int j = map.visibleTiles.firstVisibleY; j <= map.visibleTiles.lastVisibleY; j++)
				{
				    Pair<Integer, Integer> imgKey = new Pair<Integer, Integer>(i, j);
				    
					if (!imgCache.containsKey(imgKey)) {
						imgCache.put(imgKey, tileCache.GetTile(i, j, zoom));
					}
					canvas.drawBitmap(imgCache.get(imgKey), offsetX, offsetY, null);
					offsetY += 256;
				}
				offsetX += 256;
			}
		}
    }
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touchX = (int)event.getX();
			touchY = (int)event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			ShiftMap(touchX - (int)event.getX(), touchY - (int)event.getY());
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			UpdateMapLocation(touchX - (int)event.getX(), touchY - (int)event.getY());
			break;
		}
		return true;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
	
	@Override
	public void onProviderEnabled(String provider) {
	}
	
	@Override
	public void onProviderDisabled(String provider) {
	}
	
	@Override
	public void onLocationChanged(Location location) {
		UpdateMapLocation(location);
	}
	
	public void Destroy() {
		gps.Destroy();
	}
}
