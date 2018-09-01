package com.snapcheck.demo.ethereum;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.snapcheck.demo.api.BankTransaction;


@RunWith(SpringJUnit4ClassRunner.class)
public class EthereumTransactionMarshalerTest {
	BankTransaction testCase;
	String marshaledTestData;
	EthereumTransactionMarshaler marshaler;
	
	@Before
	public void setUp() throws Exception {
		//Could not get @Autowired working so manually wiring for now
		marshaler = new EthereumTransactionMarshalerImpl();
		testCase = new BankTransaction();
		testCase.setFrom("John");
		testCase.setTo("Jane");
		testCase.setRoutingNumber("123456");
		testCase.setAccountNumber("987654");
		testCase.setAmount(1.00);
		testCase.setAttachment("f1f2f3f4f5");
		
		marshaledTestData="0x1f8b0800000000000000f3cacfc8abf14acc4bad313432363135abb1b430373335a931d433a849334c334a334e33493305004fab508026000000";
	}

	@Test
	public void marshalTest() {
		String x=null;
		try {
			x = marshaler.marshal(testCase);
		} catch (Exception ex) {
			fail("Exception " + ex);
		}
		assert(marshaledTestData.equals(x));
	}

	@Test
	public void unmarshalTest() {
		BankTransaction t = new BankTransaction();
		try {
			marshaler.unmarshal(marshaledTestData, t);
		} catch (Exception ex) {
			fail("Exception " + ex);
		}
		assert(testCase.getFrom().equals(t.getFrom()));
		assert(testCase.getTo().equals(t.getTo()));
		assert(testCase.getRoutingNumber().equals(t.getRoutingNumber()));
		assert(testCase.getAccountNumber().equals(t.getAccountNumber()));
		assert(testCase.getAmount()==t.getAmount());
		assert(testCase.getAttachment().equals(t.getAttachment()));
	}
}
