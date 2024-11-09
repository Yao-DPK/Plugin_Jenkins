package io.jenkins.plugins;

import hudson.model.*;
import hudson.model.listeners.RunListener;

// @Extension
public class ListenerRunBuildInfo extends RunListener<Run<?, ?>> {

    @Override
    public void onCompleted(Run<?, ?> run, TaskListener listener) {
        // Build ID and URL
        String buildId = run.getId();
        String buildUrl = run.getUrl();
        listener.getLogger().println("Build ID: " + buildId);
        listener.getLogger().println("Build URL: " + buildUrl);

        // Build status
        Result result = run.getResult();
        listener.getLogger().println("Build result: " + (result != null ? result.toString() : "N/A"));

        // Build duration
        long duration = run.getDuration();
        listener.getLogger().println("Build duration: " + duration + " ms");

        listener.getLogger().println("Build time: " + run.getTimestamp());
    }
}
