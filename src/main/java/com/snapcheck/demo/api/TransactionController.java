package com.snapcheck.demo.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.snapcheck.demo.ethereum.EthereumService;
import com.snapcheck.demo.ethereum.EthereumServiceImpl;

@RestController
public class TransactionController {
	static Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);
	
	@Autowired
	private EthereumService service;
	
	@GetMapping("/transactions/list")
	public ResponseEntity<List<BankTransaction>> list() {
		try {
			List<BankTransaction> transactions = service.getAll();
			LOGGER.info("Got {} transactions from service", transactions.size());
			return new ResponseEntity<List<BankTransaction>>(transactions, HttpStatus.OK);
		} catch (IOException ex) {
			return new ResponseEntity<List<BankTransaction>>(new ArrayList<BankTransaction>(), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/transactions/create")
	public ResponseEntity<BankTransaction> create(@RequestBody BankTransaction t) throws IOException {		
		try {
			
			return new ResponseEntity<BankTransaction>(service.create(t), 
					HttpStatus.OK);
		} catch (IOException ex) {
			return new ResponseEntity<BankTransaction>(t, 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
 }
