package com.github.ideepakkalra.eventmanagement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventManagementApplicationTest {

    @Test
    void testContextLoads(ApplicationContext context) {
        Assertions.assertNotNull(context);
    }
}
