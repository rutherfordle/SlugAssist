package slug.project.com;

// database class
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database {
	//Global Variables
	String GET_ROW_ID;
	String GET_TITLE;
	
	/* event data */
	public static final String KEY_ROWID1 = "_id";
	public static final String KEY_DATE = "selected_date";
	public static final String KEY_TITLE = "title_of_event";
	public static final String KEY_DESCRIPTION = "description_of_event";
	public static final String KEY_START_TIME = "start_time";
	public static final String KEY_END_TIME = "end_time";
	public static final String KEY_REF = "ref_key";

	/*user information */
	public static final String KEY_ROWID2 = "_id";
	public static final String KEY_FIRST_NAME = "person_first_name";
	public static final String KEY_LAST_NAME = "person_last_name";
	public static final String KEY_EMAIL = "person_email_address";
	public static final String KEY_USER_NAME = "person_user_name";
	public static final String KEY_PASSWORD = "password";
	
	/*Course information */
	public static final String KEY_ROWID3 = "_id";
	public static final String KEY_COURSE_ID = "course_id";
	public static final String KEY_COURSE_TITLE = "course_title";
	public static final String KEY_COURSE_CNUMBER = "course_cnumber";
	public static final String KEY_COURSE_SUBJECT = "course_subject";
	public static final String KEY_COURSE_DAYS = "course_days";
	public static final String KEY_COURSE_START_TIME = "course_start_time";
	public static final String KEY_COURSE_END_TIME = "course_end_time";
	public static final String KEY_COURSE_PROFESSOR = "course_professor";
	public static final String KEY_COURSE_BUILDING = "course_building";
	public static final String KEY_COURSE_ROOM_NUMBER = "course_room_number";
	
	/* Database stuff */
	private static final String DATABASE_NAME = "test3";
	private static final String DATABASE_TABLE_EVENTS = "event_table";
	private static final String DATABASE_TABLE_USERINFO = "login_table";
	private static final String DATABASE_TABLE_COURSES = "course_table";
	private static final int DATABASE_VERSION = 3;

	private dbHelper ourHelper;
	private Context ourContext;
	private SQLiteDatabase ourDataBase;

	private class dbHelper extends SQLiteOpenHelper {

		public dbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			
			//creating table for events
			db.execSQL("CREATE TABLE " + DATABASE_TABLE_EVENTS + " ("
					+ KEY_ROWID1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
					+ KEY_DATE + " TEXT NOT NULL, " 
					+ KEY_TITLE + " TEXT NOT NULL, "
					+ KEY_DESCRIPTION + " TEXT NOT NULL, "
					+ KEY_START_TIME + " TEXT NOT NULL, " 
					+ KEY_END_TIME + " TEXT NOT NULL," 
					+ KEY_REF + " TEXT NOT NULL);"); 
			
			//Creating table for users
			db.execSQL("CREATE TABLE " + DATABASE_TABLE_USERINFO +" (" +
					KEY_ROWID2 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
					+ KEY_FIRST_NAME + " TEXT NOT NULL, " 
					+ KEY_LAST_NAME + " TEXT NOT NULL," 
					+ KEY_EMAIL + " TEXT NOT NULL," 
					+ KEY_USER_NAME + " TEXT NOT NULL," 
					+ KEY_PASSWORD + " TEXT NOT NULL);"
				);
			
			//Creating table for courses
			db.execSQL("CREATE TABLE " + DATABASE_TABLE_COURSES + " ("
					+ KEY_ROWID3 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
					+ KEY_COURSE_ID + " TEXT NOT NULL, "
					+ KEY_COURSE_TITLE + " TEXT NOT NULL, "
					+ KEY_COURSE_CNUMBER + " TEXT NOT NULL, "
					+ KEY_COURSE_SUBJECT + " TEXT NOT NULL, " 
					+ KEY_COURSE_DAYS + " TEXT NOT NULL, "
					+ KEY_COURSE_START_TIME + " TEXT NOT NULL, "
					+ KEY_COURSE_END_TIME + " TEXT NOT NULL, "
					+ KEY_COURSE_PROFESSOR + " TEXT NOT NULL, "
					+ KEY_COURSE_BUILDING + " TEXT NOT NULL, " 
					+ KEY_COURSE_ROOM_NUMBER + " TEXT NOT NULL, "
					+ "UNIQUE(course_id) ON CONFLICT IGNORE);");
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_EVENTS);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_USERINFO);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_COURSES);
			onCreate(db);
		}
	}

	public Database(Context c) {
		ourContext = c;
	}

	public Database open() throws SQLException {

		ourHelper = new dbHelper(ourContext);
		ourDataBase = ourHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		ourHelper.close();
	}

	//create an entry in events tables
	public void createEntry_in_events(String date, String title, String description,
			String starttime, String endtime, String user) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_DATE, date);
		cv.put(KEY_TITLE, title);
		cv.put(KEY_DESCRIPTION, description);
		cv.put(KEY_START_TIME, starttime);
		cv.put(KEY_END_TIME, endtime);
		cv.put(KEY_REF, user);
		ourDataBase.insert(DATABASE_TABLE_EVENTS, null, cv);
	}
	
	//create an entry in userinfo table
	public void createEntry_in_userinfo(String fname, String lname, String email, String user_name, String pass) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_FIRST_NAME,fname);
		cv.put(KEY_LAST_NAME, lname);
		cv.put(KEY_EMAIL, email);
		cv.put(KEY_USER_NAME, user_name);
		cv.put(KEY_PASSWORD, pass);
		ourDataBase.insert(DATABASE_TABLE_USERINFO, null, cv);
	}
	
	//create an entry in courses_table when finish reading from database
	public void createEntry_in_Courses(String course_idS, String course_titleS, String course_cnumberS,
			String course_subjectS, String course_daysS, String course_start_timeS,
			String course_end_timeS, String course_professorS, String course_buildingS, String course_room_numberS) {
		
			ContentValues cv = new ContentValues();
			cv.put(KEY_COURSE_ID, course_idS);
			cv.put(KEY_COURSE_TITLE, course_titleS);
			cv.put(KEY_COURSE_CNUMBER, course_cnumberS);
			cv.put(KEY_COURSE_SUBJECT, course_subjectS);
			cv.put(KEY_COURSE_DAYS, course_daysS);
			cv.put(KEY_COURSE_START_TIME, course_start_timeS);
			cv.put(KEY_COURSE_END_TIME, course_end_timeS);
			cv.put(KEY_COURSE_PROFESSOR, course_professorS);
			cv.put(KEY_COURSE_BUILDING, course_buildingS);
			cv.put(KEY_COURSE_ROOM_NUMBER, course_room_numberS);
			ourDataBase.insert(DATABASE_TABLE_COURSES, null, cv);
		 
	}

	//check if the user name exists
	public boolean checkUsername(String uName) throws SQLException{
		String[] columns = new String[]{KEY_USER_NAME};
		Cursor c = ourDataBase.query(DATABASE_TABLE_USERINFO, columns, null, null, null, null, null);
		String result = "";
		int iUserName = c.getColumnIndex(KEY_USER_NAME);
		
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result = result + c.getString(iUserName) + "\n";
		}
		//putting the string into string array
		String temp1[] = result.split("\n");
		//converting the string array into list of strings
		List<String> wordList = Arrays.asList(temp1);
		
		if(wordList.contains(uName))
			return true;
		
	return false;
	}
	
	//get the password for specific username
	public String getUserName_password(){
		String[] columns = new String[]{KEY_USER_NAME, KEY_PASSWORD};
		Cursor c = ourDataBase.query(DATABASE_TABLE_USERINFO, columns, null, null, null, null, null);
		String result = "";
				
		int iUserName = c.getColumnIndex(KEY_USER_NAME);
		int ipass = c.getColumnIndex(KEY_PASSWORD);
		
		for(c.moveToFirst(); !c.isAfterLast();c.moveToNext()){
			result = result + c.getString(iUserName)+ " andpassis "+c.getString(ipass) +"\n" ;
		}
		
		return result;
	}

	//get all the events in the database(maybe useful)
	public String getAllEvent() {

		String[] columns = new String[] { KEY_ROWID1, KEY_TITLE,
				KEY_DESCRIPTION, KEY_START_TIME, KEY_END_TIME };
		Cursor c = ourDataBase.query(DATABASE_TABLE_EVENTS, columns, null,
				null, null, null, null);
		String result = "";
		int iRow = c.getColumnIndex(KEY_ROWID1);
		int iTitle = c.getColumnIndex(KEY_TITLE);
		int iDesc = c.getColumnIndex(KEY_DESCRIPTION);
		int istartTime = c.getColumnIndex(KEY_START_TIME);
		int iendTime = c.getColumnIndex(KEY_END_TIME);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = result + "<rowid>" + c.getString(iRow) + "</rowid>" + " "
					+ "<title>" + c.getString(iTitle) + "</title>" + " "
					+ "<desc>" + c.getString(iDesc) + "/desc" + " " + "<stime>"
					+ c.getString(istartTime) + "</stime>" + " " + "<etime>"
					+ c.getString(iendTime) + "</etime>" + "\n";
		}
		return result;
	}

	//get a events based on specific date and username
	public String getOneDayEvents(String date, String username) {
		String[] columns = new String[] { KEY_TITLE, KEY_DESCRIPTION,
				KEY_START_TIME, KEY_END_TIME };
		Cursor c = ourDataBase.query(DATABASE_TABLE_EVENTS, columns, KEY_DATE
				+ " like ? and " + KEY_REF + " like ?", new String[] {date,username}, null, null, null);
		String result = "";

		int iTitle = c.getColumnIndex(KEY_TITLE);
		int iDesc = c.getColumnIndex(KEY_DESCRIPTION);
		int istartTime = c.getColumnIndex(KEY_START_TIME);
		int iendTime = c.getColumnIndex(KEY_END_TIME);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = result + " " + "<title>" + c.getString(iTitle)
					+ "</title>" + " " + "<desc>" + c.getString(iDesc)
					+ "</desc>" + " " + "<stime>" + c.getString(istartTime)
					+ "</stime>" + " " + "<etime>" + c.getString(iendTime)
					+ "</etime>" + "\n";
		}
		return result;
	}
	
	//return true if the course exists in database course_table, else return false
	public boolean Course_exists(String courseID){
		
		String[] columns = new String[]{KEY_COURSE_ID};
		Cursor c = ourDataBase.query(DATABASE_TABLE_COURSES, columns, null, null, null, null, null);
		String result = "";
		int iUserName = c.getColumnIndex(KEY_COURSE_ID);
		
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result = result + c.getString(iUserName) + "\n";
		}
			
		//see if the course is in the string
		//if yes, then get the id of the string and divide by 7 and add 1 since string have 7 spaces
		//set up the global variable for the value of ID
		if(result.contains(courseID)){
			//Log.d("resultString", result );
			int id = result.indexOf(courseID);
			id = id/7 +1;
			GET_ROW_ID = String.valueOf(id);
			//Log.d("id", ""+id);
			//Log.d("GET_ROW_ID",""+GET_ROW_ID);
			return true;
		}	
			
		return false;
	}
	
	//once we got the GET_ROW_ID, we can query it by that value and get all the information needed
	public String getCourse_info(){
		
		String[] columns = new String[] {KEY_COURSE_TITLE, KEY_COURSE_CNUMBER, KEY_COURSE_SUBJECT, KEY_COURSE_DAYS,
				KEY_COURSE_START_TIME, KEY_COURSE_END_TIME, KEY_COURSE_PROFESSOR, KEY_COURSE_BUILDING, KEY_COURSE_ROOM_NUMBER };
		
		Cursor c = ourDataBase.query(DATABASE_TABLE_COURSES, columns, KEY_ROWID3 + " like ?", new String[]{GET_ROW_ID},null, null, null);
		String result = "";
		
		int iCtitle = c.getColumnIndex(KEY_COURSE_TITLE);
		int iCcnumber = c.getColumnIndex(KEY_COURSE_CNUMBER);
		int iCsubject = c.getColumnIndex(KEY_COURSE_SUBJECT);
		int iCdays = c.getColumnIndex(KEY_COURSE_DAYS);
		int iCstart_time = c.getColumnIndex(KEY_COURSE_START_TIME);
		int iCend_time = c.getColumnIndex(KEY_COURSE_END_TIME);
		int iCprofessor = c.getColumnIndex(KEY_COURSE_PROFESSOR);
		int iCbuilding = c.getColumnIndex(KEY_COURSE_BUILDING);
		int iCroom_number= c.getColumnIndex(KEY_COURSE_ROOM_NUMBER);
		
		for(c.moveToFirst(); !c.isAfterLast();c.moveToNext()){
			result = result +c.getString(iCtitle)+ "\t\t"+c.getString(iCcnumber)+"\t\t"+ c.getString(iCsubject) + "\t\t"+
					c.getString(iCdays)+ "\t\t"+ c.getString(iCstart_time)+ "\t\t"+ c.getString(iCend_time) +"\t\t" + 
					c.getString(iCprofessor) + "\t\t"+ c.getString(iCbuilding) + "\t\t" + c.getString(iCroom_number)+ "\n" ;
		}
		//Log.d("hello", result+"test2");
		
		return result;
	}

	//method to check if the course already exists in the EVENTS TABLE database
	//if the course already exists in EVENTS table, return true else return false
	public boolean containCourse_event_table(String title){
		String[] columns = new String[]{KEY_TITLE};
		Cursor c = ourDataBase.query(DATABASE_TABLE_EVENTS, columns, null, null, null, null, null);
		String result = "";
		int iTitle = c.getColumnIndex(KEY_TITLE);
		
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result = result + c.getString(iTitle) + "\n";
		}
		
		if(result.contains(title))
			return true;
		
		return false;
	}
	//same function, double parameters
	public boolean containCourse_event_table(String title, String username){
		String[] columns = new String[]{KEY_TITLE};
		Cursor c = ourDataBase.query(DATABASE_TABLE_EVENTS, columns, KEY_REF + " like ?", new String[]{username}, null, null, null);
		String result = "";
		int iTitle = c.getColumnIndex(KEY_TITLE);
		
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result = result + c.getString(iTitle) + "\n";
		}
		
		if(result.contains(title))
			return true;
		
		return false;
	}
	
	//check if the title exists in the event_table
	public void title_exists_course_table(String cID){
		String result = "";
		//Now check if the course exists in title
		if(Course_exists(cID)){
		String [] columns = new String[]{KEY_COURSE_TITLE};
		Cursor c = ourDataBase.query(DATABASE_TABLE_COURSES, columns, KEY_ROWID3 + " like ?", new String[]{GET_ROW_ID},null, null, null);
			//Just need the title to see if it exists in events database, 
			//we added class keyword in the end to distinguish between regular event or class
		
			int iCtitle = c.getColumnIndex(KEY_COURSE_TITLE);
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
				result = result + c.getString(iCtitle)+"(class)";
			}
			GET_TITLE = result;
		}
	}
	
	public boolean does_title_exists_event_table(String cID){
		//GET_TITLE will get set up
		title_exists_course_table(cID);
		//now query the title column in event_table
		if (containCourse_event_table(GET_TITLE) == true)
			return true;
		
		return false;
	}
	
	
	public void delete_course_in_calendar(String cID, String username){
		if(does_title_exists_event_table(cID)){
			
			String result="";
			String[] columns = new String[]{KEY_ROWID1};
			Cursor c =ourDataBase.query(DATABASE_TABLE_EVENTS, columns, KEY_TITLE + " like ? and "+KEY_REF+" like ?", new String[]{GET_TITLE, username}, null, null, null);
			
			int irow = c.getColumnIndex(KEY_ROWID1);
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
				result = result + c.getString(irow)+"\n";
			}
			
			String temp [] = result.split("\n");
			//now delete it in the database
			for(String s: temp){
				ourDataBase.delete(DATABASE_TABLE_EVENTS, KEY_ROWID1 + "=" + s, null);
			}
		}//end if
	}//end function
	
	public String list_of_classes_course_table(){
		String result="";
		
		String[] columns = new String[] {KEY_COURSE_TITLE};
		Cursor c = ourDataBase.query(DATABASE_TABLE_COURSES, columns, null, null, null, null, null);
		
		int iCtitle = c.getColumnIndex(KEY_COURSE_TITLE);
		
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result = result + c.getString(iCtitle) + "\n";
			
		}
		c.close();
		return result;
	}
	
	public String other_information_course_table(String title){
		
		String[] columns = new String[] {KEY_COURSE_ID, KEY_COURSE_CNUMBER, KEY_COURSE_SUBJECT, KEY_COURSE_DAYS,
				KEY_COURSE_START_TIME, KEY_COURSE_END_TIME, KEY_COURSE_PROFESSOR, KEY_COURSE_BUILDING, KEY_COURSE_ROOM_NUMBER };
		Cursor c = ourDataBase.query(DATABASE_TABLE_COURSES, columns, KEY_COURSE_TITLE + " like ?", new String[]{title},null, null, null);
		String result="";
		
		int iCid = c.getColumnIndex(KEY_COURSE_ID);
		int iCcnumber = c.getColumnIndex(KEY_COURSE_CNUMBER);
		int iCsubject = c.getColumnIndex(KEY_COURSE_SUBJECT);
		int iCdays = c.getColumnIndex(KEY_COURSE_DAYS);
		int iCstart_time = c.getColumnIndex(KEY_COURSE_START_TIME);
		int iCend_time = c.getColumnIndex(KEY_COURSE_END_TIME);
		int iCprofessor = c.getColumnIndex(KEY_COURSE_PROFESSOR);
		int iCbuilding = c.getColumnIndex(KEY_COURSE_BUILDING);
		int iCroom_number= c.getColumnIndex(KEY_COURSE_ROOM_NUMBER);
		
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result = result +c.getString(iCid)+ "\t\t"+c.getString(iCcnumber)+"\t\t"+ c.getString(iCsubject) + "\t\t"+
					c.getString(iCdays)+ "\t\t"+ c.getString(iCstart_time)+ "\t\t"+ c.getString(iCend_time) +"\t\t" + 
					c.getString(iCprofessor) + "\t\t"+ c.getString(iCbuilding) + "\t\t" + c.getString(iCroom_number)+"\n";
		}
		c.close();
		return result;
	}

	public String get_all_courses_username(String user) {
		String result="";
		String[] columns = new String[] {KEY_TITLE};
		Cursor c = ourDataBase.query(DATABASE_TABLE_EVENTS, columns, KEY_REF + " like ?", new String[]{user},null, null, null);
		
		int ititle = c.getColumnIndex(KEY_TITLE);
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result = result + c.getString(ititle)+ "\n";
		}
		
		return result;
	}
	
	public String get_building_course_table(String title){
		String result="";
		String[] columns = new String[] {KEY_COURSE_BUILDING};
		Cursor c = ourDataBase.query(DATABASE_TABLE_COURSES, columns, KEY_COURSE_TITLE + " like ?", new String[]{title},null, null, null);
		
		int iBuilding = c.getColumnIndex(KEY_COURSE_BUILDING);
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result = result + c.getString(iBuilding);
		}
		
		return result;
	}
	
	public String get_rowID_event_table(String title, String username, String date){
		String result="";
		String[] columns = new String[] {KEY_ROWID1};
		Cursor c = ourDataBase.query(DATABASE_TABLE_EVENTS, columns, KEY_TITLE + " like ? and "+KEY_REF+" like ? and "+KEY_DATE+ " like ? ", new String[]{title, username, date},null, null, null);
		
		int iROWID = c.getColumnIndex(KEY_ROWID1);
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result = result + c.getString(iROWID);
		}
		return result;
	}
	
	public boolean delete_by_row_id(String id){
		return (ourDataBase.delete(DATABASE_TABLE_EVENTS, KEY_ROWID1 + "='" + id+"'", null))> 0;
	}
}