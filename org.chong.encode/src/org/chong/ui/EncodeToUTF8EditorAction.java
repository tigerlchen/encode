package org.chong.ui;

import org.eclipse.ui.IEditorActionDelegate;

public class EncodeToUTF8EditorAction extends AbstractEncodeToGBKAction implements IEditorActionDelegate {

    public EncodeToUTF8EditorAction() {
        super("UTF-8");
    }
}