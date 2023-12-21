package org.rock.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/dispatcher")
public class DispatcherController {

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    RestTemplate restTemplate;


    @GetMapping
    public String guitarHero(Model model) {
        model.addAttribute("appName", appName);
        String jSlash = restTemplate.getForObject(
                "http://localhost:9000/", String.class);
        model.addAttribute("imageBytes", jSlash);
        return "index";
    }


}
