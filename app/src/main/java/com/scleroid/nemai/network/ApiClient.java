package com.scleroid.nemai.network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.scleroid.nemai.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 7/23/18
 */
public class ApiClient {
	private static Retrofit retrofit;
	private static int REQUEST_TIMEOUT = 60;
	private static OkHttpClient okHttpClient;

	private static ApiClient instance;
	private static ApiService service;
	private static Gson gson;
	private static LiveNetworkMonitor networkMonitor;

	public static synchronized ApiClient getInstance() {
		if (instance == null) {
			instance = new ApiClient();
		}
		return instance;
	}

	public static synchronized ApiService getService(Context context) {
		if (networkMonitor == null)
		networkMonitor = new LiveNetworkMonitor(context);

		if (service == null) {
			service = getClient().create(ApiService.class);
		}
		return service;
	}

	public static Retrofit getClient() {

		if (okHttpClient == null) { initOkHttp(); }

		if (retrofit == null) {
			retrofit = new Retrofit.Builder()
					.baseUrl(BuildConfig.API_BASE_URL)
					.client(okHttpClient)
					.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
					.addConverterFactory(GsonConverterFactory.create(getGson()))
					.build();
		}
		return retrofit;
	}

	public static Gson getGson() {
		if (gson == null) {
			gson = new GsonBuilder()
					//	.serializeNulls()
					.create();
		}
		return gson;
	}

	/*public ApiClient() {

		HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
		loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		OkHttpClient httpClient = new OkHttpClient.Builder()
				.addInterceptor(loggingInterceptor)
				.build();

		Gson gson = new GsonBuilder()
				.excludeFieldsWithoutExposeAnnotation()
				.create();

		retrofit = new Retrofit.Builder()
				.client(httpClient)
				.baseUrl(BuildConfig.API_BASE_URL)
				.addConverterFactory(GsonConverterFactory.create(gson))
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.build();

	}*/
	private static void initOkHttp() {
		OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
				.connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
				.readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
				.writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);

		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		httpClient.addInterceptor(interceptor);
		// LOOK HERE !!!! add network monitor interceptor:
		httpClient.addInterceptor(chain -> {
			if (networkMonitor.isConnected()) {
				return chain.proceed(chain.request());
			} else {
				throw new NoNetworkException();
			}
		});

		httpClient.addInterceptor(chain -> {
			Request original = chain.request();
			Request.Builder requestBuilder = original.newBuilder()
					.addHeader("Accept", "application/json")
					.addHeader("Content-Type", "application/json");

		/*	// Adding Authorization token (API Key)
			// Requests will be denied without API key
			if (!TextUtils.isEmpty(PrefUtils.getApiKey(context))) {
				requestBuilder.addHeader("Authorization", PrefUtils.getApiKey(context));
			}*/

			Request request = requestBuilder.build();
			return chain.proceed(request);
		});

		okHttpClient = httpClient.build();
	}


	/*	public void addLoan(Loan loan) throws IOException, RemoteException {
			//	RemotePostEndpoint service = retrofit.create(RemotePostEndpoint.class);

			// Remote call can be executed synchronously since the job calling it is already
			// backgrounded.
			Response<Loan> response = service.addLoan(loan).execute();

			if (response == null || !response.isSuccessful() || response.errorBody() != null) {
				throw new RemoteException(response);
			}

			Timber.d("successful remote response: " + response.body());
		}

		public void addTransactionModel(TransactionModel transaction) throws IOException,
				RemoteException {
			//	RemotePostEndpoint service = retrofit.create(RemotePostEndpoint.class);

			// Remote call can be executed synchronously since the job calling it is already
			// backgrounded.
			Response<TransactionModel> response = service.addTransaction(transaction).execute();

			if (response == null || !response.isSuccessful() || response.errorBody() != null) {
				throw new RemoteException(response);
			}

			Timber.d("successful remote response: " + response.body());
		}

		public void addInstallment(Installment installment) throws IOException, RemoteException {
			//	RemotePostEndpoint service = retrofit.create(RemotePostEndpoint.class);

			// Remote call can be executed synchronously since the job calling it is already
			// backgrounded.
			Response<Installment> response = service.addInstallment(installment).execute();

			if (response == null || !response.isSuccessful() || response.errorBody() != null) {
				throw new RemoteException(response);
			}

			Timber.d("successful remote response: " + response.body());
		}

		public void addExpense(Expense expense) throws IOException, RemoteException {
			//	RemotePostEndpoint service = retrofit.create(RemotePostEndpoint.class);

			// Remote call can be executed synchronously since the job calling it is already
			// backgrounded.
			Response<Expense> response = service.addExpense(expense).execute();

			if (response == null || !response.isSuccessful() || response.errorBody() != null) {
				throw new RemoteException(response);
			}

			Timber.d("successful remote response: " + response.body());
		}

		public void addCustomer(Customer customer) throws IOException, RemoteException {
			//	RemotePostEndpoint service = retrofit.create(RemotePostEndpoint.class);

			// Remote call can be executed synchronously since the job calling it is already
			// backgrounded.
			Response<Customer> response = service.addCustomer(customer).execute();

			if (response == null || !response.isSuccessful() || response.errorBody() != null) {
				throw new RemoteException(response);
			}

			Timber.d("successful remote response: " + response.body());
		}


	}
*/
}
