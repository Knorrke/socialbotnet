package modules.post;

import static org.assertj.core.api.Assertions.assertThat;

import base.IntegrationTest;
import io.javalin.http.HttpCode;
import io.javalin.testtools.HttpClient;
import io.javalin.testtools.JavalinTest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import modules.helpers.TestHelpers;
import modules.post.model.Post;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

class PostAPIControllerCreatePostTest extends IntegrationTest {

  private static final String TEST_EXPECTED = "hello, test!";
  private static final String MESSAGE_PARAM = "message=hello%2C%20test%21";
  private static final String AUTH_PARAMS = "username=test&password=test";
  private static final String BODY = String.format("%s&%s", AUTH_PARAMS, MESSAGE_PARAM);

  @Test
  void unAuthorizedCreatePost() {
    JavalinTest.test(
        app,
        (server, client) -> {
          int previousCount = requestPosts(client).size();
          Response response = postWithUrlEncodedBody(client, "/api/post", MESSAGE_PARAM);
          assertThat(response.code())
              .as("Unauthorized request")
              .isEqualTo(HttpCode.UNAUTHORIZED.getStatus());
          ArrayList<Post> posts = requestPosts(client);
          assertThat(posts).as("number of posts stays the same").hasSize(previousCount);
        });
  }

  @Test
  void missingMessageParameter() {
    JavalinTest.test(
        app,
        (server, client) -> {
          int previousCount = requestPosts(client).size();
          Response response = postWithUrlEncodedBody(client, "/api/post", AUTH_PARAMS);
          assertThat(response.code())
              .as("Missing message")
              .isEqualTo(HttpCode.BAD_REQUEST.getStatus());
          assertThat(TestHelpers.toError(response).getError()).contains("Parameter message fehlt");
          ArrayList<Post> posts = requestPosts(client);
          assertThat(posts).as("number of posts stays the same").hasSize(previousCount);
        });
  }

  @Test
  void createPost() {
    JavalinTest.test(
        app,
        (server, client) -> {
          int previousCount = requestPosts(client).size();
          Response response = postWithUrlEncodedBody(client, "/api/post", BODY);
          assertThat(response.code()).as("Authorized request for creating post").isEqualTo(200);

          ArrayList<Post> posts = requestPosts(client);
          assertThat(posts).as("number of posts increased by one").hasSize(previousCount + 1);
          assertThat(posts).anyMatch(post -> post.getMessage().equals(TEST_EXPECTED));
          Post newPost =
              posts.stream()
                  .filter(post -> post.getMessage().equals(TEST_EXPECTED))
                  .collect(Collectors.toList())
                  .get(0);
          assertThat(newPost.getUser().getUsername()).isEqualTo("test");
          assertThat(newPost.getWall().getUsername()).isEqualTo("test");
          assertThat(newPost.getLikesCount()).isZero();
          assertThat(newPost.getPublishingDate()).isToday();
        });
  }

  @Test
  void messageTooLong() {
    JavalinTest.test(
        app,
        (server, client) -> {
          int previousCount = requestPosts(client).size();
          Response response =
              postWithUrlEncodedBody(
                  client,
                  "/api/post",
                  String.format("%s&message=%s", AUTH_PARAMS, StringUtils.repeat("a", 300)));
          assertThat(response.code())
              .as("Message too long")
              .isEqualTo(HttpCode.BAD_REQUEST.getStatus());
          assertThat(TestHelpers.toError(response).getError())
              .as("returns jsonified error response")
              .contains("zu lang");
          ArrayList<Post> posts = requestPosts(client);
          assertThat(posts).as("number of posts stays the same").hasSize(previousCount);
        });
  }

  @Test
  void writeToNonexistentWall() {
    JavalinTest.test(
        app,
        (server, client) -> {
          int previousCount = requestPosts(client).size();
          Response response = postWithUrlEncodedBody(client, "/api/post/nonexistentUser", BODY);
          assertThat(response.code())
              .as("Wall does not exist")
              .isEqualTo(HttpCode.NOT_FOUND.getStatus());
          assertThat(TestHelpers.toError(response).getError())
              .as("returns jsonified error response")
              .contains("User nonexistentUser");

          ArrayList<Post> posts = requestPosts(client);
          assertThat(posts).as("number of posts stays the same").hasSize(previousCount);
        });
  }

  @Test
  void writeToWall() {
    JavalinTest.test(
        app,
        (server, client) -> {
          int previousCount = requestPosts(client).size();

          Response response = postWithUrlEncodedBody(client, "/api/post/test2", BODY);
          assertThat(response.code()).as("Writing to wall of test2").isEqualTo(200);

          ArrayList<Post> posts = requestPosts(client);
          assertThat(posts).as("number of posts increased by one").hasSize(previousCount + 1);
          assertThat(posts).anyMatch(post -> post.getMessage().equals(TEST_EXPECTED));
          Post newPost =
              posts.stream()
                  .filter(post -> post.getMessage().equals(TEST_EXPECTED))
                  .collect(Collectors.toList())
                  .get(0);
          assertThat(newPost.getUser().getUsername()).isEqualTo("test");
          assertThat(newPost.getWall().getUsername()).isEqualTo("test2");
          assertThat(newPost.getLikesCount()).isZero();
          assertThat(newPost.getPublishingDate()).isToday();
        });
  }

  private ArrayList<Post> requestPosts(HttpClient client) throws IOException {
    return TestHelpers.toPostList(client.get("/api/posts?limit=100"));
  }
}
