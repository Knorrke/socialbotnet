package modules.error;

public class InputTooLongException extends Exception {
  private static final long serialVersionUID = 1L;
  private final String fieldDescription;
  private final int maxLength, currentLength;

  public InputTooLongException(String fieldDescription, int maxLength, int currentLength) {
    this.fieldDescription = fieldDescription;
    this.maxLength = maxLength;
    this.currentLength = currentLength;
  }

  @Override
  public String getMessage() {
    return String.format(
        "%s zu lang! Maximale L\u00E4nge ist %d, aber deine Eingabe ist %d Zeichen lang.",
        fieldDescription, maxLength, currentLength);
  }

  /** @return the fieldDescription */
  public String getFieldDescription() {
    return fieldDescription;
  }

  /** @return the maxLength */
  public int getMaxLength() {
    return maxLength;
  }

  /** @return the currentLength */
  public int getCurrentLength() {
    return currentLength;
  }
}
