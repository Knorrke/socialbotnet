package modules.post;

public enum PostFixtures {
  POST_BY_1("post test1"),
  POST_BY_2("post test2"),
  POST_BY_1_TO_2("test1 to test2"),
  MOST_TRENDING("more viral new"),
  LESS_TRENDING("less viral new"),
  MOST_LIKED("viral old"),
  NEWEST_POST("newest post");

  private String message;

  private PostFixtures(String message) {
    this.message = message;
  }

  public String message() {
    return message;
  }
}
