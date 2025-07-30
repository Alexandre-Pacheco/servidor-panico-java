package br.ifpb.pdp.patterns.strategy;
import java.util.Random;

public class GPSLocationStrategy implements ILocationStrategy {
    private final String[] locations = {"Bloco A (Entrada)", "Estacionamento Principal", "Ginásio de Esportes"};
    @Override
    public String findLocation() {
        return "GPS: " + locations[new Random().nextInt(locations.length)];
    }
}
