package slug.project.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ViewDescActivity extends Activity {

	TextView title, description, startTime, endTime,dateView;
	//Button goBack;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_desc);
		
		title = (TextView)findViewById(R.id.title_desc);
		description = (TextView)findViewById(R.id.description_desc);
		startTime =(TextView)findViewById(R.id.description_startTime);
		endTime = (TextView)findViewById(R.id.description_endTime);
		dateView =(TextView)findViewById(R.id.date_desc);
		
		//goBack =(Button)findViewById(R.id.goBack);
		Intent i = getIntent();
		String titleStr = i.getStringExtra("title");
		String date = i.getStringExtra("clickedDate");
		String descrip = i.getStringExtra("description");
		String start = i.getStringExtra("startTime");
		String end = i.getStringExtra("endTime");
		
		dateView.setText(date);
		title.setText(titleStr);
		description.setText(descrip);
		startTime.setText(start);
		endTime.setText(end);
		
	}
}
