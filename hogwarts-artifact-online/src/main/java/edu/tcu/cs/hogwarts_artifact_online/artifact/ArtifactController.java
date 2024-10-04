package edu.tcu.cs.hogwarts_artifact_online.artifact;

import edu.tcu.cs.hogwarts_artifact_online.artifact.artifactDto.ArtifactDto;
import edu.tcu.cs.hogwarts_artifact_online.artifact.converter.ArtifactDtoToArtifactConverter;
import edu.tcu.cs.hogwarts_artifact_online.artifact.converter.ArtifactToArtifactDtoConverter;
import edu.tcu.cs.hogwarts_artifact_online.system.Result;
import edu.tcu.cs.hogwarts_artifact_online.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ArtifactController {

    private final ArtifactService artifactService;
    private final ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter;
    private final ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter;

    public ArtifactController(ArtifactService artifactService, ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter, ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter) {
        this.artifactService = artifactService;
        this.artifactToArtifactDtoConverter = artifactToArtifactDtoConverter;
        this.artifactDtoToArtifactConverter = artifactDtoToArtifactConverter;
    }


    @GetMapping("/api/v1/artifacts/{artifactId}")// the annotation provide called @PathVariable  is used to indicate that the parameter will be received from the URL
    public Result findArtifactById(@PathVariable String artifactId){
        Artifact foundArtifact = this.artifactService.findById(artifactId);
        ArtifactDto artifactDto = this.artifactToArtifactDtoConverter.convert(foundArtifact);
        return new Result(true,StatusCode.SUCCESS,"Found one success", artifactDto);
    }
    @GetMapping("/api/v1/artifacts")
    public Result findAllArtifacts(){
        List<Artifact> foundArtifacts = this.artifactService.findAll();
        List<ArtifactDto> artifactDtos = foundArtifacts.stream()
                .map(foundArtifact-> this.artifactToArtifactDtoConverter.convert(foundArtifact))
                .collect(Collectors.toList());
        return new Result(true,StatusCode.SUCCESS,"All found success", artifactDtos);
    }
    @PostMapping("/api/v1/artifacts")
    public Result addArtifact (@RequestBody ArtifactDto artifactDto){
        Artifact newArtifact = this.artifactDtoToArtifactConverter.convert(artifactDto);
        Artifact savedArtifact = this.artifactService.save(newArtifact);
        ArtifactDto savedArtifactDto = this.artifactToArtifactDtoConverter.convert(savedArtifact);
        return new Result(true,StatusCode.SUCCESS,"Add success",savedArtifactDto);
    }
    @PutMapping("/api/v1/artifacts/{artifactId}")
    public Result updateArtifact(@PathVariable String artifactId, @Valid @RequestBody ArtifactDto artifactDto){
        Artifact update = this.artifactDtoToArtifactConverter.convert(artifactDto);
        Artifact updatedArtifact = this.artifactService.update(artifactId, update);
        ArtifactDto updatedArtifactDto = this.artifactToArtifactDtoConverter.convert(updatedArtifact);
        return new Result(true,StatusCode.SUCCESS,"update success", updatedArtifactDto);
    }
    @DeleteMapping("/api/v1/artifacts/{artifactId}")
    public Result deleteArtifact(@PathVariable String artifactId){
        this.artifactService.delete(artifactId);
        return new Result(true, StatusCode.SUCCESS,"Delete success");
    }
}
