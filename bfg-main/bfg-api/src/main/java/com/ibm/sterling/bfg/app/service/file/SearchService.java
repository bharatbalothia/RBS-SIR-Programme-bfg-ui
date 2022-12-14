package com.ibm.sterling.bfg.app.service.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.exception.file.BPHeaderNotFoundException;
import com.ibm.sterling.bfg.app.exception.file.DocumentContentNotFoundException;
import com.ibm.sterling.bfg.app.exception.file.FileNotFoundException;
import com.ibm.sterling.bfg.app.exception.file.FileTransactionNotFoundException;
import com.ibm.sterling.bfg.app.model.file.*;
import com.ibm.sterling.bfg.app.model.report.ReportType;
import com.ibm.sterling.bfg.app.service.APIDetailsHandler;
import com.ibm.sterling.bfg.app.service.PropertyService;
import com.ibm.sterling.bfg.app.service.entity.EntityService;
import com.ibm.sterling.bfg.app.utils.ListToPageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

import static com.ibm.sterling.bfg.app.config.cache.CacheSpec.CACHE_BP_HEADERS;
import static org.springframework.data.domain.PageRequest.of;

@Service
public class SearchService {

    public static final String DOCUMENT = "document";
    @Value("${file.search.url}")
    private String fileSearchUrl;

    @Value("${transaction.search.url}")
    private String transactionSearchUrl;

    @Value("${document.url}")
    private String documentUrl;

    @Value("${message.url}")
    private String messageUrl;

    @Value("${workflowSteps.url}")
    private String workflowStepsUrl;

    @Value("${workflows.url}")
    private String workflowsUrl;

    @Value("${api.userName}")
    private String userName;

    @Value("${api.password}")
    private String password;

    @Value("${property.fileStatusPrefixKey}")
    private String fileStatusPrefixKey;

    @Value("${property.transactionStatusPrefixKey}")
    private String transactionStatusPrefixKey;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityService entityService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private APIDetailsHandler apiDetailsHandler;

    @Autowired
    private ExportExcelReportService exportExcelService;

    @Autowired
    private ExportPDFReportService exportPDFService;

    @Autowired
    private Validator validator;

	private static final int RANGE = 999;

    public <T> Page<T> getFilesList(FileSearchCriteria fileSearchCriteria, Class<T> elementClass) throws JsonProcessingException {
        return convertListToPage(fileSearchCriteria, getListFromSBI(fileSearchCriteria, fileSearchUrl, elementClass));
    }

     public Page<File> getFilesList(FileSearchCriteria fileSearchCriteria) throws JsonProcessingException {
        return convertListToPage(fileSearchCriteria, getListFromSBI(fileSearchCriteria, fileSearchUrl, File.class));
    }

    public Optional<FileDetails> getFileById(Integer id) throws JsonProcessingException {
        ResponseEntity<String> response;
        try {
            response = new RestTemplate().exchange(
                    fileSearchUrl + "/" + id,
                    HttpMethod.GET,
                    new HttpEntity<>(apiDetailsHandler.getHttpHeaders(userName, password)),
                    String.class);
        } catch (HttpStatusCodeException e) {
            throw new FileNotFoundException(e.getMessage());
        }
        JsonNode jsonNode = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        return Optional.ofNullable(objectMapper.convertValue(jsonNode, FileDetails.class)).map(file -> {
            setEntityOfFile(file);
            file.setStatusLabel(propertyService.getStatusLabel(
                    fileStatusPrefixKey, file.getService(), file.getDirection(), file.getStatus()));
            return file;
        });
    }

    private void setEntityOfFile(FileDetails file) {
        Integer entityId = file.getEntityID();
        Entity entity = new Entity();
        entity.setEntityId(entityId);
        entity.setEntity(entityService.findById(entityId)
                .map(com.ibm.sterling.bfg.app.model.entity.Entity::getEntity)
                .orElseGet(() -> {
                    entity.setError("no such entity");
                    return null;
                })
        );
        file.setEntity(entity);
    }

    public Page<Transaction> getTransactionsList(Integer fileId, Integer page, Integer size) throws JsonProcessingException {
        SearchCriteria searchCriteria = new SearchCriteria(page, size);
        return convertListToPage(searchCriteria, getListFromSBI(searchCriteria, fileSearchUrl + "/" + fileId + "/transactions", Transaction.class));
    }

    public Page<Transaction> getSCTTransactionList(TransactionSearchCriteria transactionSearchCriteria) throws JsonProcessingException {
        return convertListToPage(transactionSearchCriteria, getListFromSBI(transactionSearchCriteria, transactionSearchUrl, Transaction.class));
    }

    public Optional<TransactionDetails> getTransactionById(Integer id) throws JsonProcessingException {
        ResponseEntity<String> response;
        try {
            response = new RestTemplate().exchange(
                    transactionSearchUrl + "/" + id,
                    HttpMethod.GET,
                    new HttpEntity<>(apiDetailsHandler.getHttpHeaders(userName, password)),
                    String.class);
        } catch (HttpStatusCodeException e) {
            throw new FileTransactionNotFoundException(e.getMessage());
        }
        JsonNode jsonNode = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        return Optional.ofNullable(objectMapper.convertValue(jsonNode, TransactionDetails.class))
                .map(transactionDetails -> {
                    transactionDetails.setStatusLabel(
                            propertyService.getStatusLabel(
                                    transactionStatusPrefixKey,
                                    transactionDetails.getService(),
                                    transactionDetails.getDirection(),
                                    transactionDetails.getStatus()
                            )
                    );
                    return transactionDetails;
                });
    }

    public Map<String, String> getDocumentPayload(String documentId) throws JsonProcessingException {
        ResponseEntity<String> response;
        try {
            response = new RestTemplate().exchange(
                    documentUrl + documentId + "/actions/getpayload?isPlainText=true",
                    HttpMethod.POST,
                    new HttpEntity<>(apiDetailsHandler.getHttpHeaders(userName, password)),
                    String.class);
        } catch (HttpStatusCodeException e) {
            return Collections.singletonMap(DOCUMENT, null);
        }
        JsonNode jsonNode = objectMapper.readTree(Objects.requireNonNull(response.getBody())).get("response");
        return Collections.singletonMap(DOCUMENT, jsonNode.asText());
    }

    public Map<String, String> getDocumentPayloadByMessageId(Integer messageId) {
        Map<String, String> emptyMap = Collections.singletonMap(DOCUMENT, null);
        return Optional.ofNullable(messageId).map(id -> {
            ResponseEntity<String> response;
            try {
                response = new RestTemplate().exchange(
                        messageUrl + "?messageId=" + id,
                        HttpMethod.GET,
                        new HttpEntity<>(apiDetailsHandler.getHttpHeaders(userName, password)),
                        String.class);
                        return Optional.ofNullable(
                        objectMapper.convertValue(response.getBody(), String.class))
                        .map((String documentId1) -> {
                            try {
                                return getDocumentPayload(documentId1);
                            } catch (JsonProcessingException e) {
                                return emptyMap;
                            }
                        }).orElse(emptyMap);
            } catch (HttpStatusCodeException e) {
                return emptyMap;
            }
        }).orElse(emptyMap);
    }

    public Document getDocumentById(String documentId) throws JsonProcessingException {
        ResponseEntity<String> response = new RestTemplate().exchange(
                documentUrl + "?documentId=" + documentId,
                HttpMethod.GET,
                new HttpEntity<>(apiDetailsHandler.getHttpHeaders(userName, password)),
                String.class);
        List<Document> documents = Optional.ofNullable(
                objectMapper.convertValue(objectMapper.readTree(Objects.requireNonNull(response.getBody())),
                        new TypeReference<List<Document>>() {
                        })
        ).orElseGet(ArrayList::new);
        Document document;
        if (documents.size() == 1) {
            document = documents.get(0);
            document.setDocumentPayload(getDocumentPayload(documentId).get(DOCUMENT));
        } else throw new DocumentContentNotFoundException("Document content is not found");
        return document;
    }

    private <T> List<T> getListFromSBI(SearchCriteria searchCriteria, String searchingUrl, Class<T> elementClass) throws JsonProcessingException {
        searchCriteria.setStart(searchCriteria.getPage() * searchCriteria.getSize());
        MultiValueMap<String, String> fileSearchCriteriaMultiValueMap = new LinkedMultiValueMap<>();
        objectMapper.convertValue(searchCriteria, new TypeReference<Map<String, String>>() {
        }).forEach(fileSearchCriteriaMultiValueMap::add);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(searchingUrl)
                .queryParams(fileSearchCriteriaMultiValueMap);
        ResponseEntity<String> response = new RestTemplate().exchange(
                uriBuilder.build().toString(),
                HttpMethod.GET,
                new HttpEntity<>(apiDetailsHandler.getHttpHeaders(userName, password)),
                String.class);

        JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        searchCriteria.setTotalRows(objectMapper.convertValue(root.get("totalRows"), Integer.class));
        return objectMapper.convertValue(
                root.get("results"),
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, elementClass)
        );
    }

    private <T> PageImpl<T> convertListToPage(SearchCriteria searchCriteria, List<T> results) {
        return new PageImpl<>(Optional.ofNullable(results).orElseGet(ArrayList::new),
                of(searchCriteria.getPage(), searchCriteria.getSize()), searchCriteria.getTotalRows());
    }

    public Page<WorkflowStep> getWorkflowSteps(Integer workFlowId, Integer page, Integer size) throws JsonProcessingException {
        ResponseEntity<String> response = new RestTemplate().exchange(
                workflowStepsUrl + "?fieldList=Full&workFlowId=" + workFlowId,
                HttpMethod.GET,
                new HttpEntity<>(apiDetailsHandler.getHttpHeaders(userName, password)),
                String.class);
        List<WorkflowStep> workflowSteps = Optional.ofNullable(objectMapper.convertValue(
                objectMapper.readTree(Objects.requireNonNull(response.getBody())), new TypeReference<List<WorkflowStep>>() {
                })).orElseGet(ArrayList::new);
        workflowSteps.sort(Comparator.comparing(WorkflowStep::getStepId));
        Page<WorkflowStep> workflows = ListToPageConverter.convertListToPage(workflowSteps, of(page, size));
        WFPage<WorkflowStep> wfPage = new WFPage<>(workflows.getContent(), workflows.getPageable(), workflows.getTotalElements());
        if (!workflowSteps.isEmpty()) {
            boolean isStatusError = false;
            WorkflowStep firstWorkflowStep = workflowSteps.get(0);
            for (WorkflowStep workflowStep : workflowSteps) {
                if (!firstWorkflowStep.getWfdId().equals(workflowStep.getWfdId()) ||
                        !firstWorkflowStep.getWfdVersion().equals(workflowStep.getWfdVersion())) {
                    workflowStep.setInlineInvocation(true);
                }

                if (workflowStep.getExeState().contains("Error")) isStatusError = true;
            }
            if (isStatusError) {
                wfPage.setStatus("Error");
            } else {
                wfPage.setStatus("Success");
            }
        }
        return wfPage;
    }

    public BPDetails getBPDetails(String identifier) throws JsonProcessingException {
        ResponseEntity<String> response = new RestTemplate().exchange(
                workflowsUrl + identifier,
                HttpMethod.GET,
                new HttpEntity<>(apiDetailsHandler.getHttpHeaders(userName, password)),
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

    @Cacheable(cacheNames = CACHE_BP_HEADERS)
    public List<BPName> getBPNames() throws JsonProcessingException {
        int fromRange = 0;
        int toRange = RANGE;
        List<BPName> bpNames = new ArrayList<>();
        List<BPName> bpNamesByRange = getBPByRange(fromRange, toRange);
        while (!bpNamesByRange.isEmpty()) {
            bpNames.addAll(bpNamesByRange);
            fromRange = toRange + 1;
            toRange = fromRange + RANGE;
            bpNamesByRange = getBPByRange(fromRange, toRange);
        }
        return bpNames;
    }

    private List<BPName> getBPByRange(int fromRange, int toRange) throws JsonProcessingException {
        ResponseEntity<String> response = new RestTemplate().exchange(
                workflowsUrl + "?_include=wfdVersion,wfdID,name&_range=" + fromRange + "-" + toRange +
                        "&fieldList=brief",
                HttpMethod.GET,
                new HttpEntity<>(apiDetailsHandler.getHttpHeaders(userName, password)),
                String.class);
        return objectMapper.convertValue(objectMapper.readTree(Objects.requireNonNull(response.getBody())),
                new TypeReference<List<BPName>>() {
                });
    }

    public Optional<List<File>> getFileMonitor() throws JsonProcessingException {
        return Optional.ofNullable(getListFromSBI(new FileSearchCriteria(), fileSearchUrl, File.class));
    }

    public Map<String, Integer> getWfIdAndVersionByWfcId(String wfcId) throws JsonProcessingException {
        ResponseEntity<String> response = new RestTemplate().exchange(
                workflowStepsUrl + wfcId,
                HttpMethod.GET,
                new HttpEntity<>(apiDetailsHandler.getHttpHeaders(userName, password)),
                String.class);
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        Map<String, Integer> map = new HashMap<>();
        map.put("workFlowId", objectMapper.convertValue(root.get("workFlowId"), Integer.class));
        map.put("wfdVersion", objectMapper.convertValue(root.get("wfdVersion"), Integer.class));
        return map;
    }

    private List<SEPAFile> getSepaFilesForReport(String from, String to) throws JsonProcessingException {
        FileSearchCriteria fileSearchCriteria = new FileSearchCriteria();
        Optional.ofNullable(from).ifPresent(fileSearchCriteria::setFrom);
        Optional.ofNullable(to).ifPresent(fileSearchCriteria::setTo);
        Set<ConstraintViolation<FileSearchCriteria>> violations = validator.validate(fileSearchCriteria);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        fileSearchCriteria.setSize(propertyService.getFileMaxValueForReport());
        List<SEPAFile> files = getListFromSBI(fileSearchCriteria, fileSearchUrl, SEPAFile.class);
        Optional.ofNullable(files).orElseThrow(
                () -> new FileNotFoundException("{\"message\": \"No files matched your search criteria\"}"));
        return files;
    }

    public ByteArrayInputStream generateReportForSEPAFiles(String from, String to, ReportType reportType) throws IOException {
        List<SEPAFile> files = getSepaFilesForReport(from, to);
        if (reportType.equals(ReportType.EXCEL)) {
            return exportExcelService.generateExcelReport(files);
        } else {
            return exportPDFService.generatePDFReport(files);
        }

    }

    public ByteArrayInputStream generateReportForTransactions(
            Integer fileId, String fileName, Integer size, ReportType reportType)
            throws IOException {
        List<Transaction> transactions = getTransactionsForReport(fileId, size);
        if (reportType.equals(ReportType.EXCEL)) {
            return exportExcelService.generateExcelReportForTransactions(transactions, fileName);
        } else {
            return exportPDFService.generatePDFReportForTransactions(transactions, fileName, fileId);
        }
    }

    private List<Transaction> getTransactionsForReport(Integer fileId, Integer size) throws JsonProcessingException {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setSize(Integer.min(size, propertyService.getTrxMaxValueForReport()));
        List<Transaction> transactions = getListFromSBI(searchCriteria,
                fileSearchUrl + "/" + fileId + "/transactions", Transaction.class);
        Optional.ofNullable(transactions).orElseThrow(FileTransactionNotFoundException::new);
        return transactions;
    }
}
