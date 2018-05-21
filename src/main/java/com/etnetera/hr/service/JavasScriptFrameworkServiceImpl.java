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
		List<JavaScriptFrameworkVersion> versionsToRemove = oldFramework.getVersions().stream()
				.filter(v -> newFramework.getVersions().stream().noneMatch(vv -> v.getId().equals(vv.getId())))
				.collect(Collectors.toList());
		oldFramework.getVersions().forEach(v -> v.setFramework(null));
		oldFramework.getVersions().clear();
		/* We need to delete all versions that are not present in new entity. If we wouldn't do that DB constraint would be violated because Hibernate calls inserts before deletes. */
		versionRepository.deleteAll(versionsToRemove);
		/* We also need to flush before calling save */
		versionRepository.flush();
		oldFramework.getVersions().addAll(newFramework.getVersions());
		oldFramework.prepareForRequest();
		return repository.save(oldFramework);
	}

	@Override
	public JavaScriptFramework add(JavaScriptFramework newFramework) throws ResourceException {
		return repository.save(newFramework);
	}

	@Override
	public Iterable<JavaScriptFramework> get() throws ResourceException {
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
