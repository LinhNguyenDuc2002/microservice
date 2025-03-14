package com.example.userservice.redis.repo;

import com.example.userservice.redis.model.UserCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCacheRepository extends CrudRepository<UserCache, String> {
}
