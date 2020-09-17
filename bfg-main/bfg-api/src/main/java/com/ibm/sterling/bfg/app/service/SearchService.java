package com.ibm.sterling.bfg.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.exception.BPHeaderNotFoundException;
import com.ibm.sterling.bfg.app.exception.DocumentContentNotFoundException;
import com.ibm.sterling.bfg.app.exception.FileTransactionNotFoundException;
import com.ibm.sterling.bfg.app.model.file.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
public class SearchService {

    @Value("${file.search.url}")
    private String fileSearchUrl;

    @Value("${transaction.search.url}")
    private String transactionSearchUrl;

    @Value("${document.url}")
    private String documentUrl;

    @Value("${workflowSteps.url}")
    private String workflowStepsUrl;

    @Value("${workflows.url}")
    private String workflowsUrl;

    @Value("${file.userName}")
    private String userName;

    @Value("${file.password}")
    private String password;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityService entityService;

    public Page<File> getFilesList(FileSearchCriteria fileSearchCriteria) throws JsonProcessingException {
        List<File> fileList = objectMapper.convertValue(getListFromSBI(fileSearchCriteria, fileSearchUrl), new TypeReference<List<File>>() {
        });
        Optional.ofNullable(fileList).ifPresent(this::setEntityOfFile);
        return convertListToPage(fileSearchCriteria, fileList);
    }

    public Optional<File> getFileById(Integer id) throws JsonProcessingException {
        FileSearchCriteria fileSearchCriteria = new FileSearchCriteria();
        fileSearchCriteria.setId(id);
        List<File> fileList = objectMapper.convertValue(getListFromSBI(fileSearchCriteria, fileSearchUrl), new TypeReference<List<File>>() {
        });
        List<File> files = Optional.ofNullable(fileList).orElseGet(ArrayList::new);
        if (files.size() == 1) {
            setEntityOfFile(files);
            return Optional.ofNullable(files.get(0));
        } else {
            return Optional.empty();
        }
    }

    public Page<Transaction> getTransactionsList(Integer fileId, Integer page, Integer size) throws JsonProcessingException {
        SearchCriteria searchCriteria = new SearchCriteria(page, size);
        return convertListToPage(searchCriteria, getListFromSBI(searchCriteria, fileSearchUrl + "/" + fileId + "/transactions"));
    }

    public Page<Transaction> getSCTTransactionList(TransactionSearchCriteria transactionSearchCriteria) throws JsonProcessingException {
        return convertListToPage(transactionSearchCriteria, getListFromSBI(transactionSearchCriteria, transactionSearchUrl));
    }

    public Optional<Transaction> getTransactionById(Integer fileId, Integer id) throws JsonProcessingException {
        JsonNode root;
        try {
            root = getJsonNodeFromSBI(new FileSearchCriteria(),
                    fileSearchUrl + "/" + fileId + "/transactions/" + id);
        } catch (HttpStatusCodeException e) {
            throw new FileTransactionNotFoundException(e.getMessage());
        }
        return Optional.ofNullable(objectMapper.convertValue(root, TransactionDetails.class));
    }

    public Map<String, String> getDocumentContent(String documentId) throws JsonProcessingException {
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = new RestTemplate().exchange(
                    documentUrl + documentId + "/actions/getpayload?isPlainText=true",
                    HttpMethod.POST,
                    new HttpEntity<>(getHttpHeaders()),
                    String.class);
        } catch (HttpStatusCodeException e) {
            throw new DocumentContentNotFoundException(e.getMessage());
        }
        JsonNode jsonNode = objectMapper.readTree(Objects.requireNonNull(responseEntity.getBody())).get("response");
        return Collections.singletonMap("document", jsonNode.asText());
    }

    private <T> List<T> getListFromSBI(SearchCriteria searchCriteria, String searchingUrl) throws JsonProcessingException {
        searchCriteria.setStart(searchCriteria.getPage() * searchCriteria.getSize());
        JsonNode root = getJsonNodeFromSBI(searchCriteria, searchingUrl);
        searchCriteria.setTotalRows(objectMapper.convertValue(root.get("totalRows"), Integer.class));
        return objectMapper.convertValue(root.get("results"), List.class);
    }

    private void setEntityOfFile(List<File> fileList) {
        fileList.forEach(file -> {
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
        });
    }

    private <T> PageImpl<T> convertListToPage(SearchCriteria searchCriteria, List<T> results) {
        return new PageImpl<>(Optional.ofNullable(results).orElseGet(ArrayList::new),
                PageRequest.of(searchCriteria.getPage(), searchCriteria.getSize()), searchCriteria.getTotalRows());
    }

    public List<WorkflowStep> getWorkflowSteps(Integer workFlowId) throws JsonProcessingException {
        ResponseEntity<String> response = new RestTemplate().exchange(
                workflowStepsUrl + "?fieldList=Full&workFlowId=" + workFlowId,
                HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders()),
                String.class);
        return objectMapper.convertValue(objectMapper.readTree(Objects.requireNonNull(response.getBody())),
                new TypeReference<List<WorkflowStep>>() {
                });
    }

    public String getBPHeader(Integer wfdVersion, Integer wfdID) throws JsonProcessingException {
        ResponseEntity<String> response = new RestTemplate().exchange(
                workflowsUrl + "?_include=wfdVersion,wfdID&_range=0-999&fieldList=brief",
                HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders()),
                String.class);
        List<BPName> objects = objectMapper.convertValue(objectMapper.readTree(Objects.requireNonNull(response.getBody())),
                new TypeReference<List<BPName>>() {
                });
        return objects.stream().filter(
               wf -> wf.getWfdID().equals(wfdID) && wf.getWfdVersion().equals(wfdVersion)).findAny().map(BPName::getName)
                .orElseThrow(BPHeaderNotFoundException::new);
    }

    private JsonNode getJsonNodeFromSBI(SearchCriteria searchCriteria, String fileSearchUrl) throws JsonProcessingException {
        MultiValueMap<String, String> fileSearchCriteriaMultiValueMap = new LinkedMultiValueMap<>();
        objectMapper.convertValue(searchCriteria, new TypeReference<Map<String, String>>() {
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
