package br.ifpb.pdp.patterns.factory;

import br.ifpb.pdp.models.User;

public class UserFactory {
    public User createUser(String id, String name, String type) {
        System.out.println("[Factory Method] Criando usuário: " + name);
        return new User(id, name, type);
    }
}
