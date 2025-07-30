package br.ifpb.pdp.patterns.adapter;

public class AdminAuthService implements IAuthService {
    @Override
    public boolean login(String cpf, String password) {
        System.out.println("[Adapter] Autenticando via AdminAuthService.");
        return "111.222.333-44".equals(cpf) && "admin123".equals(password);
    }
}
