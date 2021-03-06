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
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.entity.mime.StringBody;
import org.apache.hc.core5.http.ContentType;

/**
 * Api kapcsolat osztálya
 *
 * http kapcsolódás forrása:
 * https://stackoverflow.com/questions/3324717/sending-http-post-request-in-java
 *
 * @param defConn a webszerver domain címe
 * @param defRepo a webszerveren belül az api mappa elérésí útja <br> pl:
 * public/html/api esetén public/html
 */
public class dbConnect {

    private String defConn, defRepo;

    public dbConnect() {
        defConn = "localhost";
        defRepo = "konyvtar";
        try {
            //létezik e a config fájl ha nem alapértelmezett értékekkel létrehozás
            File tempFile = new File("conf.config");
            boolean exists = tempFile.exists();
            if (!exists) {
                tempFile.createNewFile();
                FileWriter fw = new FileWriter("conf.config");
                fw.write("address = localhost\n");
                fw.write("url = konyvtar");
                fw.close();
            }
            //a webszerver elérési út mentése
            BufferedReader conf = new BufferedReader(new FileReader("conf.config"));
            String sor;
            if ((sor = conf.readLine()) != null) {
                defConn = sor.split("=")[1].trim();
            }
            if ((sor = conf.readLine()) != null) {
                defRepo = sor.split("=")[1].trim();
            }

            conf.close();
        } catch (Exception e) {
            System.err.println(e);
        }

    }

    /**
     * @param request sql parancsok listája; <br>pl: végrehajtandó művelet
     * (Select, Delete, Update, ..) <br>kulcs érték fomrában megadva<br> pl:
     * action=Select;username=test;password=test
     */
    public Map getRequest(String request) {
        // az api kulcs:érték párokkal kommunikál
        Map responseString = new HashMap<String, String>();

        //http kapcsolat kiépítése
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("http://" + defConn + "/" + defRepo + "/api/");
        //elküldendő értékek szétválogatása stringből és tárolása listában
        ArrayList<BasicNameValuePair> params = new ArrayList<>(2);
        String paramArr[] = request.split(";");
        for (int i = 0; i < paramArr.length; i++) {
            params.add(new BasicNameValuePair(paramArr[i].split("=")[0], paramArr[i].split("=")[1]));
        }
        //képfeltöltés ellenőrzés
        int index = 0;
        for (BasicNameValuePair b : params) {
            if (b.getName().equals("img")) {
                index = params.indexOf(b);
                break;
            }
        }
        if (index > 0) {
            //kép feltöltése
            try {
                FileBody uploadFile = new FileBody(new File(params.get(index).getValue()));
                MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
                reqEntity.addPart("key", new StringBody("313303ef7840acb49ba489ddb9247be4969e8a650f28eda39756556868d9c1ea", ContentType.TEXT_PLAIN));
                reqEntity.addPart("uploaded_file", uploadFile);
                httppost.setEntity(reqEntity.build());
                httpclient.execute(httppost);
            } catch (Exception e) {
                System.err.println(e);
            }
        }
        params.add(new BasicNameValuePair("key", "313303ef7840acb49ba489ddb9247be4969e8a650f28eda39756556868d9c1ea"));
        httppost.setEntity(new UrlEncodedFormEntity(params, Charset.forName(StandardCharsets.UTF_8.name())));
        //többi parancs lefuttatása
        try {
            //válasz mentése, az api kulcs:érték ként fog választ küldeni
            ClassicHttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try ( InputStream instream = entity.getContent()) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(instream, Charset.forName(StandardCharsets.UTF_8.name())));
                    String sor;
                    while ((sor = br.readLine()) != null) {
                        if (sor.split(":").length > 1) {
                            responseString.put(sor.split(":")[0], sor.split(":")[1]);
                        } else {
                            responseString.put(sor.split(":")[0], " ");
                        }
                    }
                    br.close();
                }
            }
        } catch (IOException e) {
            System.err.println(e.getSuppressed());
        }
        return responseString;
    }

}
