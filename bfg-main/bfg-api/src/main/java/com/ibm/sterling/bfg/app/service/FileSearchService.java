package com.ibm.sterling.bfg.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.model.file.File;
import com.ibm.sterling.bfg.app.model.file.FileSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

import static com.ibm.sterling.bfg.app.utils.RestTemplatesConstants.HEADER_PREFIX;

@Service
public class FileSearchService {

    @Value("${file.search.url}")
    private String fileSearchUrl;

    @Value("${file.userName}")
    private String userName;

    @Value("${file.password}")
    private String password;

    @Autowired
    private ObjectMapper objectMapper;

    public Page<File> getFilesList(FileSearchCriteria fileSearchCriteria) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        String userCredentials = userName + ":" + password;
        headers.set(HttpHeaders.AUTHORIZATION,
                HEADER_PREFIX + Base64.getEncoder().encodeToString(userCredentials.getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String, String> fileSearchCriteriaMap = objectMapper.convertValue(fileSearchCriteria, new TypeReference<Map<String, String>>() {
        });
        fileSearchCriteriaMap.values().removeIf(Objects::isNull);
        MultiValueMap<String, String> fileSearchCriteriaMultiValueMap = new LinkedMultiValueMap<>();
        fileSearchCriteriaMap.forEach(fileSearchCriteriaMultiValueMap::add);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(fileSearchUrl)
                .queryParams(fileSearchCriteriaMultiValueMap);
        ResponseEntity<String> response = new RestTemplate().exchange(
                uriBuilder.build().toString(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class);

        JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        Integer totalElements = objectMapper.convertValue(root.get("totalRows"), Integer.class);
        List<File> fileList = objectMapper.convertValue(root.get("results"), List.class);
        Pageable pageable = PageRequest.of(fileSearchCriteria.getStart(), fileSearchCriteria.getRows());
        return new PageImpl<>(fileList, pageable, totalElements);
    }

}
