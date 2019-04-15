package com.bitcoin.games.app;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bitcoin.games.R;
import com.bitcoin.games.lib.CommonActivity;
import com.bitcoin.games.lib.CreateAccountTask;
import com.bitcoin.games.settings.Currency;
import com.bitcoin.games.settings.CurrencySettings;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;

public class CreateAccountActivity extends CommonActivity {

  private CurrencyChangeListener currencyChangeListener;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
    setContentView(R.layout.activity_create_account);

    final CurrencySettings currencySettings = CurrencySettings.getInstance(this);
    ((RadioButton) findViewById(currencySettings.getCurrency() == Currency.BCH ? R.id.radioBch : R.id.radioBtc)).setChecked(true);

    // TB TEMP TEST - For now always reset the preferences (true) so that we
    // can get first time players working.
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

    ((RadioGroup) findViewById(R.id.radioCurrency)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup group, int checkedId) {
        currencySettings.reload(((RadioButton) group.findViewById(checkedId)).getText().toString());
        if (currencySettings.getAccountKey() != null) {
          ((EditText) findViewById(R.id.txtAccountKey)).setText(currencySettings.getAccountKey());
        }
      }
    });
    currencyChangeListener = new CurrencyChangeListener(this::updateValues);
  }

  @Override
  protected void onStart() {
    super.onStart();
    CurrencySettings.getInstance(this).registerObserver(currencyChangeListener);
  }

  @Override
  protected void onStop() {
    super.onStop();
    CurrencySettings.getInstance(this).unregisterObserver(currencyChangeListener);
  }

  private void updateValues(Currency currency) {
    if (CurrencySettings.getInstance(this).getAccountKey() == null) {
      ((TextView) findViewById(R.id.title)).setText(getString(R.string.bitcoin_account, currency.name()));
      ((TextView) findViewById(R.id.subTitle)).setText(getString(R.string.add_existing_account_key, currency.name()));
      ((TextView) findViewById(R.id.btnOpenAccount)).setText(getString(R.string.open_account, currency.name()));
      ((TextView) findViewById(R.id.doNotHaveAccount)).setText(getString(R.string.do_not_have_an_account, currency.name()));
    } else {
      startMainActivity();
    }
  }

  public void onSetAccount(View view) {
    CurrencySettings.getInstance(this).setAccountKey(((EditText) findViewById(R.id.txtAccountKey)).getText().toString());
    startMainActivity();
  }

  private void startMainActivity() {
    startActivity(new Intent(this, MainActivity.class));
  }

  public void onCreateAccount(View view) {
    final CreateAccountTask task = new CreateAccountTask(this, ignored -> startMainActivity());
    task.executeParallel(0L);
  }
}
