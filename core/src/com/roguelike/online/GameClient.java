package com.roguelike.online;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.roguelike.game.Main;

public class GameClient {
	public Client client;

	public GameClient()
	{
		client = new Client(4194304*4, 1048576*8);
		Kryo kryo = client.getKryo();
	}

	public void listen()
	{
		client.addListener(new Listener()
		{
			public void received (Connection connection, Object object) 
			{
				Main.serverQueryEx.add(object);
			}
		});
	}
}
