package edu.tcu.cs.hogwarts_artifact_online.artifact;

public class ArtifactNotFoundException extends RuntimeException{

    public ArtifactNotFoundException(String id){
        super("Could not find artifact with the id");
    }
}
