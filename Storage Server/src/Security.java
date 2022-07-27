import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Security {
	//source: https://stackoverflow.com/questions/992019/java-256-bit-aes-password-based-encryption
	static SecretKey secret;
	static byte[] SALT, IV;
	static SecureRandom sr;
	public static byte[] generateBytes(int len) {
		byte[] tmp = new byte[len]; sr.nextBytes(tmp);
		return tmp;
	}
	public static void Generate() throws Exception {
		sr = new SecureRandom(); sr.setSeed(System.nanoTime());
		SALT = generateBytes(8); IV = generateBytes(16);
		secret = new SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(new PBEKeySpec(Constants.AES_KEY.toCharArray(), SALT, 65536, 256)).getEncoded(), "AES");
	}
	public static byte[] Encrypt(String message) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(IV));
		return cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
	}
	public static String Decrypt(byte[] encrypt) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(IV));
		return new String(cipher.doFinal(encrypt), StandardCharsets.UTF_8);
	}
}
