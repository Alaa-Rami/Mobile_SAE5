package tn.pharmacy.clientsmanagement.services;

import tn.pharmacy.clientsmanagement.entities.Client;
import java.util.List;

public interface IClientService {
    Client addClient(Client client);
    List<Client> getAllClients();
    Client getClientById(Long id);
    Client updateClient(Long id, Client client);
    void deleteClient(Long id);
    List<Client> searchClients(String firstName, String lastName, String email);
}
