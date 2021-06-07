package profile.profilefragments.qr;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sourav.ledgerproject.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import profile.addledger.BillFileUtils;
import profile.upload.QrCodeUploadIntentService;
import profile.upload.model.UploadPDF;


public class CreateQrCodeFragment extends Fragment {

    private static final String TAG = "CreateQrCodeFragment";

    private Button qrButton, go_for_scan, enter_bill_amount;
    private MaterialTextView text_choose_pdf;
    private MaterialToolbar toolbar2;
    private Bitmap bitmap;
    private QRGEncoder qrgEncoder;

    private PdfReader pdfReader;
    private PdfStamper pdfStamper;

    public CreateQrCodeFragment() {

    }


    public static CreateQrCodeFragment newInstance() {
        CreateQrCodeFragment fragment = new CreateQrCodeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_qr_code, container, false);

        toolbar2 = view.findViewById(R.id.toolbar2);
        toolbar2.setNavigationOnClickListener( (v) -> {
            requireActivity().onBackPressed();
        });

        qrButton = view.findViewById(R.id.idDataQr);
        text_choose_pdf = view.findViewById(R.id.text_choose_pdf);
        enter_bill_amount = view.findViewById(R.id.enter_bill_amount);
        go_for_scan = view.findViewById(R.id.go_for_scan);

        qrButton.setOnClickListener( v -> {

            new MaterialAlertDialogBuilder(requireContext())
                    .setMessage("Want to Upload Bill and print QR")
                    .setPositiveButton("yes", (dialog, which) -> {
                        uploadFile();
                    })
                    .setNegativeButton("no", null)
                    .create()
                    .show();;
        });

        enter_bill_amount.setOnClickListener( v -> {
            NavController controller = Navigation.findNavController(requireActivity(),R.id.bill_main_fragment);
            controller.navigate(R.id.action_createQrCodeFragment_to_billChooseClientFragment);
        });


        go_for_scan.setOnClickListener( v -> {

            NavController controller = Navigation.findNavController(requireActivity(),R.id.bill_main_fragment);
            controller.navigate(R.id.action_createQrCodeFragment_to_qrCodeScannerFragment);
        });

        return view;
    }

    private void printQrInFile( String path , String filename) {


        Log.d(TAG, "onActivityResult: number of pages " + 1);
        //content.addImage(image);

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
        try {

            bitmap = qrgEncoder.getBitmap();

            ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream3);
            Image image = Image.getInstance(stream3.toByteArray());
            pdfStamper = new PdfStamper(pdfReader,new FileOutputStream(path));
            PdfContentByte content = null;
            Log.d(TAG, "printQrInFile: number of pages: " + pdfReader.getNumberOfPages());
            for(int i = 1 ; i <= pdfReader.getNumberOfPages() ; i++) {
                content = pdfStamper.getOverContent(i);
                image.setAbsolutePosition(50, 665);
                image.scaleAbsolute(85, 85);
                //image.scalePercent(40);

                content.addImage(image);
            }

            stream3.close();
            pdfStamper.close();
            pdfReader.close();

            String simpledate = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault()).format(new Date());

            Intent intent = new Intent(getActivity(), QrCodeUploadIntentService.class);

            intent.putExtra("qr_id",qr_id);
            intent.putExtra("simpledate",simpledate);

            QrCodeUploadIntentService.enqueueWork(getActivity(),intent);

            new MaterialAlertDialogBuilder(requireContext())
                    .setMessage("QR Code Printed on " + filename + "\n Go to Downloads folder of your device")
                    .setPositiveButton("ok", null)
                    .create()
                    .show();


        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

    }

    private void uploadFile() {

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent," Select Your Bill "),1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        Log.d(TAG, "onActivityResult: ");
        if(requestCode ==1 && resultCode == Activity.RESULT_OK
                && data != null && data.getData()!=null){
            Log.d(TAG, "onActivityResult: data: " + data.getData());
            //uploadPdfFile(data.getData());

            //thanks
            String path = BillFileUtils.getPath(getActivity(), data.getData());
            File file = null;
            StringBuffer s = null;
            if(!path.isEmpty()) {
                file = new File(path);
                s = new StringBuffer(path);
            }

            try {
                StringBuffer new_file_path = s.replace(s.lastIndexOf("/")+1,s.length(),file.getName().replaceFirst("[.][^.]+$", "") + "_copy"+".pdf");
                Log.d(TAG, "onActivityResult: new_file_path: " + new_file_path);
                File new_copied_file = new File(new_file_path.toString());
                Files.copy(file.toPath(), new_copied_file.toPath());

                Log.d(TAG, "onActivityResult: path: " + path);

                File finalFile = file;
                Dexter.withContext(requireActivity())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @SuppressLint("SetTextI18n")
                            @Override public void onPermissionGranted(PermissionGrantedResponse response) {

                                try{

                                    pdfReader = new PdfReader(new FileInputStream(path));
                                    if(!finalFile.getName().isEmpty())
                                        text_choose_pdf.setText(finalFile.getName());
                                    else
                                        text_choose_pdf.setText("error: File name not found");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            @Override public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
                            @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                        }).check();

                Dexter.withContext(requireActivity())
                        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override public void onPermissionGranted(PermissionGrantedResponse response) {

                                printQrInFile(new_file_path.toString(), new_copied_file.getName());
                            }
                            @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                                Log.d(TAG, "onPermissionDenied: permission denied");
                            }
                            @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                        }).check();


                //Log.d(TAG, "onActivityResult: name: " + path);
                super.onActivityResult(requestCode, resultCode, data);


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }


}