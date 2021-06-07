package profile.myclient.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sourav.ledgerproject.BuildConfig;
import com.sourav.ledgerproject.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import profile.addvoucher.model.Voucher;
import profile.addvoucher.printvoucher.PrintLedgerVoucher;
import profile.upload.QrCodeUploadIntentService;

public class MyVoucherListAdapter extends FirestoreRecyclerAdapter<Voucher,MyVoucherListAdapter.ViewHolder> {

    private static final String TAG = "MyVoucherListAdapter";
    private static final String CHANNEL_ID = "VoucherDownloadNotificationChannel";

    private QRGEncoder qrgEncoder;
    private Bitmap bitmap;


    private Context context;
    private Activity activity;

    public MyVoucherListAdapter(@NonNull FirestoreRecyclerOptions<Voucher> options, Context context, Activity activity) {
        super(options);
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Voucher model) {
        holder.my_voucher_amount.setText(model.getAmount());
        holder.my_voucher_type.setText(model.getType());
        holder.my_voucher_date.setText(model.getTimestamp());

        holder.print_my_voucher.setOnClickListener( v -> {

            new MaterialAlertDialogBuilder(context)
                    .setTitle("Print Voucher")
                    .setMessage("Want to print Ledger?")
                    .setPositiveButton("confirm", (dialog, which) -> {
                        PrintLedgerVoucher printLedgerVoucher = new PrintLedgerVoucher();
                        Dexter.withContext(context)
                                .withPermissions(
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .withListener(new MultiplePermissionsListener() {
                                    @Override
                                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                                        // check if all permissions are granted
                                        if (report.areAllPermissionsGranted()) {

                                            Log.d(TAG, "onPermissionsChecked: ");
                                            if(isExternalStorageAvailable() || isExternalStorageReadable()) {
                                                if(model.isAdded()) {
                                                    Log.d(TAG, "onPermissionsChecked: path: " + getStorageDir(model.getName() + "_" + model.getVoucher_number() + "_" + model.getTimestamp().replace("/", "_")));
                                                    writeData(getStorageDir(model.getName() + "_" + model.getVoucher_number() + "_" + model.getTimestamp().replace("/", "_") + ".pdf"), model);
                                                    makeNotificationForBillDownload();
                                                }
                                                else{
                                                    Toast.makeText(context, "Voucher not Approved! Please get Approval.",Toast.LENGTH_LONG).show();
                                                }
                                            }

                                        }

                                        // check for permanent denial of any permission
                                        if (report.isAnyPermissionPermanentlyDenied()) {
                                            // permission is denied permenantly, navigate user to app settings
                                        }
                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                                    }


                                })
                                .onSameThread()
                                .check();

                    })
                    .setNegativeButton("cancel", null)
                    .create()
                    .show();

        });
    }
    private void makeNotificationForBillDownload() {

        final int PROGRESS_MAX = 100;
        final int PROGRESS_CURRENT = 0;

        Log.d(TAG, "makeNotificationForBillDownload: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID)
                    .setSmallIcon(R.drawable.voucher_icon)
                    .setContentTitle("Voucher Download")
                    .setContentText("Download in Progress");

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        CHANNEL_ID, "FileDownloadNotificationChannel",
                        NotificationManager.IMPORTANCE_HIGH);

                notificationManager.createNotificationChannel(notificationChannel);
            }

            new Thread( () -> {
                for(int progress = PROGRESS_CURRENT; progress <= PROGRESS_MAX; progress+=20){
                    builder.setProgress(PROGRESS_MAX, progress, false);
                    notificationManager.notify(0, builder.build());

                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException e ){
                        Log.e(TAG, "makeNotificationForBillDownload: ", e);
                    }

                }

                builder.setContentText("Download Complete")
                        .setProgress(0, 0, false);
                notificationManager.notify(0, builder.build());

            }).start();

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_voucher_list_item, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView my_voucher_amount, my_voucher_type, my_voucher_date;
        Button print_my_voucher;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            my_voucher_amount = itemView.findViewById(R.id.my_voucher_amount);
            my_voucher_type = itemView.findViewById(R.id.my_voucher_type);
            my_voucher_date = itemView.findViewById(R.id.my_voucher_date);

            print_my_voucher = itemView.findViewById(R.id.delete_my_voucher);
        }
    }

    //write data to file
    private void writeData(String filePath, Voucher model) {
        try {

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document,new FileOutputStream(filePath));
            document.open();

            WindowManager manager = activity.getWindowManager();

            Point point = new Point();
            Display display = manager.getDefaultDisplay();
            display.getSize(point);

            int width = point.x;
            int height = point.y;

            int dimen = Math.min(width, height);
            dimen = dimen * 3 / 4;

            String qr_id = UUID.randomUUID().toString();
            qrgEncoder = new QRGEncoder(qr_id, null, QRGContents.Type.TEXT, dimen);

            bitmap = qrgEncoder.getBitmap();

            ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream3);
            Image image = Image.getInstance(stream3.toByteArray());

            image.scaleAbsolute(80,80);

            Intent intent = new Intent(context, QrCodeUploadIntentService.class);
            intent.putExtra("qr_id",qr_id);
            intent.putExtra("simpledate", new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
            QrCodeUploadIntentService.enqueueWork(context, intent);

            document.add(image);

            Paragraph p;
            for(int i = 0; i < 5; i++) {
                p = new Paragraph(" ");
                document.add(p);
            }

            String myname = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();

            BaseFont baseFont = BaseFont.createFont();
            Paragraph heading, voucher_number_text, voucher_number, voucher_amount, voucher_amount_text, voucher_date_text, voucher_date, to_whom_text, to_whom, approved_by_text, approved_by, paid_by, paid_by_text;

            heading = new Paragraph("Payment Voucher", new Font(baseFont,30, Font.BOLD));
            voucher_number_text = new Paragraph("Voucher No.", new Font(baseFont, 15));
            voucher_number = new Paragraph(model.getVoucher_number(), new Font(baseFont, 15));
            voucher_amount_text = new Paragraph("Amount", new Font(baseFont, 15));
            voucher_amount = new Paragraph(model.getAmount(), new Font(baseFont, 15));
            voucher_date_text = new Paragraph("Date", new Font(baseFont, 15));
            voucher_date = new Paragraph(model.getTimestamp(), new Font(baseFont, 15));
            to_whom_text = new Paragraph("To", new Font(baseFont, 15));
            to_whom = new Paragraph(myname, new Font(baseFont, 15));
            approved_by_text = new Paragraph("Approved By", new Font(baseFont, 15));
            //approved_by = new Paragraph(model.getTimestamp(), new Font(baseFont, 15));
            paid_by_text = new Paragraph("Paid By", new Font(baseFont, 15));


            PdfPTable table = new PdfPTable(3);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.addCell("");
            table.addCell("      "+heading);
            table.addCell("");

            table.addCell(voucher_number_text+"  "+voucher_number);
            table.addCell("");
            table.addCell("");

            table.setWidthPercentage(100);

            PdfPTable mainTable = new PdfPTable(2);

            mainTable.addCell(voucher_amount_text + ":    " + voucher_amount);
            mainTable.addCell(voucher_date_text+ ":    " + voucher_date);
            mainTable.addCell(to_whom_text+":    " + to_whom);
            mainTable.addCell(approved_by_text + ":    ");
            mainTable.addCell(paid_by_text + ":    ");

            mainTable.setWidthPercentage(100);

            Paragraph line_space = new Paragraph("");
            line_space.setSpacingBefore(50);

            document.add(table);
            document.add(line_space);
            document.add(mainTable);
            document.close();

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    //read data from the file


    //checks if external storage is available for read and write
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    //checks if external storage is available for read
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    private String getStorageDir(String fileName) {
        //create folder
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
        if (!file.mkdirs()) {
            file.mkdirs();
        }
        Log.d(TAG, "getStorageDir: file path" + file.getAbsolutePath());
        return file.getAbsolutePath() + File.separator + fileName;
    }

}
