package profile.addvoucher.printvoucher;

import android.os.Environment;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PrintLedgerVoucher {

    private String filepath;

    public void printVoucher() throws DocumentException, FileNotFoundException {
        Document document = new Document();
        PdfWriter.getInstance(document,new FileOutputStream(filepath+"/Hello.pdf"));
        document.open();
        Paragraph p = new Paragraph("Hello PDF");
        document.add(p);
        document.close();
    }

    public void setFilepath(String filepath){
        this.filepath = filepath;
    }

    public String getFilepath(){
        return filepath;
    }
}
