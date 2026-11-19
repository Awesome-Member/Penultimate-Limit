local WITCH_INDEX = 19
local NAME = "Печать 8ой ступени"

local Com        = java.class("Base.Com")
local JArray     = java.class("java.lang.reflect.Array")
local state      = java.get(java.class("Base.GameCommons"), "instance")
local Strateg    = java.get(java.class("Moduls.Strategist"), "instance")
local QStrateg   = java.get(java.class("Moduls.quests.QuestStrateg"), "instance")
local controller = java.get(java.class("Base.GameMapObjectsController"), "instance")
java.call(controller, "analisMapObject(IZ)Z", WITCH_INDEX, false)

local witch
local stage
local hook
hook = hooks.after("Base.Game", "functionate()V", function()
    local map = java.get(Com, "currentGameMap")
    if java.get(map, "nameId") ~= "City_Alchemist" then return end
    if not java.call(state, "isGameLoopAvailable()Z") then return end

    if not stage then
        stage = true
        print("City_Alchemist load")

        local towns = java.get(map, "towns")
        local count = java.call(JArray, "getLength(Ljava/lang/Object;)I", towns)
        for i = 0, count - 1 do
            local object = java.call(JArray, "get(Ljava/lang/Object;I)Ljava/lang/Object;", towns, i)
            if object and java.get(object, "typeIdName") == "Ведьма-со-шрамом" then
                witch = object
                --java.call(controller, "analisMapObject(IZ)Z", i, false)
                break
            end
        end
        if not witch then print("Witch not found") script.stop() end
        return
    end

    local qom = java.call(JArray, "get(Ljava/lang/Object;I)Ljava/lang/Object;", java.get(map, "questers"), java.get(witch, "typeInd"))
    local vec = java.call(qom, "getQuestsForTake(LModuls/Strategist;LModuls/quests/QuestStrateg;)Ljava/util/Vector;", Strateg, QStrateg)
    local quest
    for i = 0, java.call(vec, "size()I") - 1 do
        local q = java.call(vec, "elementAt(I)Ljava/lang/Object;", i)
        print("Available: " .. java.get(q, "name"))
        if java.get(q, "name") == NAME then quest = q break end
    end
    if not quest then print("Quest not found: '" .. NAME .. "'") script.stop() return end

    local nr = luajava.newInstance("Base.NetRequest")
    java.call(nr, "writeHeader(I)V", 4140920)
    java.call(quest, "saveToStreamId(Ljava/io/DataOutputStream;)V", java.get(nr, "request"))
    java.call(Com, "sendRequest(LBase/NetRequest;)Z", nr)
    local code = java.call(java.get(nr, "response"), "readInt()I")
    print("Server response: " .. code)
    if code ~= 4140921 then script.stop() return end

    local qip = luajava.newInstance("Moduls.quests.QuestInPlayer", java.get(nr, "response"))
    if java.call(java.get(nr, "response"), "readBoolean()Z") then
        print(java.call(java.get(nr, "response"), "readUTF()Ljava/lang/String;"))
        java.call(Com, "applyRewardEvents(LBase/NetRequest;)V", nr)
    end
     java.call(java.get(QStrateg, "quests"), "addElement(Ljava/lang/Object;)V", qip)
    java.call(QStrateg, "changeCompassedQuest(LModuls/quests/QuestInPlayer;)V", qip)
    java.call(Com, "onQuestsChanged(Z)V", true)
    java.set(java.get(java.class("bridge.movement.QuestFollower"), "INSTANCE"), "enabled", true)
    print("Quest accepted: " .. java.get(java.get(qip, "questSingle"), "name"))
    script.stop()
end)