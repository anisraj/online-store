package me.anisjamadar.onlinestore.repositories;

import me.anisjamadar.onlinestore.domain.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}