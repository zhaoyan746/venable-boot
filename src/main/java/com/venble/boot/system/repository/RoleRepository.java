package com.venble.boot.system.repository;

import com.venble.boot.system.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author chenxc
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

}
