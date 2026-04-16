package com.codenbugs.sgeaapi.repository.career;

import com.codenbugs.sgeaapi.entity.career.Career;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ronyrojas
 */
@Repository
public interface CareerRepository extends JpaRepository<Career, Short> {

}