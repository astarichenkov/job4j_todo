package ru.job4j.todo.store;

import ru.job4j.todo.model.Item;

import java.util.List;

public interface Store {
    List<Item> findAll();

    Item add(Item item);

    Item findById(int id);

    int setDone(int id);
}