/*    */ package org.apusic.studio.tools.preference;
/*    */ 
/*    */ import org.eclipse.jface.preference.PreferencePage;
/*    */ import org.eclipse.swt.widgets.Composite;
/*    */ import org.eclipse.swt.widgets.Control;
/*    */ import org.eclipse.ui.IWorkbench;
/*    */ import org.eclipse.ui.IWorkbenchPreferencePage;
/*    */ 
/*    */ public class ToolsPreferencePage extends PreferencePage
/*    */   implements IWorkbenchPreferencePage
/*    */ {
/*    */   protected Control createContents(Composite parent)
/*    */   {
/* 13 */     Composite mainPanel = new Composite(parent, 0);
/* 14 */     return mainPanel;
/*    */   }
/*    */ 
/*    */   public void init(IWorkbench workbench)
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Users\chongpei.chencp\Desktop\org.apusic.studio.tools_5.1.5.jar
 * Qualified Name:     org.apusic.studio.tools.preference.ToolsPreferencePage
 * JD-Core Version:    0.6.2
 */