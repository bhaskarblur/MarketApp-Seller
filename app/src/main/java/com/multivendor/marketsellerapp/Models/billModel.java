package com.multivendor.marketsellerapp.Models;

import java.util.List;

public class billModel {

    public class singlebillResp {

        public String success;
        public billresult result;
        public String message;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public billresult getResult() {
            return result;
        }

        public void setResult(billresult result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
   public class billListResp {

       public String success;
       public List<billresult> result;
       public String message;

       public String getSuccess() {
           return success;
       }

       public void setSuccess(String success) {
           this.success = success;
       }

       public List<billresult> getResilt() {
           return result;
       }

       public void setResilt(List<billresult> resilt) {
           this.result = resilt;
       }

       public String getMessage() {
           return message;
       }

       public void setMessage(String message) {
           this.message = message;
       }
   }

    public class billresult {
        public String bill_id;
        public String store_id;
        public String invoice_no;
        public String invoice;
        public String status;
        public String service_charge_rate;
        public String service_charge;
        public String sgst_rate;
        public String cgst_rate;
        public String sgst_amount;
        public String cgst_amount;
        public String total_amount_words;
        public String total_amount;
        public String tax_amount;
        public String payment_status;
        public String bill_date;
        public String bill_from_date;
        public String bill_to_date;
        public String created_at;
        public String updated_at;
        public storedetials storedetials;
        public String subtotal;
        public String razororder_id;
        public String razorserver_id;

        public String getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(String subtotal) {
            this.subtotal = subtotal;
        }

        public String getRazororder_id() {
            return razororder_id;
        }

        public void setRazororder_id(String razororder_id) {
            this.razororder_id = razororder_id;
        }

        public String getRazorserver_id() {
            return razorserver_id;
        }

        public void setRazorserver_id(String razorserver_id) {
            this.razorserver_id = razorserver_id;
        }

        public String getSub_total() {
            return subtotal;
        }

        public void setSub_total(String subtotal) {
            this.subtotal = subtotal;
        }

        public String getBill_id() {
            return bill_id;
        }

        public void setBill_id(String bill_id) {
            this.bill_id = bill_id;
        }

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public String getInvoice_no() {
            return invoice_no;
        }

        public void setInvoice_no(String invoice_no) {
            this.invoice_no = invoice_no;
        }

        public String getInvoice() {
            return invoice;
        }

        public void setInvoice(String invoice) {
            this.invoice = invoice;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getService_charge_rate() {
            return service_charge_rate;
        }

        public void setService_charge_rate(String service_charge_rate) {
            this.service_charge_rate = service_charge_rate;
        }

        public String getService_charge() {
            return service_charge;
        }

        public void setService_charge(String service_charge) {
            this.service_charge = service_charge;
        }

        public String getSgst_rate() {
            return sgst_rate;
        }

        public void setSgst_rate(String sgst_rate) {
            this.sgst_rate = sgst_rate;
        }

        public String getCgst_rate() {
            return cgst_rate;
        }

        public void setCgst_rate(String cgst_rate) {
            this.cgst_rate = cgst_rate;
        }

        public String getSgst_amount() {
            return sgst_amount;
        }

        public void setSgst_amount(String sgst_amount) {
            this.sgst_amount = sgst_amount;
        }

        public String getCgst_amount() {
            return cgst_amount;
        }

        public void setCgst_amount(String cgst_amount) {
            this.cgst_amount = cgst_amount;
        }

        public String getTotal_amount_words() {
            return total_amount_words;
        }

        public void setTotal_amount_words(String total_amount_words) {
            this.total_amount_words = total_amount_words;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getTax_amount() {
            return tax_amount;
        }

        public void setTax_amount(String tax_amount) {
            this.tax_amount = tax_amount;
        }

        public String getPayment_status() {
            return payment_status;
        }

        public void setPayment_status(String payment_status) {
            this.payment_status = payment_status;
        }

        public String getBill_date() {
            return bill_date;
        }

        public void setBill_date(String bill_date) {
            this.bill_date = bill_date;
        }

        public String getBill_from_date() {
            return bill_from_date;
        }

        public void setBill_from_date(String bill_from_date) {
            this.bill_from_date = bill_from_date;
        }

        public String getBill_to_date() {
            return bill_to_date;
        }

        public void setBill_to_date(String bill_to_date) {
            this.bill_to_date = bill_to_date;
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

        public billModel.storedetials getStoredetials() {
            return storedetials;
        }

        public void setStoredetials(billModel.storedetials storedetials) {
            this.storedetials = storedetials;
        }
    }

    public class storedetials {
        public String id;
        public String name;
        public String phone;
        public String user_type;
        public String about;
        public String otp;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
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

        public String getAbout() {
            return about;
        }

        public void setAbout(String about) {
            this.about = about;
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
}
