package com.maxhitalert;
import com.google.inject.Provides;
import java.io.OutputStream;
import java.net.URL;
import java.util.Objects;
import javax.net.ssl.HttpsURLConnection;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.HitsplatID;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.config.ConfigManager;

@Slf4j
@PluginDescriptor(
		name = "MaxHitPlugin"
)
public class MaxHitAlertPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private MaxHitAlertConfig config;

	@Override
	protected void startUp() throws Exception
	{
		log.info("MaxHitAlert started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("MaxHitAlert stopped!");
	}

	@Subscribe
	public void onHitsplatApplied(HitsplatApplied hitsplatApplied)

	{
		if(hitsplatApplied.getHitsplat().isMine()){
			if(hitsplatApplied.getHitsplat().getHitsplatType() == HitsplatID.DAMAGE_MAX_ME_ORANGE ||
					hitsplatApplied.getHitsplat().getHitsplatType() == HitsplatID.DAMAGE_ME_ORANGE ||
					hitsplatApplied.getHitsplat().getHitsplatType() == HitsplatID.DAMAGE_MAX_ME ||
					hitsplatApplied.getHitsplat().getHitsplatType() == HitsplatID.DAMAGE_ME

			){
				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Hit max detected!", null);

				String tokenWebhook = config.greeting();

				if(Objects.equals(config.greeting(), "null")){
					client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Tried sending discord notification but got invalid webhook url.", null);
				}
				else{

					String title = "Got a Max Hit!";
					String message = "Max Hit!";
					///////////////////////////////////////////////
					String jsonBrut = "";
					jsonBrut += "{\"embeds\": [{"
							+ "\"title\": \""+ title +"\","
							+ "\"description\": \""+ message +"\","
							+ "\"color\": 15258703"
							+ "}]}";
					try {
						URL url = new URL(tokenWebhook);
						HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
						con.addRequestProperty("Content-Type", "application/json");
						con.addRequestProperty("User-Agent", "Java-DiscordWebhook");
						con.setDoOutput(true);
						con.setRequestMethod("POST");
						OutputStream stream = con.getOutputStream();
						stream.write(jsonBrut.getBytes());
						stream.flush();
						stream.close();
						con.getInputStream().close();
						con.disconnect();
					} catch (Exception e) {
						client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Tried sending discord webhook but got error, please change webhook url", null);
					}

				}




			}
		}


	}
	@Provides
	MaxHitAlertConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MaxHitAlertConfig.class);
	}

}