package gov.nasa.jpl.mbee.mdk.expression;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;

/**
 * This file preprocess asciimath xml because f and g in expression create unnecessary mrow (exception mfrac)
 * This will remove extra mrow from the xml so easier to prcess by Doc2InfixString.java
 * i.e.,
 * <mi>a</mi>
 * <mrow> -- remove
 * <mi>f</mi>
 * <mrow>
 * <mo>(</mo>
 * <mi>x</mi>
 * <mo>)</mo>
 * </mrow>
 * </mrow> -- remove
 * <p>
 * to
 * <mi>a</mi>
 * <mi>f</mi>
 * <mrow>
 * <mo>(</mo>
 * <mi>x</mi>
 * <mo>)</mo>
 * </mrow>
 * <p>
 * <p>
 * //for case below having mfrac below no mrow removing
 * <math>
 * <mi>d</mi>
 * <mfrac>
 * <mrow> ---- not removing this mrow because mfrac requires 2 children.  Removing mrow can not see f(x) as a constraint parameter.
 * <mi>f</mi>
 * <mrow>
 * <mo>(</mo>
 * <mi>x</mi>
 * <mo>)</mo>
 * </mrow>
 * </mrow>
 * <mrow>
 * <mi>d</mi>
 * <mi>y</mi>
 * </mrow>
 * </mfrac>
 * </math>
 *
 * @author Miyako Wilson
 */


public class DocPreprocess {

    static Document document;

    public DocPreprocess(Document _doc) {
        document = _doc;
    }

    public Document process() {

        // this cast is checked on Apache implementation (Xerces):
        DocumentTraversal traversal = (DocumentTraversal) document;
        TreeWalker walker = traversal.createTreeWalker(document.getDocumentElement(), NodeFilter.SHOW_ELEMENT, null, true);
        traverseLevel(walker, "");
        return document;

    }

    private static final void traverseLevel(TreeWalker walker, String indent) {

        // describe current node: ��
        Node parent = walker.getCurrentNode();
        //System.out.println(indent + "- " + parent.getNodeName());

        Node achild;
        Node grandgrandparent;
        // traverse children: ����
        for (Node n = walker.firstChild(); n != null; n = walker.nextSibling()) {
            if (n.getNodeName().equals("mi") &&
                    (n.getFirstChild().getNodeValue().equals("g") || n.getFirstChild().getNodeValue().equals("f")) &&
                    n.getParentNode().getNodeName().equals("mrow") &&
                    ((grandgrandparent = n.getParentNode().getParentNode()) == null || !grandgrandparent.getNodeName().equals("mfrac"))
                    ) {

                Node grandparent = parent.getParentNode(); //mrow's parent
                Node insertBefore = parent.getNextSibling();
                grandparent.removeChild(parent);

                while (parent.hasChildNodes()) {
                    achild = parent.getFirstChild();
                    if (!(achild.getNodeName().equals("mo") && achild.getFirstChild().getNodeValue().length() == 0)) {
                        grandparent.insertBefore(achild, insertBefore);
                    }
                    else {
                        parent.removeChild(achild);
                    }
                }
            }
            traverseLevel(walker, indent + '\t');
        }
        // return position to parent����
        walker.setCurrentNode(parent);
    }
}
