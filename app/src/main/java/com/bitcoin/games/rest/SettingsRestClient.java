package com.bitcoin.games.rest;

import android.content.Context;

import com.bitcoin.games.lib.JSONAndroidAppVersionResult;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;

public class SettingsRestClient extends RestClient {

  private static SettingsRestClient instance;

  public static SettingsRestClient getInstance(final Context ctx) {
    if (instance == null) {
      synchronized (SettingsRestClient.class) {
        if (instance == null) {
          instance = new SettingsRestClient(ctx);
        }
      }
    }
    instance.ctx = ctx;
    return instance;
  }

  private SettingsRestClient(final Context ctx) {
    super(ctx);
  }

  public JSONAndroidAppVersionResult getAndroidAppVersion() throws IOException {
    InputStreamReader is = getInputStreamReader("android/app_version", "", null);
    return new Gson().fromJson(is, JSONAndroidAppVersionResult.class);
  }
}
