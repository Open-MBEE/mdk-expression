package gov.nasa.jpl.mbee.mdk.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ValueSpecification;

/// originally Tree2UMLExpressionInfix
public class Tree2UMLExpression_Promela_ltl extends Tree2UMLExpression {
	
	public Tree2UMLExpression_Promela_ltl(MathEditorMain1Controller _controller, ParseTree root, ValueSpecification _originalvs) {
		super(_controller, root, _originalvs);
	}
	
	protected ValueSpecification traverse0(ParseTree n){return null;
	}
}
		
		