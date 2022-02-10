package konyvtar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;

public class dbConnect {
    private String defConn;
    /*private Connection myConn;
    public dbConnect(){
        try{
            myConn= DriverManager.getConnection("jdbc:mysql://localhost:3306/konyvtar","root","");
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }*/

    //https://stackoverflow.com/questions/3324717/sending-http-post-request-in-java

    public dbConnect(){
        defConn="localhost";
        try{
            File tempFile = new File("conf.config");
            boolean exists = tempFile.exists();
            if(!exists){
                tempFile.createNewFile();
                FileWriter fw=new FileWriter("conf.config");
                fw.write("address = localhost");
                fw.close();
            }
            BufferedReader conf=new BufferedReader(new FileReader("conf.config"));
            String sor;
            while((sor=conf.readLine())!=null ){
                defConn=sor.split("=")[1].trim();
            }
            conf.close();
        }catch(Exception e){ System.err.println(e); }

    }
    /**
    * @param request
    * "request data separated with ; <br>action to do (Select, Delete, Update) <br>param name then value<br> example: action=Select;username=test;password=test"
    */
    public Map getRequest(String request){
        Map responseString=new HashMap<String,String>();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("http://"+defConn+"/konyvtar/api/");
        // Request parameters and other properties.
        ArrayList<BasicNameValuePair> params = new ArrayList<>(2);
        String paramArr[]=request.split(";");
        for(int i=0;i<paramArr.length;i++){
            params.add(new BasicNameValuePair(paramArr[i].split("=")[0],paramArr[i].split("=")[1]));
        }
        params.add(new BasicNameValuePair("key", "313303ef7840acb49ba489ddb9247be4969e8a650f28eda39756556868d9c1ea"));
        httppost.setEntity(new UrlEncodedFormEntity(params, Charset.forName(StandardCharsets.UTF_8.name())));
        //Execute and get the response.
        try{
            ClassicHttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try (InputStream instream = entity.getContent()) {
                    BufferedReader br=new BufferedReader(new InputStreamReader(instream,Charset.forName(StandardCharsets.UTF_8.name())));
                    String sor;
                    int index=0;
                    while((sor = br.readLine())!=null ){
                        System.out.println(sor);
                        if (sor.split(":").length>1)
                            responseString.put(sor.split(":")[0],sor.split(":")[1]);
                        else
                            responseString.put(sor.split(":")[0]," ");
                    }
                    br.close();
                }
            }
        }catch(IOException e){
            System.err.println(e.getSuppressed());
        }
        return responseString;
    }
    
}
