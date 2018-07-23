package com.scleroid.nemai.network;

import io.reactivex.Single;
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
}
