package com.example.hp.teamproject;
//메뉴 정보 등록 액티비티---------------------------------------------------------------------------
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResistMenu extends AppCompatActivity {
    Uri MENUUri;
    File path;
    EditText MENUname;
    EditText MENUprice;
    EditText MENUcomment;
    String RS_id;
    String mPhotoFileName = null;
    File mPhotoFile = null;
    private RSdbHelper mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resistmenu);

        checkDangerousPermissions();//permission획득 체크
        mDbHelper = new RSdbHelper(this);

        Intent intent = getIntent(); //메뉴를 등록할 식당의 id값 받아오기
        RS_id = String.valueOf(intent.getIntExtra("RS_ID",0)); //int타입을 String으로 변환

        ImageView RSimage = (ImageView)findViewById(R.id.MENU_Image);
        RSimage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        Button ResistRS = (Button) findViewById(R.id.Resist_MENU);
        ResistRS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                insertRecord();
            }
        });
    }

    //메뉴 사진 촬영 후 sdcard에 저장하고 불러오기---------------------------------------------
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private void dispatchTakePictureIntent() { //카메라 앱 실행 요청
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePictureIntent.resolveActivity(getPackageManager())!= null) {
            //1. 카메라 앱으로 찍은 이미지를 저장할 파일 객체 생성
            mPhotoFileName = "IMG" + currentDateFormat() + ".jpg";  // 파일 이름 설정
            path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);// /storage/emulated/0/Android/data/com.example.hp.teamproject/files/Pictures
            mPhotoFile = new File(path, mPhotoFileName);// path폴더에 , mPhotoFileName 이라는 파일이름으로 저장

            if (mPhotoFile != null) {
                //2. 생성된 파일 객체에 대한 Uri 객체를 얻기
                MENUUri = Uri.fromFile(new File(path+mPhotoFileName));

                //3. Uri 객체를 Extras를 통해 카메라 앱으로 전달
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, MENUUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            } else
                Toast.makeText(getApplicationContext(), "file null", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        ImageView imageView = (ImageView) findViewById(R.id.MENU_Image);

        if (requestCode == REQUEST_IMAGE_CAPTURE) {

            if (mPhotoFileName != null) {
                rotatePhoto();
                try {
                    Bitmap image_bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), MENUUri);
                    imageView.setImageBitmap(image_bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else
                Toast.makeText(getApplicationContext(), "PhotoFile is null", Toast.LENGTH_SHORT).show();
        }
    }

    // 메뉴 정보 db에 등록하기----------------------------------------------------------------------
    private void insertRecord() {
        MENUname = (EditText)findViewById(R.id.MENUname); //맛집 이름 입력
        MENUprice = (EditText)findViewById(R.id.MENUprice); //맛집 번호 입력
        MENUcomment = (EditText)findViewById(R.id.MENUcomment); // 맛집 주소 입력

        long nOfRows = mDbHelper.insertMENUByMethod
                (String.valueOf(MENUUri),
                        MENUname.getText().toString(),MENUprice.getText().toString(), MENUcomment.getText().toString(),RS_id);

        if(nOfRows > 0) {
            Toast.makeText(this, "메뉴 등록중...", Toast.LENGTH_SHORT).show();
            Intent RestaurantDetail = new Intent(getApplicationContext(), MainRestaurant.class);
            RestaurantDetail.putExtra("MENU_id",RS_id);
            RestaurantDetail.putExtra("INT",2);
            startActivity(RestaurantDetail);

        }else Toast.makeText(this,"[Error] Try again",Toast.LENGTH_SHORT).show();
    }

    private String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
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
    public Bitmap getBitmap() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inInputShareable = true;
        options.inDither=false;
        options.inTempStorage=new byte[64 * 1024];
        options.inPurgeable = true;
        options.inJustDecodeBounds = false;

        File f = new File(MENUUri.getPath());

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
            exif = new ExifInterface(MENUUri.getPath());
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
        File file = new File(MENUUri.getPath());
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
