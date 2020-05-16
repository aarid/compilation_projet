package fr.ubordeaux.deptinfo.compilation.lea.abstract_syntax;

import fr.ubordeaux.deptinfo.compilation.lea.type.TypeCode;
import fr.ubordeaux.deptinfo.compilation.lea.type.TypeException;

public class StmFOR extends StmTernary {

	private Expr expr;

	public StmFOR(Expr expr, Stm stm1, Stm stm2, Stm stm3, int line, int column) {
		super(stm1, stm2, stm3, line, column);
		this.expr = expr;
	}
	
	@Override
	public void checkType() throws TypeException {
		expr.checkType();
		if (this.getFirst() != null)
			this.getFirst().checkType();
		if (this.getSecond() != null)
			this.getSecond().checkType();
		if (this.getThird() != null)
			this.getThird().checkType();
		expr.getType().assertType(this, TypeCode.BOOLEAN);
	}

	@Override
	public String generateCode() throws CodeException {
		String result = "";
		result += super.generateCode();
		// TODO
		String var = "_if_test__" + this.getId();
		String label_then = "_if_label_then__" + this.getId();
		String label_fin = "_if_label_fin__" + this.getId();

		String label_jump_for = "_FOR__" + this.getId();

		result += getFirst().generateCode();

		result += tab() + "int " + var + ";" + NL;
		// TODO
		result += tab() + label_jump_for + ":" + NL;
		this.incIndent();



		result += tab() + var + " = " + expr.generateCode() + ";" + NL;

		result += tab() + "if (" + var + ")" + NL;

		incIndent();
			result += tab() + "goto " + label_then + ";" + NL;
		decIndent();



		result += tab() + "goto " + label_fin + ";" + NL;


		result += tab() + label_then + ":{" + NL;
		incIndent();
		result += getThird().generateCode();
		decIndent();
		result += tab() + "}" + NL;


		result += getSecond().generateCode();
		result += tab() + "goto " + label_jump_for + ";" + NL;


		result += tab() + label_fin + ":{}" + NL;



		this.decIndent();
		return result;
	}

	@Override
	public String toString() {
		return "for (... ; ... ; ...)";
	}

}
