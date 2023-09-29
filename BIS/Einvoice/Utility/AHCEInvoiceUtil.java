package BIS.Einvoice.Utility;

import java.io.File;
import java.io.FileReader;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.util.io.pem.PemReader;

public class AHCEInvoiceUtil {

	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
	public static final String CHARACTER_ENCODING = "UTF-8";

	public static PublicKey loadPublicKeyFromFile(File publicKeyFile) {
		PublicKey caKey = null;
		try {
			FileReader file = new FileReader(publicKeyFile);
			PemReader reader = new PemReader(file);
			X509EncodedKeySpec caKeySpec = new X509EncodedKeySpec(reader.readPemObject().getContent());
			KeyFactory kf = KeyFactory.getInstance("RSA");
			caKey = kf.generatePublic(caKeySpec);
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return caKey;
	}

	public static String Encrypt(String plaintext, PublicKey key) throws Exception, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encryptedByte = cipher.doFinal(plaintext.getBytes());
		return new String(Base64.encodeBase64(encryptedByte));
	}

	public static byte[] decodeBase64StringTOByte(String stringData) throws Exception {
		return Base64.decodeBase64(stringData.getBytes(CHARACTER_ENCODING));
	}

	public static String encrypt(byte[] plaintext, PublicKey key) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encryptedByte = cipher.doFinal(plaintext);
		return new String(Base64.encodeBase64(encryptedByte));
	}

	/* use this for creating new appkey */
	public static byte[] createAESKey() {
		byte[] appKey = null;
		try {
			KeyGenerator gen = KeyGenerator.getInstance("AES");
			gen.init(256);
			SecretKey secret = gen.generateKey();
			appKey = secret.getEncoded();
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return appKey;
	}

	public static String decrptyBySyymetricKey(String encryptedSek, byte[] appKey) {
		try {
			Key aesKey = new SecretKeySpec(appKey, "AES"); // converts bytes(32 byte random generated) to key
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // encryption type = AES with padding PKCS5
			cipher.init(Cipher.DECRYPT_MODE, aesKey); // initiate decryption type with the key
			byte[] encryptedSekBytes = Base64.decodeBase64(encryptedSek); // decode the base64 encryptedSek to bytes
			byte[] decryptedSekBytes = cipher.doFinal(encryptedSekBytes); // decrypt the encryptedSek with the
																			// initialized cipher containing the
																			// key(Results in bytes)
			String decryptedSek = Base64.encodeBase64String(decryptedSekBytes); // convert the decryptedSek(bytes) to
																				// Base64 StriNG
			return decryptedSek; // return results in base64 string
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String decryptBySymmentricKey(String data, String decryptedSek) {
		byte[] sekByte = Base64.decodeBase64(decryptedSek);
		Key aesKey = new SecretKeySpec(sekByte, "AES");
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			byte[] decordedValue = Base64.decodeBase64(data);
			byte[] decValue = cipher.doFinal(decordedValue);
			return new String(decValue);
		} catch (Exception e) {
			return "Exception " + e;
		}
	}

	public static String encryptBySymmetricKey(String json, String decryptedSek) {
		byte[] sekByte = Base64.decodeBase64(decryptedSek);
		Key aesKey = new SecretKeySpec(sekByte, "AES");
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			byte[] encryptedjsonbytes = cipher.doFinal(json.getBytes());
			String encryptedJson = Base64.encodeBase64String(encryptedjsonbytes);
			return encryptedJson;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
