/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konyvtar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;

/**
 *
 * @author Gergő
 */
public class Login extends javax.swing.JFrame {

    /**
     * Creates new form Login
     */
    public Login() {
        initComponents();
        testApi();
    }

    private void onLoggedIn(){
        if(user.getText().equals("")){
            JOptionPane.showMessageDialog(rootPane, "Nem írtál be felhasználó nevet!","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(String.valueOf(pass.getPassword()).equals("")){
            JOptionPane.showMessageDialog(rootPane, "Nem írtál be jelszót!","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        dbConnect sql = new dbConnect();
        String myHash="";
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(String.valueOf(pass.getPassword()).getBytes());
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1,digest);
            myHash = bigInt.toString(16);
        }catch(Exception e){System.err.println(e.getMessage());}
        ResultSet result = sql.getResult("Select id from users Where username=\""+user.getText()+"\" and password=\""+myHash+"\"");
        try{
            while(result.next()){
                if (sql.getResult("Select id from users Where id="+result.getInt("id")+" and id in (select id from admins)").next()){
                    System.err.println("belépve");
                    Main.GuiWindow.returnLogin(result.getInt("id"));
                    user.setText("");
                    pass.setText("");
                    this.dispose();
                    return;
                }else{
                    JOptionPane.showMessageDialog(rootPane, "Nincs megfelelő jogosultságod!","Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
            }
            JOptionPane.showMessageDialog(rootPane, "Nem létezik ilyen felhasználó ezzel a jelszóval!","Error",JOptionPane.ERROR_MESSAGE);
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(rootPane, "SQL error Kérlek próbáld újra késöbb!","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void testApi(){
        //https://stackoverflow.com/questions/3324717/sending-http-post-request-in-java
        String defConn="localhost";
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
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("http://"+defConn+"/konyvtar/api/");
        // Request parameters and other properties.
        ArrayList<BasicNameValuePair> params = new ArrayList<>(2);
        params.add(new BasicNameValuePair("test", "Hello!"));
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
                    while((sor = br.readLine())!=null ){
                        System.out.println(sor);
                    }
                    br.close();
                }
            }
        }catch(IOException e){
            System.err.println(e);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        pass = new javax.swing.JPasswordField();
        user = new javax.swing.JTextField();
        submit = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");

        submit.setText("Belépés");
        submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitActionPerformed(evt);
            }
        });

        jLabel1.setText("Felhasználónév:");

        jLabel2.setText("Jelszó:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(user, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                            .addComponent(pass)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(132, 132, 132)
                        .addComponent(submit, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(86, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(107, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(user))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(submit)
                .addGap(94, 94, 94))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void submitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitActionPerformed
        onLoggedIn();
    }//GEN-LAST:event_submitActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPasswordField pass;
    private javax.swing.JButton submit;
    private javax.swing.JTextField user;
    // End of variables declaration//GEN-END:variables
}
