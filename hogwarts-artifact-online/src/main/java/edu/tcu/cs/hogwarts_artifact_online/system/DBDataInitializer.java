package edu.tcu.cs.hogwarts_artifact_online.system;

import edu.tcu.cs.hogwarts_artifact_online.artifact.Artifact;
import edu.tcu.cs.hogwarts_artifact_online.artifact.ArtifactRepository;
import edu.tcu.cs.hogwarts_artifact_online.wizard.Wizard;
import edu.tcu.cs.hogwarts_artifact_online.wizard.WizardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private final ArtifactRepository artifactRepository;
    private final WizardRepository wizardRepository;

    public DBDataInitializer(ArtifactRepository artifactRepository, WizardRepository wizardRepository) {
        this.artifactRepository = artifactRepository;
        this.wizardRepository = wizardRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Artifact a1 = new Artifact();
        a1.setId("87654321");
        a1.setName("Eldritch Amulet of Elation");
        a1.setDescription(" It glows with a faint blue light when magic is nearby");
        a1.setImageUrl("ImageUrl");

        Artifact a2 = new Artifact();
        a2.setId("1234567");
        a2.setName("Phoenix Feather Quill");
        a2.setDescription(" enhance the writer’s creativity and magical abilities");
        a2.setImageUrl("ImageUrl");

        Artifact a3 = new Artifact();
        a3.setId("7654321");
        a3.setName("Chronos-Glasses of Time");
        a3.setDescription("glasses with lenses that shift through different shades of time");
        a3.setImageUrl("ImageUrl");

        Wizard w1 = new Wizard();
        w1.setId(3);
        w1.setName("Beatrice");
        w1.addArtifact(a1);

        Wizard w2 = new Wizard();
        w2.setId(4);
        w2.setName("Beatrice");
        w2.addArtifact(a2);

        Wizard w3 = new Wizard();
        w3.setId(5);
        w3.setName("Beatrice");
        w3.addArtifact(a3);

        wizardRepository.save(w1);
        wizardRepository.save(w2);
        wizardRepository.save(w3);
    }
}
