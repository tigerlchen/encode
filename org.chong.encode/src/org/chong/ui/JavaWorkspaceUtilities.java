/*$Id: $
*--------------------------------------
* Apusic (Kingdee Middleware)
*--------------------------------------
* Copyright By Apusic ,All right Reserved
* author date comment
* Administrator 2013-5-5 Created
*/

package org.chong.ui;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.actions.SelectionConverter;
import org.eclipse.jdt.internal.ui.javaeditor.ClassFileEditor;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;

public class JavaWorkspaceUtilities {
    public static ClassFileEditor getActiveClassFileEditor(IWorkbenchPage activePage) {
        if (activePage == null) {
            return null;
        }
        IEditorPart editor = activePage.getActiveEditor();
        if ((editor instanceof ClassFileEditor)) {
            return (ClassFileEditor) editor;
        }
        return null;
    }

    public static IJavaElement getSelectedJavaElement(JavaEditor editor) throws JavaModelException {
        IJavaElement[] elements = SelectionConverter.codeResolve(editor);
        if ((elements != null) && (elements.length > 0)) {
            return elements[0];
        }
        return SelectionConverter.getInput(editor);
    }

    public static IJavaElement getJavaElementFromSelection(ISelection selection) {
        if ((selection instanceof IStructuredSelection)) {
            Object firstElement = ((IStructuredSelection) selection).getFirstElement();
            return (IJavaElement) ((firstElement instanceof IJavaElement) ? firstElement : null);
        }
        return null;
    }

    public static void writeStatusMessage(IWorkbenchPart activeWorkbenchPart, String message) {
        IStatusLineManager statusLineManager = getStatusLineManager(activeWorkbenchPart);
        if (statusLineManager == null) {
            return;
        }
        statusLineManager.setMessage(message);
    }

    private static IStatusLineManager getStatusLineManager(IWorkbenchPart part) {
        IWorkbenchPartSite site = part.getSite();
        if ((site instanceof IViewSite)) {
            IViewSite viewSite = (IViewSite) site;
            IActionBars actionBars = viewSite.getActionBars();
            if (actionBars == null) {
                return null;
            }
            return actionBars.getStatusLineManager();
        }
        if ((site instanceof IEditorSite)) {
            IEditorSite editorSite = (IEditorSite) site;
            IActionBars actionBars = editorSite.getActionBars();
            if (actionBars == null) {
                return null;
            }
            return actionBars.getStatusLineManager();
        }
        return null;
    }
}
