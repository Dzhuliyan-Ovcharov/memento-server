package com.memento.web.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.memento.MementoStarter;
import com.memento.service.UserService;
import com.memento.service.configuration.BeanConfig;
import com.memento.service.configuration.WebSecurityConfig;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(value = SpringRunner.class)
@ContextConfiguration(classes = {WebSecurityConfig.class, BeanConfig.class, MementoStarter.class})
@Import(AopAutoConfiguration.class)
public abstract class BaseApiControllerTest {

    static final String EMPTY_JSON = "";
    static final String EMPTY_JSON_COLLECTION = "[]";

    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    UserService userService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .alwaysDo(print())
                .build();
    }
}
