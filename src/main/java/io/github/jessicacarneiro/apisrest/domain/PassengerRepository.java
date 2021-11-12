package io.github.jessicacarneiro.apisrest.domain;

import io.github.jessicacarneiro.apisrest.domain.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
