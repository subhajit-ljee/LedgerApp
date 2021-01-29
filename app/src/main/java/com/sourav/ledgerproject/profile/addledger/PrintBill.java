package com.sourav.ledgerproject.profile.addledger;

import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PrintBill {

    private static final String TAG = "PrintBill";

    private Document pdfDocument;
    private String filepath;

    FileOutputStream outputStream;
    FileInputStream fileInputStream;
    public PrintBill(String filepath, FileInputStream inputStream){

        this.filepath = filepath;
        fileInputStream = inputStream;
    }

    public void makeBillPdf(){
        try{
            pdfDocument = new Document(PageSize.A4);
            outputStream = new FileOutputStream(new File("/storage/emulated/0/Download/mypdfdownload.pdf"));
            PdfWriter writer = PdfWriter.getInstance(pdfDocument, outputStream);

            PdfContentByte cb = writer.getDirectContent();
            pdfDocument.close();
            // Load existing PDF
            if(fileInputStream!=null) {
                PdfReader reader = new PdfReader(fileInputStream);
                PdfImportedPage page = writer.getImportedPage(reader, 1);

                //pdfDocument.open();
                pdfDocument.newPage();
                cb.addTemplate(page, 0, 0);

                // Add your new data / text here
                // for example...
                pdfDocument.add(new Paragraph("my timestamp"));
                Log.d(TAG,"FileInputStream: not null");
            }
            else{
                Log.d(TAG,"FileInputStream: null");
            }
            Log.d(TAG,"File loaded");

            fileInputStream.close();
            outputStream.close();
        }catch (FileNotFoundException | DocumentException fe){
            fe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
