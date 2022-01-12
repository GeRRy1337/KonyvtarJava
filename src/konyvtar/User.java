package konyvtar;

import java.sql.ResultSet;

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
