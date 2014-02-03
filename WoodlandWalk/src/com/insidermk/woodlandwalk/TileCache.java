package com.insidermk.woodlandwalk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Pair;


public class TileCache {
    private static String MAP_ROOT;
    private Context context;

    StringBuilder pathToTile = new StringBuilder();
    HashMap<Pair<Integer, Integer>, byte[]> tileCache = new HashMap<Pair<Integer, Integer>, byte[]>();
    int totlaBytesCached = 0;

    public int getCachedTiles()
    {
        return tileCache.size();
    }

    public int getTotalBytesCached()
    {
        return totlaBytesCached;
    }
    
    public TileCache(Context c) {
    	context = c;
    	
    	MAP_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
    	MAP_ROOT += "/maps/sat/";
    }

    public Bitmap GetTile(int tileX, int tileY, int zoom)
    {
        Pair<Integer, Integer> tile = new Pair<Integer, Integer>(tileX, tileY);
        byte[] tileBytes = null;

        if (!tileCache.containsKey(tile)) {
        	File tileFile = new File(GetTileName(tileX, tileY, zoom));
    		FileInputStream fin = null;
    		try {
    			fin = new FileInputStream(tileFile);

    			tileBytes = new byte[(int)tileFile.length()];
    			
    			fin.read(tileBytes);
    			tileCache.put(tile, tileBytes);
                totlaBytesCached += tileBytes.length;
    		}
    		catch (FileNotFoundException e) {
    			tileBytes = null;
    		}
    		catch (IOException ioe) {
    			tileBytes = null;
    		}
    		finally {
    			try {
    				if (fin != null) {
    					fin.close();
    				}
    			}
    			catch (IOException ioe) {
    				tileBytes = null;
    			}
    		}        	
        }
        else {
        	tileBytes = tileCache.get(tile);
        }
        
        if (tileBytes != null) {
        	return BitmapFactory.decodeByteArray(tileBytes, 0, tileBytes.length); 
        }
        else {
        	return BitmapFactory.decodeResource(context.getResources(), R.drawable.emptytile);
        }
    }

    public void ClearCache()
    {
        tileCache.clear();
        totlaBytesCached = 0;
    }

    private String GetTileName(int tileX, int tileY, int zoom)
    {
        int fractionX = tileX / 1024;
        int fractionY = tileY / 1024;

        pathToTile.setLength(0);
        pathToTile.append(MAP_ROOT);
        pathToTile.append("z");
        pathToTile.append(zoom + 1);
        pathToTile.append('/');
        pathToTile.append(fractionX);
        pathToTile.append('/');
        pathToTile.append("x");
        pathToTile.append(tileX);
        pathToTile.append('/');
        pathToTile.append(fractionY);
        pathToTile.append('/');
        pathToTile.append("y");
        pathToTile.append(tileY);
        pathToTile.append(".jpg");

        return pathToTile.toString();
    }

}
