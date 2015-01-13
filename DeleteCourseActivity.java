package slug.project.com;

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

public class DeleteCourseActivity extends Activity implements OnClickListener {

	String user="";
	EditText delete_textbox;
	//TextView view;
	Button delete_button;
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.delete_course);
		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		user = preferences.getString("username", "");
		delete_textbox = (EditText)findViewById(R.id.editText_delete_course);
		delete_button = (Button)findViewById(R.id.delete_delete_course);
		//view = (TextView)findViewById(R.id.textView_delete_course);
		
		delete_button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.delete_delete_course){
			String text_delete = delete_textbox.getText().toString();
			if(text_delete.length() == 0)
				Toast.makeText(this.getApplicationContext(), "Please enter something", Toast.LENGTH_SHORT).show();
			else if(text_delete.length() > 5 || text_delete.length() < 5)
				Toast.makeText(this.getApplicationContext(), "Course ID must be 5 digit long", Toast.LENGTH_SHORT).show();
			
			else{
					//otherwise get the course id and delete the courses in database by course id
					Database db = new Database(this);
					db.open();
						
					if(!db.Course_exists(text_delete)){
						Toast.makeText(getApplicationContext(), "Invalid Course ID", Toast.LENGTH_SHORT).show();
					}
					else{
						//now check if the title exists in event table
						if (db.does_title_exists_event_table(text_delete)){
							//view.setText("course exists");
							//now delete the course for the event_table
							db.delete_course_in_calendar(text_delete,user);
							Toast.makeText(getApplicationContext(), "Course deleted!!", Toast.LENGTH_SHORT).show();
						}
						
							//view.setText("course doesn't exists");
					}
						
					db.close();
			}
		}
	}
}
