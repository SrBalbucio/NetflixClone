package app.netflix.model;

import balbucio.org.ejsl.utils.ImageUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.io.File;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private String id;
    private String username;
    private String email;
    private String avatar;

    public Image getImage(){
        System.out.println(id);
        return ImageUtils.getImage(new File("cache/"+id+".png"));
    }

}
