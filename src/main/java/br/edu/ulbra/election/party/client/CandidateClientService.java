package br.edu.ulbra.election.party.client;


import br.edu.ulbra.election.party.output.v1.CandidateOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class CandidateClientService {

    private final CandidateClient candidateClient;

    @Autowired
    public CandidateClientService(CandidateClient candidateClient){
        this.candidateClient = candidateClient;
    }

    public CandidateOutput getPartyId(Long partyId){
        return this.candidateClient.getAllByPartyId(partyId);
    }

    @FeignClient(value="candidate-service", url="${url.candidate-service}")
    private interface CandidateClient {
        @GetMapping("/v1/candidate/getByPartyId/{partyId}")
        CandidateOutput getAllByPartyId(@PathVariable(name = "partyId") Long partyId);
    }

}
