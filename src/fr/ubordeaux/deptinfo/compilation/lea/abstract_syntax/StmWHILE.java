package fr.ubordeaux.deptinfo.compilation.lea.abstract_syntax;

import fr.ubordeaux.deptinfo.compilation.lea.type.TypeCode;
import fr.ubordeaux.deptinfo.compilation.lea.type.TypeException;

public class StmWHILE extends StmUnary {

	private Expr test;

	public StmWHILE(Expr test, Stm stm, int line, int column) {
		super(stm, line, column);
		this.test = test;
	}

	@Override
	public String generateCode() throws CodeException {

		String result = "";
		result += super.generateCode();

		String var = "_if_test__" + this.getId();
		String label_then = "_if_label_then__" + this.getId();
		String label_fin = "_if_label_fin__" + this.getId();

		String label_jump_while = "_WHILE__" + this.getId();


		result += tab() + "int " + var + ";" + NL;

		result += tab() + label_jump_while + ":" + NL;
		this.incIndent();

		result += tab() + var + " = " + test.generateCode() + ";" + NL;
		result += tab() + "if (" + var + ")" + NL;
		incIndent();
		result += tab() + "goto " + label_then + ";" + NL;
		decIndent();

		result += tab() + "goto " + label_fin + ";" + NL;

		result += tab() + label_then + ":{" + NL;
		incIndent();
		result += getSon().generateCode();
		decIndent();
		result += tab() + "}" + NL;

		result += tab() + "goto " + label_jump_while + ";" + NL;

		result += tab() + label_fin + ":{}" + NL;


		this.decIndent();
		return result;
	}

	@Override
	public void checkType() throws TypeException {
		test.checkType();
		if (this.getSon()!=null)
			getSon().checkType();
		test.getType().assertType(this, TypeCode.BOOLEAN);
	}

	@Override
	public String toString() {
		return "while ("+ test + ")";
	}

}
