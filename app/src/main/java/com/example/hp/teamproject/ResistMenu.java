package com.example.hp.teamproject;
//메뉴 정보 등록 액티비티---------------------------------------------------------------------------
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResistMenu extends AppCompatActivity {
    Uri imageUri;
    EditText MENUname;
    EditText MENUprice;
    EditText MENUcoment;
    String mPhotoFileName = null;
    File mPhotoFile = null;
    private RSdbHelper mDbHelper;

    String TAG = "food";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resistmenu);

        checkDangerousPermissions();//permission획득 체크
        mDbHelper = new RSdbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

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
                //viewAllToListView();
            }
        });
    }


    //메뉴 사진 촬영 후 sdcard에 저장하고 불러오기---------------------------------------------
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private void dispatchTakePictureIntent() { //카메라 앱 실행 요청
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePictureIntent.resolveActivity(getPackageManager())!= null) {
            //1. 카메라 앱으로 찍은 이미지를 저장할 파일 객체 생성
            mPhotoFileName = "IMG" + currentDateFormat() + ".jpg";
            // 파일 이름 설정

            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            // 외부공용디렉토리의픽쳐폴더(sdcard/Pictures)의 폴더를 path로 가져옴

            mPhotoFile = new File(path, mPhotoFileName);// path폴더에 , mPhotoFileName 이라는 파일이름으로 저장

            if (mPhotoFile != null) {
                //2. 생성된 파일 객체에 대한 Uri 객체를 얻기
                imageUri = FileProvider.getUriForFile(this,
                        "com.example.hp.teamproject", mPhotoFile);

                //3. Uri 객체를 Extras를 통해 카메라 앱으로 전달
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                Log.i(TAG, getLocalClassName() + " :camera");
            } else
                Toast.makeText(getApplicationContext(), "file null", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        ImageView imageView = (ImageView) findViewById(R.id.RS_Image);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageUri = FileProvider.getUriForFile(this,
                    "com.example.hp.teamproject", mPhotoFile);

            if (mPhotoFileName != null) {
                try {
                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    mPhotoFile = new File(path, mPhotoFileName);

                    //Bitmap bitmap = ((BitmapDrawable)mPhotoFile).getBitmap();
                    //http://citynetc.tistory.com/150 bitmap으로 db에 이미지 저장
                    Log.i(TAG, getLocalClassName() + " :savePicture");
                    imageView.setImageURI(imageUri); //imageView에 찍은 사진 표시
                }catch (Exception e) {
                    e.printStackTrace();
                }
            } else
                Toast.makeText(getApplicationContext(), "PhotoFile is null",
                        Toast.LENGTH_SHORT).show();
        }
    }

    private String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    // 메뉴 정보 db에 등록하기----------------------------------------------------------------------
    private void insertRecord() {
        MENUname = (EditText)findViewById(R.id.MENUname); //맛집 이름 입력
        MENUprice = (EditText)findViewById(R.id.MENUprice); //맛집 번호 입력
        MENUcoment = (EditText)findViewById(R.id.MENUcoment); // 맛집 주소 입력

        long nOfRows = mDbHelper.insertMENUByMethod
                ( imageUri.toString(), MENUname.getText().toString(),
                        MENUprice.getText().toString(), MENUcoment.getText().toString());
        Log.i(TAG, getLocalClassName() + " :insert" + nOfRows);

        if(nOfRows > 0) {
            Toast.makeText(this, "메뉴 등록중...", Toast.LENGTH_SHORT).show();

            Intent RestaurantDetail = new Intent(getApplicationContext(), RestaurantDetail.class);
            RestaurantDetail.setData(imageUri); //Intent로 찍은 사진의 uri값을 넘겨줌

            startActivity(RestaurantDetail);
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
}