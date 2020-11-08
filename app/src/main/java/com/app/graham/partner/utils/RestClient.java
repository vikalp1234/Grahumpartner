package com.app.graham.partner.utils;

import com.app.graham.partner.pozo.EditprofileResponceModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;

public class RestClient {
/*
    public static void registereditprofile(RequestBody referenceNo,
                                   RequestBody deviceImei,
                                   RequestBody kycType,
                                   RequestBody mobileNo,
                                   RequestBody documentType,
                                   RequestBody documentId,
                                   RequestBody pinCode,
                                   RequestBody district,
                                   RequestBody poastate,
                                   RequestBody city,
                                   RequestBody addressLine1,
                                   RequestBody addressLine2,
                                   RequestBody serviceType,

                                   MultipartBody.Part POAFront,
                                   MultipartBody.Part POABack,
                                   Callback<EditprofileResponceModel> callback) {
        RetrofitClient.getClient().registereditprofile(referenceNo, deviceImei, kycType, mobileNo, documentType, documentId, pinCode, district,poastate, city, addressLine1, addressLine2, serviceType, typeMobileWeb, POAFront, POABack).enqueue(callback);
        }*/

    public static void registereditprofile(
            RequestBody providerId,
             RequestBody name,
             RequestBody owner_name,
             RequestBody gender,
             RequestBody email,
             RequestBody address,
             RequestBody city,
             RequestBody state,
             RequestBody website,
             MultipartBody.Part editprofile,
             Callback<EditprofileResponceModel> callback) {
     RetrofitClient.getClient().registereditprofile(providerId,name,owner_name,gender,email,address,city,state,website,editprofile).enqueue(callback);
    }
}
