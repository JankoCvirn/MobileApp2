package crypto;

import android.util.Base64;
import android.util.Log;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by janko on 4/28/14.
 */
public class AESCrypto {

    private static final String TAG ="Crypto" ;
    private String key;
    private SecretKeySpec sks;



    public String encrypt(String key,String data){

       String encoded_data=null;
        prepareKey();
        if (sks!=null){

            encoded_data=encode(data);

        }
        return encoded_data;
    }

    public String decrypt(String key,String data){

        String decrypted_data=null;
        prepareKey();
        if (sks!=null){

            decrypted_data=new String(decode(data));

        }
        return decrypted_data;

    }

    private void prepareKey(){

        sks = null;
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(key.getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, sr);
            sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
        } catch (Exception e) {
            Log.e(TAG, "AES secret key spec error");
        }
    }

    private String baseEncode(byte[] toencode){


      return  Base64.encodeToString(toencode, Base64.DEFAULT);


    }

    private String encode(String data){

        byte[] encodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, sks);
            encodedBytes = c.doFinal(data.getBytes());
        } catch (Exception e) {
            Log.e(TAG, "AES encryption error");
        }
        return baseEncode(encodedBytes);

    }
    private String decode(String data){

        byte[] decodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, sks);
            decodedBytes = c.doFinal(data.getBytes());
        } catch (Exception e) {
            Log.e(TAG, "AES decryption error");
        }

        return baseEncode(decodedBytes);
    }
}
