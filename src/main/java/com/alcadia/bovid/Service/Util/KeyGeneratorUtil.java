package com.alcadia.bovid.Service.Util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class KeyGeneratorUtil {

    private static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 1000; // Número de iteraciones
    private static final int KEY_LENGTH = 256; // Longitud de la clave en bits
    private static final int IV_LENGTH = 16; // Longitud del vector de inicialización en bytes
    private final static String tipoCifrado = "AES/CBC/PKCS5Padding";

    private static SecretKey secretKey;
    private static IvParameterSpec ivParameterSpec;

    @Value("${encrypt.key}")
    private String firmaKey;

    @Value("${encrypt.iv}") // Propiedad para almacenar el IV en las propiedades
    private String ivProperty;

    @PostConstruct
    private void init() {
        secretKey = generateSecretKey(firmaKey);
        ivParameterSpec = loadIV(); // Cargar el IV desde las propiedades
        System.out.println("Secret Key: " + Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        System.out.println("IV: " + Base64.getEncoder().encodeToString(ivParameterSpec.getIV()));
    }

    private static SecretKey generateSecretKey(String password) {
        try {
            byte[] salt = new byte[100];
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
            SecretKey tmp = factory.generateSecret(spec);
            return new SecretKeySpec(tmp.getEncoded(), "AES");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error al generar la clave secreta", e);
        }
    }

    private IvParameterSpec loadIV() {
        if (ivProperty != null && !ivProperty.isEmpty()) {
            byte[] ivBytes = Base64.getDecoder().decode(ivProperty);
            return new IvParameterSpec(ivBytes);
        } else {
            return generateIV(); // Generar un nuevo IV si no está presente en las propiedades
        }
    }
 
    public static String encryptString(String input) {
        try {
            Cipher cipher = Cipher.getInstance(tipoCifrado);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            byte[] encryptedBytes = cipher.doFinal(input.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error al cifrar el string", e);
        }
    }

    public static String decryptString(String encryptedInput) {
        try {
            Cipher cipher = Cipher.getInstance(tipoCifrado);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            byte[] plaintText = cipher.doFinal(Base64.getDecoder().decode(encryptedInput));
            return new String(plaintText);   
        } catch (Exception e) {
            throw new RuntimeException("Error al descifrar el string", e);
        }
    }

    private static IvParameterSpec generateIV() {
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
}