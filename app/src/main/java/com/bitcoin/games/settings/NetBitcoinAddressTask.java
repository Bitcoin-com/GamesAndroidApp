package com.bitcoin.games.settings;

import android.app.ProgressDialog;
import android.util.Log;

import com.bitcoin.games.R;
import com.bitcoin.games.app.GameActivity;
import com.bitcoin.games.lib.CommonActivity;
import com.bitcoin.games.lib.JSONBitcoinAddressResult;
import com.bitcoin.games.lib.NetAsyncTask;
import com.bitcoin.games.rest.AccountRestClient;

import java.io.IOException;
import java.util.function.Consumer;

class NetBitcoinAddressTask extends NetAsyncTask<Long, Void, JSONBitcoinAddressResult> {

  private static final String TAG = NetBitcoinAddressTask.class.getSimpleName();

  private ProgressDialog mAlert;
  private Consumer<JSONBitcoinAddressResult> onSuccessCallback;

  NetBitcoinAddressTask(final CommonActivity activity, final Consumer<JSONBitcoinAddressResult> onSuccessCallback) {
    super(activity);
    Log.v(TAG, "NetBitcoinAddressTask go!");
    mAlert = ProgressDialog.show(activity, "", activity.getString(R.string.deposit_message_retrieving_dep_address), true);
    this.onSuccessCallback = onSuccessCallback;
  }

  @Override
  public void onDone() {
    mAlert.cancel();
  }

  @Override
  public JSONBitcoinAddressResult go(Long... v) throws IOException {
    Log.v(TAG, "deposit check go!");
    return AccountRestClient.getInstance().getBitcoinAddress();
  }

  @Override
  public void onSuccess(JSONBitcoinAddressResult result) {
    Log.v(TAG, "deposit check success!");
    onSuccessCallback.accept(result);
  }

  @Override
  public void onError(JSONBitcoinAddressResult r) {
    mShowDialogOnError = false;
    GameActivity.handleCriticalConnectionError(mActivity);
  }
}