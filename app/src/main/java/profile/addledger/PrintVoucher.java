package profile.addledger;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.util.Log;

//import com.sourav.ledgerproject.profile.addledger.model.Voucher;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import profile.addvoucher.model.Voucher;

public class PrintVoucher {

    /*private static final String TAG = "PrintVoucher";
    private PdfDocument pdfDocument;
    private PdfDocument.PageInfo pageInfo;
    private PdfDocument.Page page;
    private Paint paint;

    private String opening_balance;
    private String closing_balance;
    private String client_name;
    private List<Voucher> voucher_list;
    boolean payment;

    int x = 40;
    int y = 125;

    public PrintVoucher(){
        pdfDocument = new PdfDocument();


    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public void makePrintPdf(){

        int pagelisting = 16;

        pageInfo = new PdfDocument.PageInfo.Builder(595,842,1).create();
        Log.d(TAG,"voucher list size is: "+voucher_list.size());
       // Log.d(TAG,"page number is: "+pagenumber);

        for(int i = 0; i < 10; i++){

            if(getVoucher_list().size() < pagelisting*i){
                createPage();
            }else if(getVoucher_list().size() > pagelisting*i && getVoucher_list().size() < pagelisting*(i+1)){
                createPage();
            }
        }

        page.getCanvas().drawLine(200,y+10,380,y+10,paint);
        page.getCanvas().drawText("                                                                              "+(Integer.parseInt(getOpening_balance()) + getTotalDebitOrCredit().get("totaldebit"))+"               "+getTotalDebitOrCredit().get("totalcredit"),x,y+30,paint);
        page.getCanvas().drawText("   By  Closing Balance"+"                                                             "+(Integer.parseInt(getOpening_balance()) - getTotalDebitOrCredit().get("totalcredit")),x,y+50,paint);
        page.getCanvas().drawLine(200,y+60,380,y+60,paint);
        page.getCanvas().drawText("                                                                             "+(Integer.parseInt(getOpening_balance()) + getTotalDebitOrCredit().get("totaldebit"))+"            "+(Integer.parseInt(getOpening_balance()) + getTotalDebitOrCredit().get("totaldebit")),x,y+80,paint);
        page.getCanvas().drawLine(200,y+90,380,y+90,paint);
        pdfDocument.finishPage(page);


        String path = Environment.getExternalStorageDirectory().getPath() + "/voucherlist.pdf";
        File file = new File(path);

        try{
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String,Integer> getTotalDebitOrCredit(){
        int totaldebit=0;
        int totalcredit=0;
        for(Voucher debit:voucher_list){
            if(debit.getType().equals("Payment")){
                totaldebit+=Integer.parseInt(debit.getAmount());
                Log.d(TAG,"total debit: "+totaldebit);
            }
            else if(debit.getType().equals("Receipt")){
                totalcredit+=Integer.parseInt(debit.getAmount());
                Log.d(TAG,"total credit: "+totalcredit);
            }

        }

        Map<String,Integer> totalmap = new HashMap<>();
        totalmap.put("totaldebit",totaldebit);
        totalmap.put("totalcredit",totalcredit);
        return totalmap;
    }

    public void setOpening_balance(String opening_balance){
        this.opening_balance = opening_balance;
    }

    public void setClosing_balance(String closing_balance){
        this.closing_balance = closing_balance;
    }

    public void setVoucher_list(List<Voucher> voucher_list){
        this.voucher_list = voucher_list;
    }

    public String getOpening_balance() {
        return opening_balance;
    }

    public String getClosing_balance() {
        return closing_balance;
    }

    public List<Voucher> getVoucher_list() {
        return voucher_list;
    }

    private void createPage(){
        page = pdfDocument.startPage(pageInfo);
        paint = new Paint();

        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2);



        page.getCanvas().drawLine(30,60,380,60,paint);
        page.getCanvas().drawText("   "+"Date"+"                      "+"Voucher Type"+"                    "+"Debit"+"               "+"Credit",x,75,paint);
        page.getCanvas().drawLine(30,80,380,80,paint);


        int nameX = 200;
        int nameY = 30;

        page.getCanvas().drawText(getClient_name(),nameX,nameY,paint);

        String voucher_p = "";
        page.getCanvas().drawText("To  Opening Balance"+"                                           "+getOpening_balance(),x,105,paint);
        for(Voucher vo: getVoucher_list()){

            if(vo.getType().equals("Payment")) {
                voucher_p = vo.getTimestamp() + "                        " + vo.getType() + "                     " + vo.getAmount();
                payment = true;
            }
            else if(vo.getType().equals("Receipt"))
                voucher_p = vo.getTimestamp()+"                        "+vo.getType()+"                                              "+vo.getAmount();

            page.getCanvas().drawText(voucher_p,x,y,paint);
            y+=20;
        }
    }*/
}
