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

public class AccountManager {

    private HikariSQLiteInstance sqlite = Main.sqlite;

    public AccountManager(String loggedId){
        sqlite.createTable("account", "id VARCHAR(255) UNIQUE PRIMARY KEY, name VARCHAR(255), email VARCHAR(255)");
        if(!loggedId.equalsIgnoreCase("null")){
            AppInfo.LOGGED = true;
            AppInfo.ACCOUNT = getAccount(loggedId);
        }
    }

    public void openDiscordLogin(){
        DiscordLoginView discord = new DiscordLoginView();
        Main.window.getContent().add(discord, "LOGINDISCORD");
        Main.window.show("LOGINDISCORD");
        while (!discord.isFinished()){
        }
        System.out.println(discord.isFinished());
    }

    public Account loadUser(User user){
        Account acc = new Account(user.getId(), user.getUsername(), user.getEmail());
        sqlite.replace("id, name, email", "'"+acc.getId()+"', '"+acc.getUsername()+"','"+acc.getEmail()+"'", "account");
        return acc;
    }

    public Account loadUser(DiscordUser user){
        Account acc = new Account(String.valueOf(user.getUserId()), user.getUsername(), "rpc@email.com");
        sqlite.replace("id, name, email", "'"+acc.getId()+"', '"+acc.getUsername()+"','"+acc.getEmail()+"'", "account");
        return acc;
    }

    public Account getAccount(String id){
        Account account = new Account();
        sqlite.getAllValuesFromColumns("account", new ConditionValue[]{ new ConditionValue("id", Conditional.EQUALS, id, Operator.NULL)})
                .stream().findFirst().ifPresent(r -> {
                    account.setId(r.asString("id"));
                    account.setUsername(r.asString("name"));
                    account.setEmail(r.asString("email"));
                });
        return account;
    }
}
