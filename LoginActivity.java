package slug.project.com;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {

	EditText user, pass;
	Button login, register;
	SharedPreferences preferences;
	SharedPreferences.Editor editor;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		user = (EditText) findViewById(R.id.User_name);
		pass = (EditText) findViewById(R.id.Pass_word);
		login = (Button) findViewById(R.id.login);
		register = (Button) findViewById(R.id.Register_main_but);

		login.setOnClickListener(this);
		register.setOnClickListener(this);
		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = preferences.edit();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.login:

		   String password = pass.getText().toString();
		   if(password.length() < 6 || password.length() >11){
			  Toast.makeText(getApplicationContext(), "Password must be 6-11 character long",Toast.LENGTH_LONG).show();
		   }
			
			// search the database and if they matches go to login screen
			// else send out an error msg
			if (openDB_search()) {
				Toast.makeText(getApplicationContext(),"Accepted, Transferring...",Toast.LENGTH_SHORT).show();
				//put the username in shared preferences
				editor.putString("username", user.getText().toString());
				editor.commit();
				
				Intent title = new Intent(LoginActivity.this, TitleActivity.class);
				startActivity(title);
				//if back button is pressed, user will stay on title page
				finish();
				
			} 
			else if (!openDB_search()) {
				Toast.makeText(getApplicationContext(),"Invalid Login Information", Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.Register_main_but:
			Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(intent);
			break;
		}
	}

	// This function will open up the DB and get a string of UserName and
	// Password back,
	// Then it will break it up and compare it with login information
	// return true if the DB has the same userName and password, else return
	// false
	public boolean openDB_search() {
		String uName_pass;
		Database entry = new Database(this);
		entry.open();
		uName_pass = entry.getUserName_password();
		entry.close();
		
		// now check the return string with editText fields
		String temp1[] = uName_pass.split("\n");
		List<String> mylist = Arrays.asList(temp1);

		String userPLUSpass = user.getText().toString() + " andpassis "
				+ pass.getText().toString();
		if (mylist.contains(userPLUSpass))
			return true;

		return false;
	}
}