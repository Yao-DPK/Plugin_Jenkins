package io.jenkins.plugins;

import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;

//@Extension
public class PreBuildListener extends RunListener<Run<?, ?>> {

    @Override
    public void onStarted(Run<?, ?> run, TaskListener listener) {
        super.onStarted(run, listener);

        double startIdleConsumption = ScriptGetEnergeticValues.readRAPL();

        run.addAction(new IdleConsumptionAction(startIdleConsumption));

        listener.getLogger().println("Idle consumption measured: " + startIdleConsumption + " Joules");
    }
}
