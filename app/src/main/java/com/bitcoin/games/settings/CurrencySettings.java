package com.bitcoin.games.settings;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Observable;
import android.preference.PreferenceManager;
import android.util.Pair;

import com.bitcoin.games.lib.BitcoinGames;
import com.bitcoin.games.lib.CommonActivity;
import com.bitcoin.games.rest.RestBitcoinClient;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.bitcoin.games.lib.BitcoinGames.RunEnvironment.*;

public class CurrencySettings extends Observable<Observer<Currency>> {

  private final static String ACCOUNT_KEY_SUFFIX = "_account_key";
  private final static String CURRENT_CURRENCY = "current_currency";

  private Context ctx;
  private Currency currentCurrency;

  private static Map<Pair<Integer, Currency>, CurrencySetting> cacheMap = new HashMap<>();

  static {
    final CurrencySetting testBchSetting = new CurrencySetting(
      "cashgames.btctest.net", Currency.BCH, "cashgames@btctest.net");
    final CurrencySetting prodBchSetting = new CurrencySetting
      ("cashgames.bitcoin.com", Currency.BCH, "cashgames@bitcoin.com");
    final CurrencySetting testBtcSetting = new CurrencySetting
      ("games.btctest.net", Currency.BTC, "games@btctest.net");
    final CurrencySetting prodBtcSetting = new CurrencySetting
      ("games.bitcoin.com", Currency.BTC, "games@bitcoin.com");
    cacheMap.put(Pair.create(EMULATOR, Currency.BCH), testBchSetting);
    cacheMap.put(Pair.create(LOCAL, Currency.BCH), testBchSetting);
    cacheMap.put(Pair.create(PRODUCTION, Currency.BCH), prodBchSetting);
    cacheMap.put(Pair.create(EMULATOR, Currency.BTC), testBtcSetting);
    cacheMap.put(Pair.create(LOCAL, Currency.BTC), testBtcSetting);
    cacheMap.put(Pair.create(PRODUCTION, Currency.BTC), prodBtcSetting);
  }

  CurrencySettings(final Context ctx) {
    this.ctx = ctx;
    final String currentCurrency = PreferenceManager.getDefaultSharedPreferences(ctx).getString(CURRENT_CURRENCY, null);
    if (currentCurrency == null) {
      setCurrentCurrency(Currency.BCH, true);
    } else {
      setCurrentCurrency(Currency.valueOf(currentCurrency), false);
    }
  }

  private void setCurrentCurrency(final Currency currency, final boolean saveToPreferences) {
    if (saveToPreferences) {
      final SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
      editor.putString(CURRENT_CURRENCY, currency.name());
      editor.apply();
    }
    this.currentCurrency = currency;
    this.mObservers.forEach(o -> o.onChanged(currency));
  }

  private static CurrencySettings instance;

  public static CurrencySettings getInstance(final Context ctx) {
    if (instance == null) {
      synchronized (CurrencySettings.class) {
        if (instance == null) {
          instance = new CurrencySettings(ctx);
        }
      }
    }
    instance.ctx = ctx;

    return instance;
  }

  public void reload(final String currency) {
    setCurrentCurrency(Currency.valueOf(currency), true);
  }

  @Override
  public void registerObserver(Observer<Currency> observer) {
    super.registerObserver(observer);
    observer.onChanged(currentCurrency);
  }

  public void setAccountKey(final String accountKey) {
    final SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
    edit.putString(accountKey(), accountKey);
    edit.apply();
  }

  public String getAccountKey() {
    return PreferenceManager.getDefaultSharedPreferences(ctx).getString(accountKey(), null);
  }

  private String accountKey() {
    return currentCurrency.name().toLowerCase() + ACCOUNT_KEY_SUFFIX;
  }

  public String getServerName() {
    return getCurrencySetting().serverName;
  }

  public String getServerAddress() {
    return getCurrencySetting().getServerAddress();
  }

  public Currency getCurrency() {
    return getCurrencySetting().currency;
  }

  public String getAdminEmail() {
    return getCurrencySetting().adminEmail;
  }

  public void retrieveAddress(final CommonActivity activity, Consumer<BitcoinAddress> onSuccessCallback) {
    final NetBitcoinAddressTask task = new NetBitcoinAddressTask(activity, result -> {
      switch (getCurrency()) {
        case BCH:
          RestBitcoinClient.getInstance().retrieveAddress(
            result.address,
            json -> onSuccessCallback.accept(new BitcoinAddress(getCurrency().getPrefix(), json.getCashAddress())));
          break;
        case BTC:
          onSuccessCallback.accept(new BitcoinAddress(getCurrency().getPrefix(), result.address));
          break;
      }
    });
    task.executeParallel();
  }

  private CurrencySetting getCurrencySetting() {
    return cacheMap.get(Pair.create(BitcoinGames.RUN_ENVIRONMENT, currentCurrency));
  }

  private static class CurrencySetting {

    private String serverName;
    private Currency currency;
    private String adminEmail;

    private CurrencySetting(String serverName, Currency currency, String adminEmail) {
      this.serverName = serverName;
      this.currency = currency;
      this.adminEmail = adminEmail;
    }

    private String getServerAddress() {
      return String.format("https://%s", serverName);
    }
  }
}
