package com.multivendor.marketsellerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.multivendor.marketsellerapp.APIWork.LogregApiInterface;
import com.multivendor.marketsellerapp.CustomDialogs.change_passDialog;
import com.multivendor.marketsellerapp.Models.loginresResponse;
import com.multivendor.marketsellerapp.Models.sellerApiResp;
import com.multivendor.marketsellerapp.databinding.ActivityProfileSettingsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.picasso.transformations.MaskTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class profileSettings extends AppCompatActivity {

    private ActivityProfileSettingsBinding psbinding;
    final int IMAGE_PICK_CODE = 1000;
    final int PERMISSION_CODE = 1001;
    private int IMAGE_PERMISSION_CODE = 1002;
    private Uri imguri;
    String userid;
    private Boolean sameimg = false;
    String base64img = new String();
    private String lat;
    private String longit;
    private Boolean needimage = false;
    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String mLastLocation;
    SharedPreferences shpref;
    private final api_baseurl baseurl=new api_baseurl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        psbinding = ActivityProfileSettingsBinding.inflate(getLayoutInflater());
        setContentView(psbinding.getRoot());
        shpref = getSharedPreferences("userlogged", 0);
        userid = shpref.getString("userid", "");
        loadData(userid);
        viewfunctions();
        this.getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        psbinding.addresschange.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    private void getlatlong() {
        psbinding.addresschange.setText("Getting Location");

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(profileSettings.this);
            LocationRequest request = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10000).setFastestInterval(1000).setNumUpdates(1);
            fusedLocationProviderClient.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                }
            }, Looper.getMainLooper());
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    Location location = task.getResult();
                    if (location != null) {
                        lat = String.valueOf(location.getLatitude());
                        longit = String.valueOf(location.getLongitude());
                        Geocoder geocoder = new Geocoder(profileSettings.this
                                , Locale.getDefault());

                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                            psbinding.addresschange.setText(addresses.get(0).getAddressLine(0));
                            Animation anim0 = AnimationUtils.loadAnimation(profileSettings.this, R.anim.slide_in_down);
                            psbinding.locatcard.setAnimation(anim0);
                            psbinding.locatcard.setVisibility(View.VISIBLE);
                            loadmat(location.getLatitude(), location.getLongitude(), addresses.get(0).getAddressLine(0));
                            psbinding.addresschange.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    return false;
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {

                        LocationRequest request = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000).setFastestInterval(1000).setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                Location location1 = locationResult.getLastLocation();
                                lat = String.valueOf(location1.getLatitude());
                                longit = String.valueOf(location1.getLongitude());
                                Geocoder geocoder = new Geocoder(profileSettings.this
                                        , Locale.getDefault());

                                try {
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    psbinding.addresschange.setText(addresses.get(0).getAddressLine(0));
                                    Animation anim0 = AnimationUtils.loadAnimation(profileSettings.this, R.anim.slide_in_down);
                                    psbinding.locatcard.setAnimation(anim0);
                                    psbinding.locatcard.setVisibility(View.VISIBLE);
                                    loadmat(location.getLatitude(), location.getLongitude(), addresses.get(0).getAddressLine(0));
                                    psbinding.addresschange.setOnTouchListener(new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            return false;
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        };
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    private void loadData(String userid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.baseurl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        com.multivendor.marketsellerapp.APIWork.LogregApiInterface logregApiInterface = retrofit.create(
                com.multivendor.marketsellerapp.APIWork.LogregApiInterface.class);

        Call<sellerApiResp.sellerinfo> call = logregApiInterface.get_seller_info(userid);

        call.enqueue(new Callback<sellerApiResp.sellerinfo>() {
            @Override
            public void onResponse(Call<sellerApiResp.sellerinfo> call, Response<sellerApiResp.sellerinfo> response) {
                if (!response.isSuccessful()) {
                    Log.d("Error code", String.valueOf(response.code()));
                    return;
                }

                sellerApiResp.sellerinfo resp = response.body();
                if (resp.getMessage() != null) {
                    Log.d("message", resp.getMessage());
                }

                if (resp.getMessage().equals("My store")) {
                    if (resp.getResult().getName() != null) {
                        psbinding.unamechange.setText(resp.getResult().getName());
                        needimage = false;
                    }
                    if (resp.getResult().getStore_name() != null) {
                        psbinding.storename.setText(resp.getResult().getStore_name());
                    }
                    if (resp.getResult().getAddress() != null) {
                        psbinding.addresschange.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return false;
                            }
                        });
                        psbinding.picklocat.setVisibility(View.INVISIBLE);
                        psbinding.addresschange.setText(resp.getResult().getAddress());
                        psbinding.addresschange.setVisibility(View.VISIBLE);
                        psbinding.textView34.setVisibility(View.VISIBLE);
                        psbinding.updatebtn.setVisibility(View.VISIBLE);
                    }
                    if (resp.getResult().getAbout() != null) {
                        psbinding.aboutustxt.setText(resp.getResult().getAbout());
                    }

                    if(resp.getResult().getLat()!=null) {
                        lat=resp.getResult().getLat();
                    }
                    if(resp.getResult().getLongit()!=null) {
                        longit=resp.getResult().getLongit();
                    }
                    if (resp.getResult().getImage() != null) {
                        imguri = Uri.parse(resp.getResult().getImage());
                        sameimg = true;
                        base64img = resp.getResult().getImage();
                        final int radius = 30;
                        final int margin = 30;
                        final Transformation transformation = new RoundedCornersTransformation(radius, margin);
                        Picasso.get().load(imguri).transform(transformation).fit().into(psbinding.userimg);
                    }
                }
            }

            @Override
            public void onFailure(Call<sellerApiResp.sellerinfo> call, Throwable t) {
                Toast.makeText(profileSettings.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadmat(double sellat, double sellongit, String curlocat) {
        psbinding.currlocattxt.setText(curlocat);
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.google_map2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        final LatLng[] latLng = {new LatLng(sellat, sellongit)};
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng[0])
                                .title("Current Location").draggable(true);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng[0], 18));
                        googleMap.addMarker(markerOptions).setDraggable(true);

                        psbinding.recentrebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng[0], 18));
                            }
                        });
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                                    @Override
                                    public void onMarkerDragStart(@NonNull Marker marker) {

                                    }

                                    @Override
                                    public void onMarkerDrag(@NonNull Marker marker) {

                                    }

                                    @Override
                                    public void onMarkerDragEnd(@NonNull Marker marker) {
                                        latLng[0] =marker.getPosition();
                                        LatLng new_latlng = marker.getPosition();
                                        lat = String.valueOf(new_latlng.latitude);
                                        longit = String.valueOf(new_latlng.longitude);


                                        Geocoder geocoder = new Geocoder(profileSettings.this
                                                , Locale.getDefault());
                                        try {
                                            List<Address> addresses = geocoder.getFromLocation(new_latlng.latitude, new_latlng.longitude, 1);
                                            psbinding.currlocattxt.setText(addresses.get(0).getAddressLine(0));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }, 1000);

                    }
                });

            }
        }, 100);

        psbinding.savelocat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                psbinding.addresschange.setText(psbinding.currlocattxt.getText().toString());
                Animation anim0 = AnimationUtils.loadAnimation(profileSettings.this, R.anim.slide_out_down);
                psbinding.locatcard.setAnimation(anim0);
                psbinding.locatcard.setVisibility(View.INVISIBLE);
                psbinding.picklocat.setVisibility(View.INVISIBLE);
                psbinding.updatebtn.setVisibility(View.VISIBLE);
                psbinding.textView34.setVisibility(View.VISIBLE);
                psbinding.addresschange.setVisibility(View.VISIBLE);

            }
        });


    }

    private void viewfunctions() {



        psbinding.passchange.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        psbinding.picklocat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getlatlong();
            }
        });
        psbinding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(profileSettings.this, Mainarea.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                finish();
            }
        });

        psbinding.passchangetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.multivendor.marketsellerapp.CustomDialogs.change_passDialog change_passDialog = new change_passDialog();
                change_passDialog.show(getSupportFragmentManager(), "changepassword");

            }
        });

        psbinding.imgedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCropActivity();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                            == PackageManager.PERMISSION_DENIED) {
//                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
//                        requestPermissions(permissions, PERMISSION_CODE);
//
//                    } else {
//                        pickImageFromGallery();
//
//                    }
//                }
            }
        });





                psbinding.updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                psbinding.progressBar4.setVisibility(View.VISIBLE);
                psbinding.updatebtn.setVisibility(View.INVISIBLE);
                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.baseurl)
                        .addConverterFactory(GsonConverterFactory.create()).build();

                LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

                try {
                    if (imguri != null) {
                        if (sameimg.equals(false)) {
                            InputStream is = getContentResolver().openInputStream(imguri);
                            Bitmap image = BitmapFactory.decodeStream(is);
                            ByteArrayOutputStream by = new ByteArrayOutputStream();
                            image.compress(Bitmap.CompressFormat.JPEG, 50, by);
                            base64img = Base64.encodeToString(by.toByteArray(), Base64.DEFAULT);
                        }
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Call<loginresResponse.login> call;
                if (needimage.equals(true)) {
                    Log.d("newimg","yh");
                    call = logregApiInterface.updateprofile(userid, psbinding.unamechange.getText().toString(),
                            psbinding.storename.getText().toString(), psbinding.addresschange.getText().toString()
                            , psbinding.aboutustxt.getText().toString(),lat,longit,base64img);
                } else {
                    Log.d("newimg","no");
                    Log.d("lat","lat");
                    call = logregApiInterface.noimgupdateprofile(userid, psbinding.unamechange.getText().toString(),
                            psbinding.storename.getText().toString(), psbinding.addresschange.getText().toString()
                            , psbinding.aboutustxt.getText().toString(),lat,longit);
                }
                call.enqueue(new Callback<loginresResponse.login>() {
                    @Override
                    public void onResponse(Call<loginresResponse.login> call, Response<loginresResponse.login> response) {
                        if (!response.isSuccessful()) {

                            Log.d("Code", response.message().toString());
                            return;
                        }

                        loginresResponse.login resp = response.body();
                        if (resp.getmessage() != null) {
                            Log.d("message", resp.getmessage());
                        }
                        Log.d("message", resp.getmessage());
                        if (resp.getmessage().equals("Profile updated successfully.!")) {
                            psbinding.addresschange.setError(null);
                            psbinding.progressBar4.setVisibility(View.GONE);
                            psbinding.updatebtn.setVisibility(View.VISIBLE);
                            psbinding.picklocat.setVisibility(View.GONE);
                            Toast.makeText(profileSettings.this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                            Log.d("profup", "updated");
                            SharedPreferences.Editor editor = shpref.edit();
                            editor.putString("username", resp.getResult().getName());
                            editor.putString("storename", resp.getResult().getStore_name());
                            editor.putString("useraddress", resp.getResult().getAddress());
                            editor.putString("userimage", resp.getResult().getImage());

                            editor.commit();

                            startActivity(new Intent(profileSettings.this,Mainarea.class));


                        } else {
                            psbinding.progressBar4.setVisibility(View.GONE);
                            psbinding.updatebtn.setVisibility(View.VISIBLE);
                            Toast.makeText(profileSettings.this, "An error has occured!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<loginresResponse.login> call, Throwable t) {
                        Log.d("Failure",t.getMessage());
                        Toast.makeText(profileSettings.this,"Click Add Address",Toast.LENGTH_LONG).show();
                        psbinding.addresschange.setError("Add address");
                        psbinding.progressBar4.setVisibility(View.GONE);
                        psbinding.updatebtn.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

    }

    private void startCropActivity() {

        if(ActivityCompat.checkSelfPermission(profileSettings.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(profileSettings.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
            return;
        }

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imguri = result.getUri();
                sameimg = false;
                needimage = true;
                final int radius = 10;
                final int margin = 10;
                final Transformation transformation = new MaskTransformation(profileSettings.this, R.drawable.rounded_transf);
                Picasso.get().load(imguri).transform(transformation).fit().into(psbinding.userimg);
            }

        }
    }


    @Override
    public void onBackPressed() {
        if(psbinding.addresschange.getText().toString().isEmpty()){

         Toast.makeText(profileSettings.this,"Click Add Address",Toast.LENGTH_LONG).show();
         psbinding.addresschange.setError("Enter Address");

        }
        else {
            startActivity(new Intent(profileSettings.this,Mainarea.class));
            finish();
        }

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    
}