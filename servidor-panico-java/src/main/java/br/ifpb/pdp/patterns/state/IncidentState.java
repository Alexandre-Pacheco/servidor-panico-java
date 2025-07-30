package br.ifpb.pdp.patterns.state;

public interface IncidentState {
    void confirm();
    void cancel();
    void resolve();
    void markAsFalse();
}
