package konyvtar;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Gergő
 */
public class Gui extends javax.swing.JFrame {

    /**
     * Creates new form Gui
     */
    private final User user = new User();
    protected Login loginWindow = new Login();
    private JFileChooser chooseImage;

    public Gui() {
        initComponents();
    }

    public boolean checkLogin() {
        if (!user.loginValue()) {
            this.setVisible(false);
            loginWindow.setVisible(true);
            return true;
        } else {
            setDisplayName(user.getUsername());
            if (user.getPermission() < 2) {
                adminButton.setEnabled(false);
                adminButton.setVisible(false);
            } else {
                adminButton.setEnabled(true);
                adminButton.setVisible(true);
            }
        }
        return false;
    }

    protected void returnLogin(int id, int permission) {
        this.setVisible(true);
        user.setLogin(true);
        user.setId(id);
        user.setPermission(permission);
        //setDisplayName(user.getUsername());
        checkLogin();
    }

    private void setDisplayName(String newName) {
        displayName.setText(newName);
    }

    private void logout() {
        user.setLogin(false);
        switchPanel(borrow);
        checkLogin();
    }

    private void switchPanel(javax.swing.JPanel panel) {
        if (panel == adminPanel) {
            user.updatePermission();
            if (user.getPermission() < 2) {
                adminButton.setEnabled(false);
                adminButton.setVisible(false);
                switchPanel(borrow);
                return;
            } else {
                adminButton.setEnabled(true);
                adminButton.setVisible(true);
                updateAdmins();
                updateUsers();
            }
        }
        for (Component c : borrow.getComponents()) {
            c.setEnabled(false);
        }
        for (Component c : uploadBook.getComponents()) {
            c.setEnabled(false);
        }
        for (Component c : cardManager.getComponents()) {
            c.setEnabled(false);
        }
        for (Component c : adminPanel.getComponents()) {
            c.setEnabled(false);
        }
        for (Component c : panel.getComponents()) {
            c.setEnabled(true);
        }
        mainPanel.moveToFront(panel);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void updateBookISBN() {
        if (!uploadBookISBN.getText().equals("")) {
            dbConnect db = new dbConnect();
            Map result = db.getRequest("action=Select;from=books;ISBN=" + uploadBookISBN.getText());
            try {
                if (result.get("response").equals("True")) {
                    uploadBookTitle.setText(String.valueOf(result.get("BookTitle")));
                    uploadBookPublisher.setText(String.valueOf(result.get("Publisher")));
                    uploadBookYear.setValue(Integer.parseInt(String.valueOf(result.get("YearOfPublication"))));
                    Map author = db.getRequest("action=Select;from=author;id=" + result.get("id"));
                    if (author.get("response").equals("True")) {
                        uploadBookAuthor.setText(String.valueOf(author.get("name")));
                    } else {
                        uploadBookAuthor.setText("Ismeretlen");
                    }
                } else {
                    uploadBookTitle.setText("");
                    uploadBookPublisher.setText("");
                    uploadBookYear.setValue(0);
                    uploadBookAuthor.setText("");
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(rootPane, "SQL error Kérlek próbáld újra késöbb!", "Hiba", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            uploadBookTitle.setText("");
            uploadBookPublisher.setText("");
            uploadBookYear.setValue(0);
            uploadBookAuthor.setText("");
        }
    }

    public void updateAdmins() {
        dbConnect db = new dbConnect();
        Map result = db.getRequest("action=adminList");
        if (result.get("response").equals("True")) {
            adminRemoveUser.removeAllItems();
            String response = String.valueOf(result.get("users")).replace("\"", "");
            String users[] = response.substring(1, response.length() - 1).split(",");
            for (String s : users) {
                adminRemoveUser.addItem(s);
            }
        }
    }

    public void updateUsers() {
        dbConnect db = new dbConnect();
        Map result = db.getRequest("action=userList");
        if (result.get("response").equals("True")) {
            adminAddUser.removeAllItems();
            String response = String.valueOf(result.get("users")).replace("\"", "");
            String users[] = response.substring(1, response.length() - 1).split(",");
            for (String s : users) {
                adminAddUser.addItem(s);
            }
        }
    }

    public void updateCurrentPermission() {
        dbConnect db = new dbConnect();
        Map result = db.getRequest("action=getPermission;username=" + adminRemoveUser.getSelectedItem());
        if (result.get("response").equals("True")) {
            if (result.get("permission").equals("1")) {
                currentPermission.setText("Könyvtáros");
            } else if (result.get("permission").equals("2")) {
                currentPermission.setText("Rendszergazda");
            } else {
                currentPermission.setText("Ismeretlen");
            }
        }
    }

    public boolean CheckNewCards() {
        if (newCardName.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Nem adtál meg nevet.", "Hiba", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (newCardAddress.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Nem adtál meg lakcímet.", "Hiba", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (newCardPhone.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Nem adtál meg telefonszámot.", "Hiba", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (newCardDate.getDate() == null) {
            JOptionPane.showMessageDialog(rootPane, "Nem adtál meg születési dátumot.", "Hiba", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (newCardPhone.getText().matches("[^0-9]+$")) {
            JOptionPane.showMessageDialog(rootPane, "A telefonszám csak számokat tartalmazhat.", "Hiba", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton6 = new javax.swing.JButton();
        MenuPanel = new javax.swing.JPanel();
        displayName = new javax.swing.JLabel();
        borrowButton = new javax.swing.JButton();
        newBookButton = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        adminButton = new javax.swing.JButton();
        mainPanel = new javax.swing.JLayeredPane();
        borrow = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        CardNumber = new javax.swing.JTextField();
        borrowStockNum = new javax.swing.JTextField();
        UpBorrow = new javax.swing.JButton();
        scannerCardBorrow = new javax.swing.JToggleButton();
        scannerStockNumBorrow = new javax.swing.JToggleButton();
        jLabel6 = new javax.swing.JLabel();
        borrowDate = new com.toedter.calendar.JDateChooser();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        borrowStockNumBack = new javax.swing.JTextField();
        scannerStockNumBorrowBack = new javax.swing.JToggleButton();
        borrowBackButton = new javax.swing.JButton();
        uploadBook = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        uploadBookAuthor = new javax.swing.JTextField();
        uploadBookTitle = new javax.swing.JTextField();
        uploadBookISBN = new javax.swing.JTextField();
        uploadBookSubmit = new javax.swing.JButton();
        scannerISBNupload = new javax.swing.JToggleButton();
        uploadBookStockNum = new javax.swing.JTextField();
        scannerStockNumUpload = new javax.swing.JToggleButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        uploadBookYear = new com.toedter.calendar.JYearChooser();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        uploadBookPublisher = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        fileChooserButtonNew = new javax.swing.JButton();
        fileChooserStringNew = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        cardManager = new javax.swing.JPanel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        newCardName = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        cardUser = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        cardUserNumber = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        newCardAddress = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        newCardPhone = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        newCardDate = new com.toedter.calendar.JDateChooser();
        jButton2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        preview = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        cardValidNumber = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        cardValidDate = new com.toedter.calendar.JDateChooser();
        jLabel28 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        adminPanel = new javax.swing.JPanel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel18 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        adminAddUser = new javax.swing.JComboBox<>();
        jLabel34 = new javax.swing.JLabel();
        adminAddPermission = new javax.swing.JComboBox<>();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        adminRemoveUser = new javax.swing.JComboBox<>();
        jLabel37 = new javax.swing.JLabel();
        adminRemovePermission = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        currentPermission = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();

        jButton6.setText("jButton6");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Main");
        setResizable(false);

        MenuPanel.setBackground(new java.awt.Color(193, 118, 70));

        displayName.setText("jLabel1");

        borrowButton.setText("Kölcsönzés");
        borrowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrowButtonActionPerformed(evt);
            }
        });

        newBookButton.setText("Új Könyv");
        newBookButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newBookButtonActionPerformed(evt);
            }
        });

        jButton3.setText("Kártyák kezelése");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Kijelentkezés");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        adminButton.setText("Adminok");
        adminButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MenuPanelLayout = new javax.swing.GroupLayout(MenuPanel);
        MenuPanel.setLayout(MenuPanelLayout);
        MenuPanelLayout.setHorizontalGroup(
            MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(displayName, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                .addGap(101, 101, 101)
                .addComponent(borrowButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newBookButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(adminButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addGap(20, 20, 20))
        );
        MenuPanelLayout.setVerticalGroup(
            MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MenuPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(displayName)
                    .addComponent(borrowButton)
                    .addComponent(newBookButton)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(adminButton))
                .addContainerGap())
        );

        switchPanel(borrow);

        jLabel1.setText("Kártya azonosító:");

        jLabel2.setText("Könyv Azonosító:");

        CardNumber.setText("kártyaszám");

        borrowStockNum.setText("könyvtári szám");

        UpBorrow.setText("Küldés");
        UpBorrow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpBorrowActionPerformed(evt);
            }
        });

        scannerCardBorrow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scannerCardBorrowActionPerformed(evt);
            }
        });

        scannerStockNumBorrow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scannerStockNumBorrowActionPerformed(evt);
            }
        });

        jLabel6.setText("Visszahozás Dátuma:");

        borrowDate.setMinSelectableDate(new Date(System.currentTimeMillis()));

        jSeparator2.setBackground(new java.awt.Color(102, 102, 255));
        jSeparator2.setForeground(new java.awt.Color(102, 102, 255));
        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel13.setText("Köynv kikölcsönzése");

        jLabel14.setText("Kikölcsönzött könyv leadása");

        jLabel15.setText("Könyv Azonosító:");

        borrowStockNumBack.setText("könyvtári szám");

        scannerStockNumBorrowBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scannerStockNumBorrowBackActionPerformed(evt);
            }
        });

        borrowBackButton.setText("Küldés");
        borrowBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrowBackButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout borrowLayout = new javax.swing.GroupLayout(borrow);
        borrow.setLayout(borrowLayout);
        borrowLayout.setHorizontalGroup(
            borrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(borrowLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(borrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(borrowLayout.createSequentialGroup()
                        .addGroup(borrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(borrowLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(borrowDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(borrowLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(borrowStockNum))
                            .addGroup(borrowLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addGroup(borrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addComponent(CardNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(borrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(borrowLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(borrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(borrowLayout.createSequentialGroup()
                                        .addComponent(scannerStockNumBorrow)
                                        .addContainerGap(439, Short.MAX_VALUE))
                                    .addGroup(borrowLayout.createSequentialGroup()
                                        .addComponent(scannerCardBorrow)
                                        .addGap(53, 53, 53)
                                        .addGroup(borrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(borrowLayout.createSequentialGroup()
                                                .addComponent(borrowBackButton)
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGroup(borrowLayout.createSequentialGroup()
                                                .addComponent(jLabel15)
                                                .addGap(18, 18, 18)
                                                .addComponent(borrowStockNumBack)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(scannerStockNumBorrowBack)
                                                .addGap(60, 60, 60))))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, borrowLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel14)
                                .addGap(107, 107, 107))))
                    .addGroup(borrowLayout.createSequentialGroup()
                        .addComponent(UpBorrow)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(borrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(borrowLayout.createSequentialGroup()
                    .addGap(354, 354, 354)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(360, Short.MAX_VALUE)))
        );
        borrowLayout.setVerticalGroup(
            borrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(borrowLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(borrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(borrowLayout.createSequentialGroup()
                        .addGroup(borrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addGap(45, 45, 45)
                        .addGroup(borrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(CardNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(scannerCardBorrow, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(borrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(scannerStockNumBorrowBack, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(borrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(borrowStockNumBack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(borrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(borrowStockNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scannerStockNumBorrow, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(borrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(borrowDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 169, Short.MAX_VALUE)
                .addGroup(borrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(UpBorrow)
                    .addComponent(borrowBackButton))
                .addContainerGap())
            .addGroup(borrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE))
        );

        jLabel3.setText("Iró");

        jLabel4.setText("Cím");

        jLabel5.setText("ISBN");

        uploadBookISBN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                uploadBookISBNKeyReleased(evt);
            }
        });

        uploadBookSubmit.setText("Küldés");
        uploadBookSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadBookSubmitActionPerformed(evt);
            }
        });

        scannerISBNupload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scannerISBNuploadActionPerformed(evt);
            }
        });

        scannerStockNumUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scannerStockNumUploadActionPerformed(evt);
            }
        });

        jLabel7.setText("Könyvtári szám");

        jLabel8.setText("Kiadás Dátuma");

        jSeparator1.setBackground(new java.awt.Color(102, 102, 255));
        jSeparator1.setForeground(new java.awt.Color(102, 102, 255));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel9.setText("Raktári könyv kezelése");
        jLabel9.setToolTipText("Kötelező megadni");

        jLabel10.setText("Új könyv adatai");
        jLabel10.setToolTipText("Megadásuk csak akkor kötelező ha a könyv nem szerepel még az adatbázisban");

        jLabel11.setText("Kiadó:");

        jLabel12.setText("Kép:");

        fileChooserButtonNew.setText("Tallózás");
        fileChooserButtonNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileChooserButtonNewActionPerformed(evt);
            }
        });

        jButton9.setText("Törlés");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("i");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout uploadBookLayout = new javax.swing.GroupLayout(uploadBook);
        uploadBook.setLayout(uploadBookLayout);
        uploadBookLayout.setHorizontalGroup(
            uploadBookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(uploadBookLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(uploadBookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(uploadBookLayout.createSequentialGroup()
                        .addComponent(uploadBookSubmit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton9))
                    .addGroup(uploadBookLayout.createSequentialGroup()
                        .addComponent(jButton10)
                        .addGap(37, 37, 37)
                        .addComponent(jLabel9))
                    .addGroup(uploadBookLayout.createSequentialGroup()
                        .addGroup(uploadBookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(uploadBookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(uploadBookLayout.createSequentialGroup()
                                .addComponent(uploadBookStockNum)
                                .addGap(18, 18, 18)
                                .addComponent(scannerStockNumUpload))
                            .addGroup(uploadBookLayout.createSequentialGroup()
                                .addComponent(uploadBookISBN, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(scannerISBNupload)))))
                .addGap(70, 70, 70)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(uploadBookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(uploadBookLayout.createSequentialGroup()
                        .addGap(133, 133, 133)
                        .addComponent(jLabel10)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(uploadBookLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(uploadBookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12)
                            .addComponent(jLabel8))
                        .addGap(16, 16, 16)
                        .addGroup(uploadBookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(uploadBookLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(uploadBookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(uploadBookAuthor, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                                    .addComponent(uploadBookTitle)))
                            .addComponent(uploadBookPublisher)
                            .addComponent(uploadBookYear, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                            .addComponent(fileChooserStringNew))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(fileChooserButtonNew)
                        .addGap(19, 19, 19))))
        );
        uploadBookLayout.setVerticalGroup(
            uploadBookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(uploadBookLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(uploadBookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(uploadBookLayout.createSequentialGroup()
                        .addGroup(uploadBookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jButton10))
                        .addGap(39, 39, 39)
                        .addGroup(uploadBookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(uploadBookISBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(scannerISBNupload, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(uploadBookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(uploadBookStockNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(scannerStockNumUpload, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(uploadBookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(uploadBookSubmit)
                            .addComponent(jButton9)))
                    .addGroup(uploadBookLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(42, 42, 42)
                        .addGroup(uploadBookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(uploadBookAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(uploadBookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(uploadBookTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(uploadBookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(uploadBookPublisher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(uploadBookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addComponent(uploadBookYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(uploadBookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(fileChooserButtonNew)
                            .addComponent(fileChooserStringNew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 123, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jSeparator4.setBackground(new java.awt.Color(102, 102, 255));
        jSeparator4.setForeground(new java.awt.Color(102, 102, 255));
        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel19.setText("Új kártya kiállítása");

        jLabel20.setText("Kártya felhasználóhoz rendelése");
        jLabel20.setToolTipText("");

        jLabel21.setText("Név:");

        jLabel22.setText("Felhasználónév");

        jLabel23.setText("Kártyaszám");

        jButton1.setText("Küldés");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel24.setText("Lakcím:");

        jLabel25.setText("Telefonszám:");

        jLabel16.setText("Születési dátum:");

        newCardDate.setMaxSelectableDate(new Date());

        jButton2.setText("Előnézet");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton5.setText("Mentés");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel26.setText("Kártya meghosszabítása");

        jLabel27.setText("Kártyaszám");

        jButton11.setText("Meghosszabítás");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        cardValidDate.setMinSelectableDate(new Date());

        jLabel28.setText("Lejárati dátum:");

        jButton12.setText("Kártyák listája");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cardManagerLayout = new javax.swing.GroupLayout(cardManager);
        cardManager.setLayout(cardManagerLayout);
        cardManagerLayout.setHorizontalGroup(
            cardManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardManagerLayout.createSequentialGroup()
                .addGroup(cardManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(cardManagerLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(preview, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(cardManagerLayout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel20))
                    .addGroup(cardManagerLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(cardManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardManagerLayout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addGap(18, 18, 18)
                                .addComponent(newCardAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardManagerLayout.createSequentialGroup()
                                .addGroup(cardManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel25))
                                .addGap(18, 18, 18)
                                .addGroup(cardManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(newCardPhone)
                                    .addComponent(newCardDate, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardManagerLayout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addGap(18, 18, 18)
                                .addComponent(newCardName, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardManagerLayout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addGap(18, 18, 18)
                                .addComponent(jButton5)
                                .addGap(119, 119, 119)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 146, Short.MAX_VALUE)
                        .addGroup(cardManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(cardUser, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1)
                            .addComponent(jLabel23)
                            .addComponent(cardUserNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(112, 112, 112))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardManagerLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(cardManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardManagerLayout.createSequentialGroup()
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardManagerLayout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addGap(155, 155, 155))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardManagerLayout.createSequentialGroup()
                        .addGroup(cardManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(cardManagerLayout.createSequentialGroup()
                                .addComponent(jButton11)
                                .addGap(18, 18, 18)
                                .addComponent(jButton12))
                            .addComponent(jLabel28)
                            .addComponent(jLabel27)
                            .addComponent(cardValidNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cardValidDate, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(85, 85, 85))))
            .addGroup(cardManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(cardManagerLayout.createSequentialGroup()
                    .addGap(0, 355, Short.MAX_VALUE)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 355, Short.MAX_VALUE)))
        );
        cardManagerLayout.setVerticalGroup(
            cardManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardManagerLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(cardManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(cardManagerLayout.createSequentialGroup()
                        .addGroup(cardManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20))
                        .addGap(10, 10, 10)
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cardUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cardUserNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addGap(40, 40, 40)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel26))
                    .addGroup(cardManagerLayout.createSequentialGroup()
                        .addGroup(cardManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(newCardName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(cardManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(newCardAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(cardManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(newCardPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(cardManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(newCardDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(41, 41, 41))
                    .addGroup(cardManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2)
                        .addComponent(jButton5)))
                .addGap(18, 18, 18)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cardValidNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cardValidDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addGroup(cardManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton11)
                    .addComponent(jButton12))
                .addGap(8, 8, 8)
                .addComponent(preview, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4))
            .addGroup(cardManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(cardManagerLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jSeparator4, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jSeparator5.setBackground(new java.awt.Color(102, 102, 255));
        jSeparator5.setForeground(new java.awt.Color(102, 102, 255));
        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel18.setText("Admin hozzáadása");

        jLabel33.setText("Admin törlése/módosítása");

        jLabel34.setText("Felhasználónév:");

        adminAddPermission.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Könyvtáros", "Rendszergazda" }));

        jLabel35.setText("Engedély szint:");

        jLabel36.setText("Felhasználónév:");

        adminRemoveUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminRemoveUserActionPerformed(evt);
            }
        });

        jLabel37.setText("Engedély szint:");

        adminRemovePermission.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Könyvtáros", "Rendszergazda", "Törlés" }));
        adminRemovePermission.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminRemovePermissionActionPerformed(evt);
            }
        });

        jLabel17.setText("Jelenlegi engedély szint:");

        jButton7.setText("Hozzáadás");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Törlés/ módosítás");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout adminPanelLayout = new javax.swing.GroupLayout(adminPanel);
        adminPanel.setLayout(adminPanelLayout);
        adminPanelLayout.setHorizontalGroup(
            adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminPanelLayout.createSequentialGroup()
                .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(adminPanelLayout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addComponent(jLabel18))
                    .addGroup(adminPanelLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(adminPanelLayout.createSequentialGroup()
                                .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel34)
                                    .addComponent(jLabel35))
                                .addGap(18, 18, 18)
                                .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(adminAddUser, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(adminAddPermission, 0, 174, Short.MAX_VALUE)))
                            .addComponent(jButton7))))
                .addGap(67, 67, 67)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(adminPanelLayout.createSequentialGroup()
                        .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(adminPanelLayout.createSequentialGroup()
                                .addGap(141, 141, 141)
                                .addComponent(jLabel33))
                            .addGroup(adminPanelLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(adminPanelLayout.createSequentialGroup()
                                        .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel36)
                                            .addComponent(jLabel37))
                                        .addGap(18, 18, 18)
                                        .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(adminRemovePermission, 0, 174, Short.MAX_VALUE)
                                            .addComponent(adminRemoveUser, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGroup(adminPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel17)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(currentPermission, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                        .addGap(0, 96, Short.MAX_VALUE))
                    .addGroup(adminPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        adminPanelLayout.setVerticalGroup(
            adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                    .addGroup(adminPanelLayout.createSequentialGroup()
                        .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(jLabel33))
                        .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(adminPanelLayout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel34)
                                    .addComponent(adminAddUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(34, 34, 34)
                                .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(adminAddPermission, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel35)))
                            .addGroup(adminPanelLayout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel36)
                                    .addComponent(adminRemoveUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(adminRemovePermission, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel37))))
                        .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(adminPanelLayout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel17)
                                    .addComponent(currentPermission, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(35, 35, 35)
                                .addComponent(jButton8))
                            .addGroup(adminPanelLayout.createSequentialGroup()
                                .addGap(89, 89, 89)
                                .addComponent(jButton7)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        mainPanel.setLayer(borrow, javax.swing.JLayeredPane.DEFAULT_LAYER);
        mainPanel.setLayer(uploadBook, javax.swing.JLayeredPane.DEFAULT_LAYER);
        mainPanel.setLayer(cardManager, javax.swing.JLayeredPane.DEFAULT_LAYER);
        mainPanel.setLayer(adminPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(borrow, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(uploadBook, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(mainPanelLayout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(cardManager, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(mainPanelLayout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(adminPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(borrow, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(uploadBook, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(cardManager, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(mainPanelLayout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(adminPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MenuPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(MenuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainPanel)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        logout();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void borrowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrowButtonActionPerformed
        switchPanel(borrow);
    }//GEN-LAST:event_borrowButtonActionPerformed

    private void newBookButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newBookButtonActionPerformed
        switchPanel(uploadBook);
    }//GEN-LAST:event_newBookButtonActionPerformed

    private void scannerCardBorrowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scannerCardBorrowActionPerformed
        if (scannerCardBorrow.isSelected()) {
            CardNumber.setText(JOptionPane.showInputDialog(""));
            scannerCardBorrow.setSelected(false);
        }
    }//GEN-LAST:event_scannerCardBorrowActionPerformed

    private void scannerStockNumBorrowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scannerStockNumBorrowActionPerformed
        if (scannerStockNumBorrow.isSelected()) {
            borrowStockNum.setText(JOptionPane.showInputDialog(""));
            scannerStockNumBorrow.setSelected(false);
        }
    }//GEN-LAST:event_scannerStockNumBorrowActionPerformed

    private void scannerISBNuploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scannerISBNuploadActionPerformed
        if (scannerISBNupload.isSelected()) {
            uploadBookISBN.setText(JOptionPane.showInputDialog(""));
            scannerISBNupload.setSelected(false);
            updateBookISBN();
        }
    }//GEN-LAST:event_scannerISBNuploadActionPerformed

    private void UpBorrowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpBorrowActionPerformed
        if (CardNumber.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Nem adtál meg kártya számot!", "Hiba", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (borrowStockNum.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Nem adtál meg könyv azonosítót!", "Hiba", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (borrowDate.getDate() == null) {
            JOptionPane.showMessageDialog(rootPane, "Nem adtál visszahozási dátumot!", "Hiba", JOptionPane.ERROR_MESSAGE);
            return;
        }
        dbConnect db = new dbConnect();
        Map result = db.getRequest("action=Select;from=cards;id=" + CardNumber.getText());
        try {
            if (result.get("response").equals("False")) {
                JOptionPane.showMessageDialog(rootPane, "Hibás kártyaszám!", "Hiba", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                String validDate[] = String.valueOf(result.get("valid")).split("-");
                int year = Integer.parseInt(validDate[0]), month = Integer.parseInt(validDate[1]), day = Integer.parseInt(validDate[2]);
                if (new Date(year, month, day).before(new Date())) {
                    JOptionPane.showMessageDialog(rootPane, "Ez a kártya már lejárt!", "Hiba", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            result = db.getRequest("action=Select;from=stock;stockNum=" + borrowStockNum.getText());
            if (result.get("response").equals("False")) {
                JOptionPane.showMessageDialog(rootPane, "Hibás könyv azonosító!", "Hiba", JOptionPane.ERROR_MESSAGE);
                return;
            }
            result = db.getRequest("action=Select;from=borrow;stockNum=" + borrowStockNum.getText() + ";state=0");
            if (result.get("response").equals("True")) {
                String errorString = String.format("Kártyaszám:%s, Könyv azonosító:%s, Határidő:%s", result.get("cardNum"), result.get("stockNum"), result.get("date"));
                JOptionPane.showMessageDialog(rootPane, "Ez a könyv ki van kölcsönözve: " + errorString, "Hiba", JOptionPane.ERROR_MESSAGE);
                return;
            }
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");

            result = db.getRequest("action=Insert;to=borrow(cardNum,stockNum,date,state);values=" + String.format("VALUES( %s,%s,'%s',0 )", CardNumber.getText(), borrowStockNum.getText(), String.valueOf(sd.format(borrowDate.getDate()))));
            if (result.get("response").equals("True")) {
                JOptionPane.showMessageDialog(rootPane, "Sikeres kölcsönzés!", "Info", JOptionPane.INFORMATION_MESSAGE);
                CardNumber.setText("kártyaszám");
                borrowStockNum.setText("könyvtári szám");
                borrowDate.setDate(null);
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(rootPane, "SQL error Kérlek próbáld újra késöbb!", "Hiba", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_UpBorrowActionPerformed

    private void scannerStockNumUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scannerStockNumUploadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_scannerStockNumUploadActionPerformed

    private void fileChooserButtonNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileChooserButtonNewActionPerformed
        chooseImage = new JFileChooser();
        chooseImage.setMultiSelectionEnabled(false);
        chooseImage.setCurrentDirectory(new File("C:\\tmp"));
        chooseImage.setAcceptAllFileFilterUsed(false);
        chooseImage.addChoosableFileFilter(new FileNameExtensionFilter("Image Files (.png, .jpg, .tif)", "jpg", "png", "tif"));
        int retVal = chooseImage.showOpenDialog(rootPane);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            File selectedfiles = chooseImage.getSelectedFile();
            fileChooserStringNew.setText(selectedfiles.getAbsolutePath());
        }

    }//GEN-LAST:event_fileChooserButtonNewActionPerformed

    private void uploadBookSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadBookSubmitActionPerformed
        if (uploadBookISBN.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Nem adtál meg ISBN számot!", "Hiba", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (uploadBookStockNum.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Nem adtál meg könyv azonosítót!", "Hiba", JOptionPane.ERROR_MESSAGE);
            return;
        }
        dbConnect db = new dbConnect();
        Map result = db.getRequest("action=Select;from=books;ISBN=" + uploadBookISBN.getText());
        try {
            if (result.get("response").equals("True")) {
                Map bookId = db.getRequest("action=Select;from=books;ISBN=" + uploadBookISBN.getText());
                if (bookId.get("response").equals("True")) {
                    result = db.getRequest("action=Insert;to=stock(bookId,stockNum);values=" + String.format("VALUES(%s,%s)", bookId.get("id"), uploadBookStockNum.getText()));
                }
                if (result.get("response").equals("True")) {
                    JOptionPane.showMessageDialog(rootPane, "Sikeres könyv feltötltés!", "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Sikertelen könyv feltötltés!", "Hiba", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (uploadBookTitle.getText().equals("")) {
                    JOptionPane.showMessageDialog(rootPane, "Nem adtad meg a könyv címét!", "Hiba", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (uploadBookAuthor.getText().equals("")) {
                    JOptionPane.showMessageDialog(rootPane, "Nem adtad meg az írót!", "Hiba", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (uploadBookPublisher.getText().equals("")) {
                    JOptionPane.showMessageDialog(rootPane, "Nem adtál meg kiadót!", "Hiba", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (uploadBookYear.getYear() < 0) {
                    JOptionPane.showMessageDialog(rootPane, "Nem adtál meg kiadási dátumot!", "Hiba", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Map authorId = db.getRequest("action=Select;from=author;name=" + uploadBookAuthor.getText());
                if (authorId.get("response").equals("True")) {
                    result = db.getRequest("action=Insert;img=" + fileChooserStringNew.getText() + ";to=books(ISBN,BookTitle,AuthorId,YearOfPublication,Publisher,ImageUrlL);values=" + String.format("VALUES( %s, '%s', %s, '%s', '%s', blank )", uploadBookISBN.getText(), uploadBookTitle.getText(), authorId.get("id"), uploadBookYear.getYear(), uploadBookPublisher.getText()));
                    if (result.get("response").equals("True")) {
                        Map bookId = db.getRequest("action=Select;from=books;ISBN=" + uploadBookISBN.getText());
                        if (bookId.get("response").equals("True")) {
                            result = db.getRequest("action=Insert;to=stock(bookId,stockNum);values=" + String.format("VALUES(%s,%s)", bookId.get("id"), uploadBookStockNum.getText()));
                            if (result.get("response").equals("True")) {
                                JOptionPane.showMessageDialog(rootPane, "Sikeres könyv feltötltés!", "Info", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }
                } else {
                    result = db.getRequest("action=Insert;to=author(name,birthDate);values=" + String.format("VALUES('%s','0')", uploadBookAuthor.getText()));
                    if (result.get("response").equals("True")) {
                        authorId = db.getRequest("action=Select;from=author;name=" + uploadBookAuthor.getText());
                        if (authorId.get("response").equals("True")) {
                            result = db.getRequest("action=Insert;img=" + fileChooserStringNew.getText() + ";to=books(ISBN,BookTitle,AuthorId,YearOfPublication,Publisher,ImageUrlL);values=" + String.format("VALUES( %s, '%s', %s, '%s', '%s', blank )", uploadBookISBN.getText(), uploadBookTitle.getText(), authorId.get("id"), uploadBookYear.getYear(), uploadBookPublisher.getText()));
                            if (result.get("response").equals("True")) {
                                Map bookId = db.getRequest("action=Select;from=books;ISBN=" + uploadBookISBN.getText());
                                if (bookId.get("response").equals("True")) {
                                    result = db.getRequest("action=Insert;to=stock(bookId,stockNum);values=" + String.format("VALUES(%s,%s)", bookId.get("id"), uploadBookStockNum.getText()));
                                    if (result.get("response").equals("True")) {
                                        JOptionPane.showMessageDialog(rootPane, "Sikeres könyv feltötltés!", "Info", JOptionPane.INFORMATION_MESSAGE);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            uploadBookYear.setYear(1);
            uploadBookAuthor.setText("");
            uploadBookTitle.setText("");
            uploadBookPublisher.setText("");
            uploadBookISBN.setText("");
            uploadBookStockNum.setText("");
            fileChooserStringNew.setText("");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(rootPane, "SQL error Kérlek próbáld újra késöbb!", "Hiba", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_uploadBookSubmitActionPerformed

    private void uploadBookISBNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_uploadBookISBNKeyReleased
        updateBookISBN();
    }//GEN-LAST:event_uploadBookISBNKeyReleased

    private void scannerStockNumBorrowBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scannerStockNumBorrowBackActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_scannerStockNumBorrowBackActionPerformed

    private void borrowBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrowBackButtonActionPerformed
        if (borrowStockNumBack.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Nem adtál meg könyv azonosítót!", "Hiba", JOptionPane.ERROR_MESSAGE);
            return;
        }
        dbConnect db = new dbConnect();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        Map result = db.getRequest("action=Update;to=borrow;set=state:1,returnDate:'" + sd.format(System.currentTimeMillis()) + "';stockNum=" + borrowStockNumBack.getText() + ";state=0");
        if (result.get("response").equals("True")) {
            JOptionPane.showMessageDialog(rootPane, "Sikeres könyv leadás!", "Info", JOptionPane.INFORMATION_MESSAGE);
            borrowStockNumBack.setText("");
        }
    }//GEN-LAST:event_borrowBackButtonActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        switchPanel(cardManager);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (cardUser.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Nem adtál meg felhasználónevet!", "Hiba", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (cardUserNumber.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Nem adtál meg kártyaszámot!", "Hiba", JOptionPane.ERROR_MESSAGE);
            return;
        }

        dbConnect db = new dbConnect();
        int userId = 0;
        Map resultId = db.getRequest("action=Select;from=users;username='" + cardUser.getText() + "'");
        if (resultId.get("response").equals("True")) {
            userId = Integer.parseInt(String.valueOf(resultId.get("id")));
        }
        Map result = db.getRequest("action=Select;from=usercards;userId=" + userId);
        if (result.get("response").equals("True")) {
            int update = JOptionPane.showConfirmDialog(rootPane, "Ehez a felhasználóhoz már van kártya társítva, kívánja folytatni a folyamatot?", "Info", JOptionPane.YES_NO_OPTION);
            if (update == JOptionPane.NO_OPTION) {
                cardUser.setText("");
                cardUserNumber.setText("");
            } else if (update == JOptionPane.YES_OPTION) {
                result = db.getRequest("action=Update;to=usercards;set=cardId:" + cardUserNumber.getText() + ";userId=" + userId);
                if (result.get("response").equals("True")) {
                    JOptionPane.showMessageDialog(rootPane, "Sikeres kártya társítása!", "Info", JOptionPane.INFORMATION_MESSAGE);
                    cardUser.setText("");
                    cardUserNumber.setText("");
                }
            }
        } else {
            result = db.getRequest("action=Insert;to=usercards(userId,cardId);values=" + String.format("VALUES(%s,%s)", userId, cardUserNumber.getText()));
            if (result.get("response").equals("True")) {
                JOptionPane.showMessageDialog(rootPane, "Sikeres kártya társítása!", "Info", JOptionPane.INFORMATION_MESSAGE);
                cardUser.setText("");
                cardUserNumber.setText("");
            }
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (CheckNewCards()) {
            JFileChooser savePath = new JFileChooser();
            savePath.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int retval = savePath.showSaveDialog(this);
            if (retval == JFileChooser.APPROVE_OPTION) {
                try {
                    dbConnect db = new dbConnect();
                    SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                    String birthDate = sd.format(newCardDate.getDate());
                    Map result = db.getRequest("action=Insert;to=cards(birth,addres,phoneNumber,name);values=" + String.format("VALUES('%s', '%s', '%s', '%s')", birthDate, newCardAddress.getText(), newCardPhone.getText(), newCardName.getText()));
                    if (result.get("response").equals("True")) {
                        Document writePdf = new Document();
                        PdfWriter writer = PdfWriter.getInstance(writePdf, new FileOutputStream(savePath.getSelectedFile() + "/" + newCardName.getText() + "_" + String.valueOf(result.get("id")) + ".pdf"));
                        writePdf.open();
                        PdfPTable table = new PdfPTable(1);
                        PdfPCell cell = new PdfPCell();
                        Paragraph olvasojegy = new Paragraph("Ólvasójegy");
                        olvasojegy.setFont(new Font(Font.FontFamily.UNDEFINED, 20));
                        olvasojegy.setSpacingAfter(5f);
                        olvasojegy.setAlignment(Element.ALIGN_CENTER);
                        cell.addElement(olvasojegy);
                        Paragraph adatok = new Paragraph(String.format("Név: %s\nLakcím: %s\n", newCardName.getText(), newCardAddress.getText()));
                        adatok.setSpacingAfter(10f);
                        cell.addElement(adatok);
                        BarcodeEAN barcode = new BarcodeEAN();
                        barcode.setCodeType(Barcode.EAN8);
                        String code = String.valueOf(result.get("id"));
                        for (int i = code.length(); i < 8; i++) {
                            code = "0" + code;
                        }
                        barcode.setCode(code);
                        barcode.setGuardBars(false);
                        barcode.setBarHeight(8f);
                        barcode.setSize(5f);
                        var img = barcode.createImageWithBarcode(writer.getDirectContent(), BaseColor.BLACK, BaseColor.GRAY);
                        img.setWidthPercentage(60f);
                        img.setAlignment(Element.ALIGN_CENTER);
                        cell.addElement(img);
                        cell.setPadding(50);
                        table.addCell(cell);
                        writePdf.add(table);
                        writePdf.close();
                        JOptionPane.showMessageDialog(rootPane, "Sikeres kártya feltöltés!", "Info", JOptionPane.INFORMATION_MESSAGE);
                        newCardName.setText("");
                        newCardPhone.setText("");
                        newCardAddress.setText("");
                        newCardDate.setDate(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void adminButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminButtonActionPerformed
        switchPanel(adminPanel);
    }//GEN-LAST:event_adminButtonActionPerformed

    private void adminRemovePermissionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminRemovePermissionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_adminRemovePermissionActionPerformed

    private void adminRemoveUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminRemoveUserActionPerformed
        updateCurrentPermission();
    }//GEN-LAST:event_adminRemoveUserActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        dbConnect db = new dbConnect();
        Map result = db.getRequest("action=Select;from=users;username='" + adminAddUser.getSelectedItem() + "'");
        if (result.get("response").equals("True")) {
            result = db.getRequest("action=Insert;to=admins(id,permission);values=" + String.format("VALUES( %s, %s)", result.get("id"), (adminAddPermission.getSelectedIndex() + 1)));
            if (result.get("response").equals("True")) {
                JOptionPane.showMessageDialog(rootPane, "Sikeres admin beállítás!", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
            updateAdmins();
            updateUsers();
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        dbConnect db = new dbConnect();
        Map result = db.getRequest("action=Select;from=users;username='" + adminRemoveUser.getSelectedItem() + "'");
        if (result.get("response").equals("True")) {
            if (adminRemovePermission.getSelectedIndex() != 2) {
                result = db.getRequest("action=Update;to=admins;set=permission:" + (adminRemovePermission.getSelectedIndex() + 1) + ";id=" + result.get("id"));
                if (result.get("response").equals("True")) {
                    JOptionPane.showMessageDialog(rootPane, "Sikeres engedély szint változtatás!", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                result = db.getRequest("action=Delete;from=admins;id=" + result.get("id"));
                if (result.get("response").equals("True")) {
                    JOptionPane.showMessageDialog(rootPane, "Sikeres törlés!", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            switchPanel(adminPanel);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        if (uploadBookStockNum.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Nem adtál meg raktári számot!", "Hiba", JOptionPane.ERROR_MESSAGE);
            return;
        }
        dbConnect db = new dbConnect();
        Map result = db.getRequest("action=Delete;from=stock;stockNum=" + uploadBookStockNum.getText());
        if (result.get("response").equals("True")) {
            JOptionPane.showMessageDialog(rootPane, "Sikeres törlés!", "Info", JOptionPane.INFORMATION_MESSAGE);
            uploadBookStockNum.setText("");
        } else {
            JOptionPane.showMessageDialog(rootPane, "Hiba a törlés közben!", "Hiba", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        String message = "Könyv feltöltése vagy törlése\n\n"
                + "Ha a könyv már korábban megtalálható volt a könyvtárban és csak új példány érkezett belőle az ISBN szám megadásával az adatok automatikusan kitöltődnek.\n"
                + "ebben az esetben a raktári szám emgadása után a küldés gombra kattintva az új példány felkerül a rendszerbe.\n"
                + "Amennyiben a könyv korábban nem volt még a könyvtár rendszerében, ISBN és raktári szám melett az adatainak kitöltése is kötelező!\n"
                + "Ezek megadása után a küldés gombal minden adat felkerül a rendszerbe\n\n"
                + "Törlés esetén a raktári szám megadása után a Törlés gombra kattintva a példány törölve lessz a rendszerből.";
        JOptionPane.showMessageDialog(rootPane, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        if (cardValidNumber.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Nem adtál meg kártyaszámot!", "Hiba", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (cardValidDate.getDate() == null) {
            JOptionPane.showMessageDialog(rootPane, "Nem adtál meg dátumot!", "Hiba", JOptionPane.ERROR_MESSAGE);
            return;
        }
        dbConnect db = new dbConnect();
        Map result = db.getRequest("action=Select;from=cards;id='" + cardValidNumber.getText() + "'");
        if (result.get("response").equals("True")) {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            String validDate = sd.format(cardValidDate.getDate());
            result = db.getRequest("action=Update;to=cards;set=valid:'" + validDate + "';id=" + result.get("id"));
            if (result.get("response").equals("True")) {
                JOptionPane.showMessageDialog(rootPane, "Sikeres hosszabítás!", "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(rootPane, "Nem sikerült meghosszabítani a kártyát!", "Hiba", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Nem létezik ez a kártya!", "Hiba", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        new cards().setVisible(true);
    }//GEN-LAST:event_jButton12ActionPerformed

    public static PdfPCell createBarcode(PdfWriter writer, String code) throws DocumentException, IOException {
        BarcodeEAN barcode = new BarcodeEAN();
        barcode.setCodeType(Barcode.EAN8);
        barcode.setCode(code);
        PdfPCell cell = new PdfPCell(barcode.createImageWithBarcode(writer.getDirectContent(), BaseColor.BLACK, BaseColor.GRAY), true);
        cell.setPadding(10);
        return cell;
    }

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
            java.util.logging.Logger.getLogger(Gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Gui().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CardNumber;
    private javax.swing.JPanel MenuPanel;
    private javax.swing.JButton UpBorrow;
    private javax.swing.JComboBox<String> adminAddPermission;
    private javax.swing.JComboBox<String> adminAddUser;
    private javax.swing.JButton adminButton;
    private javax.swing.JPanel adminPanel;
    private javax.swing.JComboBox<String> adminRemovePermission;
    private javax.swing.JComboBox<String> adminRemoveUser;
    private javax.swing.JPanel borrow;
    private javax.swing.JButton borrowBackButton;
    private javax.swing.JButton borrowButton;
    private com.toedter.calendar.JDateChooser borrowDate;
    private javax.swing.JTextField borrowStockNum;
    private javax.swing.JTextField borrowStockNumBack;
    private javax.swing.JPanel cardManager;
    private javax.swing.JTextField cardUser;
    private javax.swing.JTextField cardUserNumber;
    private com.toedter.calendar.JDateChooser cardValidDate;
    private javax.swing.JTextField cardValidNumber;
    private javax.swing.JLabel currentPermission;
    private javax.swing.JLabel displayName;
    private javax.swing.JButton fileChooserButtonNew;
    private javax.swing.JTextField fileChooserStringNew;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JLayeredPane mainPanel;
    private javax.swing.JButton newBookButton;
    private javax.swing.JTextField newCardAddress;
    private com.toedter.calendar.JDateChooser newCardDate;
    private javax.swing.JTextField newCardName;
    private javax.swing.JTextField newCardPhone;
    private javax.swing.JLabel preview;
    private javax.swing.JToggleButton scannerCardBorrow;
    private javax.swing.JToggleButton scannerISBNupload;
    private javax.swing.JToggleButton scannerStockNumBorrow;
    private javax.swing.JToggleButton scannerStockNumBorrowBack;
    private javax.swing.JToggleButton scannerStockNumUpload;
    private javax.swing.JPanel uploadBook;
    private javax.swing.JTextField uploadBookAuthor;
    private javax.swing.JTextField uploadBookISBN;
    private javax.swing.JTextField uploadBookPublisher;
    private javax.swing.JTextField uploadBookStockNum;
    private javax.swing.JButton uploadBookSubmit;
    private javax.swing.JTextField uploadBookTitle;
    private com.toedter.calendar.JYearChooser uploadBookYear;
    // End of variables declaration//GEN-END:variables

}
