package pl.edu.wspa.easybud.views.notifications;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.wspa.easybud.backend.entity.NotificationEntity;
import pl.edu.wspa.easybud.backend.service.NotificationService;
import pl.edu.wspa.easybud.views.main.MainView;

@Route(value = "notification-list", layout = MainView.class)
@PageTitle("Notification List")
@CssImport(value = "styles/views/cardlist/card-list-view.css", include = "lumo-badge")
@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
public class NotificationListView extends Div implements AfterNavigationObserver {

  @Autowired
  private final NotificationService notificationService;

  Grid<NotificationEntity> grid = new Grid<>();

  public NotificationListView(NotificationService notificationService) {
    this.notificationService = notificationService;
    setId("card-list-view");
    addClassName("card-list-view");
    setSizeFull();
    grid.setHeight("100%");
    grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
    grid.addComponentColumn(notification -> createCard(notification));
    add(grid);
  }

  private HorizontalLayout createCard(NotificationEntity entity) {
    HorizontalLayout card = new HorizontalLayout();
    card.addClassName("card");
    card.setSpacing(false);
    card.getThemeList().add("spacing-s");

    VerticalLayout description = new VerticalLayout();
    description.addClassName("description");
    description.setSpacing(false);
    description.setPadding(false);

    HorizontalLayout header = new HorizontalLayout();
    header.addClassName("header");
    header.setSpacing(false);
    header.getThemeList().add("spacing-s");

    Span order = new Span("Order: " + entity.getOrder().getLabel());
    order.addClassName("label");
    Span type = new Span("Type: " + entity.getType().getName());
    type.addClassName("name");
    Span time = new Span(entity.getDateCreated().format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:MM")));
    time.addClassName("date");
    header.add(time, type, order);
    Span post = new Span(entity.getDescription());
    post.addClassName("post");

    description.add(header, post);
    card.add(description);
    return card;
  }

  @Override
  public void afterNavigation(AfterNavigationEvent event) {
    List<NotificationEntity> notifications = notificationService.getAll();
    grid.setItems(notifications);
  }
}
