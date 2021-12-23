package konyvtar;

import java.util.prefs.Preferences;
import java.sql.ResultSet;

public class User {
    private int id;
    private String username;
    private boolean login=false;
    private Preferences userPreferences = Preferences.userRoot();
    
    public User(){
        login=userPreferences.getBoolean("loggedin", false);
        id=userPreferences.getInt("userId", 0);
    }
    
    public int getId(){
        return this.id;
    }
    
    public String getUsername(){
        dbConnect db=new dbConnect();
        ResultSet result = db.getResult("Select username from users where id="+this.id);
        try{
            while(result.next()){
                username=result.getString("username");
            }
        }catch(Exception e){
            System.err.println(e.getMessage());
            return "error";
        }
        return username;
    }
    
    public void setUser(int newId){
        this.id=newId;
        userPreferences.putInt("userId", newId);
    }
    
    public void setLogin(boolean newValue){
        login=newValue;
        userPreferences.putBoolean("loggedin", newValue);
    }
    
    public void setId(int newId){
        userPreferences.putInt("userId", newId);
        this.id=newId;
    }

    public boolean loginValue(){
        return login;
    }
}
