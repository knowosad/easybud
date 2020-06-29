package pl.edu.wspa.easybud.views.employees;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.wspa.easybud.backend.util.State;
import pl.edu.wspa.easybud.backend.entity.EmployeeEntity;
import pl.edu.wspa.easybud.backend.entity.OrderEntity;
import pl.edu.wspa.easybud.backend.service.EmployeeService;
import pl.edu.wspa.easybud.backend.service.OrderService;
import pl.edu.wspa.easybud.views.main.MainView;

@Route(value = "employees", layout = MainView.class)
@PageTitle("Employees")
@CssImport("styles/views/masterdetail1/master-detail1-view.css")
public class EmployeesView extends Div implements AfterNavigationObserver {

  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private OrderService orderService;

  private Grid<EmployeeEntity> employees;

  private TextField number = new TextField();
  private TextField label = new TextField();
  private TextField firstname = new TextField();
  private TextField lastname = new TextField();
  private ComboBox<OrderEntity> orders = new ComboBox<>();

  private Button cancel = new Button("Anuluj");
  private Button save = new Button("Zapisz");
  private Button update = new Button("Zaaktualizuj");
  private Button delete = new Button("Usuń");

  private Binder<EmployeeEntity> binder;

  private HorizontalLayout buttonLayout;
  private FormLayout formLayout;

  public EmployeesView() {
    setId("employees");
    employees = new Grid<>();
    employees.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    employees.setHeightFull();
    employees.addColumn(EmployeeEntity::getNumber).setHeader("Numer");
    employees.addColumn(EmployeeEntity::getLabel).setHeader("Nazwa skrócona");
    employees.addColumn(EmployeeEntity::getFirstname).setHeader("Imię");
    employees.addColumn(EmployeeEntity::getLastname).setHeader("Nazwisko");
    employees.addColumn(emp -> emp.getOrder().getLabel()).setHeader("Zlecenie");

    //when a row is selected or deselected, populate form
    employees.addItemClickListener(event -> populateForm(event.getItem()));

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
    employeeService.delete(number.getValue());
    fillGrid();
    buttonLayout.replace(update, save);
    hideDeleteBtn();
    number.setEnabled(true);
    clearForm();
    Notification.show("Usunięto pracownika");
  }

  private void clearForm() {
    binder.readBean(new EmployeeEntity());
    employees.asSingleSelect().clear();
    orders.clear();
  }

  private void update() {
    if (allRequiredFilled()) {
      EmployeeEntity entity = buildEmployee();
      employeeService.update(entity);
      fillGrid();
      buttonLayout.replace(update, save);
      hideDeleteBtn();
      number.setEnabled(true);
      clearForm();
      Notification.show("Zaaktualizowano dane praconika");
    } else {
      Notification.show("Wprowadź dane!");
    }
  }


  private void cancel() {

//    if(VaadinService.getCurrentRequest().isUserInRole("USER")){
//      Notification.show("user is USER");
//    }
    clearForm();
    number.setEnabled(true);
    buttonLayout.replace(update, save);
    hideDeleteBtn();
  }

  private void save() {
    if (allRequiredFilled()) {
      EmployeeEntity entity = buildEmployee();
      employeeService.create(entity);
      fillGrid();
      clearForm();
      Notification.show("Utworzono pracownika");
    } else {
      Notification.show("Wprowadź dane!");
    }
  }


  private boolean allRequiredFilled() {
    return !number.isEmpty() && !label.isEmpty() && !firstname.isEmpty() && !lastname.isEmpty();
  }

  private void createEditorLayout(SplitLayout splitLayout) {
    Div editorDiv = new Div();
    editorDiv.setId("editor-layout");
    formLayout = new FormLayout();
    addFormItem(editorDiv, formLayout, number, "Numer");
    addFormItem(editorDiv, formLayout, label, "Nazwa skrócona");
    addFormItem(editorDiv, formLayout, firstname, "Imię");
    addFormItem(editorDiv, formLayout, lastname, "Nazwisko");
    addFormItem(editorDiv, formLayout, orders, "Zlecenie");
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
    VaadinSession vaadinSession = VaadinSession.getCurrent();
    if (vaadinSession.getAttribute("orderNumber") != null){
      employees.setItems(employeeService.findAllByOrderNumber((String) vaadinSession.getAttribute("orderNumber")));
      vaadinSession.setAttribute("orderNumber", null);
    } else {
      fillGrid();
    }
    orders.setItems(orderService.getAllActive());
    orders.setItemLabelGenerator(OrderEntity::getLabel);
  }

  private void populateForm(EmployeeEntity employee) {
    buttonLayout.replace(save, update);
    buttonLayout.add(delete);
    number.setEnabled(false);
    orders.setItems(orderService.getAllActive());
    orders.setValue(employee.getOrder());
    binder.readBean(employee);
  }

  private void hideDeleteBtn() {
    buttonLayout.replace(delete, null);
  }

  private void fillGrid() {
    employees.setItems(employeeService.getEmployees());
  }

  private EmployeeEntity buildEmployee() {
    return EmployeeEntity.builder()
        .state(State.ACTIVE)
        .number(number.getValue())
        .label(label.getValue())
        .firstname(firstname.getValue())
        .lastname(lastname.getValue())
        .order(orders.getValue())
        .build();
  }

}
