package com.example.dao;

import com.example.exception.DatabaseException;
import com.example.model.Category;
import com.example.model.Item;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Category entity
 * Handles all database operations for Category
 */
public class CategoryDAO {

    /**
     * Find all categories with pagination
     * 
     * @param page Page number (0-based)
     * @param size Number of items per page
     * @return List of categories
     */
    public List<Category> findAll(int page, int size) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Category> query = session.createQuery("FROM Category ORDER BY id", Category.class);
            query.setFirstResult(page * size);
            query.setMaxResults(size);
            return query.list();
        } catch (Exception e) {
            throw new DatabaseException("Error fetching categories", e);
        }
    }

    /**
     * Find a category by ID
     * 
     * @param id Category ID
     * @return Optional containing the category if found
     */
    public Optional<Category> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Category category = session.get(Category.class, id);
            return Optional.ofNullable(category);
        } catch (Exception e) {
            throw new DatabaseException("Error fetching category by id: " + id, e);
        }
    }

    /**
     * Find a category by code
     * 
     * @param code Category code
     * @return Optional containing the category if found
     */
    public Optional<Category> findByCode(String code) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Category> query = session.createQuery(
                "FROM Category WHERE code = :code", Category.class);
            query.setParameter("code", code);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            throw new DatabaseException("Error fetching category by code: " + code, e);
        }
    }

    /**
     * Save a new category
     * 
     * @param category Category to save
     * @return Saved category with generated ID
     */
    public Category save(Category category) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            category.setUpdatedAt(LocalDateTime.now());
            session.persist(category);
            transaction.commit();
            return category;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DatabaseException("Error saving category", e);
        }
    }

    /**
     * Update an existing category
     * 
     * @param category Category to update
     * @return Updated category
     */
    public Category update(Category category) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            category.setUpdatedAt(LocalDateTime.now());
            Category updated = session.merge(category);
            transaction.commit();
            return updated;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DatabaseException("Error updating category", e);
        }
    }

    /**
     * Delete a category by ID
     * 
     * @param id Category ID
     * @return true if deleted, false if not found
     */
    public boolean delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Category category = session.get(Category.class, id);
            if (category != null) {
                session.remove(category);
                transaction.commit();
                return true;
            }
            transaction.commit();
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DatabaseException("Error deleting category", e);
        }
    }

    /**
     * Find all items belonging to a category
     * 
     * @param categoryId Category ID
     * @return List of items
     */
    public List<Item> findItemsByCategory(Long categoryId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Item> query = session.createQuery(
                "FROM Item WHERE category.id = :catId ORDER BY id", Item.class);
            query.setParameter("catId", categoryId);
            return query.getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Error fetching items for category: " + categoryId, e);
        }
    }

    /**
     * Count total number of categories
     * 
     * @return Total count
     */
    public long count() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(c) FROM Category c", Long.class);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new DatabaseException("Error counting categories", e);
        }
    }

    /**
     * Check if a category exists by ID
     * 
     * @param id Category ID
     * @return true if exists
     */
    public boolean existsById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(c) FROM Category c WHERE c.id = :id", Long.class);
            query.setParameter("id", id);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            throw new DatabaseException("Error checking category existence", e);
        }
    }

    /**
     * Check if a category code already exists
     * 
     * @param code Category code
     * @param excludeId ID to exclude (for updates)
     * @return true if code exists
     */
    public boolean existsByCode(String code, Long excludeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = excludeId == null 
                ? "SELECT COUNT(c) FROM Category c WHERE c.code = :code"
                : "SELECT COUNT(c) FROM Category c WHERE c.code = :code AND c.id != :excludeId";
            
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("code", code);
            if (excludeId != null) {
                query.setParameter("excludeId", excludeId);
            }
            
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            throw new DatabaseException("Error checking category code uniqueness", e);
        }
    }
}
