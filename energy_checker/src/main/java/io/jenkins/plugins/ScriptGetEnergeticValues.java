package io.jenkins.plugins;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ScriptGetEnergeticValues {

    private static double idleConsumption = 0;
    private static double idlePowerProvision = 2;

    public static void measureIdleConsumption() {
        idleConsumption = readRAPL();
    }

    public static double calculatePowerUsed(
            double startTime, double endTime, double startConsumption, double endConsumption) {
        double consumption = endConsumption - startConsumption;
        double duration = (endTime - startTime) / 1000;
        return consumption / duration / 1000000;
    }

    public static double calculatePowerUsed(double duration, double startConsumption, double endConsumption) {
        double consumption = endConsumption - startConsumption;
        return consumption / duration / 1000000;
    }

    @SuppressFBWarnings("DMI_HARDCODED_ABSOLUTE_FILENAME")
    public static double readRAPL() {
        String chemin = "/sys/devices/virtual/powercap/intel-rapl/intel-rapl:0/energy_uj";
        try (BufferedReader lecteur =
                new BufferedReader(new InputStreamReader(new FileInputStream(chemin), StandardCharsets.UTF_8))) {
            String ligne = lecteur.readLine();
            if (ligne != null) {
                return Double.parseDouble(ligne) / 1000000;
            }
        } catch (IOException e) {
            System.err.println("Erreur de lecture du fichier : " + e.getMessage());
        }
        return 0;
    }

    public static ValuesEnergetic setEnergeticValues(double duration, double startEnergy, double endEnergy) {
        double energy = endEnergy - startEnergy;
        double power = energy / duration;
        return new ValuesEnergetic<>(energy, power);
    }
}
