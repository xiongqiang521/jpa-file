package com.xq.service;

import com.xq.domain.Author;
import com.xq.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author XQ
 * @version v1.0
 * 2020/10/25 17:51
 */
@Service
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    public Author saveAuthor(Author author){
        Author save = authorRepository.save(author);

        return save;
    }

    public List<Author> findAuthor(){
        List<Author> all = authorRepository.findAll();

        return all;
    }


}
