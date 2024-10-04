package edu.tcu.cs.hogwarts_artifact_online.artifact;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwarts_artifact_online.artifact.artifactDto.ArtifactDto;
import edu.tcu.cs.hogwarts_artifact_online.system.StatusCode;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

//import static org.h2.store.fs.FilePath.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ArtifactControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ArtifactService artifactService;
    @Autowired
    ObjectMapper objectMapper;//will help in the converting dto to json

    List<Artifact> artifacts;

    @BeforeEach
    void setUp() {

        this.artifacts = new ArrayList<>();

        Artifact a1 = new Artifact();
        a1.setId("87654321");
        a1.setName("Eldritch Amulet of Elarion");
        a1.setDescription(" It glows with a faint blue light when magic is nearby");
        a1.setImageUrl("ImageUrl");
        this.artifacts.add(a1);

        Artifact a2 = new Artifact();
        a2.setId("1234567");
        a2.setName("Phoenix Feather Quill");
        a2.setDescription(" enhance the writer’s creativity and magical abilities");
        a2.setImageUrl("ImageUrl");
        this.artifacts.add(a2);

        Artifact a3 = new Artifact();
        a3.setId("7654321");
        a3.setName("Chrono-Glasses of Time");
        a3.setDescription("glasses with lenses that shift through different shades of time");
        a3.setImageUrl("ImageUrl");
        this.artifacts.add(a3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findArtifactById() throws Exception {

        //Given
        given(artifactService.findById("7654321")).willReturn(this.artifacts.get(2));
        //When
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/artifacts/{id}", "7654321").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Found one success"))
                .andExpect(jsonPath("$.data.id").value("7654321"))
                .andExpect(jsonPath("$.data.name").value("Chrono-Glasses of Time"))
                .andExpect(jsonPath("$.data.description").value("glasses with lenses that shift through different shades of time"));
        //Then

    }

    @Test
    void findArtifactByIdNotFound() throws Exception {

        //Given
        given(artifactService.findById("7654321")).willThrow(new ArtifactNotFoundException("could not find id 7654321"));
        //When
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/artifacts/{id}", "7654321").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with the id"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void testFindAllSuccess() throws Exception {
        //Given
        given(this.artifactService.findAll()).willReturn(this.artifacts);
        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/artifacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("All found success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.artifacts.size())))
                .andExpect(jsonPath("$.data[0].id").value("87654321"))
                .andExpect(jsonPath("$.data[0].name").value("Eldritch Amulet of Elarion"))
                .andExpect(jsonPath("$.data[1].id").value("1234567"))
                .andExpect(jsonPath("$.data[1].name").value("Phoenix Feather Quill"));
    }

    @Test
    void testAddArtifactSuccess() throws Exception {
        //Given
        //we first create fake dto data
        //the data below stimulize/ imitate what the front end provide to the controller
        ArtifactDto artifactDto = new ArtifactDto(null,
                "Round ring",
                "its used to speed or slow humans emotions",
                "ImageUrl",
                null);
        String json = this.objectMapper.writeValueAsString(artifactDto);//converting the dto to json
//creating fake data that artifactService will return when the saved method in it is invoked
        Artifact savedArtifact = new Artifact();
        savedArtifact.setId("87654321");
        savedArtifact.setName("Eldritch Amulet of Elarion");
        savedArtifact.setDescription(" It glows with a faint blue light when magic is nearby");
        savedArtifact.setImageUrl("ImageUrl");

        given(this.artifactService.save(Mockito.any(Artifact.class))).willReturn(savedArtifact);

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/artifacts").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(savedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(savedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(savedArtifact.getImageUrl()));
    }
    @Test
    void testUpdateArtifactSuccess() throws Exception {
        //Given
        //we first create fake dto data
        //the data below stimulize/ imitate what the front end provide to the controller
        ArtifactDto artifactDto = new ArtifactDto("87654321",
                "Eldritch Amulet of Elarion",
                "It glows with a faint blue light when magic is nearby",
                "ImageUrl",
                null);
        String json = this.objectMapper.writeValueAsString(artifactDto);//converting the dto to json
//creating fake data that artifactService will return when the saved method in it is invoked
        Artifact updatedArtifact = new Artifact();
        updatedArtifact.setId("87654321");
        updatedArtifact.setName("Eldritch Amulet of Elarion");
        updatedArtifact.setDescription(" It glows with a faint blue light when magic is nearby");
        updatedArtifact.setImageUrl("ImageUrl");

        given(this.artifactService.update(eq("87654321"), Mockito.any(Artifact.class))).willReturn(updatedArtifact);

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/artifacts/87654321").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("update success"))
                .andExpect(jsonPath("$.data.id").value("87654321"))
                .andExpect(jsonPath("$.data.name").value(updatedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(updatedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(updatedArtifact.getImageUrl()));

    }
    @Test
    void updateNotFound() throws Exception {
        //Given
        //we first create fake dto data
        //the data below stimulize/ imitate what the front end provide to the controller
        ArtifactDto artifactDto = new ArtifactDto("87654321",
                "Eldritch Amulet of Elarion",
                "It glows with a faint blue light when magic is nearby",
                "ImageUrl",
                null);
        String json = this.objectMapper.writeValueAsString(artifactDto);//converting the dto to json
//creating fake data that artifactService will return when the saved method in it is invoked

        given(this.artifactService.update(eq("87654321"), Mockito.any(Artifact.class))).willThrow(new ArtifactNotFoundException("87654321"));

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/artifacts/87654321").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with the id"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void testDeleteArtifactSucsess() throws Exception {
        //Given
        doNothing().when(this.artifactService).delete("87654321");
        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/artifacts/87654321").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete success"))
                .andExpect(jsonPath("$.data").isEmpty());    
    }
    @Test
    void testDeleteNotFound() throws Exception {
        //Given
        doThrow(new ArtifactNotFoundException("87654321")).when(this.artifactService).delete("87654321");        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/artifacts/87654321").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with the id"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

}