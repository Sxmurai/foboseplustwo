package me.fobose.client.manager;

import java.util.ArrayList;
import me.fobose.client.Fobose;
import me.fobose.client.features.modules.client.HUD;
import me.fobose.client.features.notifications.Notification;

public class NotificationManager {
    private final ArrayList<Notification> notifications = new ArrayList<>();

    public void handleNotifications(int posY) {
        for (int i = 0; i < this.getNotifications().size(); ++i) {
            this.getNotifications().get(i).onDraw(posY);
            posY -= Fobose.moduleManager.getModuleByClass(HUD.class).renderer.getFontHeight() + 5;
        }
    }

    public void addNotification(String text, long duration) {
        this.getNotifications().add(new Notification(text, duration));
    }

    public ArrayList<Notification> getNotifications() {
        return this.notifications;
    }
}

