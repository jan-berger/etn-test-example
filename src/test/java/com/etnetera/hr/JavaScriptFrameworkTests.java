package com.etnetera.hr;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.etnetera.hr.data.JavaScriptFrameworkHypeLevel;
import com.etnetera.hr.data.JavaScriptFrameworkVersion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;


/**
 * Class used for Spring Boot/MVC based tests.
 * 
 * @author Etnetera
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class JavaScriptFrameworkTests {

	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private JavaScriptFrameworkRepository repository;

	private void prepareData() throws Exception {
		repository.deleteAll();
		JavaScriptFramework react = new JavaScriptFramework("ReactJS");
		react.addVersion("0.0.1", JavaScriptFrameworkHypeLevel.LOW, new GregorianCalendar(2018, 5, 31).getTime());
		react.addVersion("0.0.2", JavaScriptFrameworkHypeLevel.MEDIUM, new Date());
		react.addVersion("0.0.3", JavaScriptFrameworkHypeLevel.HIGH, new Date());
		react.addVersion("0.0.4", JavaScriptFrameworkHypeLevel.MEDIUM, new Date());

		JavaScriptFramework vue = new JavaScriptFramework("Vue.js");
		vue.addVersion("0.0.4", JavaScriptFrameworkHypeLevel.MEDIUM, new GregorianCalendar(2018, 5, 31).getTime());

		JavaScriptFramework angular = new JavaScriptFramework("Angular.js");
		angular.addVersion("0.0.4", JavaScriptFrameworkHypeLevel.MEDIUM, new GregorianCalendar(2018, 5, 31).getTime());
		
		repository.save(react);
		repository.save(vue);
		repository.save(angular);
	}

	@Test
	public void frameworksTest() throws Exception {
		prepareData();

		mockMvc.perform(get("/frameworks")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].name", is("ReactJS")))
				.andExpect(jsonPath("$[0].versions", hasSize(4)))
				.andExpect(jsonPath("$[0].versions[0].version", is("0.0.1")))
				.andExpect(jsonPath("$[0].versions[0].versionOrder", is(10)))
				.andExpect(jsonPath("$[0].versions[0].hypeLevel", is(JavaScriptFrameworkHypeLevel.LOW.name())))
				.andExpect(jsonPath("$[0].versions[0].deprecationDate", is(getJsonFormattedDate(2018, 5, 31))))
				.andExpect(jsonPath("$[0].versions[1].version", is("0.0.2")))
				.andExpect(jsonPath("$[0].versions[1].versionOrder", is(20)))
				.andExpect(jsonPath("$[0].versions[1].hypeLevel", is(JavaScriptFrameworkHypeLevel.MEDIUM.name())))
				.andExpect(jsonPath("$[0].versions[2].version", is("0.0.3")))
				.andExpect(jsonPath("$[0].versions[2].versionOrder", is(30)))
				.andExpect(jsonPath("$[0].versions[2].hypeLevel", is(JavaScriptFrameworkHypeLevel.HIGH.name())))
				.andExpect(jsonPath("$[0].versions[3].version", is("0.0.4")))
				.andExpect(jsonPath("$[0].versions[3].versionOrder", is(40)))
				.andExpect(jsonPath("$[0].versions[3].hypeLevel", is(JavaScriptFrameworkHypeLevel.MEDIUM.name())))
				.andExpect(jsonPath("$[1].name", is("Vue.js")))
				.andExpect(jsonPath("$[1].versions", hasSize(1)))
				.andExpect(jsonPath("$[1].versions[0].version", is("0.0.4")))
				.andExpect(jsonPath("$[1].versions[0].versionOrder", is(10)))
				.andExpect(jsonPath("$[1].versions[0].deprecationDate", is(getJsonFormattedDate(2018, 5, 31))));

	}

	@Test
	public void deleteFrameworkValid() throws JsonProcessingException, Exception {
		prepareData();
		JavaScriptFramework remaining = repository.findAll().iterator().next();
		mockMvc.perform(delete("/frameworks/"+remaining.getId())).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(get("/frameworks")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].name", is("Vue.js")));

		remaining = repository.findAll().iterator().next();

		mockMvc.perform(delete("/frameworks/"+remaining.getId())).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(get("/frameworks")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
	public void deleteFrameworkInvalid() throws JsonProcessingException, Exception {
		prepareData();
		mockMvc.perform(delete("/frameworks/999")).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors[0].message", is("NOT_FOUND")));

	}

	@Test
	public void editFrameworkValid() throws JsonProcessingException, Exception {
		prepareData();
		Iterator<JavaScriptFramework> iterator = repository.findAll().iterator();
		JavaScriptFramework toEdit = iterator.next();

		JavaScriptFramework framework = new JavaScriptFramework();
		framework.setName("goodname");

		framework.addVersion("X", JavaScriptFrameworkHypeLevel.LOW, new GregorianCalendar(2014, 4, 30).getTime());
		framework.addVersion("XX", JavaScriptFrameworkHypeLevel.LOW, new GregorianCalendar(2015, 4, 30).getTime());
		framework.addVersion("XXX", JavaScriptFrameworkHypeLevel.HIGH, new GregorianCalendar(2016, 4, 30).getTime());
		framework.addVersion("XXXX", JavaScriptFrameworkHypeLevel.MEDIUM, new GregorianCalendar(2017, 4, 30).getTime());

		mockMvc.perform(post("/frameworks/"+toEdit.getId()).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("goodname")))
				.andExpect(jsonPath("$.versions", hasSize(4)))
				.andExpect(jsonPath("$.versions[0].version", is("X")))
				.andExpect(jsonPath("$.versions[0].versionOrder", is(10)))
				.andExpect(jsonPath("$.versions[0].deprecationDate", is(getJsonFormattedDate(2014, 4, 30))))
				.andExpect(jsonPath("$.versions[0].hypeLevel", is(JavaScriptFrameworkHypeLevel.LOW.name())))

				.andExpect(jsonPath("$.versions[1].version", is("XX")))
				.andExpect(jsonPath("$.versions[1].versionOrder", is(20)))
				.andExpect(jsonPath("$.versions[1].deprecationDate", is(getJsonFormattedDate(2015, 4, 30))))
				.andExpect(jsonPath("$.versions[1].hypeLevel", is(JavaScriptFrameworkHypeLevel.LOW.name())))

				.andExpect(jsonPath("$.versions[2].version", is("XXX")))
				.andExpect(jsonPath("$.versions[2].versionOrder", is(30)))
				.andExpect(jsonPath("$.versions[2].deprecationDate", is(getJsonFormattedDate(2016, 4, 30))))
				.andExpect(jsonPath("$.versions[2].hypeLevel", is(JavaScriptFrameworkHypeLevel.HIGH.name())))

				.andExpect(jsonPath("$.versions[3].version", is("XXXX")))
				.andExpect(jsonPath("$.versions[3].versionOrder", is(40)))
				.andExpect(jsonPath("$.versions[3].deprecationDate", is(getJsonFormattedDate(2017, 4, 30))))
				.andExpect(jsonPath("$.versions[3].hypeLevel", is(JavaScriptFrameworkHypeLevel.MEDIUM.name())));

		toEdit = iterator.next();

		framework.setName("goodname2");
		framework.getVersions().clear();

		mockMvc.perform(post("/frameworks/"+toEdit.getId()).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("goodname2")))
				.andExpect(jsonPath("$.versions", hasSize(0)));

		toEdit = iterator.next();
		toEdit.setName("goodname3");
		toEdit.getVersions().get(0).setVersion("YY");

		mockMvc.perform(post("/frameworks/"+toEdit.getId()).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(toEdit)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("goodname3")))
				.andExpect(jsonPath("$.versions", hasSize(1)))
				.andExpect(jsonPath("$.versions[0].version", is("YY")))
				.andExpect(jsonPath("$.versions[0].versionOrder", is(10)))
				.andExpect(jsonPath("$.versions[0].deprecationDate", is(getJsonFormattedDate(2018, 5, 31))))
				.andExpect(jsonPath("$.versions[0].hypeLevel", is(JavaScriptFrameworkHypeLevel.MEDIUM.name())));

		mockMvc.perform(get("/frameworks")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].name", is("goodname")))
				.andExpect(jsonPath("$[0].versions", hasSize(4)))
				.andExpect(jsonPath("$[0].versions[0].version", is("X")))
				.andExpect(jsonPath("$[0].versions[0].versionOrder", is(10)))
				.andExpect(jsonPath("$[0].versions[0].deprecationDate", is(getJsonFormattedDate(2014, 4, 30))))
				.andExpect(jsonPath("$[0].versions[0].hypeLevel", is(JavaScriptFrameworkHypeLevel.LOW.name())))

				.andExpect(jsonPath("$[0].versions[1].version", is("XX")))
				.andExpect(jsonPath("$[0].versions[1].versionOrder", is(20)))
				.andExpect(jsonPath("$[0].versions[1].deprecationDate", is(getJsonFormattedDate(2015, 4, 30))))
				.andExpect(jsonPath("$[0].versions[1].hypeLevel", is(JavaScriptFrameworkHypeLevel.LOW.name())))

				.andExpect(jsonPath("$[0].versions[2].version", is("XXX")))
				.andExpect(jsonPath("$[0].versions[2].versionOrder", is(30)))
				.andExpect(jsonPath("$[0].versions[2].deprecationDate", is(getJsonFormattedDate(2016, 4, 30))))
				.andExpect(jsonPath("$[0].versions[2].hypeLevel", is(JavaScriptFrameworkHypeLevel.HIGH.name())))

				.andExpect(jsonPath("$[0].versions[3].version", is("XXXX")))
				.andExpect(jsonPath("$[0].versions[3].versionOrder", is(40)))
				.andExpect(jsonPath("$[0].versions[3].deprecationDate", is(getJsonFormattedDate(2017, 4, 30))))
				.andExpect(jsonPath("$[0].versions[3].hypeLevel", is(JavaScriptFrameworkHypeLevel.MEDIUM.name())))

				.andExpect(jsonPath("$[1].name", is("goodname2")))
				.andExpect(jsonPath("$[1].versions", hasSize(0)))

				.andExpect(jsonPath("$[2].name", is("goodname3")))
				.andExpect(jsonPath("$[2].versions", hasSize(1)))
				.andExpect(jsonPath("$[2].versions[0].version", is("YY")))
				.andExpect(jsonPath("$[2].versions[0].versionOrder", is(10)))
				.andExpect(jsonPath("$[2].versions[0].deprecationDate", is(getJsonFormattedDate(2018, 5, 31))))
				.andExpect(jsonPath("$[2].versions[0].hypeLevel", is(JavaScriptFrameworkHypeLevel.MEDIUM.name())));
	}

	@Test
	public void editFrameworkInvalid() throws JsonProcessingException, Exception {
		prepareData();
		JavaScriptFramework framework = new JavaScriptFramework();
		framework.setName("goodname");
		mockMvc.perform(post("/frameworks/999").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors[0].message", is("NOT_FOUND")));
	}

	@Test
	public void addFrameworkValid() throws JsonProcessingException, Exception {
		repository.deleteAll();
		addFrameWorkAndCheck("goodname");
		addFrameWorkAndCheck("goodname2");
		mockMvc.perform(get("/frameworks")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].name", is("goodname")))
				.andExpect(jsonPath("$[0].versions[0].version", is("0.0.1")))
				.andExpect(jsonPath("$[0].versions[0].versionOrder", is(10)))
				.andExpect(jsonPath("$[0].versions[0].deprecationDate", is(getJsonFormattedDate(2018, 5, 31))))
				.andExpect(jsonPath("$[0].versions[0].hypeLevel", is(JavaScriptFrameworkHypeLevel.HIGH.name())))
				.andExpect(jsonPath("$[0].versions[1].version", is("0.0.2")))
				.andExpect(jsonPath("$[0].versions[1].versionOrder", is(20)))
				.andExpect(jsonPath("$[0].versions[1].deprecationDate", is(getJsonFormattedDate(2017, 5, 31))))
				.andExpect(jsonPath("$[0].versions[1].hypeLevel", is(JavaScriptFrameworkHypeLevel.LOW.name())))
				.andExpect(jsonPath("$[1].name", is("goodname2")))
				.andExpect(jsonPath("$[1].versions[0].version", is("0.0.1")))
				.andExpect(jsonPath("$[1].versions[0].versionOrder", is(10)))
				.andExpect(jsonPath("$[1].versions[0].deprecationDate", is(getJsonFormattedDate(2018, 5, 31))))
				.andExpect(jsonPath("$[1].versions[0].hypeLevel", is(JavaScriptFrameworkHypeLevel.HIGH.name())))
				.andExpect(jsonPath("$[1].versions[1].version", is("0.0.2")))
				.andExpect(jsonPath("$[1].versions[1].versionOrder", is(20)))
				.andExpect(jsonPath("$[1].versions[1].deprecationDate", is(getJsonFormattedDate(2017, 5, 31))))
				.andExpect(jsonPath("$[1].versions[1].hypeLevel", is(JavaScriptFrameworkHypeLevel.LOW.name())));
	}

	@Test
	public void addFrameworkInvalid() throws JsonProcessingException, Exception {
		JavaScriptFramework framework = new JavaScriptFramework();
		mockMvc.perform(put("/frameworks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors[0].field", is("name")))
				.andExpect(jsonPath("$.errors[0].message", is("NotEmpty")));

		framework.setName("verylongnameofthejavascriptframeworkjavaisthebest");
		mockMvc.perform(put("/frameworks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors[0].field", is("name")))
				.andExpect(jsonPath("$.errors[0].message", is("Size")));

		framework.setName("goodname1");
		mockMvc.perform(put("/frameworks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
				.andExpect(status().isOk());

		mockMvc.perform(put("/frameworks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors[0].message", is("PERSISTENCE_ERROR")));
	}

	@Test
	public void addFrameworkVersionInvalid() throws JsonProcessingException, Exception {
		JavaScriptFramework framework = new JavaScriptFramework();

		/* all fields in version are null */
		framework.setName("goodname");
		framework.addVersion(null, null, null);
		mockMvc.perform(put("/frameworks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasSize(3)))
				.andExpect(jsonPath("$.errors[0].field", is("versions[0].deprecationDate")))
				.andExpect(jsonPath("$.errors[0].message", is("NotNull")))
				.andExpect(jsonPath("$.errors[1].field", is("versions[0].hypeLevel")))
				.andExpect(jsonPath("$.errors[1].message", is("NotNull")))
				.andExpect(jsonPath("$.errors[2].field", is("versions[0].version")))
				.andExpect(jsonPath("$.errors[2].message", is("NotEmpty")));

		/* too long version and date is null*/
		framework.getVersions().clear();
		framework.addVersion("verylongversionofthejavascriptframeworkjavaisthebest", JavaScriptFrameworkHypeLevel.HIGH, null);
		mockMvc.perform(put("/frameworks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasSize(2)))
				.andExpect(jsonPath("$.errors[0].field", is("versions[0].deprecationDate")))
				.andExpect(jsonPath("$.errors[0].message", is("NotNull")))
				.andExpect(jsonPath("$.errors[1].field", is("versions[0].version")))
				.andExpect(jsonPath("$.errors[1].message", is("Size")));

		/* non unique version name */
		framework.getVersions().clear();
		framework.addVersion("0.0.1", JavaScriptFrameworkHypeLevel.HIGH, new Date());
		framework.addVersion("0.0.1", JavaScriptFrameworkHypeLevel.HIGH, new Date());
		mockMvc.perform(put("/frameworks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors[0].field", is("javaScriptFramework")))
				.andExpect(jsonPath("$.errors[0].message", is("ERROR_JAVASCRIPT_FRAMEWORK_VERSIONS_UNIQUE_VALIDATION")));

		/* non unique version order */
		framework.getVersions().clear();
		framework.addVersion(new JavaScriptFrameworkVersion("0.0.1", 1, JavaScriptFrameworkHypeLevel.HIGH, new Date(), framework));
		framework.addVersion(new JavaScriptFrameworkVersion("0.0.2", 1, JavaScriptFrameworkHypeLevel.HIGH, new Date(), framework));
		mockMvc.perform(put("/frameworks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors[0].field", is("javaScriptFramework")))
				.andExpect(jsonPath("$.errors[0].message", is("ERROR_JAVASCRIPT_FRAMEWORK_VERSIONS_UNIQUE_VALIDATION")));
	}

	protected void addFrameWorkAndCheck(String name) throws Exception {
		JavaScriptFramework framework = new JavaScriptFramework();
		framework.setName(name);
		framework.addVersion("0.0.1", JavaScriptFrameworkHypeLevel.HIGH, new GregorianCalendar(2018, 5, 31).getTime());
		framework.addVersion("0.0.2", JavaScriptFrameworkHypeLevel.LOW, new GregorianCalendar(2017, 5, 31).getTime());

		mockMvc.perform(put("/frameworks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(name)))
				.andExpect(jsonPath("$.versions[0].version", is("0.0.1")))
				.andExpect(jsonPath("$.versions[0].versionOrder", is(10)))
				.andExpect(jsonPath("$.versions[0].deprecationDate", is(getJsonFormattedDate(2018, 5, 31))))
				.andExpect(jsonPath("$.versions[0].hypeLevel", is(JavaScriptFrameworkHypeLevel.HIGH.name())))
				.andExpect(jsonPath("$.versions[1].version", is("0.0.2")))
				.andExpect(jsonPath("$.versions[1].versionOrder", is(20)))
				.andExpect(jsonPath("$.versions[1].deprecationDate", is(getJsonFormattedDate(2017, 5, 31))))
				.andExpect(jsonPath("$.versions[1].hypeLevel", is(JavaScriptFrameworkHypeLevel.LOW.name())));
	}

	protected String getJsonFormattedDate(int year, int month, int dayOfMonth) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		df.setTimeZone(tz);
		return df.format(new GregorianCalendar(year, month, dayOfMonth).getTime());
	}
	
}
