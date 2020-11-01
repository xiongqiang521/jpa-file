package com.xq.controller;

import com.xq.domain.Author;
import com.xq.repository.AuthorRepository;
import com.xq.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author XQ
 * @version v1.0
 * 2020/10/25 17:51
 */
@RestController
@RequestMapping("/author")
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    @PostMapping
    public Author saveAuthor(@RequestBody Author author){
        Author save = authorService.saveAuthor(author);

        return save;
    }

    @GetMapping
    public List<Author> findAuthor(){
        List<Author> all = authorService.findAuthor();

        return all;
    }


}
