package io.jenkins.plugins;

public class ValuesEnergetic<E, P> {
    public final double energy;
    public final double power;

    public ValuesEnergetic(double energy, double power) {
        this.energy = energy;
        this.power = power;
    }

    @Override
    public String toString() {
        return "(energy=" + energy + ", power=" + power + ")";
    }

    public double getEnergy() {
        return energy;
    }

    public double getPower() {
        return power;
    }
}
