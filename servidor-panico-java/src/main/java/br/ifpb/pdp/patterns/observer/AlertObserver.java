package br.ifpb.pdp.patterns.observer;

import br.ifpb.pdp.patterns.command.Command;

public interface AlertObserver {
    void update(Command alertCommand);
}
