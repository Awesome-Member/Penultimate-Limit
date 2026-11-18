package Base;

import java.io.DataInputStream;

public interface EventsStreamParser {
   IndirectNetworkEvent parseEvent(byte eventType, DataInputStream dataInputStream);
}
