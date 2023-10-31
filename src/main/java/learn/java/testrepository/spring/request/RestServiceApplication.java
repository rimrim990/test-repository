package learn.java.testrepository.spring.request;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class RestServiceApplication {

    @RestController
    public class RestCallController {

        private final RestTemplate rt = new RestTemplate();
        private final WebClient client = WebClient.builder()
            .baseUrl("http://localhost:8081")
            .build();

        @GetMapping("/rest")
        public String restCall(@RequestParam("idx") Integer idx) {
            // 2초 동안 RestTemplate 블로킹
            final String res = rt.getForObject("http://localhost:8081/remote?req={req}", String.class,
                "hello " + idx);
            return res;
        }

        @GetMapping("/rest-async")
        public Flux<String> restAsyncCall(@RequestParam("idx") Integer idx) {
            // 논블로킹
            return client.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/remote")
                    .queryParam("req", "hello " + idx)
                    .build()
                )
                .retrieve()
                .bodyToFlux(String.class);
        }

        @GetMapping("/rest-async-callback")
        public Flux<String> restAsyncCallback(@RequestParam("idx") Integer idx) {
            // 논블로킹
            return client.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/remote")
                    .queryParam("req", "hello " + idx)
                    .build()
                )
                .retrieve()
                .bodyToFlux(String.class)
                .map(res -> res + " /work");
        }

        @GetMapping("/rest-async-chain")
        public Flux<String> restAsyncCallChain(@RequestParam("idx") Integer idx) {
            // 논블로킹
            return client.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/remote")
                    .queryParam("req", "hello " + idx)
                    .build()
                )
                .retrieve()
                .bodyToFlux(String.class)
                .flatMap(res ->
                   client.get()
                       .uri(uriBuilder -> uriBuilder
                           .path("/remote2")
                           .queryParam("req", "hello " + res)
                           .build()
                       )
                       .retrieve()
                       .bodyToFlux(String.class)
                );
        }
    }

    public static void main(String[] args) {
        System.setProperty("server.tomcat.threads.max", "1");
        SpringApplication.run(RestServiceApplication.class, args);
    }
}
