package com.zyl.mybatisplus.config;

import com.zyl.mybatisplus.relations.ScanRelationsAnnotations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoMapperConfig {
	@Bean
	public ScanRelationsAnnotations autoMapper() {
		return new ScanRelationsAnnotations(new String[] {
				"com.zyl.mybatisplus.domain",
				"com.zyl.mybatisplus.entity.vo"
		});
	}

}
