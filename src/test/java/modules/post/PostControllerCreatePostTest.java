package modules.post;

import static org.assertj.core.api.Assertions.assertThat;

import base.IntegrationTest;
import io.javalin.http.HttpStatus;
import io.javalin.testtools.HttpClient;
import io.javalin.testtools.JavalinTest;
import io.javalin.testtools.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import modules.helpers.TestHelpers;
import modules.post.model.Post;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

class PostControllerCreatePostTest extends IntegrationTest {
  private static final String TEST_EXPECTED = "hello, test!";
  private static final String MESSAGE_PARAM = "message=hello%2C%20test%21";

  @Test
  void unAuthorizedCreatePost() {
    JavalinTest.test(
        app,
        (server, client) -> {
          int previousCount = requestPosts(client).size();
          Response response = postWithUrlEncodedBody(client, "/post", MESSAGE_PARAM);
          assertThat(response.code())
              .as("Unauthorized request")
              .isEqualTo(HttpStatus.UNAUTHORIZED.getCode());
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
          assertThat(login(client, "test").code()).as("Login successfull").isEqualTo(302);

          Response response = postWithUrlEncodedBody(client, "/post", MESSAGE_PARAM);
          assertThat(response.code()).as("Authorized request for creating post").isEqualTo(302);

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
          assertThat(login(client, "test").code()).as("Login successfull").isEqualTo(302);
          Response response =
              postWithUrlEncodedBody(
                  client, "/post", String.format("message=%s", StringUtils.repeat("a", 300)));
          assertThat(response.code())
              .as("Message too long")
              .isEqualTo(HttpStatus.BAD_REQUEST.getCode());
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
          assertThat(login(client, "test").code()).as("Login successfull").isEqualTo(302);
          Response response =
              postWithUrlEncodedBody(client, "/post/nonexistentwall", MESSAGE_PARAM);
          assertThat(response.code())
              .as("Wall does not exist")
              .isEqualTo(HttpStatus.NOT_FOUND.getCode());
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
          assertThat(login(client, "test").code()).as("Login successfull").isEqualTo(302);

          Response response = postWithUrlEncodedBody(client, "/post/test2", MESSAGE_PARAM);
          assertThat(response.code()).as("Writing to wall of test2").isEqualTo(302);
          assertThat(response.headers().get("Location"))
              .as("redirected to profile")
              .isEqualTo(List.of("/pinnwand/test2"));

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
