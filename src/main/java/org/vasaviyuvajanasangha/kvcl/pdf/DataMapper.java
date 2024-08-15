package org.vasaviyuvajanasangha.kvcl.pdf;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import org.vasaviyuvajanasangha.kvcl.model.Team;

@Service
public class DataMapper {

	public Context setData(Team team) {
		
		Context context = new Context();
		
		Map<String, Object> data = new HashMap<>();
		
		data.put("team", team);
		
		context.setVariables(data);
		
		return context;
	}
}