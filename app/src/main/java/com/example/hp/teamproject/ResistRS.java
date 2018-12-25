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
    Uri RSUri;
    File path;
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


    //Restaurant사진 촬영 후 sdcard에 저장하고 불러오기------------------------------------------------
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private void dispatchTakePictureIntent() {//카메라 앱 실행 요청
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePictureIntent.resolveActivity(getPackageManager())!= null) {
            //1. 카메라 앱으로 찍은 이미지를 저장할 파일 객체 생성
            mPhotoFileName = "img"+ currentDateFormat() + ".jpg";  // 파일 이름 설정
           path = getExternalFilesDir(Environment.DIRECTORY_PICTURES); //

            mPhotoFile = new File(path,mPhotoFileName);

            if (mPhotoFile != null) {
                RSUri = Uri.fromFile(new File(path+mPhotoFileName)); //2. 생성된 파일 객체에 대한 Uri 객체를 얻기
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, RSUri);  //3. Uri 객체를 Extras를 통해 카메라 앱으로 전달
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else
                Toast.makeText(getApplicationContext(), "file null", Toast.LENGTH_SHORT).show();
        }
    }
 //사진찍고난후-----------------------------------------------------------------------------------------------
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        ImageView imageView = (ImageView) findViewById(R.id.RS_Image);

        if (requestCode == REQUEST_IMAGE_CAPTURE ) {
            if (mPhotoFileName != null) {
                rotatePhoto();
                try {
                    Bitmap image_bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), RSUri);
                    imageView.setImageBitmap(image_bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "mPhotoFile is null", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //이미지 파일 날짜 포맷 함수-----------------------------------------------------------------------------
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
                (String.valueOf(RSUri), RSname.getText().toString(),
                        RSnum.getText().toString(), RSadrress.getText().toString(),latitude, longitude);
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


    //사진 bitmap으로 변환---------------------------------------------------------------------------
    //https://github.com/skdlzl326/MyApplication/tree/master/app/src/main/java/com/example/star/myapplication[참고자료]
    public Bitmap getBitmap() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inInputShareable = true;
        options.inDither=false;
        options.inTempStorage=new byte[64 * 1024];
        options.inPurgeable = true;
        options.inJustDecodeBounds = false;

        File f = new File(RSUri.getPath());
        FileInputStream fs=null;
        try {
            fs = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bm = null;
        try {
            if(fs!=null) bm=BitmapFactory.decodeFileDescriptor(fs.getFD(), null, options);
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(fs!=null) {
                try {
                    fs.close();
                } catch (IOException e) {
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

            e.printStackTrace();
        }

    }

    public int exifOrientationToDegrees(int exifOrientation){
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