package io.jenkins.plugins;

import hudson.model.Run;
import jenkins.model.RunAction2;

import java.util.ArrayList;
import java.util.List;

public class DisplayChart implements RunAction2 {

    private transient Run run;
    private List<Double> energyHistory;
    private List<Double> powerHistory;

    @Override
    public String getIconFileName() {
        return "/plugin/energy_checker/images/chart-histogram.png";
    }

    @Override
    public String getDisplayName() {
        return "Chart Display";
    }

    @Override
    public String getUrlName() {
        return "chart";
    }

    @Override
    public void onAttached(Run<?, ?> run) {
        this.run = run;
        VariablesConsumptionsPreviousBuild action = run.getAction(VariablesConsumptionsPreviousBuild.class);
        if (action != null) {
            setEnergyHistory(action.getEnergyConsumptions());
            setPowerHistory(action.getPowerProvisions());
        }
    }

    @Override
    public void onLoad(Run<?, ?> run) {
        this.run = run;
    }

    public Run getRun() {
        return run;
    }

    public void setEnergyHistory(List<Double> energyHistory) {
        this.energyHistory = energyHistory;
    }

    public void setPowerHistory(List<Double> powerHistory) {
        this.powerHistory = powerHistory;
    }

    public String getEnergyHistoryAsJsonAll() {
        return energyHistory != null ? energyHistory.toString() : "[]";
    }

    public String getPowerHistoryAsJsonAll() {
        return powerHistory != null ? powerHistory.toString() : "[]";
    }

    public String getLabelsAsJson() {
        int dataLength = energyHistory.size();
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < dataLength; i++) {
            if (i == dataLength - 1) {
                labels.add("'CurrentBuild'");
            } else if (i == dataLength - 2) {
                labels.add("'PreviousBuild'");
            } else {
                labels.add("'CurrentBuild-" + (dataLength - i - 1) + "'");
            }
        }
        return labels.toString();
    }

    public String getStageDataAsJson() {
        return run.getAction(ValuesStageData.class).toJson() != null ? run.getAction(ValuesStageData.class).toJson() : "[]";
    }

    public String getExecutionDataAsJson() {
        return run.getAction(ValuesExecutionData.class).toJson() != null ? run.getAction(ValuesExecutionData.class).toJson() : "[]";
    }
}
