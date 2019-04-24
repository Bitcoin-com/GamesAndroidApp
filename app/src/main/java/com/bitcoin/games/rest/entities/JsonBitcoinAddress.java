package com.bitcoin.games.rest.entities;

public class JsonBitcoinAddress {
  private String legacyAddress;
  private String cashAddress;

  public String getLegacyAddress() {
    return legacyAddress;
  }

  public String getCashAddress() {
    return cashAddress;
  }
}
