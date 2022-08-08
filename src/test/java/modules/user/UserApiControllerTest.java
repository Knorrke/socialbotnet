package modules.user;

import static org.assertj.core.api.Assertions.assertThat;

import base.IntegrationTest;
import io.javalin.http.HttpCode;
import io.javalin.testtools.JavalinTest;
import java.util.stream.Stream;
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
}
