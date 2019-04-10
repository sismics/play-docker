package controllers;

import helpers.docker.BasicAuthHelper;
import play.Play;
import play.mvc.Controller;
import play.mvc.Util;
import plugins.docker.DockerPlugin;

/**
 * Access logs controller.
 *
 * @author jtremeaux
 */
public class Dockers extends Controller {
    public static void index(String distribution) {
        DockerPlugin dockerPlugin = getPluginInstance();
        if (!dockerPlugin.consoleEnabled) {
            notFound();
        }
        checkBasicAuth();
        if (distribution != null) {
            renderArgs.put("distribution", dockerPlugin.getDistribution(distribution));
        }
        render(dockerPlugin);
    }

    @Util
    public static DockerPlugin getPluginInstance() {
        return (DockerPlugin) Play.pluginCollection.getPluginInstance(DockerPlugin.class);
    }

    @Util
    private static void checkBasicAuth() {
        String username = Play.configuration.getProperty("docker.console.username");
        String password = Play.configuration.getProperty("docker.console.password");
        if (!BasicAuthHelper.checkAuthenticationHeaders(request, username, password)) {
            unauthorized("Docker console");
        }
    }
}
