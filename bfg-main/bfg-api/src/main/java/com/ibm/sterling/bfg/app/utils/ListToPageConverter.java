package com.ibm.sterling.bfg.app.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

public class ListToPageConverter {
    public static Page<Object> convertListToPage(List<Object> list, Pageable pageable) {
        int total = list.size();
        int start = toIntExact(pageable.getOffset());
        int end = Math.min((start + pageable.getPageSize()), total);

        List<Object> output = new ArrayList<>();

        if (start <= end) {
            output = list.subList(start, end);
        }
        Page<Object> page = new PageImpl<Object>(output, pageable, total);
        return page;
    }
}
