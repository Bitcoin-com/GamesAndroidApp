package com.bitcoin.games.tasks;

import android.app.Activity;

import com.bitcoin.games.lib.JSONRateResponse;
import com.bitcoin.games.lib.NetAsyncTask;
import com.bitcoin.games.rest.RateRestClient;

import java.io.IOException;
import java.util.function.Consumer;

public class RateTask extends NetAsyncTask<Long, Void, JSONRateResponse> {

  private final Consumer<Double> successCallback;

  public RateTask(final Activity a, final Consumer<Double> successCallback) {
    super(a);
    this.successCallback = successCallback == null ? ignored -> {} : successCallback;
  }

  @Override
  public JSONRateResponse go(Long... v) throws IOException {
    return RateRestClient.getInstance(mContext).getRate();
  }

  @Override
  public void onSuccess(final JSONRateResponse response) {
    if (response.err == null) {
      successCallback.accept(response.rate);
    }
  }
}
