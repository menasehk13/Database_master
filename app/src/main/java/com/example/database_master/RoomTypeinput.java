package com.example.database_master;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class RoomTypeinput extends AppCompatActivity {
    private static final int REQUEST_PERMISSION=99;
    private static int PICK_IMAGE=12;
    ImageView roompic;
    Uri picpath;
    EditText roomtypename, roomsize;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Button upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_typeinput);
        roompic=findViewById(R.id.room_image_input);
        roomtypename=findViewById(R.id.room_type_input);
        roomsize=findViewById(R.id.room_size_input);
        upload=findViewById(R.id.room_info_upload);
        roompic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Room Picture"),PICK_IMAGE);
            }
        });
    }
    private void Uploadroominfo(){
        if(picpath!=null){
            final StorageReference reference=storage.getReference().child("ROOM_IMAGE_FILES"+System.currentTimeMillis()+" "+getFileExtension(picpath));
            reference.putFile(picpath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(RoomTypeinput.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                    Task<Uri> task=taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageurl,roomN;
                            int size;
                            roomN=roomtypename.getText().toString();
                            imageurl=uri.toString();
                            DisplayRoom displayRoom=new DisplayRoom(roomN,imageurl,size);
                            DatabaseReference reference1=database.getReference().getParent()

                        }
                    })
                }
            })
        }
    }
    public String getFileExtension(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE && resultCode== RESULT_OK && data!=null && data.getData()!=null){
            picpath=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),picpath);
                picpath.setImageBitmap(bitmap);
                Picasso.get().load(picpath).fit().into(roompic);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}