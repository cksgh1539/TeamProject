package com.example.hp.teamproject;
//맛집 정보 등록 액티비티---------------------------------------------------------------------------


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import junit.framework.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.DIRECTORY_DCIM;
import static android.os.Environment.getExternalStorageDirectory;

public class ResistRS extends AppCompatActivity{
   static Uri RSUri;
    EditText RSname;
    EditText RSnum;
    EditText RSadrress;
    String mPhotoFileName = null;
    File mPhotoFile = null;
    String latitude, longitude;

    String TAG = "camera";

    private RSdbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resistrs);

        checkDangerousPermissions();//permission획득 체크
        mDbHelper = new RSdbHelper(this);

        Intent intent = getIntent();
        latitude = intent.getStringExtra("RSlatitude");
        longitude = intent.getStringExtra("RSlongitude");

        ImageView RSimage = (ImageView)findViewById(R.id.RS_Image);
        RSimage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        Button ResistRS = (Button) findViewById(R.id.Resist_RS);
        ResistRS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                insertRecord();
            }
        });
    }

    //Restaurant사진 촬영 후 sdcard에 저장하고 불러오기---------------------------------------------
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private void dispatchTakePictureIntent() {//카메라 앱 실행 요청
     //   Uri RSUri=null;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePictureIntent.resolveActivity(getPackageManager())!= null) {
            //1. 카메라 앱으로 찍은 이미지를 저장할 파일 객체 생성
            mPhotoFileName = "img"+ currentDateFormat() + ".jpg";
            // 파일 이름 설정

          //  File path = new File(Environment.getExternalStorageDirectory());
           File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            // 외부공용디렉토리의픽쳐폴더(sdcard/Pictures)의 폴더를 path로 가져옴
          //  mPhotoFile = new File("/storage/emulated/legacy/data/DCIM/Camera", mPhotoFileName);
         //   Log.i(TAG,"path="+path);
           //mPhotoFile = new File(path, mPhotoFileName);

            mPhotoFile = new File(
                    path,mPhotoFileName);
           // mPhotoFile = new File("/storage/emulated/legacy/DCIM/Camera/", mPhotoFileName);
           // mPhotoFile = new File(path, "storage/emulated/legacy/data/DCIM/Camera" + "/"+ mPhotoFileName);// path폴더에 , mPhotoFileName 이라는 파일이름으로 저장

            if (mPhotoFile != null) {
                            Log.i(TAG,"patsdfh="+mPhotoFile);
               // mPhotoFile = new File(Environment.getExternalStorageDirectory( ), "storage/emulated/legacy/data/DCIM/Camera" + "/"+ mPhotoFileName);
                //2. 생성된 파일 객체에 대한 Uri 객체를 얻기
            //    RSUri = FileProvider.getUriForFile(this,"com.example.hp.teamproject", mPhotoFile);
                RSUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath()
                        +"/Android/data/com.example.hp.teamproject/files/Pictures/"+mPhotoFileName));
                //"file:///storage/emulated/0/Pictures/"+mPhotoFileName
                //3. Uri 객체를 Extras를 통해 카메라 앱으로 전달
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, RSUri);
                Log.i(TAG,"patsdfh="+mPhotoFileName+"   URI"+RSUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
              //  Log.i(TAG, getLocalClassName() + " :camera "+path+"="+mPhotoFile);
            } else
                Toast.makeText(getApplicationContext(), "file null", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        Log.i(TAG,"파일 경로="+RSUri);
     //   Uri imgUri = RSUri;
        ImageView imageView = (ImageView) findViewById(R.id.RS_Image);
        Log.i(TAG, getLocalClassName() + " :savePicture"+resultCode+RESULT_OK+REQUEST_IMAGE_CAPTURE);

       /* rotatePhoto();
        try {
            Bitmap image_bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), RSUri);
            imageView.setImageBitmap(image_bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }*/

        if (requestCode == REQUEST_IMAGE_CAPTURE ) {
            Log.i(TAG, getLocalClassName() + " :savePicture1");
            Log.i(TAG,"파일 이름="+mPhotoFileName);
            if (mPhotoFileName != null) {
                rotatePhoto();
                try {
                    Bitmap image_bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), RSUri);
                    imageView.setImageBitmap(image_bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
              //  Log.i(TAG,"파일 경로="+imgUri);
             //   mPhotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), mPhotoFileName);
                Log.i(TAG,"mPhotoFIlePath="+mPhotoFile);
                Log.i(TAG, getLocalClassName() + " :savePicture2");
               // imageView.setImageURI(RSUri);

            } else {
                Toast.makeText(getApplicationContext(), "mPhotoFile is null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }


    // 맛집 정보 db에 등록하기----------------------------------------------------------------------
    private void insertRecord() {
        RSname = (EditText)findViewById(R.id.RSname); //맛집 이름 입력
        RSnum = (EditText)findViewById(R.id.RSnum); //맛집 번호 입력
        RSadrress = (EditText)findViewById(R.id.RSadrress); // 맛집 주소 입력

        long nOfRows = mDbHelper.insertRSByMethod
                ("file:///storage/emulated/0/Android/data/com.example.hp.teamproject/files/Pictures/"+mPhotoFileName, RSname.getText().toString(),
                        RSnum.getText().toString(), RSadrress.getText().toString(),latitude, longitude);
        Log.i(TAG, getLocalClassName() + " :insert" + nOfRows);


        if(nOfRows > 0) {
            Toast.makeText(this, "맛집 등록중...", Toast.LENGTH_SHORT).show();

            Intent TestRS = new Intent(getApplicationContext(), MainRestaurant.class);
            TestRS.putExtra("INT",0);
            startActivity(TestRS);

        }else Toast.makeText(this,"[Error] Try again",Toast.LENGTH_SHORT).show();
    }


    //permission확인 메소드-------------------------------------------------------------------------
    final int  REQUEST_EXTERNAL_STORAGE_FOR_MULTIMEDIA=1;
    private void checkDangerousPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int permissionCheck = PackageManager.PERMISSION_GRANTED;

        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_EXTERNAL_STORAGE_FOR_MULTIMEDIA);
        }
    }

    public Bitmap getBitmap() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inInputShareable = true;
        options.inDither=false;
        options.inTempStorage=new byte[32 * 1024];
        options.inPurgeable = true;
        options.inJustDecodeBounds = false;

        File f = new File(RSUri.getPath());

        FileInputStream fs=null;
        try {
            fs = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            //TODO do something intelligent
            e.printStackTrace();
        }

        Bitmap bm = null;

        try {
            if(fs!=null) bm=BitmapFactory.decodeFileDescriptor(fs.getFD(), null, options);
        } catch (IOException e) {
            //TODO do something intelligent
            e.printStackTrace();
        } finally{
            if(fs!=null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return bm;
    }


    public void rotatePhoto() {
        ExifInterface exif;
        try {
            exif = new ExifInterface(RSUri.getPath());
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int exifDegree = exifOrientationToDegrees(exifOrientation);
            if(exifDegree != 0) {
                Bitmap bitmap = getBitmap();
                Bitmap rotatePhoto = rotate(bitmap, exifDegree);
                saveBitmap(rotatePhoto);
            }
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public int exifOrientationToDegrees(int exifOrientation)
    {
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)
        {
            return 90;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)
        {
            return 180;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
        {
            return 270;
        }
        return 0;
    }

    public static Bitmap rotate(Bitmap image, int degrees)
    {
        if(degrees != 0 && image != null)
        {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float)image.getWidth(), (float)image.getHeight());

            try
            {
                Bitmap b = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), m, true);

                if(image != b)
                {
                    image.recycle();
                    image = b;
                }

                image = b;
            }
            catch(OutOfMemoryError ex)
            {
                ex.printStackTrace();
            }
        }
        return image;
    }


    public void saveBitmap(Bitmap bitmap) {
        File file = new File(RSUri.getPath());
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
        try {
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}