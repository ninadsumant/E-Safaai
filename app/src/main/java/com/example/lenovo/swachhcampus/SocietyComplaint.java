package com.example.lenovo.swachhcampus;

import android.Manifest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.provider.Settings;

import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.List;
import java.util.Locale;


public class SocietyComplaint extends AppCompatActivity {
    Button capture,submit;
    EditText lat;
    ImageView image;
    Bitmap bp;
    String description;
    String email;
    String message;
    String subject;
    Uri pictureUri;
    File output=null;
    DatabaseReference rootRef,demoRef,idRef;



    private static final int Image_Capture_code=1;

    private static final int REQUEST_CODE=1000;
    LocationManager locationManager;
    Context context;
    boolean GpsStatus;
    String fulladdress;
    RelativeLayout layout;
    public static final int CAMERA_REQUEST=10;

    Double latitude,longitude;

    Geocoder geocoder;
    List<Address> addresses;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case REQUEST_CODE:{
                if(grantResults.length>0){
                    if(grantResults[0]== PackageManager.PERMISSION_GRANTED){

                    }else if(grantResults[0]==PackageManager.PERMISSION_DENIED){

                    }
                }
            }
        }
    }



    FirebaseStorage storage=FirebaseStorage.getInstance();
    StorageReference storageReference=storage.getReferenceFromUrl("gs://swachhcampus-d5bde.appspot.com/Society");


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_society_complaint);
        layout = (RelativeLayout)findViewById(R.id.layout);
        capture=(Button)findViewById(R.id.capture);
        lat=(EditText) findViewById(R.id.location);
        submit=(Button)findViewById(R.id.submit);
        layout.setBackgroundColor(Color.BLACK);

        rootRef=FirebaseDatabase.getInstance().getReference();
        demoRef=rootRef.child("Demo");
        idRef=demoRef.child("id");


        geocoder=new Geocoder(this, Locale.getDefault());
        CheckGpsStatus();


        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        }else{
            buildLocationRequest();
            BuildLocationCallBack();

            fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
            if(ActivityCompat.checkSelfPermission(SocietyComplaint.this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SocietyComplaint.this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
                return;
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());

        image=(ImageView)findViewById(R.id.image);


        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id=demoRef.push().getKey();
                String value=lat.getText().toString().trim();
                String Lati="http://maps.google.com/maps?saddr="+latitude+","+longitude;
                ArrayList<String> list=new ArrayList<String>();
                list.add(value);
                list.add(Lati);
                demoRef.child(id).setValue(list);


                if(pictureUri!=null)
                {
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String timestamp=sdf.format(new Date());
                    StorageReference childRef=storageReference.child("issue"+timestamp+".jpg");
                    UploadTask uploadTask=childRef.putFile(pictureUri);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(SocietyComplaint.this, "upload successful", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SocietyComplaint.this, "upload failed", Toast.LENGTH_SHORT).show();
                        }
                    });

                }



                try{
                    email = "kavimitraisalso@gmail.com";
                    message = "This mail is sent under Society Complaint section from Swachh Application";
                    subject = "Location ";
                    String msg= "http://maps.google.com/maps?saddr="+latitude+","+longitude;
                    description=lat.getText().toString();
                    Intent intent = new Intent(Intent.ACTION_SEND);

                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
                    intent.putExtra(Intent.EXTRA_SUBJECT, fulladdress);
                    intent.putExtra(Intent.EXTRA_TEXT, message+"\n"+"Description About Issue \n"+description+"\n\n"+Uri.parse(msg));
                    intent.putExtra(Intent.EXTRA_STREAM,pictureUri);
                    intent.setType("image/jpeg");

                    startActivity(Intent.createChooser(intent, "Select Email Sending App :"));

                } catch (Throwable t) {
                    Toast.makeText(SocietyComplaint.this,
                            "Request failed try again: " + t.toString(),
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    private void openCamera() {
        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File pictureDirectory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureName=getPictureName();
        File imageFile=new File(pictureDirectory,pictureName);
        pictureUri= Uri.fromFile(imageFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,pictureUri);
        startActivityForResult(cameraIntent,CAMERA_REQUEST);
    }
    private String getPictureName() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp=sdf.format(new Date());
        return "plant"+timestamp+".jpg";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if(resultCode==RESULT_OK){
            Bitmap myBitmap = BitmapFactory.decodeFile(pictureUri.getPath());
            image.setImageBitmap(myBitmap);



        }else if(resultCode==RESULT_CANCELED){
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        }

    }
    private void BuildLocationCallBack() {

        locationCallback=new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for(Location location:locationResult.getLocations()){
                    latitude=location.getLatitude();
                    longitude=location.getLongitude();

                    try {
                        addresses=geocoder.getFromLocation(latitude,longitude,1);

                        String address=addresses.get(0).getAddressLine(0);
                        String area=addresses.get(0).getLocality();
                        String city=addresses.get(0).getAdminArea();
                        String country=addresses.get(0).getCountryName();
                        String postalcode=addresses.get(0).getPostalCode();

                        fulladdress=address+","+area+","+city+","+country+","+postalcode;

                        //lat.setText(fulladdress);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }


                // textview.setText(String.valueOf(location.getLatitude())+"/"+String.valueOf(location.getLongitude()));

            }
        };
    }

    @SuppressLint("RestrictedApi")
    private void buildLocationRequest(){
        locationRequest=new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);
    }

    private void CheckGpsStatus(){
        locationManager=(LocationManager)SocietyComplaint.this.getSystemService(Context.LOCATION_SERVICE);
        GpsStatus=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(GpsStatus==true)
        {
            Toast.makeText(this, "GPS Enable", Toast.LENGTH_SHORT).show();
        }else if(GpsStatus==false)
        {
            Intent intent1=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent1);
        }
    }
    }

