package me.anisjamadar.onlinestore.repositories;

import me.anisjamadar.onlinestore.domain.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {

}