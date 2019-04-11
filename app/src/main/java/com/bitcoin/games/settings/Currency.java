package com.bitcoin.games.settings;

public enum Currency {
  BCH("bitcoincash:"), BTC("bitcoin://");

  private String prefix;

  Currency(String prefix) {
    this.prefix = prefix;
  }

  public String getPrefix() {
    return prefix;
  }
}