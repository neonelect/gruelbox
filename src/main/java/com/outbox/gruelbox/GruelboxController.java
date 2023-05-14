package com.outbox.gruelbox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GruelboxController {

    @Autowired
    private GruelboxService gruelboxService;

    @PostMapping("/gruelbox")
    public void testGruelBox(@RequestBody String body){
        gruelboxService.testGruelBox(body);
    }
}
