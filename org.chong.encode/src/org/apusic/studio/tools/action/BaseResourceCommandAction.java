package org.apusic.studio.tools.action;
/*     */ 
/*     */ import org.chong.Activator;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
/*     */ 
/*     */ public abstract class BaseResourceCommandAction
/*     */   implements IObjectActionDelegate
/*     */ {
/*     */   protected ISelection selection;
/*  35 */   protected String os = SWT.getPlatform().toLowerCase();
/*     */ 
/*     */   public void setActivePart(IAction action, IWorkbenchPart targetPart)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void run(IAction action)
/*     */   {
/*  55 */     if ((this.selection == null) || (this.selection.isEmpty()) || (!(this.selection instanceof IStructuredSelection))) {
/*  56 */       return;
/*     */     }
/*  58 */     IStructuredSelection ss = (IStructuredSelection)this.selection;
/*  59 */     Object obj = ss.getFirstElement();
/*  60 */     if ((obj == null) || (!(obj instanceof IAdaptable))) {
/*  61 */       return;
/*     */     }
/*  63 */     IAdaptable adapter = (IAdaptable)obj;
/*  64 */     Object resource = adapter.getAdapter(IResource.class);
/*  65 */     if (resource == null) {
/*  66 */       System.out.println("INVALID:" + obj.getClass());
/*  67 */       return;
/*     */     }
/*  69 */     String command = parseCommand(getCommandDefinition(), (IResource)resource);
/*  70 */     if ((command == null) || ("".equals(command.trim())))
/*  71 */       MessageDialog.openInformation(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), 
/*  72 */         "Command not defined", "This command is not defined in this window system(" + this.os + "), you should define it through preference page");
/*     */     else
/*     */       try
/*     */       {
/*  76 */         Runtime.getRuntime().exec(command);
/*     */       } catch (Exception e) {
/*  78 */         e.printStackTrace();
/*  79 */         log("Exec command '" + command + "' error", e);
/*  80 */         MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), 
/*  81 */           "Command is invalid", "Command '" + command + "' is invalid.\n" + 
/*  82 */           "You can correct it through preference page(Apusic Tools).\n\n" + 
/*  83 */           e.getMessage());
/*     */       }
/*     */   }
/*     */ 
/*     */   protected abstract String getActionId();
/*     */ 
/*     */   protected String parseCommand(String commandDefinition, IResource resource)
/*     */   {
/*  91 */     if (commandDefinition != null) {
/*  92 */       commandDefinition = commandDefinition.replace("${file}", getFile(resource));
/*  93 */       commandDefinition = commandDefinition.replace("${file.directory}", getFileDirectory(resource));
/*  94 */       commandDefinition = commandDefinition.replace("${file.driver}", getFileDriver(resource));
/*     */     }
/*  96 */     return commandDefinition;
/*     */   }
/*     */ 
/*     */   protected String getFile(IResource resource) {
/* 100 */     if (resource != null)
/* 101 */       return resource.getLocation().toOSString();
/* 102 */     return "";
/*     */   }
/*     */ 
/*     */   protected String getFileDirectory(IResource resource) {
/* 106 */     if (resource != null) {
/* 107 */       if (resource.getType() == 1) {
/* 108 */         resource = resource.getParent();
/*     */       }
/* 110 */       return resource.getLocation().toOSString();
/*     */     }
/* 112 */     return "";
/*     */   }
/*     */ 
/*     */   protected String getFileDriver(IResource resource) {
/* 116 */     if ((resource != null) && 
/* 117 */       ("win32".equals(this.os))) {
/* 118 */       String str = resource.getLocation().toOSString();
/* 119 */       int index = str.indexOf(':');
/* 120 */       if (index != -1) {
/* 121 */         return str.substring(0, index + 1);
/*     */       }
/*     */     }
/* 124 */     return "";
/*     */   }
/*     */ 
/*     */   protected String getCommandDefinition() {
/* 128 */     IPreferenceStore store = Activator.getDefault().getPreferenceStore();
/* 129 */     return store.getString(getActionId() + "." + this.os);
/*     */   }
/*     */ 
/*     */   protected void log(String message, Throwable e) {
/* 133 */     Activator.getDefault().getLog().log(new Status(4, "org.apusic.studio.tools", -1, message, e));
/*     */   }
/*     */ 
/*     */   public void selectionChanged(IAction action, ISelection selection)
/*     */   {
/* 139 */     this.selection = selection;
/*     */   }
/*     */ }

/* Location:           C:\Users\chongpei.chencp\Desktop\org.apusic.studio.tools_5.1.5.jar
 * Qualified Name:     org.apusic.studio.tools.action.BaseResourceCommandAction
 * JD-Core Version:    0.6.2
 */