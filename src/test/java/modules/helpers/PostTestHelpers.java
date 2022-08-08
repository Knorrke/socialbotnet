package modules.helpers;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import modules.post.model.Post;
import modules.util.JSONUtil;
import okhttp3.Response;

public class PostTestHelpers {
  private static final JSONUtil jsonUtil = JSONUtil.create();

  public static ArrayList<Post> toPostList(Response response) throws IOException {
    return jsonUtil.fromJsonString(
        response.body().string(), new TypeToken<ArrayList<Post>>() {}.getType());
  }

  public static Post toPost(Response response) throws IOException {
    return jsonUtil.fromJsonString(response.body().string(), Post.class);
  }
}
