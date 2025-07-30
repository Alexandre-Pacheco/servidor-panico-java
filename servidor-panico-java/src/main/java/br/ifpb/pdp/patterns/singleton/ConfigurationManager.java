package br.ifpb.pdp.patterns.singleton;

public class ConfigurationManager {
    private static ConfigurationManager instance;
    private ConfigurationManager() {}

    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }
}
