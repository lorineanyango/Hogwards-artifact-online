package edu.tcu.cs.hogwarts_artifact_online.wizard;

import jakarta.validation.constraints.NotEmpty;

public record WizardDto (Integer id,
                         @NotEmpty(message = "name required")
                        String name,
                         Integer noOfArtifact){
}
