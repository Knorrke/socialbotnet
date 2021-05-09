package modules.util;

public class ScoreAssigned<T> implements Comparable<ScoreAssigned<T>> {
  private T obj;
  private double score;

  public ScoreAssigned(T obj, double score) {
    this.obj = obj;
    this.score = score;
  }

  public double getScore() {
    return score;
  }

  public T getObj() {
    return obj;
  }

  @Override
  public int compareTo(ScoreAssigned<T> o) {
    return Double.compare(this.getScore(), o.getScore());
  }
}
