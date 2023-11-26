package learn.java.testrepository.plain.io;

import java.io.IOException;

public class EventLoop {

    private static final int PORT = 8000;

    public static void main(String[] args) throws IOException {
        Reactor reactor =  new Reactor(PORT);
        reactor.run();
    }
}
