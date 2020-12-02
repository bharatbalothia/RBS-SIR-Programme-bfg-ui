package com.ibm.sterling.bfg.app.exception.file;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "There is no such BP header")
public class BPHeaderNotFoundException extends RuntimeException {
}
