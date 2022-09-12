package fpt.capstone.repository;

import fpt.capstone.entities.ConfEntryKeyDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfEntryKeyDetailsRepository extends JpaRepository<ConfEntryKeyDetails, Integer> {
}
