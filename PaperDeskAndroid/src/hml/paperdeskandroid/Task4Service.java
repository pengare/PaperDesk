package hml.paperdeskandroid;

import android.R.integer;

import com.google.android.maps.GeoPoint;

public class Task4Service {

	public static boolean bCollocate = false;
	
	//geo center of display, set by thread that receives msg from master, and used by UI thread to update mapview
	public static int slaveMapCenterLong = 0;
	public static int slaveMapCenterLat = 0;
	public static int slaveMapZoomLevel = 1;
	
	public static GeoPoint KingstonStore1;
	public static GeoPoint KingstonStore2;
	
	public static GeoPoint OttawaStore1;
	public static GeoPoint OttawaStore2;
	
	public static GeoPoint MontrealStore1;
	public static GeoPoint MontrealStore2;
	public static GeoPoint MontrealStore3;
	
	public static int xOffset = 0; //x distance from slave to master map
	public static int yOffset = 0;
	
}
