package modules.user;

import static modules.user.UserFixtures.EMPTY;
import static modules.user.UserFixtures.NEWEST;
import static modules.user.UserFixtures.TEST1;
import static modules.user.UserFixtures.TEST2;
import static org.assertj.core.api.Assertions.assertThat;

import base.IntegrationTest;
import io.javalin.http.HttpCode;
import io.javalin.testtools.JavalinTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import modules.helpers.TestHelpers;
import modules.user.model.User;
import okhttp3.Response;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class UserApiControllerTest extends IntegrationTest {

  @ParameterizedTest(name = "#{index}- Test api authentication with {arguments}")
  @MethodSource("provideLoginParameters")
  void authentication(String description, String param, HttpCode expected) {
    JavalinTest.test(
        app,
        (server, client) -> {
          assertThat(postWithUrlEncodedBody(client, "/api/user/update", param).code())
              .as(description)
              .isEqualTo(expected.getStatus());
        });
  }

  private static Stream<Arguments> provideLoginParameters() {
    return Stream.of(
        Arguments.of("No Params", "", HttpCode.UNAUTHORIZED),
        Arguments.of(
            "Nonexistent user, missing password", "username=nonexistent", HttpCode.UNAUTHORIZED),
        Arguments.of(
            "Nonexistent user", "username=nonexistent&password=test", HttpCode.UNAUTHORIZED),
        Arguments.of("Existing user, missing password", "username=test", HttpCode.UNAUTHORIZED),
        Arguments.of(
            "Existing user, wrong password", "username=test&password=wrong", HttpCode.UNAUTHORIZED),
        Arguments.of("Correct login", "username=test&password=test", HttpCode.OK));
  }

  @Test
  void apiReturnsAllUsers() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Response response = client.get("/api/users");
          assertThat(response.code()).as("response code of /api/users").isEqualTo(200);
          ArrayList<User> users = TestHelpers.toUserList(response);
          for (UserFixtures fixture : UserFixtures.values()) {
            assertThat(users)
                .as("all users")
                .anyMatch(user -> user.getUsername().equals(fixture.username()));
          }
        });
  }

  @ParameterizedTest(name = "#{index}- Test api sorting with {arguments}")
  @MethodSource("provideSortByParameters")
  void sortByAndOrder(String sortByParam, List<Pair<Integer, String>> expectedUsernames) {
    JavalinTest.test(
        app,
        (server, client) -> {
          Response response = client.get("/api/users?" + sortByParam);
          assertThat(response.code()).as("response code of /api/users").isEqualTo(200);
          ArrayList<User> users = TestHelpers.toUserList(response);

          response = client.get("/api/users?order=asc&" + sortByParam);
          assertThat(response.code()).as("response code of /api/users").isEqualTo(200);
          ArrayList<User> usersAsc = TestHelpers.toUserList(response);

          for (Pair<Integer, String> pair : expectedUsernames) {
            int index = (pair.getLeft() + users.size()) % users.size();
            assertThat(users.get(index).getUsername())
                .as("check index %d", pair.getLeft())
                .isEqualTo(pair.getRight());
            assertThat(usersAsc.get(usersAsc.size() - 1 - index).getUsername())
                .as("check ascending order %d", pair.getLeft())
                .isEqualTo(pair.getRight());
          }
        });
  }

  private static Stream<Arguments> provideSortByParameters() {
    List<Pair<Integer, String>> defaultExpectedUsernames =
        Arrays.asList(Pair.of(0, NEWEST.username()), Pair.of(-1, TEST1.username()));

    UserFixtures[] sortedUsers = UserFixtures.getSorted();

    return Stream.of(
        Arguments.of("", defaultExpectedUsernames),
        Arguments.of("sortby=unsupported", defaultExpectedUsernames),
        Arguments.of("sortby=id", defaultExpectedUsernames),
        Arguments.of(
            "sortby=username",
            IntStream.range(0, sortedUsers.length)
                .boxed()
                .map(i -> Pair.of(i, sortedUsers[i].username()))
                .collect(Collectors.toList())),
        Arguments.of(
            "sortby=hobbies",
            Arrays.asList(Pair.of(0, TEST1.username()), Pair.of(-1, EMPTY.username()))),
        Arguments.of(
            "sortby=about",
            Arrays.asList(Pair.of(0, TEST2.username()), Pair.of(-1, EMPTY.username()))));
  }
}
