package learn.java.testrepository.spring.validation;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import learn.java.testrepository.config.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("스프링 Validation 테스트")
class ValidationControllerTest extends IntegrationTest {

    @Test
    @DisplayName("number 검증에 실패하면 400 을 반환한다")
    void invalidNumber_validationFail_status400() {
        // given
        final ValidatedRequest request = new ValidatedRequest(0, "test");

        // when
        ExtractableResponse<Response> result = RestAssured
            .given().log().all()
            .when()
            .body(request)
            .contentType(ContentType.JSON)
            .post("/api/spring/validation")
            .then()
            .log().all().extract();

        // then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        final ErrorResponse response = result.body().as(ErrorResponse.class);
        assertThat(response.getMessage()).isEqualTo("field number must be greater or equal than 2");
    }

    @Test
    @DisplayName("text 검증에 실패하면 400 을 반환한다")
    void blankText_validationFail_status400() {
        // given
        final ValidatedRequest request = new ValidatedRequest(3, "");

        // when
        ExtractableResponse<Response> result = RestAssured
            .given().log().all()
            .when()
            .body(request)
            .contentType(ContentType.JSON)
            .post("/api/spring/validation")
            .then().log().all().extract();

        // then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        final ErrorResponse response = result.body().as(ErrorResponse.class);
        assertThat(response.getMessage()).isEqualTo("field message must contain at least one character");
    }

    @Test
    @DisplayName("text 검증에 실패하면 400 을 반환한다")
    void nullText_validationFail_status400() {
        // given
        final ValidatedRequest request = new ValidatedRequest(3, null);

        // when
        ExtractableResponse<Response> result = RestAssured
            .given().log().all()
            .when()
            .body(request)
            .contentType(ContentType.JSON)
            .post("/api/spring/validation")
            .then().log().all().extract();

        // then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        final ErrorResponse response = result.body().as(ErrorResponse.class);
        assertThat(response.getMessage()).isEqualTo("field message must contain at least one character");
    }
}