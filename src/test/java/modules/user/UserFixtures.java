package modules.user;

import java.util.Arrays;

public enum UserFixtures {
  TEST1("test"),
  TEST2("test2"),
  EMPTY("empty"),
  VIRAL("viral"),
  VIRAL2("viral2"),
  VIRAL3("viral3"),
  VIRAL4("viral4"),
  VIRAL5("viral5"),
  NEWEST("newest");

  private String username;

  private UserFixtures(String username) {
    this.username = username;
  }

  public String username() {
    return username;
  }

  public static UserFixtures[] getSorted() {
    UserFixtures[] values = values();
    Arrays.sort(values, (fixture1, fixture2) -> fixture2.username().compareTo(fixture1.username()));
    return values;
  }
}
