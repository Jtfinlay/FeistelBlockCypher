/**
 * Created by James on 11/24/2014.
 */
public class Encryption {

    static {
      System.loadLibrary("encryption");
    }

    private native void encrypt(int l, int r, int k);

    public String encrypt_s(String s, int k)
    {
        int l;
        int[] input, output;

        l = (s.length() % 2 == 1) ? s.length()+1 : s.length();
        input = new int[l];
        output = new int[l];

        for (int i=0; i < s.length(); i++)
        {
            input[i] = s.charAt(i);
        }

        System.out.println(input);

        for (int i=0; i < l; i+=2)
        {
           encrypt(input[i], input[i+1], k);
           output[i] = input[i];
           output[i+1] = input[i+1];
        }

        System.out.println(output);

        return output.toString();
    }

}
