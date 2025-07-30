package br.ifpb.pdp.patterns.singleton;

import br.ifpb.pdp.patterns.command.Command;
import br.ifpb.pdp.patterns.command.TriggerAlertCommand;
import br.ifpb.pdp.patterns.observer.AlertObserver;
import br.ifpb.pdp.patterns.state.IncidentContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MonitoringCentral implements AlertObserver {
    private static MonitoringCentral instance;
    private final List<IncidentContext> activeIncidents = new ArrayList<>();
    private final List<IncidentContext> historicalIncidents = new ArrayList<>();
    private final AtomicInteger incidentCounter = new AtomicInteger(0);

    private MonitoringCentral() {}

    public static synchronized MonitoringCentral getInstance() {
        if (instance == null) {
            instance = new MonitoringCentral();
        }
        return instance;
    }

    @Override
    public void update(Command alertCommand) {
        if (alertCommand instanceof TriggerAlertCommand) {
            TriggerAlertCommand triggerCommand = (TriggerAlertCommand) alertCommand;
            int newId = incidentCounter.incrementAndGet();
            IncidentContext newIncident = new IncidentContext(newId, triggerCommand.getUser(), triggerCommand.getLocation());

            synchronized (activeIncidents) {
                activeIncidents.add(newIncident);
            }
            System.out.println("[Observer] Central notificada. Novo incidente #" + newId + " para " + newIncident.getUser().getName());
        }
    }

    public synchronized void finalizeIncident(IncidentContext incident) {
        synchronized (activeIncidents) {
            activeIncidents.remove(incident);
            historicalIncidents.add(incident);
        }
    }

    public synchronized List<IncidentContext> getActiveIncidents() {
        return new ArrayList<>(activeIncidents);
    }
}