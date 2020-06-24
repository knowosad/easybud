package pl.edu.wspa.easybud.backend;

public enum NotificationType {

  NOTE("note"),
  ORDER("order"),
  ACCIDENT("accident");

  private String name;

  NotificationType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
