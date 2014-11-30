/**
 * Created by James on 11/30/2014.
 */
public class TestEncryption {

    public static void main(String[] args)
    {
        Encryption _encrypt = new Encryption();
        System.loadLibrary("encryption");

        byte[] encrypted = _encrypt.encrypt("I never want to feel like I did that day.", 1);
        String decrypted = _encrypt.decrypt(encrypted, 1);

	System.out.println("Decrypted: " + decrypted);
	
    }
}
