package com.example.database_master;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.provider.SyncStateContract;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private static final int REQUEST_PERMISSION = 99;
    private static int PICK_IMAGE = 1;
    public static final int PI1=2;
    public static final int PI2=3;
    public static final int PI3=4;
    public static final int PI4=5;
    public static final int PI5=6;
    ImageView hotelimage,ri1,ri2,ri3,ri4,ri5;
    Uri hotelpath,rp1,rp2,rp3,rp4,rp5;
    EditText hotelname,rn1,rn2,rn3,rn4,rn5,rs1,rs2,rs3,rs4,rs5;
    EditText price1, price2, price3, price4, price5;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Button upload;
    RatingBar ratingBar;
    GoogleMap map;
    LatLng hotelLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        if (mapFragment != null) { mapFragment.getMapAsync(this); }
        ri1=findViewById(R.id.roomimaget1);
        ri2=findViewById(R.id.roomimaget2);
        ri3=findViewById(R.id.roomimaget3);
        ri4=findViewById(R.id.roomimaget4);
        ri5=findViewById(R.id.roomimaget5);
        hotelimage = findViewById(R.id.hotelimage_upload);
        rn1=findViewById(R.id.rtn1);
        rn2=findViewById(R.id.rtn2);
        rn3=findViewById(R.id.rtn3);
        rn4=findViewById(R.id.rtn4);
        rn5=findViewById(R.id.rtn5);
        hotelname = findViewById(R.id.hotelnameinput);
        rs1=findViewById(R.id.rts1);
        rs2=findViewById(R.id.rts2);
        rs3=findViewById(R.id.rts3);
        rs4=findViewById(R.id.rts4);
        rs5=findViewById(R.id.rts5);
        upload = findViewById(R.id.upload_info);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        ratingBar = findViewById(R.id.rating);
        hotelimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Hotel Picture "), PICK_IMAGE);
            }
        });
        ri1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select roomtype picture"),PI1);
            }
        });
        ri2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select roomtype picture"),PI2);
            }
        });
        ri3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select roomtype picture"),PI3);
            }
        });
        ri4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select roomtype picture"),PI4);
            }
        });
        ri5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select roomtype picture"),PI5);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadHotel();
            }
        });
    }

    private void UploadHotel() {
        if (hotelpath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            final StorageReference ref = storage.getReference().child("HOTEL_IMAGE_FILES" + System.currentTimeMillis() + "." + getFileExtension(hotelpath));
            ref.putFile(hotelpath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(MainActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                    hotelimage.setImageResource(R.drawable.image_background);
                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progressDialog.dismiss();
                            String imageurl, hotelname1;
                            float count;
                            count = ratingBar.getRating();
                            hotelname1 = hotelname.getText().toString();
                            imageurl = uri.toString();
                            DisplayHotel displayHotel = new DisplayHotel(hotelname1, imageurl, count);
                            DatabaseReference reference = database.getReference().child("Hotels");
                            reference.child("HotelDetails").push().child(hotelname1).setValue(displayHotel);
                            DatabaseReference reference1 =reference.child("HotelDetails").child(hotelname1).child("RoomTypes");

                            if (rp1 != null) {
                                final StorageReference ref1 = storage.getReference().child("Room_Image_Files" + System.currentTimeMillis() + "." + getFileExtension(rp1));
                                ref1.putFile(rp1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Toast.makeText(MainActivity.this, "Image1 Uploaded", Toast.LENGTH_SHORT).show();
                                        ri1.setImageResource(R.drawable.image_background);
                                        Task<Uri> task1 = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                        task1.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String rtype, rturl;
                                                int num;int pr;
                                                rtype = rn1.getText().toString();
                                                num=Integer.parseInt(rs1.getText().toString());
                                                pr=Integer.parseInt(price1.getText().toString());
                                                rturl = uri.toString();
                                                DisplayRoom displayRoom = new DisplayRoom(rtype, rturl, num, pr);
                                                  reference1.push().child(rtype).setValue(displayRoom);
                                            }
                                        });
                                    }
                                });
                            }

                            if (rp2!=null){
                            final StorageReference ref2=storage.getReference().child("Room_Image_Files"+System.currentTimeMillis()+"."+getFileExtension(rp2));
                            ref2.putFile(rp2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(MainActivity.this, "Image2 Uploaded", Toast.LENGTH_SHORT).show();
                                    ri2.setImageResource(R.drawable.image_background);
                                    Task<Uri> task1=taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                    task1.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String rtype, rturl;
                                            int num;int pr;
                                            rtype=rn2.getText().toString();
                                            pr=Integer.parseInt(price2.getText().toString());
                                            num=Integer.parseInt(rs2.getText().toString());
                                            rturl=uri.toString();
                                            DisplayRoom displayRoom=new DisplayRoom(rtype,rturl,num,pr);
                                            reference1.push().child(rtype).setValue(displayRoom);
                                        }
                                    });
                                }
                            });
                            }

                            if (rp3!=null){
                                final StorageReference ref3=storage.getReference().child("Room_Image_Files"+System.currentTimeMillis()+"."+getFileExtension(rp3));
                                ref3.putFile(rp3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Toast.makeText(MainActivity.this, "Image3 Uploaded", Toast.LENGTH_SHORT).show();
                                        ri3.setImageResource(R.drawable.image_background);
                                        Task<Uri> task1=taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                        task1.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String rtype, rturl;
                                                int num;int pr;
                                                rtype=rn3.getText().toString();
                                                pr=Integer.parseInt(price3.getText().toString());
                                                num=Integer.parseInt(rs3.getText().toString());
                                                rturl=uri.toString();
                                                DisplayRoom displayRoom=new DisplayRoom(rtype,rturl,num,pr);
                                                reference1.push().child(rtype).setValue(displayRoom);
                                            }
                                        });
                                    }
                                });
                            }

                            if (rp4!=null){
                                final StorageReference ref4=storage.getReference().child("Room_Image_Files"+System.currentTimeMillis()+"."+getFileExtension(rp4));
                                ref4.putFile(rp4).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Toast.makeText(MainActivity.this, "Image4 Uploaded", Toast.LENGTH_SHORT).show();
                                        ri4.setImageResource(R.drawable.image_background);
                                        Task<Uri> task1=taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                        task1.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String rtype, rturl;
                                                int num ;int pr;
                                                rtype=rn4.getText().toString();
                                                pr=Integer.parseInt(price4.getText().toString());
                                                num=Integer.parseInt(rs4.getText().toString());
                                                rturl=uri.toString();
                                                DisplayRoom displayRoom=new DisplayRoom(rtype,rturl,num,pr);
                                                reference1.push().child(rtype).setValue(displayRoom);
                                            }
                                        });
                                    }
                                });
                            }

                            if (rp5!=null){
                                final StorageReference ref5=storage.getReference().child("Room_Image_Files"+System.currentTimeMillis()+"."+getFileExtension(rp5));
                                ref5.putFile(rp5).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Toast.makeText(MainActivity.this, "Image5 Uploaded", Toast.LENGTH_SHORT).show();
                                        ri5.setImageResource(R.drawable.image_background);
                                        Task<Uri> task1=taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                        task1.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String rtype, rturl;
                                                int num;int pr;
                                                rtype=rn5.getText().toString();
                                                pr=Integer.parseInt(price5.getText().toString());
                                                num=Integer.parseInt(rs5.getText().toString());
                                                rturl=uri.toString();
                                                DisplayRoom displayRoom=new DisplayRoom(rtype,rturl,num,pr);
                                                reference1.push().child(rtype).setValue(displayRoom);
                                            }
                                        });
                                    }
                                });
                            }

                            GeoFire geoFire = new GeoFire(reference.child("HotelLocation").push().child(hotelname1));
                            geoFire.setLocation(hotelname1, new GeoLocation(hotelLocation.latitude, hotelLocation.longitude));
                            MainActivity.this.hotelname.getText().clear();
                            rn1.getText().clear();
                            rn2.getText().clear();
                            rn3.getText().clear();
                            rn4.getText().clear();
                            rn5.getText().clear();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                }
            });
        } else {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        }

    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            hotelpath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), hotelpath);
                hotelimage.setImageBitmap(bitmap);
                Picasso.get().load(hotelpath).fit().into(hotelimage);
            } catch (IOException e) { e.printStackTrace(); } }
        if (requestCode == PI1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            rp1=data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), rp1);
                ri1.setImageBitmap(bitmap);
                Picasso.get().load(rp1).fit().into(ri1);
            } catch (IOException e) { e.printStackTrace(); } }
        if (requestCode == PI2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            rp2=data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), rp2);
                ri2.setImageBitmap(bitmap);
                Picasso.get().load(rp2).fit().into(ri2);
            } catch (IOException e) { e.printStackTrace(); } }
        if (requestCode == PI3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            rp3 = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), rp3);
                ri3.setImageBitmap(bitmap);
                Picasso.get().load(rp3).fit().into(ri3);
            } catch (IOException e) { e.printStackTrace(); } }
        if (requestCode == PI4 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            rp4 = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), rp4);
                ri4.setImageBitmap(bitmap);
                Picasso.get().load(rp4).fit().into(ri4);
            } catch (IOException e) { e.printStackTrace(); } }
        if (requestCode == PI5 && resultCode == RESULT_OK && data != null && data.getData() != null) {
           rp5 = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), rp5);
                ri5.setImageBitmap(bitmap);
                Picasso.get().load(rp5).fit().into(ri5);
            } catch (IOException e) { e.printStackTrace(); } }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String provider = manager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            CheckPermission();return; }
        Location location = manager.getLastKnownLocation(provider);
        if (location!=null){ onLocationChanged(location); }
        map.setMyLocationEnabled(true);
        manager.requestLocationUpdates(provider,10000,10,this);
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                hotelLocation=latLng;
                map.addMarker(new MarkerOptions().position(hotelLocation));
                Log.d("Position",String.valueOf(hotelLocation.latitude+","+hotelLocation.longitude));
            }}); }
    public boolean CheckPermission(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_PERMISSION);
            return false; }return true; }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==REQUEST_PERMISSION && grantResults[0]==PackageManager.PERMISSION_GRANTED &&  grantResults.length>1){
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show(); } }
    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16)); }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }
    @Override
    public void onProviderEnabled(String provider) { }
    @Override
    public void onProviderDisabled(String provider) { }
}