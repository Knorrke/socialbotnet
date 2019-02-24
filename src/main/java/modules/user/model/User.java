package modules.user.model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;
import modules.util.Identicons;

public class User {

  private int id;
  private String username = "", password = "";
  private String hobbies = "";
  private String about = "";

  /** @return the username */
  public String getUsername() {
    return username;
  }

  /** @param username the username to set */
  public void setUsername(String username) {
    this.username = username;
  }

  /** @return the password */
  public String getPassword() {
    return password;
  }

  /** @param password the password to set */
  public void setPassword(String password) {
    this.password = password;
  }

  /** @return the id */
  public int getId() {
    return id;
  }

  /** @param id the id to set */
  public void setId(int id) {
    this.id = id;
  }

  /** @return the hobbies */
  public String getHobbies() {
    return hobbies;
  }

  /** @param hobbies the hobbies to set */
  public void setHobbies(String hobbies) {
    this.hobbies = hobbies;
  }

  /** @return the about */
  public String getAbout() {
    return about;
  }

  /** @param about the about to set */
  public void setAbout(String about) {
    this.about = about;
  }

  public String getImageAsBase64() {
    BufferedImage icon = Identicons.generateIdenticons(username, 100, 100);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      ImageIO.write(icon, "png", baos);
      baos.flush();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    byte[] data = baos.toByteArray();
    byte[] encoded = Base64.getEncoder().encode(data);
    String base64String = new String(encoded);
    return "data:image/png;base64," + base64String;
  }
}
