package com.maxpaint.demo.extention;

import org.apache.commons.lang.SystemUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.testcontainers.utility.DockerLoggerFactory;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;
import org.zeroturnaround.exec.stream.slf4j.Slf4jStream;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class DockerCompose {

    private static final String COMPOSE_EXECUTABLE =
        SystemUtils.IS_OS_WINDOWS ? "docker-compose.exe" : "docker-compose";

    private final File pwd;
    private final String dockerCompose;
    private Supplier<Boolean> stop = () -> defaultStop();
    private Supplier<Boolean> start = () -> defaultStart();

    public DockerCompose(Path dockerComposePath) {
        final Map<String, File> pathAndName = getPathAndName(dockerComposePath);
        final Map.Entry<String, File> pair = pathAndName.entrySet().iterator().next();
        this.dockerCompose = pair.getKey();
        this.pwd = pair.getValue();
    }

    public boolean start() {
        return start.get();
    }

    public boolean stop() {
        return stop.get();
    }

    public DockerCompose setStop(Supplier<Boolean> stop) {
        this.stop = stop;
        return this;
    }

    public DockerCompose setStart(Supplier<Boolean> start) {
        this.start = start;
        return this;
    }

    private boolean defaultStart() {
        String command = format("docker-compose -f %s up", dockerCompose);
        execAsync(command);
        return true;
    }

    private boolean defaultStop() {
        String stopCommand = "docker-compose stop";
        try {
            exec(stopCommand);
        } catch (IOException | InterruptedException e) {
            logger().error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Nullable
    private Future<ProcessResult> execAsync(String commandLine) {
        try {
            return buildCommand(commandLine).start()
                                            .getFuture();
        } catch (IOException e) {
            logger().error(e.getMessage(), e);
        }

        return null;
    }

    private void exec(String commandLine) throws IOException, InterruptedException {
        buildCommand(commandLine)
            .destroyOnExit()
            .executeNoTimeout();
    }

    private ProcessExecutor buildCommand(String commandLine) {
        logger().info("run command: " + commandLine);
        List<String> commands = Stream.of(commandLine.split(" "))
                                      .collect(toList());
        return new ProcessExecutor()
            .command(commands)
            .redirectOutput(Slf4jStream.of(logger()).asInfo())
            .redirectError(Slf4jStream.of(logger()).asInfo())
            .environment(System.getenv())
            .directory(pwd)
            .exitValueNormal();
    }

    private Map<String, File> getPathAndName(Path dockerComposePath) {
        Map<String, File> pair = new HashMap<>();
        String fileName = dockerComposePath.getFileName().toString();
        File workingDir = dockerComposePath.getParent().toFile();
        pair.put(fileName, workingDir);
        return pair;
    }

    private Logger logger() {
        return DockerLoggerFactory.getLogger(COMPOSE_EXECUTABLE);
    }
}
