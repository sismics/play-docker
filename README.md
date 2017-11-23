# play-docker plugin

This plugin adds [Docker](https://www.docker.com/) support to Play! Framework 1 applications.

# How to use

####  Add the dependency to your `dependencies.yml` file

```
require:
    - docker -> docker 0.1.0
    # Needed by Docker
    - org.apache.httpcomponents -> httpclient 4.5
    - org.apache.httpcomponents -> httpcore 4.4.5

repositories:
    - sismics:
        type:       http
        artifact:   "http://release.sismics.com/repo/play/[module]-[revision].zip"
        contains:
            - docker -> *

```
####  Run the `play deps` command

####  Add the configuration to `application.conf`

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

# License

This software is released under the terms of the Apache License, Version 2.0. See `LICENSE` for more
information or see <https://opensource.org/licenses/Apache-2.0>.
