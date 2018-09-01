package com.snapcheck.demo.ethereum;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;

import com.snapcheck.demo.SnapCheckApplication;
import com.snapcheck.demo.api.BankTransaction;

@Component
public class EthereumServiceImpl implements EthereumService {
	static Logger LOGGER = LoggerFactory.getLogger(EthereumServiceImpl.class);
	@Autowired
	EthereumTransactionMarshaler marshaler;
	
	public List<BankTransaction> getAll() throws IOException {
		Web3j web3j = Web3j.build(new HttpService(SnapCheckApplication.getEthereumUrl()));
		List<BankTransaction> list = new ArrayList<>();
		
	    /* Below code should work but for some reason it returns nothing
	    Subscription subscription = web3j.replayTransactionsObservable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST)
		        .subscribe(tx -> {
		        	BankTransaction t = new BankTransaction();
		        	t.setAccountFrom(tx.getFrom());
		        	t.setAccountTo(tx.getTo());
		        	t.setBlockNumber(tx.getBlockNumber().longValue());
		        	list.add(t);
		}); */
	    
	    /* Below code is not efficient but will have to do until above code can be fixed*/
	    EthBlock.Block firstBlock = web3j.ethGetBlockByNumber(DefaultBlockParameterName.EARLIEST, false)
	    		.send()
	    		.getBlock();
	    EthBlock.Block lastBlock = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false)
	    		.send()
	    		.getBlock();
	    LOGGER.info("First Block is {} Last Block is {}", firstBlock.getNumber(), lastBlock.getNumber());
	    
	    for (long i=firstBlock.getNumber().longValue(); i<=lastBlock.getNumber().longValue(); i++) {
	    	EthBlock.Block currentBlock = web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber( BigInteger.valueOf(i) ), true)
    				.send()
    				.getBlock();
		    LOGGER.debug("Reading Block # {} with {} transactions", i, currentBlock.getTransactions().size());

    		//no idea why this typecast is needed or why TransactionResult is a raw type
		    @SuppressWarnings("rawtypes")
	    	List<EthBlock.TransactionResult> transactions = currentBlock.getTransactions();
	    	for (@SuppressWarnings("rawtypes") EthBlock.TransactionResult x : transactions) {
	    		EthBlock.TransactionObject transaction = (EthBlock.TransactionObject) x;
	        	BankTransaction t = new BankTransaction();
	        	t.setAccountFrom(transaction.getFrom());
	        	t.setAccountTo(transaction.getTo());
	        	t.setBlockNumber(transaction.getBlockNumber().longValue());
	        	t.setHash(transaction.getHash());
	        	
	        	try {
	        		marshaler.unmarshal(transaction.getInput(), t);
	        	} catch (Exception ex) {
	        		LOGGER.error("Error unmarshaling " + t.getHash(), ex);
	        	}
	        	
			    LOGGER.debug("Found transaction {}", transaction.getHash());
			    list.add(t);
	    	}
	    }
	    
	    LOGGER.info("Got {} transactions from ethereum", list.size());
		return list;
	}

	public BankTransaction create(BankTransaction t) throws IOException {
		Web3j web3j = Web3j.build(new HttpService(SnapCheckApplication.getEthereumUrl()));

	    LOGGER.debug("Creating transaction from {} to {}", t.getAccountFrom(), t.getAccountTo());

	    Transaction transaction = Transaction.createEthCallTransaction(t.getAccountFrom(), t.getAccountTo(), 
        		marshaler.marshal(t));
        EthSendTransaction response = web3j.ethSendTransaction(transaction).send();
        if (response.getError() != null) {
    	    LOGGER.error("Error {}", response.getError().getMessage());
            throw new IOException(response.getError().getMessage());
        } else {
    	    t.setHash(response.getTransactionHash());
    	    LOGGER.info("Success Hash={}", response.getTransactionHash());
        }
        
        return t;
	}		
}
