package com.scleroid.nemai.network;

import com.scleroid.nemai.data.models.Courier;
import com.scleroid.nemai.data.models.Parcel;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 7/23/18
 */
public interface ApiService {

	@FormUrlEncoded
	@POST("/alreadyexisteduser")
	Single<Boolean> isAUser(@Field("email_id") String userName);


	Completable subMitCourier(@Body Courier courier);

	//TODO Need to test this call first

	Completable subMitCouriers(@Body List<Courier> couriers);


	@POST("/insert")
	Flowable<List<Courier>> getCouriers(@Body Parcel header);

	@FormUrlEncoded
	@POST("/register")
	Maybe<JSONObject> registerUser(@Field("fname") String first_name, @Field("lname") String last_name,
								   @Field("email") String email, @Field("phone") String phone,
								   @Field("gender") String gender, @Field("password") String password,
								   @Field("login_method") String loginMethod);
	@FormUrlEncoded
	@POST("/login")
	Single<JSONObject> loginUser(@Field("email_id") String email, @Field("password") String password);



}
