package br.ifpb.pdp.patterns.state;
import br.ifpb.pdp.patterns.singleton.MonitoringCentral;

public class ResolvedState implements IncidentState {
    public ResolvedState(IncidentContext context) {
        MonitoringCentral.getInstance().finalizeIncident(context);
    }
    @Override public void confirm() {}
    @Override public void cancel() {}
    @Override public void resolve() {}
    @Override public void markAsFalse() {}
}