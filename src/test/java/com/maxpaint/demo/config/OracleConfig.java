package com.maxpaint.demo.config;

import com.maxpaint.demo.extention.DockerCompose;
import com.maxpaint.demo.extention.PathUtil;
import com.maxpaint.demo.extention.SGDockerClient;
import oracle.jdbc.pool.OracleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Arrays.asList;
import static org.awaitility.Awaitility.await;

@Configuration
public class OracleConfig {

    SGDockerClient client = new SGDockerClient().setContainers(asList("cass", "oracle", "es"));

    @Primary
    @Bean(initMethod = "start", destroyMethod = "stop")
    public DockerCompose docker() {
        final Path dockerPath = PathUtil.getPath("docker-compose.yml");
        final DockerCompose docker = new DockerCompose(dockerPath);
        //docker.setStart(() -> false);
        //docker.setStop(() -> false);

        docker.setStart(() -> {
            if (client.isStopped()) {
                client.run();
            }
            return true;
        });
        docker.setStop(() -> {
            if (client.isRunning()) {
                client.stop();
            }
            return true;
        });
        return docker;
    }

    @Bean(name = "oracleDataSource")
    @DependsOn("docker")
    public DataSource dataSource() throws SQLException {
        AtomicReference<OracleDataSource> dataSource = new AtomicReference<>();
        await().atMost(60, TimeUnit.SECONDS)
               .timeout(Duration.ofSeconds(5))
               .pollInterval(Duration.ofSeconds(1))
               .until(() -> {
                   dataSource.set(getDataSource());
                   return isValid(dataSource.get());
               });

        return dataSource.get();
    }

    private boolean isValid(DataSource dataSource) {
        try {
            Connection con = dataSource.getConnection();
            return con.isValid(5_000);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private OracleDataSource getDataSource() throws SQLException {
        OracleDataSource ds = new OracleDataSource();
        ds.setURL("jdbc:oracle:thin:@localhost:1521:xe");
        ds.setUser("ssguser");
        ds.setPassword("ssg1234");
        return ds;
    }
}
