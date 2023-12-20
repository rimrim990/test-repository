package learn.java.testrepository.spring.webflux;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import learn.java.testrepository.spring.webflux.dto.Hobby;
import learn.java.testrepository.spring.webflux.dto.Person;
import learn.java.testrepository.spring.webflux.dto.WebResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@DisplayName("WebClient 학습 테스트")
public class WebClientTest {

    private static final String SERVER_URL = "http://localhost:8080";

    private final WebClient client = WebClient.create(SERVER_URL);

    @Test
    @DisplayName("서버로부터 응답을 받아온다.")
    void retrieve() {
        // given
        final Long id = 1L;

        // when
        final Mono<WebResponse> response = client.get()
            .uri("/api/{id}", id).accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(WebResponse.class);

        // then
        final WebResponse result = response.block();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("api");
    }

    @Test
    @DisplayName("4xx 상태코드를 반환하면 예외를 던진다.")
    void badRequest_throwException() {
        // given
        final Long id = 1L;

        // when
        final Mono<WebResponse> response = client.get()
            .uri("/badRequest/{id}", id).accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(WebResponse.class);

        // then
        assertThatThrownBy(response::block)
            .isInstanceOf(WebClientResponseException.class);
    }

    @Test
    @DisplayName("4xx 상태코드를 핸들링한다.")
    void badRequest_handleException() {
        // given
        final Long id = 1L;
        final String errorMessage = "API Request Failed!";

        // when
        final Mono<WebResponse> response = client.get()
            .uri("/badRequest/{id}", id).accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError,
                res -> Mono.error(new IllegalStateException(errorMessage))
            )
            .bodyToMono(WebResponse.class);

        // then
        assertThatThrownBy(response::block)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage(errorMessage);
    }

    @Test
    @DisplayName("WebClient를 블로킹 방식으로 사용한다.")
    void synchronous_use() {
        // when
        final List<WebResponse> responses = client.get().uri("/api")
            .retrieve()
            .bodyToFlux(WebResponse.class)
            .collectList()
            .block();

        // then
        assertThat(responses.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("여러 번의 WebClient 호출을 각각 블로킹하기보다는 zip을 사용하는 것이 효율적이다.")
    void combined_multiple_calls() {
        // given
        final Mono<Person> personMono = client.get().uri("/person/{id}", 1L)
            .retrieve().bodyToMono(Person.class);

        final Mono<List<Hobby>> hobbiesMono = client.get().uri("/person/{id}/hobbies", 1L)
            .retrieve().bodyToFlux(Hobby.class).collectList();

        // when
        final Map<String, Object> data = Mono.zip(personMono, hobbiesMono, (person, hobbies) -> {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("person", person);
                map.put("hobbies", hobbies);
                return map;
            })
            .block();

        // then
        assertThat(data).containsKeys("person", "hobbies");
    }

    @Test
    @DisplayName("WebClient api를 호출한다")
    void call_webClient() {
        // given
        final WebClient client = WebClient.create("http://localhost:8081");
        final Long id = 1L;

        // when
        final WebResponse response = client.get().uri("/web-client/{id}", id)
            .retrieve()
            .bodyToMono(WebResponse.class)
            .block();

        // then
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getName()).isEqualTo("api webClient");
    }
}
