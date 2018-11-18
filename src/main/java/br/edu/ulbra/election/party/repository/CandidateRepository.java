package br.edu.ulbra.election.party.repository;

import br.edu.ulbra.election.party.model.Candidate;
import org.springframework.data.repository.CrudRepository;

public interface CandidateRepository extends CrudRepository<Candidate, Long> {
}
