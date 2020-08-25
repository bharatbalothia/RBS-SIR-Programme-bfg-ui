package com.ibm.sterling.bfg.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.exception.FileTransactionNotFoundException;
import com.ibm.sterling.bfg.app.model.file.*;
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
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

import static com.ibm.sterling.bfg.app.utils.RestTemplatesConstants.HEADER_PREFIX;

@Service
public class FileSearchService {

    @Value("${file.search.url}")
    private String fileSearchUrl;

    @Value("${document.url}")
    private String documentUrl;

    @Value("${file.userName}")
    private String userName;

    @Value("${file.password}")
    private String password;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityService entityService;

    public Page<File> getFilesList(FileSearchCriteria fileSearchCriteria) throws JsonProcessingException {
        Integer page = fileSearchCriteria.getStart();
        Integer size = fileSearchCriteria.getRows();
        fileSearchCriteria.setStart(page * size);
        JsonNode root = getFileListFromSBI(fileSearchCriteria, fileSearchUrl);
        Integer totalElements = objectMapper.convertValue(root.get("totalRows"), Integer.class);
        List<File> fileList = objectMapper.convertValue(root.get("results"), new TypeReference<List<File>>() {
        });
        return new PageImpl<>(Optional.ofNullable(fileList).map(this::setEntityOfFile).orElseGet(ArrayList::new),
                PageRequest.of(page, size), totalElements);
    }

    private List<File> setEntityOfFile(List<File> fileList) {
        return fileList.stream().peek(file -> {
            Integer entityId = file.getEntityID();
            Entity entity = new Entity();
            entity.setEntityId(entityId);
            entity.setEntity(entityService.findById(entityId)
                    .map(com.ibm.sterling.bfg.app.model.Entity::getEntity)
                    .orElseGet(() -> {
                        entity.setError("no such entity");
                        return null;
                    })
            );
            file.setEntity(entity);
        }).collect(Collectors.toList());
    }

    public Optional<File> getFileById(Integer id) throws JsonProcessingException {
        FileSearchCriteria fileSearchCriteria = new FileSearchCriteria();
        fileSearchCriteria.setId(id);
        JsonNode root = getFileListFromSBI(fileSearchCriteria, fileSearchUrl);
        Integer totalElements = objectMapper.convertValue(root.get("totalRows"), Integer.class);
        if (totalElements == 1) {
            List<File> fileList = objectMapper.convertValue(root.get("results"), new TypeReference<List<File>>() {
            });
            return Optional.ofNullable(objectMapper.convertValue(Optional.ofNullable(fileList)
                            .map(list -> {
                                setEntityOfFile(list);
                                return list.get(0);
                            }).orElse(null),
                    File.class));
        } else {
            return Optional.empty();
        }
    }

    public Page<Transaction> getTransactionsList(Integer fileId, Integer size, Integer page) throws JsonProcessingException {
        FileSearchCriteria fileSearchCriteria = new FileSearchCriteria();
        fileSearchCriteria.setRows(size);
        fileSearchCriteria.setStart(page * size);
        JsonNode root = getFileListFromSBI(fileSearchCriteria, fileSearchUrl + "/" + fileId + "/transactions");
        Integer totalElements = objectMapper.convertValue(root.get("totalRows"), Integer.class);
        List<Transaction> transactionList = objectMapper.convertValue(root.get("results"), List.class);
        Pageable pageable = PageRequest.of(page, size);
        return new PageImpl<>(Optional.ofNullable(transactionList).orElse(new ArrayList<>()), pageable, totalElements);
    }

    public Optional<Transaction> getTransactionById(Integer fileId, Integer id) throws JsonProcessingException {
        JsonNode root;
        try {
            root = getFileListFromSBI(new FileSearchCriteria(),
                    fileSearchUrl + "/" + fileId + "/transactions/" + id);
        } catch (HttpStatusCodeException e) {
            throw new FileTransactionNotFoundException(e.getMessage());
        }
        return Optional.ofNullable(objectMapper.convertValue(root, TransactionDetails.class));
    }

    public Map<String, String> getDocumentContent(String documentId) throws JsonProcessingException {
        ResponseEntity<String> responseEntity = new RestTemplate().exchange(
                documentUrl + documentId + "/actions/getpayload?isPlainText=true",
                HttpMethod.POST,
                new HttpEntity<>(getHttpHeaders()),
                String.class);
        JsonNode jsonNode = objectMapper.readTree(Objects.requireNonNull(responseEntity.getBody())).get("response");
        return Collections.singletonMap("document", jsonNode.asText());
    }

    private JsonNode getFileListFromSBI(FileSearchCriteria fileSearchCriteria, String fileSearchUrl) throws JsonProcessingException {
        MultiValueMap<String, String> fileSearchCriteriaMultiValueMap = new LinkedMultiValueMap<>();
        objectMapper.convertValue(fileSearchCriteria, new TypeReference<Map<String, String>>() {
        }).forEach(fileSearchCriteriaMultiValueMap::add);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(fileSearchUrl)
                .queryParams(fileSearchCriteriaMultiValueMap);
        ResponseEntity<String> response = new RestTemplate().exchange(
                uriBuilder.build().toString(),
                HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders()),
                String.class);

        return objectMapper.readTree(Objects.requireNonNull(response.getBody()));
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String userCredentials = userName + ":" + password;
        headers.set(HttpHeaders.AUTHORIZATION,
                HEADER_PREFIX + Base64.getEncoder().encodeToString(userCredentials.getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

}
