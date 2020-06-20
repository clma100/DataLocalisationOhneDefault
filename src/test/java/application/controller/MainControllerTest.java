package application.controller;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@ContextConfiguration
@WebAppConfiguration
@PrepareForTest(MainController.class)
@WebMvcTest(MainController.class)
public class MainControllerTest {

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();
    @Autowired
    WebApplicationContext context;
    MockMvc mvc;
    MockMultipartFile file;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        file = new MockMultipartFile("test", "test.txt", "text/plain", "test".getBytes());
    }

    @Test
    public void startDefaultTest() throws Exception {
        this.mvc.perform(get("/")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/start"));
    }

    @Test
    public void startEmptyTest() throws Exception {
        this.mvc.perform(get("/start")).andExpect(status().isOk()).andExpect(view().name("start")).andExpect(model().attribute("numberAnnotations", 0)).andExpect(model().attribute("numberCategories", 0)).andExpect(model().attribute("numberImages", 0));
    }

    @Test
    public void getNewTest() throws Exception {
        this.mvc.perform(get("/new")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/start"));
    }

    @Test
    public void getBackTest() throws Exception {
        this.mvc.perform(get("/back")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/start"));
    }

    @Test
    public void getOutputTest() throws Exception {
        this.mvc.perform(get("/output")).andExpect(status().isOk()).andExpect(view().name("output")).andExpect(model().attribute("nodata", "1"));
    }

    @Test
    public void postStartTest() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.multipart("/start").file(file)).andExpect(status().isOk()).andExpect(view().name("start")).andExpect(model().attributeExists("infoLog")).andExpect(model().attribute("numberAnnotations", 0)).andExpect(model().attribute("numberCategories", 0)).andExpect(model().attribute("numberImages", 0));
    }

    @Test
    public void postOutputTest() throws Exception {
        this.mvc.perform(post("/output").param("pathOutput", temporaryFolder.getRoot().getAbsolutePath()).param("extension", "").param("format", "").param("value", "").param("split", "").param("splitNumbers", "")).andExpect(status().isOk()).andExpect(view().name("output"));
    }
}
