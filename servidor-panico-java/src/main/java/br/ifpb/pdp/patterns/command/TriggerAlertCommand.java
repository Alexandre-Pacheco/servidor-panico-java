package br.ifpb.pdp.patterns.command;

import br.ifpb.pdp.models.User;
import br.ifpb.pdp.patterns.observer.AlertPublisher;

public class TriggerAlertCommand implements Command {
    private final User user;
    private final String location;
    private final AlertPublisher publisher;

    public TriggerAlertCommand(User user, String location, AlertPublisher publisher) {
        this.user = user;
        this.location = location;
        this.publisher = publisher;
    }

    @Override
    public void execute() {
        System.out.println("[Command] Executando TriggerAlertCommand para: " + user.getName());
        publisher.notify(this);
    }

    public User getUser() { return user; }
    public String getLocation() { return location; }
}
