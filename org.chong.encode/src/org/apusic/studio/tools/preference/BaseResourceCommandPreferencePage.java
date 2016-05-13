/*     */ package org.apusic.studio.tools.preference;
/*     */ 
/*     */ import java.util.HashMap;
import java.util.Map;

import org.chong.Activator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
/*     */ 
/*     */ public abstract class BaseResourceCommandPreferencePage extends PreferencePage
/*     */   implements IWorkbenchPreferencePage
/*     */ {
/*     */   protected TableViewer viewer;
/*  40 */   protected Map<String, String> values = new HashMap(5);
/*     */ 
/*     */   public void init(IWorkbench workbench) {
/*     */   }
/*     */ 
/*     */   @Override
protected Control createContents(Composite parent) {
/*  46 */     Composite mainPanel = new Composite(parent, 0);
/*  47 */     GridLayout gl = new GridLayout();
/*  48 */     gl.numColumns = 1;
/*  49 */     mainPanel.setLayout(gl);
/*     */ 
/*  51 */     Label lab = new Label(mainPanel, 64);
/*  52 */     lab.setText("Define different command in different OS. And you can use these variable: \n${file}: means the selected file or selected directory.\n${file.directory}: means the selected file's location.\n${file.driver}: means the selected file's driver, only valid in Windows.");
/*     */ 
/*  56 */     GridData gd = new GridData(768);
/*  57 */     lab.setLayoutData(gd);
/*     */ 
/*  59 */     loadDefaultValues();
/*     */ 
/*  61 */     Table table = new Table(mainPanel, 67586);
/*  62 */     gd = new GridData(1808);
/*  63 */     table.setLayoutData(gd);
/*  64 */     TableColumn column = new TableColumn(table, 0);
/*  65 */     column.setText("OS");
/*  66 */     column.setWidth(70);
/*  67 */     column = new TableColumn(table, 0);
/*  68 */     column.setText("Command");
/*  69 */     column.setWidth(300);
/*     */ 
/*  71 */     this.viewer = new TableViewer(table);
/*  72 */     table.setLinesVisible(true);
/*  73 */     table.setHeaderVisible(true);
/*  74 */     this.viewer.setContentProvider(new InnerTableContentProvider());
/*  75 */     this.viewer.setLabelProvider(new InnerTableLabelProvider());
/*  76 */     String[] columnProperties = { "OS", "Command" };
/*  77 */     this.viewer.setColumnProperties(columnProperties);
/*  78 */     CellEditor[] cellEditors = new CellEditor[2];
/*  79 */     cellEditors[0] = new TextCellEditor(table);
/*  80 */     cellEditors[1] = new TextCellEditor(table);
/*  81 */     this.viewer.setCellEditors(cellEditors);
/*  82 */     this.viewer.setInput(this.values);
/*  83 */     this.viewer.setCellModifier(new TableCellModifier());
/*  84 */     return mainPanel;
/*     */   }
/*     */ 
/*     */   @Override
public boolean performOk() {
/*  88 */     IPreferenceStore store = Activator.getDefault().getPreferenceStore();
/*  89 */     for (String key : this.values.keySet()) {
/*  90 */       store.putValue(getActionId() + "." + key, this.values.get(key));
/*     */     }
/*  92 */     return true;
/*     */   }
/*     */ 
/*     */   @Override
public boolean performCancel() {
/*  96 */     return super.performCancel();
/*     */   }
/*     */ 
/*     */   @Override
protected void performDefaults() {
/* 100 */     loadDefaultValues();
/* 101 */     this.viewer.refresh();
/*     */   }
/*     */ 
/*     */   private void loadDefaultValues() {
/* 105 */     IPreferenceStore store = Activator.getDefault().getPreferenceStore();
/* 106 */     this.values.put("win32", store.getString(getActionId() + "." + "win32"));
/* 107 */     this.values.put("carbon", store.getString(getActionId() + "." + "carbon"));
/* 108 */     this.values.put("motif", store.getString(getActionId() + "." + "motif"));
/* 109 */     this.values.put("photon", store.getString(getActionId() + "." + "photon"));
/* 110 */     this.values.put("gtk", store.getString(getActionId() + "." + "gtk"));
/*     */   }
/*     */   protected abstract String getActionId();
/*     */ 
/*     */   private class InnerTableContentProvider implements IStructuredContentProvider {
/*     */     private InnerTableContentProvider() {
/*     */     }
/*     */ 
/*     */     public Object[] getElements(Object inputElement) {
/* 119 */       return BaseResourceCommandPreferencePage.this.values.keySet().toArray();
/*     */     }
/*     */     public void dispose() {
/*     */     }
/*     */     public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
/*     */     }
/*     */   }
/*     */   private class InnerTableLabelProvider extends LabelProvider implements ITableLabelProvider {
/*     */     private InnerTableLabelProvider() {
/*     */     }
/* 129 */     public Image getColumnImage(Object element, int columnIndex) { return null; }
/*     */ 
/*     */     public String getColumnText(Object element, int columnIndex)
/*     */     {
/* 133 */       if (columnIndex == 0) return String.valueOf(element);
/* 134 */       return BaseResourceCommandPreferencePage.this.values.get(element);
/*     */     }
/*     */   }
/*     */   class TableCellModifier implements ICellModifier {
/*     */     TableCellModifier() {
/*     */     }
/* 140 */     public boolean canModify(Object element, String property) { if ("OS".equals(property)) {
/* 141 */         return false;
/*     */       }
/* 143 */       return true; }
/*     */ 
/*     */     public Object getValue(Object element, String property) {
/* 146 */       if ("Command".equals(property)) {
/* 147 */         return BaseResourceCommandPreferencePage.this.values.get(element);
/*     */       }
/* 149 */       return "UNKNOWN";
/*     */     }
/*     */     public void modify(Object element, String property, Object value) {
/* 152 */       if (("Command".equals(property)) && 
/* 153 */         ((element instanceof TableItem))) {
/* 154 */         Object data = ((TableItem)element).getData();
/* 155 */         BaseResourceCommandPreferencePage.this.values.put(String.valueOf(data), String.valueOf(value));
/* 156 */         BaseResourceCommandPreferencePage.this.viewer.refresh(data);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\chongpei.chencp\Desktop\org.apusic.studio.tools_5.1.5.jar
 * Qualified Name:     org.apusic.studio.tools.preference.BaseResourceCommandPreferencePage
 * JD-Core Version:    0.6.2
 */