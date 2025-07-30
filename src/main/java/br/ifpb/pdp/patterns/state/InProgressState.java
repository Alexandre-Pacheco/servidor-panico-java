package br.ifpb.pdp.patterns.state;

public class InProgressState implements IncidentState {
    private final IncidentContext context;
    public InProgressState(IncidentContext context) { this.context = context; }
    @Override public void confirm() { /* Ação inválida */ }
    @Override public void cancel() { /* Ação inválida */ }
    @Override public void resolve() { context.setState(new ResolvedState(context)); }
    @Override public void markAsFalse() { context.setState(new FalseAlarmState(context)); }
}
