package hml.paperdeskandroid;

public class EmailService {
	static int iFolderIndex = 0;  //inbox default
	static int iEmailDetailIndex = -1;
	
	static int iCurrentZone = -1;  //0 hot, 1 warm, 2 cold
	
	static String[] emailFolderList = new String[]
	{
		"Inbox                     12",
		"Priority Inbox            5",
		"Important                 2",
		"Sent",
		"Spam"
	};
	
	static String[] inboxEmailList = new String[]
	{
		"New on iTunes: Stream the New Dave Matthews Band, iTunes Festival \n >> iTune",
		"Hi When will You Come to Lab? \n >> Aneesh Pt",
		"Frosh Week! \n Queen's University"
	};
	
	static String[] priorityInboxEmailList = new String[]
	{
		"Important: Stream the New Dave Matthews Band, iTunes Festival \n >> iTune",
		"Important: Hi When will You Come to Lab? \n >> Aneesh Pt",
		"Important: Frosh Week! \n Queen's University"
	};

}
