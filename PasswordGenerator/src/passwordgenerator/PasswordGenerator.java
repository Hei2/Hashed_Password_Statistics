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
                        'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
                        'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
                        'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3',
                        '4', '5', '6', '7', '8', '9', '0', '-', '=', '!', '@',
                        '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', '`',
                        '~', ',', '.', '/', ';', '\'', '[', ']', '\\', '<',
                        '>', '?', ':', '\"', '{', '}', '|', ' ' };
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for (int i = 0; i < 10; i ++) {
            for (int j = 0; j < 10; j++) {
                System.out.println(generatePassword(i));
            }
        }
        
        /*try {
            hashString(SHA_256);
        } catch (NoSuchAlgorithmException|UnsupportedEncodingException ex) {
            Logger.getLogger(PasswordGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        /*try {
            store();
        } catch (ClassNotFoundException|SQLException ex) {
            Logger.getLogger(PasswordGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }*/
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
    
    private static String hashString(String string, String hashAlgorithm) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // Hash the password using the specified algorithm
        MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
        String text = "This is some text";

        md.update(text.getBytes("UTF-8")); // Change this to "UTF-16" if needed
        byte[] digest = md.digest();
        return digest.toString();
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
