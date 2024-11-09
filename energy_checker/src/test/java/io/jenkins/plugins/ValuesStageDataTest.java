package io.jenkins.plugins;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ValuesStageDataTest {

    private ValuesStageData valuesStageData;

    @BeforeEach
    void setUp() {
        valuesStageData = new ValuesStageData();
    }

    @Test
    void testAddAndGetStageData() {
        valuesStageData.addStageData("TestStage", 10.0, 100.0, 10.0);

        assertEquals(1, valuesStageData.getStageDataList().size());

        ValuesStageData.StageData stageData = valuesStageData.getStageDataList().get(0);
        assertEquals("TestStage", stageData.getStageName());
        assertEquals(10.0, stageData.getDuration(), 0.001);
        assertEquals(100.0, stageData.getJoulesConsumed(), 0.001);
        assertEquals(10.0, stageData.getWattsProvided(), 0.001);
    }

    @Test
    void testToJson() {
        valuesStageData.addStageData("TestStage", 10.0, 100.0, 10.0);
        String json = valuesStageData.toJson();
        assertNotNull(json);
        String expectedJson = "[{\"stageName\":\"TestStage\",\"duration\":10.0,\"joulesConsumed\":100.0,\"wattsProvided\":10.0}]";
        assertEquals(expectedJson, json);
    }

    @Test
    void testEmptyStageDataList() {
        assertEquals(0, valuesStageData.getStageDataList().size());
    }

    @Test
    void testActionMethods() {
        assertEquals(null, valuesStageData.getIconFileName());
        assertEquals(null, valuesStageData.getDisplayName());
        assertEquals(null, valuesStageData.getUrlName());
    }
}
