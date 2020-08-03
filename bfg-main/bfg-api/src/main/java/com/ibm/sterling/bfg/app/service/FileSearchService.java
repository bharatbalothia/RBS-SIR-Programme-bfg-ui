package com.ibm.sterling.bfg.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.model.file.File;
import com.ibm.sterling.bfg.app.model.security.LoginRequest;
import com.ibm.sterling.bfg.app.utils.ListToPageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

import static com.ibm.sterling.bfg.app.utils.RestTemplatesConstants.HEADER_PREFIX;

@Service
public class FileSearchService {

    @Value("${file.search.url}")
    private String fileSearchUrl;

    @Value("${file.userName}")
    private String userName;

    @Value("${file.password}")
    private String password;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Page<File> getFilesList(Pageable pageable) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        String userCredentials = userName + ":" + password;
        headers.set(HttpHeaders.AUTHORIZATION,
                HEADER_PREFIX + Base64.getEncoder().encodeToString(userCredentials.getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(fileSearchUrl);

        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<String> response =
                new RestTemplate().exchange(
                        uriBuilder.toUriString(),
                        HttpMethod.GET,
                        request,
                        String.class
                );
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        JsonNode results = root.get("results");
        List<File> fileList = objectMapper.convertValue(results, List.class);
        return ListToPageConverter.convertListToPage(fileList, pageable) ;
    }
}
