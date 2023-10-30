package learn.java.testrepository.spring.request;

import lombok.Data;

@Data
public class DrResult<T> {

    T value;
}
