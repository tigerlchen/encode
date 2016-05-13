/*$Id: $
*--------------------------------------
* Apusic (Kingdee Middleware)
*--------------------------------------
* Copyright By Apusic ,All right Reserved
* author date comment
* Administrator 2013-5-5 Created
*/

package org.chong.ui;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.internal.ui.javaeditor.ClassFileEditor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class GBKEncodeActionHandler extends AbstractHandler {
    public Object execute(ExecutionEvent event) throws ExecutionException {
        final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
        IWorkbenchPage activePage = window.getActivePage();
        ClassFileEditor javaEditor = JavaWorkspaceUtilities.getActiveClassFileEditor(activePage);

        EncodeToGBKEditorAction action = new EncodeToGBKEditorAction();
        if (javaEditor != null) {
            action = new EncodeToGBKEditorAction();
            action.setClassFileEditor(javaEditor);
            action.runHandler();
        }
        
        return action;
    }
}
