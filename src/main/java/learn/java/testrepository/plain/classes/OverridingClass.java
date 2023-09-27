package learn.java.testrepository.plain.classes;

public class OverridingClass extends SuperClass{

    @Override
    public String method() {
        return "sub";
    }

    @Override
    public String test(SuperClass obj) {
        return "sub&super";
    }

    public String test(OverridingClass obj) {
        return "sub&sub";
    }
}

class SuperClass {
    public String method() {
        return "super";
    }

    public String test(SuperClass obj) {
        return "super&super";
    }
}
