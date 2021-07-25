package ru.vinotekavf.vinotekaapp.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vinotekavf.vinotekaapp.entities.Provider;
import ru.vinotekavf.vinotekaapp.entities.User;
import ru.vinotekavf.vinotekaapp.enums.Role;
import ru.vinotekavf.vinotekaapp.services.PositionService;
import ru.vinotekavf.vinotekaapp.services.ProviderService;
import ru.vinotekavf.vinotekaapp.services.UserService;
import ru.vinotekavf.vinotekaapp.utils.ControllerUtils;
import ru.vinotekavf.vinotekaapp.utils.FileUtils;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Controller
public class UserController {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private UserService userService;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private PositionService positionService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @GetMapping("/")
    public String main(Model model){
        model.addAttribute("providers", providerService.getAllActive());
        return "main";
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

        userService.save(user);
        return "redirect:";
    }

    @GetMapping("/getPrice")
    public String getPrice() throws IOException {
        FileUtils.writeAllToXLSXFile(providerService.getAllActive(), uploadPath);
        return "redirect:/";
    }

    @GetMapping("/providers/upload/{provider}")
    public String getProviderFile(@PathVariable Provider provider) throws IOException {
        FileUtils.writeSingleToXLSXFile(providerService.getProviderById(provider.getId()), uploadPath);
        return "redirect:/";
    }
}
