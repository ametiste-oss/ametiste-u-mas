package org.ametiste.utility.xmas.helpers;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.mock.env.MockPropertySource;

public class XmasInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();
		MockPropertySource mockEnvVars = new MockPropertySource()
//				.withProperty("com.dph.hlss.config.dir", "classpath:/test_config_dir")
				.withProperty("org.ametiste.u-mas.config.dir","classpath:/test_config_dir")
                .withProperty("org.ametiste.u-mas.configuration.descriptor.path", "classpath:/test_config_dir/configurations.xml")
                ;
		propertySources.replace(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, mockEnvVars);

	}

}