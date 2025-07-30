package br.ifpb.pdp.patterns.command;

public class CommandProcessor {
    public void executeCommand(Command command) {
        command.execute();
    }
}
