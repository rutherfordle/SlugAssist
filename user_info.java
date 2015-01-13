package slug.project.com;

public class user_info {
	private String userName;
	private String password;
	private String fName;
	private String lName;
	private String email;

	public void setAll(String user, String pass, String f, String l, String em){
		userName = user;
		password = pass;
		fName = f;
		lName = l;
		email = em;
	}
	
	public String get_userName(){
		return userName;
	}
	
	public String get_passWord(){
		return password;
	}
	
	public String get_fName(){
		return fName;
	}
	
	public String get_lName(){
		return lName;
	}
	public String get_email(){
		return email;
	}
	
}
