package kr.co.manty.example.simpleribbonexample;

import kr.co.manty.example.simpleribbonexample.configuration.HiServiceLoadbalancerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootApplication
@RibbonClient(name="hi-service", configuration = HiServiceLoadbalancerConfiguration.class)
public class SimpleRibbonExampleApplication {

    private final RestTemplate restTemplate;

    public SimpleRibbonExampleApplication(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(SimpleRibbonExampleApplication.class, args);
    }
    
    
    @Bean
    public ApplicationListener<ApplicationReadyEvent> listener() {
        return it -> {
            String url = UriComponentsBuilder.fromHttpUrl("http://hi-service/v1/hi")
                                             .queryParam("name", "zbum")
                                             .build()
                                             .toUriString();
            String response = restTemplate.getForObject(url, String.class);
        };
    }

}
