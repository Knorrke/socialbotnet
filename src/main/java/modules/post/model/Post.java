package modules.post.model;

import java.awt.Image;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import modules.user.model.User;

@SuppressWarnings("serial")
public class Post implements Serializable {

  private int id;
  private String message = "";
  private Timestamp publishingDate;
  private User user;
  private User wall;
  private Image image;
  private double trending_score;
  private int likes = 0;
  private List<User> likedBy = new ArrayList<>();

  /** @return the message */
  public String getMessage() {
    return message;
  }

  /** @param message the message to set */
  public void setMessage(String message) {
    this.message = message;
  }

  /** @return the image */
  public Image getImage() {
    return image;
  }

  /** @param image the image to set */
  public void setImage(Image image) {
    this.image = image;
  }

  /** @return the publishingDate */
  public Timestamp getPublishingDate() {
    return publishingDate;
  }

  /** @param timestamp the publishingDate to set */
  public void setPublishingDate(Timestamp timestamp) {
    this.publishingDate = timestamp;
  }

  /** @return the id */
  public int getId() {
    return id;
  }

  /** @param id the id to set */
  public void setId(int id) {
    this.id = id;
  }

  /** @return the userid */
  public int getUserId() {
    return user.getId();
  }

  /** @return the username */
  public String getUsername() {
    return user.getUsername();
  }

  /** @return the user */
  public User getUser() {
    return user;
  }

  /** @param user the user to set */
  public void setUser(User user) {
    this.user = user;
  }

  /** @return the recentLikes */
  public List<User> getRecentLikes() {
    return likedBy;
  }

  /** @param recentLikes the recentLikes to set */
  public void setRecentLikes(List<User> recentLikes) {
    this.likedBy = recentLikes;
  }

  /** @return the likesCount */
  public int getLikesCount() {
    return likes;
  }

  /** @param likesCount the likesCount to set */
  public void setLikesCount(int likesCount) {
    this.likes = likesCount;
  }

  /** @return the trending_score */
  public double getTrending_score() {
    return trending_score;
  }

  /** @param trending_score the trending_score to set */
  public void setTrending_score(double trending_score) {
    this.trending_score = trending_score;
  }

  /** @return the wall */
  public User getWall() {
    return wall;
  }

  /** @param wall the wall to set */
  public void setWall(User wall) {
    this.wall = wall;
  }
}
