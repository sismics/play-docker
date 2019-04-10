package plugins.docker;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Distribution;
import com.spotify.docker.client.messages.Info;
import helpers.docker.DockerUtil;
import play.Logger;
import play.Play;
import play.PlayPlugin;
import play.mvc.Router;

/**
 * Docker plugin.
 *
 * @author jtremeaux
 */
public class DockerPlugin extends PlayPlugin {
    public boolean mock;

    public String remoteHost;

    public String remotePort;

    public String authorization;

    public String registryUsername;

    public String registryPassword;

    public String registryAddress;

    public boolean consoleEnabled;

    private static final String DOCKER_PREFIX = "docker";

    @Override
    public void onConfigurationRead() {
        mock = Boolean.parseBoolean(Play.configuration.getProperty(DOCKER_PREFIX + ".mock", String.valueOf(Play.mode.isDev())));
        remoteHost = Play.configuration.getProperty(DOCKER_PREFIX + ".remote_host");
        remotePort = Play.configuration.getProperty(DOCKER_PREFIX + ".remote_port");
        authorization = Play.configuration.getProperty(DOCKER_PREFIX + ".authorization");
        registryUsername = Play.configuration.getProperty(DOCKER_PREFIX + ".registry.username");
        registryPassword = Play.configuration.getProperty(DOCKER_PREFIX + ".registry.password");
        registryAddress = Play.configuration.getProperty(DOCKER_PREFIX + ".registry.address");
        consoleEnabled = Boolean.parseBoolean(Play.configuration.getProperty(DOCKER_PREFIX + ".console.enabled", String.valueOf(Play.mode.isDev())));
    }

    @Override
    public void onRoutesLoaded() {
        Router.prependRoute("GET", "/@docker", "Dockers.index");
    }

    public Info getDockerConnection() {
        DockerClient client = DockerUtil.createDockerClient();
        try {
            return client.info();
        } catch (DockerException e) {
            Logger.error(e, "Error connecting to Docker");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getRegistryConnection() {
        DockerClient client = DockerUtil.createDockerClient();
        try {
            return client.auth(DockerUtil.getRegistryAuth());
        } catch (Exception e) {
            Logger.error(e, "Error connecting to Docker registry");
        }
        return -1;
    }

    public Distribution getDistribution(String name) {
        DockerClient client = DockerUtil.createDockerClient();
        try {
            return client.getDistribution(name);
        } catch (Exception e) {
            Logger.error(e, "Error getting digest");
        }
        return null;
    }
}