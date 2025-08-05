package br.ifpb.pdp.patterns.facade;

import br.ifpb.pdp.models.User;
import br.ifpb.pdp.patterns.adapter.IAuthService;
import br.ifpb.pdp.patterns.command.CommandProcessor;
import br.ifpb.pdp.patterns.command.TriggerAlertCommand;
import br.ifpb.pdp.patterns.factory.UserFactory;
import br.ifpb.pdp.patterns.observer.AlertPublisher;
import br.ifpb.pdp.patterns.singleton.MonitoringCentral;
import br.ifpb.pdp.patterns.state.IncidentContext;
import br.ifpb.pdp.patterns.strategy.LocationProvider;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PanicSystemFacade {
    private final UserFactory userFactory = new UserFactory();
    private final LocationProvider locationProvider = new LocationProvider();
    private final CommandProcessor commandProcessor = new CommandProcessor();
    private final AlertPublisher alertPublisher = new AlertPublisher();
    private final IAuthService authService;
    private final List<User> registeredUsers = new ArrayList<>();
    private final Gson gson = new Gson();

    public PanicSystemFacade(IAuthService authService) {
        this.authService = authService;
        this.alertPublisher.subscribe(MonitoringCentral.getInstance());
        
        addUser("2023123", "Aluno Robson", "Aluno");
        addUser("987654", "Professora Maria", "Professor");
        System.out.println("[Facade] PanicSystemFacade (Server Mode) inicializado.");
    }
    
    public synchronized void triggerPanic(User user) {
        String location = locationProvider.getLocation();
        TriggerAlertCommand command = new TriggerAlertCommand(user, location, alertPublisher);
        commandProcessor.executeCommand(command);
    }
    
    public synchronized boolean loginAdmin(String cpf, String password) {
        return authService.login(cpf, password);
    }
    
    public synchronized User addUser(String id, String name, String type) {
        if (registeredUsers.stream().anyMatch(u -> u.getId().equals(id))) return null;
        User newUser = userFactory.createUser(id, name, type);
        registeredUsers.add(newUser);
        return newUser;
    }

    public synchronized void removeUserById(String userId) {
        registeredUsers.removeIf(user -> user.getId().equals(userId));
    }
    
    public synchronized Optional<User> getRegisteredUserById(String userId) {
        return registeredUsers.stream().filter(u -> u.getId().equals(userId)).findFirst();
    }

    public synchronized List<User> getRegisteredUsers() {
        return new ArrayList<>(registeredUsers);
    }
    
    public synchronized String getActiveIncidentsAsJson() {
        List<Map<String, Object>> simplifiedIncidents = MonitoringCentral.getInstance().getActiveIncidents()
                .stream()
                .map(incident -> Map.of(
                        "id", incident.getId(),
                        "user", incident.getUser(),
                        "location", incident.getLocation(),
                        "state", Map.of(
                                "statusName", incident.getState().getStatusName(),
                                "class", incident.getState().getClass().getName()
                        )
                ))
                .collect(Collectors.toList());
        
        return gson.toJson(simplifiedIncidents);
    }
    
    public synchronized void handleIncidentAction(int incidentId, String action) {
        MonitoringCentral.getInstance().getActiveIncidents().stream()
            .filter(inc -> inc.getId() == incidentId)
            .findFirst()
            .ifPresent(incident -> {
                switch (action) {
                    case "confirm": incident.confirm(); break;
                    case "cancel": incident.cancel(); break;
                    case "resolve": incident.resolve(); break;
                    case "markasfalse": incident.markAsFalse(); break;
                }
            });
    }

    public synchronized boolean addAdmin(String cpf, String password, String masterPassword, String name) {
        return authService.addAdmin(cpf, password, masterPassword, name);
    }

    public synchronized boolean removeAdmin(String cpf) {
        return authService.removeAdmin(cpf);
    }

    public synchronized List<Map<String, String>> getAdmins() {
        return authService.getAdmins();
    }
}
