package learn.java.testrepository.spring.validation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidatedRequest {

    @Min(value = 2)
    private Integer number;

    @NotBlank
    private String message;
}
