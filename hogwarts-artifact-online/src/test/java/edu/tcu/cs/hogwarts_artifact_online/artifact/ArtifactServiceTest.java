package edu.tcu.cs.hogwarts_artifact_online.artifact;

import edu.tcu.cs.hogwarts_artifact_online.artifact.util.IdWorker;
import edu.tcu.cs.hogwarts_artifact_online.system.exception.ObjectNotFoundException;
import edu.tcu.cs.hogwarts_artifact_online.wizard.Wizard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {

    @Mock // this mocks the real classes e.g. the repository that access the database
    ArtifactRepository artifactRepository;
    @Mock
    IdWorker idWorker;

    @InjectMocks // this tells the class under test to inject all the classes being mocked
    ArtifactService artifactService;

    List<Artifact> artifacts;

    @BeforeEach
    void setUp() {
        Artifact a1 = new Artifact();
        a1.setId("87654321");
        a1.setName("Eldritch Amulet of Elarion");
        a1.setDescription(" It glows with a faint blue light when magic is nearby");
        a1.setImageUrl("ImageUrl");

        Artifact a2 = new Artifact();
        a2.setId("1234567");
        a2.setName("Phoenix Feather Quill");
        a2.setDescription(" enhance the writer’s creativity and magical abilities");
        a2.setImageUrl("ImageUrl");

       this.artifacts = new ArrayList<>();
        this.artifacts.add(a1);
        this.artifacts.add(a2);


    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
        //Given: Arrange inputs and targets. Defines the behaviour of the mocked object(invoking the method inside it) e.g. repository
        Artifact a = new Artifact();
        a.setId("12345678");
        a.setName("Invisibility clock");
        a.setDescription("When the user turns it on it make the user invisible by creating a layer that is invisible");
        a.setImageUrl("ImageUrl");

        Wizard w = new Wizard();
        w.setId(2);
        w.setName("John Smith");

        a.setOwner(w);

        given(artifactRepository.findById("12345678")).willReturn(Optional.of(a)); // we wrap using optional class ; it's because the result is optional in that either the artifact will be found or not

        //When: Is where you call the method to be tested
        Artifact returnedArtifact = artifactService.findById("12345678");
        //Then: You compare the results from "when" with the expected results
        assertThat(returnedArtifact.getId()).isEqualTo(a.getId());
        assertThat(returnedArtifact.getName()).isEqualTo(a.getName());
        assertThat(returnedArtifact.getDescription()).isEqualTo(a.getDescription());
        assertThat(returnedArtifact.getImageUrl()).isEqualTo(a.getImageUrl());
        verify(artifactRepository,times(1)).findById("12345678"); //ensures that the artifact with the id is called once

    }
    @Test
    void testFindByIdNotFound(){
        //Given
        given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());
        //When
        Throwable thrown = catchThrowable(()-> {
            Artifact returnedArtifact = artifactService.findById("12345678");
        });
        //Then
            assertThat(thrown).isInstanceOf(ObjectNotFoundException.class).hasMessage("Could not find Artifact with the id 12345678");
        verify(artifactRepository,times(1)).findById("12345678"); //ensures that the artifact repository with the id is called once

    }
    @Test
    void testFindAllSuccess(){
        //Given
        given(artifactRepository.findAll()).willReturn(this.artifacts);
        //When
        List<Artifact> actualArtifacts = artifactService.findAll();
        //Then
        assertThat(actualArtifacts.size()).isEqualTo(this.artifacts.size());
        verify(artifactRepository, times(1)).findAll();
    }
    @Test
    void testSaveSuccess(){
        //Given
        Artifact newArtifact = new Artifact();
        newArtifact.setName("name");
        newArtifact.setDescription("Description...");
        newArtifact.setImageUrl("ImageUrl..");

        given(idWorker.nextId()).willReturn(123456L);
        newArtifact.setId("123456");
        given(artifactRepository.save(newArtifact)).willReturn(newArtifact);
        //When
        Artifact savedArtifact = artifactService.save(newArtifact);
        //Then
        assertThat(savedArtifact.getId()).isEqualTo("123456");
        assertThat(savedArtifact.getName()).isEqualTo(newArtifact.getName());
        assertThat(savedArtifact.getDescription()).isEqualTo(newArtifact.getDescription());
        assertThat(savedArtifact.getImageUrl()).isEqualTo(newArtifact.getImageUrl());
        verify(artifactRepository,times(1)).save(newArtifact);
    }
    @Test
    void testUpdateSuccess(){
        //Given
        Artifact oldArtifact = new Artifact();
        oldArtifact.setId("12345678");
        oldArtifact.setName("Invisibility clock");
        oldArtifact.setDescription("When the user turns it on it make the user invisible by creating a layer that is invisible");
        oldArtifact.setImageUrl("ImageUrl");

        Artifact update = new Artifact();
        update.setId("12345678");
        update.setName("Invisibility clock");
        update.setDescription("Description");
        update.setImageUrl("ImageUrl");

        given(artifactRepository.findById("12345678")).willReturn(Optional.of(oldArtifact));
        given(artifactRepository.save(oldArtifact)).willReturn(oldArtifact);
        //When
        Artifact updatedArtifact = this.artifactService.update("12345678", update);
        //Then
        assertThat(updatedArtifact.getId()).isEqualTo("12345678");
        assertThat(updatedArtifact.getDescription()).isEqualTo(update.getDescription());
        verify(artifactRepository, times(1)).findById("12345678");
        verify(artifactRepository,times(1)).save(oldArtifact);
    }

    @Test
    void testUpdateNotFound(){
        //Given
        Artifact update = new Artifact();
        update.setName("Invisibility clock");
        update.setDescription("Description");
        update.setImageUrl("ImageUrl");

        given(this.artifactRepository.findById("12345678")).willReturn(Optional.empty());
        //When
        assertThrows(ObjectNotFoundException.class, ()->{
            artifactService.update("12345678", update);
        });
        //Then
        verify(this.artifactRepository, times(1)).findById("12345678");
    }
    @Test
    void testDeleteSuccess(){
        //Given
        //the below data represents artifact the already exist
        Artifact artifact = new Artifact();
        artifact.setId("12345678");
        artifact.setName("Invisibility clock");
        artifact.setDescription("Description");
        artifact.setImageUrl("ImageUrl");

        given(this.artifactRepository.findById("12345678")).willReturn(Optional.of(artifact)); //first find by the id
        doNothing().when(artifactRepository).deleteById("12345678"); //then we delete
        //When
        artifactService.delete("12345678");
        //Then
        verify(this.artifactRepository, times(1)).deleteById("12345678");
    }
    @Test
    void testDeleteNotFound(){
        //Given
        given(this.artifactRepository.findById("12345678")).willReturn(Optional.empty()); //first find by the id
        //When
        assertThrows(ObjectNotFoundException.class, ()->artifactService.delete("12345678"));
        //Then
        verify(this.artifactRepository, times(1)).findById("12345678");
    }
}