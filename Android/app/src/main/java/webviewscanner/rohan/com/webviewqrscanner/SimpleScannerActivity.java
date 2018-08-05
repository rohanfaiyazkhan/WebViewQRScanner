package webviewscanner.rohan.com.webviewqrscanner;

import android.Manifest;
import android.app.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimpleScannerActivity extends Activity {

    private SurfaceView cameraView;
    //private TextView barcodeInfo;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    //Camera.Size mPreviewSize;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_simple_scanner);
        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        //barcodeInfo = (TextView) findViewById(R.id.code_info);

        barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();

        int width = 1280;
        int height = 960;

        //mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(width, height)
                .setAutoFocusEnabled(true)
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            public int requestCode = 1;

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(SimpleScannerActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                        ActivityCompat.requestPermissions(SimpleScannerActivity.this, new String[] {Manifest.permission.CAMERA}, requestCode);
                        return;
                    }
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException ie) {
                    //Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                String url;
                if (barcodes.size() != 0) {
                    url = barcodes.valueAt(0).displayValue;
                    if(URLUtil.isValidUrl(url)) {
                        Intent qrIntent = new Intent();
                        qrIntent.putExtra("QR Scan Status", "success");
                        qrIntent.putExtra("Scan Result", url);
                        setResult(RESULT_OK, qrIntent);
                        finish();
                    }else{
                        //Log.d("URLError", "Invalid URL");
                        Toast.makeText(getBaseContext(), "QR Code is not valid URL", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

//        Bitmap myQRCode;
//        try {
//            myQRCode = BitmapFactory.decodeStream(
//                    getAssets().open("myqrcode.jpg")
//            );
//            BarcodeDetector barcodeDetector =
//                    new BarcodeDetector.Builder(this)
//                            .setBarcodeFormats(Barcode.QR_CODE)
//                            .build();
//            Frame myFrame = new Frame.Builder()
//                    .setBitmap(myQRCode)
//                    .build();
//            SparseArray<Barcode> barcodes = barcodeDetector.detect(myFrame);
//            if(barcodes.size() != 0) {
//
//                // Print the QR code's message
//                Log.d("My QR Code's Data",
//                        barcodes.valueAt(0).displayValue
//                );
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }



}