package modules.helpers;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import modules.error.ResponseError;
import modules.post.model.Post;
import modules.user.model.User;
import modules.util.JSONUtil;
import okhttp3.Response;

public class TestHelpers {
  private TestHelpers() {}

  private static final JSONUtil jsonUtil = JSONUtil.create();

  public static ArrayList<Post> toPostList(Response response) throws IOException {
    return jsonUtil.fromJsonString(
        response.body().string(), new TypeToken<ArrayList<Post>>() {}.getType());
  }

  public static Post toPost(Response response) throws IOException {
    return jsonUtil.fromJsonString(response.body().string(), Post.class);
  }

  public static ResponseError toError(Response response) throws IOException {
    return jsonUtil.fromJsonString(response.body().string(), ResponseError.class);
  }

  public static ArrayList<User> toUserList(Response response) throws IOException {
    return jsonUtil.fromJsonString(
        response.body().string(), new TypeToken<ArrayList<User>>() {}.getType());
  }

  public static User toUser(Response response) throws IOException {
    return jsonUtil.fromJsonString(response.body().string(), User.class);
  }
}
