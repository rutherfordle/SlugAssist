package slug.project.com;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.CalendarView;
import android.widget.Toast;
import android.widget.CalendarView.OnDateChangeListener;

public class CalendarViewActivity extends Activity implements OnDateChangeListener {
	
	 CalendarView calView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        
        calView = (CalendarView) findViewById(R.id.CalendarView);
        calView.setOnDateChangeListener(this);
    }

    //what happens when user click on a date
	@Override
	public void onSelectedDayChange(CalendarView calView, int year, int month,
			int dayofMonth) {
		// TODO Auto-generated method stub
		String date = (month+1)+"/"+dayofMonth+"/"+year;
		callAlertBox(date);
	}
	
	//AlertBox pops up with three options
	void callAlertBox(final String date){
		AlertDialog.Builder builder = new AlertDialog.Builder(CalendarViewActivity.this);
		builder.setTitle("Choose an option: ");
		builder.setMessage("\t\t\t\t\t"+ date);
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//user press cancel, write logic here
			}
		});
		builder.setPositiveButton("Add Event", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//user press AddEvent, write logic here
				
				Toast.makeText(getApplicationContext(), date, Toast.LENGTH_SHORT).show();
				Intent addEvent = new Intent(CalendarViewActivity.this, AddEventActivity.class);
				addEvent.putExtra("clickedDate", date);
				startActivity(addEvent);
			}
		});
	    builder.setNeutralButton("View Events", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//user press ViewEvent, write logic here
				Toast.makeText(getApplicationContext(), date, Toast.LENGTH_SHORT).show();
				Intent viewEvent = new Intent(CalendarViewActivity.this, ViewEventActivity.class);
				viewEvent.putExtra("clickedDate", date);
				startActivity(viewEvent);
			}
		});
        
		builder.show();
	}

}