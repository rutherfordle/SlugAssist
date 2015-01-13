package slug.project.com;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ListCourseActivity extends Activity implements OnItemClickListener {

	ListView courseList;
	ArrayList<String> items;
	ArrayAdapter<String> itemAdapter;
	String TitleList[];
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_list);
		items = new ArrayList<String>();
		
		courseList = (ListView) findViewById(R.id.course_list_listview);
		itemAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, items);
		courseList.setAdapter(itemAdapter);
		
		//now I need to get the data from database and add it to list
		TitleList = get_info_from_database();
		
		for(String s: TitleList){
			items.add(s);
		}
		courseList.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		// TODO Auto-generated method stub
		String title = courseList.getItemAtPosition(position).toString();
		//get other information using title
		Intent intent = new Intent(ListCourseActivity.this, ClassDescActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("result", get_other_information(title));
		startActivity(intent);
	}
	
	public String[] get_info_from_database(){
		Database db = new Database(this);
		db.open();
		String result = db.list_of_classes_course_table();
		String title[] = result.split("\n");
		db.close();
		return title;
	}
	
	public String get_other_information(String title){
		Database db = new Database(this);
		db.open();
		String result = db.other_information_course_table(title);
		db.close();
		return result;
	}
}
