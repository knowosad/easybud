package pl.edu.wspa.easybud.views.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;import com.vaadin.flow.theme.lumo.Lumo;

import org.springframework.security.core.context.SecurityContextHolder;
import pl.edu.wspa.easybud.views.contractors.ContractorsView;
import pl.edu.wspa.easybud.views.orders.OrdersView;
import pl.edu.wspa.easybud.views.employees.EmployeesView;
import pl.edu.wspa.easybud.views.notifications.NotificationListView;
import pl.edu.wspa.easybud.views.notifications.NotificationView;

/**
 * The main view is a top-level placeholder for other views.
 */
@JsModule("./styles/shared-styles.js")
@PWA(name = "EasyBud", shortName = "EasyBud")
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class MainView extends AppLayout {

    private final Tabs menu;

    private final Button logoutBtn;

    public MainView() {
        setPrimarySection(Section.DRAWER);
        logoutBtn = createLogoutButton();
        addToNavbar(true, new DrawerToggle(), logoutBtn);
        menu = createMenuTabs();
        addToDrawer(menu);
    }

    private Button createLogoutButton() {
        Button logoutBtn = new Button("Wyloguj");
        logoutBtn.addClickListener(event1 -> {
            VaadinSession.getCurrent().close();
            SecurityContextHolder.clearContext();
        });
        logoutBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        logoutBtn.getStyle().set("margin-left", "auto");
        return logoutBtn;
    }

    private static Tabs createMenuTabs() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(getAvailableTabs());
        return tabs;
    }

    private static Tab[] getAvailableTabs() {
        final List<Tab> tabs = new ArrayList<>();
//        tabs.add(createTab("Dashboard", DashboardView.class));
        tabs.add(createTab("Zlecenia", OrdersView.class));
        tabs.add(createTab("Pracownicy", EmployeesView.class));
        tabs.add(createTab("Kontrahenci", ContractorsView.class));
        tabs.add(createTab("Powiadomienia", NotificationView.class));
        tabs.add(createTab("Karta powiadomień", NotificationListView.class));
        return tabs.toArray(new Tab[tabs.size()]);
    }

    private static Tab createTab(String title, Class<? extends Component> viewClass) {
        return createTab(populateLink(new RouterLink(null, viewClass), title));
    }

    private static Tab createTab(Component content) {
        final Tab tab = new Tab();
        tab.add(content);
        return tab;
    }

    private static <T extends HasComponents> T populateLink(T a, String title) {
        a.add(title);
        return a;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        selectTab();
    }

    private void selectTab() {
        String target = RouteConfiguration.forSessionScope().getUrl(getContent().getClass());
        Optional<Component> tabToSelect = menu.getChildren().filter(tab -> {
            Component child = tab.getChildren().findFirst().get();
            return child instanceof RouterLink && ((RouterLink) child).getHref().equals(target);
        }).findFirst();
        tabToSelect.ifPresent(tab -> menu.setSelectedTab((Tab) tab));
    }
}
