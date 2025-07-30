package br.ifpb.pdp.patterns.adapter;

public interface IAuthService {
    boolean login(String username, String password);
}