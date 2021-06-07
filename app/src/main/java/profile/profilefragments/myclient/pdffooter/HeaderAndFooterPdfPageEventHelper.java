package profile.profilefragments.myclient.pdffooter;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HeaderAndFooterPdfPageEventHelper extends PdfPageEventHelper {

    public void onStartPage(PdfWriter pdfWriter, Document document) {
        System.out.println("onStartPage() method > Writing header in file");
        Rectangle rect = pdfWriter.getBoxSize("rectangle");

        // TOP LEFT
        ColumnText.showTextAligned(pdfWriter.getDirectContent(),
                Element.ALIGN_CENTER, new Phrase(""), rect.getLeft(),
                rect.getTop(), 0);

        // TOP MEDIUM
        ColumnText.showTextAligned(pdfWriter.getDirectContent(),
                Element.ALIGN_CENTER, new Phrase(""),
                rect.getRight() / 2, rect.getTop(), 0);

        // TOP RIGHT
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        ColumnText.showTextAligned(pdfWriter.getDirectContent(),
                Element.ALIGN_CENTER, new Phrase("Generated on " + date), rect.getRight()-40,
                rect.getTop(), 0);
    }


    public void onEndPage(PdfWriter pdfWriter, Document document){
        System.out.println("onEndPage() method > Writing footer in file");
        Rectangle rect = pdfWriter.getBoxSize("rectangle");
        // BOTTOM LEFT
        ColumnText.showTextAligned(pdfWriter.getDirectContent(),
                Element.ALIGN_CENTER, new Phrase("No Signature Needed"),
                rect.getLeft()+45, rect.getBottom(), 0);

        // BOTTOM MEDIUM
        ColumnText.showTextAligned(pdfWriter.getDirectContent(),
                Element.ALIGN_CENTER, new Phrase(""),
                rect.getRight() / 2, rect.getBottom(), 0);

        // BOTTOM RIGHT
        ColumnText.showTextAligned(pdfWriter.getDirectContent(),
                Element.ALIGN_CENTER, new Phrase("This is App Generated Ledger Voucher Bill"),
                rect.getRight()-90, rect.getBottom(), 0);
    }

}
