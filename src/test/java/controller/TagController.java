package controller;

import com.example.demo.entity.Tag;
import com.example.demo.repository.TagRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TagController {

    private final TagRepository tagRepository;

    public TagController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @GetMapping("/tag/{name}")
    public String articlesWithTag(Model model, @PathVariable String name) {

        Tag tag = this.tagRepository.findByName(name);
        if (tag == null) {
            return "redirect:/";
        }

        model.addAttribute("view", "tag/articles");
        model.addAttribute("tag", tag);

        return "base-layout";
    }
}