package br.edu.ulbra.election.party.repository;

import br.edu.ulbra.election.party.model.Party;
import org.springframework.data.repository.CrudRepository;

public interface PartyRepository extends CrudRepository<Party, Long> {

    Party findFirstById(Long id);
    Party findFirstByCode(String code);
    Party findFirstByNumber(Integer number);

}
