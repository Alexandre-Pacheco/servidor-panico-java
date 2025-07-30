package br.ifpb.pdp.patterns.strategy;
import java.util.Random;

public class WifiLocationStrategy implements ILocationStrategy {
    private final String[] locations = {"Biblioteca (2º Andar)", "Laboratório de Informática 5", "Cantina Central"};
    @Override
    public String findLocation() {
        return "Wi-Fi: " + locations[new Random().nextInt(locations.length)];
    }
}
