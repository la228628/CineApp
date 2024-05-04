package be.helha.applicine.common.models;


import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.IOException;

public class HashedPassword {

    public static String getHashedPassword(String password) throws IOException {
        try {
            return BCrypt.hashpw(password, BCrypt.gensalt());
        } catch (Exception e) {
            throw new IOException("Error while hashing the password");
        }
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        try {
            return BCrypt.checkpw(password, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
