
package com.example.Activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Beans.Student;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetProfileImageFragment extends Fragment {

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_iMAGE = 2;

    private Uri mImageCaptureUri;
    private ImageView iv_UserPhoto;
    private TextView select_Pic;
    private Button set_Clear;

    private String get_Profile_Image;
    private String update_Profile_Image;

    String absolutePath;
    String fileName;


    public SetProfileImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_set_profile_image, container, false);

        Student mStudent = (Student)getArguments().getSerializable("myInfo");

        iv_UserPhoto = (ImageView) view.findViewById(R.id.profile_ImageView);

        //Bitmap bm = BitmapFactory.decodeFile(absolutePath);
        //iv_UserPhoto.setImageBitmap(bm);

        select_Pic = (TextView) view.findViewById(R.id.btn_Select_Pic);
        set_Clear = (Button) view.findViewById(R.id.btn_Set_Clear);

        select_Pic.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                onClickProcessSelectPic(view);
            }
        });

        set_Clear.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                onClickProcessSetClear(view);
            }
        });

        return view;
    }
    public void onClickProcessSelectPic(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("업로드할 이미지 선택");
        builder.setCancelable(false);
        builder.setPositiveButton("사진 촬영", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                doTakePhotoAction();
                Toast.makeText(getActivity(), "사진 촬영", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "취소 버튼이 눌렸습니다.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("앨범 선택", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog2, int which) {
                doTakeAlbumAction();
                Toast.makeText(getActivity(), "앨범 선택", Toast.LENGTH_SHORT).show();

            }
        });
        builder.show();
    }

    public void doTakePhotoAction(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String url = "tmp_" + String.valueOf(System.currentTimeMillis())+".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    public void doTakeAlbumAction(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        Date date = new Date();
        String strDate = formatter.format(date);

        if(resultCode != RESULT_OK)
            return;

        switch(requestCode){
            case PICK_FROM_ALBUM:{
                mImageCaptureUri = data.getData();
                Log.d("ProfileDir", mImageCaptureUri.getPath().toString());
            }
            case PICK_FROM_CAMERA: {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_iMAGE);
                break;
            }
            case CROP_FROM_iMAGE: {
                if(resultCode != RESULT_OK){
                    return;
                }

                final Bundle extras = data.getExtras();

                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/"+strDate+".jpg";

                if(extras != null){
                    Bitmap photo = extras.getParcelable("data");
                    iv_UserPhoto.setImageBitmap(photo);
                    storeCropImage(photo,filePath);
                    absolutePath = filePath;
                    break;
                }

                File f = new File(mImageCaptureUri.getPath());
                if(f.exists()){
                    f.delete();
                }
            }
        }
    }

    private void storeCropImage(Bitmap bitmap, String filePath){
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ProfileDir";
        File directory_ProfileDir = new File(dirPath);

        if(!directory_ProfileDir.exists())
            directory_ProfileDir.mkdir();

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try{
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);

            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));

            out.flush();
            out.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void onClickProcessSetClear(View view){
        Toast.makeText(getActivity(), "프로필 사진이 업데이트 되었습니다.", Toast.LENGTH_LONG).show();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(SetProfileImageFragment.this).commit();
        fragmentManager.popBackStack();
    }
}