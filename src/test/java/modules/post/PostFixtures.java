package modules.post;

public enum PostFixtures {
  POST_BY_1("post test1", 1),
  POST_BY_2("post test2", 2),
  POST_BY_1_TO_2("test1 to test2", 3),
  MOST_LIKED("viral old", 4),
  MOST_TRENDING("more viral new", 5),
  LESS_TRENDING("less viral new", 6),
  POST_BY_NEWEST("post by newest user", 7),
  POST_BY_NEWEST_TO_OLDEST("post by newest user", 8),
  NEWEST_POST("newest post", 9);

  private String message;
  private int id;

  private PostFixtures(String message, int id) {
    this.message = message;
    this.id = id;
  }

  public String message() {
    return message;
  }

  public int id() {
    return id;
  }
}
