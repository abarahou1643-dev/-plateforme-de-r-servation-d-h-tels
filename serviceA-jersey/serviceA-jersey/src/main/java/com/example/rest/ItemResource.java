package com.example.rest;

import com.example.dto.ItemRequestDTO;
import com.example.dto.ItemResponseDTO;
import com.example.service.ItemService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST API endpoints for Item operations
 * Handles HTTP requests and delegates business logic to ItemService
 */
@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource {

    private final ItemService itemService;

    public ItemResource() {
        this.itemService = new ItemService();
    }

    // Constructor for dependency injection (useful for testing)
    public ItemResource(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * GET /items - Get all items with pagination
     * 
     * @param page Page number (default: 0)
     * @param size Page size (default: 10)
     * @return List of items
     */
    @GET
    public Response getAllItems(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        
        List<ItemResponseDTO> items = itemService.getAllItems(page, size);
        return Response.ok(items).build();
    }

    /**
     * GET /items/{id} - Get an item by ID
     * 
     * @param id Item ID
     * @return Item details
     */
    @GET
    @Path("/{id}")
    public Response getItemById(@PathParam("id") Long id) {
        ItemResponseDTO item = itemService.getItemById(id);
        return Response.ok(item).build();
    }

    /**
     * GET /items/search - Search items by name
     * 
     * @param keyword Search keyword
     * @return List of matching items
     */
    @GET
    @Path("/search")
    public Response searchItems(@QueryParam("keyword") String keyword) {
        List<ItemResponseDTO> items = itemService.searchItemsByName(keyword);
        return Response.ok(items).build();
    }

    /**
     * GET /items/by-category/{categoryId} - Get items by category
     * 
     * @param categoryId Category ID
     * @return List of items in the category
     */
    @GET
    @Path("/by-category/{categoryId}")
    public Response getItemsByCategory(@PathParam("categoryId") Long categoryId) {
        List<ItemResponseDTO> items = itemService.getItemsByCategoryId(categoryId);
        return Response.ok(items).build();
    }

    /**
     * POST /items - Create a new item
     * 
     * @param requestDTO Item data
     * @return Created item with location header
     */
    @POST
    public Response createItem(ItemRequestDTO requestDTO) {
        ItemResponseDTO created = itemService.createItem(requestDTO);
        
        // Build location URI for the created resource
        URI location = UriBuilder.fromResource(ItemResource.class)
                .path("{id}")
                .build(created.getId());
        
        return Response.created(location)
                .entity(created)
                .build();
    }

    /**
     * PUT /items/{id} - Update an existing item
     * 
     * @param id Item ID
     * @param requestDTO Updated item data
     * @return Updated item
     */
    @PUT
    @Path("/{id}")
    public Response updateItem(
            @PathParam("id") Long id,
            ItemRequestDTO requestDTO) {
        
        ItemResponseDTO updated = itemService.updateItem(id, requestDTO);
        return Response.ok(updated).build();
    }

    /**
     * PATCH /items/{id}/stock - Update item stock
     * 
     * @param id Item ID
     * @param quantity Stock change (can be negative)
     * @return Updated item
     */
    @PATCH
    @Path("/{id}/stock")
    public Response updateStock(
            @PathParam("id") Long id,
            @QueryParam("quantity") int quantity) {
        
        ItemResponseDTO updated = itemService.updateStock(id, quantity);
        return Response.ok(updated).build();
    }

    /**
     * DELETE /items/{id} - Delete an item
     * 
     * @param id Item ID
     * @return No content on success
     */
    @DELETE
    @Path("/{id}")
    public Response deleteItem(@PathParam("id") Long id) {
        itemService.deleteItem(id);
        return Response.noContent().build();
    }

    /**
     * GET /items/count - Get total count of items
     * 
     * @return Total count
     */
    @GET
    @Path("/count")
    public Response getItemCount() {
        long count = itemService.getItemCount();
        return Response.ok()
                .entity("{\"count\": " + count + "}")
                .build();
    }
}
