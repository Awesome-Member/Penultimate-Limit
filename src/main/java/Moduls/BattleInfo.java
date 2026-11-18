package Moduls;
import Base.Black;

import java.io.DataInputStream;
import java.io.IOException;

public final class BattleInfo {
   private String Name;
   private String Intro;
   public String WinAutro;
   private String LoseAutro;
   private int GenHead;
   private Black[] blackEntries;
   private Higher[] Highers;
   public String atackInfo;

   public final void LoadFromStream(DataInputStream dataInputStream) {
      try {
         this.Name = dataInputStream.readUTF();
         this.Intro = dataInputStream.readUTF();
         this.WinAutro = dataInputStream.readUTF();
         this.LoseAutro = dataInputStream.readUTF();
         this.GenHead = dataInputStream.readInt();
         this.blackEntries = new Black[0];
         byte byteCode = dataInputStream.readByte();
         this.Highers = new Higher[byteCode];

         for(int higherIndex = 0; higherIndex < byteCode; ++higherIndex) {
            this.Highers[higherIndex] = new Higher();
            this.Highers[higherIndex].LoadFromStream(dataInputStream);
         }

         this.atackInfo = dataInputStream.readUTF();
      } catch (IOException exception) {
      }
   }
}
