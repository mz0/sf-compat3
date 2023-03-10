// Autogenerated Jamon implementation
// src/main/templates/com/exactpro/sf/messages/impl/templates/ComplexFieldTypeTemplate.jamon

package com.exactpro.sf.messages.impl.templates;

import com.exactpro.sf.common.messages.structures.*;
import com.exactpro.sf.common.util.CodeGenUtils;

public class ComplexFieldTypeTemplateImpl
  extends org.jamon.AbstractTemplateImpl
  implements com.exactpro.sf.messages.impl.templates.ComplexFieldTypeTemplate.Intf

{
  private final String[] distPackagePath;
  private final boolean underscoreAsPackageSeparator;
  private final String packageName;
  private final String typeName;
  private final String namespaceName;
  private final java.util.List<IFieldStructure> fields;
  protected static com.exactpro.sf.messages.impl.templates.ComplexFieldTypeTemplate.ImplData __jamon_setOptionalArguments(com.exactpro.sf.messages.impl.templates.ComplexFieldTypeTemplate.ImplData p_implData)
  {
    return p_implData;
  }
  public ComplexFieldTypeTemplateImpl(org.jamon.TemplateManager p_templateManager, com.exactpro.sf.messages.impl.templates.ComplexFieldTypeTemplate.ImplData p_implData)
  {
    super(p_templateManager, __jamon_setOptionalArguments(p_implData));
    distPackagePath = p_implData.getDistPackagePath();
    underscoreAsPackageSeparator = p_implData.getUnderscoreAsPackageSeparator();
    packageName = p_implData.getPackageName();
    typeName = p_implData.getTypeName();
    namespaceName = p_implData.getNamespaceName();
    fields = p_implData.getFields();
  }

  @Override public void renderNoFlush(final java.io.Writer jamonWriter)
    throws java.io.IOException
  {
    jamonWriter.write("/******************************************************************************\n * Copyright 2009-2018 Exactpro (Exactpro Systems Limited)\n *\n * Licensed under the Apache License, Version 2.0 (the \"License\");\n * you may not use this file except in compliance with the License.\n * You may obtain a copy of the License at\n *\n *     http://www.apache.org/licenses/LICENSE-2.0\n *\n * Unless required by applicable law or agreed to in writing, software\n * distributed under the License is distributed on an \"AS IS\" BASIS,\n * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n * See the License for the specific language governing permissions and\n * limitations under the License.\n ******************************************************************************/\npackage ");
    org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(packageName), jamonWriter);
    jamonWriter.write(";\n\nimport com.exactpro.sf.common.impl.messages.DefaultMessageFactory;\nimport com.exactpro.sf.common.impl.messages.BaseMessage;\nimport com.exactpro.sf.common.messages.IMessage;\nimport com.exactpro.sf.common.messages.FieldNotFoundException;\nimport com.exactpro.sf.aml.scriptutil.StaticUtil.IFilter;\n\nimport java.time.LocalDateTime;\nimport java.time.LocalDate;\nimport java.time.LocalTime;\nimport java.math.BigDecimal;\nimport java.util.ArrayList;\nimport java.util.List;\n\n@SuppressWarnings(\"unused\")\npublic class ");
    org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(typeName), jamonWriter);
    jamonWriter.write(" extends BaseMessage\n{\n\n\tpublic ");
    org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(typeName), jamonWriter);
    jamonWriter.write("()\n\t{\n\t\tsuper(DefaultMessageFactory.getFactory().createMessage(\"");
    org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(typeName), jamonWriter);
    jamonWriter.write("\", \"");
    org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(namespaceName), jamonWriter);
    jamonWriter.write("\"));\n\t}\n\n    public ");
    org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(typeName), jamonWriter);
    jamonWriter.write("(String name, String namespace)\n    {\n        super(DefaultMessageFactory.getFactory().createMessage(name, namespace));\n    }\n\n\tpublic ");
    org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(typeName), jamonWriter);
    jamonWriter.write("(IMessage msg)\n\t{\n\t\tsuper(msg);\n\t}\n\n    @Override\n    public ");
    org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(typeName), jamonWriter);
    jamonWriter.write(" clone() {\n        return (");
    org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(typeName), jamonWriter);
    jamonWriter.write(")super.clone();\n    }\n\n\t");
    for (int i = 0; i < fields.size(); ++i )
    {
      jamonWriter.write("\n\t");

		IFieldStructure field = fields.get(i);
		String fieldTypeName = CodeGenUtils.getTypeName(field, distPackagePath, underscoreAsPackageSeparator);
		String methodCall = "";

      if (( field.isCollection() ) )
      {
        jamonWriter.write("\n\t\t\tpublic ");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(typeName), jamonWriter);
        jamonWriter.write(" add");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(field.getName()), jamonWriter);
        jamonWriter.write("(");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(fieldTypeName), jamonWriter);
        jamonWriter.write(" value)\n\t\t\t{\n\t\t\t\t");

					String elType = "";
					if ( field.isComplex() ) {
						elType = "IMessage";
					}
					else
					{
						elType = CodeGenUtils.convertSimpleFieldTypeToJavaObjectType(field.getJavaType());
					}

        jamonWriter.write("List<");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(elType), jamonWriter);
        jamonWriter.write("> list = this.msg.<List<");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(elType), jamonWriter);
        jamonWriter.write(">>getField(\"");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(field.getName()), jamonWriter);
        jamonWriter.write("\");\n\n\t\t\t\tif(list == null){\n\t\t\t\t\tlist = new ArrayList<");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(elType), jamonWriter);
        jamonWriter.write(">();\n\t\t\t\t\tthis.msg.addField(\"");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(field.getName()), jamonWriter);
        jamonWriter.write("\" ,list);\n\t\t\t\t}\n\n\t\t\t\t");
        if (( field.isComplex() ) )
        {
          jamonWriter.write("\n\t\t\t\t\tlist.add(value.getMessage());\n\t\t\t\t\treturn this;\n\t\t\t\t");
        }
        else if (( field.isEnum() ) )
        {
          jamonWriter.write("\n\t\t\t\t\tlist.add(value.getValue());\n\t\t\t\t\treturn this;\n\t\t\t\t");
        }
        else if (( field.isSimple() ) )
        {
          jamonWriter.write("\n\t\t\t\t\tlist.add(value);\n\t\t\t\t\treturn this;\n\t\t\t\t");
        }
        else
        {
          jamonWriter.write("\n\t\t\t\t\tthrow new EPSCommonException(\"Unknown type\");\n\t\t\t\t");
        }
        jamonWriter.write("\n\t\t\t}\n\n\t\t\tpublic List<");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(fieldTypeName), jamonWriter);
        jamonWriter.write("> get");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(field.getName()), jamonWriter);
        jamonWriter.write("()\n\t\t\t{\n\t\t\t\t");

					String elemType = "";
					if ( field.isComplex() ) {
						elemType = "IMessage";
					}
					else
					{
						elemType = CodeGenUtils.convertSimpleFieldTypeToJavaObjectType(field.getJavaType());
					}

        jamonWriter.write("List<");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(elemType), jamonWriter);
        jamonWriter.write("> value = this.msg.<List<");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(elemType), jamonWriter);
        jamonWriter.write(">>getField(\"");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(field.getName()), jamonWriter);
        jamonWriter.write("\");\n\n                if(value == null)\n                {\n                    throw new FieldNotFoundException(\" ");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(field.getName()), jamonWriter);
        jamonWriter.write(" \");\n                }\n\n                List<");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(fieldTypeName), jamonWriter);
        jamonWriter.write("> list = new ArrayList<");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(fieldTypeName), jamonWriter);
        jamonWriter.write(">();\n\n\t\t\t\t");

					if ( field.isEnum() )
					{
						String simpleJavaType = CodeGenUtils.convertSimpleFieldTypeToJavaObjectType(field.getJavaType());
						methodCall =  fieldTypeName + ".getEnumValue((" + simpleJavaType + ")element)";
					}
					else if ( field.isComplex() )
					{
						methodCall = "new " + fieldTypeName + "((IMessage)element)";
					}
					else
					{
						String simpleJavaType = CodeGenUtils.convertSimpleFieldTypeToJavaObjectType(field.getJavaType());
						methodCall = "(" + simpleJavaType + ")element";
					}

        jamonWriter.write("for( ");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(elemType), jamonWriter);
        jamonWriter.write(" element : value )\n\t\t\t\t{\n\t\t\t\t\tlist.add(");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(methodCall), jamonWriter);
        jamonWriter.write(");\n\t\t\t\t}\n\n\t\t\t\treturn list;\n\t\t\t}\n\t");
      }
      else
      {
        jamonWriter.write("\n\n\tpublic ");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(fieldTypeName), jamonWriter);
        jamonWriter.write(" get");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(field.getName()), jamonWriter);
        jamonWriter.write("()\n\t{\n\t\tObject value = this.msg.getField(\"");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(field.getName()), jamonWriter);
        jamonWriter.write("\");\n\n\t\tif(value == null)\n\t\t{\n\t\t\tthrow new FieldNotFoundException(\" ");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(field.getName()), jamonWriter);
        jamonWriter.write(" \");\n\t\t}\n\n\t\t");

			if ( field.isEnum() )
			{
				String simpleJavaType = CodeGenUtils.convertSimpleFieldTypeToJavaObjectType(field.getJavaType());
				methodCall =  fieldTypeName + ".getEnumValue((" + simpleJavaType + ")value)";
			}
			else if ( field.isComplex() )
			{
				methodCall = "(" + fieldTypeName + ")value";
			}
			else
			{
				String simpleJavaType = CodeGenUtils.convertSimpleFieldTypeToJavaObjectType(field.getJavaType());
				methodCall = "(" + simpleJavaType + ")value";
			}

        jamonWriter.write("return ");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(methodCall), jamonWriter);
        jamonWriter.write(";\n\t}\n\n\tpublic ");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(typeName), jamonWriter);
        jamonWriter.write(" set");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(field.getName()), jamonWriter);
        jamonWriter.write("(");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(fieldTypeName), jamonWriter);
        jamonWriter.write(" value)\n\t{\n\t\t");

			if ( field.isEnum() ){
				methodCall = ".getValue()";
			}
			else if ( field.isComplex() ){
				if(!field.isCollection()){
					methodCall = ".getMessage()";
				}
			} else {
				methodCall = "";
			}

        jamonWriter.write("this.msg.addField(\"");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(field.getName()), jamonWriter);
        jamonWriter.write("\", value");
        org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(methodCall), jamonWriter);
        jamonWriter.write(");\n\t\treturn this;\n\t}\n\t");
      }
      jamonWriter.write("\n\n    public IFilter get");
      org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(field.getName()), jamonWriter);
      jamonWriter.write("Filter()\n    {\n        return this.msg.getField(\"");
      org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(field.getName()), jamonWriter);
      jamonWriter.write("\");\n    }\n\n    public ");
      org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(typeName), jamonWriter);
      jamonWriter.write(" set");
      org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(field.getName()), jamonWriter);
      jamonWriter.write("Filter(IFilter filter)\n    {\n        this.msg.addField(\"");
      org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(field.getName()), jamonWriter);
      jamonWriter.write("\", filter);\n        return this;\n    }\n\n    public ");
      org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(typeName), jamonWriter);
      jamonWriter.write(" remove");
      org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(field.getName()), jamonWriter);
      jamonWriter.write("()\n    {\n        this.msg.removeField(\"");
      org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(field.getName()), jamonWriter);
      jamonWriter.write("\");\n        return this;\n    }\n\n\tpublic boolean isSet");
      org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(field.getName()), jamonWriter);
      jamonWriter.write("()\n\t{\n\t\treturn this.msg.getField(\"");
      org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(field.getName()), jamonWriter);
      jamonWriter.write("\") != null;\n\t}\n\t\n\tpublic boolean isFilter");
      org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(field.getName()), jamonWriter);
      jamonWriter.write("()\n    {\n        return this.msg.getField(\"");
      org.jamon.escaping.Escaping.NONE.write(org.jamon.emit.StandardEmitter.valueOf(field.getName()), jamonWriter);
      jamonWriter.write("\") instanceof IFilter;\n    }\n\t");
    }
    jamonWriter.write("\n}");
  }

}
