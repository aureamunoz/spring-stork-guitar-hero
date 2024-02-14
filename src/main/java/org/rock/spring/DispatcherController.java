package org.rock.spring;

import io.smallrye.mutiny.Uni;
import io.smallrye.stork.Stork;
import io.smallrye.stork.api.ServiceInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/dispatcher")
public class DispatcherController {

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    Stork stork;


    @GetMapping
    public String guitarHero(Model model) throws URISyntaxException, MalformedURLException {
        model.addAttribute("appName", appName);
        List<String> rockStarImages = new ArrayList<>();

        for(int i=1; i<=10; i++) {
            ServiceInstance serviceInstance = stork.getService("guitar-hero-service").selectInstance().await().atMost(Duration.ofSeconds(5));

        String url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(serviceInstance.getHost())
                .port(serviceInstance.getPort())
                .path(serviceInstance.getPath().orElse(""))
                .build()
                .toUri()
                .toURL().toString();

        System.out.println(" ---------- Selected Rock Start: " + url);

            String rockStar = restTemplate.getForObject(
                    url, String.class);
            rockStarImages.add(rockStar);
        }
        model.addAttribute("imageBytes", rockStarImages);
        return "indexfragment :: index_frag";
    }


}
