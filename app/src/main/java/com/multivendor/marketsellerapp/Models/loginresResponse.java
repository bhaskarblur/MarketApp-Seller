package com.multivendor.marketsellerapp.Models;

import com.google.gson.annotations.SerializedName;

public class loginresResponse {

    public class sendotp {
        @SerializedName("success")
        private String successs;

        @SerializedName("result")
        private String stat;

        public String getmessage() {
            return message;
        }

        public void setmessage(String message) {
            this.message = message;
        }

        @SerializedName("message")
        private String message;

        public String getsuccesss() {
            return successs;
        }

        public void setsuccesss(String successs) {
            this.successs = successs;
        }

        public String getstat() {
            return stat;
        }

        public void setstat(String stat) {
            this.stat = stat;
        }
    }

    public class register {

        @SerializedName("message")
        private String message;

        public String getmessage() {
            return message;
        }

        public void setmessage(String message) {
            this.message = message;
        }

        @SerializedName("success")
        private String successs;

        @SerializedName("result")
        private String registration;

        public String getsuccesss() {
            return successs;
        }

        public void setsuccesss(String successs) {
            this.successs = successs;
        }

        public String getRegistration() {
            return registration;
        }

        public void setRegistration(String registration) {
            this.registration = registration;
        }
    }

    public class login {

        @SerializedName("success")
        private String successs;

        public loginResult getResult() {
            return result;
        }

        public void setResult(loginResult result) {
            this.result = result;
        }

        @SerializedName("result")
        public loginResult result;

        public String getmessage() {
            return message;
        }

        public void setmessage(String message) {
            this.message = message;
        }

        private String message;
        public String getsuccesss() {
            return successs;
        }

        public void setsuccesss(String successs) {
            this.successs = successs;
        }



}
    public class forgotpass {

        @SerializedName("success")
        private String successs;

        @SerializedName("result")
        private String otpstat;

        @SerializedName("message")
        private String message;

        public String getmessage() {
            return message;
        }

        public void setmessage(String message) {
            this.message = message;
        }

        public String getsuccesss() {
            return successs;
        }

        public void setsuccesss(String successs) {
            this.successs = successs;
        }

        public String getOtpstat() {
            return otpstat;
        }

        public void setOtpstat(String otpstat) {
            this.otpstat = otpstat;
        }
    }

    public class verifyforgpass {
        @SerializedName("success")
        private String successs;

        @SerializedName("result")
        private String passchange;

        @SerializedName("message")
        private String message;

        public String getsuccesss() {
            return successs;
        }

        public void setsuccesss(String successs) {
            this.successs = successs;
        }

        public String getPasschange() {
            return passchange;
        }

        public void setPasschange(String passchange) {
            this.passchange = passchange;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public class loginResult {

        public int id;
        public String name;
        public String phone;
        public String user_type;
        public String otp;
        public String about;

        public String getAbout() {
            return about;
        }

        public void setAbout(String about) {
            this.about = about;
        }

        public String store_name;
        public String address;
        public String city;
        public String state;
        public String category;
        public String image;
        public String email;
        public String email_verified_at;
        public String created_at;
        public String updated_at;

        public String delivery_code;

        public String getDelivery_code() {
            return delivery_code;
        }

        public void setDelivery_code(String delivery_code) {
            this.delivery_code = delivery_code;
        }
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public String getStore_name() {
            return store_name;
        }

        public void setStore_name(String store_name) {
            this.store_name = store_name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEmail_verified_at() {
            return email_verified_at;
        }

        public void setEmail_verified_at(String email_verified_at) {
            this.email_verified_at = email_verified_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }

    public class shopregister {

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @SerializedName("success")
        private String success;

        @SerializedName("result")
        private String result;

        @SerializedName("message")
        private String message;
    }

    public class shopsetup {

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public shopsetupResult getResult() {
            return result;
        }

        public void setResult(shopsetupResult result) {
            this.result = result;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @SerializedName("success")
        private String success;

        @SerializedName("result")
        private shopsetupResult result;

        @SerializedName("message")
        private String message;
    }

    public class shopsetupResult {
        public String id;
        public String user_id;
        public String delivery_redius;
        public String lat;
        public String longit;
        public String min_order_amount;
        public String free_delivery_above;
        public String delivery_charge;
        public Object pan_number;
        public Object gst_number;
        public String upi_id;
        public String status;
        public String created_at;
        public String updated_at;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getDelivery_redius() {
            return delivery_redius;
        }

        public void setDelivery_redius(String delivery_redius) {
            this.delivery_redius = delivery_redius;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLongit() {
            return longit;
        }

        public void setLongit(String longit) {
            this.longit = longit;
        }

        public String getMin_order_amount() {
            return min_order_amount;
        }

        public void setMin_order_amount(String min_order_amount) {
            this.min_order_amount = min_order_amount;
        }

        public String getFree_delivery_above() {
            return free_delivery_above;
        }

        public void setFree_delivery_above(String free_delivery_above) {
            this.free_delivery_above = free_delivery_above;
        }

        public String getDelivery_charge() {
            return delivery_charge;
        }

        public void setDelivery_charge(String delivery_charge) {
            this.delivery_charge = delivery_charge;
        }

        public Object getPan_number() {
            return pan_number;
        }

        public void setPan_number(Object pan_number) {
            this.pan_number = pan_number;
        }

        public Object getGst_number() {
            return gst_number;
        }

        public void setGst_number(Object gst_number) {
            this.gst_number = gst_number;
        }

        public String getUpi_id() {
            return upi_id;
        }

        public void setUpi_id(String upi_id) {
            this.upi_id = upi_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }
    }
