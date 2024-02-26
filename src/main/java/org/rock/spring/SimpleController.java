package org.rock.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SimpleController {
    @Value("${spring.application.name}")
    String appName;

    @Value("${stork.guitar-hero-service.load-balancer.type:round-robin}")
    String lbStrategy;

    @Value("${stork.guitar-hero-service.service-discovery.type:static}")
    String sdStrategy;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("serviceDiscovery", sdStrategy);
        model.addAttribute("loadBalancer", lbStrategy);
        return "index";
    }
}
