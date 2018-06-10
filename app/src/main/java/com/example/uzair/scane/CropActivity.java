package com.example.uzair.scane;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;

import net.doo.snap.lib.detector.ContourDetector;
import net.doo.snap.lib.detector.DetectionResult;
import net.doo.snap.lib.detector.Line2D;
import net.doo.snap.ui.EditPolygonImageView;
import net.doo.snap.ui.MagnifierView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class CropActivity extends Fragment {

    private EditPolygonImageView editPolygonView;
    private MagnifierView magnifierView;
    private Bitmap originalBitmap;
    private Button cropButton;
    private Button cancelButton;
    private Button rotateButton;
    private int rotationDegrees = 0;
    private long lastRotationEventTs = 0L;


    private RadioGroup radioGroup;
    private int result;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater i=getActivity().getLayoutInflater();
        View baseView =  i.inflate(R.layout.activity_crop, container, false);
//        result=ContourDetector.IMAGE_FILTER_NONE;
//        radioGroup=baseView.findViewById(R.id.radio_btn);
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
//        {
//            @Override
//            public void onCheckedChanged (RadioGroup group,int checkedId){
//                // checkedId is the RadioButton selected
//                if (originalBitmap == null)
//                    return;
//                result = ContourDetector.IMAGE_FILTER_NONE;
//                switch (checkedId) {
//                    case R.id._b_w:
//                        result = ContourDetector.IMAGE_FILTER_PURE_BINARIZED;
//                        break;
//                    case R.id._black:
//                        result = ContourDetector.IMAGE_FILTER_BINARIZED;
//                        break;
//                    case R.id._clr:
//                        result = ContourDetector.IMAGE_FILTER_COLOR_DOCUMENT;
//                        break;
//                    case R.id._gray:
//                        result = ContourDetector.IMAGE_FILTER_GRAY;
//                        break;
//                    case R.id._enh:
//                        result = ContourDetector.IMAGE_FILTER_COLOR_ENHANCED;
//                        break;
//                    case R.id._none:
//                        result = ContourDetector.IMAGE_FILTER_NONE;
//                        break;
//                }
//
////                Bitmap documentImage = new ContourDetector().processImageF(
////                        orgImage, null, result);
//                //image.setImageBitmap(null);
//
//            }
//        });
        editPolygonView = (EditPolygonImageView) baseView.findViewById(R.id.polygonView);
        editPolygonView.setImageBitmap(((main_dashbord)getActivity()).getCenterImage());
       ///////////////////////////////////////////////////////////////////
//        editPolygonView.setImageResource(R.drawable.test_receipt);
//        editPolygonView.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.test_receipt));
//        Uri selectedImgUri = getIntent().getData();
//        if (selectedImgUri != null) {
//            Log.e("Gallery ImageURI", "" + selectedImgUri);
//            String[] selectedImgPath = {MediaStore.Images.Media.DATA};
//
//            Cursor cursor = getContentResolver().query(selectedImgUri,
//                    selectedImgPath, null, null, null);
//            cursor.moveToFirst();
//
//            int indexCol = cursor.getColumnIndex(selectedImgPath[0]);
//            String imgPath = cursor.getString(indexCol);
//            cursor.close();
//            Bitmap bm = BitmapFactory.decodeStream(
////                    getContentResolver().openInputStream(orgUri));
//            try {
//                editPolygonView.setImageBitmap(BitmapFactory.decodeStream(
//                        getContentResolver().openInputStream(selectedImgUri)));
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//        else {
//            Bitmap bitmapimage = (Bitmap) getIntent().getParcelableExtra("image");
//            if (bitmapimage != null)
//                Log.e("message:Uzair", bitmapimage.toString());
//
//            editPolygonView.setImageBitmap(bitmapimage);
//        }
//
//        doneButton=baseView.findViewById(R.id.doneButton);
//        doneButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Log.e("imagg",resultImageView.toString());
////                Intent i=new Intent(getApplicationContext(),FilterActivity.class);
////                i.putExtra("image",resultImageView.getDrawingCache());
////                startActivity(i);
//            }
//        });

        ////////////////////////////
        originalBitmap = ((BitmapDrawable) editPolygonView.getDrawable()).getBitmap();

        magnifierView = (MagnifierView) baseView.findViewById(R.id.magnifier);
        // MagifierView should be set up every time when editPolygonView is set with new image
        magnifierView.setupMagnifier(editPolygonView);

//        resultImageView = (ImageView) baseView.findViewById(R.id.resultImageView);
//        resultImageView.setVisibility(View.GONE);

        cropButton = (Button) baseView.findViewById(R.id.cropButton);
        cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crop();
            }
        });

        rotateButton = (Button) baseView.findViewById(R.id.rotateButton);
        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotatePreview();
            }
        });
        cancelButton=(Button) baseView.findViewById(R.id.Cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removethisFragement();
            }
        });



//        backButton = (Button) baseView.findViewById(R.id.backButton);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                backButton.setVisibility(View.GONE);
//                resultImageView.setVisibility(View.GONE);
//
//                editPolygonView.setVisibility(View.VISIBLE);
//                cropButton.setVisibility(View.VISIBLE);
//                rotateButton.setVisibility(View.VISIBLE);
//                doneButton.setVisibility(View.GONE);
//            }
//        });
        new InitImageViewTask().executeOnExecutor(Executors.newSingleThreadExecutor(), originalBitmap);
    return baseView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().findViewById(R.id.main_screen).setVisibility(View.VISIBLE);

    }
    private void removethisFragement()
    {
        getActivity().getSupportFragmentManager()
                .beginTransaction().remove(this).commit();
    }
    private void rotatePreview() {
        if ((System.currentTimeMillis() - lastRotationEventTs) < 350) {
            return;
        }
        rotationDegrees += 90;
        //editPolygonView.
        editPolygonView.rotateClockwise(); // only rotates the preview image (animated)
        lastRotationEventTs = System.currentTimeMillis();
    }

    private void crop() {
        // crop & warp image by selected polygon (editPolygonView.getPolygon())
        Bitmap documentImage = new ContourDetector().processImageF(
                originalBitmap, editPolygonView.getPolygon(),
                result);//ContourDetector.IMAGE_FILTER_NONE);

        if (rotationDegrees > 0) {
            // rotate the final cropped image result based on current rotation value:
            final Matrix matrix = new Matrix();
            matrix.postRotate(rotationDegrees);
            documentImage = Bitmap.createBitmap(documentImage, 0, 0, documentImage.getWidth(), documentImage.getHeight(), matrix, true);
        }

        editPolygonView.setVisibility(View.GONE);
        cropButton.setVisibility(View.GONE);
        rotateButton.setVisibility(View.GONE);

        ((main_dashbord) getActivity()).setCenterImage(documentImage);

        removethisFragement();
//        resultImageView.setImageBitmap(documentImage);
//        resultImageView.setVisibility(View.VISIBLE);
//        backButton.setVisibility(View.VISIBLE);
//        doneButton.setVisibility(View.VISIBLE);

        Log.e("imagg",documentImage.toString());
        //Intent i=new Intent(getApplication(),FilterActivity.class);
//
//        ByteArrayOutputStream bs = new ByteArrayOutputStream();
//        documentImage.compress(Bitmap.CompressFormat.PNG, 50, bs);
//
//        i.putExtra("image",bs.toByteArray());
//        Uri tempUri = getImageUri(getApplicationContext(), documentImage);
//        i.setData(tempUri);
//        startActivity(i);
    }

    /**
     * Detects horizontal and vertical lines and polygon of the given bitmap image.
     * Initializes EditPolygonImageView with detected lines and polygon.
     */
    class InitImageViewTask extends AsyncTask<Bitmap, Void, InitImageResult> {

        @Override
        protected InitImageResult doInBackground(Bitmap... params) {
            Bitmap image = params[0];
            ContourDetector detector = new ContourDetector();
            final DetectionResult detectionResult = detector.detect(image);
            Pair<List<Line2D>, List<Line2D>> linesPair = null;
            List<PointF> polygon = new ArrayList<>(EditPolygonImageView.DEFAULT_POLYGON);

            switch (detectionResult) {
                case OK:
                case OK_BUT_BAD_ANGLES:
                case OK_BUT_TOO_SMALL:
                case OK_BUT_BAD_ASPECT_RATIO:
                    linesPair = new Pair<>(detector.getHorizontalLines(), detector.getVerticalLines());
                    polygon = detector.getPolygonF();
                    break;
            }

            return new InitImageResult(linesPair, polygon);
        }

        @Override
        protected void onPostExecute(final InitImageResult initImageResult) {
            // set detected polygon and lines into EditPolygonImageView
            editPolygonView.setPolygon(initImageResult.polygon);
            if (initImageResult.linesPair != null) {
                editPolygonView.setLines(initImageResult.linesPair.first, initImageResult.linesPair.second);
            }
        }
    }

    class InitImageResult {
        final Pair<List<Line2D>, List<Line2D>> linesPair;
        final List<PointF> polygon;

        InitImageResult(final Pair<List<Line2D>, List<Line2D>> linesPair, final List<PointF> polygon) {
            this.linesPair = linesPair;
            this.polygon = polygon;

        }
    }
//    public Uri getImageUri(Context inContext, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }
//    public String getRealPathFromURI(Uri uri) {
//        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//        return cursor.getString(idx);
//    }

}
