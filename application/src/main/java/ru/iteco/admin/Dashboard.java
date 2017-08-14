package ru.iteco.admin;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.vaadin.teemusa.sidemenu.SideMenu;

@SpringUI(path = "/admin")
public class Dashboard extends UI {
    @Override
    public void init(VaadinRequest request) {
        SideMenu menu = new SideMenu();
        menu.setMenuCaption("MyUI Menu");
        menu.addMenuItem("My Menu Entry", () ->
                Notification.show("Here is my custom action for this menu item.")
        );
        setContent(menu);
    }
}