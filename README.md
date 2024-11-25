[![GitHub release](https://img.shields.io/github/release/sismics/play-docker.svg?style=flat-square)](https://github.com/sismics/play-docker/releases/latest)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# play-docker plugin

This plugin adds [Docker](https://www.docker.com/) support to Play! Framework 1 applications.

# How to use

####  Add the dependency to your `dependencies.yml` file

```
require:
    - docker -> docker 1.2.0
    
    # Needed by Docker
    - org.apache.httpcomponents -> httpclient 4.5.3
    - org.apache.httpcomponents -> httpcore 4.4.5

repositories:
    - sismicsNexusRaw:
        type: http
        artifact: "https://nexus.sismics.com/repository/sismics/[module]-[revision].zip"
        contains:
            - docker -> *

```
####  Run the `play deps` command

####  Add the configuration to `application.conf`

```
docker.remote_host=yourdockerhost.com
docker.remote_port=2400
```

#### Connect with socket

```
docker.use_socket=true
```

####  Connect with certificate

```
docker.remote_host=yourdockerhost.com
docker.remote_port=2376
docker.cert_dir=/your/cert/dir
```

####  Basic authentication

Alternately, you can authenticate to the Docker Engine using basic auth:

```
docker.authorization=base64_encoded
```

####  Registry authentication

If you need to authenticate to the Docker Registry, add the following configuration parameters: 

```
docker.registry.username=username
docker.registry.password=password
docker.registry.address=registry_address
```

# Enable the admin console

The admin console allows you to monitor queries in realtime.

Add the following parameter to enable the admin console:

```
docker.console.enabled=true
```

Note: the admin console is enabled by default in Dev mode.

### Secure the admin console

Add the following parameter to secure the admin console

```
docker.console.username=console
docker.console.password=pass1234
```

# License

This software is released under the terms of the Apache License, Version 2.0. See `LICENSE` for more
information or see <https://opensource.org/licenses/Apache-2.0>.
