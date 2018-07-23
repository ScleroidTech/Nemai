package com.scleroid.nemai.network;

import com.scleroid.nemai.models.Courier;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 7/23/18
 */
public interface ApiService {

	@FormUrlEncoded
	@POST("/alreadyexisteduser")
	Single<Boolean> isAUser(@Query("email_id") String userName);

	@FormUrlEncoded
	Completable subMitCourier(@Body Courier courier);

	//TODO Need to test this call first
	@FormUrlEncoded
	Completable subMitCouriers(@Body List<Courier> couriers);
}
