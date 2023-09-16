package learn.java.testrepository.spring.request;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/spring/request")
@RestController
public class RequestController {

    @GetMapping("/cookie")
    public void cookie(final HttpServletResponse response) {
        // 1시간 후에 만료되는 쿠키
        final Cookie storedCookie = new Cookie("auth", "1");
        // localhost/*
        storedCookie.setPath("/");
        storedCookie.setMaxAge(60 * 60);
        storedCookie.setHttpOnly(true);

        // 브라우저에 저장되지 않는 쿠키
        final Cookie notStoredCookie = new Cookie("auth", "2");
        // localhost/api/spring/request
        notStoredCookie.setMaxAge(0);
        notStoredCookie.setHttpOnly(true);

        // 브라우저를 종료하면 만료되는 쿠키
        final Cookie tempCookie = new Cookie("auth", "3");
        tempCookie.setMaxAge(-1);
        tempCookie.setHttpOnly(true);

        response.addCookie(storedCookie);
        response.addCookie(notStoredCookie);
        response.addCookie(tempCookie);
    }

    @GetMapping("/session")
    public void session(final HttpServletRequest request, final HttpServletResponse response) {
        HttpSession session = request.getSession();
        System.out.println(session);
        System.out.println(session.getId());
        System.out.println(session.getCreationTime());
        System.out.println(session.getLastAccessedTime());
        System.out.println(session.getAttribute("auth"));

        session.setAttribute("auth", 1);
        session.setAttribute("auth", 2);
        session.setAttribute("auth", 3);
    }
}
