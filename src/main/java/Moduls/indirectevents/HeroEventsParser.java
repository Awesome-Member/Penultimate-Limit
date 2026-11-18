
package Moduls.indirectevents;

import Base.IndirectNetworkEvent;
import Base.EventsStreamParser;
import Moduls.indirectevents.HeroEventBanDevice;
import Moduls.indirectevents.HeroEventChatCounting;
import Moduls.indirectevents.HeroEventChatMessagesPack;
import Moduls.indirectevents.HeroEventChatUnreadnPrivates;
import Moduls.indirectevents.HeroEventFloatMessage;
import Moduls.indirectevents.HeroEventMenuMessage;
import Moduls.indirectevents.HeroEventNewLevel;
import Moduls.indirectevents.HeroEventPartyInfo;
import Moduls.indirectevents.HeroEventPartyInvite;
import Moduls.indirectevents.HeroEventPartyKillMobs;
import Moduls.indirectevents.HeroEventPartyState;
import Moduls.indirectevents.HeroEventPartyUseTowns;
import Moduls.indirectevents.HeroEventPrerespawnEnemy;
import Moduls.indirectevents.HeroEventRespawnEnemy;
import Moduls.indirectevents.HeroEventToSold;
import Moduls.indirectevents.WorldHeroEventCreateSkeletor;
import battlepck.calls.HeroEventAddBattleCall;
import battlepck.calls.HeroEventRemoveBattleCall;
import java.io.DataInputStream;

public final class HeroEventsParser
implements EventsStreamParser {
    @Override
    public final IndirectNetworkEvent parseEvent(byte eventType, DataInputStream dataInputStream) {
        switch (eventType) {
            case -125: {
                int integerCode = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
                eventType = (byte)integerCode;
                switch (integerCode) {
                    case 1301: {
                        return new HeroEventRemoveBattleCall();
                    }
                    case 1302: {
                        return new HeroEventAddBattleCall();
                    }
                    case 1200: {
                        return new WorldHeroEventCreateSkeletor();
                    }
                    case 1102: {
                        return new HeroEventChatUnreadnPrivates();
                    }
                    case 1101: {
                        return new HeroEventChatCounting();
                    }
                    case 1100: {
                        return new HeroEventChatMessagesPack();
                    }
                    case 1012: {
                        return new HeroEventPartyInfo();
                    }
                    case 1011: {
                        return new HeroEventBanDevice();
                    }
                    case 1010: {
                        return new HeroEventMenuMessage();
                    }
                    case 1009: {
                        return new HeroEventPartyInvite();
                    }
                    case 1008: {
                        return new HeroEventPartyUseTowns();
                    }
                    case 1007: {
                        return new HeroEventPartyKillMobs();
                    }
                    case 1006: {
                        return new HeroEventPartyState();
                    }
                    case 1005: {
                        return new HeroEventRespawnEnemy();
                    }
                    case 1004: {
                        return new HeroEventPrerespawnEnemy();
                    }
                    case 1002: {
                        return new HeroEventFloatMessage();
                    }
                    case 1000: {
                        return new HeroEventNewLevel();
                    }
                    case 1001: {
                        return new HeroEventToSold();
                    }
                }
                System.out.println("IndirectEventsParser unknown indirect event type " + eventType);
                return null;
            }
        }
        return null;
    }
}

