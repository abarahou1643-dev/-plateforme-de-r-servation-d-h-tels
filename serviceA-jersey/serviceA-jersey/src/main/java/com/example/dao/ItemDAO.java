package com.example.dao;

import com.example.exception.DatabaseException;
import com.example.model.Item;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Item entity
 * Handles all database operations for Item
 */
public class ItemDAO {

    /**
     * Find all items with pagination
     * 
     * @param page Page number (0-based)
     * @param size Number of items per page
     * @return List of items
     */
    public List<Item> findAll(int page, int size) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Use JOIN FETCH to eagerly load the category relationship
            Query<Item> query = session.createQuery(
                "FROM Item i LEFT JOIN FETCH i.category ORDER BY i.id", Item.class);
            query.setFirstResult(page * size);
            query.setMaxResults(size);
            return query.list();
        } catch (Exception e) {
            throw new DatabaseException("Error fetching items", e);
        }
    }

    /**
     * Find an item by ID
     * 
     * @param id Item ID
     * @return Optional containing the item if found
     */
    public Optional<Item> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Use JOIN FETCH to eagerly load the category relationship
            Query<Item> query = session.createQuery(
                "FROM Item i LEFT JOIN FETCH i.category WHERE i.id = :id", Item.class);
            query.setParameter("id", id);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            throw new DatabaseException("Error fetching item by id: " + id, e);
        }
    }

    /**
     * Find an item by SKU
     * 
     * @param sku Item SKU
     * @return Optional containing the item if found
     */
    public Optional<Item> findBySku(String sku) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Item> query = session.createQuery(
                "FROM Item i LEFT JOIN FETCH i.category WHERE i.sku = :sku", Item.class);
            query.setParameter("sku", sku);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            throw new DatabaseException("Error fetching item by SKU: " + sku, e);
        }
    }

    /**
     * Save a new item
     * 
     * @param item Item to save
     * @return Saved item with generated ID
     */
    public Item save(Item item) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            item.setUpdatedAt(LocalDateTime.now());
            session.persist(item);
            transaction.commit();
            return item;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DatabaseException("Error saving item", e);
        }
    }

    /**
     * Update an existing item
     * 
     * @param item Item to update
     * @return Updated item
     */
    public Item update(Item item) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            item.setUpdatedAt(LocalDateTime.now());
            Item updated = session.merge(item);
            transaction.commit();
            return updated;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DatabaseException("Error updating item", e);
        }
    }

    /**
     * Delete an item by ID
     * 
     * @param id Item ID
     * @return true if deleted, false if not found
     */
    public boolean delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Item item = session.get(Item.class, id);
            if (item != null) {
                session.remove(item);
                transaction.commit();
                return true;
            }
            transaction.commit();
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DatabaseException("Error deleting item", e);
        }
    }

    /**
     * Search items by name (case-insensitive)
     * 
     * @param keyword Search keyword
     * @return List of matching items
     */
    public List<Item> searchByName(String keyword) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Item> query = session.createQuery(
                "FROM Item i LEFT JOIN FETCH i.category WHERE LOWER(i.name) LIKE LOWER(:kw) ORDER BY i.id", Item.class);
            query.setParameter("kw", "%" + keyword + "%");
            return query.getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Error searching items by name", e);
        }
    }

    /**
     * Find items by category ID
     * 
     * @param categoryId Category ID
     * @return List of items
     */
    public List<Item> findByCategoryId(Long categoryId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Item> query = session.createQuery(
                "FROM Item i LEFT JOIN FETCH i.category WHERE i.category.id = :catId ORDER BY i.id", Item.class);
            query.setParameter("catId", categoryId);
            return query.getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Error fetching items by category", e);
        }
    }

    /**
     * Count total number of items
     * 
     * @return Total count
     */
    public long count() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(i) FROM Item i", Long.class);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new DatabaseException("Error counting items", e);
        }
    }

    /**
     * Check if an item exists by ID
     * 
     * @param id Item ID
     * @return true if exists
     */
    public boolean existsById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(i) FROM Item i WHERE i.id = :id", Long.class);
            query.setParameter("id", id);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            throw new DatabaseException("Error checking item existence", e);
        }
    }

    /**
     * Check if an item SKU already exists
     * 
     * @param sku Item SKU
     * @param excludeId ID to exclude (for updates)
     * @return true if SKU exists
     */
    public boolean existsBySku(String sku, Long excludeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = excludeId == null 
                ? "SELECT COUNT(i) FROM Item i WHERE i.sku = :sku"
                : "SELECT COUNT(i) FROM Item i WHERE i.sku = :sku AND i.id != :excludeId";
            
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("sku", sku);
            if (excludeId != null) {
                query.setParameter("excludeId", excludeId);
            }
            
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            throw new DatabaseException("Error checking item SKU uniqueness", e);
        }
    }
}
