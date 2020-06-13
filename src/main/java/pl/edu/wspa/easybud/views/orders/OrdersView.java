package pl.edu.wspa.easybud.views.orders;

import ch.qos.logback.core.joran.conditional.IfAction;
import com.vaadin.flow.component.html.Label;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.wspa.easybud.backend.entity.OrderEntity;
import pl.edu.wspa.easybud.backend.service.EmployeeService;
import pl.edu.wspa.easybud.backend.entity.Employee;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import pl.edu.wspa.easybud.backend.service.OrderService;
import pl.edu.wspa.easybud.views.main.MainView;

@Route(value = "orders", layout = MainView.class)
@PageTitle("Orders")
@CssImport("styles/views/masterdetail/master-detail-view.css")
public class OrdersView extends Div implements AfterNavigationObserver {

    @Autowired
    private EmployeeService service;

    @Autowired
    private OrderService orderService;

    private Grid<Employee> employees;

    private TextField firstname = new TextField();
    private TextField lastname = new TextField();
//    private TextField email = new TextField();
//    private PasswordField password = new PasswordField();

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private Binder<Employee> binder;

    public OrdersView() {
        setId("orders");
        // Configure Grid
        employees = new Grid<>();
        employees.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        employees.setHeightFull();
        employees.addColumn(Employee::getFirstname).setHeader("Label");
        employees.addColumn(Employee::getLastname).setHeader("Name");
//        employees.addColumn(Employee::getEmail).setHeader("Email");

        //when a row is selected or deselected, populate form
        employees.asSingleSelect().addValueChangeListener(event -> populateForm(event.getValue()));

        // Configure Form
        binder = new Binder<>(Employee.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.bindInstanceFields(this);
        // note that password field isn't bound since that property doesn't exist in
        // Employee

        // the grid valueChangeEvent will clear the form too
        cancel.addClickListener(e -> employees.asSingleSelect().clear());

        save.addClickListener(e -> {
            save();
        });

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);
    }

    private void save() {
        if (!firstname.isEmpty() && !lastname.isEmpty()){
            OrderEntity entity = new OrderEntity(firstname.getValue(), lastname.getValue());
            String response = orderService.create(entity);
            Notification.show(response);
        } else {
            Notification.show("Set required values!");
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorDiv = new Div();
        editorDiv.setId("editor-layout");
        FormLayout formLayout = new FormLayout();
        addFormItem(editorDiv, formLayout, firstname, "Label");
        addFormItem(editorDiv, formLayout, lastname, "Name");
//        addFormItem(editorDiv, formLayout, email, "Email");
//        addFormItem(editorDiv, formLayout, password, "Password");
        createButtonLayout(editorDiv);
        splitLayout.addToSecondary(editorDiv);
    }

    private void createButtonLayout(Div editorDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setId("button-layout");
        buttonLayout.setWidthFull();
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(cancel, save);
        editorDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(employees);
    }

    private void addFormItem(Div wrapper, FormLayout formLayout,
            AbstractField field, String fieldName) {
        formLayout.addFormItem(field, fieldName);
        wrapper.add(formLayout);
        field.getElement().getClassList().add("full-width");
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

        // Lazy init of the grid items, happens only when we are sure the view will be
        // shown to the user
        employees.setItems(service.getEmployees());
    }

    private void populateForm(Employee value) {
        // Value can be null as well, that clears the form
        binder.readBean(value);

        // The password field isn't bound through the binder, so handle that
//        password.setValue("");
    }
}
