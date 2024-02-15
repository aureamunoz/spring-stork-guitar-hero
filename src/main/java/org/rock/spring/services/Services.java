package org.rock.spring.services;

import com.sun.net.httpserver.HttpServer;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Random;

@Service
public class Services {

    private static final Logger logger = LoggerFactory.getLogger(Services.class);

    @Value("${slash.http-port}")
    int slashPort;

    @Value("${slash.delay-in-ms}")
    int slashDelay;

    @Value("${hendrix.http-port}")
    int hendrixPort;

    @Value("${hendrix.delay-in-ms}")
    int hendrixDelay;

    @Value("${eddie.http-port}")
    int eddiePort;

    @Value("${eddie.failure-ratio}")
    int eddieFailureRatio;

    public void createHttpServer(String rockStar, int rockStarPort) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(rockStarPort), 0);
        server.createContext("/", (exchange -> {
            byte[] imageBytes = new byte[0];
            try {
                imageBytes = Files
                        .readAllBytes(new File(getClass().getResource("/static/" + rockStar).toURI()).toPath());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            String respText = Base64.getEncoder().encodeToString(imageBytes);
            exchange.sendResponseHeaders(200, respText.getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(respText.getBytes());
            output.flush();
            exchange.close();
        }));
        server.setExecutor(null); // creates a default executor
        System.out.println("Listening in port " + rockStarPort);
        server.start();
    }

    @PostConstruct
    public void init() throws IOException, URISyntaxException {

        createHttpServer("JimiHendrix.jpg", hendrixPort);
        createHttpServer("Slash.jpg", slashPort);
        createHttpServer("EddieVanHalen.jpg", eddiePort);

        logger.info("""
                Services Started:
                    - Slash -> port: %d, delay: %dms
                    - Jimi Hendrix -> port: %d, delay: %dms
                    - Eddie Van Halen -> %d, failure ratio: %s
                """, slashPort, slashDelay, hendrixPort, hendrixDelay, eddiePort, eddieFailureRatio + "%");
    }

}
