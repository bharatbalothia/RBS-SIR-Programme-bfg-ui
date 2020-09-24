package com.ibm.sterling.bfg.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.exception.BPHeaderNotFoundException;
import com.ibm.sterling.bfg.app.exception.DocumentContentNotFoundException;
import com.ibm.sterling.bfg.app.exception.FileTransactionNotFoundException;
import com.ibm.sterling.bfg.app.model.file.*;
import com.ibm.sterling.bfg.app.utils.ListToPageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

import static com.ibm.sterling.bfg.app.utils.RestTemplatesConstants.HEADER_PREFIX;
import static org.springframework.data.domain.PageRequest.of;

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

    @Autowired
    private SearchService searchService;

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

    public Map<String, String> getDocumentPayload(String documentId) throws JsonProcessingException {
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

    public Document getDocumentById(String documentId) throws JsonProcessingException {
        ResponseEntity<String> response = new RestTemplate().exchange(
                documentUrl + "?documentId=" + documentId,
                HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders()),
                String.class);
        List<Document> documents = Optional.ofNullable(
                objectMapper.convertValue(objectMapper.readTree(Objects.requireNonNull(response.getBody())),
                        new TypeReference<List<Document>>() {
                        })
        ).orElseGet(ArrayList::new);
        Document document;
        if (documents.size() == 1) {
            document = documents.get(0);
            document.setDocumentPayload(getDocumentPayload(documentId).get("document"));
        } else throw new DocumentContentNotFoundException();
        return document;
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
                of(searchCriteria.getPage(), searchCriteria.getSize()), searchCriteria.getTotalRows());
    }

    public Page<WorkflowStep> getWorkflowSteps(Integer workFlowId, Integer page, Integer size) throws JsonProcessingException {
        ResponseEntity<String> response = new RestTemplate().exchange(
                workflowStepsUrl + "?_sort=stepId&fieldList=Full&workFlowId=" + workFlowId,
                HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders()),
                String.class);
        List<WorkflowStep> workflowSteps = Optional.ofNullable(objectMapper.convertValue(
                objectMapper.readTree(Objects.requireNonNull(response.getBody())), new TypeReference<List<WorkflowStep>>() {
                })).orElseGet(ArrayList::new);
        Collections.reverse(workflowSteps);
        Page<WorkflowStep> workflows = ListToPageConverter.convertListToPage(workflowSteps, of(page, size));
        WFPage<WorkflowStep> wfPage = new WFPage<>(workflows.getContent(), workflows.getPageable(), workflows.getTotalElements());
        if (!workflowSteps.isEmpty()) {
            int iteration = 0;
            boolean isStatusSuccessful = true;
            boolean isListFull = true;
            WorkflowStep firstWorkflowStep = workflowSteps.get(0);
            for (WorkflowStep workflowStep : workflowSteps) {
                if (!firstWorkflowStep.getWfdId().equals(workflowStep.getWfdId()) ||
                        !firstWorkflowStep.getWfdVersion().equals(workflowStep.getWfdVersion())) {
                    workflowStep.setInlineInvocation(true);
                }
                isStatusSuccessful = isStatusSuccessful & workflowStep.getExeState().equals("Success");
                isListFull = isListFull & workflowStep.getStepId() == iteration++;
            }
            if (isStatusSuccessful) {
                wfPage.setState("");
                wfPage.setState("Success");
            } else {
                wfPage.setState("Halted");
                wfPage.setStatus("Error");
            }
            if (!isListFull) {
                wfPage.setFullTracking(false);
            }
        }
        return wfPage;
    }

    public BPDetails getBPDetails(String identifier) throws JsonProcessingException {
        ResponseEntity<String> response = new RestTemplate().exchange(
                workflowsUrl + identifier,
                HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders()),
                String.class);
        return objectMapper.convertValue(objectMapper.readTree(
                Objects.requireNonNull(response.getBody())), new TypeReference<BPDetails>() {
        });
    }

    public Map<String, String> getBPHeader(Integer wfdVersion, Integer wfdID) throws JsonProcessingException {
        List<BPName> objects = searchService.getBPNames();
        BPName bpName = objects.stream()
                .filter(wf -> wf.getWfdID().equals(wfdID) && wf.getWfdVersion().equals(wfdVersion))
                .findAny()
                .orElseThrow(BPHeaderNotFoundException::new);
        Map<String, String> header = new HashMap<>();
        header.put("bpName", bpName.getName());
        header.put("bpRef", bpName.getId());
        return header;
    }

    @Cacheable("bpNames")
    public List<BPName> getBPNames() throws JsonProcessingException {
        ResponseEntity<String> response = new RestTemplate().exchange(
                workflowsUrl + "?_include=wfdVersion,wfdID,name&_range=0-999&fieldList=brief",
                HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders()),
                String.class);
        return objectMapper.convertValue(objectMapper.readTree(Objects.requireNonNull(response.getBody())),
                new TypeReference<List<BPName>>() {
                });
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
