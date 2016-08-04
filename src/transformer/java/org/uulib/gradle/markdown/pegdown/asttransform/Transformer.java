package org.uulib.gradle.markdown.pegdown.asttransform;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.BitwiseNegationExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.FieldExpression;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.syntax.SyntaxException;
import org.codehaus.groovy.syntax.Token;
import org.codehaus.groovy.syntax.Types;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;
import org.pegdown.Extensions;

import static org.codehaus.groovy.ast.ClassHelper.make;
import static groovyjarjarasm.asm.Opcodes.ACC_PUBLIC;
import static groovyjarjarasm.asm.Opcodes.ACC_ABSTRACT;
import static org.codehaus.groovy.ast.tools.GeneralUtils.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Transforms classes and interfaces annotated with {@linkplain HasPegDownOptions @HasPegDownOptions}.
 * 
 * It adds boolean getters and setters for each of the options flags defined in the {@linkplain Extensions} interface.
 * 
 * @author Rowan Lonsdale
 */
@GroovyASTTransformation(phase=CompilePhase.CANONICALIZATION)
public class Transformer implements ASTTransformation {
	
	private static final String HPDO_NAME = HasPegDownOptions.class.getSimpleName();
	private static final String PDO_NAME = PegDownOptions.class.getSimpleName();
	private static final ClassNode C_PEG_DOWN_OPTIONS = make(PegDownOptions.class);
	private static final ClassNode C_INT = make(int.class);
	private static final ClassNode C_BOOL = make(boolean.class);
	private static final ClassNode C_VOID = make(void.class);
	
	private static final Token AND = Token.newSymbol(Types.BITWISE_AND, -1, -1);
	private static final Token OR = Token.newSymbol(Types.BITWISE_OR, -1, -1);
	
	private static final List<FieldNode> EXTENSIONS = Collections.unmodifiableList(getExtensionFields());

	@Override
	public void visit(ASTNode[] nodes, SourceUnit source) {
		try {
			AnnotationNode annotation = (AnnotationNode) nodes[0];
			ClassNode clazz = (ClassNode) nodes[1];

			if(clazz.isInterface()) {
				addInterfaceMethods(clazz);
			} else {
				addClassMethods(annotation, clazz);
			}
		} catch (SyntaxException e) {
			source.addError(e);
		}

	}
	
	private static void addInterfaceMethods(ClassNode clazz) throws SyntaxException {
		for(FieldNode extension: EXTENSIONS) {
			clazz.addMethod(getterName(extension), ACC_PUBLIC | ACC_ABSTRACT, C_BOOL, Parameter.EMPTY_ARRAY,
					ClassNode.EMPTY_ARRAY, null);
			clazz.addMethod(setterName(extension), ACC_PUBLIC | ACC_ABSTRACT, C_VOID,
					new Parameter[]{param(C_BOOL, "val")}, ClassNode.EMPTY_ARRAY, null);
		}
	}
	
	private static void addClassMethods(AnnotationNode annotation, ClassNode clazz) throws SyntaxException {
		FieldNode optionsField = findOptionsField(annotation, clazz);
		if(optionsField.isFinal()) {
			throw syntaxException(optionsField, null, "The field annotated with %s must not be final.", PDO_NAME);
		}
		if(optionsField.getType() != C_INT) {
			throw syntaxException(optionsField, null, "The field annotated with %s must be type 'int'.", PDO_NAME);
		}
		FieldExpression optsX = fieldX(optionsField);
		
		for(FieldNode extension: EXTENSIONS) {
			FieldExpression extX = fieldX(extension);
			
			clazz.addMethod(getterName(extension), ACC_PUBLIC, C_BOOL, Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY,
				returnS(neX(binX(optsX, AND, extX), constX(0, true)))
			);
			clazz.addMethod(setterName(extension), ACC_PUBLIC, C_VOID, new Parameter[]{param(C_BOOL, "val")},
					ClassNode.EMPTY_ARRAY,
					assignS(optsX, ternaryX(varX("val"), binX(optsX, OR, extX), binX(optsX, AND, bitNotX(extX))))
			);
		}
	}
	
	private static FieldNode findOptionsField(AnnotationNode annotation, ClassNode clazz) throws SyntaxException {
		for(FieldNode field: clazz.getFields()) {
			if(annotationsContainPegDownOptions(field.getAnnotations())) {
				return field;
			}
		}
		throw syntaxException(annotation, null,
				"A class annotated with %s must include a field annotated with %s.", HPDO_NAME, PDO_NAME);
	}
	
	private static boolean annotationsContainPegDownOptions(Collection<AnnotationNode> annotations) {
		for(AnnotationNode annotation: annotations) {
			if(annotation.getClassNode().equals(C_PEG_DOWN_OPTIONS)) {
				return true;
			}
		}
		return false;
	}
	
	private static SyntaxException syntaxException(ASTNode node, Throwable cause, String message, Object... params) {
		return new SyntaxException(String.format(message,  params), cause,
			node.getLineNumber(), node.getColumnNumber(), node.getLastLineNumber(), node.getLastColumnNumber());
	}
	
	private static final Pattern WORD_BOUNDRY = Pattern.compile("(?:^|_)([a-z0-9])");
	private static String transformExtensionName(FieldNode extension) {
		String exName = extension.getName();
		StringBuffer sb = new StringBuffer(exName.length());
		Matcher matcher = WORD_BOUNDRY.matcher(exName.toLowerCase());
		while(matcher.find()) {
			matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	
	private static String getterName(FieldNode extension) {
		return "is" + transformExtensionName(extension);
	}
	
	private static String setterName(FieldNode extension) {
		return "set" + transformExtensionName(extension);
	}
	
	private static Expression bitNotX(Expression x) {
		return new BitwiseNegationExpression(x);
	}
	
	private static Expression binX(Expression left, Token operator, Expression right) {
		return new BinaryExpression(left, operator, right);
	}
	
	private static List<FieldNode> getExtensionFields() {
		List<FieldNode> extensions = make(Extensions.class).getFields();
		Iterator<FieldNode> it = extensions.iterator();
		while(it.hasNext()) {
			if(it.next().getName().equals("NONE")) {
				it.remove();
				break;
			}
		}
		return extensions;
	}

}
