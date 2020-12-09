
package com.dlib.bibliothek.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 
 * The type Web config.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${app.cover-image-path}")
	private String coverImagePath;

	@Value("${app.cover-thumb-image-path}")
	private String coverThumbImagePath;

	@Value("${app.qr-code-pdf-path}")
	private String qrCodePdfPath;

	@Value("${app.privacy-policy-path}")
	private String privacyPolicyPath;

	@Override
	public void addCorsMappings(final CorsRegistry registry) {

		registry.addMapping("").allowedOrigins("")
				.allowedMethods("GET", "POST", "OPTIONS", "HEAD", "PUT", "TRACE", "PATCH", "DELETE")
				.allowCredentials(false).maxAge(3600);
	}

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars").addResourceLocations("classpath:/META-INF/resources/webjars/");
		registry.addResourceHandler("/book/images/cover").addResourceLocations("file:///" + coverImagePath);
		registry.addResourceHandler("/book/images/thumb").addResourceLocations("file:///" + coverThumbImagePath);
		registry.addResourceHandler("/book/pdf").addResourceLocations("file:///" + qrCodePdfPath);
		registry.addResourceHandler("/toshokan/privacy_policy").addResourceLocations("file:///" + privacyPolicyPath);

	}
}
