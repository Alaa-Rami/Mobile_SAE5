package tn.pharmacy.clientsmanagement.repositories;

import tn.pharmacy.clientsmanagement.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByEmail(String email);
    // Rechercher par prénom
    List<Client> findByFirstNameContainingIgnoreCase(String firstName);

    // Rechercher par nom de famille
    List<Client> findByLastNameContainingIgnoreCase(String lastName);

    // Rechercher par prénom et nom de famille
    List<Client> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);

    // Rechercher par email
    List<Client> findByEmailContainingIgnoreCase(String email);
}
