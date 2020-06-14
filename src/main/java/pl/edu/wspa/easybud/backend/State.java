package pl.edu.wspa.easybud.backend;

public enum State {

  ACTIVE("active"),
  DELETED("deleted"),
  FINISHED("finished");

  private String name;

  State(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
