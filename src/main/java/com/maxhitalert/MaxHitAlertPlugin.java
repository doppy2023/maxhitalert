package com.maxhitalert;
import com.google.inject.Provides;
import java.io.IOException;
import java.util.Objects;
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
import okhttp3.*;

@Slf4j
@PluginDescriptor(
		name = "MaxHitPlugin"
)
public class MaxHitAlertPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private OkHttpClient okHttpClient;

	@Inject
	private MaxHitAlertConfig config;

	@Subscribe
	public void onHitsplatApplied(HitsplatApplied hitsplatApplied)

	{
		if(hitsplatApplied.getHitsplat().isMine()){
			if(hitsplatApplied.getHitsplat().getHitsplatType() == HitsplatID.DAMAGE_MAX_ME_ORANGE ||
					hitsplatApplied.getHitsplat().getHitsplatType() == HitsplatID.DAMAGE_ME_ORANGE ||
					hitsplatApplied.getHitsplat().getHitsplatType() == HitsplatID.DAMAGE_MAX_ME

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

					OkHttpClient client = new OkHttpClient();

					RequestBody formBody = new MultipartBody.Builder()
							.setType(MultipartBody.FORM)
							.addFormDataPart("payload_json", jsonBrut)
							.build();
					Request request = new Request.Builder()
							.url(tokenWebhook)
							.post(formBody)
							.addHeader("Content-type", "application/json")
							.build();

					try {
						Response response = client.newCall(request).execute();

						// Do something with the response.
					} catch (IOException e) {

						e.printStackTrace();

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