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
    /**
     * 
     * @param table
     *  "name(rownames)" example: "users(username,password)" <br>
     * @param value
     *  "Values(values)" example: "VALUES ('username','password')" <br>
     * @return boolean value if succesful then true else false
     */
    public boolean insertToSql(String table,String value){
        
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
    /**
     * @param table
     *  "name (+ join)" example: "table (inner join table2 on table.record = table2.record ... etc)" <br>
     * @param record
     *  "record=new value" example: "record1=value1, record2=value2" <br>
     * @param statement
     *  "Where ..." <br>
     * @return boolean value if succesful then true else false
    */
    public boolean updateSql(String table,String record,String statement){
        if(table.equals("") || record.equals("")){
            return false;
        }
        try{
            Statement myStmt=this.myConn.createStatement();
            myStmt.executeUpdate("Update "+table +"Set " +record+" "+statement);
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
