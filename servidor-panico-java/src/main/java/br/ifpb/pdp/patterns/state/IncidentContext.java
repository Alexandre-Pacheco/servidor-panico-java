package br.ifpb.pdp.patterns.state;

import br.ifpb.pdp.models.User;

public class IncidentContext {
    private final int id;
    private final User user;
    private final String location;
    private transient IncidentState state; // `transient` para evitar serialização pelo GSON

    public IncidentContext(int id, User user, String location) {
        this.id = id;
        this.user = user;
        this.location = location;
        this.state = new PendingState(this);
    }

    public void setState(IncidentState newState) {
        this.state = newState;
        System.out.println("[State] Incidente #" + id + " transicionou para: " + newState.getClass().getSimpleName());
    }

    public void confirm() { state.confirm(); }
    public void cancel() { state.cancel(); }
    public void resolve() { state.resolve(); }
    public void markAsFalse() { state.markAsFalse(); }

    public int getId() { return id; }
    public User getUser() { return user; }
    public String getLocation() { return location; }
    public IncidentState getState() { return state; }
}
