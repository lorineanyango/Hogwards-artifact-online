package edu.tcu.cs.hogwarts_artifact_online.wizard;

import edu.tcu.cs.hogwarts_artifact_online.artifact.Artifact;
import edu.tcu.cs.hogwarts_artifact_online.artifact.ArtifactRepository;
import edu.tcu.cs.hogwarts_artifact_online.artifact.util.IdWorker;
import edu.tcu.cs.hogwarts_artifact_online.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WizardServiceTest {
    @Mock
    WizardRepository wizardRepository;

    @Mock
    ArtifactRepository artifactRepository;
    @Mock
    IdWorker idWorker;

    @InjectMocks
    WizardService wizardService;

    List<Wizard> wizards; //This is the list that will hold all the wizards

    @BeforeEach
    void setUp() {
        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Walker");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry");

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Peter");

        this.wizards = new ArrayList<>();
        this.wizards.add(w1);
        this.wizards.add(w2);
        this.wizards.add(w3);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllSuccess() {
        //Given
        given(wizardRepository.findAll()).willReturn(this.wizards);
        //When
        List<Wizard> officialWizards = this.wizardService.findAll();
        //Then
        assertThat(officialWizards.size()).isEqualTo(this.wizards.size());
        verify(this.wizardRepository, times(1)).findAll();
    }
    @Test
    void testFindByIdSuccess(){
        //Given
        Artifact a = new Artifact();
        a.setId("12345678");
        a.setName("Invisibility clock");
        a.setDescription("When the user turns it on it make the user invisible by creating a layer that is invisible");
        a.setImageUrl("ImageUrl");

        Artifact a2 = new Artifact();
        a2.setId("1234567");
        a2.setName("Phoenix Feather Quill");
        a2.setDescription(" enhance the writer’s creativity and magical abilities");
        a2.setImageUrl("ImageUrl");



        Wizard w = new Wizard();
        w.setId(1);
        w.setName("Walker");
        w.setArtifacts(Arrays.asList(a, a2));


        given(this.wizardRepository.findById(1)).willReturn(Optional.of(w));
        //When
        Wizard returnedWizard = this.wizardService.findById(1);
        //Then
        assertThat(returnedWizard.getId()).isEqualTo(w.getId());
        assertThat(returnedWizard.getName()).isEqualTo(w.getName());
        assertThat(returnedWizard.getNumberOfArtifacts()).isEqualTo(w.getNumberOfArtifacts());

        verify(this.wizardRepository, times(1)).findById(1);
    }
    @Test
    void testFindByIdNotFound() {
        //Given
        given(wizardRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());
        //When
        Throwable thrown = catchThrowable(() -> {
            Wizard returnedWizard = wizardService.findById(1);
        });
        //Then
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class).hasMessage("Could not find Wizard with the id " + 1);
        verify(wizardRepository, times(1)).findById(1); //ensures that the artifact repository with the id is called once
    }
    @Test
    void testAssignArtifactSuccess(){
        //Given
        Artifact a = new Artifact();
        a.setId("12345678");
        a.setName("Invisibility belt");
        a.setDescription("When the user turns it on it make the user invisible by creating a layer that is invisible");
        a.setImageUrl("ImageUrl");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("John");
        w2.addArtifact(a);

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Harry");


        given(this.artifactRepository.findById("12345678")).willReturn(Optional.of(a));
        given(this.wizardRepository.findById(3)).willReturn(Optional.of(w3));
        //When
        this.wizardService.assignArtifact(3, "12345678");
        //Then
        assertThat(a.getOwner().getId()).isEqualTo(3);
        assertThat(w3.getArtifacts()).contains(a);
    }
    @Test
    void testAssignArtifactErrorWithNoWizardId(){
        //Given
        Artifact a = new Artifact();
        a.setId("12345678");
        a.setName("Invisibility belt");
        a.setDescription("When the user turns it on it make the user invisible by creating a layer that is invisible");
        a.setImageUrl("ImageUrl");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("John");
        w2.addArtifact(a);


        given(this.artifactRepository.findById("12345678")).willReturn(Optional.of(a));
        given(this.wizardRepository.findById(3)).willReturn(Optional.empty());
        //When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, ()->{
            this.wizardService.assignArtifact(3, "12345678");
        });
        //Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                        .hasMessage("Could not find Wizard with the id 3");
        assertThat(a.getOwner().getId()).isEqualTo(2);

    }
    @Test
    void testAssignArtifactErrorWithNoArtifactId(){
        //Given
        given(this.artifactRepository.findById("12345678")).willReturn(Optional.empty());
        //When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, ()-> this.wizardService.assignArtifact(3, "12345678"));

        //Then
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class).hasMessage("Could not find Artifact with the id 12345678");
    }
    }