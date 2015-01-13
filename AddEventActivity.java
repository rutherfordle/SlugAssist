package slug.project.com;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddEventActivity extends Activity implements OnClickListener {

	/** Called when the activity is first created. */
	EditText title, description;
	TextView changeDate, start, end;
	Button addEvent, startTime, endTime;
	String date, Title, desc, time_start, time_end, user;
	int hour1, hour2, minute1, minute2;
	static final int TIME_DIALOG_ID_1 = 1;
	static final int TIME_DIALOG_ID_2 = 2;
	
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_event);
		title = (EditText) findViewById(R.id.EditTitle);
		description = (EditText) findViewById(R.id.EditDescription);
		addEvent = (Button) findViewById(R.id.AddEventButton);
		changeDate = (TextView) findViewById(R.id.changeDate);
		start = (TextView) findViewById(R.id.Start);
		end = (TextView) findViewById(R.id.end);
		startTime = (Button) findViewById(R.id.startTime);
		endTime = (Button) findViewById(R.id.endTime);

		// register for button when clicked
		addEvent.setOnClickListener(this);
		startTime.setOnClickListener(this);
		endTime.setOnClickListener(this);
		
		//To get the username from shared preferences
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		user = preferences.getString("username", "");

		Intent intent = getIntent();
		date = intent.getStringExtra("clickedDate");
		changeDate.setText(date);

	}

	@Override
	public void onClick(View buttonClicked) {
		// TODO Auto-generated method stub
		switch (buttonClicked.getId()) {
		case R.id.startTime:
			// popup the dialog box and select start time
			showDialog(TIME_DIALOG_ID_1);
			break;
		case R.id.endTime:
			// pop up the dialog box and select end time
			showDialog(TIME_DIALOG_ID_2);
			break;
		case R.id.AddEventButton:
			Title = title.getText().toString();
			desc = description.getText().toString();
			time_start = start.getText().toString();
			time_end = end.getText().toString();
			create_entry();
			break;
		}

	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID_1:
			// set time picker as current time
			return new TimePickerDialog(this, timePickerListener1, hour1,
					minute1, false);
		case TIME_DIALOG_ID_2:
			return new TimePickerDialog(this, timePickerListener2, hour2,
					minute2, false);
		}
		return null;
	}

	private TimePickerDialog.OnTimeSetListener timePickerListener1 = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour1 = selectedHour;
			minute1 = selectedMinute;

			// set current time into textview
			start.setText(new StringBuilder().append(pad(hour1)).append(":")
					.append(pad(minute1)));
		}
	};
	private TimePickerDialog.OnTimeSetListener timePickerListener2 = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour2 = selectedHour;
			minute2 = selectedMinute;
			// set current time into textview
			end.setText(new StringBuilder().append(pad(hour2)).append(":")
					.append(pad(minute2)));
		}
	};
	//something to do with timepicker time
	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	// creating an entry in the database
	public void create_entry() {
		// TODO Auto-generated method stub
		boolean diditWork = true;
		try {
			Database myDb = new Database(this);
			myDb.open();
			if (Title.equals("") || desc.equals("")) {
				Toast.makeText(this, "Title and description cannot be empty",
						Toast.LENGTH_SHORT).show();
			} else {
				
				//put it in database
				myDb.createEntry_in_events(date, Title, desc, time_start, time_end, user);
				Toast.makeText(getApplicationContext(), "Event registered",
						Toast.LENGTH_SHORT).show();
				title.setText("");
				description.setText("");
				start.setText("00:00");
				end.setText("00:00");

			}
			myDb.close();

		} catch (Exception e) {
			diditWork = false;
		} finally {
			if (!diditWork) {
				Dialog d = new Dialog(this);
				d.setTitle("NO!!!");
				TextView tv = new TextView(this);
				tv.setText("Error: Event didn't get created, Hit the back button");
				d.setContentView(tv);
				d.show();
			}
		}
	}
}