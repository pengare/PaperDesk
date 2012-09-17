package hml.paperdeskandroid;

import android.R.bool;
import android.R.integer;

public class Task2Service {

	enum Zone
	{
		Hot,
		Warm,
		Cold
	}
	public static Zone iCurrentZone = Zone.Warm;
	
	public static int iSelectedAlbum = 0;  // 0 first album, 1 second album

	public static int[] album1 = new int[]
	{
		R.drawable.album1_1,
		R.drawable.album1_2,
		R.drawable.album1_3,
		R.drawable.album1_4,
		R.drawable.album1_5,
		R.drawable.album1_6,
	};
	
	public static int[] album2 = new int[]
	{
		R.drawable.album2_1,
		R.drawable.album2_2,
		R.drawable.album2_3,
		R.drawable.album2_4,
		R.drawable.album2_5,
		R.drawable.album2_6,
		R.drawable.album2_7,
	};
	public static int selectedPhoto = 0;
	
	public static Boolean bStartMoving = false;
	
}
