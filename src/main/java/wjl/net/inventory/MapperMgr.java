package wjl.net.inventory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapperMgr {
    private static Map<String, DeviceMapper> allDevices = new ConcurrentHashMap<>();
    private static Map<String, LinkMapper> allLinks = new ConcurrentHashMap<>();

    public static void addDeviceMapper(DeviceMapper mapper) {
        allDevices.put(mapper.getDevId(), mapper);
    }

    public static DeviceMapper getDeviceMapper(String devId) {
        return allDevices.get(devId);
    }

    public static void addLinkMapper(LinkMapper mapper) {
        allLinks.put(mapper.getLinkId(), mapper);
    }

    public static LinkMapper getLinkMapper(String linkId) {
        return allLinks.get(linkId);
    }
}
