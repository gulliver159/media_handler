package com.neuron.exception;

import io.swagger.model.Error;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class ServerException extends Exception {
    Error error;
}
