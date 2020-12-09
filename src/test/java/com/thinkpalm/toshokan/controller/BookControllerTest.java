package com.thinkpalm.toshokan.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import com.dlib.bibliothek.controller.BookController;
import com.dlib.bibliothek.request.BookForm;
import com.dlib.bibliothek.response.Data;
import com.dlib.bibliothek.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinkpalm.toshokan.ToshokanApplicationTests;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ToshokanApplicationTests.class)
@AutoConfigureMockMvc
public class BookControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private BookController bookController;

	@Mock
	private BookService bookService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
	}

	@Test
	public void getFeedTest() throws JsonProcessingException, Exception {
		ReflectionTestUtils.setField(bookController, "interval", "30");
		ReflectionTestUtils.setField(bookController, "itemsPerPage", "10");
		Data feedData = new Data();
		//when(bookService.getFeedBooks("30", 1, Integer.valueOf(10))).thenReturn(feedData);
		ObjectMapper mapper = new ObjectMapper();
		BookForm bookForm = new BookForm();
		bookForm.setPage(1);
		mockMvc.perform(post("/api/book/get_feed").content(mapper.writeValueAsString(bookForm))
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
	}

	@Test(expected = NestedServletException.class)
	public void getFeedNullTest() throws JsonProcessingException, Exception {
		ReflectionTestUtils.setField(bookController, "interval", "30");
		ReflectionTestUtils.setField(bookController, "itemsPerPage", "10");
		Data feedData = null;
		//when(bookService.getFeedBooks("30", 1, Integer.valueOf(10))).thenReturn(feedData);
		ObjectMapper mapper = new ObjectMapper();
		BookForm bookForm = new BookForm();
		bookForm.setPage(1);
		mockMvc.perform(post("/api/book/get_feed").content(mapper.writeValueAsString(bookForm))
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
	}
}
