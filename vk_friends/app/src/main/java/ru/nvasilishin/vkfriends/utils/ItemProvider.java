package ru.nvasilishin.vkfriends.utils;

import java.util.NoSuchElementException;

/**
 * Created by Nick on 27.01.2016.
 */
public interface ItemProvider<Item, Identificator> {

    public Item[] fetchAll();
    public Item[] fetchAllOrWait();
    public Item fetch(Identificator identificator) throws NoSuchElementException;
}
