package helpers.docker;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerCertificates;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.auth.FixedRegistryAuthSupplier;
import com.spotify.docker.client.messages.RegistryAuth;
import play.Logger;
import play.Play;

import java.net.URI;
import java.nio.file.Paths;

/**
 * Docker utilities.
 *
 * @author jtremeaux
 */
public class DockerUtil {

    /**
     * Create a new Docker client from Play! parameters.
     *
     * @return New Docker client
     */
    public static DockerClient createDockerClient() {
        try {
            DefaultDockerClient.Builder builder = DefaultDockerClient.builder();
            if (isUseSocket()) {
                String socketUri = "unix:///var/run/docker.sock";
                Logger.debug("Using docker socket: " + socketUri);
                builder.uri(URI.create(socketUri));
            } else {
                if (getDockerCertDir() != null) {
                    Logger.debug("Using docker certificate directory: " + getDockerCertDir());
                    builder
                            .uri(URI.create("https://" + getDockerRemoteHost() + ":" + getDockerRemotePort("2376")))
                            .dockerCertificates(DockerCertificates.builder()
                                    .dockerCertPath(Paths.get(getDockerCertDir()))
                                    .build().orNull());
                } else {
                    Logger.debug("Using docker without certificates");
                    builder.uri(URI.create("http://" + getDockerRemoteHost() + ":" + getDockerRemotePort("2400")));
                    if (getDockerAuthorization() != null) {
                        Logger.debug("Using docker with basic authorization");
                        builder.header("Authorization", "Basic " + getDockerAuthorization());
                    }
                }
                if (getRegistryUsername() != null && getRegistryPassword() != null && getRegistryAddress() != null) {
                    builder.registryAuthSupplier(new FixedRegistryAuthSupplier(getRegistryAuth(), null));
                }
            }
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException("Error building Docker Client", e);
        }
    }

    public static RegistryAuth getRegistryAuth() {
        return RegistryAuth.builder()
                .username(getRegistryUsername())
                .password(getRegistryPassword())
                .serverAddress(getRegistryAddress())
                .build();
    }

    private static String getDockerCertDir() {
        return Play.configuration.getProperty("docker.cert_dir");
    }

    private static String getDockerRemoteHost() {
        return Play.configuration.getProperty("docker.remote_host");
    }

    private static String getDockerRemotePort(String defaultPort) {
        return Play.configuration.getProperty("docker.remote_port", defaultPort);
    }

    private static boolean isUseSocket() {
        return Boolean.parseBoolean(Play.configuration.getProperty("docker.use_socket"));
    }

    private static String getDockerAuthorization() {
        return Play.configuration.getProperty("docker.authorization");
    }

    private static String getRegistryUsername() {
        return Play.configuration.getProperty("docker.registry.username");
    }

    private static String getRegistryPassword() {
        return Play.configuration.getProperty("docker.registry.password");
    }

    private static String getRegistryAddress() {
        return Play.configuration.getProperty("docker.registry.address");
    }
}
