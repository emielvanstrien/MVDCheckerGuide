grammar MvdXMLv1_1;

@header {
package nl.tue.ddss.rule_parse;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import org.bimserver.ifc.IfcModel;
import org.bimserver.models.ifc2x3tc1.IfcObject;
import org.bimserver.models.ifc2x3tc1.IfcRoot;
import org.buildingsmart_tech.mvdxml.mvdxml1_1.*;
import nl.tue.ddss.ifc_check.IfcHashMapBuilder.ObjectToValue;
import nl.tue.ddss.ifc_check.Parameter;
import nl.tue.ddss.ifc_check.Metric;
}

@lexer::header {
package nl.tue.ddss.rule_parse;
}

@members{
HashMap <AbstractRule,ObjectToValue> hashMap;
public MvdXMLv1_1Parser(TokenStream input,HashMap <AbstractRule,ObjectToValue> hashMap) {
        this(input, new RecognizerSharedState());
        this.hashMap=hashMap;
    }
}

/*----------------
* PARSER RULES
*----------------*/
expression returns [Boolean expressionReturns]
	:	boolean_expression {$expressionReturns=$boolean_expression.result;
	};
	catch[RecognitionException re]{
	}
boolean_expression returns [Boolean result]
    	:	left=boolean_term {$result=left;}(logical=logical_interconnection right=boolean_term
    	{if (logical.equals("AND")) {
    	AndOperator andOperator=new AndOperator(left,right);
    	$result=andOperator.getResult();
    	}else if (logical.equals("OR")) {
      OrOperator orOperator=new OrOperator(left,right);
      $result=orOperator.getResult();
      }else if (logical.equals("XOR")) {
      XorOperator xorOperator=new XorOperator(left,right);
      $result=xorOperator.getResult();
      }} )*  ;
boolean_term returns [Boolean boolTermReturns]
	:	(( leftOperand=parameter ( leftOperand=metric[leftOperand] )?) op=operator ( rightOperand=value | rightOperand=parameter ( rightOperand=metric[rightOperand] )? ) )
	 {if (op.equals("=")){
	 EqualOperator equalOperator=new EqualOperator(leftOperand,rightOperand);
	 $boolTermReturns=equalOperator.getResult();
   }if (op.equals(">")){
   GreaterOperator greaterOperator=new GreaterOperator(leftOperand,rightOperand);
   $boolTermReturns=greaterOperator.getResult();
   }if (op.equals("<")){
   LessOperator lessOperator=new LessOperator(leftOperand,rightOperand);
   $boolTermReturns=lessOperator.getResult();
   }if (op.equals(">=")){
   GreaterEqualOperator greaterEqualOperator=new GreaterEqualOperator(leftOperand,rightOperand);
   $boolTermReturns=greaterEqualOperator.getResult();
   }if (op.equals("<=")){
   LessEqualOperator lessEqualOperator=new LessEqualOperator(leftOperand,rightOperand);
   $boolTermReturns=lessEqualOperator.getResult();
   }if (op.equals("!=")){
   InEqualOperator inEqualOperator=new InEqualOperator(leftOperand,rightOperand);
   $boolTermReturns=inEqualOperator.getResult();
   }  
}|  ( LPAREN valueReturns=boolean_expression RPAREN ){$boolTermReturns=valueReturns;};
parameter returns [Object paraReturns] 
	:	SIMPLEID {Parameter parameter=new Parameter($SIMPLEID.text,hashMap);
	$paraReturns=parameter.getResult();
	};
metric [Object value]returns [Object metricReturns] 	
	:	'[Value]' {Metric metric=new Metric($value);$metricReturns=metric.getMetricValue();}
	| '[Size]' {Metric metric=new Metric($value);$metricReturns=metric.getMetricSize();}
	| '[Type]' {Metric metric=new Metric($value);$metricReturns=metric.getMetricType();}| '[Unique]';
logical_interconnection returns [String logical]
	:	AND {$logical="AND";}| OR{$logical="OR";} | XOR {$logical="XOR";};
operator returns[String op]
	:	EQUAL {$op="=";}| NOT_EQUAL{$op="!=";} | GREATER_THAN {$op=">";} | GREATER_THAN_OR_EQUAL {$op=">=";}| LESS_THAN {$op="<";}| LESS_THAN_OR_EQUAL{$op="<=";};
value returns[Object valueReturns]	
	:	logical_literal{
	$valueReturns=$logical_literal.logicalReturns;
	} | real_literal {
  $valueReturns=$real_literal.realReturns;
  }| string_literal {
  $valueReturns=$string_literal.stringReturns;
  }| regular_expression{
  $valueReturns=$regular_expression.regReturns;
  };
logical_literal	returns[Boolean logicalReturns]
	: 	FALSE {$logicalReturns=false;}| TRUE {$logicalReturns=true;}| UNKNOWN ;
real_literal returns[Double realReturns]
	:	(sign)? ( DIGIT | INT ) ('.')? ( ( DIGIT | INT ) )? ( 'e' (sign)? ( DIGIT | INT ) )? {$realReturns=Double.parseDouble($real_literal.text);};
string_literal returns [String stringReturns]
	:	STRING {$stringReturns=$STRING.text.substring(1,$STRING.text.length()-1);};
regular_expression returns [String regReturns]
	:	'reg' STRING {$regReturns="reg "+$STRING.text.substring(1,$STRING.text.length()-1);};
sign 	
	:	'+' | '-' ;

/*----------------
* LEXER RULES
*----------------*/
AND 	
	:	'AND' | 'and' | '&' | ';' ;
OR 	
	:	'OR' | 'or' | '|' ;
XOR 	
	:	'XOR' | 'xor' ;
EQUAL 	
	:	'=' ;
NOT_EQUAL 
	:	'!=' ;
GREATER_THAN 
	:	'>' ; 
GREATER_THAN_OR_EQUAL 
	:	'>=' ;
LESS_THAN 
	:	'<' ;
LESS_THAN_OR_EQUAL 
	:	'<=' ;
FALSE 	
	:	'FALSE' | 'false' ;
TRUE 	
	:	'TRUE' | 'true' ;
UNKNOWN 
	:	'UNKNOWN' | 'unknown' ;
DIGIT 	
	:	'0'..'9' ;
INT 	
	:	'0'..'9'+;
HEX_DIGIT 
	:	DIGIT | ('a'..'f' | 'A'..'F') ;
LETTER 
	:	('a'..'z') | ('A'..'Z') ; 
SIMPLEID 
	:	LETTER ( LETTER | DIGIT | '_' )* ;    
LPAREN  
	:   	'(';
RPAREN  
	:   	')';  
OCTAL_ESC
	:   	'\\' ('0'..'3') ('0'..'7') ('0'..'7')   |   '\\' ('0'..'7') ('0'..'7')   |   '\\' ('0'..'7')  ;
UNICODE_ESC
	:   	'\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT ;
ESC_SEQ
    	:   	'\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')  |   UNICODE_ESC  |   OCTAL_ESC ;
STRING	
	:  	'\'' ( ESC_SEQ | ~('\\'|'\'') )* '\'';
WS 	
	:	(' '|'\t'|'\n'|'\r')+ { $channel = HIDDEN; } ;
	
