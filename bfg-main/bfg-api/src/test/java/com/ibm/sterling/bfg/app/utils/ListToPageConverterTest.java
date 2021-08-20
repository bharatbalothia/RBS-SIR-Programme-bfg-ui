package com.ibm.sterling.bfg.app.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListToPageConverterTest {
    private Pageable pageable;
    private List<String> list;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(1, 2);
        list = Arrays.asList("one", "two", "three", "four", "five");
    }

    @Test
    void convertListToPage_ShouldReturnPage() {
        assertEquals(new PageImpl<>(Arrays.asList("three", "four"), pageable, 5),
                ListToPageConverter.convertListToPage(list, pageable));
    }
}