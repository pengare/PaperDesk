package hml.paperdeskandroid;

import java.util.HashMap;
import java.util.Map;

import android.R.integer;


public class TrainingService {
	enum TrainingStage
	{
		Blank,
		ViewDocument,
		ViewPhoto,
		ViewEmail,
		Stack
	}
	
	public static TrainingStage trainingStage;
	
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
	
	
	//all the bookcover image
	public static int[] bookcoverImageList = new int[]{
		R.drawable.a1_android_developer, R.drawable.a2_beginning_python, R.drawable.a3_opengl_es_for_android,
		R.drawable.a4_biology, R.drawable.a5_classical_composer, R.drawable.a6_kingston
	};
	
	
	//all the chapter list
	final static String[] AndroidChapter = new String[]
	{
		"Chapter 1: Creating an Android Project",
		"Chapter 2: Running Your App",
		"Chapter 3: Building a Simple User Interface",
		"Chapter 4: Starting Another Activity",
		"Chapter 5: Pausing and Resuming an Activity"
	};
	
	final static String[] PythonChapter = new String[]
	{
		"Chapter 1: Whetting Your Appetite",
		"Chapter 2: Using the Python Interpreter",
		"Chapter 3: An Informal Introduction to Python",
		"Chapter 4: More Control Flow Tools",
		"Chapter 5: Data Structures",
		"Chapter 6: Modules",
		"Chapter 7: Input and Output"
	};
	
	//all the pages and chapter-page map
	final static int[] AndroidPage = new int[]
	{
		R.drawable.art_photo_1,
		R.drawable.art_photo_2,
		R.drawable.art_photo_3,
		R.drawable.art_photo_4,
		R.drawable.art_photo_5,
		R.drawable.art_photo_6,
		R.drawable.art_photo_7,
		R.drawable.art_photo_8,
		R.drawable.art_photo_9
	};
	
	static Map AndroidChapterMap = new HashMap();
	
	public TrainingService() {
		AndroidChapterMap.put("1", "1");
		AndroidChapterMap.put("2", "3");
		AndroidChapterMap.put("3", "5");
		AndroidChapterMap.put("4", "7");
		AndroidChapterMap.put("5", "9");
		
	}
	
	
	
	

}
