package fr.ubordeaux.deptinfo.compilation.lea.abstract_syntax;

import java.util.Iterator;
import java.util.List;

import fr.ubordeaux.deptinfo.compilation.lea.type.TypeCode;
import fr.ubordeaux.deptinfo.compilation.lea.type.TypeException;

public class StmSWITCH extends StmList {

	private Expr expr;
	private Stm defaultStm; // default

	public StmSWITCH(Expr expr, List<Stm> stms, Stm stm, int line, int column) {
		super(stms, line, column);
		this.expr = expr;
		this.defaultStm = stm;
	}


/*		String result = "";
		result += super.generateCode();

		if (this.getRight()!=null) {
			result += tab() + "// Ooops, ELSE pas encore supporté, seul le THEN est généré ici" + NL;
		}
		String var = "_if_test__" + this.getId();
		String label_then = "_if_label_then__" + this.getId();
		String label_fin = "_if_label_fin__" + this.getId();
		result += tab() + "int " + var + " = " + test.generateCode() + ";" + NL;
		result += tab() + "if (" + var + ")" + NL;
		incIndent();
			result += tab() + "goto " + label_then + ";" + NL;
		decIndent();
		result += tab() + "goto " + label_fin + ";" + NL;
		result += tab() + label_then + ":{" + NL;
		incIndent();
			result += getLeft().generateCode();
		decIndent();
		result += tab() + "}" + NL;
		result += tab() + label_fin + ":{}" + NL;
		return result;
		*/
	
	@Override
	public String generateCode() throws CodeException {
		
		String result = "";
		result += super.generateCode();
		// TODO
		String end = "_FIN_SWITCH_" + this.getId();
		result += tab() + "_SWITCH_" + this.getId() + ":{" + NL;
		this.incIndent();
			Iterator<Stm> iterator = getStms().iterator();
			while (iterator.hasNext()) {
				StmCASE stmCASE = (StmCASE)iterator.next();
				String var_case = "_case_eq_expr__" + stmCASE.getId();
				String do_case = "_do_case__" + stmCASE.getId();
				String jump_case = "_jump_case__" + stmCASE.getId();
				
				result += tab() + "int " + var_case + " = " + expr.generateCode() + "==" +  stmCASE.getExpr().generateCode() + ";" + NL;
				
				result += tab() + "if (" + var_case + ")" + NL;
				incIndent();
					result += tab() + "goto " + do_case + ";" + NL;
				decIndent();
				
				result += tab() + "goto " + jump_case + ";" + NL;
				result += tab() + do_case + ":{" + NL;
				incIndent();
					result += stmCASE.generateCode();
					result += tab() + "goto " + end + ";" + NL;
				decIndent();
				result += tab() + "}" + NL;
				
				result += tab() + jump_case + ":{}" + NL + NL;				
				
			}
		result += tab() + "default_" + defaultStm.getId() + ": {" + NL;
		incIndent();
			result += defaultStm.generateCode();
		decIndent();
		result += tab() + "}" + NL;
		
		result += tab() + end + ":{}" + NL + NL;
		
		this.decIndent();
		result += tab() + "}" + NL;
		
		return result;
	}

	@Override
	public void checkType() throws TypeException {
		expr.checkType();
		TypeCode[] typeCodes = {TypeCode.INTEGER, TypeCode.ENUM};
		expr.getType().assertType(this, typeCodes);

		if (defaultStm != null)
			defaultStm.checkType();
		Iterator<Stm> iterator = getStms().iterator();
		while (iterator.hasNext()) {
			StmCASE stmCASE = (StmCASE)iterator.next();
			stmCASE.checkType();
			stmCASE.getExpr().getType().assertType(stmCASE, expr.getType());
			
		}
	}
	
	@Override
	public void indent() {
		Iterator<Stm> iterator = getStms().iterator();
		while (iterator.hasNext()) {
			Stm stm = iterator.next();
			stm.setIndent(getIndent());
			stm.indent();
		}
		if (defaultStm != null) {
			defaultStm.setIndent(getIndent());
			defaultStm.indent();
		}
	}

	@Override
	public String toString() {
		return "switch (" + expr + ")";
	}

}
