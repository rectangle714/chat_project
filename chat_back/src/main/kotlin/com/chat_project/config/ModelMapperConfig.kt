package com.chat_project.config

import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ModelMapperConfig {
    @Bean
    fun ModelMapper() : ModelMapper {
        val modelMapper : ModelMapper = org.modelmapper.ModelMapper()
        modelMapper.apply {
            configuration.isSkipNullEnabled = true
            configuration.fieldAccessLevel = org.modelmapper.config.Configuration.AccessLevel.PRIVATE
            configuration.isFieldMatchingEnabled = true
        }
        return modelMapper
    }
}