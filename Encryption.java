import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by James on 11/24/2014.
 */
public class Encryption {

    static {
      System.loadLibrary("encryption");
    }

    private native byte[] encryptArray(byte[] v, byte[] key);
    private native byte[] decryptArray(byte[] v, byte[] key);

    public String encrypt(String s, int k)
    {
//        int l = 2*s.length();
        int l = (s.length() % 2 == 1) ? s.length()+1 : s.length();
        l *= 2; // size of char
        while (l % 8 != 0)
	{
	    System.out.println("Padding input.");
            l++;
	}

        byte[] v = ByteBuffer.allocate(l).put(s.getBytes(Charset.defaultCharset())).array();
	byte[] key = ByteBuffer.allocate(32).putInt(k).array();

        byte[] result = encryptArray(v, key);

	System.out.println("--- INPUT ---");
        for (byte b : v) System.out.print(b);
        System.out.println();

	System.out.println("--- ENCRYPTED ---");
	for (byte b : result) System.out.print(b);
	System.out.println();

	byte[] decrypt = decryptArray(result, key);

	System.out.println("--- DECRYPTED ---");
        for (byte b : decrypt) System.out.print(b);
        System.out.println();


        return new String(result, Charset.defaultCharset());
    }

    public String decrypt(String s, int k)
    {
        byte[] v = ByteBuffer.allocate(s.length()*2).put(s.getBytes(Charset.defaultCharset())).array();
        byte[] key = ByteBuffer.allocate(32).putInt(k).array();

        byte[] result = decryptArray(v, key);

        return new String(result, Charset.defaultCharset());
    }


}
