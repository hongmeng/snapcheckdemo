package com.snapcheck.demo.ethereum;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.snapcheck.demo.api.BankTransaction;
import com.snapcheck.demo.security.AESEncryption;


@RunWith(SpringJUnit4ClassRunner.class)
public class EthereumTransactionMarshalerTest {
	BankTransaction testCase;
	String marshaledTestData;
	EthereumTransactionMarshalerImpl marshaler;
	
	@Before
	public void setUp() throws Exception {
		//Could not get @Autowired working so manually wiring for now
		marshaler = new EthereumTransactionMarshalerImpl();
		marshaler.enc=new AESEncryption();
		
		testCase = new BankTransaction();
		testCase.setFrom("John");
		testCase.setTo("Jane");
		testCase.setRoutingNumber("123456");
		testCase.setAccountNumber("987654");
		testCase.setAmount(1.00);
		testCase.setAttachment("f1f2f3f4f5");
		
		marshaledTestData="0x1f8b0800000000000000f3cacfc8abf14acc4bad313432363135ab712ff64b33ae2a0a282e7234d6778e30ceb20c30f428b7b5ad31d433a849334c334a334e33493305002596d8ee38000000";
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
	
	/*(
	public static void main(String arg[]) throws Exception {
		EthereumTransactionMarshalerTest t = new EthereumTransactionMarshalerTest();
		t.setUp();
		
		((EthereumTransactionMarshalerImpl) t.marshaler).enc=new AESEncryption();
		System.out.println(t.marshaler.marshal(t.testCase));
	}
	*/

}
