package io.jenkins.plugins;

import hudson.model.Action;

public class VariablesConsumptionAction implements Action {
    private final double startTime;
    private final double startConsumption;
    private double energyConsumed;
    private double powerUsed;

    public VariablesConsumptionAction(double startTime, double startConsumption) {
        this.startTime = startTime;
        this.startConsumption = startConsumption;
    }

    public double getEnergyConsumed() {
        return energyConsumed;
    }

    public double getPowerUsed() {
        return powerUsed;
    }

    public void setEnergyConsumed(double energyConsumed) {
        this.energyConsumed = energyConsumed;
    }

    public void setPowerUsed(double powerUsed) {
        this.powerUsed = powerUsed;
    }

    public double getStartTime() {
        return startTime;
    }

    public double getStartConsumption() {
        return startConsumption;
    }

    @Override
    public String getIconFileName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "Energy Monitoring";
    }

    @Override
    public String getUrlName() {
        return null;
    }
}
