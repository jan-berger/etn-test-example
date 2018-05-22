package com.etnetera.hr.service;

import com.etnetera.hr.controller.exception.ResourceError;
import com.etnetera.hr.controller.exception.ResourceException;
import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.data.JavaScriptFrameworkHypeLevel;
import com.etnetera.hr.data.JavaScriptFrameworkVersion;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import com.etnetera.hr.repository.JavaScriptFrameworkVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Sane implementation
 *
 * @author Berger
 */
@Service
public class JavasScriptFrameworkServiceImpl implements JavasScriptFrameworkService {

	private final JavaScriptFrameworkRepository repository;
	private final JavaScriptFrameworkVersionRepository versionRepository;

	@Autowired
	public JavasScriptFrameworkServiceImpl(JavaScriptFrameworkRepository repository, JavaScriptFrameworkVersionRepository versionRepository) {
		this.repository = repository;
		this.versionRepository = versionRepository;
	}

	@Override
	@Transactional
	public JavaScriptFramework edit(Long frameworkId, JavaScriptFramework newFramework) throws ResourceException {
		if (newFramework.getId() != null && !newFramework.getId().equals(frameworkId)) {
			ResourceError.INCONSISTENT_IDS.throwException();
		}
		JavaScriptFramework oldFramework = repository.findById(frameworkId)
				.orElseThrow(ResourceError.NOT_FOUND.supplyException());
		oldFramework.setName(newFramework.getName());
		/* Looking for all versions that are not present in new framework entity */
		List<JavaScriptFrameworkVersion> versionsToRemove = oldFramework.getVersions().stream()
				.filter(v -> newFramework.getVersions().stream().noneMatch(vv -> v.getId().equals(vv.getId())))
				.collect(Collectors.toList());
		/* We need to delete all versions that are not present in new entity. If we wouldn't do that DB constraint would be violated because Hibernate calls inserts before deletes. */
		oldFramework.clearVersions(versionsToRemove);
		versionRepository.deleteAll(versionsToRemove);
		/* We also need to flush before calling save */
		versionRepository.flush();

		/* now it is possible to add new versions and save */
		oldFramework.addVersions(newFramework.getVersions());
		return repository.save(oldFramework);
	}

	@Override
	public JavaScriptFramework add(JavaScriptFramework newFramework) {
		return repository.save(newFramework);
	}

	@Override
	public Iterable<JavaScriptFramework> get() {
		return repository.findAll();
	}

	@Override
	public JavaScriptFramework get(Long frameworkId) throws ResourceException {
		return repository.findById(frameworkId)
				.orElseThrow(ResourceError.NOT_FOUND.supplyException());
	}

	@Override
	public JavaScriptFramework delete(Long frameworkId) throws ResourceException {
		JavaScriptFramework framework = repository.findById(frameworkId)
				.orElseThrow(ResourceError.NOT_FOUND.supplyException());
		repository.delete(framework);
		return framework;
	}

	@Override
	public Iterable<JavaScriptFramework> search(String name, String version, JavaScriptFrameworkHypeLevel hypeLevel) {
		return repository.search(name, version, hypeLevel);
	}
}
