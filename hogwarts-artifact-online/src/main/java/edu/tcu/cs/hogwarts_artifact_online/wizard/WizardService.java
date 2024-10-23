package edu.tcu.cs.hogwarts_artifact_online.wizard;

import edu.tcu.cs.hogwarts_artifact_online.artifact.util.IdWorker;
import edu.tcu.cs.hogwarts_artifact_online.system.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WizardService {
    private final WizardRepository wizardRepository;
    private final IdWorker idWorker;

    public WizardService(WizardRepository wizardRepository, IdWorker idWorker) {
        this.wizardRepository = wizardRepository;
        this.idWorker = idWorker;
    }


    public List<Wizard> findAll(){
        return this.wizardRepository.findAll();
    }
    public Wizard findById(Integer wizardId){
        return this.wizardRepository.findById(wizardId).orElseThrow(()-> new ObjectNotFoundException("Wizard",wizardId));
    }
    public Wizard save(Wizard newWizard){
        newWizard.setId(newWizard.getId());// we have to set the id first because the new wizard don't have an id
        return this.wizardRepository.save(newWizard);
    }
    public Wizard update(Integer wizardId, Wizard update){
        return this.wizardRepository.findById(wizardId)
                .map(oldWizard->{
                    oldWizard.setName(update.getName());
                    oldWizard.setArtifacts(update.getArtifacts());
                    return this.wizardRepository.save(oldWizard);
                        }).orElseThrow(()-> new ObjectNotFoundException("Wizard",wizardId));
    }
    public void delete(Integer wizardId){
        Wizard wizardToBeDeleted = this.wizardRepository.findById(wizardId)
                .orElseThrow(()-> new ObjectNotFoundException("Wizard",wizardId)); // this part helps to find the artifact first by id and if not found we throw an exception
        wizardToBeDeleted.removeAllArtifact();// we remove the artifact the wizard was owning
        this.wizardRepository.deleteById(wizardId); //after finding we delete by id
    }
}
