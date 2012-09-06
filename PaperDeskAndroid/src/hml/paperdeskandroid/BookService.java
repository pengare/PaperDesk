package hml.paperdeskandroid;

public class BookService {
	
	//indicator variables
	static int iBookIndex = -1;
	static int iChapterIndex = -1;
	
	static int iCurrentZone = -1;  //0 hot, 1 warm, 2 cold
	
	final static String[] books = new String[]
	{
		"Android Development",
		"Beginning Python",
		"Pro OpenGL ES for Android",
		"Introduction to Biology",
		"Spiritual Lives of the Great Composers",
		"Uncovering Kingston",
		"Annals of Flight",
		"Textbook of Astronomy"
	};
	
	final static String[] AndroidChapter = new String[]
	{
		"Chapter 1: Creating an Android Project",
		"Chapter 2: Running Your App",
		"Chapter 3: Building a Simple User Interface",
		"Chapter 4: Starting Another Activity",
		"Chapter 5: Pausing and Resuming an Activity"
	};
	
	final static String[] AndroidLink = new String[]
	{
		"http://developer.android.com/training/basics/firstapp/creating-project.html",
		"http://developer.android.com/training/basics/firstapp/running-app.html",
		"http://developer.android.com/training/basics/firstapp/building-ui.html",
		"http://developer.android.com/training/basics/firstapp/starting-activity.html",
		"http://developer.android.com/training/basics/activity-lifecycle/pausing.html"
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
	
	final static String[] PythonLink = new String[]
	{
		"http://docs.python.org/py3k/tutorial/appetite.html",
		"http://docs.python.org/py3k/tutorial/interpreter.html",
		"http://docs.python.org/py3k/tutorial/introduction.html",
		"http://docs.python.org/py3k/tutorial/controlflow.html",
		"http://docs.python.org/py3k/tutorial/datastructures.html",
		"http://docs.python.org/py3k/tutorial/modules.html",
		"http://docs.python.org/py3k/tutorial/inputoutput.html"
	};

}
