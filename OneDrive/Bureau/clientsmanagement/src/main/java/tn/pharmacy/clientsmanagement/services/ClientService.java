package tn.pharmacy.clientsmanagement.services;

import tn.pharmacy.clientsmanagement.entities.Client;
import tn.pharmacy.clientsmanagement.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClientService implements IClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Client addClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public Client updateClient(Long id, Client clientDetails) {
        Client client = getClientById(id);
        if (client != null) {
            client.setFirstName(clientDetails.getFirstName());
            client.setLastName(clientDetails.getLastName());
            client.setEmail(clientDetails.getEmail());
            client.setPhoneNumber(clientDetails.getPhoneNumber());
            return clientRepository.save(client);
        }
        return null;
    }

    @Override
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    @Override
    public List<Client> searchClients(String firstName, String lastName, String email) {
        if (firstName != null && lastName != null) {
            return clientRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(firstName, lastName);
        } else if (firstName != null) {
            return clientRepository.findByFirstNameContainingIgnoreCase(firstName);
        } else if (lastName != null) {
            return clientRepository.findByLastNameContainingIgnoreCase(lastName);
        } else if (email != null) {
            return clientRepository.findByEmailContainingIgnoreCase(email);
        } else {
            return clientRepository.findAll();
        }
    }



}
