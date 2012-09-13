package hml.paperdeskandroid;

import android.R.integer;

public class Task3Service {

	public static int selectedEmail = 0;
	
	public static boolean bNewEmailComing = false;
	public static boolean bEmailReplySent = false;
	
	public static int attachmentResId = R.drawable.attachment;
	enum AttachmentStatus
	{
		Blank,
		Attach,
		Move
	}
	
	public static AttachmentStatus attachmentStatus = AttachmentStatus.Blank;
	
	enum Zone
	{
		Hot,
		Warm,
		Cold
	}
	public static Zone iCurrentZone = Zone.Cold;
	
	//show in email list
	public static int emailList = R.drawable.inbox1;
	
	//show in email detail page
	public static int[] emails = new int[]
	{
		R.drawable.email2,
		R.drawable.email3,
		R.drawable.email4,
		R.drawable.email5,
		R.drawable.email6
	};
	
	public static int[] emailsNewComing = new int[]
	{
		R.drawable.email1,
		R.drawable.email2,
		R.drawable.email3,
		R.drawable.email4,
		R.drawable.email5,
		R.drawable.email6
	};
	
	
	//for Id1 email index
	public static int selectedEmailId1 = 0;
}
