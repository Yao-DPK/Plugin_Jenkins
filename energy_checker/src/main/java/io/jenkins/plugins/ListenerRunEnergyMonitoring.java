package io.jenkins.plugins;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Extension
public class ListenerRunEnergyMonitoring extends RunListener<Run<?, ?>> {

  Instant Starttime;

  Instant get_starttime() {
    return Starttime;
  }

  @Override
  public void onInitialize(@NonNull Run<?, ?> run) {
    run.addAction(
      new VariablesConsumptionAction(
        System.currentTimeMillis(),
        ScriptGetEnergeticValues.lectureConsommation()
      )
    );
  }

  @Override
  public void onStarted(Run<?, ?> run, TaskListener listener) {
    super.onStarted(run, listener);
    Starttime = Instant.ofEpochMilli(System.currentTimeMillis());
    listener
      .getLogger()
      .println(
        "Début de la surveillance de la consommation d'énergie. (onStarted)"
      );
    listener
      .getLogger()
      .println(
        "Temps à onInitialize() : " +
        run.getAction(VariablesConsumptionAction.class).getStartTime()
      );
    listener
      .getLogger()
      .println(
        "Temps actuel : " + Instant.ofEpochMilli(System.currentTimeMillis())
      );
  }

  @Override
  public void onCompleted(Run<?, ?> run, @NonNull TaskListener listener) {
    listener.getLogger().println("onCompleted() launched");
    VariablesConsumptionAction action = run.getAction(
      VariablesConsumptionAction.class
    );
    String host = "127.0.0.1";
    String port = "8086"; 
    String database = "power_consumption"; 
    String username = "admin"; 
    String password = "password"; 

    if (action != null) {
      String Date = Starttime.toString();
      double power_consumption = InfluxDBData.processInfluxDBData(
        Date,
        host,
        port,
        database,
        username,
        password
      )
        .power;
      double energy_consumption = InfluxDBData.processInfluxDBData(
        Date,
        host,
        port,
        database,
        username,
        password
      )
        .energy;
      listener.getLogger().println("startTime = " + Starttime);

      listener
        .getLogger()
        .println(
          "Consommation d'énergie pendant le build : " +
          energy_consumption +
          " Joules"
        );
      listener
        .getLogger()
        .println(
          "Puissance mobilisé lors du build : " + power_consumption + " Watts"
        );
      action.setEnergyConsumed(energy_consumption);
      action.setPowerUsed(power_consumption);
      double previousBuildConsumption = getPreviousBuildEnergyConsumed(run);
      double previousBuildProvision = getPreviousBuildPowerProvided(run);
      listener
        .getLogger()
        .println("previousBuildConsumption = " + previousBuildConsumption);
      listener
        .getLogger()
        .println("previousBuildProvision = " + previousBuildProvision);

      List<Double> previousEnergies = getPreviousBuildsEnergy(run);
      List<Double> previousPowers = getPreviousBuildsPower(run);
      VariablesConsumptionsPreviousBuild historyConsumptions = new VariablesConsumptionsPreviousBuild(
        previousEnergies,
        previousPowers
      );
      run.addAction(historyConsumptions);
      historyConsumptions.addEnergyConsumption(energy_consumption);
      historyConsumptions.addWattUsage(power_consumption);
      listener
        .getLogger()
        .println(
          "History of Energy Consumptions Updated: " +
          historyConsumptions.getEnergyConsumptions()
        );
      listener
        .getLogger()
        .println(
          "History of Power Usages Updated: " +
          historyConsumptions.getPowerProvisions()
        );

      listener.getLogger().println("Launching chart");
      run.addAction(new DisplayChart());
      CompletionLatch.getInstance().countDown();
    }
  }

  @Override
  public void onFinalized(@NonNull Run<?, ?> run) {}

  public double getPreviousBuildEnergyConsumed(Run<?, ?> currentRun) {
    Run<?, ?> previousBuild = currentRun.getPreviousBuild();
    if (previousBuild != null) {
      VariablesConsumptionAction action = previousBuild.getAction(
        VariablesConsumptionAction.class
      );
      if (action != null) {
        return action.getEnergyConsumed();
      }
    }
    return 0;
  }

  public double getPreviousBuildPowerProvided(Run<?, ?> currentRun) {
    Run<?, ?> previousBuild = currentRun.getPreviousBuild();
    if (previousBuild != null) {
      VariablesConsumptionAction action = previousBuild.getAction(
        VariablesConsumptionAction.class
      );
      if (action != null) {
        return action.getPowerUsed();
      }
    }
    return 0;
  }

  public List<Double> getPreviousBuildsEnergy(Run<?, ?> currentRun) {
    Run<?, ?> previousBuild = currentRun.getPreviousBuild();
    if (previousBuild != null) {
      VariablesConsumptionsPreviousBuild action = previousBuild.getAction(
        VariablesConsumptionsPreviousBuild.class
      );
      if (action != null) {
        return action.getEnergyConsumptions();
      }
    }
    return Collections.emptyList();
  }

  public List<Double> getPreviousBuildsPower(Run<?, ?> currentRun) {
    Run<?, ?> previousBuild = currentRun.getPreviousBuild();
    if (previousBuild != null) {
      VariablesConsumptionsPreviousBuild action = previousBuild.getAction(
        VariablesConsumptionsPreviousBuild.class
      );
      if (action != null) {
        return action.getPowerProvisions();
      }
    }
    return Collections.emptyList();
  }
}
