/**
 * Created by James on 11/30/2014.
 */
public class TestEncryption {

    public static void main(String[] args)
    {
        Encryption _encrypt = new Encryption();
        System.loadLibrary("encryption");
        String encrypted = _encrypt.encrypt("Apple", 1);

        System.out.println("Encrypted: " + encrypted);

//        String decrypted = _encrypt.decrypt(encrypted, 1);
	//System.out.println("Decrypted: " + decrypted);

	
    }
}
