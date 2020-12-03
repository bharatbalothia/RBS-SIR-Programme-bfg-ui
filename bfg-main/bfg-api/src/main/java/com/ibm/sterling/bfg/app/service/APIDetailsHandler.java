package com.ibm.sterling.bfg.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.exception.changecontrol.InvalidUserForUpdateChangeControlException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class APIDetailsHandler {

    public String extractErrorMessage(String message, String messageKey) {
        return Optional.ofNullable(message)
                .map(errorMessage -> String.valueOf(
                        Optional.ofNullable(getErrorListFromErrorMessage(errorMessage))
                                .map(errorList -> errorList.get(0).get(messageKey))
                                .orElse(null)
                        )
                ).orElse(null);
    }

    public Map<String, List<Object>> processErrorMessage(HttpStatusCodeException e) {
        return Optional.ofNullable(e.getMessage()).map(errorMessage ->
                Optional.ofNullable(getErrorListFromErrorMessage(errorMessage)).map(errors -> {
                    Map<String, List<Object>> errorMap = new HashMap<>();
                    errors.forEach(error -> {
                        String key = String.valueOf(error.get("attribute"));
                        if (errorMap.containsKey(key))
                            errorMap.get(key).add(error.get("message"));
                        else
                            errorMap.put(key, new ArrayList<>(Collections.singletonList(error.get("message"))));
                    });
                    return errorMap;
                }).orElseGet(() -> Collections.singletonMap("error", Collections.singletonList(errorMessage)))).orElse(null);
    }

    private List<Map<String, Object>> getErrorListFromErrorMessage(String errorMessage) {
        List<Map<String, Object>> errorList = null;
        Matcher matcher = Pattern.compile("\\{.*}", Pattern.DOTALL).matcher(errorMessage);
        if (matcher.find()) {
            String errorMessageList = "[" + matcher.group(0) + "]";
            try {
                errorList = new ObjectMapper().readValue(errorMessageList, new TypeReference<List<Map<String, Object>>>() {
                });
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
        }
        return errorList;
    }

    public HttpHeaders getHttpHeaders(String userName, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(userName, password);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    public void checkPermissionForUpdateChangeControl(String changer) {
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals(changer))
            throw new InvalidUserForUpdateChangeControlException();
    }

}
