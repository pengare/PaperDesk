package hml.paperdeskandroid;

import java.util.HashMap;
import java.util.Map;

import android.R.bool;
import android.R.integer;
import android.R.raw;

public class Task1Service {

	enum Zone
	{
		Hot,
		Warm,
		Cold
	}
	public static Zone iCurrentZone = Zone.Cold;
	
	//view doc, open first doc, first chapter, first page by default
	public static int selectedBook = 0;
	public static int selectedChapter = 0;
	public static int selectedPage = 0;
	
	//for id 0 device
	public static int selectedBookId0 = 0;
	public static int selectedChapterId0 = 0;
	public static int selectedPageId0 = 0;
	public static Zone iCurrentZoneId0 = Zone.Cold;
	
	public static boolean bCollocate = false;
	
	//all the bookcover image
	public static int[] bookcoverImageList = new int[]{
		R.drawable.album_book1, 
		R.drawable.album_book2, 
		R.drawable.album_book3, 
		R.drawable.album_book4, 
		R.drawable.album_book5, 
	};
	
	
	//all the chapter list
	final static String[] Book1Chapter = new String[]  // A Tale of Two Cities
	{
		"Chapter 1: The Period",
		"Chapter 2: The Mail",
		"Chapter 3: The Night Shadows",
		"Chapter 4: The Preparation",
		"Chapter 5: The Wine-shop"
	};
	
	final static String[] Book2Chpater = new String[] //Huckleberry Finn
	{
		"Chapter 1",
		"Chapter 2",
		"Chapter 3",
		"Chapter 4",
		"Chapter 5",
	};
	
	final static String[] Book3Chpater = new String[] //Just William
	{
		"Chapter 1: WILLIAM GOES TO THE PICTURES",
		"Chapter 2: WILLIAM THE INTRUDER",
		"Chapter 3: WILLIAM BELOW STAIRS",
		"Chapter 4: THE FALL OF THE IDOL",
		"Chapter 5: THE SHOW",
	};
	
	final static String[] Book4Chpater = new String[] //Secret Adversary
	{
		"Chapter 1: THE YOUNG ADVENTURERS, LTD",
		"Chapter 2: MR. WHITTINGTON'S OFFER",
		"Chapter 3: A SET BACK",
		"Chapter 4: WHO IS JANE FINN?",
		"Chapter 5: MR. JULIUS P.HERSHEIMMER",
	};
	
	final static String[] Book5Chpater = new String[] //Siddhartha
	{
		"Chapter 1: THE SON OF THE BRAHMAN",
		"Chapter 2: WITH THE SAMANAS",
		"Chapter 3: GOTAMA",
		"Chapter 4: AWAKENING",
		"Chapter 5: KAMALA",
	};
	
	//All pages per book
	final static int[] Book1Page = new int[]
	{
		R.drawable.book1_1,
		R.drawable.book1_2,
		R.drawable.book1_3,
		R.drawable.book1_4,
		R.drawable.book1_5,
		R.drawable.book1_6,
		R.drawable.book1_7,
		R.drawable.book1_8,
		R.drawable.book1_9,
		R.drawable.book1_10,
		R.drawable.book1_11,
		R.drawable.book1_12,
		R.drawable.book1_13,
		R.drawable.book1_14,
		R.drawable.book1_15,
		
	};
	
	final static int[] Book2Page = new int[]
	{
		R.drawable.book2_1,
		R.drawable.book2_2,
		R.drawable.book2_3,
		R.drawable.book2_4,
		R.drawable.book2_5,
		R.drawable.book2_6,
		R.drawable.book2_7,
		R.drawable.book2_8,
		R.drawable.book2_9,
		R.drawable.book2_10,
		R.drawable.book2_11,
		R.drawable.book2_12,
		R.drawable.book2_13,
		R.drawable.book2_14,
		R.drawable.book2_15,
	};
	
	final static int[] Book3Page = new int[]
	{
		R.drawable.book3_1,
		R.drawable.book3_2,
		R.drawable.book3_3,
		R.drawable.book3_4,
		R.drawable.book3_5,
		R.drawable.book3_6,
		R.drawable.book3_7,
		R.drawable.book3_8,
		R.drawable.book3_9,
		R.drawable.book3_10,
		R.drawable.book3_11,
		R.drawable.book3_12,
		R.drawable.book3_13,
		R.drawable.book3_14,
		R.drawable.book3_15,
	};
	
	final static int[] Book4Page = new int[]
	{
		R.drawable.book4_1,
		R.drawable.book4_2,
		R.drawable.book4_3,
		R.drawable.book4_4,
		R.drawable.book4_5,
		R.drawable.book4_6,
		R.drawable.book4_7,
		R.drawable.book4_8,
		R.drawable.book4_9,
		R.drawable.book4_10,
		R.drawable.book4_11,
		R.drawable.book4_12,
		R.drawable.book4_13,
		R.drawable.book4_14,
		R.drawable.book4_15,
	};
	
	final static int[] Book5Page = new int[]
	{
		R.drawable.book5_1,
		R.drawable.book5_2,
		R.drawable.book5_3,
		R.drawable.book5_4,
		R.drawable.book5_5,
		R.drawable.book5_6,
		R.drawable.book5_7,
		R.drawable.book5_8,
		R.drawable.book5_9,
		R.drawable.book5_10,
		R.drawable.book5_11,
		R.drawable.book5_12,
		R.drawable.book5_13,
		R.drawable.book5_14,
		R.drawable.book5_15,
	};
	
	static Map ChapterMap = new HashMap();
	
	public Task1Service() {
		ChapterMap.put("1", "0");
		ChapterMap.put("2", "3");
		ChapterMap.put("3", "6");
		ChapterMap.put("4", "9");
		ChapterMap.put("5", "12");
		
	}
}
