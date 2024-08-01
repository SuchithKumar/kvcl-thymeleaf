package org.vasaviyuvajanasangha.kvcl.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApprovalTable {

	private String teamName;
	private Integer approved;
	private Integer unapproved;
	
}
