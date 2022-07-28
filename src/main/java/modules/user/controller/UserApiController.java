package modules.user.controller;

import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import java.util.List;
import modules.error.InputTooLongException;
import modules.error.ResponseError;
import modules.user.model.User;
import modules.user.service.UserService;
import modules.util.EncodingUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jetty.util.MultiMap;

public class UserApiController {

  private UserService service;

  public UserApiController(UserService service) {
    this.service = service;
  }

  public User login(Context ctx) {
    User user = new User();
    try {
      MultiMap<String> params = EncodingUtil.decode(ctx);
      List<String> password = params.getOrDefault("password", params.getValues("passwort"));
      params.put("password", password);
      BeanUtils.populate(user, params);
      return service.checkUser(user);
    } catch (Exception e) {
      ctx.status(HttpCode.INTERNAL_SERVER_ERROR)
          .result("Interner Fehler aufgetreten. Bitte melde das Problem!");
      return null;
    }
  }

  /**
   * @api {get} /api/users Übersicht aller Nutzer
   * @apiDescription Liefert die letzten 100 registrierten Nutzer.
   * @apiGroup Users — GET
   * @apiQuery {String="id", "username", "hobbies", "about"} [sortby=id] Sortierung
   * @apiQuery {String="asc","desc"} [order=desc] Aufsteigende oder absteigende Sortierung
   * @apiQuery {Number} [limit=100] Limit der angezeigten Posts.
   * @apiSampleRequest /api/users
   * @apiComment <pre>
   * @apiSuccessExample {json} Beispiel: /api/users
   * HTTP/1.1 200 OK
   * [
   *  {
   *   "id":1,
   *   "username":"root",
   *   "hobbies":"doing stuff",
   *   "about":"I am root"
   *  }
   * ]
   * @apiComment </pre>
   */
  public void getUsers(Context ctx) {
    int limit = ctx.queryParamAsClass("limit", Integer.class).getOrDefault(100);
    String sortby = ctx.queryParamAsClass("sortby", String.class).getOrDefault("id");
    String order = ctx.queryParamAsClass("order", String.class).getOrDefault("desc");
    boolean asc = order.equalsIgnoreCase("asc");

    ctx.json(service.getAllUsersSorted(sortby.toLowerCase(), asc, limit));
  }

  /**
   * @api {post} /api/user/update Profilinformationen aktualisieren
   * @apiDescription Aktualisiere die Profilinformationen wie Nutzername, "Hobbies" und "Über mich"
   * @apiGroup Users — POST
   * @apiBody (Anmeldedaten) {String} username Aktueller Benutzername
   * @apiBody (Anmeldedaten) {String} password Passwort des Benutzers
   * @apiBody (Änderungen) {String} [newUsername] Optional. Ändert den Benutzernamen
   * @apiBody (Änderungen) {String} [hobbies] Optional. Ändert die Profilinformation "Hobbies"
   * @apiBody (Änderungen) {String} [about] Optional. Ändert die Profilinformation "Über mich"
   * @apiSampleRequest /api/user/update
   */
  public void updateProfile(Context ctx) {
    User newUser = new User();
    User oldUser = new User();
    try {
      MultiMap<String> params = EncodingUtil.decode(ctx);

      oldUser = service.getUserbyUsername(params.getString("username"));

      newUser.setUsername(
          params.getString("newUsername") != null
              ? params.getString("newUsername")
              : oldUser.getUsername());
      newUser.setHobbies(
          params.getString("hobbies") != null ? params.getString("hobbies") : oldUser.getHobbies());
      newUser.setAbout(
          params.getString("about") != null ? params.getString("about") : oldUser.getAbout());
      newUser.setId(oldUser.getId());

    } catch (Exception e) {
      ctx.status(500)
          .json(new ResponseError("Interner Fehler aufgetreten. Bitte melde das Problem!"));
    }
    try {
      service.updateUser(oldUser, newUser);
    } catch (InputTooLongException e) {
      ctx.status(HttpCode.BAD_REQUEST).json(new ResponseError(e));
    }
    ctx.json(oldUser);
  }
}
