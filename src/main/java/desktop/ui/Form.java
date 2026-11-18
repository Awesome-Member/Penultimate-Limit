package desktop.ui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

public class Form extends Displayable {
   private CommandListener listener = null;
   private JPanel jPanel = new JPanel();
   private JPanel jPanel2;
   protected JPanel jPanel3;
   private JLabel jLabel;
   private Spacer spacer = new Spacer();
   private JScrollPane jScrollPane;
   private boolean focusNavigationEnabled = true;
   private LinkedList linkedList = new LinkedList();

   public Form(String messageText) {
      this.jPanel.addFocusListener(new FormFocusListener(this));
      this.jPanel.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
      this.jScrollPane = new JScrollPane();
      this.jScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
      this.jScrollPane.setHorizontalScrollBarPolicy(31);
      this.jPanel.add(this.jScrollPane, new GridConstraints(1, 1, 1, 1, 0, 3, 5, 5, (Dimension)null, new Dimension(154, 249), (Dimension)null, 0, false));
      this.jPanel3 = new JPanel();
      this.jPanel3.setLayout(new GridLayoutManager(100, 1, new Insets(0, 0, 0, 0), -1, -1));
      this.jScrollPane.setViewportView(this.jPanel3);
      JPanel jPanel4;
      (jPanel4 = new JPanel()).setLayout(new FlowLayout(2, 5, 5));
      this.jPanel.add(jPanel4, new GridConstraints(0, 1, 1, 1, 0, 3, 3, 3, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
      this.jLabel = new JLabel();
      this.jLabel.setFont(new Font(this.jLabel.getFont().getName(), 1, 14));
      this.jLabel.setText(messageText);
      this.jPanel2 = jPanel4;
      JPanel jPanel5;
      (jPanel5 = new JPanel()).setLayout(new BorderLayout(0, 0));
      this.jPanel.add(jPanel5, new GridConstraints(1, 0, 1, 1, 0, 3, 0, 3, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
      (jPanel5 = new JPanel()).setLayout(new BorderLayout(0, 0));
      this.jPanel.add(jPanel5, new GridConstraints(1, 2, 1, 1, 0, 3, 0, 3, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
      this.jPanel.setFocusable(true);
      FormPreviousFocusAction formPreviousFocusAction = new FormPreviousFocusAction(this);
      this.jPanel.getInputMap().put(KeyStroke.getKeyStroke(10, 0), "doNothing1");
      this.jPanel.getActionMap().put("doNothing1", formPreviousFocusAction);
      FormNextFocusAction formNextFocusAction = new FormNextFocusAction(this);
      this.jPanel.getInputMap(2).put(KeyStroke.getKeyStroke(27, 0), "doNothing2");
      this.jPanel.getActionMap().put("doNothing2", formNextFocusAction);
      this.jScrollPane.getVerticalScrollBar().addAdjustmentListener(new FormScrollListener(this));
   }

   protected final void handleKeyCommand(String messageText) {
      Component component2;
      int listenerCount;
      int actionListenerIndex;
      ActionListener[] actionListeners;
      if (messageText.equals("ENTER")) {
         if ((component2 = this.jPanel2.getComponent(0)) == null) {
            return;
         }

         if (component2 instanceof JButton) {
            listenerCount = (actionListeners = ((JButton)component2).getActionListeners()).length;

            for(actionListenerIndex = 0; actionListenerIndex < listenerCount; ++actionListenerIndex) {
               actionListeners[actionListenerIndex].actionPerformed((ActionEvent)null);
            }
         }
      }

      if (messageText.equals("ESCAPE")) {
         if ((component2 = this.jPanel2.getComponent(1)) == null) {
            return;
         }

         if (component2 instanceof JButton) {
            listenerCount = (actionListeners = ((JButton)component2).getActionListeners()).length;

            for(actionListenerIndex = 0; actionListenerIndex < listenerCount; ++actionListenerIndex) {
               actionListeners[actionListenerIndex].actionPerformed((ActionEvent)null);
            }
         }
      }

   }

   protected final void setTitle(String messageText) {
      this.jLabel.setText(messageText);
   }

   public final Component getComponent() {
      return this.jPanel;
   }

   public final void appendItem(FormItem formItem) {
      this.jPanel3.remove(this.spacer);
      this.linkedList.add(formItem);
      formItem.setForm(this);
      this.jPanel3.add(formItem.getComponent(), new GridConstraints(this.jPanel3.getComponentCount(), 0, 1, 1, 1, 1, 2, 1, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
      this.jPanel3.add(this.spacer, new GridConstraints(this.jPanel3.getComponentCount(), 0, 1, 1, 0, 2, 1, 4, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
   }

   public final void addCommand(CommandItem commandItem2) {
      this.jPanel2.add(commandItem2.getComponent());
      ((JButton)commandItem2.getComponent()).addActionListener(new CommandActionListener(this, commandItem2));
   }

   public final void setCommandListener(CommandListener commandListener) {
      this.listener = commandListener;
   }


   static LinkedList getFormItems(Form form2) {
      return form2.linkedList;
   }


   static boolean setFocusNavigationEnabled(Form form2, boolean focusNavigationEnabled2) {
      return form2.focusNavigationEnabled = focusNavigationEnabled2;
   }


   static boolean isFocusNavigationEnabled(Form form2) {
      return form2.focusNavigationEnabled;
   }


   static JScrollPane getScrollPane(Form form2) {
      return form2.jScrollPane;
   }


   static CommandListener getCommandListener(Form form2) {
      return form2.listener;
   }
}
