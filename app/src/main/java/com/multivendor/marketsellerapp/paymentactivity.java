package com.multivendor.marketsellerapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.multivendor.marketsellerapp.APIWork.LogregApiInterface;
import com.multivendor.marketsellerapp.Models.billModel;
import com.multivendor.marketsellerapp.databinding.ActivityPaymentactivityBinding;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class paymentactivity extends AppCompatActivity implements PaymentResultListener {

    private ActivityPaymentactivityBinding pmbinding;
    private String storeid;
    private String billid;
    private String amount;
    private String status;
    private String razserver_id="";
    private String razorord_id="";
    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;
    private final api_baseurl baseurl=new api_baseurl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pmbinding=ActivityPaymentactivityBinding.inflate(getLayoutInflater());
        setContentView(pmbinding.getRoot());
        this.getSupportActionBar().hide();
        Intent intent=getIntent();
        SharedPreferences sharedPreferences=getSharedPreferences("userlogged",0);
        storeid=sharedPreferences.getString("userid","");
        billid=intent.getStringExtra("bill_id");
        amount=intent.getStringExtra("amount");
        status=intent.getStringExtra("status");
        razserver_id=intent.getStringExtra("razor_id");
        razorord_id=intent.getStringExtra("billorder_id");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }
        viewfunctions();
        Checkout.preload(getApplicationContext());
    }

    public void startPayment() {

        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID(razserver_id);
        /**
         * Set your logo here
         */
        checkout.setImage(R.mipmap.app_logo_main);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Lmart Solutions Private Limited");
            options.put("description", "Pay bill");
            options.put("image", R.mipmap.app_logo_main);
            options.put("order_id", razorord_id); //from response of step 3.
            options.put("theme.color", "#0881E3");
            options.put("currency", "INR");
            options.put("amount", String.valueOf(Integer.parseInt(amount)*100));//pass amount in currency subunits
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e("error", "Error in starting Razorpay Checkout", e);
        }
    }


    private void viewfunctions() {
        pmbinding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            }
        });

        pmbinding.paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Uri uri = new Uri.Builder().scheme("upi").authority("pay")
//                        .appendQueryParameter("pa", "8299189690@okbizaxis")
//                        .appendQueryParameter("pn", "sanjay")
//                        .appendQueryParameter("am", amount.toString().replace("₹ ", ""))
//                        .appendQueryParameter("cu", "INR")
//                        .build();
//
//                Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
//                upiPayIntent.setData(uri);
//                Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
//
//                if (chooser.resolveActivity(getPackageManager()) != null) {
//                    try {
//                        startActivityForResult(upiPayIntent, GOOGLE_PAY_REQUEST_CODE);
//                    } catch (Exception e) {
//                        Toast.makeText(paymentactivity.this, "No UPI Apps Found", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(paymentactivity.this, "No UPI Apps Found!", Toast.LENGTH_SHORT).show();
//                }


                startPayment();
            }
        });
    }


    private void paybillAPI(String statusrec) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.baseurl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<billModel .singlebillResp> call = logregApiInterface.paybill( storeid, billid, amount, statusrec);

        call.enqueue(new Callback<billModel .singlebillResp>() {
            @Override
            public void onResponse(Call<billModel .singlebillResp> call, Response<billModel .singlebillResp> response) {
                if (!response.isSuccessful()) {
                    Log.d("errorcode", String.valueOf(response.code()));
                    return;
                }

                billModel .singlebillResp resp = response.body();
                Log.d("messagepayment", resp.getMessage());

                if (resp.getMessage().equals("Bill pay successfully.!")) {
                    startActivity(new Intent(paymentactivity.this, billpaidactivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                } else {
                    Toast.makeText(paymentactivity.this, "Error while paying bill!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<billModel .singlebillResp> call, Throwable t) {
                Log.d("failed", t.getMessage());
            }
        });

    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String status = new String();
        if (data != null) {
            if(data.getData()!=null) {
                status = data.getDataString().toLowerCase();
            }
            if (requestCode == RESULT_OK) {
                try {
                    if (status.equals("success")) {
                        String resper = data.getStringExtra("response");
                        Log.d("response", resper);
                        ArrayList<String> breakresp = new ArrayList<>();
                        breakresp.add(resper);
                        upiPaymentDataOperation(breakresp);
                    } else {
                        Toast.makeText(paymentactivity.this, "Payment Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    Toast.makeText(paymentactivity.this, "There Was An Error While Paying.", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void upiPaymentDataOperation(ArrayList<String> breakresp) {
        String str=breakresp.get(0);
        Log.d("description:",str);
        String paycancel="";
        String status="";
        String approvalrefno="";
        String resp[]=str.split("&");
        for(int i=0;i<resp.length;i++) {
            String equalstr[]=resp[i].split("=");
            if(equalstr.length>=2) {
                if (equalstr[0].toLowerCase().equals("Status".toLowerCase())) {
                    status = equalstr[1].toLowerCase();
                }

                else if (equalstr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) ||
                        equalstr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                    approvalrefno=equalstr[1];
                }
                else {
                    paycancel="Payment Cancelled By User.";
                }
            }

            if(status.equals("success")) {
                Log.d("success with RefNo:",approvalrefno);
                Toast.makeText(paymentactivity.this, "Please Wait!", Toast.LENGTH_SHORT).show();
                paybillAPI("success");
            }
        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }

    @Override
    public void onPaymentSuccess(String s) {
        paybillAPI("success");
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(paymentactivity.this, "Payment Failed", Toast.LENGTH_SHORT).show();
        Log.d("errorrazor",s.toString());
    }
}