package com.rcoem.filmrentalstore.helper;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class LocationHelper {

    // JTS requires a factory to build spatial objects
    private static final GeometryFactory geometryFactory = new GeometryFactory();

    public static Point createPoint(double latitude, double longitude) {
        // JTS Coordinates take (X, Y). On a map, Longitude is X and Latitude is Y.
        // You MUST pass longitude first, then latitude!
        Coordinate coordinate = new Coordinate(longitude, latitude);

        Point point = geometryFactory.createPoint(coordinate);

        // Your SQL script specifically noted: /*!80003 SRID 0 */
        // Setting the SRID (Spatial Reference System Identifier) ensures
        // Hibernate formats the SQL query exactly how MySQL expects it.
        point.setSRID(0);

        return point;
    }
}
