package eBIS.CDAC.Utility.Service;

import java.util.Base64;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.stereotype.Service;

import Global.CommonUtility.ResourceBundleFile;

@Service
public class JasyptEncryptionDecryptionImp implements JasyptEncryptionDecryption {

	@Override
	public String Encryption(String PlainText) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		String stPrivateKey = ResourceBundleFile.getValueFromKey("SECRETPRIVATEKEY");
	    encryptor.setPassword(stPrivateKey);        
	    String encryptedText = encryptor.encrypt(PlainText);
	    String encodedString = Base64.getEncoder().withoutPadding().encodeToString(encryptedText.getBytes());
	    return encodedString;
	}

	@Override
	public String Decryption(String EncryptedText) {
		String decode=new String(Base64.getDecoder().decode(EncryptedText));
		StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();
		String stPrivateKey = ResourceBundleFile.getValueFromKey("SECRETPRIVATEKEY");
	    decryptor.setPassword(stPrivateKey);  
	    String decryptedText = decryptor.decrypt(decode);
	    System.out.println("decrypted String: "+decryptedText);
		return decryptedText;
	}
	
}
