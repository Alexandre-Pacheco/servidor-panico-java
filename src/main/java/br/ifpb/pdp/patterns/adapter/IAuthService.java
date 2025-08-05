package br.ifpb.pdp.patterns.adapter;

import java.util.List;
import java.util.Map;

public interface IAuthService {
    boolean login(String username, String password);
    boolean addAdmin(String cpf, String password, String masterPassword, String name);
    boolean removeAdmin(String cpf);
    List<Map<String, String>> getAdmins();
}
