package gov.nasa.jpl.mbee.mdk.expression;

import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ValueSpecification;
import org.antlr.v4.runtime.tree.ParseTree;

/// originally Tree2UMLExpressionInfix
public class Tree2UMLExpression_Promela_ltl extends Tree2UMLExpression {

    public Tree2UMLExpression_Promela_ltl(MathEditorMain1Controller _controller, ParseTree root, ValueSpecification _originalvs) {
        super(_controller, root, _originalvs);
    }

    protected ValueSpecification traverse0(ParseTree n) {
        return null;
    }
}

		