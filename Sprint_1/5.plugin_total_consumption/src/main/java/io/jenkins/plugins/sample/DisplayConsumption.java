package io.jenkins.plugins.sample;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

public class DisplayConsumption extends Builder implements SimpleBuildStep {

    private final String name;

    // Constructeur avec annotation pour lier la variable avec le formulaire HTML du fichier config.jelly
    @DataBoundConstructor
    public DisplayConsumption(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) {
        // Affiche le contenu du fichier dans la console de build
        System.out.println("1");
        String user_path = System.getProperty("user.dir");
        String path = user_path + "/work/workspace/bash.sh";
        String content = BashScriptExecutor.executeBash(path);
        listener.getLogger().println(content);

        return false;
    }

    @Symbol("displayFileContent")
    @Extension // Cette annotation permet à Jenkins de découvrir cette classe comme une extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        public FormValidation doCheckFilePath(@QueryParameter String value) throws IOException, ServletException {
            if (value.isEmpty()) {
                return FormValidation.error("Veuillez saisir un chemin de fichier");
            }
            if (!Files.exists(Paths.get(value))) {
                return FormValidation.warning("Le fichier n'existe pas");
            }
            return FormValidation.ok();
        }

        @SuppressWarnings("rawtypes")
        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indique que notre builder peut être utilisé avec tous types de projets Jenkins
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Afficher le contenu d'un fichier";
        }
    }
}
