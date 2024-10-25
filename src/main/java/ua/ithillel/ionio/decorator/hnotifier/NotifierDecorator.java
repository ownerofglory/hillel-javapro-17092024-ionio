package ua.ithillel.ionio.decorator.hnotifier;

import ua.ithillel.ionio.decorator.notifier.Notifier;

public abstract class NotifierDecorator implements Notifier {
    protected Notifier notifier;

    public NotifierDecorator(Notifier notifier) {
        this.notifier = notifier;
    }
}
