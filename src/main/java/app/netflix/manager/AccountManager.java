package app.netflix.manager;

import app.netflix.AppInfo;
import app.netflix.Main;
import app.netflix.model.Account;
import app.netflix.view.DiscordLoginView;
import balbucio.discordoauth.model.User;
import balbucio.sqlapi.model.ConditionValue;
import balbucio.sqlapi.model.Conditional;
import balbucio.sqlapi.model.Operator;
import balbucio.sqlapi.sqlite.HikariSQLiteInstance;
import de.jcm.discordgamesdk.user.DiscordUser;
import org.apache.commons.io.FileUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Base64;

public class AccountManager {

    private HikariSQLiteInstance sqlite = Main.sqlite;

    public AccountManager(String loggedId) {
        sqlite.createTable("account", "id VARCHAR(255) UNIQUE PRIMARY KEY, name VARCHAR(255), email VARCHAR(255), avatarHash TEXT");
        if (!loggedId.equalsIgnoreCase("null")) {
            AppInfo.LOGGED = true;
            AppInfo.ACCOUNT = getAccount(loggedId);
        }
    }

    public void openDiscordLogin() {
        DiscordLoginView discord = new DiscordLoginView();
        Main.window.getContent().add(discord, "LOGINDISCORD");
        Main.window.show("LOGINDISCORD");
    }

    public Account loadUser(User user) {
        String avatarHash = "";
        try {
            File avatarFile = new File("cache/"+user.getId());
            BufferedInputStream in = new BufferedInputStream(new URL("https://cdn.discordapp.com/avatars/" + user.getId() + "/" + user.getAvatar() + ".png").openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(avatarFile);
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            byte[] fileContent = FileUtils.readFileToByteArray(avatarFile);
            avatarHash = Base64.getEncoder().encodeToString(fileContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Account acc = new Account(user.getId(), user.getUsername(), user.getEmail(), avatarHash);
        sqlite.replace("id, name, email, avatarHash", "'" + acc.getId() + "', '" + acc.getUsername() + "','" + acc.getEmail() + "', '"+avatarHash+"'", "account");
        return acc;
    }

    public Account loadUser(DiscordUser user) {
        System.out.println(user.getAvatar());
        Account acc = new Account(String.valueOf(user.getUserId()), user.getUsername(), "rpc@email.com", user.getAvatar());
        sqlite.replace("id, name, email, avatarHash", "'" + acc.getId() + "', '" + acc.getUsername() + "','" + acc.getEmail() + "', ''", "account");
        return acc;
    }

    public Account getAccount(String id) {
        Account account = new Account();
        sqlite.getAllValuesFromColumns("account", new ConditionValue[]{new ConditionValue("id", Conditional.EQUALS, id, Operator.NULL)})
                .stream().findFirst().ifPresent(r -> {
                    account.setId(r.asString("id"));
                    account.setUsername(r.asString("name"));
                    account.setEmail(r.asString("email"));
                    account.setAvatar(r.asString("avatarHash"));
                });
        return account;
    }
}
