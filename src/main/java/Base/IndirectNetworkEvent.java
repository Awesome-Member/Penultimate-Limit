package Base;

import java.io.DataInputStream;

public interface IndirectNetworkEvent {
   void loadFromStream(DataInputStream dataInputStream);

   void apply();

   void afterApply();

   int getApplyOrder();
}
