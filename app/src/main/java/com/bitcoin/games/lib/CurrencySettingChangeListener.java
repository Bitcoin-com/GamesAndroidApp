package com.bitcoin.games.lib;

import android.database.Observable;

import com.bitcoin.games.settings.CurrencySetting;

public interface CurrencySettingChangeListener {

  void update(Observable<CurrencySettingChangeListener> o, CurrencySetting currencySetting);
}
