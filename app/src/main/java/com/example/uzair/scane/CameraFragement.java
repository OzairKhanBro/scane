package com.example.uzair.scane;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import net.doo.snap.camera.AutoSnappingController;
import net.doo.snap.camera.CameraOpenCallback;
import net.doo.snap.camera.ContourDetectorFrameHandler;
import net.doo.snap.camera.PictureCallback;
import net.doo.snap.camera.ScanbotCameraView;
import net.doo.snap.lib.detector.ContourDetector;
import net.doo.snap.lib.detector.DetectionResult;
import net.doo.snap.ui.PolygonView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.Activity.RESULT_OK;


public class CameraFragement extends Fragment implements PictureCallback,
        ContourDetectorFrameHandler.ResultHandler {

    private ScanbotCameraView cameraView;
    private PolygonView polygonView;
    private ImageView resultView;
    private ContourDetectorFrameHandler contourDetectorFrameHandler;
    private AutoSnappingController autoSnappingController;
    private Toast userGuidanceToast;
    private ImageView autoSnappingToggleButton;

    private boolean flashEnabled = false;
    private boolean autoSnappingEnabled = false;
    private static final int RESULT_LOAD_IMAGE=654;
    private Timer timer;
    final ArrayList<Integer> arrayList=new ArrayList<>();
    private RotateAnimation rotate_camer_icon_clockwise;


    @Override
    public void onStop() {
        super.onStop();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View baseView =  inflater.inflate(R.layout.activity_camera_fragement, container, false);

        cameraView = (ScanbotCameraView) baseView.findViewById(R.id.camera);
        cameraView.setCameraOpenCallback(new CameraOpenCallback() {
            @Override
            public void onCameraOpened() {
                cameraView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            cameraView.setAutoFocusSound(true);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                cameraView.setShutterSound(false);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        cameraView.continuousFocus();
                        cameraView.useFlash(flashEnabled);
                    }
                }, 700);
            }
        });

        resultView = (ImageView) baseView.findViewById(R.id.result);

        contourDetectorFrameHandler = ContourDetectorFrameHandler.attach(cameraView);

        // Please note: https://github.com/doo/Scanbot-SDK-Examples/wiki/Detecting-and-drawing-contours#contour-detection-parameters
        contourDetectorFrameHandler.setAcceptedAngleScore(60);
        contourDetectorFrameHandler.setAcceptedSizeScore(70);

        polygonView = (PolygonView) baseView.findViewById(R.id.polygonView);
        contourDetectorFrameHandler.addResultHandler(polygonView);
        contourDetectorFrameHandler.addResultHandler(this);

        autoSnappingController = AutoSnappingController.attach(cameraView, contourDetectorFrameHandler);

        cameraView.addPictureCallback(this);

        userGuidanceToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        userGuidanceToast.setGravity(Gravity.CENTER, 0, 0);

        baseView.findViewById(R.id.snap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.takePicture(false);
            }
        });

        baseView.findViewById(R.id.select_from_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, RESULT_LOAD_IMAGE);

            }
        });

        baseView.findViewById(R.id.flash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashEnabled = !flashEnabled;
                ((ImageView)(v)).setImageResource(
                        flashEnabled?R.drawable.ic_flash_off_black_24dp:
                                R.drawable.if_flash_light_2639815
                );
                cameraView.useFlash(flashEnabled);
            }
        });

        autoSnappingToggleButton = baseView.findViewById(R.id.autoSnappingToggle);
        autoSnappingToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoSnappingEnabled = !autoSnappingEnabled;
                setAutoSnapEnabled(autoSnappingEnabled);
            }
        });

        setAutoSnapEnabled(autoSnappingEnabled);
        arrayList.add(R.drawable.ic_if_camera_1);
        arrayList.add(R.drawable.ic_if_camera_2);
        arrayList.add(R.drawable.ic_if_camera_3);
        arrayList.add(R.drawable.ic_if_camera_5);
        arrayList.add(R.drawable.ic_if_camera_6);
        arrayList.add(R.drawable.ic_if_camera_4);
        arrayList.add(R.drawable.ic_if_camera_7);
        arrayList.add(R.drawable.ic_if_camera_8);
        arrayList.add(R.drawable.ic_if_camera_9);
        arrayList.add(R.drawable.ic_if_camera_10);
        arrayList.add(R.drawable.ic_if_camera_11);

        rotate_camer_icon_clockwise = new RotateAnimation(270,360 , Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate_camer_icon_clockwise.setDuration(100);
        rotate_camer_icon_clockwise.setInterpolator(new LinearInterpolator());


        return baseView;
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraView.onResume();
        final Random r=new Random();
        timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int last=arrayList.size()-1;
                        int index=arrayList.get(last);
                        // TODO Auto-generated method stub
                        ImageView image=((ImageView)getActivity().findViewById(R.id.snap));
                        image.setImageResource(arrayList.
                                get(r.nextInt(arrayList.size())));
                        image.startAnimation(rotate_camer_icon_clockwise);
                    }
                });
            }
        }, 1000, 2000);
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraView.onPause();
        timer.cancel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().findViewById(R.id.main_screen).setVisibility(View.VISIBLE);

    }

    @Override
    public boolean handleResult(final ContourDetectorFrameHandler.DetectedFrame detectedFrame) {
        // Here you are continuously notified about contour detection results.
        // For example, you can show a user guidance text depending on the current detection status.
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showUserGuidance(detectedFrame.detectionResult);
            }
        });

        return false; // typically you need to return false
    }

    private void showUserGuidance(final DetectionResult result) {
        if (!autoSnappingEnabled) {
            return;
        }

        switch (result) {
            case OK:
                userGuidanceToast.setText("Don't move");
                userGuidanceToast.show();
                break;
            case OK_BUT_TOO_SMALL:
                userGuidanceToast.setText("Move closer");
                userGuidanceToast.show();
                break;
            case OK_BUT_BAD_ANGLES:
                userGuidanceToast.setText("Perspective");
                userGuidanceToast.show();
                break;
            case ERROR_NOTHING_DETECTED:
                userGuidanceToast.setText("No Document");
                userGuidanceToast.show();
                break;
            case ERROR_TOO_NOISY:
                userGuidanceToast.setText("Background too noisy");
                userGuidanceToast.show();
                break;
            case ERROR_TOO_DARK:
                userGuidanceToast.setText("Poor light");
                userGuidanceToast.show();
                break;
            default:
                userGuidanceToast.cancel();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            Log.e("message:Uzair", "Uri " + imageUri.toString());
            try {
//                Intent IntentGallery = new Intent(this.getContext(), main_dashbord.class);
//                IntentGallery.setData(imageUri);
////                IntentGallery.removeExtra("image");
////                IntentGallery.putExtra("image",selectedImage);
//                startActivity(IntentGallery);

                Bitmap bm = BitmapFactory.decodeStream(
                        getActivity().getContentResolver().openInputStream(imageUri));
                Log.e("message:Uzair", "Uri " + bm.toString());
                closeFragement(bm);
                // loadImageFromLocalStore(imageUri.toString());
                //openActivity();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onPictureTaken(byte[] image, int imageOrientation) {
        // Here we get the full image from the camera.
        // Implement a suitable async(!) detection and image handling here.
        // This is just a demo showing detected image as downscaled preview image.

        // Decode Bitmap from bytes of original image:
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8; // use 1 for original size (if you want no downscale)!
        // in this demo we downscale the image to 1/8 for the preview.
        Bitmap originalBitmap = BitmapFactory.decodeByteArray(image, 0, image.length, options);

        // rotate original image if required:
        if (imageOrientation > 0) {
            final Matrix matrix = new Matrix();
            matrix.setRotate(imageOrientation, originalBitmap.getWidth() / 2f, originalBitmap.getHeight() / 2f);
            originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, false);
        }

        // Run document detection on original image:
        final ContourDetector detector = new ContourDetector();
        detector.detect(originalBitmap);
        final Bitmap documentImage = detector.processImageAndRelease(originalBitmap, detector.getPolygonF(), ContourDetector.IMAGE_FILTER_NONE);

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                closeFragement(documentImage);
            }
        });
        resultView.post(new Runnable() {
            @Override
            public void run() {
                resultView.setImageBitmap(documentImage);
//                cameraView.continuousFocus();
//                cameraView.startPreview();

                    }
        });
    }

    private void closeFragement(Bitmap documentImage) {
        ((main_dashbord) getActivity()).setCenterImage(documentImage);
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();

    }

    private void setAutoSnapEnabled(boolean enabled) {
        autoSnappingController.setEnabled(enabled);
        contourDetectorFrameHandler.setEnabled(enabled);
        polygonView.setVisibility(enabled ? View.VISIBLE : View.GONE);
        autoSnappingToggleButton.setImageResource(enabled ?
                R.drawable.ic_touch_app_black_24dp:R.drawable.meter_01_28);
    }



}
