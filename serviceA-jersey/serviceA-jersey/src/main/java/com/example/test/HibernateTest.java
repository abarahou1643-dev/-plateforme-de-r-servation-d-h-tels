package com.example.test;
import com.example.model.Item;
import com.example.model.Category;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class HibernateTest {

    public static void main(String[] args) {

        // ✅ ouvrir une session Hibernate
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Transaction tx = session.beginTransaction();

            // ✅ 1️⃣ Créer et enregistrer des catégories
            Category c1 = new Category();
            c1.setCode("C001");
            c1.setName("Informatique");
            c1.setUpdatedAt(LocalDateTime.now());
            session.persist(c1);

            Category c2 = new Category();
            c2.setCode("C002");
            c2.setName("Électronique");
            c2.setUpdatedAt(LocalDateTime.now());
            session.persist(c2);

            // ✅ 2️⃣ Créer et enregistrer des produits (items)
            Item i1 = new Item();
            i1.setSku("I001");
            i1.setName("Clavier Mécanique");
            i1.setPrice(new BigDecimal("450.00"));
            i1.setStock(10);
            i1.setUpdatedAt(LocalDateTime.now());
            i1.setCategory(c1);
            session.persist(i1);

            Item i2 = new Item();
            i2.setSku("I002");
            i2.setName("Souris sans fil");
            i2.setPrice(new BigDecimal("199.99"));
            i2.setStock(25);
            i2.setUpdatedAt(LocalDateTime.now());
            i2.setCategory(c1);
            session.persist(i2);

            Item i3 = new Item();
            i3.setSku("I003");
            i3.setName("Écran LED 24 pouces");
            i3.setPrice(new BigDecimal("1499.99"));
            i3.setStock(5);
            i3.setUpdatedAt(LocalDateTime.now());
            i3.setCategory(c2);
            session.persist(i3);

            tx.commit();
            System.out.println("✅ Catégories et items enregistrés avec succès dans PostgreSQL !");

            // ✅ 3️⃣ Lire les données de la BD
            List<Category> categories = session.createQuery("from Category", Category.class).list();

            for (Category c : categories) {
                System.out.println("🔹 Catégorie: " + c.getName());
                if (c.getItems() != null) {
                    c.getItems().forEach(it ->
                            System.out.println("   → " + it.getName() + " (" + it.getPrice() + " MAD)")
                    );
                }
            }

            // ✅ 4️⃣ Exemple de mise à jour
            Transaction tx2 = session.beginTransaction();
            Item itemToUpdate = session.get(Item.class, i1.getId());
            if (itemToUpdate != null) {
                itemToUpdate.setPrice(new BigDecimal("499.99"));
                itemToUpdate.setStock(15);
                session.merge(itemToUpdate);
                System.out.println("✏️ Item mis à jour: " + itemToUpdate.getName());
            }
            tx2.commit();

            // ✅ 5️⃣ Exemple de suppression
            Transaction tx3 = session.beginTransaction();
            Item itemToDelete = session.get(Item.class, i2.getId());
            if (itemToDelete != null) {
                session.remove(itemToDelete);
                System.out.println("🗑️ Item supprimé: " + itemToDelete.getName());
            }
            tx3.commit();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
