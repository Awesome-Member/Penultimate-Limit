package com.intellij.uiDesigner.core;

public final class SupportCode$TextWithMnemonic {
   public final String myText;
   public final int myMnemonicIndex;

   private SupportCode$TextWithMnemonic(String labelText, int mnemonicIndex) {
      if (labelText == null) {
         throw new IllegalArgumentException("text cannot be null");
      } else if (mnemonicIndex == -1 || mnemonicIndex >= 0 && mnemonicIndex < labelText.length()) {
         this.myText = labelText;
         this.myMnemonicIndex = mnemonicIndex;
      } else {
         throw new IllegalArgumentException("wrong index: " + mnemonicIndex + "; text = '" + labelText + "'");
      }
   }

   public final char getMnemonicChar() {
      if (this.myMnemonicIndex == -1) {
         throw new IllegalStateException("text doesn't contain mnemonic");
      } else {
         return Character.toUpperCase(this.myText.charAt(this.myMnemonicIndex));
      }
   }

   SupportCode$TextWithMnemonic(String messageText, int mnemonicIndex, SupportCode$1 unusedAccessMarker) {
      this(messageText, mnemonicIndex);
   }
}
