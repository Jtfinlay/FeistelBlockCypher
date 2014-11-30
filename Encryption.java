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

    public byte[] encrypt(String s, String k)
    {
        int l = s.length();
        while (l % 16 != 0) l++;

        byte[] v = ByteBuffer.allocate(l).put(s.getBytes(Charset.defaultCharset())).array();
	byte[] key = ByteBuffer.allocate(32).put(k.getBytes(Charset.defaultCharset())).array();

        byte[] result = encryptArray(v, key);

        return result;
    }

    public String decrypt(byte[] v, String k)
    {
        byte[] key = ByteBuffer.allocate(32).put(k.getBytes(Charset.defaultCharset())).array();

        byte[] result = decryptArray(v, key);

        return new String(result, Charset.defaultCharset());
    }


}
