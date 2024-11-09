package io.jenkins.plugins;

import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.workflow.flow.FlowExecution;
import org.jenkinsci.plugins.workflow.flow.FlowExecutionListener;

import java.io.IOException;

@Extension
public class ListenerExecution extends FlowExecutionListener {

    private volatile boolean isRunning = false;
    double previousPartTime;
    double joulesConsumedPrevious;
    boolean firstPart = true;


    @Override
    public void onRunning(FlowExecution execution) {
        double startRunning = System.currentTimeMillis();
        double joulesStartRunning = ScriptGetEnergeticValues.readRAPL();
        firstPart = true;


        Run<?, ?> run = null;
        try {
            run = (Run<?, ?>) execution.getOwner().getExecutable();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            //execution.getOwner().getListener().getLogger().println("----------------\nLaunching is running\n----------------");
        }catch (Exception e){e.printStackTrace();}
        isRunning = true;

        ValuesExecutionData action = run.getAction(ValuesExecutionData.class);

        while (isRunning) {
            double currentPartTime = (System.currentTimeMillis() - startRunning)/1000;
            double joulesConsumed = ScriptGetEnergeticValues.readRAPL() - joulesStartRunning;
            if (!firstPart) {
                double duration = currentPartTime - previousPartTime;
                double deltaJoules = joulesConsumed - joulesConsumedPrevious;
                double wattProvidedPrevious = deltaJoules / duration;
                try {
                    //execution.getOwner().getListener().getLogger().println("Running " + String.format("%.3f", duration) + " seconds, provided " + wattProvidedPrevious + " watts and consumed " + deltaJoules + " joules");
                    //execution.getOwner().getListener().getLogger().println("duration : " + duration + ", at time : " + System.currentTimeMillis());
                    action.addPartData(duration, deltaJoules, wattProvidedPrevious);
                } catch (Exception e) {e.printStackTrace();}

            }
            try {
                //execution.getOwner().getListener().getLogger().println("Part started at: " + String.format("%.3f", currentPartTime) + " seconds");
            } catch (Exception e) {
                e.printStackTrace();
            }
            previousPartTime = currentPartTime;
            joulesConsumedPrevious = joulesConsumed;
            firstPart = false;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    @Override
    public void onCompleted(FlowExecution execution) {
        System.out.println("Pipeline completed");
        try {
            CompletionLatch.getInstance().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isRunning = false;
    }

    @Override
    public void onResumed(FlowExecution execution) {
        System.out.println("Pipeline resumed");
    }
}
