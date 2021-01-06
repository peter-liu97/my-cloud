package com.cloud;

import com.cloud.reactive.UmsHandler;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = UmsHandler.class)
public class TestReactive {
}
