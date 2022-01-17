package com.multivendor.marketsellerapp.APIWork;


import android.net.Uri;

import com.multivendor.marketsellerapp.Models.billModel;
import com.multivendor.marketsellerapp.Models.cartModel;
import com.multivendor.marketsellerapp.Models.loginresResponse;
import com.multivendor.marketsellerapp.Models.notiModel;
import com.multivendor.marketsellerapp.Models.quickorderModel;
import com.multivendor.marketsellerapp.Models.sellerApiResp;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LogregApiInterface {


    @FormUrlEncoded
    @POST("send-otp")
    Call<loginresResponse.sendotp> sendOTP(@Field("phone") String phone);


    @FormUrlEncoded
    @POST("validate-otp")
    Call<loginresResponse.register> signin(@Field("name") String name,
                                           @Field("phone") String phone, @Field("otp") String OTP
            , @Field("password") String password, @Field("user_type") String usetype);

    @FormUrlEncoded
    @POST("login")
    Call<loginresResponse.login> login(@Field("phone") String phone, @Field("password") String password,
                                       @Field("user_type")String user_type,@Field("device_token") String device_token);


    @FormUrlEncoded
    @POST("forgot-password-sendotp")
    Call<loginresResponse.forgotpass> doforgotpass(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("save-forgot-password")
    Call<loginresResponse.verifyforgpass> verifyforgpass(@Field("phone") String phone,
                                                         @Field("otp") String OTP,
                                                         @Field("new_password") String password);


    @FormUrlEncoded
    @POST("seller-register")
    Call<loginresResponse.shopregister> shopregister(@Field("user_id") String userid,
                                                     @Field("store_name") String shopname,
                                                     @Field("city") String city,
                                                     @Field("state") String state,
                                                     @Field("category") String categories);

    @FormUrlEncoded
    @POST("setup-store")
    Call<loginresResponse.shopsetup> shopsetup(@Field("user_id") String userid,
                                               @Field("delivery_redius") String delradius,
                                               @Field("min_order_amount") String minord,
                                               @Field("free_delivery_above") String freedelabv,
                                               @Field("delivery_charge") String delvchar,
                                               @Field("upi_id") String upiid,
                                               @Field("pan_number")String panno,
                                               @Field("gst_number")String gstno);

    @FormUrlEncoded
    @POST("seller-change-password")
    Call<loginresResponse.verifyforgpass> changepass(@Field("user_id") String userid, @Field("password") String password);


    @FormUrlEncoded
    @POST("seller-update-profile")
    Call<loginresResponse.login> updateprofile
            (@Field("user_id") String userid, @Field("name") String name,@Field("store_name") String storename,
             @Field("address")String address, @Field("about") String about,@Field("lat")
                     String lat, @Field("long")String longit,@Field("image") String img);


    @FormUrlEncoded
    @POST("seller-update-profile")
    Call<loginresResponse.login> noimgupdateprofile(@Field("user_id") String userid, @Field("name") String name,@Field("store_name") String storename,
             @Field("address")String address, @Field("about") String about,@Field("lat")
                     String lat, @Field("long")String longit);

    @FormUrlEncoded
    @POST("add-category")
    Call<sellerApiResp.commonresult> addcategory(@Field("user_id")String userid,@Field("category_name") String name,
                                                    @Field("image") String image);


    @FormUrlEncoded
    @POST("get-category")
    Call<sellerApiResp.get_category> get_category (@Field("user_id") String userid);

    @FormUrlEncoded
    @POST("my-store")
    Call<sellerApiResp.sellerinfo> get_seller_info(@Field("user_id")String userid);

    @FormUrlEncoded
    @POST("add-product")
    Call<sellerApiResp.addproductinfo> addproduct(@Field("user_id")String userid,@Field("category_id")
                                                  String catid,@Field("image")String img,@Field("product_name")
                                                  String prodname,@Field("price") String price,@Field("selling_price") String selpr,
                                                  @Field("size")String size,@Field("qty") String qty );

    @FormUrlEncoded
    @POST("my-products")
    Call<sellerApiResp.getproduct_info> getallproducts(@Field("user_id") String userid);

    @FormUrlEncoded
    @POST("my-products")
    Call<sellerApiResp.getproduct_info> getqueproducts(@Field("user_id") String userid,
                                                       @Field("category_id") String categoryid);
    @FormUrlEncoded
    @POST("delete-product")
    Call<sellerApiResp.commonresult> deleteproduct(@Field("user_id") String userid,
                                                   @Field("product_id") String product_id);
    @FormUrlEncoded
    @POST("update-product")
    Call<sellerApiResp.addproductinfo> updateproduct(@Field("user_id")String userid,@Field("product_id")
            String prodid,@Field("variation_id")String varid,@Field("image")String img,@Field("product_name")
            String prodname,@Field("price") String price,@Field("selling_price") String selpr,
                                                  @Field("size")String size,@Field("qty") String qty );

    @FormUrlEncoded
    @POST("update-stock-status")
    Call<sellerApiResp.addproductinfo> updatestock(@Field("user_id") String userid,
                                                   @Field("product_id") String product_id,
                                                 @Field("stock_status")String stockstat);


    @GET("all-state")
    Call<sellerApiResp.getstate> getstate();

    @FormUrlEncoded
    @POST("get-city")
    Call<sellerApiResp.getcity> getcity(@Field("state_id") String stateid);

    @FormUrlEncoded
    @POST("delete-category")
    Call<sellerApiResp.commonresult> delete_category(@Field("category_id")String catid);

    @FormUrlEncoded
    @POST("seller-get-orders")
    Call<cartModel.multcartResp> getorders(@Field("store_id") String storeid,
                                       @Field("order_type") String order_type);
    @FormUrlEncoded
    @POST("seller-find-single-order")
    Call<cartModel.singlecartResp> getsingleorder(@Field("store_id") String storeid,
                                       @Field("order_id") String order_id);

    @FormUrlEncoded
    @POST("seller-update-order")
    Call<cartModel.multcartResp> update_orderstatus(@Field("store_id") String storeid,@Field("order_id")
                                                String orderid,@Field("order_status") String orderstatus);


    @FormUrlEncoded
    @POST("get-rating-reviews")
    Call<sellerApiResp.reviewresp> getreviews(@Field("store_id") String userid);

    @FormUrlEncoded
    @POST("get-total-orders")
    Call<sellerApiResp.statsresp> getstatsinfo(@Field("store_id") String userid);

    @FormUrlEncoded
    @POST("seller-get-newquick-orders")
    Call<quickorderModel .quickordResp> get_quickorders(@Field("store_id") String storeid);

    @FormUrlEncoded
    @POST("seller-final-getall-quick-orders")
    Call<quickorderModel .quickordResp> get_nextquickorders(@Field("store_id") String storeid);

    @FormUrlEncoded
    @POST("user-online-offline-status")
    Call<sellerApiResp.statusUpdateResp> getStore_Status(@Field("user_id") String userid);

    @FormUrlEncoded
    @POST("user-online-offline-status")
    Call<sellerApiResp.statusUpdateResp> updatetStore_Status(@Field("user_id") String userid,
                                                             @Field("status") String status);


    @FormUrlEncoded
    @POST("seller-get-single-quick-order")
    Call<quickorderModel.singlequickordResp> get_singleQuick(@Field("store_id") String storeid,
                                                         @Field("quick_id") String quickid);

    @FormUrlEncoded
    @POST("seller-add-quick-order")
    Call<quickorderModel.singlequickordResp> update_quickorder(@Field("store_id") String storeid,
                                                               @Field("quick_id") String quickid,
                                                               @Field("seller_status") String status,
                                                               @Field("product_name") String prodname,
                                                               @Field("qty") String qty,
                                                               @Field("price") String price,
                                                               @Field("subtotal")String subtotal,
                                                               @Field("shipping_charge") String delvcharge,
                                                               @Field("total_price") String totalprice,
                                                               @Field("expected_delivery")String expdelivery);


    @FormUrlEncoded
    @POST("seller-final-update-quick-order-status")
    Call<quickorderModel.singlequickordResp> finalupdate_quickorder(@Field("store_id") String storeid,
                                                               @Field("quick_id") String quickid,
                                                               @Field("status") String status);


    @FormUrlEncoded
    @POST("seller-get-invoice")
    Call<billModel .billListResp> getbill(@Field("store_id") String storeid,@Field("status") String status);

    @FormUrlEncoded
    @POST("seller-billpay")
    Call<billModel.singlebillResp> paybill(@Field("store_id") String storeid,
                                           @Field("bill_id") String billid,
                                           @Field("total_amount") String totalamount,
                                           @Field("payment_status") String status);

    @FormUrlEncoded
    @POST("seller-notification-list")
    Call<notiModel .notiResp> getAllnotis(@Field("store_id") String userid,@Field("noti_type") String notitype);

    @FormUrlEncoded
    @POST("update-seller-notification-list")
    Call<notiModel.notiupdateResp> updatenotis(@Field("store_id") String userid, @Field("noti_id") String notiid,
                                               @Field("noti_type") String notitype);
}