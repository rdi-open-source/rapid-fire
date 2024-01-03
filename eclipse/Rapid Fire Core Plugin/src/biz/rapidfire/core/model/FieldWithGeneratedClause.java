/*******************************************************************************
 * Copyright (c) 2017-2023 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model;

import java.util.List;

import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.model.dao.FieldsWithGeneratedClauseDAO;
import biz.rapidfire.core.model.dao.IJDBCConnection;

public class FieldWithGeneratedClause {

	private IRapidFireJobResource job;
	private String library;
	private String file;
	private String field;
	private String text;
	
	public FieldWithGeneratedClause(IRapidFireJobResource job, String library, String file, String field, String text) {
		this.job = job;
		this.library = library;
		this.file = file;
		this.field = field;
		this.text = text;
	}

	public IRapidFireJobResource getJob() {
		return job;
	}

	public String getLibrary() {
		return library;
	}

	public String getFile() {
		return file;
	}

	public String getField() {
		return field;
	}

	public String getText() {
		return text;
	}

	public static FieldWithGeneratedClause[] getFields(Shell shell, IJDBCConnection dao, IRapidFireJobResource job) {

		FieldsWithGeneratedClauseDAO fieldsDAO = new FieldsWithGeneratedClauseDAO(dao);

		List<FieldWithGeneratedClause> _fields = null;
		try {
			_fields = fieldsDAO.load(job, shell);
		} catch (Exception e) {
			e.printStackTrace();
		}

		FieldWithGeneratedClause[] fields;

		if (_fields == null) {
			fields = new FieldWithGeneratedClause[0];
		}
		else {
			fields = new FieldWithGeneratedClause[_fields.size()];
			_fields.toArray(fields);
		}

		return fields;
		
	}
	
}
