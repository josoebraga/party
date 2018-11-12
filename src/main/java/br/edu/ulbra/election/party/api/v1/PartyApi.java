package br.edu.ulbra.election.party.api.v1;

import br.edu.ulbra.election.party.input.v1.PartyInput;
import br.edu.ulbra.election.party.model.Party;
import br.edu.ulbra.election.party.output.v1.GenericOutput;
import br.edu.ulbra.election.party.output.v1.PartyOutput;
import br.edu.ulbra.election.party.repository.PartyRepository;
import br.edu.ulbra.election.party.service.PartyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/party")
public class PartyApi {

    private final PartyRepository partyRepository;


    private final PartyService partyService;
    @Autowired
    public PartyApi(PartyService partyService, PartyRepository partyRepository){
        this.partyRepository = partyRepository;
        this.partyService = partyService;
    }

    @GetMapping("/")
    @ApiOperation(value = "Get parties List")
    public List<PartyOutput> getAll(){
        return partyService.getAll();
    }

    @GetMapping("/{partyId}")
    @ApiOperation(value = "Get party by Id")
    public PartyOutput getById(@PathVariable Long partyId){
        return partyService.getById(partyId);
    }

    @PostMapping("/")
    @ApiOperation(value = "Create new party")
    public PartyOutput create(@RequestBody PartyInput partyInput){
        return partyService.create(partyInput);
    }

    @PutMapping("/{partyId}")
    @ApiOperation(value = "Update party")
    public PartyOutput update(@PathVariable Long partyId, @RequestBody PartyInput partyInput){
        return partyService.update(partyId, partyInput);
    }

    @DeleteMapping("/{partyId}")
    @ApiOperation(value = "Delete party")
    public GenericOutput delete(@PathVariable Long partyId){
        return partyService.delete(partyId);
    }

/*
    @GetMapping("/{code}")
    @ApiOperation(value = "Get party by code")
    public String getByCode(@PathVariable String code){
        return partyService.getByCode(code);
    }
*/

    @RequestMapping(value = "/{code}", method = RequestMethod.GET)
    public String ListCode(@PathVariable("code") String code, Model model) {
        List<Party> ListCode = partyRepository.findByCode(code);
        if (ListCode != null) {
            model.addAttribute("code", ListCode);
        }
        return "ListCode";
    }

    @RequestMapping(value = "/{number}", method = RequestMethod.GET)
    public String ListNumber(@PathVariable("number") String number, Model model) {
        List<Party> ListNumber = partyRepository.findByNumber(number);
        if (ListNumber != null) {
            model.addAttribute("number", ListNumber);
        }
        return "ListNumber";
    }



}
