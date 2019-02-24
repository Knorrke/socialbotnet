package modules.error;

public class ResponseError {
  private String error;

  public ResponseError(String message, String... args) {
    this.error = String.format(message, (Object[]) args);
  }

  public ResponseError(Exception e) {
    this.error = e.getMessage();
  }

  public String getError() {
    return this.error;
  }
}
