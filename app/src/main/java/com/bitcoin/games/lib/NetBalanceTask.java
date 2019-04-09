package com.bitcoin.games.lib;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Observable;

import com.bitcoin.games.settings.CurrencySetting;

import java.io.IOException;

public class NetBalanceTask extends NetAsyncTask<Long, Void, JSONBalanceResult> implements CurrencySettingChangeListener {

  private CurrencySetting mCurrencySetting;

  public NetBalanceTask(Activity a) {
    super(a);
    mBVC.registerObserver(this);
  }

  public JSONBalanceResult go(Long... v) throws IOException {
    return mBVC.getBalance();
  }

  public void onUserConfirmNewBalance() {
    // Deposit activity uses this to pop the user back to the main screen
  }

  public void onSuccess(JSONBalanceResult result) {
    mBVC.mIntBalance = result.intbalance;
    mBVC.mFakeIntBalance = result.fake_intbalance;
    mBVC.mUnconfirmed = result.unconfirmed;

    if (result.notify_transaction != null) {
      new AlertDialog.Builder(mActivity)
        .setMessage(String.format("Received %s %s\n\nTransaction ID: %s", result.notify_transaction.amount, mCurrencySetting.getCurrency(), result.notify_transaction.txid))
        .setTitle("New Deposit")
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
            onUserConfirmNewBalance();
          }
        })
        .create()
        .show();
    }
  }

  @Override
  public void update(Observable<CurrencySettingChangeListener> o, CurrencySetting currencySetting) {
    mCurrencySetting = currencySetting;
  }
}
