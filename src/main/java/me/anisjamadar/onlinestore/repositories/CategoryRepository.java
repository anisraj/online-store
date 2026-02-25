package me.anisjamadar.onlinestore.repositories;

import me.anisjamadar.onlinestore.domain.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Byte> {
}