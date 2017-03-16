package gov.nasa.jpl.mbee.mdk.expression;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Expression;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralInteger;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralReal;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ValueSpecification;

import gov.nasa.jpl.mbee.mdk.expression.Doc2InfixStringUtil;

public class Test {

	public Test(){
		
	}
	/*public List<Object> zz(List<Object> mainList){
		
		MyClass a = new MyClass("a");
		MyClass b = new MyClass("b");
		mainList.add(a);
		mainList.add(b);
		return mainList;
	}*/
	public static void main(String[] xx){
		
		Integer i = 0;
		Test m = new Test();
		Object[] zz = m.xx(i);
		System.out.println(zz[0]);
		System.out.println(zz[1]);
		MyClass g = m.gg(100);
		System.out.println(g.value);
		System.out.println(g.offset);
		
		
		/*
		xx = new String[]{"a", "a", "x"};
		int i;
		for ( i = 0; i < xx.length; i++){
			if(xx[i].equals("a")){
				System.out.println(i + ": " + xx[i]);
			}
			else {
				break;
			}
		}
		System.out.println(i);
		*/
		/*
		
		List<Object> mainList = new ArrayList<Object>();
		System.out.println(mainList.size());
		
		Test xx1 = new Test();
		mainList = xx1.zz(mainList);
		
		System.out.println(mainList.size());
		System.out.println(mainList.size());
		*/
		
	}
	public Object[] xx(Integer i){
		i = i + 100;
		System.out.println("xx: " + i);
		return new Object[] {"abc", i};
	}
	public MyClass gg(Integer i){
		return new MyClass("zz", i);
	}
	public class MyClass
	{
		public String value;
		public int offset;
		public MyClass(String _value, int _offset) {
			value = _value;
			offset = _offset;
		}
	}	
	private String getPositiveOrNegativeNumber(Node n)
	{
		String s = "";
		
		if (n.getNodeName().equals("mo") && (
				n.getFirstChild().getNodeValue().equals("-")
				||n.getFirstChild().getNodeValue().equals("+")))
			s+=n.getFirstChild().getNodeValue(); // + or -
		if ( n.getNextSibling() != null && n.getNextSibling().getNodeName().equals("mn")){
			s+=n.getNextSibling().getFirstChild().getNodeValue();
			return s;
		}
		return null;
	}
	
	public class Vs
	{
		public String value;
		public int offset;
		public Vs(String _value, int _offset) {
			value = _value;
			offset = _offset;
		}
	}	
		
}

