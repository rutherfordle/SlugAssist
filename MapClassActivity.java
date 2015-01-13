package slug.project.com;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MapClassActivity extends Activity implements OnItemClickListener {

	ListView courseList;
	ArrayList<String> items;
	ArrayAdapter<String> itemAdapter;
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	String user;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_myclass);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		user = preferences.getString("username", "");
		
		items = new ArrayList<String>();
		
		courseList = (ListView) findViewById(R.id.list_myclass_listview);
		itemAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, items);
		courseList.setAdapter(itemAdapter);
		
		get_info_from_database();
		courseList.setOnItemClickListener(this);
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		// TODO Auto-generated method stub
		String title = courseList.getItemAtPosition(position).toString();
		//now open the course_table and get the building name
		Database db = new Database(this);
		db.open();
		String building = db.get_building_course_table(title);
		db.close();
		createAlertBox(title, building);
	}
	
	public void get_info_from_database(){
		String result = "";
		Database db = new Database(this);
		db.open();
		result = db.get_all_courses_username(user);
		db.close();
		//now we got all the title names from the database, now see if the title ends with(class) tag
		String temp[] = result.split("\n");
		List<String> classes = new ArrayList<String>();
		for(String s : temp){
			if(s.endsWith("(class)")){
				classes.add(s);
			}
		}
		//now remove the duplicating values
		HashSet<String> hs = new HashSet<String>();
		hs.addAll(classes);
		classes.clear();
		classes.addAll(hs);
		
	
		for(String s: classes){
			items.add(s.replace("(class)",""));
		}
			
	}
	
	void createAlertBox(String title, String building){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		Database db = new Database(this);
		db.open();
		
		   final String loc = db.get_building_course_table(title);
			// set title
			alertDialogBuilder.setTitle(title);
			db.close();
			
 
			// set dialog message
			alertDialogBuilder
				.setMessage("MAP IT!!!")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						//go to map activity from here
						
				    	  Intent i = new Intent("com.slugmap");
				    	  
				    	  i.putExtra("keyword1",loc);
				    	  startActivity(i);
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
			}
}
