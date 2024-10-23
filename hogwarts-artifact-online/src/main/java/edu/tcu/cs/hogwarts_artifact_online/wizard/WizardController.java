package edu.tcu.cs.hogwarts_artifact_online.wizard;

import edu.tcu.cs.hogwarts_artifact_online.system.Result;
import edu.tcu.cs.hogwarts_artifact_online.system.StatusCode;
import edu.tcu.cs.hogwarts_artifact_online.wizard.converter.WizardDtoToWizardConverter;
import edu.tcu.cs.hogwarts_artifact_online.wizard.converter.WizardToWizardDtoConverter;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class WizardController {
    private final WizardService wizardService;
    private final WizardToWizardDtoConverter wizardToWizardDtoConverter;
    private final WizardDtoToWizardConverter wizardDtoToWizardConverter;

    public WizardController(WizardService wizardService, WizardToWizardDtoConverter wizardToWizardDtoConverter, WizardDtoToWizardConverter wizardDtoToWizardConverter) {
        this.wizardService = wizardService;
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
        this.wizardDtoToWizardConverter = wizardDtoToWizardConverter;
    }


    @GetMapping("/api/v1/wizards")
    public Result findAllWizards(){
        List<Wizard> foundWizards = this.wizardService.findAll(); // we find all the items(wizard) then covert them to dto; each and every item is converted to dto
        List<WizardDto> foundWizardDtos = foundWizards.stream().map(
                foundWizard-> this.wizardToWizardDtoConverter.convert(foundWizard))
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS,"All found success", foundWizardDtos);
    }
    @GetMapping("/api/v1/wizards/{wizardId}")
    Result foundWizardById(@PathVariable Integer wizardId){
        Wizard foundWizard = this.wizardService.findById(wizardId);
        WizardDto foundDto = this.wizardToWizardDtoConverter.convert(foundWizard);
        return new Result(true,StatusCode.SUCCESS,"Find one success", foundDto);
    }
    @PostMapping("/api/v1/wizards")
    Result addWizard(@RequestBody WizardDto wizardDto){
       Wizard newWizard = this.wizardDtoToWizardConverter.convert(wizardDto);
       Wizard savedWizard = this.wizardService.save(newWizard);
       WizardDto savedDto = this.wizardToWizardDtoConverter.convert(savedWizard);
        return new Result(true, StatusCode.SUCCESS,"Wizard saved", savedDto);
    }
    @PutMapping("/api/v1/wizard/{wizardId}")
    Result updateWizard(@PathVariable Integer wizardId, @Valid @RequestBody WizardDto updatedWizardDto){
        Wizard update = this.wizardDtoToWizardConverter.convert(updatedWizardDto);
        Wizard updatedWizard = this.wizardService.update(wizardId,update);
        WizardDto updatedDto = this.wizardToWizardDtoConverter.convert(updatedWizard);
        return new Result(true,StatusCode.SUCCESS,"Wizard updated", updatedDto);
    }
    @DeleteMapping("/api/v1/wizard/{wizardId}")
    Result deleteWizard(@PathVariable Integer wizardId){
        this.wizardService.delete(wizardId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}
