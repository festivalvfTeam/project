package ru.vinotekavf.vinotekaapp.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vinotekavf.vinotekaapp.entities.User;
import ru.vinotekavf.vinotekaapp.enums.Role;
import ru.vinotekavf.vinotekaapp.repos.UserRepository;
import ru.vinotekavf.vinotekaapp.services.UserService;
import ru.vinotekavf.vinotekaapp.utils.ControllerUtils;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class UserController {

    private final UserService userService;

    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/registration")
    public String addUser(
        @RequestParam("passwordConfirm") String passwordConfirm,
        @Valid User user,
        BindingResult bindingResult,
        Model model,
        @RequestParam(value = "isAdmin", required = false) String isAdmin
    ){
        boolean isEmpty = StringUtils.isEmpty(passwordConfirm);

        if(isEmpty) {
            model.addAttribute("password2Error", "Повторите пароль");
        }

        if(!StringUtils.isEmpty(user.getPassword()) && !user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Пароли не совпадают!");
        }
        if (isEmpty || bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }
        if(!userService.addUser(user)) {
            model.addAttribute("usernameError", "Пользователь уже существует");
            return "registration";
        }

        if(StringUtils.isEmpty(isAdmin)){
            user.setRoles(Collections.singleton(Role.USER));
        } else {
            user.setRoles(Collections.singleton(Role.ADMIN));
        }

        userRepository.save(user);
        return "redirect:";
    }
}
