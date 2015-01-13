package slug.project.com;


import java.util.Calendar;
import java.util.GregorianCalendar;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddCourseActivity extends Activity implements OnClickListener{

	Button add;
	EditText textBox;
	String add_course_text;
	//TextView text;
	String user="", description = "";
	String result = "";
	String ctitle ,cCnumber,cSubject,cDays,cSTime,cETime,cProfessor,cBuilding,cRoomNumber,cCourseId;
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	//array to hold mwf, tth, t classs for november
	String nov_mwfArray[], nov_tthArray[], nov_tuesArray[], dec_mwfArray[], dec_tthArray[], dec_tuesArray[];
	
	String mwf_nov_dec_Array[], tth_nov_dec_Array[], tues_nov_dec_Array[];
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_course);
		
		add = (Button) findViewById(R.id.add_add_course);
		textBox = (EditText) findViewById(R.id.editText_add_course);
		//text = (TextView) findViewById(R.id.textView_add_course);
		add.setOnClickListener(this);
		
		//to get the user name
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		user = preferences.getString("username", "");
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		add_course_text = textBox.getText().toString();
		if(add_course_text.length() == 0)
			Toast.makeText(getApplicationContext(), "Please enter something", Toast.LENGTH_SHORT).show();
		else if(add_course_text.length() >5 || add_course_text.length() <5){
			//print out an erro msg
			Toast.makeText(getApplicationContext(), "Course ID is 5 digits long", Toast.LENGTH_SHORT).show();
		}
		else{
				//open up the database, copy the stuff from courses table to new table
				Database db = new Database(this);
				db.open();
				
				//check to see if the course exists
				if(db.Course_exists(add_course_text)){
					//get all information related to the course by courseID
					result = db.getCourse_info();
					
					//get the days for mwf, tth, tues for nov and dec
					getCalendarDays();
					//parse the data
					parseString_data();
					
					//now make an entry to database
					if(makeEntry_to_db())
						Toast.makeText(this.getApplicationContext(), "Course entered in the calendar", Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(this.getApplicationContext(), "Course already exists in the database", Toast.LENGTH_SHORT).show();
					
					textBox.setText("");
				}
				
				else
					Toast.makeText(getApplicationContext(), "Course doesn't exists \nPlease check Course ID again", Toast.LENGTH_SHORT).show();
				
				db.close();
		}
	}
	
	public void parseString_data(){
		//parse the result
		String temp[] = result.split("\t\t");
		ctitle = temp[0]+"(class)";
		cCnumber = temp[1];
		cSubject = temp[2];
		cDays = temp[3];
		cSTime = temp[4];
		cETime = temp[5];
		cProfessor = temp[6];
		cBuilding = temp[7];
		cRoomNumber = temp[8];
		cCourseId = textBox.getText().toString();
		
		description = "Course ID: "+cCourseId+", "+"Class Number: "+cCnumber +", "+"Subject: "+ cSubject +", "+"Professor: " +cProfessor + ", "+ 
		"Building: "+cBuilding+ ", "+"Room Number: "+cRoomNumber; 
		
		//To trim the \n in the end of the string
		description = description.replaceAll("[\r\n]+$", ""); 
		Log.d("title", ctitle);
	}
	
	public boolean makeEntry_to_db(){
		
		//we are going to assume that quarter started from 11/1/2012 and end until 12/25/2012
		
		Database database = new Database(this);
		database.open();
		
		//first check that if the course already exists in the database, if does print out a warning else insert
		
		if(database.containCourse_event_table(ctitle, user)){
			return false;
		}
		
		else{
			// some logic to create entry for specific days
			if(cDays.equals("MWF")){
				for(String s: mwf_nov_dec_Array)
					database.createEntry_in_events(s, ctitle, description, cSTime, cETime, user);
				
				return true;
			}
			else if(cDays.equals("TTh")){
				for(String s: tth_nov_dec_Array)
					database.createEntry_in_events(s, ctitle, description, cSTime, cETime, user);
				
				return true;
			}
			else if (cDays.equals("T")){
				for(String s: tues_nov_dec_Array)
					database.createEntry_in_events(s, ctitle, description, cSTime, cETime, user);
				
				return true;
			}
		}
		database.close();
		return true;
	}
	
	public void getCalendarDays(){
		
		int year = 2012;
		int month = Calendar.NOVEMBER;
		int month2 = Calendar.DECEMBER;
		//add two months+ 1 day to calendar
		Calendar cal = new GregorianCalendar(year,month,1);
		Calendar cal2 = new GregorianCalendar(year,month2, 1);
		
		String mwf_nov = "";
		String tth_nov="";
		String tues_nov = "";
		String mwf_dec="";
		String tth_dec="";
		String tues_dec="";
		
		//this will get date for mwf,tth,tues in the month of november
		do{
			int day = cal.get(Calendar.DAY_OF_WEEK);
			if (day == Calendar.MONDAY || day == Calendar.WEDNESDAY || day == Calendar.FRIDAY) {
				mwf_nov = mwf_nov+ cal.get(Calendar.DAY_OF_MONTH)+"\t\t";
			}
			if(day == Calendar.TUESDAY || day == Calendar.THURSDAY){
				tth_nov = tth_nov+ cal.get(Calendar.DAY_OF_MONTH)+"\t\t";
			}
			if(day == Calendar.TUESDAY)
				tues_nov= tues_nov+ cal.get(Calendar.DAY_OF_MONTH)+"\t\t";
			
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}while (cal.get(Calendar.MONTH) == month);
		
		nov_mwfArray = put_in_month(mwf_nov,11);
		nov_tthArray = put_in_month(tth_nov,11);
		nov_tuesArray = put_in_month(tues_nov,11);
		
		//this will get date for mwf,tth,tues in the month of december
		do{
			int day2 = cal2.get(Calendar.DAY_OF_WEEK);
			if (day2 == Calendar.MONDAY || day2 == Calendar.WEDNESDAY || day2 == Calendar.FRIDAY) {
				mwf_dec = mwf_dec+ cal2.get(Calendar.DAY_OF_MONTH)+"\t\t";
			}
			if(day2 == Calendar.TUESDAY || day2 == Calendar.THURSDAY){
				tth_dec = tth_dec+ cal2.get(Calendar.DAY_OF_MONTH)+"\t\t";
			}
			if(day2 == Calendar.TUESDAY)
				tues_dec= tues_dec+ cal2.get(Calendar.DAY_OF_MONTH)+"\t\t";
			
			cal2.add(Calendar.DAY_OF_YEAR, 1);
		}while (cal2.get(Calendar.MONTH) == month2);
		
		dec_mwfArray = put_in_month(mwf_dec,12);
		dec_tthArray = put_in_month(tth_dec,12);
		dec_tuesArray = put_in_month(tues_dec,12);
		
		
		//combine mwf_nov_dec_Array, tth_nov_dec_Array, tues_nov_dec_Array
		mwf_nov_dec_Array = concat(nov_mwfArray, dec_mwfArray);
		tth_nov_dec_Array = concat(nov_tthArray, dec_tthArray);
		tues_nov_dec_Array = concat(nov_tuesArray, dec_tuesArray);
		
	} 
	
	public String[] put_in_month(String day, int month){
		
			String temp[] = day.split("\t\t");
			for(int i=0; i < temp.length; i++){
				temp[i] = month+"/" +temp[i]+ "/2012";
			}
			return temp;
	}
	
	public String[] concat(String[] s1, String[] s2) {
	      String[] erg = new String[s1.length + s2.length];

	      System.arraycopy(s1, 0, erg, 0, s1.length);
	      System.arraycopy(s2, 0, erg, s1.length, s2.length);

	      return erg;
	  }
}
