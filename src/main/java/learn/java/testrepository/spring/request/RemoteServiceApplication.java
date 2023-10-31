package learn.java.testrepository.spring.request;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class RemoteServiceApplication {

    @RestController
    public static class RemoteController {
        @GetMapping("/remote")
        public String remoteCall(@RequestParam("req") String req) throws InterruptedException {
            Thread.sleep(2000);
            return req + " /service";
        }

        @GetMapping("/remote2")
        public String remoteCall2(@RequestParam("req") String req) throws InterruptedException {
            Thread.sleep(2000);
            return req + " /service2";
        }

    }

    public static void main(String[] args) {
        System.setProperty("server.port", "8081");
        System.setProperty("server.tomcat.threads.max", "1000");
        SpringApplication.run(RemoteServiceApplication.class, args);
    }
}
