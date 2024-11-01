package edu.tcu.cs.hogwarts_artifact_online.user.converter;

import edu.tcu.cs.hogwarts_artifact_online.user.HogwartsUser;
import edu.tcu.cs.hogwarts_artifact_online.user.userdto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, HogwartsUser> {
    @Override
    public HogwartsUser convert(UserDto source) {
        HogwartsUser hogwartsUser = new HogwartsUser();
        hogwartsUser.setId(source.id());
        hogwartsUser.setUsername(source.username());
        hogwartsUser.setEnabled(source.enabled());
        hogwartsUser.setRoles(source.role());

        return hogwartsUser;
    }
}
