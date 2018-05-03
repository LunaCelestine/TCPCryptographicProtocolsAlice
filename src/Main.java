import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {


        SenderReceiver alice = new SenderReceiver();

        //Alice generates a 1024 bit RSA keypair
        alice.generateKeyPair("RSA", 1024);


        //Alice sends Bob her public key, Bob uses Alice's public key to encrypt his symmertic key
        alice.getPublicKey()//send thius to bob
        //Alice compares key hashes, if both hashes are match, then she begins decryption
        if(alice.compareHashes(alice.generateHash(encryptedKeyB, "SHA-256"), hashB)){
            if(alice.compareHashes(alice.generateHash(fileToBytes(encryptedFile), "SHA-256"), hashM)){

                //Alice uses bob's public key to decrypt the key, then uses her own private key to decrypt that key
                byte[] originalKeyBytes = alice.decryptKey(alice.decryptKey(encryptedKeyB, bob.getPublicKey()), alice.getPrivateKey());

                //This simply generates a SecretKey object from the byte array
                SecretKey originalKey = new SecretKeySpec(originalKeyBytes, 0, originalKeyBytes.length, "AES");

                //Alice decrypts the file using the decrypted key
                alice.decrypt(originalKey, encryptedFile, outFile, "AES");
            }
        }

        Thread t1=new Thread(TCPClientServer::client);
        t1.start();
        Thread t2=new Thread(TCPClientServer::server);
        t2.start();
        t1.join();
        t2.join();
    }

    public static byte[] fileToBytes(File f) {
        //A helper function to clean up main
        byte[] fileContents = new byte[(int) f.length()];
        return fileContents;
    }
}
