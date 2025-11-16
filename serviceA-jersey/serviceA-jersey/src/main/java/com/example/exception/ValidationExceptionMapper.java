package com.example.exception;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Global exception mapper for ValidationException
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {
    
    @Context
    private UriInfo uriInfo;
    
    @Override
    public Response toResponse(ValidationException exception) {
        ErrorResponse error = new ErrorResponse(
            Response.Status.BAD_REQUEST.getStatusCode(),
            "Validation Error",
            exception.getMessage(),
            uriInfo != null ? uriInfo.getPath() : null
        );
        
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(error)
                .build();
    }
}
