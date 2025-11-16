package com.example.exception;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Global exception mapper for ResourceNotFoundException
 */
@Provider
public class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {
    
    @Context
    private UriInfo uriInfo;
    
    @Override
    public Response toResponse(ResourceNotFoundException exception) {
        ErrorResponse error = new ErrorResponse(
            Response.Status.NOT_FOUND.getStatusCode(),
            "Not Found",
            exception.getMessage(),
            uriInfo != null ? uriInfo.getPath() : null
        );
        
        return Response.status(Response.Status.NOT_FOUND)
                .entity(error)
                .build();
    }
}
