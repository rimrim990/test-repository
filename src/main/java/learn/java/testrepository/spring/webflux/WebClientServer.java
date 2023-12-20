package learn.java.testrepository.spring.webflux;

import learn.java.testrepository.spring.webflux.dto.WebResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@SpringBootApplication
public class WebClientServer {

    @RestController
    static class WebController {

        private final WebClient webClient = WebClient.create("http://localhost:8080");

        @GetMapping("/web-client/{id}")
        public Mono<WebResponse> getById(@PathVariable Long id) {
            log.info("/web-client/{id} called", id);
            return webClient.get().uri("/api/{id}", id)
                .retrieve()
                .bodyToMono(WebResponse.class)
                .log()
                .map(res -> new WebResponse(res.getId(), res.getName() + " webClient"));
        }
    }

    public static void main(String[] args) {
        System.setProperty("server.port", "8081");
        SpringApplication.run(WebClientServer.class);
    }
}
