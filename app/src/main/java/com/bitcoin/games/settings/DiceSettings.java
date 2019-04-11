package com.bitcoin.games.settings;

import com.bitcoin.games.lib.Bitcoin;

public class DiceSettings {

  private static DiceSettings instance;

  public static DiceSettings getInstance() {
    if (instance == null) {
      synchronized (DiceSettings.class) {
        if (instance == null) {
          instance = new DiceSettings();
        }
      }
    }
    return instance;
  }

  private DiceSettings() {
  }

  public long getCreditValue() {
    return Bitcoin.stringAmountToLong(
      CurrencySettings.getInstance().getCurrency() == Currency.BCH ? "0.001" : "0.0001");
  }
}
