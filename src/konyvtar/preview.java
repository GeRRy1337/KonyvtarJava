package konyvtar;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.Barcode;
import com.itextpdf.text.pdf.BarcodeEAN;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 *
 * @author Gergő
 */
public class preview {

    private static String cardName, cardAddress;
    private static BufferedImage image;

    private static BufferedImage loadPreview() {
        try {
            //pdf file
            Document writePdf = new Document();
            FileOutputStream fout = new FileOutputStream("temp.pdf");
            PdfWriter writer = PdfWriter.getInstance(writePdf, fout);
            writePdf.open();

            //tábla formázás
            PdfPTable table = new PdfPTable(1);
            PdfPCell cell = new PdfPCell();

            //olvasójegy felirat
            Paragraph olvasojegy = new Paragraph("Olvasójegy");
            olvasojegy.setFont(new Font(Font.FontFamily.UNDEFINED, 20));
            olvasojegy.setSpacingAfter(5f);
            olvasojegy.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(olvasojegy);

            //adatok
            Paragraph adatok = new Paragraph(String.format("Név: %s\nLakcím: %s\n", cardName, cardAddress));
            adatok.setSpacingAfter(10f);
            cell.addElement(adatok);

            //vonalkód
            BarcodeEAN barcode = new BarcodeEAN();
            barcode.setCodeType(Barcode.EAN8);
            String code = String.valueOf("00000000");
            barcode.setCode(code);
            barcode.setGuardBars(false);
            barcode.setBarHeight(8f);
            barcode.setSize(5f);
            var img = barcode.createImageWithBarcode(writer.getDirectContent(), BaseColor.BLACK, BaseColor.GRAY);
            img.setWidthPercentage(60f);
            img.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(img);
            cell.setPadding(50);

            //fájlba írás
            table.addCell(cell);
            writePdf.add(table);
            writePdf.close();
            writer.close();
            fout.close();

            //kép létrehozása
            PDDocument document = PDDocument.load(new File("temp.pdf"));
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage bi = pdfRenderer.renderImage(0, 1.5f);
            document.close();

            return bi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param args the command line arguments
     */
    //https://stackoverflow.com/questions/17865465/how-do-i-draw-an-image-to-a-jpanel-or-jframe
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                cardName = args[0];
                cardAddress = args[1];
                image = loadPreview();
                JFrame frame = buildFrame();
                JPanel pane = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.drawImage(image, -100, 0, null);
                    }
                };

                frame.add(pane);
            }
        });
    }

    private static JFrame buildFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(710, 500);
        frame.setVisible(true);
        return frame;
    }

}
