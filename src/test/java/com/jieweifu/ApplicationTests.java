package com.jieweifu;

import static org.junit.Assert.*;

import com.jieweifu.models.ResultModel;
import com.jieweifu.models.admin.MenuModel;
import com.jieweifu.services.admin.MenuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	MenuService menuService;

	private TestRestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void contextLoads() {
//		menuService.addMenu(new MenuModel());
//		HttpEntity<String> entity = new HttpEntity<>("parameters", getHttpHeaders());
//		ResponseEntity<ResultModel> respEntity= restTemplate.exchange("http://127.0.0.1:8080/sys/user/token/refresh", HttpMethod.GET, entity, ResultModel.class);
//		assertEquals(respEntity.getStatusCode(), HttpStatus.OK);
//		assertNotNull(respEntity.getBody());
//		assertEquals(respEntity.getBody().isSuccess(), true);
	}

	private HttpHeaders getHttpHeaders(){
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("TokenAuthorization", "your value");
		return headers;
	}
}
