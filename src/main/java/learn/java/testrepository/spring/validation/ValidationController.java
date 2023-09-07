package learn.java.testrepository.spring.validation;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/spring/validation")
public class ValidationController {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String validationCheck(@Validated @RequestBody ValidatedRequest request) {
        return request.getMessage();
    }
}
