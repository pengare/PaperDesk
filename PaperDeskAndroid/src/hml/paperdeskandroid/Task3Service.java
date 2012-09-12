package hml.paperdeskandroid;

import android.R.integer;

public class Task3Service {

	public static int selectedEmail = 0;
	
	public static boolean bNewEmailComing = false;
	enum Zone
	{
		Hot,
		Warm,
		Cold
	}
	public static Zone iCurrentZone = Zone.Cold;
	
	//show in email list
	public static int emailList = R.drawable.animal_album;
	
	//show in email detail page
	public static int[] emails = new int[]
	{
		R.drawable.animal_photo_1   //Todo: update email detail
	};
	
	
	//for Id1 email index
	public static int selectedEmailId1 = 0;
}
