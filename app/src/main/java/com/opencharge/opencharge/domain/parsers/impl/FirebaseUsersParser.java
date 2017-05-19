package com.opencharge.opencharge.domain.parsers.impl;

import com.opencharge.opencharge.domain.Entities.User;
import com.opencharge.opencharge.domain.parsers.UsersParser;

import java.util.Map;

/**
 * Created by DmnT on 18/05/2017.
 */

public class FirebaseUsersParser implements UsersParser {
    public static final double COORDINATES_PRECISION = 0.0001;

    public static final String TOWN_KEY = "town";
    public static final String STREET_KEY = "street";
    public static final String NUMBER_KEY = "number";
    public static final String LON_KEY = "lon";
    public static final String LAT_KEY = "lat";
    public static final String ACCESS_TYPE_KEY = "accessType";
    public static final String CONNECTOR_TYPE_KEY = "connectorType";
    public static final String SCHEDULE_KEY = "schedule";

    @Override
    public User parseFromMap(String key, Map<String, Object> map) {
        User user = new User();

        //TODO aquesta part, i la resta també, s'ha d'emplenar correctament quan es sàpiga com es guarden els usuaris al firebase
        /*
        point.setAccessType(parseAccessTypeFromMap(map));
        point.setConnectorType(parseConnectorTypeFromMap(map));

        point.setLat(parseDoubleKeyFromMap(LAT_KEY, map));
        point.setLon(parseDoubleKeyFromMap(LON_KEY, map));

        point.setTown(parseStringKeyFromMap(TOWN_KEY, map));
        point.setStreet(parseStringKeyFromMap(STREET_KEY, map));
        point.setNumber(parseStringKeyFromMap(NUMBER_KEY, map));

        point.setSchedule(parseStringKeyFromMap(SCHEDULE_KEY, map));
        */

        user.setUsername("Pepito");
        user.setEmail("pepito@dominiofalso.fake");
        user.setMinutes(42);

        return user;
    }

    /*

    private @Point.AccessType String parseAccessTypeFromMap(Map<String, Object> map) {
        @Point.AccessType String accessType = (String)map.get(ACCESS_TYPE_KEY);
        if (!isCorrectAccessType(accessType)) {
            accessType = Point.UNKNOWN_ACCESS;
        }

        return accessType;
    }

    private @Point.ConnectorType String parseConnectorTypeFromMap(Map<String, Object> map) {
        @Point.ConnectorType String connectorType = (String)map.get(CONNECTOR_TYPE_KEY);
        if (!isCorrectConnectorType(connectorType)) {
            connectorType = Point.UNKNOWN_CONNECTOR;
        }

        return connectorType;
    }

    private double parseDoubleKeyFromMap(String key, Map<String, Object> map) {
        double value = 0.0;
        if (map.containsKey(key)) {
            value = (double)map.get(key);
        }
        return value;
    }

    private String parseStringKeyFromMap(String key, Map<String, Object> map) {
        if (map.containsKey(key)) {
            return (String)map.get(key);
        }
        else {
            return null;
        }
    }

    private boolean isCorrectAccessType(String accessType) {
        String[] allowedTypes = new String[] {Point.PUBLIC_ACCESS, Point.PRIVATE_ACCESS, Point.PARTICULAR_ACCESS};
        return Arrays.asList(allowedTypes).contains(accessType);
    }

    private boolean isCorrectConnectorType(String connectorType) {
        String[] allowedTypes = new String[] {Point.SLOW_CONNECTOR, Point.FAST_CONNECTOR, Point.RAPID_CONNECTOR};
        return Arrays.asList(allowedTypes).contains(connectorType);
    }
    */
}
