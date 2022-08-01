package modules.post;

import static modules.post.PostFixtures.MOST_LIKED;
import static modules.post.PostFixtures.MOST_TRENDING;
import static modules.post.PostFixtures.NEWEST_POST;
import static modules.post.PostFixtures.POST_BY_1;
import static modules.post.PostFixtures.POST_BY_2;
import static org.assertj.core.api.Assertions.assertThat;

import base.IntegrationTest;
import io.javalin.testtools.JavalinTest;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

public class PostControllerTest extends IntegrationTest {

  @Test
  public void landingPageShowsAllPosts() {
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
  public void defaultOrder() {
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
  public void sortedByLikes() {
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
  public void sortedByTime() {
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
  public void sortedByTrending() {
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
  public void unknownSortOption() {
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
}
