package com.xq.repository;

import com.xq.obs.domain.ObsBeanDTO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author XQ
 * @version v1.0
 * 2020/11/1 0:32
 */
public interface FileRepository extends JpaRepository<ObsBeanDTO, Long> {

    public ObsBeanDTO findObsBeanDTOByUrl(String url);
}
