package io.jenkins.plugins;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ScriptGetEnergeticValuesTest {

    @Test
    public void testCalculatePowerUsed() {
        double startTime = 0.0;
        double endTime = 2000.0;
        double startConsumption = 100000000.0;
        double endConsumption = 200000000.0;

        double expectedPower = 50.0;
        double power = ScriptGetEnergeticValues.calculatePowerUsed(startTime, endTime, startConsumption, endConsumption);
        assertEquals(expectedPower, power, 0.001);
    }

    @Test
    public void testSetEnergeticValues() {
        double duration = 2.0;
        double startEnergy = 100.0;
        double endEnergy = 200.0;

        ValuesEnergetic expectedValues = new ValuesEnergetic<>(100.0, 50.0);
        ValuesEnergetic values = ScriptGetEnergeticValues.setEnergeticValues(duration, startEnergy, endEnergy);
        assertEquals(expectedValues.getEnergy(), values.getEnergy(), 0.001);
        assertEquals(expectedValues.getPower(), values.getPower(), 0.001);
    }
}
