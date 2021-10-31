package modules.user.controller;

import java.util.List;
import modules.error.InputTooLongException;
import modules.error.ResponseError;
import modules.user.model.User;
import modules.user.service.UserService;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import spark.Request;
import spark.Response;
import spark.Spark;

public class UserApiController {

  private UserService service;

  public UserApiController(UserService service) {
    this.service = service;
  }

  public User login(Request req, Response res) {
    User user = new User();
    try {
      MultiMap<String> params = new MultiMap<String>();
      UrlEncoded.decodeTo(req.body(), params, "UTF-8");
      List<String> password = params.getOrDefault("password", params.getValues("passwort"));
      params.put("password", password);
      BeanUtils.populate(user, params);
    } catch (Exception e) {
      Spark.halt(500, "Interner Fehler aufgetreten. Bitte melde das Problem!");
    }
    return service.checkUser(user);
  }

  /**
   * @api {get} /api/users Übersicht aller Nutzer
   * @apiDescription Liefert die letzten 100 registrierten Nutzer.
   * @apiGroup Users — GET
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
  public Object getUsers(Request req, Response res) {
    return service.getAllUsers();
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
  public Object updateProfile(Request req, Response res) {
    User newUser = new User();
    User oldUser = new User();
    try {
      MultiMap<String> params = new MultiMap<String>();
      UrlEncoded.decodeTo(req.body(), params, "UTF-8");

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
      res.status(500);
      return new ResponseError("Interner Fehler aufgetreten. Bitte melde das Problem!");
    }
    try {
      service.updateUser(oldUser, newUser);
    } catch (InputTooLongException e) {
      res.status(400);
      return new ResponseError(e);
    }
    return oldUser;
  }
}
