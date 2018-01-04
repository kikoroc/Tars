package com.qq.tars.web.udb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

@Configuration
public class UdbLoginConfigAdapterLocalDataRegister {

	@Autowired
	@Qualifier("udbLoginConfigAdapter")
	private UdbLoginConfigAdapter udbLoginConfigAdapter;

	@Bean(name = "udbLoginConfigAdapterLocalData")
	public UdbLoginConfigAdapterLocalData udbLoginConfigAdapterLocalData() {
		Assert.isTrue(udbLoginConfigAdapter != null, "请增加UDB配置类");
		return new UdbLoginConfigAdapterLocalData(udbLoginConfigAdapter);
	}

}
