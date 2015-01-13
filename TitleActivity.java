package slug.project.com;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class TitleActivity extends Activity implements OnClickListener{

	Button Calendar, logout, schedule, add_class, delete_class, mapClass;
	private Handler mHandler = new Handler();
	String user; // for username
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.title);
		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		user = preferences.getString("username", "");
		
		Calendar =(Button)findViewById(R.id.Calendar_title);
		logout = (Button)findViewById(R.id.logout_title);
		schedule = (Button)findViewById(R.id.schedule_title);
		add_class = (Button)findViewById(R.id.Add_title);
		delete_class = (Button)findViewById(R.id.Delete_title);
		mapClass = (Button)findViewById(R.id.gps_title);
		
		Calendar.setOnClickListener(this);
		logout.setOnClickListener(this);
		schedule.setOnClickListener(this);
		add_class.setOnClickListener(this);
		delete_class.setOnClickListener(this);
		mapClass.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		
			case R.id.Calendar_title:
				Intent intent1 = new Intent(TitleActivity.this, CalendarViewActivity.class);
				startActivity(intent1);
				break;
			case R.id.Add_title:
				Intent intent2 = new Intent(TitleActivity.this, AddCourseActivity.class);
				startActivity(intent2);
				break;
			case R.id.Delete_title:
				Intent intent3 = new Intent(TitleActivity.this, DeleteCourseActivity.class);
				startActivity(intent3);
				break;
			case R.id.schedule_title:
				Intent intent4 = new Intent(TitleActivity.this, ListCourseActivity.class);
				startActivity(intent4);
				break;
			case R.id.gps_title:
				Intent intent5 = new Intent(TitleActivity.this, MapClassActivity.class);
				startActivity(intent5);
				break;
			case R.id.logout_title:
				Toast.makeText(this.getApplicationContext(), user+ " logging out, Goodbye!!", Toast.LENGTH_SHORT).show();
				mHandler.postDelayed(new Runnable(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Intent backLogin = new Intent(TitleActivity.this, LoginActivity.class);
						startActivity(backLogin);
					}
				},2000);	
				finish();
				break;
		}
	}
	
}
