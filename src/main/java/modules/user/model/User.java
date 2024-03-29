package modules.user.model;

import java.io.Serializable;
import org.eclipse.jetty.util.UrlEncoded;

@SuppressWarnings("serial")
public class User implements Serializable {

  private int id;
  private String username = "";
  private transient String password = null;
  private String hobbies = "";
  private String about = "";

  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @param password the password to set
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return the hobbies
   */
  public String getHobbies() {
    return hobbies;
  }

  /**
   * @param hobbies the hobbies to set
   */
  public void setHobbies(String hobbies) {
    this.hobbies = hobbies;
  }

  /**
   * @return the about
   */
  public String getAbout() {
    return about;
  }

  /**
   * @param about the about to set
   */
  public void setAbout(String about) {
    this.about = about;
  }

  public String getImage() {
    return String.format(
        "https://api.dicebear.com/7.x/bottts/svg?seed=%s.&size=%d",
        UrlEncoded.encodeString(username), 100);
  }
}
