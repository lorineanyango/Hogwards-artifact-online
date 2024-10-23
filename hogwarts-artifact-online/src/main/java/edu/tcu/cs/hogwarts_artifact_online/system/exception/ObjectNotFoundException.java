package edu.tcu.cs.hogwarts_artifact_online.system.exception;

public class ObjectNotFoundException extends RuntimeException{

    //this class is for domains with string id
    public ObjectNotFoundException(String objectName, String artifactId){
        super("Could not find " +objectName +" with the id " + artifactId);
    }

    //this class is for domain with Integer id
    public ObjectNotFoundException(String objectName, Integer wizardId){
        super("Could not find " + objectName +" with the id " + wizardId);
    }

}
