package pl.edu.wspa.easybud.backend;

public enum State {

  ACTIVE("Active"),
  DELETED("Deleted"),
  FINISHED("Finished");

  private String name;

  State(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
