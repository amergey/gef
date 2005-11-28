/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.tests;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.viewers.NestedGraphViewer;
import org.eclipse.mylar.zest.layouts.LayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.LayoutStyles;
import org.eclipse.mylar.zest.layouts.algorithms.FadeLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.algorithms.HorizontalLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.algorithms.VerticalLayoutAlgorithm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.ViewPart;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 * 
 * @author Ian Bull
 * @author Chris Callendar
 */
public class SampleNestedView extends ViewPart {
	
	private NestedGraphViewer nestedViewer;
	private Action action;
	private ZestImages images;
	
	/**
	 * The constructor.
	 */
	public SampleNestedView() {
		this.images = new ZestImages();
	}
	
	
	class MyResourceChangedListener implements IResourceChangeListener {

		NestedGraphViewer _viewer;
		public MyResourceChangedListener( NestedGraphViewer viewer ) {
			_viewer = viewer;
			
		}
		public void resourceChanged(IResourceChangeEvent event) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					if (_viewer.getContentProvider() != null) {
						// must have a content provider
						_viewer.setInput(ResourcesPlugin.getWorkspace().getRoot().getProjects() );
					}
				}
			});
		}
		
	}
	
	
	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		nestedViewer = new NestedGraphViewer(parent, 
				ZestStyles.HIGHLIGHT_ADJACENT_NODES | /* ZestStyles.NO_OVERLAPPING_NODES | */ 
				ZestStyles.DIRECTED_GRAPH | ZestStyles.ZOOM_FAKE | ZestStyles.ENFORCE_BOUNDS);
		
		//NestedGraphEntityContentProvider contentProvider = new NestedGraphEntityContentProvider();
		JavaHierarchyContentProvider contentProvider = new JavaHierarchyContentProvider();
		nestedViewer.setContentProvider(contentProvider);

//		DecoratingLabelProvider labelProvider = new DecoratingLabelProvider(new NestedGraphLabelProvider(), 
//					PlatformUI.getWorkbench().getDecoratorManager().getLabelDecorator());

		DecoratingLabelProvider labelProvider = new DecoratingLabelProvider(new WorkbenchLabelProvider(),
				PlatformUI.getWorkbench().getDecoratorManager().getLabelDecorator());
		
		nestedViewer.setLabelProvider(labelProvider);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new MyResourceChangedListener(nestedViewer) );
		nestedViewer.setInput(ResourcesPlugin.getWorkspace().getRoot().getProjects());
		
		
		//makeActions();
		contributeToActionBars();
		//hookContextMenu();
	}
	
	

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		//viewer.getControl().setFocus();
		nestedViewer.getControl().setFocus();
	}
	
	protected void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				SampleNestedView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(nestedViewer.getControl());
		nestedViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, nestedViewer);
	}
	
	protected void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}
	
	protected void fillLocalToolBar(IToolBarManager manager) {
		manager.add(new Separator());	
	}

	protected void fillContextMenu(IMenuManager manager) {
		manager.add(new Separator());

		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalPullDown(IMenuManager manager) {
		action = new LayoutAction("Horizontal Layout", new HorizontalLayoutAlgorithm(LayoutStyles.NONE));
		action.setImageDescriptor(images.getImageDescriptor(ZestImages.IMG_ZEST_LAYOUT_HORIZONTAL));
		manager.add(action);
		action = new LayoutAction("Vertical Layout", new VerticalLayoutAlgorithm(LayoutStyles.NONE));
		action.setImageDescriptor(images.getImageDescriptor(ZestImages.IMG_ZEST_LAYOUT_VERTICAL));
		manager.add(action);
		action = new LayoutAction("Grid Layout", new GridLayoutAlgorithm(LayoutStyles.NONE));
		action.setImageDescriptor(images.getImageDescriptor(ZestImages.IMG_ZEST_LAYOUT_GRID));
		manager.add(action);
		action = new LayoutAction("Vertical Tree Layout", new TreeLayoutAlgorithm(LayoutStyles.NONE));
		action.setImageDescriptor(images.getImageDescriptor(ZestImages.IMG_ZEST_LAYOUT_TREE));
		manager.add(action);
		action = new LayoutAction("Horizontal Tree Layout", new HorizontalTreeLayoutAlgorithm(LayoutStyles.NONE));
		action.setImageDescriptor(images.getImageDescriptor(ZestImages.IMG_ZEST_LAYOUT_TREE_HORIZ));
		manager.add(action);
		action = new LayoutAction("Radial Layout", new RadialLayoutAlgorithm(LayoutStyles.NONE));
		action.setImageDescriptor(images.getImageDescriptor(ZestImages.IMG_ZEST_LAYOUT_RADIAL));
		manager.add(action);
		action = new LayoutAction("Spring Layout", new SpringLayoutAlgorithm(LayoutStyles.NONE));
		action.setImageDescriptor(images.getImageDescriptor(ZestImages.IMG_ZEST_LAYOUT_SPRING));
		manager.add(action);
		action = new LayoutAction("Fade Layout", new FadeLayoutAlgorithm(LayoutStyles.NONE));
		//action.setImageDescriptor(images.getImageDescriptor(ZestImages.IMG_ZEST_LAYOUT_SPRING));
		manager.add(action);
	}
	
	class LayoutAction extends Action {

		private LayoutAlgorithm algorithm;
		public LayoutAction(String name, LayoutAlgorithm algorithm) {
			this.algorithm = algorithm;
			setText(name);
		}
		
		public void run() {
			nestedViewer.setLayoutAlgorithm(algorithm);
		}
	}
	
}