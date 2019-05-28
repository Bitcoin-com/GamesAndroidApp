package com.bitcoin.services;

import android.content.Context;

import com.bitcoin.games.BuildConfig;
import com.bitcoin.games.settings.CurrencySettings;
import com.leanplum.Leanplum;

import java.math.BigDecimal;

public class LeanplumService {

  private static class LeanplumConfig {
    private static final String APP_ID = "app_ibqskCRxEOE3E4VOk7pZsQb4zLWrPihDEE47SAqiXe8";
    private static final String PROD_KEY = "prod_vYZJVkwLddJgPaQ8oZdHoOsnagg5aUZv78ZZyRIy2wQ";
    private static final String DEV_KEY = "dev_lyNuas0nGKO1b0d2mmkXZoWxat3r6FBi4WjzoQu3SYw";
  }

  private Context ctx;

  private static LeanplumService instance;

  public static LeanplumService getInstance(final Context ctx) {
    if (instance == null) {
      synchronized (LeanplumService.class) {
        if (instance == null) {
          instance = new LeanplumService();
        }
      }
    }
    instance.ctx = ctx;
    return instance;
  }

  private LeanplumService() {
  }

  public void initialize() {
    if (BuildConfig.DEBUG) {
      Leanplum.setAppIdForDevelopmentMode(LeanplumConfig.APP_ID, LeanplumConfig.DEV_KEY);
    } else {
      Leanplum.setAppIdForProductionMode(LeanplumConfig.APP_ID, LeanplumConfig.PROD_KEY);
    }
    Leanplum.trackAllAppScreens();
    Leanplum.start(ctx);
  }

  public void pushEventNewDepositPopup(final BigDecimal value) {
    pushEventDepositPopup("new_deposit", value);
  }

  public void pushEventPendingDepositPopup(final BigDecimal value) {
    pushEventDepositPopup("pending_deposit", value);
  }

  private void pushEventDepositPopup(final String eventName, final BigDecimal value) {
    final String currency = CurrencySettings.getInstance(ctx).getCurrency().name().toLowerCase();
    Leanplum.track(String.format("%s_%s", eventName, currency), value.doubleValue());
  }
}
