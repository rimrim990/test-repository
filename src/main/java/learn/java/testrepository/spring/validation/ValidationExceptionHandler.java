package learn.java.testrepository.spring.validation;

import io.micrometer.common.util.StringUtils;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class ValidationExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMessages = resolvedFieldErrorMessages(ex);

        System.out.println(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(errorMessages));
    }

    private String resolvedFieldErrorMessages(final MethodArgumentNotValidException ex) {
        return ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(this::getFieldErrorMessage)
            .collect(Collectors.joining(", "));
    }

    private String getFieldErrorMessage(final FieldError error) {
        final Object[] arguments = error.getArguments();
        final Locale locale = LocaleContextHolder.getLocale();

        return Arrays.stream(error.getCodes())
            .map(code -> searchMessageSource(code, arguments, locale))
            .filter(StringUtils::isNotBlank)
            .findFirst()
            .orElse(error.getDefaultMessage());
    }

    private String searchMessageSource(final String code, final Object[] arguments, final Locale locale) {
        try {
            return messageSource.getMessage(code, arguments, locale);
        } catch (NoSuchMessageException e) {
            return "";
        }
    }
}
