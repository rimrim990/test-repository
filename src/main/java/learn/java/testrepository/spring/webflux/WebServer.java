package learn.java.testrepository.spring.webflux;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import learn.java.testrepository.spring.webflux.dto.Hobby;
import learn.java.testrepository.spring.webflux.dto.Person;
import learn.java.testrepository.spring.webflux.dto.WebResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@SpringBootApplication
public class WebServer {

    @RestController
    static class WebController {

        @GetMapping("/api/{id}")
        public WebResponse getById(@PathVariable Long id) {
            log.info("/api/{id} called", id);
            return new WebResponse(id, "api");
        }

        @GetMapping("/api")
        public List<WebResponse> getAll() {
            final List<WebResponse> responses = new LinkedList<>();
            for (int i = 0; i < 5; i++) {
                responses.add(new WebResponse((long) i, "api/" + i));
            }
            return responses;
        }

        @GetMapping("/badRequest/{id}")
        public ResponseEntity<WebResponse> badRequest(@PathVariable Long id) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new WebResponse(id, "badRequest"));
        }

        @GetMapping("/person/{id}")
        public Person getPersonById(@PathVariable Long id) {
            return new Person("person" + id, (int) (20 + id));
        }

        @GetMapping("/person/{id}/hobbies")
        public List<Hobby> getPersonHobbies(@PathVariable Long id) {
            final List<Hobby> hobbies = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                hobbies.add(new Hobby("hobby" + i));
            }
            return hobbies;
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(WebServer.class);
    }
}
