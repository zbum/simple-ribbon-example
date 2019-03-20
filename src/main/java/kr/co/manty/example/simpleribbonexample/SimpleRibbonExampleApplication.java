package kr.co.manty.example.simpleribbonexample;

import kr.co.manty.example.simpleribbonexample.configuration.HiServiceLoadbalancerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static java.lang.String.format;

@SpringBootApplication
@RibbonClient(name="hi-service", configuration = HiServiceLoadbalancerConfiguration.class)
public class SimpleRibbonExampleApplication {

    private final RestTemplate restTemplate;
    private final LoadBalancerClient loadBalancer;

    public SimpleRibbonExampleApplication(RestTemplate restTemplate, LoadBalancerClient loadBalancer) {
        this.restTemplate = restTemplate;
        this.loadBalancer = loadBalancer;
    }

    public static void main(String[] args) {
        SpringApplication.run(SimpleRibbonExampleApplication.class, args);
    }
    
    
    @Bean
    public ApplicationListener<ApplicationReadyEvent> listener1() {
        return it -> {
            String url = UriComponentsBuilder.fromHttpUrl("http://hi-service/")
                                             .build()
                                             .toUriString();
            String response = restTemplate.getForObject(url, String.class);
            System.out.println(response);
        };
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> listener2() {
        return it -> {
            ServiceInstance instance = loadBalancer.choose("hi-service");
            System.out.println(format("%s:%d", instance.getHost(), instance.getPort()));
        };
    }

}
