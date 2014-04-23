/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package passwordgenerator;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
    static char[] characters = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                        'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                        'w', 'x', 'y', 'z' };
    
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
        
        for (int length = startingStringLength; length < startingStringLength + numberOfStringLengths; length++) {
            for (int amount = 0; amount < numberOfStrings; amount++) {
                String password = generatePassword(length);
                
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
            }
        }
    }
    
    // Generate a random password of a specified length
    private static String generatePassword(int length) {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            int rand = 0 + (int)(Math.random() * (((characters.length - 1) - 0) + 1));
            sb.append(characters[rand]);
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
    
    /*private static void store()
            throws ClassNotFoundException, SQLException {
        //Load the driver.
        Class.forName("com.mysql.jdbc.Driver");

        //Create a connection.
        Connection conn = DriverManager.getConnection("jdbc:mysql://" + url + "/databaseName", user, pass);

        //Create a statement object to use.
        Statement stmt = conn.createStatement();

        //Begin creating tables.
        String create = "CREATE TABLE IF NOT EXISTS PASSWORDS (password VARCHAR(10) NOT NULL, " +
                        "hashedPassword VARCHAR(256) NOT NULL, " +
                        "hashingAlgorithm VARCHAR(10) NOT NULL, " +
                        "PRIMARY KEY (password, hashingAlgorithm))";
        stmt.executeUpdate(create);
    }*/
}
