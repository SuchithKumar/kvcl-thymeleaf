package org.vasaviyuvajanasangha.kvcl.pdf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.vasaviyuvajanasangha.kvcl.model.Team;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class DemoDocument {

	@Autowired
	private DocumentGenerator documentGenerator;
	
	@Autowired
	private SpringTemplateEngine springTemplateEngine;
	
	@Autowired
	private DataMapper dataMapper;

	public ByteArrayOutputStream generateDocument(String template, Team team, List<String> images) {
		
		String finalHtml = null;
		
		Context dataContext = dataMapper.setData(team);
		dataContext.setVariable("images",images);
		finalHtml = springTemplateEngine.process(template, dataContext);

		return  documentGenerator.htmlToPdf(finalHtml);
	}
}