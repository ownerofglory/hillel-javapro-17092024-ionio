package ua.ithillel.ionio.decorator.notifier;

public class DefaultNotifier implements Notifier {
    @Override
    public void doNotify(String message) {
        System.out.println("Notification: " + message);
    }
}
