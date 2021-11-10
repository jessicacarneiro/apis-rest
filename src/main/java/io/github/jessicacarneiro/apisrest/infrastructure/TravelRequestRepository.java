package io.github.jessicacarneiro.apisrest.infrastructure;

import io.github.jessicacarneiro.apisrest.domain.TravelRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelRequestRepository extends JpaRepository<TravelRequest, Long> {
}
