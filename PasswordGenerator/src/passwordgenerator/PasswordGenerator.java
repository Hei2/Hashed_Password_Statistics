/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package passwordgenerator;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Keenan
 */
public class PasswordGenerator {

    // Hashing algorithms
    private static final String SHA_256 = "SHA-256";
    
    // Array for characters to use
    static char[] characters = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                                '9'};
    
    static char[] strongCharacters = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
                                        'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
                                        'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
                                        'y', 'z' };
    
    // The number of string of a specified length to create
    static int numberOfStrings = 10;
    // The number of different string lengths to create
    static int numberOfStringLengths = 10;
    // The starting string length
    static int startingStringLength = 2;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Strings that have been generated
        String[] passwords = new String[numberOfStrings * numberOfStringLengths];
        // The bytes that represent each character in the password strings
        byte[][] passwordBytes = new byte[passwords.length][];
        // The bytes that represent each "character" in the hashed strings
        byte[][] hashedPasswordBytes = new byte[passwordBytes.length][];
        boolean strong = false;
        
        for (int length = startingStringLength; length < startingStringLength + numberOfStringLengths; length++) {
            for (int amount = 0; amount < numberOfStrings; amount++) {
                String password = "";
                if (amount == 0) {
                    strong = false;
                }
                else if (amount >= (numberOfStrings / 2)) {
                    strong = true;
                }
                password = generatePassword(length, strong);
                
                // Skip this iteration of the password was already generated before
                if (Arrays.asList(passwords).contains(password)) {
                    amount--;
                    continue;
                }
                
                passwords[(length - startingStringLength) * numberOfStringLengths + amount] = password;
                
                // Create the byte array to hold the password's characters as bytes
                passwordBytes[(length - startingStringLength) * numberOfStringLengths + amount] = new byte[password.length()];
                
                // Populate the password byte array
                for (int i = 0; i < password.length(); i++) {
                    passwordBytes[(length - startingStringLength) * numberOfStringLengths + amount][i] = password.substring(i, i+1).getBytes()[0];
                }
                
                try {
                    hashedPasswordBytes[(length - startingStringLength) * numberOfStringLengths + amount] = hashString(password, SHA_256);
                } catch (NoSuchAlgorithmException|UnsupportedEncodingException ex) {
                    Logger.getLogger(PasswordGenerator.class.getName()).log(Level.SEVERE, null, ex);
                    amount--;
                    continue;
                }
                
                System.out.print("\n" + ((length - startingStringLength) * numberOfStringLengths +
                        amount) + "\t" + password + "\t" + password.length());
                
                if (strong) {
                    System.out.print("\t STRONG");
                } else {
                    System.out.print("\t WEAK");
                }
                
                ArrayList<Byte> chars = new ArrayList<>();
                for (int i = 0; i  < password.length(); i++) {
                    // Add the current character to the byte array if it's not already there
                    if (chars.contains(password.substring(i, i+1).getBytes()[0])) {
                        continue;
                    }
                    
                    // Count the number of times the characters occurs in the original password
                    int occursInPass = 0;
                    for (int j = 0; j < password.length(); j++) {
                        if (password.substring(i, i+1).equals(password.substring(j, j+1))) {
                            occursInPass++;
                        }
                    }
                    
                    // Count the number of times the character's byte occurs in the hashed password
                    int occursInHash = 0;
                    for (int j = 0; j < hashedPasswordBytes[(length - startingStringLength) * numberOfStringLengths + amount].length; j++) {
                        if (password.substring(i, i+1).getBytes()[0] == hashedPasswordBytes[(length - startingStringLength) * numberOfStringLengths + amount][j]) {
                            occursInHash++;
                        }
                    }
                    
                    chars.add(password.substring(i, i+1).getBytes()[0]);
                    
                    // Print out the character being counted, (the int value of the character),
                    // the number of times it occurs in the password, and the number of times
                    // it occurs in the hashed password
                    System.out.print("\t" + password.substring(i, i+1) + " (" + (int)(password.substring(i, i+1).getBytes()[0]) + ") " + occursInPass + " " + occursInHash);
                }
            }
        }
    }
    
    // Generate a random password of a specified length
    private static String generatePassword(int length, boolean strong) {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            int rand;
            if (strong && (i % 2) == 0) {
                rand = 0 + (int)(Math.random() * (((strongCharacters.length - 1) - 0) + 1));
                sb.append(strongCharacters[rand]);
            } else {
                rand = 0 + (int)(Math.random() * (((characters.length - 1) - 0) + 1));
                sb.append(characters[rand]);
            }
        }
        
        return sb.toString();
    }
    
    // Hash the password using the specified algorithm
    private static byte[] hashString(String string, String hashAlgorithm) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance(hashAlgorithm);

        md.update(string.getBytes("UTF-8")); // Change this to "UTF-16" if needed
        byte[] digest = md.digest();
        
        return digest;
    }
}
