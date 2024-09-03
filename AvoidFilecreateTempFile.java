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
import javax.crypto.Cipher;

public class AvoidFilecreateTempFile extends BaseChecker {    
	@Override
	protected void handleVisit(Node node, ParseContext context, ICodeIssuePublisher codeIssuePublisher)
			throws ParserException {
 
		MethodCallExpr md = ObjCaster.castNode(MethodCallExpr.class, node);
		if (md != null) {
			boolean isfile = false;
			boolean isdelete = false;
			boolean ismkdir = false;
			boolean ismethod = false;
			List<Node> child = md.getChildrenNodes();
			for (Node nodes : child) {
				if (nodes instanceof NameExpr) {
					if (StringUtils.equals(((NameExpr) nodes).getName(), "createTempFile")) {
						ismethod = true;
					}
				}
			}
			if (ismethod) {
				Node childs = md.getParentNode();
				Node parentNode = childs.getParentNode();
				Node parentSuper = parentNode.getParentNode();
				List<Node> childss = parentSuper.getChildrenNodes();

				for (Node n : childss) {
					if (isfile == false) {
						if (n instanceof ExpressionStmt) {
							List<Node> childN = n.getChildrenNodes();
							for (Node childNode : childN) {
								if (childNode instanceof AssignExpr) {
									List<Node> assignChild = childNode.getChildrenNodes();
									for (Node assignExp : assignChild) {
										if (assignExp instanceof MethodCallExpr) {
											if (StringUtils.equals(((MethodCallExpr) assignExp).getName(),
													"createTempFile")) {
												isfile = true;
												continue;
											}
										}
									}
								}
							}
						}
						if (n instanceof MethodCallExpr) {
							if (StringUtils.equals(((MethodCallExpr) n).getName(), "createTempFile")) {
								isfile = true;
								continue;
							}
						}
					}
					if (isfile == true && !isdelete) {
						if (n instanceof ExpressionStmt) {
							List<Node> childN = n.getChildrenNodes();
							for (Node childNode : childN) {
								if (childNode instanceof MethodCallExpr) {
									if (StringUtils.equals(((MethodCallExpr) childNode).getName(), "delete")) {
										isdelete = true;
										continue;
									}
								}
							}
						}
					}

					if (isfile && isdelete) {
						if (n instanceof ExpressionStmt) {
							List<Node> chNode = n.getChildrenNodes();
							for (Node childNode : chNode) {
								if (childNode instanceof MethodCallExpr) {
									if (StringUtils.equals(((MethodCallExpr) childNode).getName(), "mkdir")) {
										ismkdir = true;
										break;
									}
								}

							}

						}
					}
				}
			}

			if (isfile && isdelete && ismkdir) {
				System.out.println("");
				publishCodeIssue(node, context, codeIssuePublisher);
			}
		}

	}

	private void publishCodeIssue(Node node, ParseContext context, ICodeIssuePublisher codeIssuePublisher) {
		codeIssuePublisher.publish(new CodeIssueModel(context.getCu().getCuFile(), node.getRange().begin.line,
				Severity.high, "Avoid FilecreateTempFile",
				CodeIssueNames.AVOID_FILE_CREATE_TEMP_FILE));
		
		Cipher c1 = Cipher.getInstance("DES"); 
	}
}
