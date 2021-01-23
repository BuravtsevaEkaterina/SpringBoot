package com.controller;

import com.model.Role;
import com.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.model.User;
import com.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/new")
    public String newUserPage(Model model) {
        model.addAttribute("user", new User());
        return "newUser";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute User user,
                             @RequestParam("role") String[] roles) {
        Set<Role> roleSet = new HashSet<>();
        for (String role : roles) {
            roleSet.add(roleService.getRoleByName(role));
        }
        user.setRoles(roleSet);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/update")
    public String updateUserPage(@RequestParam(value = "id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "updateUser";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute User user,
                         @RequestParam("role") String[] roles){
        User updUser = userService.getUserById(user.getId());
        updUser.setUsername(user.getUsername());
        updUser.setEmail(user.getEmail());

        if (user.getPassword() != null) {
            updUser.setPassword(user.getPassword());
        }

        Set<Role> roleSet = new HashSet<>();
        for (String role : roles) {
            roleSet.add(roleService.getRoleByName(role));
        }
        updUser.setRoles(roleSet);

        userService.saveUser(updUser);

        return "redirect:/admin";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam Long id, Model model) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
