package com.bitcoin.games.leanplum;

import android.content.Context;

import com.bitcoin.games.BuildConfig;
import com.bitcoin.games.settings.CurrencySettings;
import com.leanplum.Leanplum;

import java.math.BigDecimal;

public class LeanplumService {

  private Context ctx;
  private LeanplumConfig leanplumConfig;

  private static LeanplumService instance;

  public static LeanplumService getInstance(final Context ctx) {
    if (instance == null) {
      synchronized (LeanplumService.class) {
        if (instance == null) {
          instance = new LeanplumService(ctx);
        }
      }
    }
    instance.ctx = ctx;
    return instance;
  }

  private LeanplumService(final Context ctx) {
    leanplumConfig = new LeanplumConfig(ctx);
  }

  public void initialize() {
    if (BuildConfig.DEBUG) {
      Leanplum.setAppIdForDevelopmentMode(leanplumConfig.getAppId(), leanplumConfig.getDevKey());
    } else {
      Leanplum.setAppIdForProductionMode(leanplumConfig.getAppId(), leanplumConfig.getProdKey());
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