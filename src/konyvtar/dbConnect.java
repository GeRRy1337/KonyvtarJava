package konyvtar;

import java.sql.*;
public class dbConnect {
    private Connection myConn;
    public dbConnect(){
        try{
            myConn= DriverManager.getConnection("jdbc:mysql://localhost:3306/konyvtar","phpAdmin","5mtYrEd0j1nWomRb");
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
    
    public ResultSet getResult(String sql){
        try{
            Statement myStmt=this.myConn.createStatement();
            ResultSet myRs = myStmt.executeQuery(sql);
            return myRs;
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
        return null;
    }
    
    public boolean insertToSql(String table,String value){
        //table: "name(rownames)" example: "users(username,password)"
        //value: "Values(values)" example: "VALUES ('username','password')"
        if(table.equals("") || value.equals("")){
            return false;
        }
        try{
            Statement myStmt=this.myConn.createStatement();
            myStmt.executeUpdate("INSERT INTO "+table +" " +value);
            return true;
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
        return false;
    }
    
    public boolean updateSql(String table,String record,String statement){
        //table: "name (+ join)" example: "table (inner join table2 on table.record = table2.record ... etc)"
        //record: "record=new value" example: "record1=value1, record2=value2"
        //statement: "Where ..."
        if(table.equals("") || record.equals("")){
            return false;
        }
        try{
            Statement myStmt=this.myConn.createStatement();
            myStmt.executeUpdate("Upate "+table +"Set " +record+" "+statement);
            return true;
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
        return false;
    }
    
    public boolean deleteFromSql(String statement){
        try{
            String query = "Delete From "+statement; 
            PreparedStatement preparedStmt = this.myConn.prepareStatement(query);
            preparedStmt.execute();
            return true;
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
        return false;
    }
}
