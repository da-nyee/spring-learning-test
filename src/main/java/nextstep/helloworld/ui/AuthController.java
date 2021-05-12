package nextstep.helloworld.ui;

import nextstep.helloworld.application.AuthService;
import nextstep.helloworld.application.AuthorizationException;
import nextstep.helloworld.dto.MemberResponse;
import nextstep.helloworld.dto.TokenRequest;
import nextstep.helloworld.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class AuthController {
    private static final String SESSION_KEY = "USER";
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * ex) request sample
     * <p>
     * POST /login/session HTTP/1.1
     * content-type: application/x-www-form-urlencoded; charset=ISO-8859-1
     * host: localhost:55477
     * <p>
     * email=email@email.com&password=1234
     */
    @PostMapping("/login/session")
    public ResponseEntity<Void> sessionLogin(@RequestParam(value = "email") String email,
                                             @RequestParam(value = "password") String password,
                                             HttpSession httpSession) {
        if (authService.checkInvalidLogin(email, password)) {
            throw new AuthorizationException();
        }
        httpSession.setAttribute(SESSION_KEY, email);
        return ResponseEntity.ok().build();
    }

    /**
     * ex) request sample
     * <p>
     * GET /members/me HTTP/1.1
     * cookie: JSESSIONID=E7263AC9557EF658C888F02EEF840A19
     * accept: application/json
     */
    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMyInfo(HttpSession httpSession) {
        String email = (String) httpSession.getAttribute(SESSION_KEY);
        MemberResponse member = authService.findMember(email);
        return ResponseEntity.ok().body(member);
    }

    /**
     * ex) request sample
     * <p>
     * POST /login/token HTTP/1.1
     * accept: application/json
     * content-type: application/json; charset=UTF-8
     * <p>
     * {
     * "email": "email@email.com",
     * "password": "1234"
     * }
     */
    @PostMapping("/login/token")
    public ResponseEntity tokenLogin() {
        // TODO: TokenRequest 값을 메서드 파라미터로 받아오기 (hint: @RequestBody)
        TokenRequest tokenRequest = null;
        TokenResponse tokenResponse = authService.createToken(tokenRequest);
        return ResponseEntity.ok().body(tokenResponse);
    }

    /**
     * ex) request sample
     * <p>
     * GET /members/you HTTP/1.1
     * authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MTAzNzY2NzIsImV4cCI6MTYxMDM4MDI3Mn0.Gy4g5RwK1Nr7bKT1TOFS4Da6wxWh8l97gmMQDgF8c1E
     * accept: application/json
     */
    @GetMapping("/members/you")
    public ResponseEntity findYourInfo(HttpServletRequest request) {
        // TODO: authorization 헤더의 Bearer 값을 추출하기
        String token = "";
        MemberResponse member = authService.findMemberByToken(token);
        return ResponseEntity.ok().body(member);
    }
}
