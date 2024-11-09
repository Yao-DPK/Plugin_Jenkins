package io.jenkins.plugins;

import hudson.model.Action;
import java.util.ArrayList;
import java.util.List;

public class VariablesConsumptionsPreviousBuild implements Action {
    private List<Double> energyConsumptions;
    private List<Double> powerProvisions;

    public VariablesConsumptionsPreviousBuild(List<Double> energyConsumptions, List<Double> powerProvisions) {
        this.energyConsumptions =
                (energyConsumptions != null) ? new ArrayList<>(energyConsumptions) : new ArrayList<>();
        this.powerProvisions = (powerProvisions != null) ? new ArrayList<>(powerProvisions) : new ArrayList<>();
    }

    public synchronized void addEnergyConsumption(double energyConsumed) {
        this.energyConsumptions.add(energyConsumed);
    }

    public synchronized void addWattUsage(double wattConsumed) {
        this.powerProvisions.add(wattConsumed);
    }

    public List<Double> getEnergyConsumptions() {
        return new ArrayList<>(energyConsumptions);
    }

    public List<Double> getPowerProvisions() {
        return new ArrayList<>(powerProvisions);
    }

    @Override
    public String getIconFileName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "Consumption History";
    }

    @Override
    public String getUrlName() {
        return "consumption-history";
    }
}
