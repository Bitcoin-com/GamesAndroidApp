package com.bitcoin.games.rest;

import android.content.Context;
import android.util.Log;

import com.bitcoin.games.lib.BitcoinGames;
import com.bitcoin.games.lib.JSONBalanceResult;
import com.bitcoin.games.lib.JSONBitcoinAddressResult;
import com.bitcoin.games.lib.JSONCreateAccountResult;
import com.bitcoin.games.lib.JSONWithdrawResult;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;

import static com.bitcoin.games.lib.BitcoinGames.RUN_ENVIRONMENT;

public class AccountRestClient extends RestClient {

  private static AccountRestClient instance;

  public static AccountRestClient getInstance(final Context context) {
    if (instance == null) {
      synchronized (AccountRestClient.class) {
        if (instance == null) {
          instance = new AccountRestClient(context);
        }
      }
    }
    return instance;
  }

  private AccountRestClient(final Context context) {
    super(context);
  }

  // By using our referral system on new accounts, we can see how many new android users were created.
  private final String NEW_ACCOUNT_REFERRAL_KEY_PRODUCTION = "4116305284";
  private final String NEW_ACCOUNT_REFERRAL_KEY_LOCAL = "3678545110";

  public JSONCreateAccountResult createAccount() throws IOException {
    String referralKey;
    if (RUN_ENVIRONMENT == BitcoinGames.RunEnvironment.PRODUCTION) {
      referralKey = NEW_ACCOUNT_REFERRAL_KEY_PRODUCTION;
    } else {
      referralKey = NEW_ACCOUNT_REFERRAL_KEY_LOCAL;
    }
    String params = encodeKeyValuePair("r", referralKey);
    InputStreamReader is = getInputStreamReader("account/new", params, null);
    return new Gson().fromJson(is, JSONCreateAccountResult.class);
  }

  private void printInputStreamReader(InputStreamReader is) {
    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
    String str = s.hasNext() ? s.next() : "";
    Log.v(tag(), str);
  }

  public JSONWithdrawResult getWithdraw(String address, long intAmount) throws IOException {
    String params = encodeKeyValuePair("address", address);
    params += "&" + encodeKeyValuePair("intamount", intAmount);

    InputStreamReader is = getInputStreamReader("account/withdraw", params, getAccountKey());
    return new Gson().fromJson(is, JSONWithdrawResult.class);
  }

  // We also use this service call to check the validity of an account_key before setting it,
  // so we need this method to ignore what value is currently set in this class.
  public JSONBalanceResult getBalance() throws IOException {
    InputStreamReader is = getInputStreamReader("account/balance", null, getAccountKey());
    return new Gson().fromJson(is, JSONBalanceResult.class);
  }

  public JSONBitcoinAddressResult getBitcoinAddress() throws IOException {
    InputStreamReader is = getInputStreamReader("account/bitcoinaddress", null, getAccountKey());
    return new Gson().fromJson(is, JSONBitcoinAddressResult.class);
  }
}
