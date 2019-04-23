package com.bitcoin.games.rest;

import android.content.Context;

import com.bitcoin.games.lib.JSONReseedResult;
import com.bitcoin.games.lib.JSONVideoPokerDealResult;
import com.bitcoin.games.lib.JSONVideoPokerDoubleDealerResult;
import com.bitcoin.games.lib.JSONVideoPokerDoublePickResult;
import com.bitcoin.games.lib.JSONVideoPokerHoldResult;
import com.bitcoin.games.lib.JSONVideoPokerUpdateResult;
import com.bitcoin.games.settings.CurrencySettings;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;

public class PokerRestClient extends RestClient {

  private static PokerRestClient instance;

  public static PokerRestClient getInstance(final Context ctx) {
    if (instance == null) {
      synchronized (PokerRestClient.class) {
        if (instance == null) {
          instance = new PokerRestClient(ctx);
        }
      }
    }
    return instance;
  }

  private PokerRestClient(final Context ctx) {
    super(ctx);
  }

  public JSONReseedResult videoPokerReseed() throws IOException {
    InputStreamReader is = getInputStreamReader("videopoker/reseed", null, CurrencySettings.getInstance(ctx).getAccountKey());
    return new Gson().fromJson(is, JSONReseedResult.class);
  }

  public JSONVideoPokerUpdateResult videoPokerUpdate(int last, int chatlast, long creditBTCValue) throws IOException {
    String params = encodeKeyValuePair("last", last);
    params += "&" + encodeKeyValuePair("chatlast", chatlast);
    params += "&" + encodeKeyValuePair("credit_btc_value", creditBTCValue);

    InputStreamReader is = getInputStreamReader("videopoker/update", params, CurrencySettings.getInstance(ctx).getAccountKey());
    return new Gson().fromJson(is, JSONVideoPokerUpdateResult.class);
  }

  public JSONVideoPokerDealResult videoPokerDeal(
      int betSize, int paytable, long creditBTCValue, String serverSeedHash, String clientSeed, boolean useFakeCredits) throws IOException {
    String params = encodeKeyValuePair("bet_size", betSize);
    params += "&" + encodeKeyValuePair("paytable", paytable);
    params += "&" + encodeKeyValuePair("credit_btc_value", creditBTCValue);
    params += "&" + encodeKeyValuePair("server_seed_hash", serverSeedHash);
    params += "&" + encodeKeyValuePair("client_seed", clientSeed);
    params += "&" + encodeKeyValuePair("use_fake_credits", useFakeCredits);

    InputStreamReader is = getInputStreamReader("videopoker/deal", params, CurrencySettings.getInstance(ctx).getAccountKey());
    JSONVideoPokerDealResult result = new Gson().fromJson(is, JSONVideoPokerDealResult.class);
    return result;
  }

  public JSONVideoPokerHoldResult videoPokerHold(String gameID, String holds, String serverSeed) throws IOException {
    String params = encodeKeyValuePair("game_id", gameID);
    params += "&" + encodeKeyValuePair("holds", holds);
    params += "&" + encodeKeyValuePair("server_seed", serverSeed);

    InputStreamReader is = getInputStreamReader("videopoker/hold", params, CurrencySettings.getInstance(ctx).getAccountKey());
    JSONVideoPokerHoldResult result = new Gson().fromJson(is, JSONVideoPokerHoldResult.class);
    return result;
  }

  public JSONVideoPokerDoubleDealerResult videoPokerDoubleDealer(String gameID, String serverSeedHash, String clientSeed, int level) throws IOException {
    String params = encodeKeyValuePair("game_id", gameID);
    params += "&" + encodeKeyValuePair("server_seed_hash", serverSeedHash);
    params += "&" + encodeKeyValuePair("client_seed", clientSeed);
    params += "&" + encodeKeyValuePair("level", level);

    InputStreamReader is = getInputStreamReader("videopoker/double_dealer", params, CurrencySettings.getInstance(ctx).getAccountKey());
    JSONVideoPokerDoubleDealerResult result = new Gson().fromJson(is, JSONVideoPokerDoubleDealerResult.class);
    return result;
  }

  public JSONVideoPokerDoublePickResult videoPokerDoublePick(String gameID, int level, int hold) throws IOException {
    String params = encodeKeyValuePair("game_id", gameID);
    params += "&" + encodeKeyValuePair("level", level);
    params += "&" + encodeKeyValuePair("hold", hold);

    InputStreamReader is = getInputStreamReader("videopoker/double_pick", params, CurrencySettings.getInstance(ctx).getAccountKey());
    JSONVideoPokerDoublePickResult result = new Gson().fromJson(is, JSONVideoPokerDoublePickResult.class);
    return result;
  }
}
