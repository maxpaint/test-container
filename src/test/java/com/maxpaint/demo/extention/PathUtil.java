package com.maxpaint.demo.extention;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtil {

    public static Path getPath(String resourceName) {
        URL resource = PathUtil.class.getClassLoader().getResource(resourceName);
        if (Files.exists(Paths.get(resource.getPath()))) {
            return Paths.get(resource.getPath());
        }
        throw new RuntimeException("Incorrect resource path: " + resourceName);
    }
}
