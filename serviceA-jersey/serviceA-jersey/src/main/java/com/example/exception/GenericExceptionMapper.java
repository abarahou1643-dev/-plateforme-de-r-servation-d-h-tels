package com.example.exception;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Global exception mapper for generic exceptions
 */
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {
    
    @Context
    private UriInfo uriInfo;
    
    @Override
    public Response toResponse(Exception exception) {
        // Log the exception (in production, use proper logging)
        exception.printStackTrace();
        
        ErrorResponse error = new ErrorResponse(
            Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
            "Internal Server Error",
            "An unexpected error occurred: " + exception.getMessage(),
            uriInfo != null ? uriInfo.getPath() : null
        );
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(error)
                .build();
    }
}
