package leesd.crossithackathon.data;


import java.util.HashMap;
import java.util.Map;

import leesd.crossithackathon.R;

/**
 * Created by cmtyx on 2017-08-05.
 */

public class MapList {
    public static HashMap<String,Integer> mapList = new HashMap<>();

    public static void mapInit(){
        mapList.put("국방부", R.string.국방부);
        mapList.put("관세청",R.string.관세청);
    }
}
