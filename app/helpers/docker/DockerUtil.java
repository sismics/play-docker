package helpers.docker;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerCertificates;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.auth.FixedRegistryAuthSupplier;
import com.spotify.docker.client.messages.RegistryAuth;
import com.spotify.docker.client.messages.RegistryConfigs;
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
            if (getDockerCertDir() != null) {
                builder
                        .uri(URI.create("https://" + getDockerRemoteHost() + ":" + getDockerRemotePort("2376")))
                        .dockerCertificates(DockerCertificates.builder()
                                .dockerCertPath(Paths.get(getDockerCertDir()))
                                .build().orNull());
            } else {
                builder.uri(URI.create("http://" + getDockerRemoteHost() + ":" + getDockerRemotePort("2400")));
                if (getDockerAuthorization() != null) {
                    builder.header("Authorization", "Basic " + getDockerAuthorization());
                }
            }
            if (getRegistryUsername() != null && getRegistryPassword() != null && getRegistryAddress() != null) {
                builder.registryAuthSupplier(new FixedRegistryAuthSupplier(getRegistryAuth(), null));
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
