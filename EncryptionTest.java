/**
 * Created by James on 11/30/2014.
 */
public class EncryptionTest {

    public static void main(String[] args)
    {
        Encryption _encrypt = new Encryption();
        System.loadLibrary("encryption");
        String result = _encrypt.encrypt("Smile", 234567);
        System.out.println("Result: " + result);
    }
}
