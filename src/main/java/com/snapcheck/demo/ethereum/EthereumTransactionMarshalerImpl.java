package com.snapcheck.demo.ethereum;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.utils.Numeric;

import com.snapcheck.demo.api.BankTransaction;
import com.snapcheck.demo.security.AESEncryption;

@Component
public class EthereumTransactionMarshalerImpl implements EthereumTransactionMarshaler {
	/* The format of the marshaled data is the following:
	 * 
	 * <from>|<to>|<routing#>|<account# encrypted>|<amount>|<hex encoded binary data (PDF)>
	 * The entire data is then ZIP compressed for efficiency
	 * 
	 */
	
	@Autowired
	protected AESEncryption enc;
	
	public String marshal(BankTransaction t) throws IOException {
		StringBuffer buf = new StringBuffer();
		buf.append(t.getFrom()).append("|")
			.append(t.getTo()).append("|")
			.append(t.getRoutingNumber()).append("|")
			.append(enc.encrypt(t.getAccountNumber())).append("|")
			.append(t.getAmount()).append("|")
			.append(t.getAttachment());
		
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(buf.toString().getBytes());
        gzip.close();
	    return Numeric.toHexString(out.toByteArray());
	}

	public void unmarshal(String x, BankTransaction t) throws IOException {
		GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(
				Numeric.hexStringToByteArray(x)
				));
		
		StringBuffer buf = new StringBuffer();
		int data = gzip.read();
		while(data != -1){
			buf.append( (char) data);
		    data = gzip.read();
		}
		
		String fields[] = buf.toString().split("\\|");
		t.setFrom(fields[0]);
		t.setTo(fields[1]);
		t.setRoutingNumber(fields[2]);
		t.setAccountNumber(enc.decrypt(fields[3]));
		t.setAmount(Double.parseDouble(fields[4]));
		t.setAttachment(fields[5]);
		
		return;
	}	
}
