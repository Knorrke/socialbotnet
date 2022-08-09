package modules.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
  private PasswordUtil() {}

  public static String hashPassword(String pwd) {
    return BCrypt.hashpw(pwd, BCrypt.gensalt());
  }

  public static boolean verifyPassword(String pwd, String hash) {
    return BCrypt.checkpw(pwd, hash);
  }
}
