/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.causeway.viewer.thymeflux.test.config;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.reactive.config.ViewResolverRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.view.AbstractView;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Configuration
@Log4j2
public class ThymefluxConfig_customViewResolution implements WebFluxConfigurer {

    @Override
    public void configureViewResolvers(final ViewResolverRegistry registry) {
        ViewResolver resolver = (viewName, locale) -> Mono.just(new HelloView());
        //registry.viewResolver(resolver);
        //registry.
    }

    static class HelloView extends AbstractView {

        @Override
        public Mono<Void> render(final Map<String, ?> model, final MediaType contentType, final ServerWebExchange exchange) {
            log.info("hello render request");
            return super.render(model, contentType, exchange);
        }

        @Override
        protected Mono<Void> renderInternal(final Map<String, Object> renderAttributes,
                @Nullable final MediaType contentType, final ServerWebExchange exchange) {

            return exchange.getResponse().writeWith(Mono
                    .fromCallable(() -> {
                        // Expose all standard FreeMarker hash models.
                        //SimpleHash freeMarkerModel = getTemplateModel(renderAttributes, exchange);


                        log.info(exchange.getLogPrefix() + "Rendering [" + getUrl() + "]");


                        Locale locale = LocaleContextHolder.getLocale(exchange.getLocaleContext());
                        FastByteArrayOutputStream bos = new FastByteArrayOutputStream();
                        try {
                            Charset charset = getCharset(contentType);
                            Writer writer = new OutputStreamWriter(bos, charset);
                            //getTemplate(locale).process(freeMarkerModel, writer);
                            process(renderAttributes, exchange, writer);

                            byte[] bytes = bos.toByteArrayUnsafe();
                            return exchange.getResponse().bufferFactory().wrap(bytes);
                        }
                        catch (IOException ex) {
                            String message = "Could not load FreeMarker template for URL [" + getUrl() + "]";
                            throw new IllegalStateException(message, ex);
                        }
                    })
                    .doOnDiscard(DataBuffer.class, DataBufferUtils::release));
        }

        private void process(final Map<String, Object> renderAttributes, final ServerWebExchange exchange, final Writer writer)
                throws IOException{
            // TODO Auto-generated method stub
            writer.append("Hello World! " + LocalTime.now());
        }

        private Charset getCharset(final MediaType contentType) {
            return StandardCharsets.UTF_8;
        }

        private String getUrl() {
            return "dummy";
        }

    }

}
