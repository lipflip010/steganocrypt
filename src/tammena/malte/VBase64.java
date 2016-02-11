package tammena.malte;

import java.util.Base64;

public class VBase64 {

    public static final int NO_WRAP = 0;

    public static byte[] decode(String s, int flag) {
        Base64.Decoder d = Base64.getDecoder();
        return d.decode(s.getBytes());
    }

    public static String encodeToString(byte[] b, int flag) {
        Base64.Encoder e = Base64.getEncoder();
        return e.encodeToString(b);
    }
}
