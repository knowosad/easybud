package pl.edu.wspa.easybud.views.employees;

import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.wspa.easybud.backend.State;
import pl.edu.wspa.easybud.backend.entity.OrderEntity;
import pl.edu.wspa.easybud.backend.service.EmployeeService;
import pl.edu.wspa.easybud.backend.entity.EmployeeEntity;
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
import pl.edu.wspa.easybud.views.main.MainView;

@Route(value = "employees", layout = MainView.class)
@PageTitle("Employees")
@CssImport("styles/views/masterdetail1/master-detail1-view.css")
public class EmployeesView extends Div implements AfterNavigationObserver {

  @Autowired
  private EmployeeService service;

  private Grid<EmployeeEntity> employees;

  private TextField number = new TextField();
  private TextField label = new TextField();
  private TextField firstname = new TextField();
  private TextField lastname = new TextField();
  //    private PasswordField password = new PasswordField();

  private Button cancel = new Button("Cancel");
  private Button save = new Button("Save");
  private Button update = new Button("Update");
  private Button delete = new Button("Delete");

  private Binder<EmployeeEntity> binder;

  private HorizontalLayout buttonLayout;
  private FormLayout formLayout;

  public EmployeesView() {
    setId("employees");
    employees = new Grid<>();
    employees.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    employees.setHeightFull();
    employees.addColumn(EmployeeEntity::getNumber).setHeader("Number");
    employees.addColumn(EmployeeEntity::getLabel).setHeader("Label");
    employees.addColumn(EmployeeEntity::getFirstname).setHeader("First name");
    employees.addColumn(EmployeeEntity::getLastname).setHeader("Last name");

    //when a row is selected or deselected, populate form
    employees.asSingleSelect().addValueChangeListener(event -> populateForm(event.getValue()));

    // Configure Form
    binder = new Binder<>(EmployeeEntity.class);

    // Bind fields. This where you'd define e.g. validation rules
    binder.bindInstanceFields(this);
    // note that password field isn't bound since that property doesn't exist in
    // Employee

    // the grid valueChangeEvent will clear the form too
    cancel.addClickListener(e -> cancel());
    save.addClickListener(e -> save());
    update.addClickListener(e -> update());
    delete.addClickListener(e -> delete());

    SplitLayout splitLayout = new SplitLayout();
    splitLayout.setSizeFull();

    createGridLayout(splitLayout);
    createEditorLayout(splitLayout);

    add(splitLayout);
  }

  private void delete() {
    service.delete(number.getValue());
    employees.setItems(service.getEmployees());
    buttonLayout.replace(update, save);
    buttonLayout.replace(delete, null);
    number.setEnabled(true);
    clearForm();
    Notification.show("The employee has been deleded");
  }

  private void clearForm() {
    binder.readBean(new EmployeeEntity());
    employees.asSingleSelect().clear();
  }

  private void update() {
    if (allRequiredFilled()) {
      EmployeeEntity entity = new EmployeeEntity();
      entity.setNumber(number.getValue());
      entity.setLabel(label.getValue());
      entity.setFirstname(firstname.getValue());
      entity.setLastname(lastname.getValue());
      service.update(entity);
      employees.setItems(service.getEmployees());
      buttonLayout.replace(update, save);
      buttonLayout.replace(delete, null);
      number.setEnabled(true);
      clearForm();
      Notification.show("The employee has been updated");
    } else {
      Notification.show("Set required values!");
    }
  }

  private void cancel() {
    clearForm();
    number.setEnabled(true);
    buttonLayout.replace(update, save);
    buttonLayout.replace(delete, null);
  }

  private void save() {
    if (allRequiredFilled()) {
      EmployeeEntity entity =
          EmployeeEntity.builder()
              .state(State.ACTIVE.getName())
              .number(number.getValue())
              .label(label.getValue())
              .firstname(firstname.getValue())
              .lastname(lastname.getValue())
              .build();
      service.create(entity);
      employees.setItems(service.getEmployees());
      clearForm();
      Notification.show("The employee has been created");
    } else {
      Notification.show("Set required values!");
    }
  }

  private boolean allRequiredFilled() {
    return !number.isEmpty() && !label.isEmpty() && !firstname.isEmpty() && !lastname.isEmpty();
  }

  private void createEditorLayout(SplitLayout splitLayout) {
    Div editorDiv = new Div();
    editorDiv.setId("editor-layout");
    formLayout = new FormLayout();
    addFormItem(editorDiv, formLayout, number, "Number");
    addFormItem(editorDiv, formLayout, label, "Label");
    addFormItem(editorDiv, formLayout, firstname, "First name");
    addFormItem(editorDiv, formLayout, lastname, "Last name");
    createButtonLayout(editorDiv);
    splitLayout.addToSecondary(editorDiv);
  }

  private void createButtonLayout(Div editorDiv) {
    buttonLayout = new HorizontalLayout();
    buttonLayout.setId("button-layout");
    buttonLayout.setWidthFull();
    buttonLayout.setSpacing(true);
    cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    update.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
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

  private void addFormItem(Div wrapper, FormLayout formLayout, AbstractField field, String fieldName) {
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

  private void populateForm(EmployeeEntity value) {
    buttonLayout.replace(save, update);
    buttonLayout.add(delete);
    number.setEnabled(false);
    // Value can be null as well, that clears the form
    binder.readBean(value);

    // The password field isn't bound through the binder, so handle that
    //        password.setValue("");
  }
}
