package edu.tcu.cs.hogwarts_artifact_online.user;

import edu.tcu.cs.hogwarts_artifact_online.system.Result;
import edu.tcu.cs.hogwarts_artifact_online.system.StatusCode;
import edu.tcu.cs.hogwarts_artifact_online.user.converter.UserDtoToUserConverter;
import edu.tcu.cs.hogwarts_artifact_online.user.converter.UserToUserDtoConverter;
import edu.tcu.cs.hogwarts_artifact_online.user.userdto.UserDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private final UserService userService;
    private final UserToUserDtoConverter userToUserDtoConverter;
    private final UserDtoToUserConverter userDtoToUserConverter;

    public UserController(UserService userService, UserToUserDtoConverter userToUserDtoConverter, UserDtoToUserConverter userDtoToUserConverter) {
        this.userService = userService;
        this.userToUserDtoConverter = userToUserDtoConverter;
        this.userDtoToUserConverter = userDtoToUserConverter;
    }

    @GetMapping("/api/v1/users")
    public Result findAllUsers(){
        List<HogwartsUser> foundHogwarts = this.userService.findAll();

        List<UserDto> userDtos = foundHogwarts.stream()
                .map(foundHogwart-> this.userToUserDtoConverter.convert(foundHogwart))
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find all success",userDtos);
    }
    @GetMapping("/api/v1/users/{userId}")
    public Result findUserById(@PathVariable Integer userId){
        HogwartsUser user = this.userService.findById(userId);
        UserDto userDto = this.userToUserDtoConverter.convert(user);
        return new Result(true,StatusCode.SUCCESS, "Found one success", userDto);
    }
    @PutMapping("/api/v1/users")
    public Result addUser(@Valid @RequestBody HogwartsUser newHogwartsUser){
        HogwartsUser savedUser = this.userService.save(newHogwartsUser);
        UserDto savedUserDto = this.userToUserDtoConverter.convert(savedUser);
        return new Result(true,StatusCode.SUCCESS,"User save success", savedUserDto);
    }
    @PutMapping("/api/v1/users/{userId}")
    public Result updateUser(@PathVariable Integer userId, @Valid @RequestBody UserDto userDto){
        HogwartsUser update = this.userDtoToUserConverter.convert(userDto);
        HogwartsUser updatedUser = this.userService.update(userId,update);
        UserDto updatedDto = this.userToUserDtoConverter.convert(updatedUser);
        return new Result(true,StatusCode.SUCCESS,"update success",updatedDto);
    }
    @DeleteMapping("/api/v1/users/{userId}")
    public Result deleteUser(@PathVariable Integer userId){
        this.userService.delete(userId);
        return new Result(true,StatusCode.SUCCESS,"delete success");
    }
}
