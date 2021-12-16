package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.todo.model.Item;

import javax.persistence.Query;
import java.util.List;

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
        Session session = sf.openSession();
        session.beginTransaction();
        List<Item> result = session.createQuery("from ru.job4j.todo.model.Item ORDER BY id").list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public Item add(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
        session.close();
        return item;
    }

    @Override
    public Item findById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Item result = session.get(Item.class, id);
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public int setDone(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Query query = session.createQuery(
                "update Item i set i.done = :newDone where i.id = :fId"
        );
        query.setParameter("newDone", true);
        query.setParameter("fId", id);
        int rsl = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        return rsl;
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
