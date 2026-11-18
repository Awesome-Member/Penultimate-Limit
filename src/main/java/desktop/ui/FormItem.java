package desktop.ui;

import java.awt.Component;

public abstract class FormItem {
   protected Form form;

   public final void setForm(Form form2) {
      this.form = form2;
   }

   public abstract Component getComponent();

   public abstract void requestFocus();
}
