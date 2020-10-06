package my.test.myapplication.bean;


import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/5/8.
 */
public class SerMap  implements Serializable {
    public HashMap<String,String> map;
    public  SerMap(){

    }

    public HashMap<String, String> getMap() {
        return map;
    }

    public void setMap(HashMap<String, String> map) {
        this.map = map;

    }
}
