package nativelevel.conversations;


import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.Prompt;
import org.bukkit.plugin.Plugin;

public class ConversationTeste extends Conversation {

    public ConversationTeste(Plugin plugin, Conversable forWhom, Prompt firstPrompt) {
        super(plugin, forWhom, firstPrompt);
    }
}
