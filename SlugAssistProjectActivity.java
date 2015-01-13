package slug.project.com;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SlugAssistProjectActivity extends Activity {

	String val = "";
	int NUMBER_OF_ROW;
	ArrayList<String> course_id, title, cnumber, subject, days, stime, etime,
			professor, building, room;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// intialize the arraylists
		course_id = new ArrayList<String>();
		title = new ArrayList<String>();
		cnumber = new ArrayList<String>();
		subject = new ArrayList<String>();
		days = new ArrayList<String>();
		stime = new ArrayList<String>();
		etime = new ArrayList<String>();
		professor = new ArrayList<String>();
		building = new ArrayList<String>();
		room = new ArrayList<String>();

		// get the information from courses database
		load_to_database();
		jump_to_login();
	}

	public void load_to_database() {
		// opens up the workbook(excel sheet) and put it into a string
		openWorkbook();

		// parse the string into 10 arraylists, so it will stay in proper column
		parseString();

		// Then load all the stuff from arraylists to database
		addEntryToDB();
	}

	public void openWorkbook() {
		// TODO Auto-generated method stub

		try {
			InputStream inputs = getResources().openRawResource(R.raw.book2);

			Workbook workbook = Workbook.getWorkbook(inputs);
			Sheet sheet = workbook.getSheet(0);
			Cell cell;
			// Cell a1 = sheet.getCell(0,1);
			// String stringa1 = a1.getContents();

			// reading the data from excel sheet
			for (int row = 1; row < sheet.getRows(); row++) {
				for (int col = 0; col < sheet.getColumns(); col++) {
					cell = sheet.getCell(col, row);
					val = val + cell.getContents() + "\t\t";
				}
				val = val + "\n";
			}
			NUMBER_OF_ROW = sheet.getRows() - 1;
			workbook.close();

		} catch (BiffException e) {
			// TODO Auto-generated catch block
			Log.d("error", "biffException");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("error", "IOexception");
			e.printStackTrace();
		}
	}

	public void parseString() {

		String temp[];
		temp = val.split("\\t\\t");
		List<String> aList = Arrays.asList(temp);

		Log.d("fdsf", "After temp");
		for (int i = 0; i < aList.size(); i++) {

			switch (i % 10) {
			case 0:
				course_id.add(aList.get(i));
				break;
			case 1:
				title.add(aList.get(i));
				break;
			case 2:
				cnumber.add(aList.get(i));
				break;
			case 3:
				subject.add(aList.get(i));
				break;
			case 4:
				days.add(aList.get(i));
				break;
			case 5:
				stime.add(aList.get(i));
				break;
			case 6:
				etime.add(aList.get(i));
				break;
			case 7:
				professor.add(aList.get(i));
				break;
			case 8:
				building.add(aList.get(i));
				break;
			case 9:
				room.add(aList.get(i));
				break;
			}
		}

		// for debugging only
		/*
		 * for (String s: course_id){ Log.d("course_id", s); } for (String s:
		 * title){ Log.d("title", s); } for (String s: cnumber){
		 * Log.d("cnumber", s); } for (String s: subject){ Log.d("subject", s);
		 * } for (String s: days){ Log.d("days", s); } for (String s: stime){
		 * Log.d("course_id", s); } for (String s: etime){ Log.d("etime", s); }
		 * for (String s: professor){ Log.d("professor", s); } for (String s:
		 * building){ Log.d("building", s); } for (String s: room){
		 * Log.d("room", s); }
		 */
	}

	// creating entries in the courses_table
	public void addEntryToDB() {
		Database database = new Database(this);
		database.open();
		for (int i = 0; i < NUMBER_OF_ROW; i++) {
			database.createEntry_in_Courses(course_id.get(i), title.get(i),
					cnumber.get(i), subject.get(i), days.get(i), stime.get(i),
					etime.get(i), professor.get(i), building.get(i),
					room.get(i));
		}
		database.close();
	}
	
	public void jump_to_login(){
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(2000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				finally{
					Intent intent = new Intent(SlugAssistProjectActivity.this,LoginActivity.class);
					startActivity(intent);
					finish();
				}
			}
		};
		timer.start();
		
	}
}