package edu.tcu.cs.hogwarts_artifact_online.wizard.converter;

import edu.tcu.cs.hogwarts_artifact_online.wizard.Wizard;
import edu.tcu.cs.hogwarts_artifact_online.wizard.WizardDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizardDtoToWizardConverter implements Converter<WizardDto, Wizard> {
    @Override
    public Wizard convert(WizardDto source) {
        Wizard wizard = new Wizard(); // this is used to hold the wizard that is converted from the dto
        wizard.setId(source.id());
        wizard.setName(source.name());
        return wizard;
    }
}
