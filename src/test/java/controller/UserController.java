package controller;

import com.example.demo.bindingModel.UserBindingModel;
import com.example.demo.bindingModel.UserEditBindingModel;
import com.example.demo.entity.Article;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




@Controller
public class UserController {
    //my solution

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final ArticleRepository articleRepository;


    public UserController(RoleRepository roleRepository, UserRepository userRepository, ArticleRepository articleRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    //my solution
    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("view", "user/register");
        return "base-layout";
    }

    @PostMapping("/register")
    public String registerProcess(UserBindingModel userBindingModel){
        if (!userBindingModel.getPassword().equals(userBindingModel.getConfirmPassword())) {
            return "redirect:/register";
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User user = new User(
                userBindingModel.getEmail(),
                userBindingModel.getFullName(),
                bCryptPasswordEncoder.encode(userBindingModel.getPassword()));

        Role userRole = this.roleRepository.findByName("ROLE_USER");
        user.addRole(userRole);

        this.userRepository.saveAndFlush(user);

        return "redirect:/login";
    }
    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("view", "user/login");
        return "base-layout";
    }
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profilePage(Model model) {
        UserDetails principal = (UserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        User user = this.userRepository.findByEmail(principal.getUsername());

        model.addAttribute("user", user);
        model.addAttribute("view", "user/profile");

        return "base-layout";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        if (!this.userRepository.existsById(id)) {
            return "redirect:/";
        }

        User user = this.userRepository.getOne(id);

        model.addAttribute("user", user);
        model.addAttribute("view", "user/edit");

        return "base-layout";
    }

    @PostMapping("/edit/{id}")
    public String editProcess(@PathVariable Integer id,
                              UserEditBindingModel userBindingModel) {
        if (!this.userRepository.existsById(id)) {
            return "redirect:/";
        }

        User user = this.userRepository.getOne(id);

        if (!StringUtils.isEmpty(userBindingModel.getPassword())
                && !StringUtils.isEmpty(userBindingModel.getConfirmPassword())) {

            if (userBindingModel.getPassword().equals(userBindingModel.getConfirmPassword())) {
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

                user.setPassword(bCryptPasswordEncoder.encode(userBindingModel.getPassword()));
            }
        }

        user.setFullName(userBindingModel.getFullName());
        user.setEmail(userBindingModel.getEmail());

        this.userRepository.saveAndFlush(user);

        return "redirect:/profile/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        if (!this.userRepository.existsById(id)) {
            return "redirect:/";
        }

        User user = this.userRepository.getOne(id);

        model.addAttribute("user", user);
        model.addAttribute("view", "user/delete");

        return "base-layout";
    }

    @PostMapping("/delete/{id}")
    public String deleteProcess(@PathVariable Integer id) {
        if (!this.userRepository.existsById(id)) {
            return "redirect:/";
        }

        User user = this.userRepository.getOne(id);

        for (Article article : user.getArticles()) {
            this.articleRepository.delete(article);
        }

        this.userRepository.delete(user);

        return "redirect:/";
    }
}