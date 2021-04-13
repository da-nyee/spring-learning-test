package nextstep.helloworld.mvc.mapping;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/param-header/message")
public class ParamHeaderController {

    @GetMapping
    public ResponseEntity<String> message() {
        return ResponseEntity.ok().body("message");
    }

    @GetMapping(params = "name=hello")
    public ResponseEntity<String> messageForParam() {
        return ResponseEntity.ok().body("hello");
    }

    @GetMapping(headers = "header=hi")
    public ResponseEntity<String> messageForHeader() {
        return ResponseEntity.ok().body("hi");
    }
}