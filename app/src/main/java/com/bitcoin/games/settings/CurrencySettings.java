package com.bitcoin.games.settings;

import android.util.Pair;

import com.bitcoin.games.lib.BitcoinGames;
import com.bitcoin.games.lib.CommonActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CurrencySettings {

  private static final String TAG = CurrencySettings.class.getSimpleName();
  private Map<Currency, String> accountKeyMap = new HashMap<>();
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
    cacheMap.put(Pair.create(BitcoinGames.RunEnvironment.EMULATOR, Currency.BCH), testBchSetting);
    cacheMap.put(Pair.create(BitcoinGames.RunEnvironment.LOCAL, Currency.BCH), testBchSetting);
    cacheMap.put(Pair.create(BitcoinGames.RunEnvironment.PRODUCTION, Currency.BCH), prodBchSetting);
    cacheMap.put(Pair.create(BitcoinGames.RunEnvironment.EMULATOR, Currency.BTC), testBtcSetting);
    cacheMap.put(Pair.create(BitcoinGames.RunEnvironment.LOCAL, Currency.BTC), testBtcSetting);
    cacheMap.put(Pair.create(BitcoinGames.RunEnvironment.PRODUCTION, Currency.BTC), prodBtcSetting);
  }

  CurrencySettings() {
    currentCurrency = Currency.BCH;
  }

  private static CurrencySettings instance;

  public static CurrencySettings getInstance() {
    if (instance == null) {
      synchronized (CurrencySettings.class) {
        if (instance == null) {
          instance = new CurrencySettings();
        }
      }
    }

    return instance;
  }

  public void reload(final String currency) {
    this.currentCurrency = Currency.valueOf(currency);
  }

  public void setAccountKey(final String accountKey) {
    this.accountKeyMap.put(currentCurrency, accountKey);
  }

  public String getAccountKey() {
    return accountKeyMap.get(currentCurrency);
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
    final NetBitcoinAddressTask task = new NetBitcoinAddressTask(activity, result ->
      onSuccessCallback.accept(new BitcoinAddress(getCurrency().getPrefix(), result.address)));
    task.executeParallel(Long.valueOf(0));
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
