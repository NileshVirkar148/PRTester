package com.acellere.corona.javacodechecker.ci.checkers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.acellere.corona.cmx.mod.cmn.Severity;
import com.acellere.corona.cmx.msg.data.auxmod.CodeIssueModel;
import com.acellere.corona.commons.cast.ObjCaster;
import com.acellere.corona.javaparsercore.ParseContext;
import com.acellere.corona.javaparsercore.ci.ICodeIssuePublisher;
import com.acellere.corona.javaparsercore.exceptions.ParserException;
import com.acellere.corona.javaparsercore.jp.parserdb.query.NodeInfo;
import com.acellere.corona.javaparsercore.jp.sym.ScopeResolver;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ExpressionStmt;

public class AvoidFilecreateTempFile extends BaseChecker {    
	@Override
	protected void handleVisit(Node node, ParseContext context, ICodeIssuePublisher codeIssuePublisher)
			throws ParserException {
 
		MethodCallExpr md = ObjCaster.castNode(MethodCallExpr.class, node);

	}

	private void publishCodeIssue(Node node, ParseContext context, ICodeIssuePublisher codeIssuePublisher) {
		codeIssuePublisher.publish(new CodeIssueModel(context.getCu().getCuFile(), node.getRange().begin.line,
				Severity.high, "Avoid FilecreateTempFile",
				CodeIssueNames.AVOID_FILE_CREATE_TEMP_FILE));
	}
}
