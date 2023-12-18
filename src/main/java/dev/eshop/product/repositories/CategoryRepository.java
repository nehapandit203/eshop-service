package dev.eshop.product.repositories;

import dev.eshop.product.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Override
    Optional<Category> findById(UUID uuid);

    @Override
    List<Category> findAllById(Iterable<UUID> uuids);

    // get category name from category id
    @Query(value = "SELECT name FROM Category WHERE id = :id", nativeQuery = true)
    String findNameById(@Param("id") UUID id);

    @Query(value = "SELECT id FROM Product WHERE category IN (SELECT id FROM Categorōy WHERE name = :category)", nativeQuery = true)
    List<String> findAllByCategoryIn(@Param("category") String category);

   // @Query(value = CustomQueries.FIND_CATEGORY_BY_NAME, nativeQuery = true)
    Optional<Category> findByNameIgnoreCase(String name);

}
