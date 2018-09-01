package com.snapcheck.demo.ethereum;

import java.io.IOException;

import com.snapcheck.demo.api.BankTransaction;

public interface EthereumTransactionMarshaler {
	public String marshal(BankTransaction t) throws IOException;
	public  void unmarshal(String x, BankTransaction t) throws IOException;
}
