package io.jenkins.plugins;

import hudson.model.InvisibleAction;

public class IdleConsumptionAction extends InvisibleAction {

    private final double idleConsumption;

    public IdleConsumptionAction(double idleConsumption) {
        this.idleConsumption = idleConsumption;
    }

    public double getIdleConsumption() {
        return idleConsumption;
    }
}
