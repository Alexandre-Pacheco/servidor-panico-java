package br.ifpb.pdp.patterns.observer;

import br.ifpb.pdp.patterns.command.Command;
import java.util.ArrayList;
import java.util.List;

public class AlertPublisher {
    private List<AlertObserver> observers = new ArrayList<>();

    public void subscribe(AlertObserver observer) {
        observers.add(observer);
    }

    public void notify(Command alertCommand) {
        for (AlertObserver observer : observers) {
            observer.update(alertCommand);
        }
    }
}