package ct.rmi.util;

import org.apache.commons.codec.digest.DigestUtils;

public class Security {
    private static final String salt = "NygJBqvDL6kgAMSqcER7";

    public static String hashPassword(String password) {
        StringBuffer buffer = new StringBuffer(password);
        buffer.insert(password.length() % 8, salt);
        return DigestUtils.sha256Hex(buffer.toString());
    }
}
