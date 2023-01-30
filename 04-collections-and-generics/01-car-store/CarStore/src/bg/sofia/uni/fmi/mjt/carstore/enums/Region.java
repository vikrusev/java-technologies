package bg.sofia.uni.fmi.mjt.carstore.enums;

import java.util.HashMap;
import java.util.Map;

public enum Region {
    SOFIA("CB"), BURGAS("A"),
    VARNA("B"), PLOVDIV("PB"),
    RUSE("P"), GABROVO("EB"),
    VIDIN("BH"), VRATSA("BP");

    private final String prefix;

    private static Map<Region, Integer> regionNumber = new HashMap<Region, Integer>() {{
        for(Region region : Region.values()) {
            put(region, 999);
        }
    }};

    Region(String value) {
        this.prefix = value;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public int getCurrentRegionNumber() {
        regionNumber.put(this, regionNumber.get(this) + 1);
        return regionNumber.get(this);
    }
}
