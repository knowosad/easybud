package pl.edu.wspa.easybud.backend.util;

public enum NotificationType {

  NOTE("Notatka"),
  ORDER("Zamówienie"),
  ACCIDENT("Zgłoszenie");

  private String name;

  NotificationType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
