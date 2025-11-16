package com.example.rest;

import com.example.dto.CategoryRequestDTO;
import com.example.dto.CategoryResponseDTO;
import com.example.service.CategoryService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST API endpoints for Category operations
 * Handles HTTP requests and delegates business logic to CategoryService
 */
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {

    private final CategoryService categoryService;

    public CategoryResource() {
        this.categoryService = new CategoryService();
    }

    // Constructor for dependency injection (useful for testing)
    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * GET /categories - Get all categories with pagination
     * 
     * @param page Page number (default: 0)
     * @param size Page size (default: 10)
     * @return List of categories
     */
    @GET
    public Response getAllCategories(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        
        List<CategoryResponseDTO> categories = categoryService.getAllCategories(page, size);
        return Response.ok(categories).build();
    }

    /**
     * GET /categories/{id} - Get a category by ID
     * 
     * @param id Category ID
     * @return Category details
     */
    @GET
    @Path("/{id}")
    public Response getCategoryById(@PathParam("id") Long id) {
        CategoryResponseDTO category = categoryService.getCategoryById(id);
        return Response.ok(category).build();
    }

    /**
     * GET /categories/{id}/with-items - Get a category with its items
     * 
     * @param id Category ID
     * @return Category details with items
     */
    @GET
    @Path("/{id}/with-items")
    public Response getCategoryWithItems(@PathParam("id") Long id) {
        CategoryResponseDTO category = categoryService.getCategoryWithItems(id);
        return Response.ok(category).build();
    }

    /**
     * POST /categories - Create a new category
     * 
     * @param requestDTO Category data
     * @return Created category with location header
     */
    @POST
    public Response createCategory(CategoryRequestDTO requestDTO) {
        CategoryResponseDTO created = categoryService.createCategory(requestDTO);
        
        // Build location URI for the created resource
        URI location = UriBuilder.fromResource(CategoryResource.class)
                .path("{id}")
                .build(created.getId());
        
        return Response.created(location)
                .entity(created)
                .build();
    }

    /**
     * PUT /categories/{id} - Update an existing category
     * 
     * @param id Category ID
     * @param requestDTO Updated category data
     * @return Updated category
     */
    @PUT
    @Path("/{id}")
    public Response updateCategory(
            @PathParam("id") Long id,
            CategoryRequestDTO requestDTO) {
        
        CategoryResponseDTO updated = categoryService.updateCategory(id, requestDTO);
        return Response.ok(updated).build();
    }

    /**
     * DELETE /categories/{id} - Delete a category
     * 
     * @param id Category ID
     * @return No content on success
     */
    @DELETE
    @Path("/{id}")
    public Response deleteCategory(@PathParam("id") Long id) {
        categoryService.deleteCategory(id);
        return Response.noContent().build();
    }

    /**
     * GET /categories/count - Get total count of categories
     * 
     * @return Total count
     */
    @GET
    @Path("/count")
    public Response getCategoryCount() {
        long count = categoryService.getCategoryCount();
        return Response.ok()
                .entity("{\"count\": " + count + "}")
                .build();
    }
}
