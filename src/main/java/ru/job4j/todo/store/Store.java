package ru.job4j.todo.store;

import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.util.List;

public interface Store {
    List<Item> findAll();

    Item add(Item item);

    User add(User user);

    Item findItemById(int id);

    User findUserById(int id);

    User findUserByEmail(String email);

    int setDone(int id);
}