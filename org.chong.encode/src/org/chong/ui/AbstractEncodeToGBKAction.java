package org.chong.ui;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.chong.Activator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModelStatusConstants;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.compiler.util.SuffixConstants;
import org.eclipse.jdt.internal.core.ClassFile;
import org.eclipse.jdt.internal.core.JavaModelManager;
import org.eclipse.jdt.internal.core.JavaModelStatus;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jdt.internal.core.PackageFragmentRoot;
import org.eclipse.jdt.internal.core.util.Util;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.ui.javaeditor.ClassFileDocumentProvider;
import org.eclipse.jdt.internal.ui.javaeditor.ClassFileEditor;
import org.eclipse.jdt.internal.ui.javaeditor.IClassFileEditorInput;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.IDocumentProvider;

public abstract class AbstractEncodeToGBKAction implements IActionDelegate, IEditorActionDelegate{

    
    private String encoding;
    
    private ClassFileEditor classFileEditor;
    
    protected AbstractEncodeToGBKAction(String encoding) {
        this.encoding = encoding;
    }

    public final void selectionChanged(IAction action, ISelection selection) {
    }

    @SuppressWarnings("restriction")
    public final void setActiveEditor(IAction action, IEditorPart targetEditor) {
        if ((targetEditor instanceof ClassFileEditor)) {
            this.classFileEditor = ((ClassFileEditor) targetEditor);
        } else {
            this.classFileEditor = null;
        }
    }
    
    public final void run(IAction action) {
        runHandler();
    }
    
    public void runHandler() {
        IDocument document = getDocument(getProvider(), getEditorInput());
        String str = document.get();
        
        try {
            String sourceStr = new String(getBytes(), encoding);
            document.replace(0,str.length() -1 , sourceStr);
            classFileEditor.doSave(new NullProgressMonitor());
        } catch (Exception e) {
            Activator.log(e);
        }
    }
    
    public byte[] getBytes() throws JavaModelException {
        IPath sourceAttachmentPath = getSourceAttachmentPath();
        IJavaElement pkg = getClassFile().getParent();
        if (pkg instanceof PackageFragment) {
            ZipFile zip = null;
            try {
                zip = JavaModelManager.getJavaModelManager().getZipFile(sourceAttachmentPath)/*root.getJar()*/;
                String entryName = Util.concatWith(((PackageFragment) pkg).names, getClassFile().getElementName(), '/');
                entryName = entryName.substring(0, entryName.lastIndexOf(SuffixConstants.SUFFIX_STRING_class) + 1) + "java";
                ZipEntry ze = zip.getEntry(entryName);
                if (ze != null) {
                    return org.eclipse.jdt.internal.compiler.util.Util.getZipEntryByteContent(ze, zip);
                }
                throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, getClassFile()));
            } catch (IOException ioe) {
                throw new JavaModelException(ioe, IJavaModelStatusConstants.IO_EXCEPTION);
            } catch (CoreException e) {
                if (e instanceof JavaModelException) {
                    throw (JavaModelException)e;
                } else {
                    throw new JavaModelException(e);
                }
            } finally {
                JavaModelManager.getJavaModelManager().closeZipFile(zip);
            }
        } else {
            IFile file = (IFile) ((ClassFile)getClassFile()).resource();
            return Util.getResourceContentsAsByteArray(file);
        }
    }
    
    private ClassFileDocumentProvider getProvider() {
        if (classFileEditor.getDocumentProvider() instanceof ClassFileDocumentProvider) {
            ClassFileDocumentProvider classFileDocumentProvider = (ClassFileDocumentProvider) classFileEditor.getDocumentProvider();
            return classFileDocumentProvider;
        }
        return null;
    }
    
    private IPath getSourceAttachmentPath() throws JavaModelException {
        IClassFile classFile = getClassFile();
        PackageFragmentRoot fRoot  = (PackageFragmentRoot) classFile.getAncestor(IJavaElement.PACKAGE_FRAGMENT_ROOT);
        
        IJavaProject jproject= fRoot.getJavaProject();
        IClasspathEntry entry= JavaModelUtil.getClasspathEntry(fRoot);
        
        if (entry.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {entry.getSourceAttachmentPath();
            IPath containerPath= entry.getPath();
            IClasspathContainer container= JavaCore.getClasspathContainer(containerPath, jproject);
            entry= JavaModelUtil.findEntryInContainer(container, fRoot.getPath());
            return entry.getSourceAttachmentPath();
        } else if (entry.getEntryKind() == IClasspathEntry.CPE_VARIABLE) {
            return JavaCore.getResolvedVariablePath(entry.getSourceAttachmentPath());
        }
        
        return null;
    }
    
    private IClassFileEditorInput getEditorInput() {
        return classFileEditor.getEditorInput() instanceof IClassFileEditorInput ? (IClassFileEditorInput) classFileEditor.getEditorInput() : null;
    }
    
    private IClassFile getClassFile() {
        IClassFileEditorInput classFileEditorInput = getEditorInput();
        return classFileEditorInput.getClassFile();
    }
    
    private IDocument getDocument(IDocumentProvider provider, IEditorInput input) {
        if (input == null)
            return null;
        IDocument result= null;
        try {
            provider.connect(input);
            result= provider.getDocument(input);
        } catch (CoreException e) {
        } finally {
            provider.disconnect(input);
        }
        return result;
    }

    public ClassFileEditor getClassFileEditor() {
        return classFileEditor;
    }

    public void setClassFileEditor(ClassFileEditor classFileEditor) {
        this.classFileEditor = classFileEditor;
    }

}
