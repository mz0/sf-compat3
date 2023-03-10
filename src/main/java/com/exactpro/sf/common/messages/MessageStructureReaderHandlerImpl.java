/*
 * Copyright 2009-2018 Exactpro (Exactpro Systems Limited)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exactpro.sf.common.messages;

import com.exactpro.sf.common.messages.structures.IFieldStructure;
import com.exactpro.sf.common.util.EPSCommonException;

public class MessageStructureReaderHandlerImpl implements IMessageStructureReaderHandler {
	
	private static final MessageStructureReaderHandlerImpl instance = new MessageStructureReaderHandlerImpl();

	private MessageStructureReaderHandlerImpl(){}

	@Override
	public void onRequiredFieldAbsence(IFieldStructure field) {
		throw new EPSCommonException(String.format("Required field [%s] is missed.", field.getName()));
	}

	public static IMessageStructureReaderHandler instance() {
		return instance;
	}
}
