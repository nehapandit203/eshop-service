package dev.eshop.product.repositories;

public interface CustomQueries {
    String FIND_CATEGORY_BY_NAME =  "SELECT c.name FROM category c WHERE LOWER(c.name) = LOWER(:name)";


}
