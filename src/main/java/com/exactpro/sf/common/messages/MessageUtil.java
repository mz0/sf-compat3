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

import com.exactpro.sf.common.impl.messages.DefaultMessageFactory;
import com.exactpro.sf.common.impl.messages.HashMapWrapper;
import com.exactpro.sf.common.messages.IFieldInfo.FieldType;
import com.exactpro.sf.common.messages.structures.IDictionaryStructure;
import com.exactpro.sf.common.messages.structures.IFieldStructure;
import com.exactpro.sf.common.messages.structures.IMessageStructure;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Contains message processing routines
 *
 */
//TODO: class should be moved to com.exactpro.sf.common.impl.messages
public class MessageUtil
{
    public static final MessageTraverser MESSAGE_TRAVERSER = new MessageTraverser();

    private static final long NANOSECONDS_IN_SECOND = 1_000_000_000L;
    private static final AtomicLong counter = new AtomicLong();
    private static final AtomicLong sequenceGenerator = new AtomicLong(getStartSequence());
    public static final String MESSAGE_REJECTED_POSTFIX = " (REJECTED)";

    public static String toString(IMessage msg, String separator)
	{
        if(msg == null) {
            return "null";
        }

        StringBuilder result = new StringBuilder(128);

        for(String fldName : msg.getFieldNames())
		{
			IFieldInfo fldInfo = msg.getFieldInfo(fldName);

			result.append(fldInfo.getName());
			result.append("=");
			if (fldInfo.isCollection()) {
				List<?> vals = (List<?>) fldInfo.getValue();
				if (vals == null) {
					result.append("null");
				} else {
					result.append('{');
					for(Object o : vals) {
                        if(o instanceof IMessage) {
							result.append(toString((IMessage) o, separator));
						} else {
                            result.append(o);
						}
						result.append(separator);
					}
					result.append('}');
				}
				result.append(separator);
			} else if ( fldInfo.getFieldType() == FieldType.SUBMESSAGE )
			{
				result.append('{');

				result.append(toString((IMessage)fldInfo.getValue(), separator));

				result.append('}');
			}
			else
			{
                result.append(fldInfo.getValue());
				result.append(separator);
			}
		}

		return result.toString();
	}

	public static HashMapWrapper<String, Object> convertToHashMap(IMessage message) {
	    if (message != null) {
            HashMapWrapper<String, Object> result = new HashMapWrapper<>(message.getMetaData());

            for (String fieldName : message.getFieldNames()) {
                Object value = message.getField(fieldName);

                if (value instanceof List) {
                    List<Object> list = new ArrayList<>();

                    for (Object element : (List<?>)value) {
                        list.add(element instanceof IMessage ? convertToHashMap((IMessage)element) : element);
                    }

                    result.put(fieldName, list);
                } else {
                    result.put(fieldName, value instanceof IMessage ? convertToHashMap((IMessage)value) : value);
                }
            }

            return result;
	    }

	    return null;
    }

	public static IHumanMessage convertToIHumanMessage(IMessageFactory messageFactory, IMessageStructure messageStructure, IMessage message) {
	    HumanMessageStructureVisitor visitor = new HumanMessageStructureVisitor(messageFactory, message.getName());
	    MESSAGE_TRAVERSER.traverse(visitor, messageStructure, message, EmptyMessageStructureReaderHandler.instance());
	    return visitor.getHumanMessage();
    }

    public static IMessage convertToIMessage(Map<?, ?> map, IMessageFactory messageFactory, String namespace, String name) {
        if (map == null) {
            return null;
        }

        messageFactory = messageFactory != null ? messageFactory : DefaultMessageFactory.getFactory();

        IMessage result;

        if (map instanceof HashMapWrapper) {
            HashMapWrapper wrapper = (HashMapWrapper) map;

            result = messageFactory.createMessage(wrapper.getMetaData().getId(), name, namespace);
            result.getMetaData().setDirty(wrapper.getMetaData().isDirty());

        } else {
            result = messageFactory.createMessage(name, namespace);
        }

        for (Entry<?, ?> entry : map.entrySet()) {
            String key = (String)entry.getKey();
            Object value = entry.getValue();

            if (value instanceof List) {
                List<Object> list = new ArrayList<>();

                for (Object element : (List<?>)value) {
                    list.add(element instanceof Map ? convertToIMessage((Map)element, messageFactory, namespace, name) : element);
                }

                result.addField(key, list);
            } else {
                result.addField(key, value instanceof Map ? convertToIMessage((Map)value, messageFactory, namespace, name) : value);
            }
        }

        return result;
    }

    public static String convertMsgToHumanReadable(IMessage msg, IDictionaryStructure dict) {
        if(msg == null) {
            throw new NullPointerException("msg");
        }

        if(dict == null) {
            throw new NullPointerException("dict");
        }

        IMessageStructure msgStruct = dict.getMessages().get(msg.getName());

        if(msgStruct == null) {
            return null;
        }

		StringBuilder buffer = new StringBuilder(512);

		MsgVisitor visitor = new MsgVisitor(buffer);

        MessageStructureReader.READER.traverse(visitor, msgStruct, msg, MessageStructureReaderHandlerImpl.instance());

		return escapeCharacter(buffer);
	}

	public static IMessage extractMessage(Object field) {
	    if(field instanceof List<?>) {
	        List<?> list = (List<?>)field;

    	    if(list.isEmpty()) {
                return null;
            }

            Object element = list.get(0);

            if(element instanceof IMessage) {
                return (IMessage)element;
            }
	    } else if(field instanceof IMessage) {
	        return (IMessage)field;
	    }

	    return null;
	}

	private static class MsgVisitor extends DefaultMessageStructureVisitor
	{

		private static final String COLLECTION_END = "]; ";
		private static final String COLLECTION_START = "=[";
		private final StringBuilder buffer;
        private final boolean showNulls = false;

        private static final ThreadLocal<DecimalFormat> df = new ThreadLocal<DecimalFormat>() {
	        @Override
            protected DecimalFormat initialValue() {
	            return new DecimalFormat("#.################");
	        }
	    };

		private MsgVisitor(StringBuilder buffer)
		{
			this.buffer = buffer;
		}


		@Override
		public void visit(String fieldName, BigDecimal value,
				IFieldStructure fldStruct, boolean isDefault)
		{
			// value.toPlainString() for correct BigDecimal presentation. RM #9016.
			convertField(buffer, fieldName, (value == null) ? value: value.toPlainString(), fldStruct, showNulls);
		}


		@Override
		public void visit(String fieldName, Boolean value,
				IFieldStructure fldStruct, boolean isDefault)
		{
			convertField(buffer, fieldName, value, fldStruct, showNulls);
		}


		@Override
		public void visit(String fieldName, Byte value,
				IFieldStructure fldStruct, boolean isDefault)
		{
			convertField(buffer, fieldName, value, fldStruct, showNulls);
		}


		@Override
		public void visit(String fieldName, Character value,
				IFieldStructure fldStruct, boolean isDefault)
		{
			convertField(buffer, fieldName, value, fldStruct, showNulls);
		}


		@Override
		public void visit(String fieldName, LocalDateTime value,
				IFieldStructure fldStruct, boolean isDefault)
		{
			convertField(buffer, fieldName, value, fldStruct, showNulls);
		}


		@Override
        public void visit(String fieldName, LocalDate value,
                IFieldStructure fldStruct, boolean isDefault) {
            convertField(buffer, fieldName, value, fldStruct, showNulls);
        }

        @Override
        public void visit(String fieldName, LocalTime value,
                IFieldStructure fldStruct, boolean isDefault) {
            convertField(buffer, fieldName, value, fldStruct, showNulls);
        }

        @Override
		public void visit(String fieldName, Double value,
				IFieldStructure fldStruct, boolean isDefault)
		{
			// we also can check Double.isInfinite(), but DecimalFormat works well with +-Infinity
            convertField(buffer, fieldName, value == null || Double.isNaN(value) ? value : df.get().format(value), fldStruct, showNulls);
		}


		@Override
		public void visit(String fieldName, Float value,
				IFieldStructure fldStruct, boolean isDefault)
		{
			convertField(buffer, fieldName, value, fldStruct, showNulls);
		}


		@Override
		public void visit(String fieldName, Integer value,
				IFieldStructure fldStruct, boolean isDefault)
		{
			convertField(buffer, fieldName, value, fldStruct, showNulls);
		}


		@Override
		public void visit(String fieldName, Long value,
				IFieldStructure fldStruct, boolean isDefault)
		{
			convertField(buffer, fieldName, value, fldStruct, showNulls);
		}


		@Override
		public void visit(String fieldName, Short value,
				IFieldStructure fldStruct, boolean isDefault)
		{
			convertField(buffer, fieldName, value, fldStruct, showNulls);
		}


		@Override
		public void visit(String fieldName, String value,
				IFieldStructure fldStruct, boolean isDefault)
		{
			convertField(buffer, fieldName, value, fldStruct, showNulls);
		}


		@Override
		public void visit(String fieldName, IMessage message, IFieldStructure fldType, boolean isDefault)
        {

			if (message == null) {
				if (showNulls)
				{
					buffer.append(fieldName);
					buffer.append("={null}; ");
				}
				return;
			}
			buffer.append(fieldName);
			buffer.append("={");

            MessageStructureReader.READER.traverse(this, fldType.getFields(), message, MessageStructureReaderHandlerImpl.instance());

			buffer.append("}; ");
		}

		@Override
		public void visitMessageCollection(String fieldName, List<IMessage> messages, IFieldStructure fldType, boolean isDefault)
		{
			boolean required = fldType.isRequired();
			if (!required && !showNulls && messages == null) {
				return;
			}

			buffer.append(fieldName);
			buffer.append(COLLECTION_START);
			if (messages == null) {
				buffer.append("null");
			} else {
				for (IMessage msg : messages) {
					buffer.append("{");
                    MessageStructureReader.READER.traverse(this, fldType.getFields(), msg, MessageStructureReaderHandlerImpl.instance());
					buffer.append("}");
				}
			}
			buffer.append(COLLECTION_END);
		}

		@Override
		public void visitStringCollection(String fieldName, List<String> values, IFieldStructure fldStruct, boolean isDefault)
		{
			boolean required = fldStruct.isRequired();
			if (!required && !showNulls && values == null) {
				return;
			}

			buffer.append(fieldName);
			buffer.append(COLLECTION_START);
			if (values == null) {
				buffer.append("null");
			} else {
                for(String value : values) {
                    convertField(buffer, null, value, fldStruct, showNulls);
                }
			}

			buffer.append(COLLECTION_END);


		}

		@Override
		public void visitIntCollection(String fieldName, List<Integer> values,
				IFieldStructure fldStruct, boolean isDefault) {
			boolean required = fldStruct.isRequired();
			if (!required && !showNulls && values == null) {
				return;
			}

			buffer.append(fieldName);
			buffer.append(COLLECTION_START);

			if (values == null) {
				buffer.append("null");
			} else {
                for(Integer value : values) {
                    convertField(buffer, null, value, fldStruct, showNulls);
                }
			}

			buffer.append(COLLECTION_END);
		}

		@Override
		public void visitLongCollection(
				String fieldName,
				List<Long> values,
				IFieldStructure fldStruct, boolean isDefault)
		{
			boolean required = fldStruct.isRequired();
			if (!required && !showNulls && values == null) {
				return;
			}

			buffer.append(fieldName);
			buffer.append(COLLECTION_START);

			if (values == null) {
				buffer.append("null");
			} else {
                for(Long value : values) {
                    convertField(buffer, null, value, fldStruct, showNulls);
                }
			}

			buffer.append(COLLECTION_END);
		}

		@Override
		public void visitDoubleCollection(
				String fieldName,
				List<Double> values,
				IFieldStructure fldStruct, boolean isDefault)
		{
			boolean required = fldStruct.isRequired();
			if (!required && !showNulls && values == null) {
				return;
			}

			buffer.append(fieldName);
			buffer.append(COLLECTION_START);

			if (values == null) {
				buffer.append("null");
			} else {
                for(Double value : values) {
                    convertField(buffer, null, value, fldStruct, showNulls);
                }
			}

			buffer.append(COLLECTION_END);
		}

		@Override
		public void visitByteCollection(String fieldName, List<Byte> values,
				IFieldStructure fldStruct, boolean isDefault) {

			boolean required = fldStruct.isRequired();
			if (!required && !showNulls && values == null) {
				return;
			}

			buffer.append(fieldName);
			buffer.append(COLLECTION_START);

			if (values == null) {
				buffer.append("null");
			} else {
                for(Byte value : values) {
                    convertField(buffer, null, value, fldStruct, showNulls);
                }
			}

			buffer.append(COLLECTION_END);
		}

		@Override
		public void visitBooleanCollection(
				String fieldName,
				List<Boolean> values,
				IFieldStructure fldStruct,
				boolean isDefault) {
			boolean required = fldStruct.isRequired();
			if (!required && !showNulls && values == null) {
				return;
			}

			buffer.append(fieldName);
			buffer.append(COLLECTION_START);

			if (values == null) {
				buffer.append("null");
			} else {
                for(Boolean value : values) {
                    convertField(buffer, null, value, fldStruct, showNulls);
                }
			}

			buffer.append(COLLECTION_END);
		}

		@Override
        public void visitBigDecimalCollection(
		        String fieldName,
		        List<BigDecimal> values,
		        IFieldStructure fldStruct,
		        boolean isDefault) {
		    boolean required = fldStruct.isRequired();
            if (!required && !showNulls && values == null) {
                return;
            }

            buffer.append(fieldName);
            buffer.append(COLLECTION_START);

            if (values == null) {
                buffer.append("null");
            } else {
                for(BigDecimal value : values) {
                    convertField(buffer, null, (value == null) ? value: value.toPlainString(), fldStruct, showNulls);
                }
            }

            buffer.append(COLLECTION_END);
	    }
	}

	private static void convertField(StringBuilder buffer, String fieldName, Object value, IFieldStructure fldStruct, boolean showNulls)
	{
		boolean required = fldStruct.isRequired();
		if (!required && !showNulls && value == null) {
			return;
		}
		if (fieldName != null) {
			buffer.append(fieldName);
			buffer.append('=');
		}

		if ( fldStruct.isEnum()  )
		{
			if ( value != null )
			{
				String alias = getAlias(fldStruct, value);
				buffer.append(alias);
            } else {
                buffer.append("null");
            }
		}
		else
		{
            buffer.append(value);
		}

		buffer.append("; ");
	}

	public static String escapeCharacter(StringBuilder stringBuilder) {
	    int index = stringBuilder.length();
	    
	    while (--index >= 0) {
	        if (stringBuilder.charAt(index) == '\u0000') {
	            stringBuilder.replace(index, index + 1, "&NULL");
	        }
        }
	    return stringBuilder.toString();
	}

    public static String escapeCharacter(String source) {
        return escapeCharacter(new StringBuilder(source));
    }

    /***
     * Copy properties of instace MsgMetadata to another instance
     * @param source
     * @param destination
     */
    public static void transferMetadata(MsgMetaData source, MsgMetaData destination) {
        destination.setRawMessage(source.getRawMessage());
        destination.setAdmin(source.isAdmin());
        destination.setDictionaryURI(source.getDictionaryURI());
        destination.setProtocol(source.getProtocol());
        destination.setDirty(source.isDirty());
        destination.setFromService(source.getFromService());
        destination.setToService(source.getToService());
        destination.setRejected(source.isRejected());
        destination.setServiceInfo(source.getServiceInfo());
    }

	private static String getAlias(IFieldStructure enumFldType, Object value)
	{
		for ( String enumEl : enumFldType.getValues().keySet() )
		{
            if(enumFldType.getValues().get(enumEl).getCastValue().equals(value)) {
                return enumEl;
            }
		}

		return value.toString();
	}

	public static long generateId(){
	    return counter.getAndIncrement();
    }

	public static long generateSequence(){
	    return sequenceGenerator.getAndIncrement();
    }

    //FIXME: This values should be got from memcache
    private static long getStartSequence() {
        Instant now = Instant.now();
        return now.getEpochSecond() * NANOSECONDS_IN_SECOND + now.getNano();
    }
}
