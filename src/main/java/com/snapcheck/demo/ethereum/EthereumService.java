package com.snapcheck.demo.ethereum;

import java.io.IOException;
import java.util.List;

import com.snapcheck.demo.api.BankTransaction;

public interface EthereumService {
	public List<BankTransaction> getAll() throws IOException;
	public BankTransaction create(BankTransaction t) throws IOException;
}
