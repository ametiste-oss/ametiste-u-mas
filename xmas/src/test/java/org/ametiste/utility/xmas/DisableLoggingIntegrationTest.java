package org.ametiste.utility.xmas;

import org.ametiste.utility.xmas.domain.model.RawDataBox;
import org.ametiste.utility.xmas.helpers.XmasDisableLogInitializer;
import org.ametiste.utility.xmas.infrastructure.configurations.BridgeRelayConfiguration;
import org.ametiste.utility.xmas.infrastructure.configurations.FormUriEncodedRelayConfiguration;
import org.ametiste.utility.xmas.infrastructure.persistance.TestLoggingRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by Daria on 24.12.2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:spring/app-config.xml", "classpath:spring/test-config.xml"} ,
        initializers = XmasDisableLogInitializer.class)
public class DisableLoggingIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    @Qualifier("testTemplate")
    private RestTemplate template;

    @Autowired
    @Qualifier("firstTestConfig")
    private BridgeRelayConfiguration config;

    @Autowired
    @Qualifier("secondTestConfig")
    private BridgeRelayConfiguration config2;

    @Autowired
    @Qualifier("formSupportConfig")
    private FormUriEncodedRelayConfiguration formConfig;

    private MockMvc mockMvc;

    @Autowired
    private TestLoggingRepository logger;


    @Before
    public void setup() throws IOException, URISyntaxException {

        MockitoAnnotations.initMocks(this);
        Mockito.reset(config, config2);


           this.mockMvc = webAppContextSetup(this.wac).build();
   }

    @Test
    public void  testFormData() throws Exception {


        MockRestServiceServer mockService = MockRestServiceServer.createServer(template);

        mockService
                .expect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_FORM_URLENCODED))
                .andRespond(withSuccess().contentType(MediaType.APPLICATION_JSON));

        mockService
                .expect(MockRestRequestMatchers.method(HttpMethod.GET))
                        //.andExpect(org.springframework.test.web.client.match.MockRestRequestMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockRestRequestMatchers.requestTo(org.hamcrest.core.StringContains.containsString("addMe=added")))
                .andRespond(withSuccess().contentType(MediaType.APPLICATION_JSON));

        when(config.canTransfer(any(RawDataBox.class))).thenReturn(false);
        when(config2.canTransfer(any(RawDataBox.class))).thenReturn(false);

        this.mockMvc.perform(post("/xmas").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("user_item_id", "86253573").param("deposit_item_id", "65045915").param("item_type","raster"))
                .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        ;
        Thread.sleep(1000);
        verify(config, times(1)).canTransfer(any(RawDataBox.class));
        verify(config, times(0)).transfer(any(RawDataBox.class));
        verify(config2, times(1)).canTransfer(any(RawDataBox.class));
        verify(config2, times(0)).transfer(any(RawDataBox.class));
        mockService.verify();

        logger.assertOriginalWasLogged(0);
        logger.assertTransferResultWasLogged(0);

    }

}
