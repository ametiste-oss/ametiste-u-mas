package org.ametiste.utility.xmas;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.net.URISyntaxException;

import org.ametiste.utility.xmas.domain.model.RawDataBox;
import org.ametiste.utility.xmas.helpers.XmasInitializer;
import org.ametiste.utility.xmas.infrastructure.FatalTransactionException;
import org.ametiste.utility.xmas.infrastructure.configurations.BridgeRelayConfiguration;
import org.ametiste.utility.xmas.infrastructure.configurations.FormUriEncodedRelayConfiguration;
import org.ametiste.utility.xmas.infrastructure.persistance.TestLoggingRepository;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by Daria on 24.12.2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles(value = "org.ametiste.u-mas.logging.global.enabled")
@ContextConfiguration(locations = { "classpath:spring/app-config.xml", "classpath:spring/test-config.xml"} , initializers = XmasInitializer.class)
public class IntegrationTest {

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

        logger.reset();
        this.mockMvc = webAppContextSetup(this.wac).build();
   }



    @Test
    public void testTwoConfigsCalled() throws Exception {

        String dst = "{\"a\":\"b\"}";

        MockRestServiceServer mockService = MockRestServiceServer.createServer(template);

        mockService
                .expect(MockRestRequestMatchers.method(HttpMethod.GET))
                        //.andExpect(org.springframework.test.web.client.match.MockRestRequestMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockRestRequestMatchers.requestTo(org.hamcrest.core.StringContains.containsString("addMe=added")))
                .andRespond(withSuccess().contentType(MediaType.APPLICATION_JSON));

        when(config.canTransfer(any(RawDataBox.class))).thenReturn(true);
        when(config2.canTransfer(any(RawDataBox.class))).thenReturn(true);
        this.mockMvc.perform(post("/xmas").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(dst))
                    .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            ;
        Thread.sleep(1000);
        verify(config, times(1)).canTransfer(any(RawDataBox.class));
        verify(config, times(1)).transfer(any(RawDataBox.class));
        verify(config2, times(1)).canTransfer(any(RawDataBox.class));
        verify(config2, times(1)).transfer(any(RawDataBox.class));
    }

    @Test
    public void testOnlyOneConfigSupportTransfer() throws Exception {


        String dst = "{\"a\":\"b\"}";

        MockRestServiceServer mockService = MockRestServiceServer.createServer(template);

        mockService
                .expect(MockRestRequestMatchers.method(HttpMethod.GET))
                        //.andExpect(org.springframework.test.web.client.match.MockRestRequestMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockRestRequestMatchers.requestTo(org.hamcrest.core.StringContains.containsString("addMe=added")))
                .andRespond(withSuccess().contentType(MediaType.APPLICATION_JSON));

        when(config.canTransfer(any(RawDataBox.class))).thenReturn(true);
        when(config2.canTransfer(any(RawDataBox.class))).thenReturn(false);

        this.mockMvc.perform(post("/xmas").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(dst))
                .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        ;
        Thread.sleep(1000);
        verify(config, times(1)).canTransfer(any(RawDataBox.class));
        verify(config, times(1)).transfer(any(RawDataBox.class));
        verify(config2, times(1)).canTransfer(any(RawDataBox.class));
        verify(config2, times(0)).transfer(any(RawDataBox.class));
    }

    @Test
    public void  testFormData() throws Exception {

        ArgumentCaptor<RawDataBox> argument = ArgumentCaptor.forClass(RawDataBox.class);

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

        when(config.canTransfer(any(RawDataBox.class))).thenReturn(true);
        when(config2.canTransfer(any(RawDataBox.class))).thenReturn(false);

        this.mockMvc.perform(post("/xmas").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("user_item_id", "86253573").param("deposit_item_id", "65045915").param("item_type","raster"))
                .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        ;
        Thread.sleep(1000);
        verify(config, times(1)).canTransfer(any(RawDataBox.class));
        verify(config, times(1)).transfer(argument.capture());
        verify(config2, times(1)).canTransfer(any(RawDataBox.class));
        verify(config2, times(0)).transfer(any(RawDataBox.class));
        mockService.verify();

        assertTrue(argument.getValue().containsKey("user_item_id"));
    }

    @Test
    public void  testLogging() throws Exception {


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
                .param("user_item_id", "86253573").param("deposit_item_id", "65045915").param("item_type", "raster"))
                .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        ;
        Thread.sleep(1000);
        verify(config, times(1)).canTransfer(any(RawDataBox.class));
        verify(config, times(0)).transfer(any(RawDataBox.class));
        verify(config2, times(1)).canTransfer(any(RawDataBox.class));
        verify(config2, times(0)).transfer(any(RawDataBox.class));
        mockService.verify();

        logger.assertOriginalWasLogged(1);
        logger.assertTransferResultWasLogged(2);

    }

    @Test
    public void testConfigTransactionalTransferWhenOnlyTransactionalFails() throws Exception {


        String dst = "{\"a\":\"b\"}";

        MockRestServiceServer mockService = MockRestServiceServer.createServer(template);

        mockService
                .expect(MockRestRequestMatchers.method(HttpMethod.GET))
                        //.andExpect(org.springframework.test.web.client.match.MockRestRequestMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockRestRequestMatchers.requestTo(org.hamcrest.core.StringContains.containsString("addMe=added")))
                .andRespond(withSuccess().contentType(MediaType.APPLICATION_JSON));

        when(config.canTransfer(any(RawDataBox.class))).thenReturn(true);
        when(config2.canTransfer(any(RawDataBox.class))).thenReturn(true);
        when(config.isTransactional()).thenReturn(false);
        when(config2.isTransactional()).thenReturn(true);
        doThrow(FatalTransactionException.class).when(config2).transfer(any(RawDataBox.class));

        this.mockMvc.perform(post("/xmas").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(dst))
                .andExpect(status().isInternalServerError()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        ;
        Thread.sleep(1000);
        verify(config, times(1)).canTransfer(any(RawDataBox.class));
        verify(config, times(1)).transfer(any(RawDataBox.class));
        verify(config2, times(1)).canTransfer(any(RawDataBox.class));
        verify(config2, times(1)).transfer(any(RawDataBox.class));
    }

    @Test
    public void testConfigTransactionalTransferWhenNonOnlyTransactionalFails() throws Exception {


        String dst = "{\"a\":\"b\"}";
        MockRestServiceServer mockService = MockRestServiceServer.createServer(template);

        mockService
                .expect(MockRestRequestMatchers.method(HttpMethod.GET))
                        //.andExpect(org.springframework.test.web.client.match.MockRestRequestMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockRestRequestMatchers.requestTo(org.hamcrest.core.StringContains.containsString("addMe=added")))
                .andRespond(withSuccess().contentType(MediaType.APPLICATION_JSON));

        when(config.canTransfer(any(RawDataBox.class))).thenReturn(true);
        when(config2.canTransfer(any(RawDataBox.class))).thenReturn(true);
        when(config.isTransactional()).thenReturn(false);
        when(config2.isTransactional()).thenReturn(true);

        doThrow(FatalTransactionException.class).when(config).transfer(any(RawDataBox.class));
        doThrow(FatalTransactionException.class).when(config2).transfer(any(RawDataBox.class));

        this.mockMvc.perform(post("/xmas").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(dst))
                .andExpect(status().isInternalServerError()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        ;
        Thread.sleep(1000);
        verify(config, times(1)).canTransfer(any(RawDataBox.class));
        verify(config, times(1)).transfer(any(RawDataBox.class));
        verify(config2, times(1)).canTransfer(any(RawDataBox.class));
        verify(config2, times(1)).transfer(any(RawDataBox.class));
    }

    private static <T> Matcher<T> negate(final Matcher<T> matcher) {
        return new BaseMatcher<T>() {
            @Override
            public void describeTo(Description description) {

            }

            @Override
            public boolean matches(Object item) {
                return !matcher.matches(item);
            }
        };
    }

    @Test
    public void testGet() throws Exception {

        ArgumentCaptor<RawDataBox> argument = ArgumentCaptor.forClass(RawDataBox.class);

        MockRestServiceServer mockService = MockRestServiceServer.createServer(template);

        mockService
                .expect(MockRestRequestMatchers.method(HttpMethod.GET))
                //.andExpect(org.springframework.test.web.client.match.MockRestRequestMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockRestRequestMatchers.requestTo(org.hamcrest.core.StringContains.containsString("addMe=added")))
                .andExpect(MockRestRequestMatchers.requestTo(org.hamcrest.core.StringContains.containsString("replaceMe=replaced")))
                .andExpect(MockRestRequestMatchers.requestTo(negate(org.hamcrest.core.StringContains.containsString("removeMe"))))
                        .andRespond(withSuccess().contentType(MediaType.APPLICATION_JSON));

        when(config.canTransfer(any(RawDataBox.class))).thenReturn(true);
        when(config2.canTransfer(any(RawDataBox.class))).thenReturn(false);

        this.mockMvc.perform(get("/xmas?user_item_id=86253573&removeMe=please&replaceMe=imbad").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        ;
        Thread.sleep(1000);
        verify(config, times(1)).canTransfer(any(RawDataBox.class));
        verify(config, times(1)).transfer(argument.capture());
        verify(config2, times(1)).canTransfer(any(RawDataBox.class));
        verify(config2, times(0)).transfer(any(RawDataBox.class));
        mockService.verify();

        assertTrue(argument.getValue().containsKey("user_item_id"));
    }
}
