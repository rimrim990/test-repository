package learn.java.testrepository.spring.circuit;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class BookstoreController {

    @RequestMapping(value = "/recommend")
    public Mono<String> readingList() {
        return Mono.just("외부 API 호출 성공");
    }
}
