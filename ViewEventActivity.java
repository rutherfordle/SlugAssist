package slug.project.com;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class ViewEventActivity extends Activity implements OnItemClickListener, OnItemLongClickListener {

	ListView titleList;
	ArrayList<String> items;
	ArrayAdapter<String> itemAdapter;
	ArrayList<String> titleArray, descArray, sTimeArray, eTimeArray;
	String date,user, temp;
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_event);

		titleList = (ListView) findViewById(R.id.ListOftitles);

		Intent i = getIntent();
		date = i.getStringExtra("clickedDate");
		
		//get the save data from preferences
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		user = preferences.getString("username", "");
		
		// setting adapter for titleList
		items = new ArrayList<String>();
		titleArray = new ArrayList<String>();
		descArray = new ArrayList<String>();
		sTimeArray = new ArrayList<String>();
		eTimeArray = new ArrayList<String>();

		itemAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, items);
		titleList.setAdapter(itemAdapter);

		// open db and get the string for specific date
		parseString();
		
		//reverse the arraylist so when click on listview, it display accurate date
		Collections.reverse(descArray);
		Collections.reverse(eTimeArray);
		Collections.reverse(sTimeArray);

		//Log.d("firstEle", titleArray.get(0));
		//Log.d("secondEle", titleArray.get(1));
		
		//getting the strings from titleArray and displaying it on screen
		for(int x=0; x < titleArray.size(); x++)
			items.add(0,titleArray.get(x));
		
		itemAdapter.notifyDataSetChanged();

		titleList.setOnItemClickListener(this);
		titleList.setOnItemLongClickListener(this);
	}

	// what happens when the user clicks on an item in the listView, he/she get
	// transported to next intent
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		//Toast.makeText(getApplicationContext(), "clicked: "+position,Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(ViewEventActivity.this,
				ViewDescActivity.class);
		String text = titleList.getItemAtPosition(position).toString();
		String descrip = descArray.get(position);
	
		String startTime = sTimeArray.get(position);
		String endTime = eTimeArray.get(position);
		
		//sending the data to other activities
		intent.putExtra("title", text);
		intent.putExtra("clickedDate", date);
		intent.putExtra("description", descrip);
		intent.putExtra("startTime", startTime);
		intent.putExtra("endTime", endTime);
		startActivity(intent);
	}

	public void parseString() {
		Database db = new Database(this);
		db.open();
		String events = db.getOneDayEvents(date,user);
		db.close();

		String temp1[] = events.split("\n");

		// now parse each column to display in the listView
		Pattern pattern1 = Pattern.compile("<title>(.*?)</title>");
		Pattern pattern2 = Pattern.compile("<desc>((.*?)[\\r\\n]|(.*?))</desc>");
		Pattern pattern3 = Pattern.compile("<stime>(.*?)</stime>");
		Pattern pattern4 = Pattern.compile("<etime>(.*?)</etime>");
		
		//getting title, desc, time using pattern
		Matcher m,n,o,p;
		for (int i = 0; i < temp1.length; i++) {
			m = pattern1.matcher(temp1[i]);
			n = pattern2.matcher(temp1[i]);
			o = pattern3.matcher(temp1[i]);
			p = pattern4.matcher(temp1[i]);
			if(m.find())
				//storing all the titles in arraylist
				titleArray.add(m.group(1));
			if(n.find()){
				descArray.add(n.group(1));
				//Log.d("tags",n.group(1));
			}
			if(o.find())
				sTimeArray.add(o.group(1));
			if(p.find())
				eTimeArray.add(p.group(1));
		}

	}


	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		final Database db = new Database(this);
		db.open();
		final int temp2 = position;
		//now look for row ID for specific title
		String title = titleList.getItemAtPosition(position).toString();
		//get the rowid by username, title, and date
		temp = db.get_rowID_event_table(title, user,date);
		 
		 //replace the \n with nothing
		// temp = temp.replaceAll("[\r\n]+$", "");
		
		alertDialogBuilder.setMessage("Delete it!!").setCancelable(false)
		.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked,delete the specific event from the list
				 if( db.delete_by_row_id(temp)){
					Toast.makeText(getApplicationContext(), "Item Deleted", Toast.LENGTH_SHORT).show();
					items.remove(temp2);
				 	itemAdapter.notifyDataSetChanged();
				 }
				 
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
		return true;
	}
}
