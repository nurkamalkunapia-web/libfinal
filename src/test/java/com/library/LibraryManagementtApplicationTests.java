package com.library;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")   // ← Это важно!
class LibraryManagementtApplicationTests {

    @Test
    void contextLoads() {
    }
}