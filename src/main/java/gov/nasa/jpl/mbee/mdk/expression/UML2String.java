package gov.nasa.jpl.mbee.mdk.expression;

import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ValueSpecification;

public abstract class UML2String {

    protected String strg = "";
    protected ValueSpecification root;

    public UML2String(ValueSpecification root) {
        this.root = root;
        strg = "";
    }

    public abstract String parse();

}
 