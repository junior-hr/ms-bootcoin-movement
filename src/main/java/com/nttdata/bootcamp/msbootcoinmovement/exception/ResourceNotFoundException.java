package com.nttdata.bootcamp.msbootcoinmovement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class ResourceNotFoundException.
 * BootcoinMovement microservice class ResourceNotFoundException.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Getter
@Setter
@NoArgsConstructor
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String resourceName;
    private String fieldName;
    private String fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s no encontrada: %s = '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public ResourceNotFoundException(String resourceName) {
        super(String.format(resourceName));
        this.resourceName = resourceName;
    }

}
