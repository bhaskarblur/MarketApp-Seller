package com.multivendor.marketsellerapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.multivendor.marketsellerapp.APIWork.LogregApiInterface;
import com.multivendor.marketsellerapp.CustomDialogs.forg1_dialog;
import com.multivendor.marketsellerapp.CustomDialogs.forg2_dialog;
import com.multivendor.marketsellerapp.Models.loginresResponse;
import com.multivendor.marketsellerapp.databinding.ActivityLoginactBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Loginact extends AppCompatActivity {

    ActivityLoginactBinding lgbinding;
    private SharedPreferences setregist;
    private String token;
    private final api_baseurl baseurl=new api_baseurl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lgbinding = ActivityLoginactBinding.inflate(getLayoutInflater());
        setContentView(lgbinding.getRoot());


        this.getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        SharedPreferences getregist = getSharedPreferences("userlogged", 0);
        setregist = getSharedPreferences("userlogged", 0);
        String logcheck = getregist.getString("userlogged", "");

        if (logcheck.equals("yes")) {
            startActivity(new Intent(Loginact.this, act_shopdet.class));
            finish();
        }

        getfirebaseToken();
        lgbinding.passinvisimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show pass and change icon
                lgbinding.passinvisimg.setVisibility(View.INVISIBLE);
                lgbinding.passvisimg.setVisibility(View.VISIBLE);
                lgbinding.passtxt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                lgbinding.passtxt.setSelection(lgbinding.passtxt.getText().length());
            }
        });


        lgbinding.passvisimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show pass and change icon
                lgbinding.passinvisimg.setVisibility(View.VISIBLE);
                lgbinding.passvisimg.setVisibility(View.INVISIBLE);
                lgbinding.passtxt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                lgbinding.passtxt.setSelection(lgbinding.passtxt.getText().length());
            }
        });

         lgbinding.loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.baseurl)
                        .addConverterFactory(GsonConverterFactory.create()).build();

                LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);
                Call<loginresResponse.login> call = logregApiInterface.login(lgbinding.phonetxt.getText().toString(), lgbinding.passtxt.getText().toString(),
                        "seller",token);

             //   startActivity(new Intent(Loginact.this,Mainarea.class));
                if (lgbinding.phonetxt.getText().toString().isEmpty()) {

                    lgbinding.phonetxt.setError("Enter Phone Number.");
                    Toast.makeText(Loginact.this, "Please Enter Your Phone Number.", Toast.LENGTH_SHORT).show();
                } else if (lgbinding.phonetxt.getText().toString().length() < 10) {
                    lgbinding.phonetxt.setError("Invalid Number.");
                    Toast.makeText(Loginact.this, "Invalid Mobile Number.", Toast.LENGTH_SHORT).show();
                } else if (lgbinding.passtxt.getText().toString().isEmpty()) {
                    lgbinding.passtxt.setError("Enter Password.");
                    Toast.makeText(Loginact.this, "Please Enter Your Password.", Toast.LENGTH_SHORT).show();
                } else {
                    lgbinding.progressBar.setVisibility(View.VISIBLE);
                    lgbinding.loginbtn.setVisibility(View.INVISIBLE);
                    call.enqueue(new Callback<loginresResponse.login>() {
                        @Override
                        public void onResponse(Call<loginresResponse.login> call, Response<loginresResponse.login> response) {
                            if (!response.isSuccessful()) {
                                Log.d("Error code", String.valueOf(response.code()));
                                return;
                            }

                            loginresResponse.login login = response.body();
                            Log.d("loginresp", String.valueOf(login.getResult()));
                            Log.d("message", login.getmessage().toString());

                            if (login.getmessage().equals("Login Successfully.!")) {
                                Toast.makeText(Loginact.this, "Logged In!", Toast.LENGTH_SHORT).show();
                                if (login.getResult() != null) {
                                    lgbinding.progressBar.setVisibility(View.GONE);
                                    lgbinding.loginbtn.setVisibility(View.VISIBLE);
                                    SharedPreferences.Editor editor = setregist.edit();
                                    editor.putString("userid", String.valueOf(login.getResult().getId()));
                                    editor.putString("username", login.getResult().getName());
                                    editor.putString("userphone", login.getResult().getPhone());
                                    editor.putString("usertype", login.getResult().getUser_type());

                                    if(login.getResult().getCity()!=null) {
                                        editor.putString("city",login.getResult().getCity());
                                    }
                                    if(login.getResult().getState()!=null) {
                                        editor.putString("state",login.getResult().getState());
                                    }
                                    if(login.getResult().getCategory()!=null) {
                                        editor.putString("categories",login.getResult().getCategory());
                                    }
                                    if(login.getResult().getStore_name()!=null) {
                                        editor.putString("storename",login.getResult().getStore_name());
                                    }
                                    if (login.getResult().getAddress() != null) {
                                        editor.putString("useraddress", login.getResult().getAddress());
                                    }
                                    if (login.getResult().getImage() != null) {
                                        editor.putString("userimage", login.getResult().getImage());
                                    }
                                    if(login.getResult().getDelivery_code()!=null) {
                                        editor.putString("deliverycode", login.getResult().getDelivery_code());
                                        //     pfbinding.shopaddress.setText(resp.getResult().getAddress());
                                        editor.apply();
                                    }
                                    editor.putString("userlogged", "yes");
                                    editor.commit();
                                    startActivity(new Intent(Loginact.this, act_shopdet.class));
                                    finish();
                                }

                            } else if (login.getmessage().toString().contains("not registered")) {
                                lgbinding.progressBar.setVisibility(View.GONE);
                                lgbinding.loginbtn.setVisibility(View.VISIBLE);
                                Toast.makeText(Loginact.this, "No Such User Exists.", Toast.LENGTH_SHORT).show();
                            } else if (login.getmessage().toString().equals("Invalid phone or password, Please try again.!")) {
                                Toast.makeText(Loginact.this, "Incorrect Number Or Password", Toast.LENGTH_SHORT).show();
                                lgbinding.progressBar.setVisibility(View.GONE);
                                lgbinding.loginbtn.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<loginresResponse.login> call, Throwable t) {
                            Toast.makeText(Loginact.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            lgbinding.progressBar.setVisibility(View.GONE);
                            lgbinding.loginbtn.setVisibility(View.VISIBLE);
                        }
                    });

                }
            }
        });

        lgbinding.signingo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Loginact.this, com.multivendor.marketsellerapp.Signinact.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                finish();
            }
        });

        lgbinding.fgpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.multivendor.marketsellerapp.CustomDialogs.forg1_dialog forg1_dialog = new forg1_dialog();
                forg1_dialog.show(getSupportFragmentManager(), "forg1_dialog");
            }
        });

    }

    private void getfirebaseToken() {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();

                        // Log and toast
                        String msg = token;
                        Log.d("token", msg);
                    }
                });
    }
    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
}