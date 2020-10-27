package collectionPath;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashClass {
    static String hash(String pass) throws NoSuchAlgorithmException{
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        byte[] res = messageDigest.digest(pass.getBytes());
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < res.length; i++){
            sb.append(Integer.toString((res[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
