/*    */ package org.apusic.studio.tools;
/*    */ 
/*    */ import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.osgi.service.prefs.Preferences;
/*    */ 
/*    */ public class PreferenceInitializer extends AbstractPreferenceInitializer
/*    */ {
/*    */   public static final String OS_WIN32 = "win32";
/*    */   public static final String OS_MOTIF = "motif";
/*    */   public static final String OS_GTK = "gtk";
/*    */   public static final String OS_CARBON = "carbon";
/*    */   public static final String OS_PHOTON = "photon";
/*    */ 
/*    */   @Override
public void initializeDefaultPreferences()
/*    */   {
/* 17 */     Preferences node = new DefaultScope().getNode("org.chong.encode");
/* 18 */     node.put("org.apusic.studio.tools.action.ShowInExplorerAction.win32", "explorer /select, ${file}");
/* 19 */     node.put("org.apusic.studio.tools.action.ShowInExplorerAction.motif", "");
/* 20 */     node.put("org.apusic.studio.tools.action.ShowInExplorerAction.gtk", "nautilus ${file.directory}");
/* 21 */     node.put("org.apusic.studio.tools.action.ShowInExplorerAction.carbon", "");
/* 22 */     node.put("org.apusic.studio.tools.action.ShowInExplorerAction.photon", "");
/*    */ 
/* 24 */     node.put("org.apusic.tools.action.OpenTerminalAction.win32", "cmd.exe /c start cmd /k \"cd ${file.directory}&&${file.driver}\"");
/* 25 */     node.put("org.apusic.tools.action.OpenTerminalAction.motif", "");
/* 26 */     node.put("org.apusic.tools.action.OpenTerminalAction.gtk", "gnome-terminal --working-directory=${file.directory}");
/* 27 */     node.put("org.apusic.tools.action.OpenTerminalAction.carbon", "");
/* 28 */     node.put("org.apusic.tools.action.OpenTerminalAction.photon", "");
/*    */   }
/*    */ }

/* Location:           C:\Users\chongpei.chencp\Desktop\org.apusic.studio.tools_5.1.5.jar
 * Qualified Name:     org.apusic.studio.tools.PreferenceInitializer
 * JD-Core Version:    0.6.2
 */