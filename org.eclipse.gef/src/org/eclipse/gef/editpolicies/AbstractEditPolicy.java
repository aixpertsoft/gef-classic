package org.eclipse.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;

/**
 * The default implementation of {@link EditPolicy}.
 * <P>
 * Since this is the default implementation of an interface, this document deals with
 * proper sub-classing.  This class is not the API.  For documentation on proper usage of
 * the public API, see the documentation for the interface itself: {@link EditPolicy}.
 * <P>
 * <Table>
 * 	 <tr>
 * 	   <TD><img src="../doc-files/green.gif"/>
 * 	   <TD>Indicates methods that are commonly overridden.
 *   </tr>
 *   <tr>
 *     <TD><img src="../doc-files/blue.gif"/>
 *     <TD>These methods might be overridden.
 * 	 </tr>
 * 	 <tr>
 * 	   <TD><img src="../doc-files/black.gif"/>
 * 	   <TD>Should rarely be overridden.
 * 	 </tr>
 *   <tr>
 * 	   <TD><img src="../doc-files/dblack.gif"/>
 * 	   <TD>Essentially "internal" and should never be overridden.
 *   </tr>
 * </table>
 * <P>
 */
public abstract class AbstractEditPolicy
	implements EditPolicy, RequestConstants
{

private EditPart host;

/**
 * Does nothing by default.
 * @see org.eclipse.gef.EditPolicy#activate() */
public void activate() { }

/**
 * Does nothing by default.
 * @see org.eclipse.gef.EditPolicy#deactivate() */
public void deactivate() { }

/**
 * This method will log the message to GEF's trace/debug system if the corrseponding flag
 * for FEEDBACK is set to true.
 * @param message the String to log */
protected final void debugFeedback(String message) {
	if (!GEF.DebugFeedback)
		return;
	GEF.debug("\tFEEDBACK:\t" + toString() + ":\t" + message);//$NON-NLS-2$//$NON-NLS-1$
}

/**
 * Does nothing by default.
 * @see org.eclipse.gef.EditPolicy#eraseSourceFeedback(Request) */
public void eraseSourceFeedback(Request request) { }

/**
 * Does nothing by default.
 * @see org.eclipse.gef.EditPolicy#eraseTargetFeedback(Request) */
public void eraseTargetFeedback(Request request) { }

/**
 * Returns <code>null</code> by default. <code>null</code> is used to indicate that the
 * EditPolicy does not contribute to the specified <code>Request</code>.
 * @see org.eclipse.gef.EditPolicy#getCommand(Request) */
public Command getCommand(Request request) {
	return null;
}

/** * @see org.eclipse.gef.EditPolicy#getHost() */
public EditPart getHost() {
	return host;
}
/**
 * Returns <code>null</code> by default. <code>null</code> indicates that this policy is
 * unable to determine the target for the specified <code>Request</code>.
 * @see org.eclipse.gef.EditPolicy#getTargetEditPart(Request) */
public EditPart getTargetEditPart(Request request) {
	return null;
}

/** * @see org.eclipse.gef.EditPolicy#setHost(EditPart) */
public void setHost(EditPart host) {
	this.host = host;
}

/**
 * Does nothing by default.
 * @see org.eclipse.gef.EditPolicy#showSourceFeedback(Request) */
public void showSourceFeedback(Request request) { }

/** * Does nothing by default.
 * @see org.eclipse.gef.EditPolicy#showTargetFeedback(Request)
 */
public void showTargetFeedback(Request request) { }

/** * @see java.lang.Object#toString() */
public String toString() {
	String c = getClass().getName();
	c = c.substring(c.lastIndexOf('.') + 1);
	return getHost().toString() + "." + c;//$NON-NLS-1$
}

/**
 * Returns <code>false</code> by default.
 * @see org.eclipse.gef.EditPolicy#understandsRequest(Request) */
public boolean understandsRequest(Request req) {
	return false;
}

}