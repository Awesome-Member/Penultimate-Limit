package com.intellij.uiDesigner.core;

import java.lang.reflect.Method;
import javax.swing.JComponent;

public final class SupportCode {
   public static SupportCode$TextWithMnemonic parseText(String messageText) {
      if (messageText == null) {
         throw new IllegalArgumentException("textWithMnemonic cannot be null");
      } else {
         int length = -1;
         StringBuffer stringBuffer = new StringBuffer();

         for(int lengthIndex = 0; lengthIndex < messageText.length(); ++lengthIndex) {
            char character;
            if ((character = messageText.charAt(lengthIndex)) == '&') {
               ++lengthIndex;
               if (lengthIndex >= messageText.length()) {
                  break;
               }

               if ((character = messageText.charAt(lengthIndex)) != '&') {
                  length = stringBuffer.length();
               }
            }

            stringBuffer.append(character);
         }

         return new SupportCode$TextWithMnemonic(stringBuffer.toString(), length, (SupportCode$1)null);
      }
   }

   public static void setDisplayedMnemonicIndex(JComponent jComponent, int mnemonicIndex) {
      try {
         Method mnemonicSetter;
         (mnemonicSetter = jComponent.getClass().getMethod("setDisplayedMnemonicIndex", Integer.TYPE)).setAccessible(true);
         mnemonicSetter.invoke(jComponent, mnemonicIndex);
      } catch (Exception exception) {
      }
   }
}
