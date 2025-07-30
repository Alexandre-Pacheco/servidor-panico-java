package br.ifpb.pdp.patterns.state;

public class PendingState implements IncidentState {
    private final IncidentContext context;
    public PendingState(IncidentContext context) { this.context = context; }
    @Override public void confirm() { context.setState(new InProgressState(context)); }
    @Override public void cancel() { context.setState(new CancelledState(context)); }
    @Override public void resolve() { /* Ação inválida */ }
    @Override public void markAsFalse() { /* Ação inválida */ }
}