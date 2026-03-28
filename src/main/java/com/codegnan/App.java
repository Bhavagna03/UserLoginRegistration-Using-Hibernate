package com.codegnan;

import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import com.codegnan.entity.User;

public class App {

    public static void main(String[] args) {

        StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .configure("hibernate.cfg.xml")
                        .build();

        Metadata metadata = new MetadataSources(registry)
                .getMetadataBuilder()
                .build();

        SessionFactory sessionFactory =
                metadata.getSessionFactoryBuilder().build();

        Scanner sc = new Scanner(System.in);

        try (Session session = sessionFactory.openSession()) {

            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                register(session, sc);
            }
            else if (choice == 2) {
                login(session, sc);
            }

        } finally {
            sessionFactory.close();
            StandardServiceRegistryBuilder.destroy(registry);
            sc.close();
        }
    }

    // ---------------- REGISTER ----------------
    private static void register(Session session, Scanner sc) {

        System.out.print("Username: ");
        String uname = sc.nextLine();

        System.out.print("Password: ");
        String pass = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        Transaction tx = session.beginTransaction();

        User user = new User(uname, pass, email);
        session.persist(user);
        

        tx.commit();

        System.out.println("\n Registration Successful!");
    }
    private static void register(Session session) {

        Transaction tx = session.beginTransaction();

        User u1 = new User("sameed", "123", "sameed@gmail.com");

        session.persist(u1);

        tx.commit();

        System.out.println("User inserted!");
    }

    // ---------------- LOGIN ----------------
    private static void login(Session session, Scanner sc) {

        System.out.print("Username: ");
        String uname = sc.nextLine();

        System.out.print("Password: ");
        String pass = sc.nextLine();

        String hql = "from User where username=:u and password=:p";

        List<User> users = session.createQuery(hql, User.class)
                .setParameter("u", uname)
                .setParameter("p", pass)
                .list();

        if (users.isEmpty()) {
            System.out.println("\n Invalid Credentials!");
        } else {
            System.out.println("\n Login Successful!");
            System.out.println(users.get(0));
        }
    }
}
