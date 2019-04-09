package com.bitcoin.games.settings;

import android.util.Pair;

import com.bitcoin.games.lib.BitcoinGames;

import java.util.HashMap;
import java.util.Map;

public class CurrencySettingFactory {

  private static Map<Pair<Integer, String>, CurrencySetting> cacheMap = new HashMap<>();

  static {
    final CurrencySetting testBchSetting = new CurrencySetting(
      "cashgames.btctest.net", "BCH", "cashgames@btctest.net");
    final CurrencySetting prodBchSetting = new CurrencySetting(
      "cashgames.bitcoin.com", "BCH", "cashgames@bitcoin.com");
    final CurrencySetting testBtcSetting = new CurrencySetting(
      "games.btctest.net", "BTC", "games@btctest.net");
    final CurrencySetting prodBtcSetting = new CurrencySetting(
      "games.bitcoin.com", "BTC", "games@bitcoin.com");
    cacheMap.put(Pair.create(BitcoinGames.RunEnvironment.EMULATOR, "BCH"), testBchSetting);
    cacheMap.put(Pair.create(BitcoinGames.RunEnvironment.LOCAL, "BCH"), testBchSetting);
    cacheMap.put(Pair.create(BitcoinGames.RunEnvironment.PRODUCTION, "BCH"), prodBchSetting);
    cacheMap.put(Pair.create(BitcoinGames.RunEnvironment.EMULATOR, "BTC"), testBtcSetting);
    cacheMap.put(Pair.create(BitcoinGames.RunEnvironment.LOCAL, "BTC"), testBtcSetting);
    cacheMap.put(Pair.create(BitcoinGames.RunEnvironment.PRODUCTION, "BTC"), prodBtcSetting);
  }

  public static CurrencySetting create(final String currency) {
    return cacheMap.get(Pair.create(BitcoinGames.RUN_ENVIRONMENT, currency.toUpperCase()));
  }
}
