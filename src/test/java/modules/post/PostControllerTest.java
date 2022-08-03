package modules.post;

import static modules.post.PostFixtures.MOST_LIKED;
import static modules.post.PostFixtures.MOST_TRENDING;
import static modules.post.PostFixtures.NEWEST_POST;
import static modules.post.PostFixtures.POST_BY_1;
import static modules.post.PostFixtures.POST_BY_2;
import static org.assertj.core.api.Assertions.assertThat;

import base.IntegrationTest;
import io.javalin.http.HttpCode;
import io.javalin.testtools.HttpClient;
import io.javalin.testtools.JavalinTest;
import java.io.IOException;
import java.util.stream.Stream;
import modules.post.model.Post;
import modules.util.JSONUtil;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PostControllerTest extends IntegrationTest {

  @Test
  void landingPageShowsAllPosts() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Response response = client.get("/");
          assertThat(response.code()).as("response code of starting page").isEqualTo(200);
          String html = response.body().string();
          for (PostFixtures fixture : PostFixtures.values()) {
            assertThat(html).as("all posts").contains(fixture.message());
          }
        });
  }

  @Test
  void defaultOrder() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Response response = client.get("/");
          assertThat(response.code()).as("response code of starting page").isEqualTo(200);
          String html = response.body().string();

          assertThat(html.indexOf(MOST_TRENDING.message()))
              .describedAs("trending posts before newest post")
              .isLessThan(html.indexOf(NEWEST_POST.message()));

          assertThat(html.indexOf(MOST_TRENDING.message()))
              .describedAs("currently trending posts before most liked post")
              .isLessThan(html.indexOf(MOST_LIKED.message()));

          assertThat(html.indexOf(NEWEST_POST.message()))
              .describedAs("newer posts above older posts outside of trending")
              .isLessThan(html.indexOf(POST_BY_1.message()));
        });
  }

  @Test
  void sortedByLikes() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Response response = client.get("/?sortby=likes");
          assertThat(response.code()).as("fetch posts sorted by likes").isEqualTo(200);
          String html = response.body().string();

          assertThat(html.indexOf(MOST_LIKED.message()))
              .describedAs("most liked post before currently trending posts")
              .isLessThan(html.indexOf(MOST_TRENDING.message()));

          assertThat(html.indexOf(POST_BY_1.message()))
              .describedAs("older posts with likes before new posts without likes")
              .isLessThan(html.indexOf(NEWEST_POST.message()));
        });
  }

  @Test
  void sortedByTime() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Response response = client.get("/?sortby=time");
          assertThat(response.code()).as("fetch posts sorted by time").isEqualTo(200);
          String html = response.body().string();

          assertThat(html.indexOf(NEWEST_POST.message()))
              .describedAs("newest post before most liked post")
              .isLessThan(html.indexOf(MOST_LIKED.message()));

          assertThat(html.indexOf(POST_BY_2.message()))
              .describedAs("newer post before older post")
              .isLessThan(html.indexOf(POST_BY_1.message()));
        });
  }

  @Test
  void sortedByTrending() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Response response = client.get("/?sortby=trending");
          assertThat(response.code()).as("fetch posts sorted by trending").isEqualTo(200);
          String html = response.body().string();

          assertThat(html.indexOf(MOST_TRENDING.message()))
              .describedAs("trending posts before newest post")
              .isLessThan(html.indexOf(NEWEST_POST.message()));

          assertThat(html.indexOf(MOST_TRENDING.message()))
              .describedAs("currently trending posts before most liked post")
              .isLessThan(html.indexOf(MOST_LIKED.message()));

          assertThat(html.indexOf(POST_BY_1.message()))
              .describedAs("older post with some likes above newest post with no likes")
              .isLessThan(html.indexOf(NEWEST_POST.message()));
        });
  }

  @Test
  void unknownSortOption() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Response response = client.get("/?sortby=unsupported");
          assertThat(response.code()).as("successful request").isEqualTo(200);
          String html = response.body().string();

          assertThat(html)
              .describedAs("same result as no parameter")
              .isEqualTo(client.get("/").body().string());
        });
  }

  @Test
  void unAuthorizedLike() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Response response = postWithUrlEncodedBody(client, "/like", "post=3");
          assertThat(response.code())
              .as("Unauthorized request")
              .isEqualTo(HttpCode.UNAUTHORIZED.getStatus());
          Post post =
              JSONUtil.create()
                  .fromJsonString(client.get("/api/post/3").body().string(), Post.class);
          assertThat(post.getLikesCount()).isEqualTo(0);
        });
  }

  @Test
  void likePost() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Post post = requestPostById(client, 3);

          assertThat(post.getLikesCount()).as("number of likes before").isEqualTo(0);
          assertThat(useLogin(client, "test").code()).as("Login successfull").isEqualTo(200);

          Response response = postWithUrlEncodedBody(client, "/like", "post=3");
          assertThat(response.code()).as("Authorized request for liking postid 3").isEqualTo(200);

          post = requestPostById(client, 3);
          assertThat(post.getLikesCount()).as("number of likes afterwards").isEqualTo(1);
        });
  }

  @ParameterizedTest(name = "#{index}- Test redirect with referer {arguments}")
  @MethodSource("provideRefererParameters")
  void redirectBackToKnownRefererOnLike(String referer, String expectedPath) {
    JavalinTest.test(
        app,
        (server, client) -> {
          assertThat(useLogin(client, "test").code()).as("Login successfull").isEqualTo(200);
          Response response =
              postWithUrlEncodedBody(
                  client,
                  "/like",
                  "post=3",
                  referer == null ? null : req -> req.addHeader("referer", referer));
          assertThat(response.code()).isEqualTo(200);
          assertThat(response.request().url().fragment()).as("jump to post").isEqualTo("post-3");
          assertThat(response.request().url().encodedPath())
              .as("validated path from referer")
              .isEqualTo(expectedPath);
        });
  }

  private static Stream<Arguments> provideRefererParameters() {
    String defaultPath = "/pinnwand/test2";
    return Stream.of(
        Arguments.of("https://domain.tld/pinnwand/test2", defaultPath),
        Arguments.of("https://domain.tld/", "/"),
        Arguments.of("https://domain.tld", "/"),
        Arguments.of("https://domain.tld/unexpected/path", defaultPath),
        Arguments.of("mailto:bad@uri.com", defaultPath),
        Arguments.of("unexpected referer format", defaultPath),
        Arguments.of(null, defaultPath));
  }

  @Test
  void unlikePost() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Post post = requestPostById(client, 1);
          assertThat(post.getLikesCount()).as("number of likes before").isEqualTo(2);
          assertThat(post.getRecentLikes()).anyMatch(user -> user.getUsername().equals("test"));
          assertThat(useLogin(client, "test").code()).as("Login successfull").isEqualTo(200);

          Response response = postWithUrlEncodedBody(client, "/unlike", "post=1");
          assertThat(response.code())
              .as("Redirect authorized request for liking postid 1")
              .isEqualTo(200);

          post = requestPostById(client, 1);
          assertThat(post.getLikesCount()).as("number of likes afterwards").isEqualTo(1);
          assertThat(post.getRecentLikes()).noneMatch(user -> user.getUsername().equals("test"));
        });
  }

  private Post requestPostById(HttpClient client, int id) throws IOException {
    return JSONUtil.create()
        .fromJsonString(client.get("/api/post/" + id).body().string(), Post.class);
  }
}
