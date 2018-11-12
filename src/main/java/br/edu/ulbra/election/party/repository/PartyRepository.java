package br.edu.ulbra.election.party.repository;

import br.edu.ulbra.election.party.model.Party;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PartyRepository extends CrudRepository<Party, Long> {
    List<Party> findByCode(String code);
    List<Party> findByNumber(String number);

}
