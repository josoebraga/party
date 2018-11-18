package br.edu.ulbra.election.party.service;

import br.edu.ulbra.election.party.client.CandidateClientService;
import br.edu.ulbra.election.party.exception.GenericOutputException;
import br.edu.ulbra.election.party.input.v1.PartyInput;
import br.edu.ulbra.election.party.model.Candidate;
import br.edu.ulbra.election.party.model.Party;
import br.edu.ulbra.election.party.output.v1.CandidateOutput;
import br.edu.ulbra.election.party.output.v1.GenericOutput;
import br.edu.ulbra.election.party.output.v1.PartyOutput;
import br.edu.ulbra.election.party.repository.CandidateRepository;
import br.edu.ulbra.election.party.repository.PartyRepository;
import feign.FeignException;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartyService {

    private static final String MESSAGE_INVALID_ID = "Invalid id";
    private static final String MESSAGE_PARTY_NOT_FOUND = "Party not found";
    private static final String MESSAGE_INVALID_TO_DELETE_PARTY = "You can't delete a Party with members";
    private final PartyRepository partyRepository;
    private final CandidateClientService candidateClientService;
    private final CandidateRepository candidateRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PartyService(PartyRepository partyRepository, CandidateClientService candidateClientService, CandidateRepository candidateRepository, ModelMapper modelMapper){
        this.partyRepository = partyRepository;
        this.candidateClientService = candidateClientService;
        this.candidateRepository = candidateRepository;
        this.modelMapper = modelMapper;
    }

    public List<PartyOutput> getAll(){
        List<Party> partyList = (List<Party>)partyRepository.findAll();
        return partyList.stream().map(Party::toPartyOutput).collect(Collectors.toList());
    }

    public PartyOutput create(PartyInput partyInput) {
        validateInput(partyInput);
        validateDuplicate(partyInput, null);
        Party party = modelMapper.map(partyInput, Party.class);
        party = partyRepository.save(party);
        return Party.toPartyOutput(party);
    }

    public PartyOutput getById(Long partyId){
        if (partyId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Party party = partyRepository.findById(partyId).orElse(null);
        if (party == null){
            throw new GenericOutputException(MESSAGE_PARTY_NOT_FOUND);
        }

        return modelMapper.map(party, PartyOutput.class);
    }

    public PartyOutput update(Long partyId, PartyInput partyInput) {
        if (partyId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }
        validateInput(partyInput);
        validateDuplicate(partyInput, partyId);

        Party party = partyRepository.findById(partyId).orElse(null);
        if (party == null){
            throw new GenericOutputException(MESSAGE_PARTY_NOT_FOUND);
        }

        party.setCode(partyInput.getCode());
        party.setName(partyInput.getName());
        party.setNumber(partyInput.getNumber());
        party = partyRepository.save(party);
        return modelMapper.map(party, PartyOutput.class);
    }

    public GenericOutput delete(Long partyId) {
        if (partyId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Party party = partyRepository.findById(partyId).orElse(null);
        if (party == null){
            throw new GenericOutputException(MESSAGE_PARTY_NOT_FOUND);
        }

/*********************/

        /* Não pode ser excluído um partido com candidatos */

            CandidateOutput candidateOutput;
            for(int i = 1; i<1000; i++) {

        try {
                candidateOutput = new CandidateOutput();
                candidateOutput = candidateClientService.getById(Long.valueOf(i));
            if(candidateOutput.getPartyId().toString().equals(partyId.toString())) {
                i = 1001;
                throw new GenericOutputException(MESSAGE_INVALID_TO_DELETE_PARTY);
            }

        } catch (FeignException e){
            if (e.status() == 500) {
                //throw new GenericOutputException(MESSAGE_INVALID_TO_DELETE_PARTY);
            }
        }

            }
/*********************/

        partyRepository.delete(party);

        return new GenericOutput("Party deleted");
    }

    private void validateDuplicate(PartyInput partyInput, Long id){
        Party party = partyRepository.findFirstByCode(partyInput.getCode());
        if (party != null && !party.getId().equals(id)){
            throw new GenericOutputException("Duplicate Code");
        }
        party = partyRepository.findFirstByNumber(partyInput.getNumber());
        if (party != null && !party.getId().equals(id)){
            throw new GenericOutputException("Duplicate Number");
        }
    }

    private void validateInput(PartyInput partyInput){
        if (StringUtils.isBlank(partyInput.getName()) || partyInput.getName().trim().length() < 5){
            throw new GenericOutputException("Invalid Name");
        }
        if (StringUtils.isBlank(partyInput.getCode())){
            throw new GenericOutputException("Invalid Code");
        }
        if (partyInput.getNumber() == null || partyInput.getNumber() < 10 || partyInput.getNumber() > 99){
            throw new GenericOutputException("Invalid Party Number");
        }
    }

}
