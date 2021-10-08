package io.github.jessicacarneiro.apisrest.infrastructure;

import io.github.jessicacarneiro.apisrest.domain.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
}
