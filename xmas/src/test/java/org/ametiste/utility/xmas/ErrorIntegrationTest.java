package org.ametiste.utility.xmas;

import org.ametiste.utility.xmas.helpers.XmasInitializer;
import org.ametiste.utility.xmas.infrastructure.configurations.BridgeRelayConfiguration;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by Daria on 24.12.2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles(value = "org.ametiste.u-mas.logging.global.enabled")
@ContextConfiguration(locations = { "classpath:spring/app-config.xml", "classpath:spring/test-config.xml"} , initializers = XmasInitializer.class)
@DirtiesContext
public class ErrorIntegrationTest {
    @Autowired
    private WebApplicationContext wac;

    //       @Autowired
    //     private RestTemplate template;

    @Autowired
    @Qualifier("firstTestConfig")
    private BridgeRelayConfiguration config;

    @Autowired
    @Qualifier("secondTestConfig")
    private BridgeRelayConfiguration config2;

    private MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;

    @Before
    public void setup() throws IOException, URISyntaxException {

        MockitoAnnotations.initMocks(this);
        Mockito.reset(config, config2);

        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    @Ignore
    public void testError() throws Exception {


        String dst = "{\"a\":\"b\"}";

        JdbcTemplate t = new JdbcTemplate(dataSource);
        t.execute("DROP TABLE  INT_MESSAGE IF EXISTS;");

        this.mockMvc.perform(post("/xmas").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(dst))
                .andExpect(status().isInternalServerError()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists()).andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.request_id").exists()).andExpect(jsonPath("$.code").value("InternalError"))
        ;
    }

    @Test
    public void testErrorLogging() throws Exception {


        String dst = "{\"a\":\"b\", \"error\":\"true\"}";

        this.mockMvc.perform(post("/xmas").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(dst))
                .andExpect(status().isInternalServerError()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists()).andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.request_id").exists()).andExpect(jsonPath("$.code").value("InternalError"))
        ;
    }

}
