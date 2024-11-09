package io.jenkins.plugins;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class VariablesConsumptionActionTest {

    private VariablesConsumptionAction action;

    @BeforeEach
    void setUp() {
        action = new VariablesConsumptionAction(1000L, 500.0);
    }

    @Test
    void testGetStartTime() {
        assertEquals(1000L, action.getStartTime());
    }

    @Test
    void testGetStartConsumption() {
        assertEquals(500.0, action.getStartConsumption(), 0.001);
    }

    @Test
    void testSetAndGetEnergyConsumed() {
        action.setEnergyConsumed(200.0);
        assertEquals(200.0, action.getEnergyConsumed(), 0.001);
    }

    @Test
    void testSetAndGetPowerUsed() {
        action.setPowerUsed(50.0);
        assertEquals(50.0, action.getPowerUsed(), 0.001);
    }
}
