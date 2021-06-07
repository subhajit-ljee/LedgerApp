package profile.profilefragments.myclient;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableFooter;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sourav.ledgerproject.BuildConfig;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import profile.addledger.BillFileUtils;
import profile.addledger.dependency.GetLedgerComponent;
import profile.addledger.model.GetLedgerRepository;
import profile.addledger.model.Ledger;
import profile.addvoucher.printvoucher.PrintLedgerVoucher;
import profile.profilefragments.myclient.pdffooter.HeaderAndFooterPdfPageEventHelper;
import profile.upload.QrCodeUploadIntentService;

import static java.lang.Math.abs;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyLedgerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyLedgerFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "MyLedgerFragment";
    private static final String CHANNEL_ID = "FileDownloadNotificationChannel";
    //private CalcBroadCastReceiver calcBroadCastReceiver;

    private String lid;
    private String cid;
    private String last_voucher_date;

    private GetLedgerComponent getLedgerComponent;

    @Inject
    GetLedgerRepository getLedgerRepository;

    SwipeRefreshLayout ledgerdetailsswiperefresh;

    ConstraintLayout main_layout;

    TextView ledger_date_text, ledger_number_text, my_ledger_holder_name,
            my_client_address, my_client_pincode, my_client_state, my_client_country,
    debit_text, credit_text, opening_balance, closing_balance, ledger_type_text, your_ledger_text;

    Button print_ledger_voucher, see_voucher_list_button;

    ContentLoadingProgressBar content_prog_bar;
    private NotificationManager notificationManager;

    private QRGEncoder qrgEncoder;
    private Bitmap bitmap;

    private File filefornot;

    public MyLedgerFragment() {
        // Required empty public constructor
    }


    public static MyLedgerFragment newInstance() {
        return new MyLedgerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lid = getArguments().getString("ledger_id");
            cid = getArguments().getString("client_id");
            Log.d(TAG, "onCreate: lid: " + lid + ", cid: " + cid);
            //calcBroadCastReceiver = new CalcBroadCastReceiver();
            //requireActivity().registerReceiver(calcBroadCastReceiver,new IntentFilter("GET_CALCULATED_VALUE"));
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_ledger, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        main_layout.setVisibility(View.INVISIBLE);

        //content_prog_bar.setVisibility(View.VISIBLE);
        content_prog_bar.show();

        String authid;
        authid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        Ledger ledger = new Ledger();
        ledger.setId(lid);
        ledger.setUser_id(cid);
        ledger.setClient_id(authid);

        assert getArguments() != null;
        if(getArguments().getString("print") != null){
            ledger.setId(lid);
            ledger.setUser_id(authid);
            ledger.setClient_id(cid);

            your_ledger_text.setText("Your Client Ledger Details");
        }


        getLedgerComponent = ((LedgerApplication)requireActivity().getApplication()).getAppComponent()
                .getGetLedgerComponent().create(ledger);

        getLedgerComponent.inject(this);

        requireActivity().runOnUiThread( () -> {
            Query query = getLedgerRepository.getLedger();
            query.get()
                    .addOnCompleteListener( task -> {
                        if(task.isSuccessful()) {

                            content_prog_bar.hide();
                            main_layout.setVisibility(View.VISIBLE);

                            for (QueryDocumentSnapshot s : Objects.requireNonNull(task.getResult())) {
                                ledger_date_text.setText(s.getString("timestamp"));
                                ledger_type_text.setText(s.getString("account_type"));
                                ledger_number_text.setText(s.getString("ledger_number"));
                                my_ledger_holder_name.setText(s.getString("account_name"));
                                my_client_address.setText(s.getString("account_address"));
                                my_client_pincode.setText(s.getString("account_pincode"));
                                my_client_state.setText(s.getString("account_state"));
                                my_client_country.setText(s.getString("account_country"));
                                opening_balance.setText(s.getString("opening_balance")+".00");

                                Log.d(TAG, "onViewCreated: s.getString(\"timestamp\"): " + s.getString("timestamp") + ", s.getString(\"account_type\"): " + s.getString("account_type") +
                                        ", s.getString(\"ledger_number\"): " + s.getString("ledger_number") + ", s.getString(\"account_name\"): " + s.getString("account_name") +
                                        ", s.getString(\"account_address\"):" + s.getString("account_address") + ", s.getString(\"account_pincode\")" + s.getString("account_pincode") +
                                        ", s.getString(\"account_state\"): " + s.getString("account_state") + ", s.getString(\"account_country\"): " + s.getString("account_country") +
                                        ", s.getString(\"opening_balance\")+\".00\": " +s.getString("opening_balance")+".00");

                            }
                        }

                    })
                    .addOnFailureListener( e -> Log.e(TAG, "onViewCreated: error: ", e));
        });

        Query query = getLedgerRepository.getVoucher();

        requireActivity().runOnUiThread( () ->

            query.get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){

                            int de = 0;
                            int ce = 0;
                            List<DocumentSnapshot> documents = null;
                            for(QueryDocumentSnapshot snapshot : Objects.requireNonNull(task.getResult())){
                                Log.d(TAG, "onViewCreated: amount: " + snapshot.getString("amount"));
                                if(Objects.equals(snapshot.getBoolean("added"),true)) {
                                    if (Objects.requireNonNull(snapshot.getString("type")).toUpperCase().equals("Payment".toUpperCase()))
                                        de += Integer.parseInt(snapshot.getString("amount"));
                                    else if (Objects.requireNonNull(snapshot.getString("type")).toUpperCase().equals("Receipt".toUpperCase()))
                                        ce += Integer.parseInt(snapshot.getString("amount"));
                                }

                            }

                            for(DocumentSnapshot s : task.getResult().getDocuments()){
                                Log.d(TAG, "onViewCreated: id: " + s.getString("id"));
                            }

                            documents = task.getResult().getDocuments();

                            Log.d(TAG, "onViewCreated: de: " + de + " ,ce: " + ce);
                            debit_text.setText(de+".00");
                            credit_text.setText(ce+".00");


                            List<DocumentSnapshot> finalDocuments1 = documents;
                            int finalDe = de;
                            int finalCe = ce;
                            getLedgerRepository.getLedger()
                                    .get()
                                    .addOnCompleteListener( task1 -> {
                                        if(task1.isSuccessful()){

                                            Integer op_bal = 0;
                                            for(QueryDocumentSnapshot snapshot : Objects.requireNonNull(task1.getResult())){
                                                op_bal = Integer.parseInt(snapshot.getString("opening_balance"));
                                            }

                                            Log.d(TAG, "onViewCreated: opening_balance: " + op_bal);
                                            String closing_bal;
                                            Log.d(TAG, "onViewCreated: ledger_type_text.getText().toString(): " + ledger_type_text.getText().toString());
                                            if(ledger_type_text.getText().toString().equals("Debitor")) {
                                                int closing_bal_cal = (op_bal + finalDe) - finalCe;
                                                closing_bal = abs(closing_bal_cal) + ".00";
                                                if(closing_bal_cal < 0) {
                                                    credit_text.setText(abs(closing_bal_cal) + ".00");
                                                }
                                            }
                                            else {
                                                int closing_bal_cal = ((op_bal + finalCe) - finalDe);
                                                closing_bal = abs(closing_bal_cal) + ".00";
                                                if(closing_bal_cal < 0){
                                                    debit_text.setText(abs(closing_bal_cal)+ ".00");
                                                }
                                            }
                                            closing_balance.setText(closing_bal);

                                            List<DocumentSnapshot> finalDocuments = finalDocuments1;
                                            getLedgerRepository.getBill_amount()
                                                    .get()
                                                    .addOnCompleteListener( task2 -> {
                                                        if(task2.isSuccessful()){
                                                            List<DocumentSnapshot> bill_amount = Objects.requireNonNull(task2.getResult()).getDocuments();

                                                            print_ledger_voucher.setOnClickListener(v -> new MaterialAlertDialogBuilder(requireContext())
                                                                    .setTitle("Ledger Voucher Print")
                                                                    .setMessage("Want to print Ledger?")
                                                                    .setPositiveButton("confirm", (dialog, which) -> {
                                                                        PrintLedgerVoucher printLedgerVoucher = new PrintLedgerVoucher();
                                                                        Dexter.withContext(requireContext())
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
                                                                                                String date = new SimpleDateFormat("dd_MM_yyyy", Locale.getDefault()).format(new Date());
                                                                                                Log.d(TAG, "onPermissionsChecked: path: " + getStorageDir(my_ledger_holder_name.getText().toString().trim() + ledger_number_text.getText().toString().trim() + "_bill_"+date+".pdf"));
                                                                                                if(finalDocuments.size() > 0) {
                                                                                                    writeData(getStorageDir(my_ledger_holder_name.getText().toString().trim() + "_" + ledger_number_text.getText().toString().trim() + "_bill_" + date + ".pdf"), finalDocuments, bill_amount);
                                                                                                    makeNotificationForBillDownload(my_ledger_holder_name.getText().toString().trim() + ledger_number_text.getText().toString().trim() + "_bill_"+date+".pdf");
                                                                                                }else{
                                                                                                    Toast.makeText(getContext(), "No Voucher found for this ledger!", Toast.LENGTH_SHORT).show();
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
                                                                    .show());
                                                        }
                                                    });

                                        }
                                    });


                        }
                    })
        );

        see_voucher_list_button.setOnClickListener( v -> {
            Bundle bundle = new Bundle();
            bundle.putString("cid",cid);
            bundle.putString("lid",lid);
            NavController findViewController = Navigation.findNavController(view);

            if(getArguments().getString("print") != null) {
                bundle.putString("print","print");
                findViewController.navigate(R.id.action_myLedgerFragment2_to_myVoucherListFragment2, bundle);
            }
            else
                findViewController.navigate(R.id.action_myLedgerFragment_to_myVoucherListFragment, bundle);
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        Log.d(TAG, "onViewCreated: print: " + getArguments().getString("print"));

        view.setOnKeyListener((View v1, int keyCode, KeyEvent event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Bundle bundle = new Bundle();
                    bundle.putString("cid",cid);
                    if(getArguments().getString("print") == null)
                        Navigation.findNavController(requireActivity(), R.id.pro_main_fragment).navigate(R.id.action_myLedgerFragment_to_myLedgerListFragment, bundle);
                    else if(getArguments().getString("print").equals("print"))
                        Navigation.findNavController(requireActivity(), R.id.create_client_fragment).navigate(R.id.action_myLedgerFragment2_to_showLedgerListForPrintFragment, bundle);
                    Log.d(TAG, "onViewCreated: backpressed");
                    return true;
                }
            }
            return false;
        });

    }

    private void makeNotificationForBillDownload(String filename) {

        final int PROGRESS_MAX = 100;
        final int PROGRESS_CURRENT = 0;

        Log.d(TAG, "makeNotificationForBillDownload: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Uri uri = FileProvider.getUriForFile(getContext().getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", getFile());
            Log.d(TAG, "makeNotificationForBillDownload: path: " + uri.getPath());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setDataAndType(uri, "application/pdf");

            Log.d(TAG, "makeNotificationForBillDownload: file: " + getFile().getAbsolutePath());

            try {
                
                if(getFile() == null){
                    Log.d(TAG, "makeNotificationForBillDownload: file null");
                }
                
                Scanner myReader = new Scanner(getFile());
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    Log.d(TAG, data + "\n");
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                Log.e(TAG, "makeNotificationForBillDownload: ",e);
                e.printStackTrace();
            }

            Intent chooser = Intent.createChooser(intent, "Open with");
            PendingIntent pendingIntent = PendingIntent.getActivity(requireContext(), 0, chooser, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),CHANNEL_ID)
                    .setSmallIcon(R.drawable.voucher_icon)
                    .setContentTitle("Bill Download")
                    .setContentText("Download in Progress")
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

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

    //write data to file
    private void writeData(String filePath, List<DocumentSnapshot> documentSnapshots, List<DocumentSnapshot> bill_amount) {
        try {

            Document document = new Document(PageSize.A4);

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            setFile(new File(filePath));
            Rectangle rectangle = new Rectangle(30, 30, 550, 800);
            writer.setBoxSize("rectangle", rectangle);
            HeaderAndFooterPdfPageEventHelper headerAndFooter =
                    new HeaderAndFooterPdfPageEventHelper();

            writer.setPageEvent(headerAndFooter);
            document.open();

            WindowManager manager = requireActivity().getWindowManager();

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

            Intent intent = new Intent(requireContext(), QrCodeUploadIntentService.class);
            intent.putExtra("qr_id",qr_id);
            intent.putExtra("simpledate", new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
            QrCodeUploadIntentService.enqueueWork(requireContext(), intent);

            document.add(image);


            Paragraph p;
            for(int i = 0; i < 5; i++) {
                p = new Paragraph(" ");
                document.add(p);
            }

            String myname = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();

            BaseFont baseFont = BaseFont.createFont();
            Paragraph client_name, client_address, name, from_date, to_date;
            client_name = new Paragraph(my_ledger_holder_name.getText().toString(), new Font(baseFont,15, Font.BOLD));

            client_address = new Paragraph(my_client_address.getText().toString(), new Font(baseFont, 15));

            name = new Paragraph(myname, new Font(baseFont, 12, Font.BOLD));

            PdfPTable table = new PdfPTable(3);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.addCell("");
            table.addCell("      " + client_name);
            table.addCell("");

            table.addCell("");
            table.addCell(client_address);
            table.addCell("");

            table.addCell("");
            table.addCell("      " + name);
            table.addCell("");
            table.setWidthPercentage(100);


            Paragraph date, to_by, particulars, v_type, v_number, debit_col, credit_col = null;
            PdfPTable mainTable = new PdfPTable(7);

            mainTable.addCell(new Paragraph("Date"));
            mainTable.addCell(new Paragraph("    "));
            mainTable.addCell(new Paragraph("Particulars"));
            mainTable.addCell(new Paragraph("Vch Type"));
            mainTable.addCell(new Paragraph("Vch No."));
            mainTable.addCell(new Paragraph("Debit"));
            mainTable.addCell(new Paragraph("Credit"));

            mainTable.addCell(new Paragraph(ledger_date_text.getText().toString(), new Font(baseFont, 12)));
            mainTable.addCell(new Paragraph("      TO"));
            mainTable.addCell(new Paragraph("Opening Balance"));
            mainTable.addCell("");
            mainTable.addCell("");
            Log.d(TAG, "writeData: type: " + ledger_type_text.getText().toString().trim());
            if (ledger_type_text.getText().toString().trim().equals("Debitor")) {
                mainTable.addCell(opening_balance.getText().toString());
                mainTable.addCell("");
            } else if (ledger_type_text.getText().toString().trim().equals("Creditor")) {
                Log.d(TAG, "writeData: this area");
                mainTable.addCell("");
                mainTable.addCell(opening_balance.getText().toString());
            }

            Integer tot_bill_bal = 0;

            if (bill_amount.size() > 0) {
                for (DocumentSnapshot snapshot : bill_amount) {
                    mainTable.addCell(new Paragraph(ledger_date_text.getText().toString(), new Font(baseFont, 12)));
                    mainTable.addCell(new Paragraph("      TO"));
                    mainTable.addCell(new Paragraph("Bill Balance"));
                    mainTable.addCell("");
                    mainTable.addCell("");
                    mainTable.addCell(snapshot.getString("bill_balance") + ".00");
                    mainTable.addCell("");
                    tot_bill_bal += Integer.parseInt(snapshot.getString("bill_balance"));
                }
            }
            mainTable.setWidthPercentage(100);

            int de = 0;
            int ce = 0;
            String amount = null;
            for (DocumentSnapshot snapshot : documentSnapshots) {

                date = new Paragraph(snapshot.getString("timestamp"), new Font(baseFont, 12));
                Boolean added = snapshot.getBoolean("added");
                String type = snapshot.getString("type");
                amount = snapshot.getString("amount");
                last_voucher_date = snapshot.getString("timestamp");

                if (added) {
                    if (!type.isEmpty() || type != null) {
                        if (type.toUpperCase().equals("Payment".toUpperCase())) {
                            to_by = new Paragraph("      TO");
                            debit_col = new Paragraph(amount + ".00");
                            credit_col = new Paragraph("");
                            de += Integer.parseInt(amount);
                        } else if (type.toUpperCase().equals("Receipt".toUpperCase())) {
                            to_by = new Paragraph("      By");
                            debit_col = new Paragraph("");
                            credit_col = new Paragraph(amount + ".00");
                            ce += Integer.parseInt(amount);
                        } else {
                            to_by = new Paragraph("        ");
                            debit_col = new Paragraph("");
                            credit_col = new Paragraph("");
                            de = 0;
                            ce = 0;
                        }
                    } else {
                        to_by = new Paragraph("        ");
                        debit_col = new Paragraph("");
                        credit_col = new Paragraph("");
                    }

                    particulars = new Paragraph("Cash");
                    v_type = new Paragraph(type);
                    v_number = new Paragraph(" ");
                    mainTable.addCell(date);
                    mainTable.addCell(to_by);
                    mainTable.addCell(particulars);
                    mainTable.addCell(v_type);
                    mainTable.addCell(v_number);
                    mainTable.addCell(debit_col);
                    mainTable.addCell(credit_col);
                }

            }

            if (ledger_type_text.getText().toString().equals("Debitor") && de > Integer.parseInt(opening_balance.getText().toString().substring(0, opening_balance.getText().toString().indexOf(".")))) {
                particulars = new Paragraph("Cash");
                v_type = new Paragraph("Receipt");
                v_number = new Paragraph(" ");
                mainTable.addCell("");
                mainTable.addCell("       By");
                mainTable.addCell(particulars);
                mainTable.addCell(v_type);
                mainTable.addCell(v_number);
                mainTable.addCell(String.valueOf(abs(Integer.parseInt(opening_balance.getText().toString().substring(0, opening_balance.getText().toString().indexOf("."))) - de)));
                mainTable.addCell(" ");
            } else if (ledger_type_text.getText().toString().equals("Creditor") && ce > Integer.parseInt(opening_balance.getText().toString().substring(0, opening_balance.getText().toString().indexOf(".")))) {
                particulars = new Paragraph("Cash");
                v_type = new Paragraph("Payment");
                v_number = new Paragraph(" ");
                mainTable.addCell("");
                mainTable.addCell("       TO");
                mainTable.addCell(particulars);
                mainTable.addCell(v_type);
                mainTable.addCell(v_number);
                mainTable.addCell(" ");
                mainTable.addCell(String.valueOf(abs(Integer.parseInt(opening_balance.getText().toString().substring(0, opening_balance.getText().toString().indexOf("."))) - ce)));
            }

            if (!opening_balance.getText().toString().isEmpty() || opening_balance.getText().toString() != null) {
                String tot_bal = String.valueOf(Integer.parseInt(opening_balance.getText().toString().substring(0, opening_balance.getText().toString().indexOf("."))) + de);

                mainTable.addCell(new Paragraph(""));
                mainTable.addCell(new Paragraph(""));
                mainTable.addCell(new Paragraph(""));
                mainTable.addCell("");
                mainTable.addCell("");
                //Log.d(TAG, "writeData: Integer.parseInt(amount): " + Integer.parseInt(amount) + ", tot_bal: " + tot_bal + ", tot_bill_bal: " + tot_bill_bal);

                if (ledger_type_text.getText().toString().equals("Debitor")) {
                    mainTable.addCell(Integer.parseInt(tot_bal) + tot_bill_bal + ".00");
                    mainTable.addCell(" ");
                } else {
                    Log.d(TAG, "writeData: credit");
                    tot_bal = Integer.parseInt(opening_balance.getText().toString().substring(0, opening_balance.getText().toString().indexOf("."))) + ce + "";
                    mainTable.addCell(" ");
                    mainTable.addCell((Integer.parseInt(tot_bal) + tot_bill_bal) + ".00");
                }
                //mainTable.addCell(String.valueOf(ce));

                String cl_bal = String.valueOf(Integer.parseInt(tot_bal) - ce);

                mainTable.addCell("");
                mainTable.addCell(new Paragraph("      By"));
                mainTable.addCell(new Paragraph("Closing Balance"));
                mainTable.addCell("");
                mainTable.addCell("");
                if (ledger_type_text.getText().toString().equals("Debitor")) {
                    if (ce == 0) {
                        Log.d(TAG, "writeData: amount in debit: " + de + " and ce == 0");
                        mainTable.addCell("");
                        mainTable.addCell(String.valueOf((Integer.parseInt(opening_balance.getText().toString().substring(0, opening_balance.getText().toString().indexOf("."))) + tot_bill_bal + de) - ce));
                    } else {
                        Log.d(TAG, "writeData: amount in debit: " + de + " and ce != 0");
                        mainTable.addCell(String.valueOf((Integer.parseInt(opening_balance.getText().toString().substring(0, opening_balance.getText().toString().indexOf("."))) + tot_bill_bal + de) - ce));
                        mainTable.addCell("");
                    }
                } else {
                    if (de == 0) {
                        Log.d(TAG, "writeData: amount in credit: " + ce + " and de == 0");
                        mainTable.addCell(String.valueOf((Integer.parseInt(opening_balance.getText().toString().substring(0, opening_balance.getText().toString().indexOf("."))) + tot_bill_bal + ce) - de));
                        mainTable.addCell("");
                    } else {
                        Log.d(TAG, "writeData: amount in debit: " + ce + " and de != 0");
                        mainTable.addCell("");
                        mainTable.addCell(String.valueOf((Integer.parseInt(opening_balance.getText().toString().substring(0, opening_balance.getText().toString().indexOf("."))) + tot_bill_bal + ce) - de));
                    }
                }


                mainTable.addCell("");
                mainTable.addCell("");
                mainTable.addCell("");
                mainTable.addCell("");
                mainTable.addCell("");
                if (ledger_type_text.getText().toString().equals("Debitor")) {
                    if (ce == 0) {
                        mainTable.addCell((Integer.parseInt(opening_balance.getText().toString().substring(0, opening_balance.getText().toString().indexOf("."))) + de + tot_bill_bal) + ".00");
                        mainTable.addCell((Integer.parseInt(opening_balance.getText().toString().substring(0, opening_balance.getText().toString().indexOf("."))) + de + tot_bill_bal + ce) + ".00");
                    } else {
                        mainTable.addCell(((Integer.parseInt(opening_balance.getText().toString().substring(0, opening_balance.getText().toString().indexOf("."))) + de + tot_bill_bal) - ce) + ce + ".00");
                        mainTable.addCell((Integer.parseInt(opening_balance.getText().toString().substring(0, opening_balance.getText().toString().indexOf("."))) + de + tot_bill_bal) + ".00");
                    }
                } else {
                    if (de == 0) {
                        mainTable.addCell((Integer.parseInt(opening_balance.getText().toString().substring(0, opening_balance.getText().toString().indexOf("."))) + ce + tot_bill_bal) + ".00");
                        mainTable.addCell((Integer.parseInt(opening_balance.getText().toString().substring(0, opening_balance.getText().toString().indexOf("."))) + tot_bill_bal + de) + ".00");
                    } else {
                        mainTable.addCell(((Integer.parseInt(opening_balance.getText().toString().substring(0, opening_balance.getText().toString().indexOf("."))) + ce + tot_bill_bal) - de) + de + ".00");
                        mainTable.addCell((Integer.parseInt(opening_balance.getText().toString().substring(0, opening_balance.getText().toString().indexOf("."))) + ce + tot_bill_bal) + ".00");
                    }
                }
            }

            if (!ledger_date_text.getText().toString().isEmpty() || ledger_date_text.getText().toString() != null) {
                table.addCell("");
                table.addCell(ledger_date_text.getText().toString() + "   to   " + last_voucher_date);
                table.addCell("");

                Paragraph line_space = new Paragraph("");
                line_space.setSpacingBefore(50);

                document.add(table);
                document.add(line_space);
                document.add(mainTable);
            }

            document.close();

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    //read data from the file

    private void setFile(File file){
        filefornot = file;
    }

    private File getFile(){
        return filefornot;
    }

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

    @Override
    public void onStop() {
        super.onStop();
        //requireActivity().unregisterReceiver(calcBroadCastReceiver);
    }

    private void initViews(View view) {
        ledgerdetailsswiperefresh = view.findViewById(R.id.ledgerdetailsswiperefresh);
        ledgerdetailsswiperefresh.setOnRefreshListener(this);
        main_layout = view.findViewById(R.id.main_layout);

        ledger_date_text = view.findViewById(R.id.ledger_date_text);
        ledger_type_text = view.findViewById(R.id.ledger_type_text);
        ledger_number_text = view.findViewById(R.id.ledger_number_text);
        my_ledger_holder_name = view.findViewById(R.id.my_ledger_holder_name);
        my_client_address = view.findViewById(R.id.my_client_address);
        my_client_pincode = view.findViewById(R.id.my_client_pincode);
        my_client_state = view.findViewById(R.id.my_client_state);
        my_client_country = view.findViewById(R.id.my_client_country);

        debit_text = view.findViewById(R.id.my_client_ledger_debit_balance);
        credit_text = view.findViewById(R.id.my_client_ledger_credit_balance);
        opening_balance = view.findViewById(R.id.my_client_opening_balance);
        closing_balance = view.findViewById(R.id.my_client_closing_balance);

        print_ledger_voucher = view.findViewById(R.id.print_ledger_voucher);

        see_voucher_list_button = view.findViewById(R.id.see_voucher_list_button);

        content_prog_bar = view.findViewById(R.id.content_prog_bar);

        your_ledger_text = view.findViewById(R.id.your_ledger_text);

    }

    @Override
    public void onRefresh() {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
        ledgerdetailsswiperefresh.setRefreshing(false);
    }
}