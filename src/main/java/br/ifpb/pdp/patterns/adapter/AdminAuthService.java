package br.ifpb.pdp.patterns.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminAuthService implements IAuthService {
    private final List<Map<String, String>> admins = new ArrayList<>();
    private static final String MASTER_PASSWORD = "senhaMestra123";
    private static final String PRIMARY_ADMIN_CPF = "111.222.333-44";

    public AdminAuthService() {
        addInitialAdmin(PRIMARY_ADMIN_CPF, "Admin Principal", "admin123");
        addInitialAdmin("444.555.666-77", "Reitor João Neves", "reitor123");
        addInitialAdmin("888.999.000-11", "Coordenador Mário Santos", "coordenador123");
    }

    private void addInitialAdmin(String cpf, String name, String password) {
        Map<String, String> admin = new HashMap<>();
        admin.put("cpf", cpf);
        admin.put("name", name);
        admin.put("password", password);
        admins.add(admin);
    }

    @Override
    public synchronized boolean login(String cpf, String password) {
        return admins.stream()
                .anyMatch(admin -> admin.get("cpf").equals(cpf) && admin.get("password").equals(password));
    }

    @Override
    public synchronized boolean addAdmin(String cpf, String password, String masterPassword, String name) {
        if (!MASTER_PASSWORD.equals(masterPassword)) return false;
        if (admins.stream().anyMatch(admin -> admin.get("cpf").equals(cpf))) return false;
        
        Map<String, String> newAdmin = new HashMap<>();
        newAdmin.put("cpf", cpf);
        newAdmin.put("name", name);
        newAdmin.put("password", password);
        admins.add(newAdmin);
        return true;
    }

    @Override
    public synchronized boolean removeAdmin(String cpf) {
        if (PRIMARY_ADMIN_CPF.equals(cpf)) return false;
        return admins.removeIf(admin -> admin.get("cpf").equals(cpf));
    }

    @Override
    public synchronized List<Map<String, String>> getAdmins() {
        List<Map<String, String>> publicAdminList = new ArrayList<>();
        for (Map<String, String> admin : admins) {
            Map<String, String> publicAdmin = new HashMap<>();
            publicAdmin.put("cpf", admin.get("cpf"));
            publicAdmin.put("name", admin.get("name"));
            publicAdminList.add(publicAdmin);
        }
        return publicAdminList;
    }
}
