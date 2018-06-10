package com.example.uzair.scane;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.net.URI;

public class main_dashbord extends AppCompatActivity implements View.OnClickListener{

    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1531;
    private static final int RESULT_LOAD_IMAGE = 456;
    private static final int CAMERA_REQUEST_CODE = 64;
    public static final File APP_FOLDER_ABSOLUTE_PATH= new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/"+"scane");
    private static final String MY_APP_FOLDER="scane";

    private boolean cameraPermissionGranted;
    private ImageView save;
    private ImageView openFolder;
    private ImageView rotateRight;
    private ImageView rotateLeft;
    private ImageView delete;
    private ImageView crop;
    private ImageView camera;
    ///private ImageView share;
    private ImageView CenterImage;
    private ImageView filter;
    private long lastRotationEventTs;
    private int rotationDegrees;
    private CameraFragement cameraFragement;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.


                } else {


                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            }
            case CAMERA_REQUEST_CODE:
            {
                cameraPermissionGranted=true;

                break;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            Log.e("message:Uzair", "Uri " + imageUri.toString());
            Picasso.get().load(imageUri).fit().into(CenterImage);

//            try {
////                Intent IntentGallery = new Intent(getApplication(), main_dashbord.class);
////                IntentGallery.setData(imageUri);
//////                IntentGallery.removeExtra("image");
//////                IntentGallery.putExtra("image",selectedImage);
////                startActivity(IntentGallery);
//
////                Bitmap bm = BitmapFactory.decodeStream(
////                        getContentResolver().openInputStream(imageUri));
////                Log.e("message:Uzair", "Uri " + bm.toString());
////                CenterImage.setImageBitmap(bm);
//                // loadImageFromLocalStore(imageUri.toString());
//                //openActivity();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        //setSupportActionBar((Toolbar) findViewById(R.id.camera_toolbar));
        setContentView(R.layout.activity_main_dashbord);

        cameraFragement=null;
        openFolder=findViewById(R.id._folder);
        rotateLeft=findViewById(R.id._rotate_left);
        rotateRight=findViewById(R.id._rotate_right);
        delete=findViewById(R.id._delete);
        crop=findViewById(R.id._crop);
        //share=findViewById(R.id._shere);
        filter=findViewById(R.id._filter);
        CenterImage=findViewById(R.id._center_image);
        camera=findViewById(R.id._open_camera);

        camera.setOnClickListener(this);
        openFolder.setOnClickListener(this);
        rotateLeft.setOnClickListener(this);
        rotateRight.setOnClickListener(this);
        delete.setOnClickListener(this);
        crop.setOnClickListener(this);
        //share.setOnClickListener(this);
        filter.setOnClickListener(this);



        Uri selectedImgUri = getIntent().getData();
        if (selectedImgUri != null) {
//            try {
//                Log.e("Uzair:Uri Recived", selectedImgUri.toString());
//                CenterImage.setImageBitmap(BitmapFactory.decodeStream(
//                        getContentResolver().openInputStream(selectedImgUri)));
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
            Picasso.get().load(selectedImgUri).fit().into(CenterImage);

        }
        else
        {
            start();
        }
//              {
//            Bitmap bitmapimage = (Bitmap) getIntent().getParcelableExtra("image");
//            if (bitmapimage != null)
//                Log.e("Uzair:bitmap Recived", bitmapimage.toString());
//            CenterImage.setImageBitmap(bitmapimage);
//        }


//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//
//            }
//        });

    }

    @Override
    public void onClick(View v) {
        ImageView clickedView=(ImageView)v;
        switch (v.getId()) {
            case R.id._folder:
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");

                startActivityForResult(gallery, RESULT_LOAD_IMAGE);

                break;
            case R.id._open_camera:
            {
                Log.e("uzair", "Croped clicked");
                findViewById(R.id.main_screen).setVisibility(View.GONE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragements_frame, getCameraFragement(), "Camera").commit();
            }
            break;
        }
        if(getCenterImage()==null)
            return;

        switch (v.getId()) {
            case R.id._rotate_left:
            {
                if ((System.currentTimeMillis() - lastRotationEventTs) < 350) {
                return;
                }
                RotateAnimation rotate = new RotateAnimation(90, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(150);
                rotate.setInterpolator(new LinearInterpolator());

                CenterImage.startAnimation(rotate);

                rotationDegrees += 90;
                //editPolygonView.
                Matrix matrix = new Matrix();
                matrix.postRotate(-90);
                Bitmap myImg=((BitmapDrawable) CenterImage.getDrawable()).getBitmap();
                Bitmap rotated = Bitmap.createBitmap(myImg, 0, 0, myImg.getWidth(), myImg.getHeight(),
                        matrix, true);
                CenterImage.setImageBitmap(rotated);
                lastRotationEventTs = System.currentTimeMillis();
                break;
            }
            case R.id._rotate_right:
            {
                if ((System.currentTimeMillis() - lastRotationEventTs) < 350) {
                    return;
                }

                RotateAnimation rotate = new RotateAnimation(270,360 , Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(150);
                rotate.setInterpolator(new LinearInterpolator());

                CenterImage.startAnimation(rotate);
///
                rotationDegrees -= 90;
                //editPolygonView.
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap myImg=((BitmapDrawable) CenterImage.getDrawable()).getBitmap();
                Bitmap rotated = Bitmap.createBitmap(myImg, 0, 0, myImg.getWidth(), myImg.getHeight(),
                        matrix, true);
                CenterImage.setImageBitmap(rotated);
                lastRotationEventTs = System.currentTimeMillis();
                break;}
            case R.id._delete: {

                confirmationDialog();
                break;
            }

            case R.id._crop: {
                Log.e("uzair", "Croped clicked");

                findViewById(R.id.main_screen).setVisibility(View.GONE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.fragements_frame, new CropActivity(), "Crop").commit();

            }   break;
//            case R.id._shere:
//                break;

            case R.id._filter: {
                Log.e("uzair", "Croped clicked");

                findViewById(R.id.main_screen).setVisibility(View.GONE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.fragements_frame, new FilterActivity(), "Filter").commit();

                break;
            }

        }

    }

    private void confirmationDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(main_dashbord.this);

            builder
                    .setMessage("Do you want to Discart this?")
                    .setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            start();
                            // Yes-code
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    })
                    .show();

    }


    private  void start()
    {
        Log.e("uzair", "Camera Started");
        if(!isCameraPermissionGranted())
            return;
        findViewById(R.id.main_screen).setVisibility(View.GONE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.fragements_frame, getCameraFragement(), "Camera").commit();

    }

    private boolean isCameraPermissionGranted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
                cameraPermissionGranted=false;
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.CAMERA)) {return  false;
//            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_REQUEST_CODE);
                return false;
            }

        return  true;
    }

    public ImageView getSave() {
        return save;
    }

    public void setSave(ImageView save) {
        this.save = save;
    }

    public ImageView getOpenFolder() {
        return openFolder;
    }

    public void setOpenFolder(ImageView openFolder) {
        this.openFolder = openFolder;
    }


    public ImageView getCrop() {
        return crop;
    }

    public void setCrop(ImageView crop) {
        this.crop = crop;
    }

    public Bitmap getCenterImage() {
        if(CenterImage.getDrawable()!=null)
            return ((BitmapDrawable) CenterImage.getDrawable()).getBitmap();
        else return null;
    }

    public void setCenterImage(Bitmap centerImage) {
        CenterImage.setImageBitmap(centerImage);
    }

    public CameraFragement getCameraFragement() {
        if(cameraFragement==null)
            cameraFragement=new CameraFragement();
        return cameraFragement;
    }

    public void setCameraFragement(CameraFragement cameraFragement) {
        this.cameraFragement = cameraFragement;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.camera_top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save_image) {
            Log.e("storage","i m about to save");
            SaveImage(getCenterImage());
            startActivity(new Intent(main_dashbord.this,AllImagesActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public int mkFolder(){//String folderName){ // make a folder under Environment.DIRECTORY_DCIM
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)){
            Log.d("myAppName", "Error: external storage is unavailable");
            return 0;
        }
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.d("myAppName", "Error: external storage is read only.");
            return 0;
        }
        Log.d("myAppName", "External storage is not read only or unavailable");

        if (ContextCompat.checkSelfPermission(this, // request permission when it is not granted.
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("myAppName", "permission:WRITE_EXTERNAL_STORAGE: NOT granted!");
            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//
//                // Show an expanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            //}
        }
        File folder = APP_FOLDER_ABSOLUTE_PATH;//new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),folderName);
        Log.e("app Path",APP_FOLDER_ABSOLUTE_PATH.toString());
        Log.e("current Path",folder.toString());

        int result = 0;
        if (folder.exists()) {
            Log.d("myAppName","folder exist:"+folder.toString());
            result = 2; // folder exist
        }else{
            try {
                if (folder.mkdir()) {
                    Log.d("myAppName", "folder created:" + folder.toString());
                    result = 1; // folder created
                } else {
                    Log.e("Scane", "creat folder fails:" + folder.toString());
                    result = 0; // creat folder fails
                }
            }catch (Exception ecp){
                ecp.printStackTrace();
            }
        }

        return result;
    }


    private void SaveImage(Bitmap finalBitmap) {

        if(mkFolder()==0)
            return;
        File myDir =APP_FOLDER_ABSOLUTE_PATH;// new File(APP_FOLDER_ABSOLUTE_PATH.toString());
        //myDir.mkdir();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ssmmHHddMMyy");
        String  fname = simpleDateFormat.format(new Date())+".jpg";
        //String fname = "Image-.jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();

        try {
//            int i=1;
//            while(file.exists ())
//                file=new File (myDir,i+fname);
            Log.e("file",file.toString());
            //file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            Log.d("saved",file.toString());
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Not saved",file.toString());

        }
    }



    private static void SaveImage2(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(APP_FOLDER_ABSOLUTE_PATH.toString());
        myDir.mkdirs();

        String fname = "Image-.jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
