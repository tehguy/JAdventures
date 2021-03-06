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

package logic.core;

import logic.core.file.LoadSystem;
import logic.core.file.SQLiteJDBCDriverConnection;
import logic.core.file.SaveSystem;
import logic.entity.Monster;
import logic.entity.Player;
import logic.item.Item;
import logic.observer.GameObserver;
import logic.observer.MessageObserver;
import logic.quests.Quest;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class World {
    private static Player _player;
    private static Monster _currentMonster;

    private static List<GameObserver> observerList = new ArrayList<>();
    private static List<MessageObserver> messageObservers = new ArrayList<>();

    private World() {

    }

    public static void init() throws UnsupportedEncodingException {
        SaveSystem.init();
        LoadSystem.init();
        SQLiteJDBCDriverConnection.connect();
    }

    public static void finishInit(){
        sendObserverNotification("plr_gold");
        sendObserverNotification("plr_exp");
        sendObserverNotification("plr_curhp");
        sendObserverNotification("plr_lvl");

        LocationLogic.moveToPlayerHome();
        _player.addItemToInventory(World.ItemByID(0));
    }

    private static void ensureObserverListCreation() {
        if (observerList == null) {
            observerList = new ArrayList<>();
        }

        if (messageObservers == null) {
            messageObservers = new ArrayList<>();
        }
    }

    static Monster getCurrentMonster() {
        return _currentMonster;
    }

    static void setCurrentMonster(Monster monster) {
        _currentMonster = monster;
    }

    public static void addObserverToList(GameObserver observer) {
        ensureObserverListCreation();
        observerList.add(observer);
    }

    public static void addMessengerObserver(MessageObserver observer) {
        ensureObserverListCreation();
        messageObservers.add(observer);
    }

    public static void removeObserverFromList(GameObserver observer) {
        ensureObserverListCreation();
        observerList.remove(observer);
    }

    public static void removeMessengerObserver(MessageObserver observer) {
        ensureObserverListCreation();
        messageObservers.remove(observer);
    }

    public static void sendObserverNotification(String message) {
        for (GameObserver observer : observerList) {
            observer.sendNotification(message);
        }
    }

    public static void sendMessengerObserverNotification(String type, String message) {
        for (MessageObserver mo : messageObservers) {
            mo.sendMessage(type, message);
        }
    }

    public static Player getPlayer() {
        return _player;
    }

    public static void setPlayer(Player player){
        _player = player;
    }

    public static void loadPlayer(){
        _player = LoadSystem.loadPlayer();
    }

    public static Item ItemByID(Integer id) {
        if(id != null){
            return LoadSystem.loadItem(id);
        }

        return null;
    }

    static Monster MonsterByID(Integer id) {
        if(id != null){
            return LoadSystem.loadMonster(id);
        }

        return null;
    }

    public static Quest QuestByID(Integer id) {
        if(id != null){
            return LoadSystem.loadQuest(id);
        }

        return null;
    }

    public static Location LocationByID(Integer id) {
        if(id != null){
            return LoadSystem.loadLocation(id);
        }

        return null;
    }
}
