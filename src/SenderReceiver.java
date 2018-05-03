import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.security.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.*;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.KeyGenerator;

public class SenderReceiver {


    private SecretKey symmetricKey;
    private KeyPair AsymmetricKeyPair;

    public SenderReceiver(){

    }
    public void generateSymmetricKey(String algorithm, int keySize) {


        try {
            //Make a key generator that generates AES Keys
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);//AES
            SecureRandom secureRandom = new SecureRandom();

            //Initializes this key generator with the key size and a user-provided source of randomness.
            keyGenerator.init(keySize, secureRandom);

            this.symmetricKey = keyGenerator.generateKey();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
    public void generateKeyPair(String algorithm, int keySize){
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            keyGen.initialize(keySize, random);
            AsymmetricKeyPair = keyGen.generateKeyPair();

        }
        catch(Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
    public SecretKey getSymmetricKey(){
        return this.symmetricKey;
    }
    public PrivateKey getPrivateKey(){
        return this.AsymmetricKeyPair.getPrivate();
    }
    public PublicKey getPublicKey(){
        return this.AsymmetricKeyPair.getPublic();

    }
    public byte[] encryptKey(byte[] keyToEncrypt, Key encryptionKey) {

        byte[] encryptedKey=null;

        try {

            //Get an RSA cipher object
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

            //Encrypt the plaintext symmetric key using the asymmetric key
            cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);//init( Mode, key: the key used to encrypt)
            encryptedKey = cipher.doFinal(keyToEncrypt);//doFinal(byte[]: the data to be encrypted)

        }

        catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return encryptedKey;

    }
    public byte[] decryptKey(byte[] encryptedKey, Key decryptionKey) {

        byte[] decryptedKey = null;

        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, decryptionKey);
            decryptedKey = cipher.doFinal(encryptedKey);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

        return decryptedKey;

    }
    public void encrypt(SecretKey key, File inputFile, File outputFile, String transformation){
        doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile, transformation);
    }
    public void decrypt(SecretKey key, File inputFile, File outputFile, String transformation){
        doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile, transformation);
    }
    private void doCrypto(int cipherMode, SecretKey key, File inputFile, File outputFile, String transformation){

        try {

            //Create a cipher that implements the "AES" transformation with the specified mode: cipherMode
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(cipherMode, key);

            //Read the file into inputStream and place in the buffer, inputBytes
            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            //Encrypt/Decrypt the byte array using AES key according to the mode set by cipherMode
            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);

            inputStream.close();
            outputStream.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException ex) {
            System.out.println("Error encrypting/decrypting file");
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e);
        }
    }
    public String generateHash(byte[] data, String algorithm) {
        String hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] digest = md.digest(data);
            hash = byteToString(digest);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e);
        }
        return hash;
    }
    private static String byteToString(byte[] bytes) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            buf.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return buf.toString();
    }
    public boolean compareHashes(String hash1, String hash2){
        return hash1.equals(hash2);
    }

}
