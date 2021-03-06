/*
   JAdventure - A Java-based RPG
   Copyright (C) 2017  TehGuy

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package logic.quests;

import logic.core.World;

public class PlayerQuest {
    private int details;
    private boolean isCompleted;

    PlayerQuest(int quest){
        details = quest;
        isCompleted = false;
    }

    public Quest getDetails() {
        return World.QuestByID(details);
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    void setCompleted() {
        isCompleted = true;
        World.sendObserverNotification("plr_quest");
    }
}
