package com.multivendor.marketsellerapp.APIWork;



import com.multivendor.marketsellerapp.Models.newProductModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiWork {



    @FormUrlEncoded
    @POST("seller_products")
    Call<newProductModel.homeprodResp> getallproducts(@Field("user_id") String userid, @Field("latitude") String lat,
                                                      @Field("longitude") String longit, @Field("city")
                                                       String cityname);
    @FormUrlEncoded
    @POST("category_products")
    Call<newProductModel .homeprodResp> getcategoryproducts(@Field("category_name") String catname,@Field("latitude") String lat,
                                                       @Field("longitude") String longit,@Field("city_name")
                                                                    String cityname);

    @FormUrlEncoded
    @POST("seller_product_detail")
    Call<newProductModel.productdetailResp> getproduct_details(@Field("user_id") String userid,
                                                               @Field("product_id") String productid);


    @FormUrlEncoded
    @POST("apply_coupon")
    Call<newProductModel.couponResp> applycoupon(@Field("user_id") String userid,
                                                 @Field("coupon_code") String coupon,
                                                 @Field("cart_id") String cartid);

    @FormUrlEncoded
    @POST("update-product")
    Call<newProductModel.productdetailResp> update_product(@Field("user_id") String userid,
                                                           @Field("product_id") String prodid,
                                                               @Field("city") String city,
                                                               @Field("variation_id") String ids,
                                                               @Field("variants") String variants,
                                                               @Field("price") String mrp,
                                                               @Field("selling_price") String price,
                                                               @Field("normal_product") String type1,
                                                           @Field("best_deal_product") String type2,
                                                           @Field("top_selling_product") String type3,
                                                           @Field("deal_day_product") String type4);


}

