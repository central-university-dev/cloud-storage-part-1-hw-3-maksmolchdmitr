package maks.molch.dmitr;

import maks.molch.dmitr.config.ServerConfig;
import maks.molch.dmitr.data.RequestData;
import maks.molch.dmitr.network.Network;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableConfigurationProperties(ServerConfig.class)
public class ClientApplication {
    private final Network network;

    public ClientApplication(Network network) {
        this.network = network;
    }

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Bean
    public void run() throws InterruptedException {
        Scanner in = new Scanner(System.in);
        while (!network.wasRunning() && !network.wasCrashed()) {
            System.out.println("Please wait... Client is trying to connect :)");
            TimeUnit.SECONDS.sleep(1);
        }
        while (network.isAlive()) {
            System.out.println("You want to try log in? Y/n");
            String input = in.nextLine();
            boolean continuing = (input.equals("Y") || input.equals("y"));
            if (!continuing) break;
            if (!network.isAlive()) {
                System.out.println("Client was disconnected!");
                break;
            }
            String username = ReadStr("Enter username > ", in);
            String password = ReadStr("Enter password > ", in);
            network.sendRequest(new RequestData(username, password));
            System.out.println("Server: " + network.waitServerResponse());
        }
        System.out.println("Buy!");
        network.close();
    }

    private static String ReadStr(String message, Scanner in) {
        System.out.print(message);
        return in.nextLine();
    }
}
