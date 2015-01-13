package slug.project.com;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ClassDescActivity extends Activity implements OnClickListener {

	Button add;
	TextView titleView,descView;
	String title="", result="", user="", description="";
	String courseid, cnumber, subject, days, stime, etime, professor, building, roomnumber;
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	//array to hold mwf, tth, t classs for november
		String nov_mwfArray[], nov_tthArray[], nov_tuesArray[], dec_mwfArray[], dec_tthArray[], dec_tuesArray[];
		
		String mwf_nov_dec_Array[], tth_nov_dec_Array[], tues_nov_dec_Array[];
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_list_item_view);
		titleView = (TextView) findViewById(R.id.product_label);
		descView = (TextView) findViewById(R.id.Location);
		add = (Button)findViewById(R.id.buttonADD);
		
		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		result = intent.getStringExtra("result");
		
		//get the save data from preferences
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		user = preferences.getString("username", "");
		
		titleView.setText(title);
		parseString(result);
		String description = "Course ID: "+ courseid +"\n"+"Class Number: "+cnumber+ "\n"+"Subject: "+subject+"\n"+
							 "Days: "+ days + "\n"+ "Start Time: "+stime+"\n" + "End Time: "+ etime + "\n"+ "Professor: "+
							  professor+"\n"+ "Building: "+building+"\n"+"Room Number: "+roomnumber;
		descView.setText(description);
		add.setOnClickListener(this);
	}
	
	//parse the string and setting each string according to the position
	public void parseString(String result){
		String temp[] = result.split("\t\t");
		courseid = temp[0];
		//replace the space at beginning of the string with empty
		courseid = courseid.replaceAll("^+[\r\n]", "");
		cnumber = temp[1];
		subject = temp[2];
		days = temp[3];
		stime = temp[4];
		etime = temp[5];
		professor = temp[6];
		building = temp[7];
		roomnumber = temp[8];
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view.getId() == R.id.buttonADD){
			//first check if it exists in database
			if(check_if_course_exists()){
				Toast.makeText(getApplicationContext(), "Course already exists in Calendar", Toast.LENGTH_SHORT).show();
			}
			else{
				//add it in the database
				getCalendarDays();
				add_entry();
				Toast.makeText(getApplicationContext(), title+ " added to calendar", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public boolean check_if_course_exists(){
		Database db = new Database(this);
		db.open();
		if(db.containCourse_event_table(title+"(class)", user))
			return true;
		db.close();
		
		return false;
	}
	public void add_entry(){
		Database db = new Database(this);
		db.open();
		description = "Course ID: "+courseid+", "+"Class Number: "+cnumber +", "+"Subject: "+ subject +", "+"Professor: " +professor + ", "+ 
		"Building: "+building+ ", "+"Room Number: "+roomnumber; 
		//To trim the \n in the end of the string
		
		description = description.replaceAll("[\r\n]+$", "");
		
		if(days.equals("MWF")){
			for(String s: mwf_nov_dec_Array)
				db.createEntry_in_events(s,title+"(class)",description, stime, etime, user);
				//Log.d("title", title);
				//Log.d("desc", description);
				//Log.d("stime", stime);
				//Log.d("etime", etime);
				//Log.d("user",user);
				//Log.d("everything", title + description + stime + etime + user);
			db.close();
		}
		else if(days.equals("TTh")){
			for(String s:tth_nov_dec_Array)
				db.createEntry_in_events(s, title+"(class)", description,stime, etime, user);
			db.close();
		}
		else if(days.equals("T")){
			for(String s:tues_nov_dec_Array)
				db.createEntry_in_events(s,title+"(class)",description, stime, etime, user);
			db.close();
		}
	}
	
	//calendar stuff
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
