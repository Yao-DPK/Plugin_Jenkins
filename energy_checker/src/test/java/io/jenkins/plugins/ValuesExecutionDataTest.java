package io.jenkins.plugins;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ValuesExecutionDataTest {

    private ValuesExecutionData valuesExecutionData;

    @BeforeEach
    void setUp() {
        valuesExecutionData = new ValuesExecutionData();
    }

    @Test
    void testAddAndGetPartData() {
        valuesExecutionData.addPartData(10.0, 100.0, 10.0);

        assertEquals(1, valuesExecutionData.getPartDataList().size());

        ValuesExecutionData.PartData partData = valuesExecutionData.getPartDataList().get(0);
        assertEquals(10.0, partData.getDuration(), 0.001);
        assertEquals(100.0, partData.getJoulesConsumed(), 0.001);
        assertEquals(10.0, partData.getWattsProvided(), 0.001);
    }

    @Test
    void testToJson() {
        valuesExecutionData.addPartData(10.0, 100.0, 10.0);
        String json = valuesExecutionData.toJson();
        assertNotNull(json);
        String expectedJson = "[{\"duration\":10.0,\"joulesConsumed\":100.0,\"wattsProvided\":10.0}]";
        assertEquals(expectedJson, json);
    }

    @Test
    void testEmptyPartDataList() {
        assertEquals(0, valuesExecutionData.getPartDataList().size());
    }

    @Test
    void testActionMethods() {
        assertEquals(null, valuesExecutionData.getIconFileName());
        assertEquals(null, valuesExecutionData.getDisplayName());
        assertEquals(null, valuesExecutionData.getUrlName());
    }
}
