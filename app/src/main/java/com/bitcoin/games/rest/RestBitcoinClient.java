package com.bitcoin.games.rest;

import com.bitcoin.games.rest.entities.JsonBitcoinAddress;

import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class RestBitcoinClient {

  private final Retrofit retrofit;

  private RestBitcoinClient() {
    retrofit = new Retrofit.Builder()
      .baseUrl("https://rest.bitcoin.com/v2/")
      .addConverterFactory(GsonConverterFactory.create())
      .build();
  }

  private static RestBitcoinClient instance;

  public static RestBitcoinClient getInstance() {
    if (instance == null) {
      synchronized (RestBitcoinClient.class) {
        if (instance == null) {
          instance = new RestBitcoinClient();
        }
      }
    }
    return instance;
  }

  public void retrieveAddress(
      final String address, final Consumer<JsonBitcoinAddress> onSuccessCallback) {
    retrofit.create(RestBitcoinService.class).getBitcoinAddress(address).enqueue(new Callback<JsonBitcoinAddress>() {
      @Override
      public void onResponse(Call<JsonBitcoinAddress> call, Response<JsonBitcoinAddress> response) {
        onSuccessCallback.accept(response.body());
      }

      @Override
      public void onFailure(Call<JsonBitcoinAddress> call, Throwable t) {
      }
    });
  }

  private interface RestBitcoinService {
    @GET("address/details/{address}")
    Call<JsonBitcoinAddress> getBitcoinAddress(@Path("address") String address);
  }
}
