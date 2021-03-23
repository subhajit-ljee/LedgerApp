package profile.profilefragments.qr;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.fonts.otf.TableHeader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import javax.inject.Inject;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import profile.ProfileActivity;
import profile.addledger.BillFileUtils;
import profile.qrscanner.QRCodeScanActivity;
import profile.upload.PdfUploadActivity;
import profile.upload.QrCodeUploadIntentService;
import profile.upload.dependency.UploadPdfComponent;
import profile.upload.model.PdfUploadRepository;
import profile.upload.model.UploadPDF;

import static android.content.Context.WINDOW_SERVICE;


public class CreateQrCodeFragment extends Fragment {

    private static final String TAG = "CreateQrCodeFragment";
    
    private ImageView qrCodeImg;
    private Button qrButton, go_for_scan;

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


        qrButton = view.findViewById(R.id.idDataQr);
        qrCodeImg = view.findViewById(R.id.idImageQr);

        go_for_scan = view.findViewById(R.id.go_for_scan);

        qrButton.setOnClickListener( v -> {

            uploadFile();

        });


        go_for_scan.setOnClickListener( v -> {

            Intent intent = new Intent(getActivity(), QRCodeScanActivity.class);
            startActivity(intent);

        });

        return view;
    }

    private void printQrInFile( String path ) {

        try {

            pdfStamper = new PdfStamper(pdfReader,new FileOutputStream(path));

            PdfContentByte content = pdfStamper.getOverContent(1);
            Log.d(TAG, "onActivityResult: number of pages " + 1);
            //content.addImage(image);

            WindowManager manager = getActivity().getWindowManager();

            Point point = new Point();
            Display display = manager.getDefaultDisplay();
            display.getSize(point);

            int width = point.x;
            int height = point.y;

            int dimen = (width < height)? width:height;
            dimen = dimen * 3 / 4;

            String qr_id = UUID.randomUUID().toString();

            qrgEncoder = new QRGEncoder(qr_id, null, QRGContents.Type.TEXT, dimen);
            try {

                bitmap = qrgEncoder.getBitmap();
                qrCodeImg.setImageBitmap(bitmap);

                ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream3);
                Image image = Image.getInstance(stream3.toByteArray());

                image.setAbsolutePosition(50, 665);
                image.scaleAbsolute(85,85);
                //image.scalePercent(40);

                content.addImage(image);

                stream3.close();
                pdfStamper.close();
                pdfReader.close();


                String simpledate = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault()).format(new Date());

                UploadPDF uploadPDF = new UploadPDF(qr_id, simpledate);

                Intent intent = new Intent(getActivity(), QrCodeUploadIntentService.class);

                intent.putExtra("qr_id",qr_id);
                intent.putExtra("simpledate",simpledate);

                QrCodeUploadIntentService.enqueueWork(getActivity(),intent);

            } catch (Exception e) {
                Log.e("Tag", e.toString());
            }



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
        if(requestCode ==1 && resultCode == getActivity().RESULT_OK
                && data != null && data.getData()!=null){
            Log.d(TAG, "onActivityResult: data: " + data.getData());
            //uploadPdfFile(data.getData());

            //thanks
            String path = BillFileUtils.getPath(getActivity(), data.getData());
            Log.d(TAG, "onActivityResult: path: " + path);

            Dexter.withContext(getActivity())
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override public void onPermissionGranted(PermissionGrantedResponse response) {

                            try{
                                pdfReader = new PdfReader(new FileInputStream(path));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        @Override public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
                        @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                    }).check();

            Dexter.withContext(getActivity())
                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                            printQrInFile(path);
                        }
                        @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                            Log.d(TAG, "onPermissionDenied: permission denied");
                        }
                        @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                    }).check();


            //Log.d(TAG, "onActivityResult: name: " + path);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}