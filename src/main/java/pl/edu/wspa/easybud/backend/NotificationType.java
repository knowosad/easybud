package pl.edu.wspa.easybud.backend;

public enum NotificationType {

  NOTE("Note"),
  ORDER("Order"),
  ACCIDENT("Accident");

  private String name;

  NotificationType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
