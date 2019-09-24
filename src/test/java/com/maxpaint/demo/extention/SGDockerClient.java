package com.maxpaint.demo.extention;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SGDockerClient {

    private DefaultDockerClientConfig.Builder defaultConfig = DefaultDockerClientConfig.createDefaultConfigBuilder();
    private DockerClient dockerClient = DockerClientBuilder.getInstance(defaultConfig).build();
    private List<String> containers;

    public SGDockerClient setDefaultConfig(DefaultDockerClientConfig.Builder defaultConfig) {
        this.defaultConfig = defaultConfig;
        return this;
    }

    public SGDockerClient setContainers(List<String> containers) {
        this.containers = containers;
        return this;
    }

    public void run() {
        findContainers().stream()
                        .filter(container -> {
                            final String name = String.join(",", container.getNames())
                                                      .replace("/", "");
                            return containers.contains(name);
                        })
                        .forEach(container -> dockerClient.startContainerCmd(container.getId()).exec());
    }

    public void stop() {
        findContainers().stream()
                        .filter(container -> {
                            final String name = String.join(",", container.getNames())
                                                      .replace("/", "");
                            return containers.contains(name);
                        })
                        .forEach(container -> dockerClient.stopContainerCmd(container.getId()).exec());
    }

    public boolean isRunning() {
        Set<String> activeContainers = findContainersName("running");
        return activeContainers.containsAll(containers);
    }

    public boolean isStopped() {
        Set<String> activeContainers = findContainersName("exited");
        return activeContainers.containsAll(containers);
    }

    private List<Container> findContainers() {
        return dockerClient.listContainersCmd()
                           .withShowSize(true)
                           .withShowAll(true)
                           .exec();
    }

    private Set<String> findContainersName(String state) {
        List<Container> listContainersCmd = dockerClient.listContainersCmd()
                                                        .withShowSize(true)
                                                        .withShowAll(true)
                                                        .withStatusFilter(Collections.singletonList(state))
                                                        .exec();

        return listContainersCmd.stream()
                                .map(container -> String.join(",", container.getNames()))
                                .map(name -> name.replace("/", ""))
                                .collect(Collectors.toSet());
    }
}
