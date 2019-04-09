package com.bitcoin.games.settings;

public class CurrencySetting {

  private String serverName;
  private String currency;
  private String adminEmail;

  public CurrencySetting(String serverName, String currency, String adminEmail) {
    this.serverName = serverName;
    this.currency = currency;
    this.adminEmail = adminEmail;
  }

  public String getServerName() {
    return serverName;
  }

  public String getServerAddress() {
    return String.format("https://%s", serverName);
  }

  public String getCurrency() {
    return currency;
  }

  public String getAdminEmail() {
    return adminEmail;
  }
}
