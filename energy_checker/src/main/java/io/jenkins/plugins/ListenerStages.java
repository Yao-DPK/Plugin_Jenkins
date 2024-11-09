package io.jenkins.plugins;

import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.workflow.flow.GraphListener;
import org.jenkinsci.plugins.workflow.graph.FlowNode;

import java.io.IOException;

@Extension
public class ListenerStages implements GraphListener {

    double timeStartOfClass = System.currentTimeMillis();
    double joulesStartOfClass = ScriptGetEnergeticValues.readRAPL();
    boolean firstNode = true;
    double startTimePrevious;
    String stageNamePrevious;
    double joulesConsumedPrevious;

    @Override
    public void onNewHead(FlowNode node) {
        if (node != null) {
            Run<?, ?> run = null;
            try {
                run = (Run<?, ?>) node.getExecution().getOwner().getExecutable();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            String stageName = node.getDisplayName();
            double startTime = (System.currentTimeMillis() - timeStartOfClass) / 1000;
            double joulesConsumed = ScriptGetEnergeticValues.readRAPL() - joulesStartOfClass;
            if (!firstNode) {
                double duration = startTime - startTimePrevious;
                double deltaJoules = joulesConsumed - joulesConsumedPrevious;
                double wattProvidedPrevious = deltaJoules / duration;

                ValuesStageData action = run.getAction(ValuesStageData.class);

                try {
                    TaskListener listener = node.getExecution().getOwner().getListener();
                    listener.getLogger().println("Stage '" + stageNamePrevious + "' lasted for " + String.format("%.3f", duration) + " seconds, provided " + wattProvidedPrevious + " watts and consumed " + deltaJoules + " joules");

                    action.addStageData(stageNamePrevious, duration, deltaJoules, wattProvidedPrevious);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Stage '" + stageName + "' started at: " + startTime);
            try {
                TaskListener listener = node.getExecution().getOwner().getListener();
                listener.getLogger().println("Stage '" + stageName + "' started at: " + String.format("%.3f", startTime) + " seconds");
            } catch (Exception e) {
                e.printStackTrace();
            }
            startTimePrevious = startTime;
            stageNamePrevious = stageName;
            joulesConsumedPrevious = joulesConsumed;
            firstNode = false;
        }
    }
}
