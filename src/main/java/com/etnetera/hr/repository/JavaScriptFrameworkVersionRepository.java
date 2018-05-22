package com.etnetera.hr.repository;

import com.etnetera.hr.data.JavaScriptFrameworkVersion;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring data repository interface used for accessing the data in database.
 * 
 * @author Etnetera
 *
 */
public interface JavaScriptFrameworkVersionRepository extends JpaRepository<JavaScriptFrameworkVersion, Long> {

}
