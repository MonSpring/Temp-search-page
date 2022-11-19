package com.example.testsearch;

import org.junit.jupiter.api.Test;

public class VersionTest {

    @Test
    public void versionTest() throws Exception {
        String test = org.springframework.core.SpringVersion.getVersion();
        System.out.println(test);
    }
}
