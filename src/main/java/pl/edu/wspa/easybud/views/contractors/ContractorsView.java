package pl.edu.wspa.easybud.views.contractors;

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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.wspa.easybud.backend.entity.ContractorEntity;
import pl.edu.wspa.easybud.backend.service.ContractorService;
import pl.edu.wspa.easybud.backend.util.State;
import pl.edu.wspa.easybud.views.main.MainView;

@Route(value = "contractors", layout = MainView.class)
@PageTitle("Contractors")
@CssImport("styles/views/masterdetail/master-detail-view.css")
public class ContractorsView extends Div implements AfterNavigationObserver {

  @Autowired
  private ContractorService contractorService;

  private Grid<ContractorEntity> contractors;

  private TextField label = new TextField();
  private TextField name = new TextField();
  private TextField number = new TextField();
  private TextField address = new TextField();

  private Button cancel = new Button("Anuluj");
  private Button save = new Button("Zapisz");
  private Button update = new Button("Zaktualizuj");
  private Button delete = new Button("Usuń");

  private Binder<ContractorEntity> binder;

  private HorizontalLayout buttonLayout;
  private FormLayout formLayout;

  public ContractorsView() {
    setId("contractors");
    // Configure Grid
    contractors = new Grid<>();
    contractors.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    contractors.setHeightFull();
    contractors.addColumn(ContractorEntity::getNumber).setHeader("Numer");
    contractors.addColumn(ContractorEntity::getLabel).setHeader("Nazwa skrócona");
    contractors.addColumn(ContractorEntity::getName).setHeader("Nazwa");

    //when a row is selected or deselected, populate form
    contractors.asSingleSelect().addValueChangeListener(event -> populateForm(event.getValue()));

    // Configure Form
    binder = new Binder<>(ContractorEntity.class);

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
    contractorService.delete(number.getValue());
    contractors.setItems(contractorService.getAllActive());
    buttonLayout.replace(update, save);
    buttonLayout.replace(delete, null);
    number.setEnabled(true);
    clearForm();
    Notification.show("Usunięto kontrahenta");
  }

  private void clearForm() {
    binder.readBean(ContractorEntity.builder().build());
    contractors.asSingleSelect().clear();
  }

  private void update() {
    if (allRequiredFilled()) {
      ContractorEntity entity = ContractorEntity.builder().build();
      entity.setNumber(number.getValue());
      entity.setLabel(label.getValue());
      entity.setName(name.getValue());
      entity.setAddress(address.getValue());

      contractorService.update(entity);
      contractors.setItems(contractorService.getAllActive());
      buttonLayout.replace(update, save);
      buttonLayout.replace(delete, null);
      number.setEnabled(true);
      clearForm();
      Notification.show("Zaaktualizowano dane kontrahenta");
    } else {
      Notification.show("Wprowadź dane!");
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
      ContractorEntity entity =
          ContractorEntity.builder()
              .state(State.ACTIVE)
              .number(number.getValue())
              .label(label.getValue())
              .name(name.getValue())
              .address(address.getValue())
              .build();
      contractorService.create(entity);
      contractors.setItems(contractorService.getAllActive());
      clearForm();
      Notification.show("Utworzono kontrahenta");
    } else {
      Notification.show("Wprowadz dane!");
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
    wrapper.add(contractors);
  }

  private void addFormItem(Div wrapper, FormLayout formLayout, AbstractField field, String fieldName) {
    formLayout.addFormItem(field, fieldName);
    wrapper.add(formLayout);
    field.getElement().getClassList().add("full-width");
  }

  @Override
  public void afterNavigation(AfterNavigationEvent event) {
    contractors.setItems(contractorService.getAllActive());
  }

  private void populateForm(ContractorEntity value) {
    buttonLayout.replace(save, update);
    buttonLayout.add(delete);
    number.setEnabled(false);
    binder.readBean(value);
  }
}
