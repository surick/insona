package com.jieweifu.application;

import com.jieweifu.services.gizwits.impl.GizwitsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Test
	public void contextLoads() {
		GizwitsServiceImpl gizwitsService = new GizwitsServiceImpl();
		gizwitsService.getDeviceNameById("9","02d3084343bf");
	}

}
