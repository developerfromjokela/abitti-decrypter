package com.developerfromjokela.opdecoder;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Main {

    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Arguments missing!");
            return;
        }
        String pass = args[0];

        pass = pass.trim();


        try {
            System.out.println("Reading file");

            byte[] fileToDecrypt = Files.readAllBytes(new File(args[1]).toPath());

            byte[] generated_pbkdf2 = rawPbkdf2(pass.toCharArray(), pass.getBytes(), 2000, 32+16);
            System.out.println("Calculating keys");

            DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(generated_pbkdf2));
            byte[] encKey = new byte[32];
            byte[] ivArray = new byte[16];


            inputStream.read(encKey, 0, 32);
            inputStream.read(ivArray, 0, 16);
            System.out.println("Done!");

            System.out.println("Decrypting");

            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(encKey, "AES"), new IvParameterSpec(ivArray));
            byte[] decryptedResult = cipher.doFinal(fileToDecrypt);
            System.out.println("File decrypted!");
            System.out.println("Writing to file");

            FileOutputStream fileOutputStream = new FileOutputStream(new File(args[2]));
            fileOutputStream.write(decryptedResult);
            fileOutputStream.close();
            System.out.println("Done!");

        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

    }

    /**
     *  Computes the PBKDF2 hash of a password.
     *
     * @param   password    the password to hash.
     * @param   salt        the salt
     * @param   iterations  the iteration count (slowness factor)
     * @param   bytes       the length of the hash to compute in bytes
     * @return              the PBDKF2 hash of the password
     */
    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }

    /**
     *  Computes the PBKDF2 hash of a password.
     *
     * @param   password    the password to hash.
     * @param   salt        the salt
     * @param   iterations  the iteration count (slowness factor)
     * @param   bytes       the length of the hash to compute in bytes
     * @return              the PBDKF2 hash of the password
     */
    private static byte[] rawPbkdf2(char[] password, byte[] salt, int iterations, int bytes)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }

}
