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
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String[][] passwords = new String[2][900];
        
        // Generate the passwords and enter 
        for (int i = 2; i < 11; i++) {
            for (int j = 0; j < 100; j++) {
                String pass = generatePassword(i);
                
                // Skip this iteration of the password was already generated before
                if (Arrays.asList(passwords[0]).contains(pass)) {
                    j--;
                    continue;
                }
                
                passwords[0][(i-2)*100+j] = pass;
                String hash = "";
                try {
                    hash = hashString(pass, SHA_256);
                } catch (NoSuchAlgorithmException|UnsupportedEncodingException ex) {
                    Logger.getLogger(PasswordGenerator.class.getName()).log(Level.SEVERE, null, ex);
                }
                passwords[1][(i-2)*100+j] = hash;
                System.out.println("j: " + ((i-2)*100+j) + " Password: " + passwords[0][(i-2)*100+j] + " Hash: " + passwords[1][(i-2)*100+j]);
            }
        }
        
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
    
    // Hash the password using the specified algorithm
    private static String hashString(String string, String hashAlgorithm) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance(hashAlgorithm);

        md.update(string.getBytes("UTF-8")); // Change this to "UTF-16" if needed
        byte[] digest = md.digest();
        
        //convert the byte to hex format method
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
        }
        
        return sb.toString();
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
