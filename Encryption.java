import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

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
//        byte[] v = ByteBuffer.allocate(l).put(s.getBytes(Charset.defaultCharset())).array();
        return encrypt(s.getBytes(), k);
    }
    public byte[] encrypt(byte[] s, String k)
    {
        int l = s.length+4;
        int padding = 0;
        while ((l+padding) % 16 != 0) padding++;

        byte[] v = ByteBuffer.allocate(l + padding).putInt(padding).put(s).array();
	    byte[] key = ByteBuffer.allocate(32).put(k.getBytes()).array();

        return encryptArray(v, key);
    }

    public byte[] decrypt(byte[] v, String k)
    {
        byte[] key = ByteBuffer.allocate(32).put(k.getBytes()).array();
        byte[] decrypted = decryptArray(v, key);

        int padding = ByteBuffer.wrap(Arrays.copyOfRange(decrypted, 0, 4)).getInt();

        return Arrays.copyOfRange(decrypted, 4, decrypted.length - padding);
    }


}
