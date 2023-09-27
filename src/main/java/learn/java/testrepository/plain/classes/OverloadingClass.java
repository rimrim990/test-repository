package learn.java.testrepository.plain.classes;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class OverloadingClass {

    public String method(Collection<?> param) {
        return "collection";
    }

    public String method(List<?> param) {
        return "list";
    }

    public String method(Set<?> param) {
        return "set";
    }

    public String classify(Collection<?> param) {
        return param instanceof Set ? "set" : param instanceof List ? "list" : "collection";
    }
}
