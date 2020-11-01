package com.xq.repository;

import com.xq.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author XQ
 * @version v1.0
 * 2020/10/24 23:39
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
