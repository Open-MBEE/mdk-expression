package gov.nasa.jpl.mbee.mdk.expression;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.antlr.v4.runtime.tree.ParseTree;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import gov.nasa.jpl.mbee.mdk.expression.AsciiMathML2Tree;
import gov.nasa.jpl.mbee.mdk.expression.AsciiMathParser;
import gov.nasa.jpl.mbee.mdk.expression.AsciiMathParserOptions;
import gov.nasa.jpl.mbee.mdk.expression.DocPreprocess;
import net.sourceforge.jeuclid.DOMBuilder;
import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.converter.Converter;
import net.sourceforge.jeuclid.elements.generic.DocumentElement;

public class AsciiMathML2TreeTestxx {
	/*a(x) + af(x) + abc123 + afbc123b + f(x) + g(xy1) + f1a + fa123a + g1aaabr + gab156d3
	 * 
  <math>
    <semantics>
      <mrow>

        <mi>a</mi>
        <mrow>
          <mo>(</mo>
          <mi>x</mi>
          <mo>)</mo>
        </mrow>
        
        <mo>+</mo>
        
        <mi>a</mi>
        <mrow>
          <mi>f</mi>
          <mrow>
            <mo>(</mo>
            <mi>x</mi>
            <mo>)</mo>
          </mrow>
        </mrow>
        
        <mo>+</mo>
        
        <mi>a</mi>
        <mi>b</mi>
        <mi>c</mi>
        <mn>123</mn>
        
        <mo>+</mo>
        
        <mi>a</mi>
        <mrow>
          <mi>f</mi>
          <mi>b</mi>
        </mrow>
        <mi>c</mi>
        <mn>123</mn>
        <mi>b</mi>
        
        <mo>+</mo>
        
        <mrow>
          <mi>f</mi>
          <mrow>
            <mo>(</mo>
            <mi>x</mi>
            <mo>)</mo>
          </mrow>
        </mrow>
        
        <mo>+</mo>
        
        <mrow>
          <mi>g</mi>
          <mrow>
            <mo>(</mo>
            <mi>x</mi>
            <mi>y</mi>
            <mn>1</mn>
            <mo>)</mo>
          </mrow>
        </mrow>
        
        <mo>+</mo>
        
        <mrow> //f1a <mi>f<.
          <mi>f</mi>
          <mn>1</mn>
        </mrow>
        <mi>a</mi>
        
        <mo>+</mo>
        <mrow>
          <mi>f</mi>
          <mi>a</mi>
        </mrow>
        <mn>123</mn>
        <mi>a</mi>
        <mo>+</mo>
        <mrow>
          <mi>g</mi>
          <mn>1</mn>
        </mrow>
        <mi>a</mi>
        <mi>a</mi>
        <mi>a</mi>
        <mi>b</mi>
        <mi>r</mi>
        <mo>+</mo>
        <mrow>
          <mi>g</mi>
          <mi>a</mi>
        </mrow>
        <mi>b</mi>
        <mn>156</mn>
        <mi>d</mi>
        <mn>3</mn>
      </mrow>
      <annotation>a(x) + af(x) + abc123 + afbc123b + f(x) + g(xy1) + f1a + fa123a + g1aaabr + gab156d3</annotation>
    </semantics>
  </math>	 */
	@org.junit.Test
	 public void test_f0() { testRoutine("a(x)f + af(x)fa + abc123 + afbc123b + f(x) + g(xy1) + f1a + fa123a + g1aaabr + gab156d3");}

	
	@org.junit.Test
	 public void test_f1() { testRoutine("f123f + f123ggab");}
	
	/*
	 * (x-x_6^3)*(z-sqrt(xy)
  <math>
    <mrow>
      <mo>(</mo>
      <mi>x</mi>
      <mo>-</mo>
      <mrow>
        <msubsup>
          <mi>x</mi>
          <mn>6</mn>
          <mn>3</mn>
        </msubsup>
      </mrow>
      <mo>)</mo>
    </mrow>
    <mo>?*</mo>
    <mrow>
      <mo>(</mo>
      <mi>z</mi>
      <mo>-</mo>
      <msqrt>
        <mrow>
          <mi>x</mi>
          <mi>y</mi>
        </mrow>
      </msqrt>
      <mo>)</mo>
    </mrow>
  </math>
	 */
/*
<math>
    <mrow>
      <mo>[</mo>
      <mtable>
        <mtr>
          <mtd>
            <mi>a</mi>
          </mtd>
          <mtd>
            <mi>b</mi>
          </mtd>
        </mtr>
        <mtr>
          <mtd>
            <mi>c</mi>
          </mtd>
          <mtd>
            <mi>d</mi>
          </mtd>
        </mtr>
      </mtable>
      <mo>]</mo>
    </mrow>
    <mrow>
      <mo>(</mo>
      <mtable>
        <mtr>
          <mtd>
            <mi>e</mi>
          </mtd>
        </mtr>
        <mtr>
          <mtd>
            <mi>h</mi>
          </mtd>
        </mtr>
      </mtable>
      <mo>)</mo>
    </mrow>
  </math>*/
	  public void test_matrix_column() { testRoutine("[[a,b],[c,d]]((e),(h))");}
	/*
	
  <math>
    <mrow>
      <mo>[</mo>
      <mtable>
        <mtr>
          <mtd>
            <mi>a</mi>
          </mtd>
          <mtd>
            <mi>b</mi>
          </mtd>
        </mtr>
        <mtr>
          <mtd>
            <mi>c</mi>
          </mtd>
          <mtd>
            <mi>d</mi>
          </mtd>
        </mtr>
        <mtr>
          <mtd>
            <mi>e</mi>
          </mtd>
          <mtd>
            <mi>h</mi>
          </mtd>
        </mtr>
      </mtable>
      <mo>]</mo>
    </mrow>
  </math>
	*/
	  public void test_matrix_2x3_1() { testRoutine("[[a,b],[c,d],[e,h]]");}
		/*
		 <math>
		    <mi>a</mi>
		    <mn>1</mn>
		    <mo>+</mo>
		    <mi>a</mi>
		    <mn>2</mn>
		    <mo>=</mo>
		    <mi>c</mi>
		    <mn>33</mn>
		  </math>
		*/
		  public void test_parameter_2() { testRoutine("a1+a2=c33");}
	  
/*
	  <math>
	    <mrow>
	      <mi>f</mi>
	      <mo>=</mo>
	    </mrow>
	    <mi>a</mi>
	    <mn>1</mn>
	    <mo>+</mo>
	    <mi>a</mi>
	    <mn>2</mn>
	    <mo>+</mo>
	    <mrow>
	      <mi>f</mi>
	      <mn>1</mn>
	    </mrow>
	    <mo>+</mo>
	    <mrow>
	      <mi>f</mi>
	      <mn>2</mn>
	    </mrow>
	    <mo>+</mo>
	    <mrow>
	      <mi>g</mi>
	      <mrow>
	        <mo>(</mo>
	        <mi>x</mi>
	        <mo>)</mo>
	      </mrow>
	    </mrow>
	    <mo>+</mo>
	    <mrow>
	      <mi>g</mi>
	      <mn>1</mn>
	    </mrow>
	    <mo>+</mo>
	    <mrow>
	      <mi>g</mi>
	      <mn>2</mn>
	    </mrow>
	  </math>
*/
	
	
	/*
	  <math>
	    <mrow>
	      <mo>[</mo>
	      <mtable> - add "," between children
	        <mtr>  - add "[", process children, then add "]" at the end
	          <mtd>
	            <mi>a</mi>
	          </mtd>
	          <mtd>
	            <mi>b</mi>
	          </mtd>
	        </mtr>
	        <mtr> [c,d]
	          <mtd>
	            <mi>c</mi>
	          </mtd>
	          <mtd>
	            <mi>d</mi>
	          </mtd>
	        </mtr>
	      </mtable>
	      <mo>]</mo>
	    </mrow>
	  </math>
	*/
	public void test_matrix_2x2() { testRoutine("[[a,b],[c,d]]");}
	/*
	 * 
  <math>
    <mrow>
      <mo>[</mo>
      <mrow>
        <mo>[</mo>
        <mi>a</mi>
        <mo>,</mo>
        <mi>b</mi>
        <mo>]</mo>
      </mrow>
      <mo>,</mo>
      <mrow>
        <mo>[</mo>
        <mi>c</mi>
        <mo>,</mo>
        <mi>d</mi>
        <mo>]</mo>
      </mrow>
      <mo>,</mo>
      <mrow>
        <mo>[</mo>
        <mi>e</mi>
        <mo>,</mo>
        <mrow> -----------------because f it seems creating <mrow></mrow>
          <mi>f</mi>
          <mo>]</mo>
        </mrow>
        <mo>]</mo>
      </mrow>
    </mrow>
  </math>
	 */
	@org.junit.Test                              
	public void test_matrix_2x3_0() { testRoutine("[[a,b],[c,d], [e,f]]");}
	/*
	 * 
  <math>
    <mrow>
      <mo>[</mo>
      <mtable>
        <mtr>
          <mtd>
            <mi>x</mi>
          </mtd>
          <mtd>
            <mi>y</mi>
          </mtd>
        </mtr>
        <mtr>
          <mtd>
            <mi>z</mi>
          </mtd>
          <mtd>
            <mi>x</mi>
            <mi>y</mi>
          </mtd>
        </mtr>
        <mtr>
          <mtd>
            <mi>a</mi>
          </mtd>
          <mtd>
            <mi>b</mi>
          </mtd>
        </mtr>
      </mtable>
      <mo>]</mo>
    </mrow>
  </math>*/
	@org.junit.Test
	public void test_matrix_2x3_2() { testRoutine("[[x,y],[z,xy], [a,b]]");}

	/*
	  <math>
	    <mrow>
	      <msubsup>
	        <mo>?integral</mo>
	        <mn>0</mn>
	        <mn>1</mn>
	      </msubsup>
	    </mrow>
	    <mrow>
	      <mi>f</mi>
	      <mrow>
	        <mo>(</mo>
	        <mi>x</mi>
	        <mo>)</mo>
	      </mrow>
	    </mrow>
	    <mrow>
	      <mi>d</mi>
	      <mi>x</mi>
	    </mrow>
	  </math>
	*/
	@org.junit.Test
	public void test_integral0() { testRoutine("int_0^1f(x)dx");}
	/*
	  <math>
	    <mrow>
	      <mi>f</mi>
	      <mrow>
	        <mo>(</mo>
	        <mi>x</mi>
	        <mo>)</mo>
	      </mrow>
	    </mrow>
	    <mo>=</mo>
	    <mrow>
	      <munderover>
	        <mo>?sum</mo>
	        <mrow>
	          <mi>b</mi>
	          <mo>=</mo>
	          <mn>0</mn>
	        </mrow>
	        <mo>?infinity</mo>
	      </munderover>
	    </mrow>
	    <mfrac>
	      <mrow>
	        <msup>
	          <mi>f</mi>
	          <mrow>
	            <mrow>
	              <mo>(</mo>
	              <mi>b</mi>
	              <mo>)</mo>
	            </mrow>
	          </mrow>
	        </msup>
	        <mrow>
	          <mo>(</mo>
	          <mi>a</mi>
	          <mo>)</mo>
	        </mrow>
	      </mrow>
	      <mrow>
	        <mi>b</mi>
	        <mo>!</mo>
	      </mrow>
	    </mfrac>
	    <msup>
	      <mrow>
	        <mo>(</mo>
	        <mi>x</mi>
	        <mo>-</mo>
	        <mi>a</mi>
	        <mo>)</mo>
	      </mrow>
	      <mi>b</mi>
	    </msup>
	  </math>
	
	*/
	
	@org.junit.Test
	public void test_infinity_frac() { testRoutine("f(x)=sum_{b=0}^inftyfrac{f^{(b)}(a)}{b!}(x-a)^b");}
	/*
	  <math>
	    <mrow>
	      <mo>(</mo>
	      <mrow>
	        <mi>f</mi>
	        <mrow>
	          <mo>(</mo>
	          <mi>x</mi>
	          <mo>)</mo>
	        </mrow>
	      </mrow>
	      <mo>)</mo>
	    </mrow>
	    <mo>=</mo>
	    <mrow>
	      <mo>(</mo>
	      <mrow>
	        <munderover>
	          <mo>?sum</mo>
	          <mrow>
	            <mi>b</mi>
	            <mo>=</mo>
	            <mn>0</mn>
	          </mrow>
	          <mo>?infinity</mo>
	        </munderover>
	      </mrow>
	      <mo>)</mo>
	    </mrow>
	    <mfrac>
	      <mrow>
	        <msup>
	          <mi>f</mi>
	          <mrow>
	            <mi>b</mi>
	          </mrow>
	        </msup>
	        <mrow>
	          <mo>(</mo>
	          <mi>a</mi>
	          <mo>)</mo>
	        </mrow>
	      </mrow>
	      <mrow>
	        <mi>b</mi>
	        <mo>!</mo>
	      </mrow>
	    </mfrac>
	    <msup>
	      <mrow>
	        <mo>(</mo>
	        <mi>x</mi>
	        <mo>-</mo>
	        <mi>a</mi>
	        <mo>)</mo>
	      </mrow>
	      <mi>b</mi>
	    </msup>
	  </math>*/
	@org.junit.Test
	public void test_infinity_frac2() { testRoutine("(f(x))=(sum_(b=0)^oo)(f^(b)(a))/(b!)(x-a)^b");}
	
	
	
	/*
	  <math>
	    <mrow>
	      <mi>f</mi>
	      <mrow>
	        <mo>(</mo>
	        <mi>x</mi>
	        <mo>)</mo>
	      </mrow>
	    </mrow>
	    <mo>=</mo>
	    <mrow>
	      <munderover>
	        <mo>?sum</mo>
	        <mrow>
	          <mi>b</mi>
	          <mo>=</mo>
	          <mn>0</mn>
	        </mrow>
	        <mo>?infinity</mo>
	      </munderover>
	    </mrow>
	    <mfrac>
	      <mrow>
	        <msup>
	          <mi>f</mi>
	          <mrow>
	            <mrow>
	              <mo>(</mo>
	              <mi>b</mi>
	              <mo>)</mo>
	            </mrow>
	          </mrow>
	        </msup>
	        <mrow>
	          <mo>(</mo>
	          <mi>a</mi>
	          <mo>)</mo>
	        </mrow>
	      </mrow>
	      <mrow>
	        <mi>b</mi>
	        <mo>!</mo>
	      </mrow>
	    </mfrac>
	    <msup>
	      <mrow>
	        <mo>(</mo>
	        <mi>x</mi>
	        <mo>-</mo>
	        <mi>a</mi>
	        <mo>)</mo>
	      </mrow>
	      <mi>n</mi>
	    </msup>
	  </math>*/
	@org.junit.Test
	public void test_xxx() { testRoutine("fracxy");}
	
	
/*	
	<math>
    <semantics>
      <mfrac>
        <mi>x</mi>
        <mi>y</mi>
      </mfrac>
      <annotation>fracxy</annotation>
    </semantics>
  </math>
*/
	@org.junit.Test
	public void test_frac_binary() { testRoutine("fracxy");}
	/*
	<math>
    <semantics>
      <mfrac>
        <mi>a</mi>
        <mi>b</mi>
      </mfrac>
      <annotation>a/b</annotation>
    </semantics>
  </math>
  */	
	@org.junit.Test
	public void test_frac_infix() { testRoutine("a/b");}
	
	
	/*
	<math>
    <semantics>
      <mrow>
        <mi>x</mi>
        <mover> -------------------------- mover {input:"vec", tag:"mover", output:"\u2192", tex:null, ttype:UNARY, acc:true},
          <mn>5</mn>
          <mo>?</mo>
        </mover>
      </mrow>
      <annotation>xvec5</annotation>
    </semantics>
  </math>
	 */
	@org.junit.Test
	public void test_u2192_vec() { testRoutine("xvec5");}
	/*
	{input:"rarr", tag:"mo", output:"\u2192", tex:"rightarrow", ttype:CONST},
	{input:"->",   tag:"mo", output:"\u2192", tex:"to", ttype:CONST},
	<math>
    <semantics>
      <mrow>
        <mi>x</mi>
        <mo>?</mo>
        <mn>5</mn>
      </mrow>
      <annotation>x->5</annotation>
    </semantics>
  </math>
	 */
	@org.junit.Test
	public void test_u2192_rarr() { testRoutine("x->5");}

	
/*
	  <math>
	    <mrow>
	      <munderover>
	        <mo>?</mo>
	        <mrow>
	          <mi>x</mi>
	          <mo>=</mo>
	          <mn>1</mn>
	        </mrow>
	        <mi>y</mi>
	      </munderover>
	    </mrow>
	    <msup>
	      <mi>i</mi>
	      <mn>3</mn>
	    </msup>
	    <mo>=</mo>
	    <msup>
	      <mrow>
	        <mo>(</mo>
	        <mfrac>
	          <mrow>
	            <mi>x</mi>
	            <mrow>
	              <mo>(</mo>
	              <mi>x</mi>
	              <mo>+</mo>
	              <mn>1</mn>
	              <mo>)</mo>
	            </mrow>
	          </mrow>
	          <mn>2</mn>
	        </mfrac>
	        <mo>)</mo>
	      </mrow>
	      <mn>2</mn>
	    </msup>
	  </math>*/
	@org.junit.Test
	public void test_complex_subscripts_are_bracketed_displayed_under_lim() { testRoutine("sum_(i=1)^n i^3=((n(n+1))/2)^2");}

	
/*
   <math>
    <msqrt>
      <msqrt>
        <mroot>
          <mi>x</mi>
          <mn>3</mn>
        </mroot>
      </msqrt>
    </msqrt>
    <mo>-</mo>
    <mi>z</mi>
    <mi>z</mi>
  </math>
*/
	@org.junit.Test
	public void test_unary0() { testRoutine("sqrtsqrtroot3x-zz");}
	/*
	  <math>
	    <msqrt>
	      <msqrt>
	        <mroot>
	          <mrow>
	            <mi>x</mi>
	            <mo>-</mo>
	            <mi>z</mi>
	            <mi>z</mi>
	          </mrow>
	          <mn>3</mn>
	        </mroot>
	      </msqrt>
	    </msqrt>
	  </math>
	 */
	@org.junit.Test
	public void test_unary1() { testRoutine("sqrtsqrtroot3(x-zz)");}
/*	
	<math>
    <msqrt>
      <mrow>
        <msqrt>
          <mrow>
            <mroot>
              <mrow>
                <mi>x</mi>
              </mrow>
              <mrow>
                <mn>3</mn>
              </mrow>
            </mroot>
          </mrow>
        </msqrt>
      </mrow>
    </msqrt>
  </math>
*/
	@org.junit.Test
	public void test_unary2() { testRoutine("sqrt(sqrt(root(3)(x)))");}
/*
	<math>
    <mover>
      <mrow>
        <mi>a</mi>
        <mi>b</mi>
      </mrow>
      <mo>^</mo>
    </mover>
    <mover>
      <mrow>
        <mi>x</mi>
        <mi>y</mi>
      </mrow>
      <mo>�</mo>
    </mover>
    <munder>
      <mi>A</mi>
      <mo>?</mo>
    </munder>
    <mover>
      <mi>v</mi>
      <mo>?</mo>
    </mover>
    <mover>
      <mi>x</mi>
      <mo>.</mo>
    </mover>
    <mover>
      <mi>y</mi>
      <mo>..</mo>
    </mover>
  </math>
	*/
	@org.junit.Test
	public void test_unary_mover() { testRoutine("hat(ab) bar(xy) ulA vec v dotx ddoty");} //ab^
	//mover munder flips work for hat and bar but not work for lim
	
	 /* <math>
	    <munder>
	      <mo>lim</mo>
	      <mrow>
	        <mi>h</mi>
	        <mo>?</mo> /u2192
	        <mn>0</mn>
	      </mrow>
	    </munder>
	    <mfrac>
	      <mrow>
	        <mrow>
	          <mi>f</mi>
	          <mrow>
	            <mo>(</mo>
	            <mi>x</mi>
	            <mo>+</mo>
	            <mi>h</mi>
	            <mo>)</mo>
	          </mrow>
	        </mrow>
	        <mo>-</mo>
	        <mrow>
	          <mi>f</mi>
	          <mrow>
	            <mo>(</mo>
	            <mi>x</mi>
	            <mo>)</mo>
	          </mrow>
	        </mrow>
	      </mrow>
	      <mi>h</mi>
	    </mfrac>
	  </math>
	*/
	@org.junit.Test
	public void test_munder_lim() { testRoutine("lim_(h->0)(f(x+h)-f(x))/h");} //d/dxf(x)=lim_(h->0)(f(x+h)-f(x))/h
	
	 
	
	@org.junit.Test
	public void test_why2() { testRoutine("a=fun(x,y,z,xy)+LQR(x,y,z,xy)");}
	
	@org.junit.Test
	public void test14() {	testRoutine("sqrt(((x*(a+F))/sin(alpha-beta))^(epsilon/beta)+cos(gamma)/tan((x*a^-(delta))/(x+a)))");	}
	@org.junit.Test
	public void test15() {	testRoutine("grad(grad(epsilon))+int(x^4.0-sum(a/(a+1.0)))");	}
	@org.junit.Test
	public void test16() {	testRoutine("epsilon+gamma=mass^beta");	}
	@org.junit.Test
	public void test16_1() {testRoutine("mass+gamma=epsilon^beta");	}
	
	@org.junit.Test
	public void test17() {	testRoutine("Z=fun(i,u,f,Z)+LQR(H)");	} //f is not a vaild constraint parameter
	@org.junit.Test
	public void test18() {	testRoutine("X=-(1.0)/(2.0*pi*a*C)");	}
	@org.junit.Test
	public void test18_1() {	testRoutine("f(x)=5*x+2");	}
	@org.junit.Test
	public void test19() {	testRoutine("a=F/m-c/m*x-d/m*v");	}
	@org.junit.Test
	public void test20() {	testRoutine("c=a*b");	}
	/*@org.junit.Test
	public void test21() {	testRoutine("(sqrt,(+,(^,(/,(*,x,(+,a,F)),(sin,(-,alpha,beta))),(/,epsilon,beta)),(/,(cos,gamma),(tan,(/,(*,x,(^,a,(-,delta))),(+,x,a))))))");	}
	@org.junit.Test
	public void test22() {	testRoutine("(sqrt,(+,(^,(/,(*,x,(+,a,F)),(sin,(-,alpha,beta))),(/,epsilon,beta)),(/,(cos,gamma),(tan,(/,(*,x,(^,a,(-,delta))),(+,x,a))))))");	}
	*/
	
	@org.junit.Test
	public void test1() {	testRoutine("sin(x)", "sinx");}	
	@org.junit.Test
	public void test1_1() {	testRoutine("sin^-12.5(x)");}
	@org.junit.Test
	public void test_subsuper0() {testRoutine("sin_22^x(x)");}
	@org.junit.Test
	public void test_subsuper1() {testRoutine("sin_22^(x444)(x)");}
	@org.junit.Test
	public void test_subsuper2() {testRoutine("sin_22^x444(x)");}
	
	@org.junit.Test
	public void test1_3() {	testRoutine("custFunc(x,y,z)", "custFuncxyz");}
	
	
	/*
  <math>
    <msup>
      <mi>x</mi>
      <mn>2</mn>
    </msup>
    <mo>+</mo>
    <msub>
      <mi>y</mi>
      <mn>1</mn>
    </msub>
    <mo>+</mo>
    <mrow>
      <msubsup>
        <mi>z</mi>
        <mn>12</mn>
        <mn>34</mn>
      </msubsup>
    </mrow>
    <mo>+</mo>
    <mi>x</mi>
  </math>*/
	
	@org.junit.Test
	public void test_msub_msup_msubsup_1() {testRoutine("x^2+y_1+z_12^34+x"); };
	
	/*
<math>
    <msup>
      <mi>x</mi>
      <mi>t</mi>
    </msup>
    <mo>+</mo>
    <msub>
      <mi>y</mi>
      <mi>z</mi>
    </msub>
    <mo>+</mo>
    <mrow>
      <msubsup>
        <mi>z</mi>
        <mn>12</mn>
        <mi>a</mi>
      </msubsup>
    </mrow>
    <mi>b</mi>
</math>
*/
	@org.junit.Test
	public void test_msub_msup_msubsup_2() {testRoutine("x^t+y_z+z_12h"); };
/*
 <math>
    <msup>
      <mi>x</mi>
      <mn>11.7</mn>
    </msup>
    <mo>+</mo>
    <msub>
      <mi>y</mi>
      <mi>z</mi>
    </msub>
    <mo>+</mo>
    <msub>
      <mi>z</mi>
      <mi>a</mi>
    </msub>
    <msup>
      <mi>b</mi>
      <mi>c</mi>
    </msup>
    <mi>d</mi>
  </math>	
 */
	@org.junit.Test
	public void test_msub_msup_msubsup_3() {testRoutine("x^11.7+y_z+z_ab^cd"); };
/*
 *   <math>
    <msup>
      <mi>x</mi>
      <mn>11.7</mn>
    </msup>
    <mo>+</mo>
    <msub>
      <mi>y</mi>
      <mi>z</mi>
    </msub>
    <mo>+</mo>
    <mrow>
      <msubsup>
        <mi>z</mi>
        <mrow>
          <mi>b</mi>
          <mi>c</mi>
        </mrow>
        <mrow>
          <mi>c</mi>
          <mi>d</mi>
        </mrow>
      </msubsup>
    </mrow>
  </math>
	
 */
	@org.junit.Test
	public void test_msub_msup_msubsup_4() {testRoutine("x^11.7+y_z+z_(bc)^(cd)"); };
	/*
  <math>
    <msup>
      <mi>x</mi>
      <mrow>
        <mo>-</mo>
        <mn>11.7</mn>
      </mrow>
    </msup>
    <mo>+</mo>
    <msub>
      <mi>y</mi>
      <mi>z</mi>
    </msub>
    <mo>+</mo>
    <mi>z</mi>
    <mi>z</mi>
    <mrow>
      <msubsup>
        <mi>z</mi>
        <mrow>
          <mo>-</mo> need () when -/+ variable name
          <mi>b</mi>
          <mi>c</mi>
        </mrow>
        <mrow>
          <mi>a</mi> need () mrow has mix (not number, not variable)
          <mi>b</mi>
          <mo>-</mo>
          <mi>d</mi>>
        </mrow>
      </msubsup>
    </mrow>
  </math>
	 */
	@org.junit.Test
	public void test_msub_msup_msubsup_5() {testRoutine("x^-11.7+y_z+zzz_(-bc)^(ab-c)"); };
/*
 <math>
    <mo>-</mo>
    <msup>
      <mn>12.9</mn>
      <mrow>
        <mo>-</mo>
        <mn>11.7</mn>
      </mrow>
    </msup>
    <mo>+</mo>
    <msub>
      <mn>6.2</mn>
      <mrow>
        <mo>-</mo>
        <mn>11.3</mn>
      </mrow>
    </msub>
    <mo>+</mo>
    <mrow>
      <msubsup>
        <mo>�</mo>
        <mrow>
          <mi>b</mi>
          <mi>c</mi>
        </mrow>
        <mrow>
          <mo>+</mo>
          <mi>c</mi>
          <mi>d</mi>
        </mrow>
      </msubsup>
    </mrow>
  </math>
 */
	@org.junit.Test
	public void test_msub_msup_msubsup_6() {testRoutine("-12.9^-11.7+6.2_-11.3+xx_(bc)^(+cd)"); };
	/*
 <math>
    <mo>-</mo>
    <msup>
      <mi>xy</mi>
      <mrow>
        <mo>-</mo>
        <mn>11.7</mn>
      </mrow>
    </msup>
  </math>	 */
	@org.junit.Test
	public void test_msub_msup_msubsup_7() {testRoutine("-x^-11.7"); };
	/*
  <math>
    <mo>-</mo>
    <mi>x</mi>
    <msup>
      <mi>y</mi>
      <mrow>
        <mo>-</mo>
        <mn>11.7</mn>
      </mrow>
    </msup>
  </math>
	 */
	@org.junit.Test
	public void test_msub_msup_msubsup_8() {testRoutine("-xy^-11.7"); };
	/*
  <math>
    <mo>-</mo>
    <msup>
      <mrow>
        <mo>(</mo>
        <mi>x</mi>
        <mi>y</mi>
        <mo>)</mo>
      </mrow>
      <mrow>
        <mo>-</mo>
        <mn>11.7</mn>
      </mrow>
    </msup>
    <mo>+</mo>
    <msub>
      <mn>12</mn>
      <mrow>
        <mo>-</mo>
        <mi>x</mi>
      </mrow>
    </msub>
  </math>	 */
	@org.junit.Test
	public void test_msub_msup_msubsup_9() {testRoutine("-(xy)^-11.7 + 12_-x"); };
	
	@org.junit.Test
	public void test3() {testRoutine("x^(-1)");}
	@org.junit.Test
	public void test4() {testRoutine("a//b");}
	@org.junit.Test
	public void test5() { testRoutine("[a,b]", "ab");}
	@org.junit.Test
	public void test6() {testRoutine("[[a,b],[c,d]]");}
	@org.junit.Test
	public void test7() {testRoutine("((a,b),(c,d))");}
	@org.junit.Test
	public void test8() {testRoutine("frac(5)(6)");}
	@org.junit.Test
	public void test8_1() {testRoutine("frac(x)(y)");}
	@org.junit.Test
	public void test9() {testRoutine("a=b");}
	@org.junit.Test
/*	gov.nasa.jpl.mbee.mdk.expression.antlr.generated.ArithmeticBinaryParser$UnaryExpContext
	gov.nasa.jpl.mbee.mdk.expression.antlr.generated.ArithmeticBinaryParser$UnarySubExpContext
	gov.nasa.jpl.mbee.mdk.expression.antlr.generated.ArithmeticBinaryParser$ParExpContext
	gov.nasa.jpl.mbee.mdk.expression.antlr.generated.ArithmeticBinaryParser$LcbracketContext
	gov.nasa.jpl.mbee.mdk.expression.antlr.generated.ArithmeticBinaryParser$BinaryExp1Context
	gov.nasa.jpl.mbee.mdk.expression.antlr.generated.ArithmeticBinaryParser$LitExpContext
	gov.nasa.jpl.mbee.mdk.expression.antlr.generated.ArithmeticBinaryParser$LitExpContext
	gov.nasa.jpl.mbee.mdk.expression.antlr.generated.ArithmeticBinaryParser$RcbracketContext
*/
	public void test10() {testRoutine("lim_(x->oo)f(x)");}
	@org.junit.Test
	public void test10_1() {testRoutine("limf(x)");}
	
	@org.junit.Test
	public void test11() {testRoutine("int_0^1f(x)dx");}
	@org.junit.Test
	public void test12() { testRoutine("d/dxf(x)=lim_(h->0)((f(x+h)-f(x))/h)");}
	@org.junit.Test
	public void test12_1() { testRoutine("d/dxx^2");}
	@org.junit.Test
	public void test12_2() { testRoutine("d/dxg(x)");}
	@org.junit.Test
	public void test12_3() { testRoutine("dy/dx");}
	@org.junit.Test
	public void test12_4() { testRoutine("d/dxy");}
	@org.junit.Test
	public void test13(){testRoutine("(f^(n)(a))/(n!)(x-a)^n");} //f(x) =sum_(n=0)^(oo)(f^((n))(a))/(n!)(x-a)^n"	
	@org.junit.Test
	public void test13_01(){testRoutine("f(x)=sum_(n=0)^oo(f^(n)(a))/(n!)(x-a)^n");}
	@org.junit.Test
	public void test13_02(){testRoutine("f(x)=sum_(n=0)^oo(f^n(a))/(n!)(x-a)^n");}	
	@org.junit.Test
	public void test13_1(){testRoutine("a=n!");}
	@org.junit.Test
	public void test13_2(){testRoutine("a=n!*(xxx)+b");}

	@org.junit.Test
	public void test_gf() { testRoutine("gf");}
	
	@org.junit.Test
	public void test_gfg() { testRoutine("gffg+a+f(xy)");}
	
	@org.junit.Test
	public void test_docpreprocess_0() { testRoutine("d/dxf(x)=lim_(a->0)(f(x+a)-f(x))/a");}
	@org.junit.Test
	public void test_docpreprocess_1() { testRoutine("d/dx(f(x))=lim_(a->0)(f(x+a)-g(x))/a");}
	
	/*
	 * <math>
    <munder>
      <mo>?</mo>
      <mrow>
        <mi>i</mi>
        <mo>=</mo>
        <mn>1</mn>
      </mrow>
    </munder>
  </math>
	 */
	@org.junit.Test
	public void test_munder0() { testRoutine("sum_(i=1)");} //////////////supported
	
	
	/*
    <math>
    <munder>
      <mo>lim</mo>
      <mrow>
        <mi>a</mi>
        <mo>?</mo>
        <mn>0</mn>
      </mrow>
    </munder>
  </math>
	 */
	@org.junit.Test
	public void test_munder1() { testRoutine("lim_(a->0)");}/////////////supported
	
	/*
	   <math>
    <msub>
      <mi>x</mi>
      <mn>123</mn>
    </msub>
    <mo>+</mo>
    <msub>
      <mi>a</mi>
      <mi>i</mi>
    </msub>
  </math>
	  */
	@org.junit.Test
	public void test_msub0() {	testRoutine("x_123 + a_i");} /////////////////////// supported

	/*
	 * 
	<math>
    	<mi>x</mi>
    	<msub>
      		<mi>y</mi>
      		<mn>123</mn>
    	</msub>
  	</math>
	*/
	@org.junit.Test
	public void test_msub1() {	testRoutine("xy_123");} //////////////////////////supported xy1_2345666 x1z1_x (after _ must have a one character or number to be a valid constraint parameter)
	/*<math>
	    <msub>
	      <msqrt>
	        <mi>x</mi>
	      </msqrt>
	      <mn>123</mn>
	    </msub>
	    <mo>+</mo>
	    <msqrt>
	      <mi>x</mi>
	    </msqrt>
	    <msub>
	      <mi>y</mi>
	      <mn>123</mn>
	    </msub>
	  </math>*/
	@org.junit.Test
	public void test_msub11() {	testRoutine("sqrtx_123 + sqrt(x_123) + sqrt(xy_123)");} 
	
	/*
	 * <math>
    <msub>
      <mi>?</mi> - alpha unicode character(length = 1)
      <mn>5</mn>
    </msub>
  </math>
	 */
	
	@org.junit.Test
	public void test_msub2() {	testRoutine("alpha_5 + alpha5");} //////////////////////////supported
	/* must have () around variable otherwise asciimath xml show as as a/x*z23z
	 *   <math>
    <mfrac>
      <mi>a</mi>
      <mrow>
        <mi>x</mi>
        <mi>z</mi>
        <mn>23</mn>
        <mi>z</mi>
      </mrow>
    </mfrac>
  </math>
	 */
	@org.junit.Test
	public void test_mfrac() {	testRoutine("a/(xz23z)");}////////////////////////supported

	
	/*
 * <math>
    <mfrac>
      <mi>a</mi>
      <mi>x</mi>
    </mfrac>
    <mi>z</mi>
    <mn>23</mn>
    <msub>
      <mi>z</mi>
      <mn>5</mn>
    </msub>
    <mo>)</mo>
  </math>
 */
	@org.junit.Test
	public void test_msub3() {	testRoutine("a/xz23z_5");} ///////////////asciimath xml display as a/x and then z23z_5 so to consider xz23z_5 as a constraint parameter it must be enclosed by ()
	/* <math> //asciimath xml parser requires xz23z_5 enclosed by () otherwise consider denominator as just x.
	    <mfrac>
	      <mi>a</mi>
	      <mrow>
	        <mi>x</mi>
	        <mi>z</mi>
	        <mn>23</mn>
	        <msub>
	          <mi>z</mi>
	          <mn>5</mn>
	        </msub>
	      </mrow>
	    </mfrac>
	    <mo>)</mo>
	  </math>*/
	@org.junit.Test
	public void test_msub4() {	testRoutine("a/(xz23z_5)");} //////////////////////supported

	
/*<math>
    <msub>
      <mi>a</mi>
      <mi>i</mi>
    </msub>
    <mo>=</mo>
    <mrow>
      <msubsup>
        <mi>a</mi>
        <mi>i</mi>
        <mn>1</mn>
      </msubsup>
    </mrow>
    <mo>-</mo>
    <mrow>
      <msubsup>
        <mi>a</mi>
        <mi>i</mi>
        <mn>2</mn>
      </msubsup>
    </mrow>
  </math>
	*/
	@org.junit.Test
	public void test_msubover() {	testRoutine("a_i = a_i^1-a_i^2");}

	
	/*
<math>
    <mrow>
      <mo>tan</mo>
      <mi>a</mi>
    </mrow>
    <mi>b</mi>
    <mi>c</mi>
    <mn>123</mn>
    <mi>a</mi>
 </math>

	 */
	@org.junit.Test
	public void test_tanabc123a() {	testRoutine("tanabc123a");}

	
	/*
	 *   <math>
    <mi>a</mi>
    <mo>?</mo>
    <mi>b</mi>
    <mo>+</mo>
    <mi>a</mi>
    <mo>?</mo>
    <mi>b</mi>
    <mo>+</mo>
    <mi>a</mi>
    <mo>�</mo>
    <mi>b</mi>
    <mo>+</mo>
    <mi>a</mi>
    <mi>x</mi>
    <mi>b</mi>
  </math>
	 */
	@org.junit.Test
	public void test_muliply() {	testRoutine("a*b+a**b+axxb+axb");}

	
	
	public void testRoutine(String infix){
		testRoutine(infix, null);
	}
	public void testRoutine(String infix, String prefix){

		try{
		System.out.println(infix);
		DisplayExpression de = new DisplayExpression();
		Document doc = de.display(infix);
		String x = Doc2InfixStringUtil.printXML(doc);
		System.out.println(x);
		
		DocPreprocess c = new DocPreprocess(doc);
		Document newDoc = c.process();
		String ggx = Doc2InfixStringUtil.printXML(newDoc);
		System.out.println(ggx);
		
		
		
    	AsciiMathML2Tree a2t0 = new AsciiMathML2Tree(infix);
    	a2t0.showTree();	//shows the LISP tree generated by an
    	//Doc2InfixString gg = new Doc2InfixString(doc);
    	//ValueSpecification xx = gg.printXML();
    	/*ParseTree pt0 = a2t0.parse();
	
		
    	
		DebugTree t = new DebugTree();
		t.parse(pt0);
		
		Tree2UMLExpression_InfixString	t2uml = new Tree2UMLExpression_InfixString(null, pt0, null);
		t2uml.DEBUG = true;
		try {
			ValueSpecification vs = t2uml.parse();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		assertEquals(infix, t2uml.debug);
		
		/*if ( prefix != null){
			Tree2UMLExpression_Prefix t2umlp = new Tree2UMLExpression_Prefix(null, pt0, null);
			ValueSpecification vsp = t2umlp.parse();	
			assertEquals(prefix, t2umlp.debug);
		}*/
		System.out.println("done");
		}
		catch (Exception e){
			
			fail( e.getMessage());
		}
	}
	public class DisplayExpression {
		
		private JFrame mainFrame;
		   private  JLabel lblRender;
		   private JPanel controlPanel;
		
		public DisplayExpression(){
			init();
		} 
		public void init(){
			
			mainFrame = new JFrame("Java Swing Examples");
	    	mainFrame.setSize(400,400);
	        mainFrame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
	            System.exit(0);
	         }        
	        });    
	      
	        controlPanel = new JPanel();
	        controlPanel.setLayout(new FlowLayout());
	    
		  	lblRender = new JLabel();
			lblRender.setVerticalAlignment(SwingConstants.CENTER);
			lblRender.setBackground(Color.WHITE);
			lblRender.setOpaque(true);
			mainFrame.add(lblRender);
			mainFrame.setVisible(true);  
	    
	    }
	    public Document display(String _textExpression) {
	    
	    	//AsciiMathParserOptions opt = new AsciiMathParserOptions();
	    	//opt.setAddSourceAnnotation(true);
	    	//opt.setDisplayMode(false); - nothing seems changed
		      	AsciiMathParser amp = new AsciiMathParser();
				Document docExp = amp.parseAsciiMath(_textExpression/*, opt*/);
				
				Converter getPic = Converter.getInstance();
				LayoutContextImpl l = (LayoutContextImpl) LayoutContextImpl.getDefaultLayoutContext();
				l.setParameter(Parameter.MATHSIZE, 30);
				BufferedImage pic;///
				try {
					pic = getPic.render(docExp, (LayoutContext) l);
					lblRender.setIcon(new ImageIcon(pic));
					return docExp;
				} catch (IOException e1) {
					e1.printStackTrace();
					return null;
				}
	    }
	    
	}
	public class DebugTree {
	    	
	    	public DebugTree(){}
	    	public void parse(ParseTree pt)
	    	{
	    		traverse(pt, 0);
	    	}
		    public void traverse(ParseTree pt, int depth){
		    	System.out.println("\n" + pt.getText() + "    " + pt.getClass().getName());
		    	System.out.println("num child: " + pt.getChildCount());
		    	String ds = "";
				for ( int i = 0; i < depth; i++){
					ds = ds + "\t";
				}
				++depth;
				for ( int i = 0; i < pt.getChildCount(); i++) {
					System.out.println(ds + "======" + i + " of " + pt.getChildCount() + " =====" + pt.getChild(i).getText() + " " + pt.getChild(i).getClass().getName());
				}
				for ( int i = 0; i < pt.getChildCount(); i++) {
					System.out.println(ds + "======" + i + "=====");
					//System.out.println(pt.getChild(i).getText() + " " + pt.getChild(i).getClass().getName());
					traverse(pt.getChild(i), depth);
				}
		    }
	    }

	}
	
	
}
