package pl.edu.wspa.easybud.views.orders;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
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
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.wspa.easybud.backend.util.State;
import pl.edu.wspa.easybud.backend.entity.ContractorEntity;
import pl.edu.wspa.easybud.backend.entity.OrderEntity;
import pl.edu.wspa.easybud.backend.service.ContractorService;
import pl.edu.wspa.easybud.backend.service.OrderService;
import pl.edu.wspa.easybud.views.employees.EmployeesView;
import pl.edu.wspa.easybud.views.main.MainView;

@Route(value = "orders", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Orders")
@CssImport("styles/views/masterdetail/master-detail-view.css")
public class OrdersView extends Div implements AfterNavigationObserver {

  @Autowired
  private OrderService orderService;

  @Autowired
  private ContractorService contractorService;

  private Grid<OrderEntity> orders;

  private TextField label = new TextField();
  private TextField name = new TextField();
  private TextField number = new TextField();
  private TextField address = new TextField();
  private DatePicker startDate = new DatePicker();
  private DatePicker endDate = new DatePicker();
  private ComboBox<ContractorEntity> contractors = new ComboBox<>();
  //    private PasswordField password = new PasswordField();

  private Button cancel = new Button("Anuluj");
  private Button save = new Button("Zapisz");
  private Button update = new Button("Zaaktualizuj");
  private Button delete = new Button("Usuń");
  private Button goToEmployees = new Button("Pokaż pracowników");

  private Binder<OrderEntity> binder;

  private HorizontalLayout buttonLayout;
  private FormLayout formLayout;

  public OrdersView() {
    setId("orders");
    // Configure Grid
    orders = new Grid<>();
    orders.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    orders.setHeightFull();
    orders.addColumn(OrderEntity::getNumber).setHeader("Numer");
    orders.addColumn(OrderEntity::getLabel).setHeader("Nazwa skrócona");
    orders.addColumn(OrderEntity::getName).setHeader("Nazwa");
    orders.addColumn(OrderEntity::getStartDate).setHeader("Data startu");
    orders.addColumn(OrderEntity::getEndDate).setHeader("Data końca");
    //when a row is selected or deselected, populate form
    orders.addItemClickListener(event -> populateForm(event.getItem()));

    // Configure Form
    binder = new Binder<>(OrderEntity.class);

    // Bind fields. This where you'd define e.g. validation rules
    binder.bindInstanceFields(this);
    // note that password field isn't bound since that property doesn't exist in

    // the grid valueChangeEvent will clear the form too
    cancel.addClickListener(e -> cancel());
    save.addClickListener(e -> save());
    update.addClickListener(e -> update());
    delete.addClickListener(e -> delete());
    goToEmployees.addClickListener(e -> showEmployees(number.getValue()));

    SplitLayout splitLayout = new SplitLayout();
    splitLayout.setSizeFull();

    createGridLayout(splitLayout);
    createEditorLayout(splitLayout);

    add(splitLayout);
  }

  private void showEmployees(String number) {
    VaadinSession vaadinSession = VaadinSession.getCurrent();
    vaadinSession.setAttribute("orderNumber", number);
    UI.getCurrent().navigate(EmployeesView.class);
  }

  private void delete() {
    orderService.delete(number.getValue());
    orders.setItems(orderService.getOrders());
    buttonLayout.replace(update, save);
    buttonLayout.replace(delete, null);
    buttonLayout.replace(goToEmployees, null);
    number.setEnabled(true);
    clearForm();
    Notification.show("Usunięto zlecenie");
  }

  private void clearForm() {
    binder.readBean(null);
    orders.asSingleSelect().clear();
    contractors.clear();
  }

  private void update() {
    if (allRequiredFilled()) {
      OrderEntity entity = OrderEntity.builder().build();
      entity.setNumber(number.getValue());
      entity.setLabel(label.getValue());
      entity.setName(name.getValue());
      entity.setAddress(address.getValue());
      entity.setStartDate(startDate.getValue());
      entity.setEndDate(endDate.getValue());
      entity.setContractor(contractors.getValue());

      orderService.update(entity);
      orders.setItems(orderService.getOrders());
      buttonLayout.replace(update, save);
      buttonLayout.replace(delete, null);
      buttonLayout.replace(goToEmployees, null);
      number.setEnabled(true);
      clearForm();
      Notification.show("Zaaktualizowano dane zlecenia");
    } else {
      Notification.show("Wprowadź dane!");
    }
  }

  private void cancel() {
    clearForm();
    number.setEnabled(true);
    buttonLayout.replace(update, save);
    buttonLayout.replace(delete, null);
    buttonLayout.replace(goToEmployees, null);
  }

  private void save() {
    if (allRequiredFilled()) {
      OrderEntity entity =
          OrderEntity.builder()
              .state(State.ACTIVE)
              .number(number.getValue())
              .label(label.getValue())
              .name(name.getValue())
              .address(address.getValue())
              .startDate(startDate.getValue())
              .endDate(endDate.getValue())
              .contractor(contractors.getValue())
              .build();
      orderService.create(entity);
      orders.setItems(orderService.getOrders());
      clearForm();
      Notification.show("Utworzono zlecenie");
    } else {
      Notification.show("Wprowadź dane!");
    }
  }

  private boolean allRequiredFilled() {
    return !number.isEmpty() && !label.isEmpty() && !name.isEmpty();
  }

  private void createEditorLayout(SplitLayout splitLayout) {
    Div editorDiv = new Div();
    editorDiv.setId("editor-layout");
    formLayout = new FormLayout();
    addFormItem(editorDiv, formLayout, number, "Numer");
    addFormItem(editorDiv, formLayout, label, "Nazwa skrócona");
    addFormItem(editorDiv, formLayout, name, "Nazwa");
    addFormItem(editorDiv, formLayout, address, "Adres");
    addFormItem(editorDiv, formLayout, startDate, "Data startu");
    addFormItem(editorDiv, formLayout, endDate, "Data końca");
    addFormItem(editorDiv, formLayout, contractors, "Kontrahent");

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
    goToEmployees.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    buttonLayout.add(cancel, save);
    editorDiv.add(buttonLayout);
  }

  private void createGridLayout(SplitLayout splitLayout) {
    Div wrapper = new Div();
    wrapper.setId("wrapper");
    wrapper.setWidthFull();
    splitLayout.addToPrimary(wrapper);
    wrapper.add(orders);
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
    orders.setItems(orderService.getOrders());
    contractors.setItems(contractorService.getAllActive());
    contractors.setItemLabelGenerator(ContractorEntity::getLabel);
  }

  private void populateForm(OrderEntity order) {
    buttonLayout.replace(save, update);
    buttonLayout.add(delete);
    buttonLayout.add(goToEmployees);
    number.setEnabled(false);

    contractors.setItems(contractorService.getAllActive());
    contractors.setValue(order.getContractor());
    // Value can be null as well, that clears the form
    binder.readBean(order);

  }
}
