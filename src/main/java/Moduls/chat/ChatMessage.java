package Moduls.chat;

import Base.Com;
import Engine.MystString;
import Engine.MystText;
import Engine.Mystery;
import java.io.DataInputStream;

public final class ChatMessage {
   public long creationTime;
   public long time;
   public int playerId;
   public String playerName;
   public int playerRace;
   public int playerGender;
   public int playerLevel;
   public String message;
   public int channelType;
   public int channelId1;
   public int channelId2;
   public MystText headText;

   public ChatMessage(DataInputStream dataInputStream) {
      this.creationTime = Mystery.currentTimeMillis;
      this.time = decompiledsupport.UncheckedDataInput.readLong(dataInputStream);
      this.playerId = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.playerName = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.playerRace = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      this.playerGender = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      this.playerLevel = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.message = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.channelType = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.channelId1 = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.channelId2 = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      MystString mystString;
      (mystString = new MystString()).Create(this.message, Com.fontSmall);
      this.headText = new MystText();
      this.headText.create(mystString, 110, 2, '\u0000', 1, Com.fontSmall);
   }

   public final int getPrivateOponentId(int forPlayerId) {
      return this.channelId1 == forPlayerId ? this.channelId2 : this.channelId1;
   }
}
