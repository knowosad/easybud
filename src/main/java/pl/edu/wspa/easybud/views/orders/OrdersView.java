package pl.edu.wspa.easybud.views.orders;

import com.vaadin.flow.component.AbstractField;
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
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.wspa.easybud.backend.State;
import pl.edu.wspa.easybud.backend.entity.ContractorEntity;
import pl.edu.wspa.easybud.backend.entity.OrderEntity;
import pl.edu.wspa.easybud.backend.service.ContractorService;
import pl.edu.wspa.easybud.backend.service.OrderService;
import pl.edu.wspa.easybud.views.main.MainView;

@Route(value = "orders", layout = MainView.class)
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

  private Button cancel = new Button("Cancel");
  private Button save = new Button("Save");
  private Button update = new Button("Update");
  private Button delete = new Button("Delete");

  private Binder<OrderEntity> binder;

  private HorizontalLayout buttonLayout;
  private FormLayout formLayout;

  public OrdersView() {
    setId("orders");
    // Configure Grid
    orders = new Grid<>();
    orders.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    orders.setHeightFull();
    orders.addColumn(OrderEntity::getNumber).setHeader("Number");
    orders.addColumn(OrderEntity::getLabel).setHeader("Label");
    orders.addColumn(OrderEntity::getName).setHeader("Name");
    orders.addColumn(OrderEntity::getStartDate).setHeader("Start date");
    orders.addColumn(OrderEntity::getEndDate).setHeader("Start end");
    //when a row is selected or deselected, populate form
//    orders.asSingleSelect().addValueChangeListener(event -> populateForm(event.getValue()));
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

    SplitLayout splitLayout = new SplitLayout();
    splitLayout.setSizeFull();

    createGridLayout(splitLayout);
    createEditorLayout(splitLayout);

    add(splitLayout);
  }

  private void delete() {
    orderService.delete(number.getValue());
    orders.setItems(orderService.getOrders());
    buttonLayout.replace(update, save);
    buttonLayout.replace(delete, null);
    number.setEnabled(true);
    clearForm();
    Notification.show("The order has been deleded");
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
      String label = contractors.getValue().getLabel();
      Notification.show("update: " + label);
      entity.setContractor(contractors.getValue());

      orderService.update(entity);
      orders.setItems(orderService.getOrders());
      buttonLayout.replace(update, save);
      buttonLayout.replace(delete, null);
      number.setEnabled(true);
      clearForm();
      Notification.show("The order has been updated");
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
      OrderEntity entity =
          OrderEntity.builder()
              .state(State.ACTIVE.getName())
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
      Notification.show("The order has been created");
    } else {
      Notification.show("Set required values!");
    }
  }

  private boolean allRequiredFilled() {
    return !number.isEmpty() && !label.isEmpty() && !name.isEmpty();
  }

  private void createEditorLayout(SplitLayout splitLayout) {
    Div editorDiv = new Div();
    editorDiv.setId("editor-layout");
    formLayout = new FormLayout();
    addFormItem(editorDiv, formLayout, number, "Number");
    addFormItem(editorDiv, formLayout, label, "Label");
    addFormItem(editorDiv, formLayout, name, "Name");
    addFormItem(editorDiv, formLayout, address, "Address");
    addFormItem(editorDiv, formLayout, startDate, "Start date");
    addFormItem(editorDiv, formLayout, endDate, "End date");
    addFormItem(editorDiv, formLayout, contractors, "Contractor");

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
    number.setEnabled(false);

    contractors.setItems(contractorService.getAllActive());
    contractors.setValue(order.getContractor());
    // Value can be null as well, that clears the form
    binder.readBean(order);

    // The password field isn't bound through the binder, so handle that
    //        password.setValue("");
  }
}
