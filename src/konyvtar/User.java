package konyvtar;

import java.util.Map;

public class User {
    private int id;
    private String username;
    private boolean login=false;
    
    public User(){
        login=false;
        id=0;
    }
    
    public int getId(){
        return this.id;
    }
    
    public String getUsername(){
        dbConnect db=new dbConnect();
        Map result = db.getRequest("action=Select;from=users;id="+this.id);
        try{
            if(result.get("response").equals("True")){
                username=""+result.get("username");
            }
        }catch(Exception e){
            System.err.println(e.getMessage());
            return "error";
        }
        return username;
    }
    
    
    public void setLogin(boolean newValue){
        login=newValue;
    }
    
    public void setId(int newId){
        this.id=newId;
    }

    public boolean loginValue(){
        return login;
    }
}
