package io.github.jessicacarneiro.apisrest.domain;

import io.github.jessicacarneiro.apisrest.domain.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
}
