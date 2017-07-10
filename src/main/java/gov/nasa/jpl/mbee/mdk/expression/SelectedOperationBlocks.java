package gov.nasa.jpl.mbee.mdk.expression;

import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.NamedElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class SelectedOperationBlocks {

    Element asciiMathLibraryBlock;
    Element customFuncBlock;

    Collection<Element> aCollection;
    Collection<Element> cCollection;

    public SelectedOperationBlocks(Element _a, Element _c) {
        this.asciiMathLibraryBlock = _a;
        this.customFuncBlock = _c;
        reset();
    }

    public boolean isBlock(Element _e) {
        if (_e == asciiMathLibraryBlock || _e == customFuncBlock) {
            return true;
        }
        return false;
    }

    public void reset() {
        //java.util.Collection<Element> operationsCollection = MDKExpressionPlugin.asciiMathLibraryBlock.getOwnedElement();
        //java.util.Collection<Element> functionsCollection = MDKExpressionPlugin.customFuncBlock.getOwnedElement();
        //only NamedElement are valid
        aCollection = this.asciiMathLibraryBlock.getOwnedElement().stream().filter(e -> e instanceof NamedElement).collect(Collectors.toList());
        cCollection = this.customFuncBlock.getOwnedElement().stream().filter(e -> e instanceof NamedElement).collect(Collectors.toList());
    }

    public void reset(Element _a, Element _c) {
        this.asciiMathLibraryBlock = _a;
        this.customFuncBlock = _c;
        reset();
    }

    public Element getOperationAsciiMathLibrary(String _lookingfor) { //Property.get() throws NoSuchElementException if not found
        Optional<Element> p = aCollection.stream()
                .filter(e -> ((NamedElement) e).getName().equals(_lookingfor))
                .findFirst();//.get();
        if (p.hashCode() != 0) {
            return p.get();
        }
        else {
            return null;
        }
    }

    public Element getOperationCustom(String _lookingfor) { //Property.get() throws NoSuchElementException if not found
        Optional<Element> p = cCollection.stream()
                .filter(e -> ((NamedElement) e).getName().equals(_lookingfor))
                .findFirst();//.get();
        if (p.hashCode() != 0) {
            return p.get();
        }
        else {
            return null;
        }
    }

    public Collection<String> getOperationsInStringForAutoComplete() {
        List<String> l = new ArrayList<String>();

        //adding () for a operator like sin so auto-complete will show "sin( )"
        //or add ()() for a operator like frac so auto-complete will show "frac( )( )"
        aCollection.stream()
                .forEach(o -> {
                    if (MDSysMLConstants.suffixParentheses1.contains(((NamedElement) o).getName())) {
                        l.add(((NamedElement) o).getName() + "( )");
                    }
                    else if (MDSysMLConstants.suffixParentheses2.contains(((NamedElement) o).getName())) {
                        l.add(((NamedElement) o).getName() + "( )( )");
                    }
                    else {
                        l.add(((NamedElement) o).getName());
                    }
                });

        //same as
        //getOperations().forEach(o->l.add(((NamedElement)o).getName()));
        cCollection.forEach(o -> l.add(((NamedElement) o).getName() + "( )"));
        return l;
    }

    //return both asciimathlibrary and customfunctions
    public Collection<Element> getOperations() {
        Stream<Element> combinedStream = Stream.of(aCollection, cCollection)
                .flatMap(Collection::stream);
        return combinedStream.collect(Collectors.toList());
    }

    public List<String> getCustomOperations() {
        List<String> l = new ArrayList<String>();
        cCollection.forEach(o -> l.add(((NamedElement) o).getName()));
        return l;
    }

    //java.util.Collection<Element> operationsCollection = MDKExpressionPlugin.asciiMathLibraryBlock.getOwnedElement();
    //java.util.Collection<Element> functionsCollection = MDKExpressionPlugin.customFuncBlock.getOwnedElement();
    public Element getAsciiMathLibraryBlock() {
        return asciiMathLibraryBlock;
    }

    public Element getCustomFuncBlock() {
        return customFuncBlock;
    }

    public int getOperationSize() {
        return aCollection.size() + cCollection.size();
    }

}
