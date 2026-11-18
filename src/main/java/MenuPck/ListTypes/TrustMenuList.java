package MenuPck.ListTypes;

import Base.NetRequest;
import Base.Com;
import MenuPck.MenuList;
import java.util.Vector;

public final class TrustMenuList extends MenuList {
   private static String _hlp = "";

   public TrustMenuList(int id, int backMenu, String[] list) {
      super(43, 36, (String[])list);
   }

   protected final void onGenerateHintEvent(int menuId, int itemIndex) {
      setAnimatedTitle(_hlp);
   }

   protected final Vector onGenerateItemsAndSkipsEvent(int menuId) {
      Vector vector3 = super.onGenerateItemsAndSkipsEvent(menuId);
      Com.PrBarTh.Set(true);
      NetRequest netRequest = new NetRequest();
      _hlp = "Произошла ошибка!";

      try {
         netRequest.writeHeader(4200000);
         Com.sendRequest(netRequest);
         if (netRequest.response.readInt() == 4200001) {
            _hlp = netRequest.response.readUTF();
            vector3.addElement("Подопечные:");
            int integerCode = netRequest.response.readInt();

            for(int integerCodeIndex = 0; integerCodeIndex < integerCode; ++integerCodeIndex) {
               vector3.addElement(netRequest.response.readUTF());
            }
         }
      } catch (Exception exception) {
         exception.printStackTrace();
      }

      Com.PrBarTh.Stop();
      setAnimatedTitle(_hlp);
      return vector3;
   }
}
