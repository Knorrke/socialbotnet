package modules.post;

import static modules.post.PostFixtures.LESS_TRENDING;
import static modules.post.PostFixtures.MOST_LIKED;
import static modules.post.PostFixtures.MOST_TRENDING;
import static modules.post.PostFixtures.NEWEST_POST;
import static modules.post.PostFixtures.POST_BY_1;
import static modules.post.PostFixtures.POST_BY_1_TO_2;
import static modules.post.PostFixtures.POST_BY_2;
import static modules.post.PostFixtures.POST_BY_NEWEST;
import static modules.post.PostFixtures.POST_BY_NEWEST_TO_OLDEST;
import static org.assertj.core.api.Assertions.assertThat;

import base.IntegrationTest;
import io.javalin.testtools.JavalinTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import modules.helpers.TestHelpers;
import modules.post.model.Post;
import okhttp3.Response;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PostAPIControllerGetPostsTest extends IntegrationTest {
  @Test
  void getPostById() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Response response = client.get("/api/post/" + POST_BY_1_TO_2.id());
          assertThat(response.code()).as("api accessible").isEqualTo(200);
          Post post = TestHelpers.toPost(response);
          assertThat(post).isNotNull();
          assertThat(post.getMessage()).isEqualTo(POST_BY_1_TO_2.message());
          assertThat(post.getId()).isEqualTo(POST_BY_1_TO_2.id());
          assertThat(post.getLikesCount()).isZero();
          assertThat(post.getRecentLikes()).isEmpty();
          assertThat(post.getUser().getUsername()).isEqualTo("test");
          assertThat(post.getWall().getUsername()).isEqualTo("test2");
        });
  }

  @Test
  void apiReturnsAllPosts() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Response response = client.get("/api/posts");
          assertThat(response.code()).as("response code of /api/posts").isEqualTo(200);
          ArrayList<Post> posts = TestHelpers.toPostList(response);
          for (PostFixtures fixture : PostFixtures.values()) {
            assertThat(posts)
                .as("all posts")
                .anyMatch(post -> post.getMessage().equals(fixture.message()));
          }
        });
  }

  @ParameterizedTest(name = "#{index}- Test api sorting with {arguments}")
  @MethodSource("provideSortByParameters")
  void sortByAndOrder(String sortByParam, List<Pair<Integer, Integer>> expectedIds) {
    JavalinTest.test(
        app,
        (server, client) -> {
          Response response = client.get("/api/posts?" + sortByParam);
          assertThat(response.code()).as("response code of /api/posts").isEqualTo(200);
          ArrayList<Post> posts = TestHelpers.toPostList(response);

          response = client.get("/api/posts?order=asc&" + sortByParam);
          assertThat(response.code()).as("response code of /api/posts").isEqualTo(200);
          ArrayList<Post> postsAsc = TestHelpers.toPostList(response);

          for (Pair<Integer, Integer> pair : expectedIds) {
            int index = (pair.getLeft() + posts.size()) % posts.size();
            assertThat(posts.get(index).getId())
                .as("check index %d", pair.getLeft())
                .isEqualTo(pair.getRight());
            assertThat(postsAsc.get(postsAsc.size() - 1 - index).getId())
                .as("check ascending order %d", pair.getLeft())
                .isEqualTo(pair.getRight());
          }
        });
  }

  private static Stream<Arguments> provideSortByParameters() {
    List<Pair<Integer, Integer>> defaultExpectedIds =
        Arrays.asList(Pair.of(0, NEWEST_POST.id()), Pair.of(-1, 1));

    PostFixtures[] sortedMessages = PostFixtures.getSorted();

    return Stream.of(
        Arguments.of("", defaultExpectedIds),
        Arguments.of("sortby=unsupported", defaultExpectedIds),
        Arguments.of("sortby=publishingdate", defaultExpectedIds),
        Arguments.of(
            "sortby=message",
            IntStream.range(0, sortedMessages.length)
                .boxed()
                .map(i -> Pair.of(i, sortedMessages[i].id()))
                .collect(Collectors.toList())),
        Arguments.of(
            "sortby=likes",
            Arrays.asList(Pair.of(0, MOST_LIKED.id()), Pair.of(1, MOST_TRENDING.id()))),
        Arguments.of(
            "sortby=user",
            Arrays.asList(
                Pair.of(0, POST_BY_NEWEST_TO_OLDEST.id()), Pair.of(1, POST_BY_NEWEST.id()))),
        Arguments.of(
            "sortby=wall",
            Arrays.asList(Pair.of(0, POST_BY_NEWEST.id()), Pair.of(-1, POST_BY_1.id()))),
        Arguments.of(
            "sortby=trendingscore",
            Arrays.asList(
                Pair.of(0, MOST_TRENDING.id()),
                Pair.of(1, LESS_TRENDING.id()),
                Pair.of(2, MOST_LIKED.id()))),
        Arguments.of(
            "sortby=trending",
            Arrays.asList(
                Pair.of(0, MOST_TRENDING.id()),
                Pair.of(1, LESS_TRENDING.id()),
                Pair.of(2, MOST_LIKED.id()))));
  }

  @Test
  void wallPosts() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Response response = client.get("/api/pinnwand/test2");
          assertThat(response.code()).as("response code of /api/pinnwand/test2").isEqualTo(200);
          ArrayList<Post> posts = TestHelpers.toPostList(response);
          assertThat(posts).as("contains only posts written to test2").hasSize(2);
          assertThat(posts)
              .as("contains the post by test2 on own wall")
              .anyMatch(post -> post.getMessage().equals(POST_BY_2.message()));
          assertThat(posts)
              .as("contains the post by test on test2 wall")
              .anyMatch(post -> post.getMessage().equals(POST_BY_1_TO_2.message()));
        });
  }

  @Test
  void wallNonexistentUser() {
    JavalinTest.test(
        app,
        (server, client) -> {
          Response response = client.get("/api/pinnwand/nonexistentUser");
          assertThat(response.code()).as("user should not exist").isEqualTo(404);
          assertThat(TestHelpers.toError(response).getError())
              .as("returns jsonified error response")
              .contains("User nonexistentUser");
        });
  }
}
