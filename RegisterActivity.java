package slug.project.com;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener{

	Button register;
	EditText fName_editbox, lName_editbox, Email_editbox, user_name_editbox, pass_editbox, confirm_pass_editbox;
	
	private Handler mHandler = new Handler();
	user_info info = new user_info();
	
	public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.register);
	      
	      fName_editbox = (EditText)findViewById(R.id.first_name);
	      lName_editbox = (EditText)findViewById(R.id.last_name);
	      Email_editbox = (EditText)findViewById(R.id.e_mail);
	      user_name_editbox = (EditText)findViewById(R.id.user_name_register);
	      pass_editbox = (EditText)findViewById(R.id.Password_register);
	      confirm_pass_editbox = (EditText)findViewById(R.id.confirm_pass);
	      register = (Button)findViewById(R.id.Register_reg_button);
	      register.setOnClickListener(this);
	     
	 }
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.Register_reg_button){
			
			String fname = fName_editbox.getText().toString();
			String lname = lName_editbox.getText().toString();
			String email = Email_editbox.getText().toString();
			String uName = user_name_editbox.getText().toString();
			String passw = pass_editbox.getText().toString();
			String passc = confirm_pass_editbox.getText().toString();

			//check to see if any of the edittext is left blank
			if(fname.matches("") || lname.matches("") ||email.matches("") ||
					uName.matches("") || passw.matches("") || passc.matches(""))
			{
				Toast.makeText(getApplicationContext(), "One or more field(s) left empty", Toast.LENGTH_LONG).show();
			}
			
			//first check that if the fName and lName doesn't contain any number
			else if(containNumber(fname)|| containNumber(lname)){
				Toast.makeText(getApplicationContext(), "Please don't use numbers in First Name and Last Name fields",Toast.LENGTH_LONG).show();
			} 
			//if password length is less than 6 and greater than 11
			else if(passw.length()<6 || passw.length()>11){
				Toast.makeText(getApplicationContext(), "Password must be between 6-11 character long", Toast.LENGTH_LONG).show();
			}
			//if the password doesn't matches confirm password
			else if(!passw.equals(passc))
				Toast.makeText(getApplicationContext(), "Password doesn't match", Toast.LENGTH_LONG).show();
			
			//calls the user_info class instance to set the methods
			else{
				info.setAll(uName, passw, fname,lname, email);
				create_Entry();
				//Log.d("FROM", "create entry");
			}
		}
	}
	
	//check to see if the string have any digit in it
	public boolean containNumber(String input){
		for(char c : input.toCharArray()){
			if(Character.isDigit(c)){
				return true;
			}	
		}
		return false;
	} //end containNumber
	
	public void create_Entry(){
		//create an entry in the db
		boolean diditWork = true;
		try{
			Database entry = new Database(this);
			entry.open();
			
			//check if the entry already exist in db
			if (entry.checkUsername(user_name_editbox.getText().toString()) == true){
				//username already exists
				Toast.makeText(getApplicationContext(), "username already exists", Toast.LENGTH_LONG).show();
			}
			else{
				entry.createEntry_in_userinfo(info.get_fName(), info.get_lName(), info.get_email(), info.get_userName(), info.get_passWord());
				
				Toast.makeText(getApplicationContext(), user_name_editbox.getText().toString()+" is now registered", Toast.LENGTH_LONG).show();
				//transfer back to login here
				//wait for few seconds before transferring
				mHandler.postDelayed(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						transfer_to_login();
					}
					
				},800);
			}
			
			//close the database
			entry.close();
		}catch(Exception e){
			diditWork = false;
		}finally{
			if(!diditWork){
				Dialog d = new Dialog(this);
				d.setTitle("NO!!!");
				TextView tv = new TextView(this);
				tv.setText("Error: Database entry didn't get created, Hit the back button");
				d.setContentView(tv);
				d.show();
			}
		}
	}
	
	public void transfer_to_login(){
		Intent backToLogin = new Intent(RegisterActivity.this, LoginActivity.class);
		startActivity(backToLogin);
	}
	public void deleteEntry(){
		//delete an entry in the db
	}
}
