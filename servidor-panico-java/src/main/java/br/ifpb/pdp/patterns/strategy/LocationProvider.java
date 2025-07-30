package br.ifpb.pdp.patterns.strategy;

public class LocationProvider {
    private ILocationStrategy strategy;

    public LocationProvider() {
        this.strategy = new GPSLocationStrategy();
    }

    public void setStrategy(ILocationStrategy strategy) {
        System.out.println("[Strategy] Estratégia de localização alterada para: " + strategy.getClass().getSimpleName());
        this.strategy = strategy;
    }

    public String getLocation() {
        return strategy.findLocation();
    }
}