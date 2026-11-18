package MenuPck;

import Moduls.StrategGraphic;
import java.io.DataInputStream;

public final class MenuPlayerDescription {
   public String name;
   public String aboutMe;
   public int level;
   private boolean inClan = false;
   public int id = -1;
   public String time;
   public String logId;
   public boolean withNew;
   public byte race;
   public byte gender;
   public StrategGraphic strategGraphic;

   public MenuPlayerDescription(String name2, String aboutMe2, int level2, StrategGraphic strategGraphic2) {
      this.name = name2;
      this.aboutMe = aboutMe2;
      this.level = level2;
      this.strategGraphic = strategGraphic2;
   }

   public MenuPlayerDescription(DataInputStream dataInputStream) {
      this.name = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.aboutMe = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.level = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.strategGraphic = new StrategGraphic(dataInputStream);
      this.id = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
   }

   public final String toLevelNameText() {
      return "(" + this.level + ")" + this.name;
   }
}
