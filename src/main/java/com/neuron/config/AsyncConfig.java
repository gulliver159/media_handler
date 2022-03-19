package com.neuron.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Profile("!unitTest")
@Configuration
public class AsyncConfig {

}
