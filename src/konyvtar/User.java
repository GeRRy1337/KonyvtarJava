package konyvtar;

import java.util.prefs.Preferences;

public class User {
    private String username,password;
    private boolean login=false;
    
    public User(){
        Preferences userPreferences = Preferences.userRoot();
        login=userPreferences.getBoolean("loggedin", false);
    }
    
    public void setLogin(boolean newValue){
        login=newValue;
        Preferences userPreferences = Preferences.userRoot();
        userPreferences.putBoolean("loggedin", newValue);
    }
    
    public boolean loginValue(){
        return login;
    }
}
