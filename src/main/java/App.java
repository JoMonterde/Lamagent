import api.OllamaClient;
import agent.ChatAgent;
import ui.ConsoleUI;

import java.nio.file.*;

public class App {
    public static void main(String[] args) throws Exception {

        OllamaClient ollama = new OllamaClient("http://localhost:11434");
        String model = "llama3:latest";

        ChatAgent agent = new ChatAgent(ollama, model);
        ConsoleUI ui = new ConsoleUI(agent);
        ui.run();
    }
}
