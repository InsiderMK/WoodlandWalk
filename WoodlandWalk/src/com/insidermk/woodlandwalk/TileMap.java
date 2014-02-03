package com.insidermk.woodlandwalk;

import android.graphics.Point;
import android.location.Location;

public class TileMap {
    Location locationGeo = null;

    MapPoint locationOnMap = new MapPoint();
    MapWindow visibleTiles = new MapWindow();
    Point tileShift = new Point();

    public TileMap()
    {
        visibleTiles.isInit = false;
    }

    public void CenterMapToLocation(Location location, int zoom)
    {
    	if (location == null) {
    		return;
    	}

    	locationGeo = location;

        int[] map = Mercator.LatLongToPixelXY(location.getLatitude(), location.getLongitude(), zoom);

        locationOnMap.x = map[0];
        locationOnMap.y = map[1];

        visibleTiles.isInit = false;
    }

    public void ShiftMap(int shiftX, int shiftY, int zoom)
    {
    	if (locationGeo == null) {
    		return;
    	}
    		
        locationOnMap.x += shiftX;
        locationOnMap.y += shiftY;

        double[] geo = Mercator.PixelXYToLatLong(locationOnMap.x, locationOnMap.y, zoom);

        locationGeo.setLatitude(geo[0]);
        locationGeo.setLongitude(geo[1]);

        visibleTiles.isInit = false;
    }

    public void CalculateMapWindow(int w, int h, int zoom)
    {
        CalculateMapWindow(w, h, zoom, 0, 0);
    }

    public void CalculateMapWindow(int w, int h, int zoom, int shiftX, int shiftY)
    {
    	if (locationGeo == null || h == 0 || w == 0) {
    		return;
    	}
    	
        int topLeftX = locationOnMap.x + shiftX - w / 2;
        int topLeftY = locationOnMap.y + shiftY - h / 2;

        tileShift.x = -(topLeftX % 256);
        tileShift.y = -(topLeftY % 256);

        visibleTiles.firstVisibleX = Math.max(topLeftX / 256, 0);
        visibleTiles.firstVisibleY = Math.max(topLeftY / 256, 0);

        int bottomRightX = topLeftX + w;
        int bottomRightY = topLeftY + h;

        visibleTiles.lastVisibleX = Math.min((bottomRightX / 256), Mercator.MapMaxTile(zoom));
        visibleTiles.lastVisibleY = Math.min((bottomRightY / 256), Mercator.MapMaxTile(zoom));

        visibleTiles.isInit = true;
    }

}
