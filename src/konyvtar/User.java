package konyvtar;

import java.util.Map;

public class User {
    private int id, permission;
    private String username;
    private boolean login=false;
    
    public User(){
        this.login=false;
        this.id=0;
    }
    
    public int getId(){
        return this.id;
    }
    
    public int getPermission(){
        return this.permission;
    }
    
    public String getUsername(){
        dbConnect db=new dbConnect();
        Map result = db.getRequest("action=Select;from=users;id="+this.id);
        try{
            if(result.get("response").equals("True")){
                this.username=""+result.get("username");
            }
        }catch(Exception e){
            System.err.println(e.getMessage());
            return "error";
        }
        return this.username;
    }
    
    public void updatePermission(){
        dbConnect db=new dbConnect();
        Map result=db.getRequest("action=getPermission;username="+this.username);
        if(result.get("response").equals("True")){
            this.permission=Integer.parseInt( String.valueOf( result.get("permission") ) );
        }else{
            this.permission=0;
        }
    }
    
    public void setLogin(boolean newValue){
        this.login=newValue;
    }
    
    public void setId(int newId){
        this.id=newId;
    }

    public void setPermission(int permission){
        this.permission=permission;
    }
    
    public boolean loginValue(){
        return this.login;
    }
}
