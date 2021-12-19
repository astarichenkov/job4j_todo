package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import javax.persistence.*;
import java.util.List;
import java.util.function.Function;

public class HbmStore implements Store, AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private static final class Lazy {
        private static final Store INST = new HbmStore();
    }

    private HbmStore() {
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public List<Item> findAll() {
        return this.tx(
                session -> session.createQuery("from ru.job4j.todo.model.Item ORDER BY id").list()
        );
    }

    @Override
    public Item add(Item item) {
        return (Item) this.tx(
                session -> session.save(item)
        );
    }

    @Override
    public User add(User user) {
        Session session = sf.openSession();
        session.beginTransaction();
        System.out.println("hbm-add" + user);
        session.save(user);
        session.getTransaction().commit();
        session.close();
        return user;
    }

    @Override
    public Item findItemById(int id) {
        return this.tx(
                session -> session.get(Item.class, id)
        );
    }

    @Override
    public int setDone(int id) {
        return this.tx(
                session -> {
                    return session.createQuery(
                                    "update Item i set i.done = :newDone where i.id = :fId"
                            ).setParameter("newDone", true)
                            .setParameter("fId", id)
                            .executeUpdate();

                });
    }

    private <T> T tx(final Function<Session, T> command) {
        Session session = sf.openSession();
        session.beginTransaction();
        T result = command.apply(session);
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public User findUserById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        User result = session.get(User.class, id);
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public User findUserByEmail(String email) {
        Session session = sf.openSession();
        session.beginTransaction();

        Query query = session.createQuery(
                "from User u where u.email = :email"
        );
        query.setParameter("email", email);

        User result = (User) query.getSingleResult();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
