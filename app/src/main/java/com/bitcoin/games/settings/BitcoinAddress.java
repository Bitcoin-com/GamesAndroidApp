package com.bitcoin.games.settings;

public class BitcoinAddress {
  private final String prefix;
  private final String address;

  public BitcoinAddress(String prefix, String address) {
    this.prefix = prefix;
    this.address = address;
  }

  public String getPrefix() {
    return prefix;
  }

  public String getAddress() {
    return address;
  }

  @Override
  public String toString() {
    return prefix + address;
  }
}
