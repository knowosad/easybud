package pl.edu.wspa.easybud.views.notifications;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.wspa.easybud.backend.util.NotificationType;
import pl.edu.wspa.easybud.backend.entity.NotificationEntity;
import pl.edu.wspa.easybud.backend.entity.OrderEntity;
import pl.edu.wspa.easybud.backend.service.NotificationServive;
import pl.edu.wspa.easybud.backend.service.OrderService;
import pl.edu.wspa.easybud.views.main.MainView;

import java.time.LocalDateTime;

@Route(value = "notification", layout = MainView.class)
@PageTitle("Notification")
@CssImport("styles/views/form/form-view.css")
public class NotificationView extends Div implements AfterNavigationObserver {

    @Autowired
    private OrderService orderService;

    @Autowired
    private NotificationServive notificationServive;

    private ComboBox<NotificationType> type = new ComboBox<>();
    private ComboBox<OrderEntity> orders = new ComboBox<>();
    private TextArea description = new TextArea();

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    public NotificationView() {
        setId("form-view");
        VerticalLayout wrapper = createWrapper();

        createTitle(wrapper);
        createFormLayout(wrapper);
        createButtonLayout(wrapper);

        // Configure Form
        Binder<NotificationEntity> binder = new Binder<>(NotificationEntity.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> cancel());
        save.addClickListener(e -> save());

        add(wrapper);
    }

    private void save() {
        if (allRequiredFilled()) {
            NotificationEntity entity = buildNotification();
            notificationServive.create(entity);
            Notification.show("The notification has been created");

            UI.getCurrent().navigate("card-list");
            clearForm();
        }else {
            Notification.show("Set required values!");
        }
    }

    private boolean allRequiredFilled() {
        return !orders.isEmpty() && !description.isEmpty() && !type.isEmpty();
    }

    private NotificationEntity buildNotification() {
        return NotificationEntity.builder()
            .type(type.getValue())
            .order(orders.getValue())
            .description(description.getValue())
            .dateCreated(LocalDateTime.now())
            .userCreated(VaadinService.getCurrentRequest().getRemoteUser())
            .build();
    }

    private void cancel() {
        clearForm();
    }

    private void clearForm() {
        type.clear();
        orders.clear();
        description.clear();
    }

    private void createTitle(VerticalLayout wrapper) {
        H1 h1 = new H1("Notification");
        wrapper.add(h1);
    }

    private VerticalLayout createWrapper() {
        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setId("wrapper");
        wrapper.setSpacing(false);
        return wrapper;
    }

    private void createFormLayout(VerticalLayout wrapper) {
        FormLayout formLayout = new FormLayout();
        addFormItem(wrapper, formLayout, type, "Type");
        FormLayout.FormItem ordersFormItem = addFormItem(wrapper, formLayout, orders, "Order");
        formLayout.setColspan(ordersFormItem, 2);
        FormLayout.FormItem descriptionFormItem = addFormItem(wrapper, formLayout,
            description, "Notes");
        formLayout.setColspan(descriptionFormItem, 2);
    }

    private void createButtonLayout(VerticalLayout wrapper) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        buttonLayout.setWidthFull();
        buttonLayout
                .setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(cancel);
        buttonLayout.add(save);
        wrapper.add(buttonLayout);
    }

    private FormLayout.FormItem addFormItem(VerticalLayout wrapper,
            FormLayout formLayout, Component field, String fieldName) {
        FormLayout.FormItem formItem = formLayout.addFormItem(field, fieldName);
        wrapper.add(formLayout);
        field.getElement().getClassList().add("full-width");
        return formItem;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        orders.setItems(orderService.getAllActive());
        orders.setItemLabelGenerator(order -> order.getLabel() + " [" + order.getAddress() + "]");

        type.setItems(NotificationType.values());
        type.setItemLabelGenerator(NotificationType::getName);
    }


}
