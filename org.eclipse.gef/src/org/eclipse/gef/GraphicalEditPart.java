package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;
import org.eclipse.draw2d.*;

/**
 * A Specialization of {@link EditPart} for use with {@link GraphicalViewer
 * GraphicalViewers}.  The <i>visual part</i> of a GraphicalEditPart is a {@link
 * org.eclipse.draw2d.IFigure Figure}.
 */
public interface GraphicalEditPart
	extends EditPart
{

/**
 * Returns the primary Figure representing this GraphicalEditPart. The parent will add
 * this Figure to its <i>content pane</i>.  The Figure may be a composition of several
 * Figures.
 * @return this EditPart's Figure
 */
IFigure getFigure();

/**
 * Returns the <i>source</i> connections for this GraphicalEditPart. This method should
 * only be called by the EditPart itself, and its helpers such as EditPolicies.
 * @return the source connections
 */
List getSourceConnections();

/**
 * Returns the <i>target</i> connections for this GraphicalEditPart. This method should
 * only be called by the EditPart itself, and its helpers such as EditPolicies.
 * @return the target connections
 */
List getTargetConnections();

/**
 * The Figure into which childrens' Figures will be added. May return the same Figure as
 * {@link #getFigure()}. The GraphicalEditPart's {@link #getFigure() primary Figure} may
 * be composed of multiple figures.  This is the figure in that composition that will
 * contain children's figures.
 * @return the <i>content pane</i> Figure
 */
IFigure getContentPane();

/**
 * Sets the specified constraint for a child's Figure on the {@link #getContentPane()
 * content pane} figure for this GraphicalEditPart. The constraint will be applied to the
 * content pane's {@link LayoutManager}. <code>revalidate()</code> is called on the
 * content pane, which will cuase it to layout during the next update.
 * @param child the <i>child</i> GraphicalEditPart whose constraint is being set
 * @param figure the Figure whose constraint is being set
 * @param constraint the constraint for the draw2d {@link LayoutManager}
 */
void setLayoutConstraint(EditPart child, IFigure figure, Object constraint);

}
