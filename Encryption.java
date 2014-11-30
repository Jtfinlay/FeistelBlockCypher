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

    public String encrypt(String s, int k)
    {
        int l = (s.length() % 2 == 1) ? s.length()+1 : s.length();
        l *= 2; // size of char
        while (l % 4 != 0)
            l++;

        byte[] v = ByteBuffer.allocate(l).put(s.getBytes(Charset.defaultCharset())).array();
        byte[] key = ByteBuffer.allocate(4).putInt(k).array();

        System.out.println("V: " + v.length);
        System.out.println("key: " + key.length);

        byte[] result = encryptArray(v, key);

        return new String(result, Charset.defaultCharset());
    }

}
