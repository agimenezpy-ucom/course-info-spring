package com.pluralsight.courseinfo.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resourse not found")
public class NotFoundException extends RuntimeException {

}
