package com.example.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
            System.out.println("✅ Hibernate SessionFactory créée avec succès !");
        } catch (Throwable ex) {
            System.err.println("❌ Erreur lors de la création de la SessionFactory : " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    // ✅ Fermeture propre lors de l’arrêt de l’application
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            System.out.println("🧹 Hibernate SessionFactory fermée proprement.");
        }
    }
}
