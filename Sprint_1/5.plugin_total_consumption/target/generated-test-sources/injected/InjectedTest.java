import java.util.*;
/**
 * Entry point to auto-generated tests (generated by maven-hpi-plugin).
 * If this fails to compile, you are probably using Hudson &lt; 1.327. If so, disable
 * this code generation by configuring maven-hpi-plugin to &lt;disabledTestInjection&gt;true&lt;/disabledTestInjection&gt;.
 */
public class InjectedTest extends junit.framework.TestCase {
  public static junit.framework.Test suite() throws Exception {
    System.out.println("Running tests for "+"io.jenkins.plugins:demo:1.0-SNAPSHOT");
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("basedir","/home/pyke/Documents/ProjetSI/Sprint_1/5.plugin_total_consumption");
    parameters.put("artifactId","demo");
    parameters.put("packaging","hpi");
    parameters.put("outputDirectory","/home/pyke/Documents/ProjetSI/Sprint_1/5.plugin_total_consumption/target/classes");
    parameters.put("testOutputDirectory","/home/pyke/Documents/ProjetSI/Sprint_1/5.plugin_total_consumption/target/test-classes");
    parameters.put("requirePI","true");
    return org.jvnet.hudson.test.PluginAutomaticTestBuilder.build(parameters);
  }
}
