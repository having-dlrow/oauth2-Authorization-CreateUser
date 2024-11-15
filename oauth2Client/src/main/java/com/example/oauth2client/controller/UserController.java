package com.example.oauth2client.controller;

import com.example.oauth2client.model.User;
import com.example.oauth2client.service.RoleService;
import com.example.oauth2client.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/user")
    public String getUsers(Model model) {
        List<User> users = userService.getUsers();
        model.addAttribute("users", users);
        // view
        return "user";
    }

    @GetMapping("/user/{username}")
    public String getUserByUsername(Model model, @PathVariable String username) {
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        // view
        return "userbyusername";
    }

    @GetMapping("/createuser")
    public String userForm(Model model, @RequestParam(defaultValue = "false") Boolean error) {
        model.addAttribute("user", new User("", "", false, new ArrayList<>()));
        model.addAttribute("method", "post");
        model.addAttribute("allRoles", roleService.getRoles());
        model.addAttribute("error", error);
        // view
        return "userform";
    }

    @GetMapping("/updateuser/{username}")
    public String userFormUpdate(Model model, @RequestParam(defaultValue = "false") Boolean error, @PathVariable String username) {
        model.addAttribute("user", userService.getUserByUsername(username));
        model.addAttribute("method", "put");
        model.addAttribute("allRoles", roleService.getRoles());
        model.addAttribute("error", error);
        // view
        return "userform";
    }

    @PostMapping("/user")
    public String createUser(@ModelAttribute User user) {
        Integer row = userService.createUser(user);
        if (row == 0) {
            return "redirect:/createuser?error=true";
        }
        return "redirect:/user";
    }

    @PutMapping("/user")
    public String updateUser(@ModelAttribute User user) {
        Integer row = userService.updateUser(user);
        if (row == 0) {
            return "redirect:/updateuser?error=true";
        }
        return "redirect:/user";
    }

    @GetMapping("/deleteuser/{username}")
    public String deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return "redirect:/user";
    }
}