package br.ifpb.pdp.patterns.state;

public class InProgressState implements IncidentState {
    private final IncidentContext context;
    public InProgressState(IncidentContext context) { this.context = context; }
    @Override public void confirm() {}
    @Override public void cancel() {}
    @Override public void resolve() { context.setState(new ResolvedState(context)); }
    @Override public void markAsFalse() { context.setState(new FalseAlarmState(context)); }
    @Override public String getStatusName() { return "Em Atendimento"; }
}
